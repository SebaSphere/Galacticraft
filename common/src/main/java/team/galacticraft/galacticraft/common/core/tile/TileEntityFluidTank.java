package team.galacticraft.galacticraft.common.core.tile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.network.NetworkUtil;
import team.galacticraft.galacticraft.common.core.network.PacketDynamic;
import team.galacticraft.galacticraft.common.core.util.DelayTimer;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import team.galacticraft.galacticraft.common.core.wrappers.FluidHandlerWrapper;
import team.galacticraft.galacticraft.common.core.wrappers.IFluidHandlerWrapper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
import net.minecraftforge.fml.network.PacketDistributor;
//import net.minecraftforge.registries.ObjectHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class TileEntityFluidTank extends TileEntityAdvanced implements IFluidHandlerWrapper
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.fluidTank)
    public static BlockEntityType<TileEntityFluidTank> TYPE;

    public FluidTankGC fluidTank = new FluidTankGC(16000, this);
    public boolean updateClient = false;
    private final DelayTimer delayTimer = new DelayTimer(1);
    private AABB renderAABB;

    public TileEntityFluidTank()
    {
        super(TYPE);
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

//    public void onBreak()
//    {
//        if (fluidTank.getFluidAmount() > 0)
//        {
//            FluidEvent.fireEvent(new FluidEvent.FluidSpilledEvent(fluidTank.getFluid(), world, pos));
//            if (!this.world.isRemote && fluidTank.getFluidAmount() > 1000)
//            {
//                Block b = fluidTank.getFluid().getFluid().getBlock();
//                if (!(b == null || b instanceof AirBlock))
//                {
//                	TickHandlerServer.scheduleNewBlockChange(GCCoreUtil.getDimensionID(this.world), new ScheduledBlockChange(pos, b.getStateFromMeta(0), 3));
//                }
//            }
//        }
//    }

    @Override
    public void tick()
    {
        super.tick();

        if (fluidTank.getFluid() != FluidStack.EMPTY)
        {
            moveFluidDown();
        }

        if (!this.level.isClientSide && updateClient && delayTimer.markTimeIfDelay(this.level))
        {
            PacketDynamic packet = new PacketDynamic(this);
            GalacticraftCore.packetPipeline.sendToAllAround(packet, new PacketDistributor.TargetPoint(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), this.getPacketRange(), GCCoreUtil.getDimensionType(this.level)));
            this.updateClient = false;
        }
    }

    @Override
    public int fill(Direction from, FluidStack resource, ActionType action)
    {
        if (resource == null)
        {
            return 0;
        }

        FluidStack copy = resource.copy();
        int totalUsed = 0;
        TileEntityFluidTank toFill = getLastTank();

        FluidStack fluid = toFill.fluidTank.getFluid();
        if (!fluid.isEmpty() && fluid.getAmount() > 0 && !fluid.isFluidEqual(copy))
        {
            return 0;
        }

        while (toFill != null && copy.getAmount() > 0)
        {
            int used = toFill.fluidTank.fill(copy, action);
            if (used > 0)
            {
                toFill.updateClient = true;
            }
            copy.setAmount(copy.getAmount() - used);
            totalUsed += used;
            toFill = getNextTank(toFill.getBlockPos());
        }

        return totalUsed;
    }

    @Override
    public FluidStack drain(Direction from, FluidStack resource, ActionType action)
    {
        if (resource == null)
        {
            return null;
        }
        TileEntityFluidTank last = getLastTank();
        if (!resource.isFluidEqual(last.fluidTank.getFluid()))
        {
            return null;
        }
        return drain(from, resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, ActionType action)
    {
        TileEntityFluidTank last = getLastTank();
        FluidStack stack = last.fluidTank.drain(maxDrain, action);
        if (action.execute() && !stack.isEmpty() && stack.getAmount() > 0)
        {
            last.updateClient = true;
        }
        return stack;
    }

    @Override
    public boolean canFill(Direction from, Fluid fluid)
    {
        return fluidTank.getFluid() == FluidStack.EMPTY || fluidTank.getFluid() == FluidStack.EMPTY || fluid == null || fluidTank.getFluid().getFluid() == fluid;
    }

    @Override
    public boolean canDrain(Direction from, Fluid fluid)
    {
        return fluid == null || fluidTank.getFluid() != FluidStack.EMPTY && fluidTank.getFluid().getFluid() == fluid;
    }

    @Override
    public int getTanks()
    {
        return 1;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        TileEntityFluidTank last = getLastTank();

        if (last != null)
        {
            return last.fluidTank.getFluid();
        }
        else
        {
            return FluidStack.EMPTY;
        }
    }

    @Override
    public int getTankCapacity(int tank)
    {
        if (tank != 0)
        {
            return 0;
        }

        TileEntityFluidTank last = getLastTank();

        int capacity = last.fluidTank.getCapacity();
        last = getNextTank(last.getBlockPos());

        while (last != null)
        {
            capacity += last.fluidTank.getCapacity();
            last = getNextTank(last.getBlockPos());
        }

        return capacity;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack)
    {
        if (tank != 0)
        {
            return false;
        }

        TileEntityFluidTank last = getLastTank();

        if (last != null)
        {
            if (last.fluidTank.getFluid() != FluidStack.EMPTY)
            {
                return last.fluidTank.getFluid().getFluid() == stack.getFluid();
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    //    @Override
//    public FluidTankInfo[] getTankInfo(Direction from)
//    {
//        FluidTank compositeTank = new FluidTank(fluidTank.getCapacity());
//        TileEntityFluidTank last = getLastTank();
//
//        if (last != null && last.fluidTank.getFluid() != FluidStack.EMPTY)
//        {
//            compositeTank.setFluid(last.fluidTank.getFluid().copy());
//        }
//        else
//        {
//            return new FluidTankInfo[] { compositeTank.getInfo() };
//        }
//
//        int capacity = last.fluidTank.getCapacity();
//        last = getNextTank(last.getPos());
//
//        while (last != null)
//        {
//            FluidStack fluid = last.fluidTank.getFluid();
//            if (fluid == null || fluid.amount == 0)
//            {
//
//            }
//            else if (!compositeTank.getFluid().isFluidEqual(fluid))
//            {
//                break;
//            }
//            else
//            {
//                compositeTank.getFluid().getAmount() += fluid.amount;
//            }
//
//            capacity += last.fluidTank.getCapacity();
//            last = getNextTank(last.getPos());
//        }
//
//        compositeTank.setCapacity(capacity);
//        return new FluidTankInfo[] { compositeTank.getInfo() };
//    }

    public void moveFluidDown()
    {
        TileEntityFluidTank next = getPreviousTank(this.getBlockPos());
        if (next != null)
        {
            int used = next.fluidTank.fill(fluidTank.getFluid(), ActionType.EXECUTE);

            if (used > 0)
            {
                this.updateClient = true;
                next.updateClient = true;
                fluidTank.drain(used, ActionType.EXECUTE);
            }
        }
    }

    public TileEntityFluidTank getLastTank()
    {
        TileEntityFluidTank lastTank = this;

        while (true)
        {
            TileEntityFluidTank tank = getPreviousTank(lastTank.getBlockPos());
            if (tank != null)
            {
                lastTank = tank;
            }
            else
            {
                break;
            }
        }

        return lastTank;
    }

    public TileEntityFluidTank getNextTank(BlockPos current)
    {
        BlockEntity above = this.level.getBlockEntity(current.above());
        if (above instanceof TileEntityFluidTank)
        {
            return (TileEntityFluidTank) above;
        }
        return null;
    }

    public TileEntityFluidTank getPreviousTank(BlockPos current)
    {
        BlockEntity below = this.level.getBlockEntity(current.below());
        if (below instanceof TileEntityFluidTank)
        {
            return (TileEntityFluidTank) below;
        }
        return null;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);

        if (nbt.contains("fuelTank"))
        {
            this.fluidTank.readFromNBT(nbt.getCompound("fuelTank"));
        }

        this.updateClient = GCCoreUtil.getEffectiveSide() == EnvType.SERVER;
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);

        if (this.fluidTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("fuelTank", this.fluidTank.writeToNBT(new CompoundTag()));
        }

        return nbt;
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("net-type", "desc-packet");
        PacketDynamic packet = new PacketDynamic(this);
        ByteBuf buf = Unpooled.buffer();
        packet.encodeInto(buf);
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        nbt.putByteArray("net-data", bytes);
        ClientboundBlockEntityDataPacket tileUpdate = new ClientboundBlockEntityDataPacket(getBlockPos(), 0, nbt);
        return tileUpdate;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        if (!this.level.isClientSide)
        {
            return;
        }
        if (pkt.getTag() == null)
        {
            throw new RuntimeException("[GC] Missing NBTTag compound!");
        }
        CompoundTag nbt = pkt.getTag();
        try
        {
            if ("desc-packet".equals(nbt.getString("net-type")))
            {
                byte[] bytes = nbt.getByteArray("net-data");
                ByteBuf data = Unpooled.wrappedBuffer(bytes);
                PacketDynamic packet = new PacketDynamic();
                packet.decodeInto(data);
                packet.handleClientSide(Minecraft.getInstance().player);
            }
        }
        catch (Throwable t)
        {
            throw new RuntimeException("[GC] Failed to read a packet! (" + nbt.get("net-type") + ", " + nbt.get("net-data"), t);
        }
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList)
    {
        if (!this.level.isClientSide)
        {
            if (fluidTank == null)
            {
                networkedList.add(0);
                networkedList.add("");
                networkedList.add(0);
            }
            else
            {
                networkedList.add(fluidTank.getCapacity());
                networkedList.add(fluidTank.getFluid() == FluidStack.EMPTY ? "" : fluidTank.getFluid().getFluid().getRegistryName().toString());
                networkedList.add(fluidTank.getFluidAmount());
            }
        }
    }

    @Override
    public void readExtraNetworkedData(ByteBuf buffer)
    {
        if (this.level.isClientSide)
        {
            int capacity = buffer.readInt();
            String fluidName = NetworkUtil.readUTF8String(buffer);
            FluidTankGC fluidTank = new FluidTankGC(capacity, this);
            int amount = buffer.readInt();

            if (fluidName.equals(""))
            {
                fluidTank.setFluid(FluidStack.EMPTY);
            }
            else
            {
                Fluid fluid = Registry.FLUID.get(new ResourceLocation(fluidName));
//                Fluid fluid = FluidRegistry.getFluid(fluidName);
                fluidTank.setFluid(new FluidStack(fluid, amount));
            }

            this.fluidTank = fluidTank;
        }
    }

    @Override
    public double getPacketRange()
    {
        return 40;
    }

    @Override
    public int getPacketCooldown()
    {
        return 1;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return false;
    }

    private LazyOptional<IFluidHandler> holder = null;

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            if (holder == null)
            {
                holder = LazyOptional.of(new NonNullSupplier<IFluidHandler>()
                {
                    @NotNull
                    @Override
                    public IFluidHandler get()
                    {
                        return new FluidHandlerWrapper(TileEntityFluidTank.this, facing);
                    }
                });
            }
            return holder.cast();
        }
        return super.getCapability(capability, facing);
    }

//    @Override
//    public boolean hasCapability(Capability<?> capability, @Nullable Direction facing)
//    {
//        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
//    }
//
//    @Nullable
//    @Override
//    public <T> T getCapability(Capability<T> capability, @Nullable Direction facing)
//    {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
//        {
//            return (T) new FluidHandlerWrapper(this, facing);
//        }
//        return super.getCapability(capability, facing);
//    }

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
        return Constants.RENDERDISTANCE_MEDIUM;
    }
}