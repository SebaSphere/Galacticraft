package team.galacticraft.galacticraft.common.core.tile;

import me.shedaniel.architectury.utils.Fraction;
import team.galacticraft.galacticraft.common.api.tile.IColorable;
import team.galacticraft.galacticraft.common.api.transmission.NetworkType;
import team.galacticraft.galacticraft.common.api.transmission.grid.IGridNetwork;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.common.core.GCBlocks;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.blocks.BlockFluidPipe;
import team.galacticraft.galacticraft.common.core.fluid.FluidNetwork;
import team.galacticraft.galacticraft.common.core.network.PacketSimple;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;
import me.shedaniel.architectury.fluid.FluidStack;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
//import net.minecraftforge.registries.ObjectHolder;

import org.jetbrains.annotations.NotNull;

public class TileEntityFluidPipe extends TileEntityFluidTransmitter implements IColorable
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.fluidPipe)
    public static BlockEntityType<TileEntityFluidPipe> TYPE;

    public FluidTankGC buffer = new FluidTankGC(1000, this);
    private final boolean dataRequest = false;
    private AABB renderAABB;

    public TileEntityFluidPipe()
    {
        super(TYPE, 100);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState)
//    {
//        // Do not re-create tile entity if only the pipe's color changed!
//        if (oldState != newState)
//        {
//            return oldState.getBlock() != newState.getBlock();
//        }
//
//        return super.shouldRefresh(world, pos, oldState, newState);
//    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        BlockEntity adjacentTile = new BlockVec3(this).getTileEntityOnSide(this.level, direction);

        if (type == NetworkType.FLUID)
        {
            if (adjacentTile instanceof IColorable)
            {
                BlockState state = this.level.getBlockState(this.getBlockPos());
                BlockState adjacentTileState = adjacentTile.getLevel().getBlockState(adjacentTile.getBlockPos());
                byte thisCol = this.getColor(state);
                byte otherCol = ((IColorable) adjacentTile).getColor(adjacentTileState);
                return thisCol == otherCol || thisCol == DyeColor.WHITE.getId() || otherCol == DyeColor.WHITE.getId();
            }

            return true;
        }

        return false;
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return this.world == null || !this.world.isRemote;
//
//    }

    @Override
    public double getPacketRange()
    {
        return 12.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 5;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return false;
    }

    @Override
    public void onLoad()
    {
        if (this.level.isClientSide)
        {
//            this.world.notifyLightSet(getPos());
            level.getChunkSource().getLightEngine().checkBlock(getBlockPos());
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_DATA, GCCoreUtil.getDimensionType(this.level), new Object[]{GCCoreUtil.getDimensionType(this.level), this.getBlockPos()}));
        }
    }

    @Override
    public void onColorUpdate()
    {
        if (this.level != null)
        {
            if (this.level.isClientSide)
            {
//                this.world.notifyLightSet(getPos());
                level.getChunkSource().getLightEngine().checkBlock(getBlockPos());
            }
            else
            {
                this.getNetwork().split(this);
                this.resetNetwork();
            }
        }
    }

    @Override
    public byte getColor(BlockState state)
    {
        if (state.getBlock() instanceof BlockFluidPipe)
        {
            return (byte) state.getValue(BlockFluidPipe.COLOR).getId();
        }
        return 15;
    }

    @Override
    public void onAdjacentColorChanged(Direction direction)
    {
        BlockState state = this.level.getBlockState(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), state, state, 3);

        if (!this.level.isClientSide)
        {
            this.refresh();
        }
    }

    @Override
    public CompoundTag save(CompoundTag tagCompound)
    {
        super.save(tagCompound);
        if (this.buffer.getFluidStack() != FluidStack.empty())
        {
            tagCompound.put("buff", this.buffer.writeToNBT(new CompoundTag()));
        }
        return tagCompound;
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }

    @Override
    public void load(CompoundTag tagCompound)
    {
        super.load(tagCompound);

        if (tagCompound.contains("pipeColor"))
        {
            // Backwards compatibility
            this.level.setBlockAndUpdate(getBlockPos(), this.level.getBlockState(getBlockPos()).setValue(BlockFluidPipe.COLOR, DyeColor.byId(tagCompound.getByte("pipeColor"))));
        }

        if (tagCompound.contains("buff"))
        {
            this.buffer.readFromNBT(tagCompound.getCompound("buff"));
        }
    }

    @Override
    public FluidStack getBuffer()
    {
        return buffer.getFluidStack().isEmpty() ? null : buffer.getFluidStack();
    }

    @Override
    public int getCapacity()
    {
        return 200;
    }

    @Override
    public FluidStack fill(Direction from, FluidStack resource, ActionType action)
    {
        IGridNetwork network = this.getNetwork();
        if (network instanceof FluidNetwork)
        {
            FluidNetwork fluidNetwork = (FluidNetwork) network;
            return fluidNetwork.emitToBuffer(resource, action);
        }
        else
        {
            return this.buffer.fill(resource, action);
        }
    }

    @Override
    public FluidStack drain(Direction from, FluidStack resource, ActionType action)
    {
        return null;
    }

    @Override
    public FluidStack drain(Direction from, Fraction maxDrain, ActionType action)
    {
        return null;
    }

    @Override
    public boolean canFill(Direction from, Fluid fluid)
    {
        return true;
    }

    @Override
    public boolean canDrain(Direction from, Fluid fluid)
    {
        return false;
    }

//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from)
//    {
//        return new FluidTankInfo[0];
//    }

    @Override
    public int getTanks()
    {
        return 0;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return FluidStack.empty();
    }

    @Override
    public Fraction getTankCapacity(int tank)
    {
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack)
    {
        return false;
    }

    public boolean switchType()
    {
        if (this.ticks < 10)
        {
            return false;
        }

        Block block;
        Block currentType = this.getBlockState().getBlock();

        if (!(currentType instanceof BlockFluidPipe))
        {
            return false;
        }

        switch (((BlockFluidPipe) currentType).getMode())
        {
        case NORMAL:
            block = GCBlocks.fluidPipePull;
            break;
        default:
            block = GCBlocks.fluidPipe;
            break;
        }

        BlockFluidPipe.ignoreDrop = true;
        this.level.setBlockAndUpdate(worldPosition, block.defaultBlockState());
        BlockFluidPipe.ignoreDrop = false;
        if (this.hasNetwork())
        {
            this.refresh();
            this.getNetwork().refresh();
        }

        return true;
    }

    @Override
    public boolean canTransmit()
    {
        Block currentType = this.getBlockState().getBlock();

        if (!(currentType instanceof BlockFluidPipe))
        {
            // Walkway blocks, etc
            return true;
        }

        return ((BlockFluidPipe) currentType).getMode() != BlockFluidPipe.EnumPipeMode.PULL;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public AABB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AABB(worldPosition, worldPosition.offset(1, 1, 1));
        }
        return this.renderAABB;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public double getViewDistance()
    {
        return 16384;  //128 squared
    }

//    @Override
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public int receiveGas(Direction side, GasStack stack, boolean doTransfer)
//    {
//        String mekGas = stack.getGas().getName();
//        if (mekGas == null)
//        {
//            return 0;
//        }
//        FluidStack mekEquivalent = null;
//        if (mekGas.equals("oxygen"))
//        {
//            mekEquivalent = FluidStack.create(GCFluidRegistry.fluidOxygenGas, stack.amount);
//        }
//        else if (mekGas.equals("hydrogen"))
//        {
//            mekEquivalent = FluidStack.create(GCFluidRegistry.fluidHydrogenGas, stack.amount);
//        }
//        else
//        {
//            return 0;
//        }
//        return this.fill(EnvType, mekEquivalent, doTransfer);
//    }
//
//    @Override
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public int receiveGas(Direction side, GasStack stack)
//    {
//        return this.receiveGas(EnvType, stack, true);
//    }
//
//    @Override
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public GasStack drawGas(Direction side, int amount, boolean doTransfer)
//    {
//        return null;
//    }
//
//    @Override
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public GasStack drawGas(Direction side, int amount)
//    {
//        return null;
//    }
//
//    @Override
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public boolean canReceiveGas(Direction side, Gas type)
//    {
//        return type.getName().equals("oxygen") || type.getName().equals("hydrogen");
//    }
//
//    @Override
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public boolean canDrawGas(Direction side, Gas type)
//    {
//        return false;
//    }
//
//    @Override
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = CompatibilityManager.modidMekanism)
//    public boolean canTubeConnect(Direction side)
//    {
//        return false;
//    }

//    @Override
//    public boolean hasCapability(Capability<?> capability, Direction facing)
//    {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
//            return true;
//
//    	if (EnergyUtil.checkMekGasHandler(capability))
//    		return true;
//
//    	return super.hasCapability(capability, facing);
//    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing)
    {
//    	if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
//    	{
//    		return (T) new FluidHandlerWrapper(this, facing);
//    	}

//    	if (EnergyUtil.checkMekGasHandler(capability))
//    	{
//    		return (T) this;
//    	}

        return super.getCapability(capability, facing);
    }
}
