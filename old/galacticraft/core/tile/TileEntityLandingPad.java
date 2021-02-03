package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.entity.ICargoEntity;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.entity.IFuelable;
import micdoodle8.mods.galacticraft.api.entity.ILandable;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.core.GCBlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashSet;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class TileEntityLandingPad extends TileEntityFake implements IMultiBlock, IFuelable, IFuelDock, ICargoEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.FULL_ROCKET_LAUNCH_PAD)
    public static BlockEntityType<TileEntityLandingPad> TYPE;

    public TileEntityLandingPad()
    {
        super(TYPE);
    }

    private IDockable dockedEntity;
    private boolean initialised;

    @Override
    public void tick()
    {
        if (!this.initialised)
        {
            if (!this.level.isClientSide)
            {
                this.onCreate(this.level, this.getBlockPos());
            }
            this.initialiseMultiTiles(this.getBlockPos(), this.level);
            this.initialised = true;
        }

        if (!this.level.isClientSide)
        {
            List<Entity> list = this.level.getEntitiesOfClass(Entity.class, new AABB(this.getBlockPos().getX() - 0.5D, this.getBlockPos().getY(), this.getBlockPos().getZ() - 0.5D, this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 1.0D, this.getBlockPos().getZ() + 0.5D));

            boolean docked = false;

            for (Entity entity : list)
            {
                if (entity instanceof IDockable && entity.isAlive())
                {
                    IDockable fuelable = (IDockable) entity;

                    if (!fuelable.inFlight())
                    {
                        docked = true;

                        if (fuelable != this.dockedEntity && fuelable.isDockValid(this))
                        {
                            if (fuelable instanceof ILandable)
                            {
                                ((ILandable) fuelable).landEntity(this.getBlockPos());
                            }
                            else
                            {
                                fuelable.setPad(this);
                            }
                        }

                        break;
                    }
                }
            }

            if (!docked)
            {
                this.dockedEntity = null;
            }
        }
    }

    @Override
    public InteractionResult onActivated(Player entityPlayer)
    {
        return ActionResultType.PASS;
    }

    @Override
    public void onCreate(Level world, BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.setChanged();

        List<BlockPos> positions = Lists.newArrayList();
        this.getPositions(placedPosition, positions);
        ((BlockMulti) GCBlocks.MULTI_BLOCK).makeFakeBlock(world, positions, placedPosition, this.getMultiType());
    }

    @Override
    public BlockMulti.EnumBlockMultiType getMultiType()
    {
        return EnumBlockMultiType.ROCKET_PAD;
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int y = placedPosition.getY();
        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                if (x == 0 && z == 0)
                {
                    continue;
                }
                positions.add(new BlockPos(placedPosition.getX() + x, y, placedPosition.getZ() + z));
            }
        }
    }

    @Override
    public void onDestroy(BlockEntity callingBlock)
    {
        BlockPos thisBlock = getBlockPos();
        List<BlockPos> positions = Lists.newArrayList();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            BlockState stateAt = this.level.getBlockState(pos);

            if (stateAt.getBlock() == GCBlocks.MULTI_BLOCK && stateAt.getValue(BlockMulti.MULTI_TYPE) == EnumBlockMultiType.ROCKET_PAD)
            {
                if (this.level.isClientSide && this.level.random.nextDouble() < 0.1D)
                {
                    Minecraft.getInstance().particleEngine.destroy(pos, this.level.getBlockState(pos));
                }
                this.level.destroyBlock(pos, false);
            }
        }
        this.level.destroyBlock(thisBlock, true);

        if (this.dockedEntity != null)
        {
            this.dockedEntity.onPadDestroyed();
            this.dockedEntity = null;
        }

    }

    @Override
    public int addFuel(FluidStack liquid, IFluidHandler.FluidAction action)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.addFuel(liquid, action);
        }

        return 0;
    }

    @Override
    public FluidStack removeFuel(int amount)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.removeFuel(amount);
        }

        return null;
    }

    @Override
    public HashSet<ILandingPadAttachable> getConnectedTiles()
    {
        HashSet<ILandingPadAttachable> connectedTiles = Sets.newHashSet();

        for (int x = this.getBlockPos().getX() - 1; x < this.getBlockPos().getX() + 2; x++)
        {
            this.testConnectedTile(x, this.getBlockPos().getZ() - 2, connectedTiles);
            this.testConnectedTile(x, this.getBlockPos().getZ() + 2, connectedTiles);
        }

        for (int z = this.getBlockPos().getZ() - 1; z < this.getBlockPos().getZ() + 2; z++)
        {
            this.testConnectedTile(this.getBlockPos().getX() - 2, z, connectedTiles);
            this.testConnectedTile(this.getBlockPos().getX() + 2, z, connectedTiles);
        }

        return connectedTiles;
    }

    private void testConnectedTile(int x, int z, HashSet<ILandingPadAttachable> connectedTiles)
    {
        BlockPos testPos = new BlockPos(x, this.getBlockPos().getY(), z);
        if (!this.level.hasChunkAt(testPos))
        {
            return;
        }

        BlockEntity tile = this.level.getBlockEntity(testPos);

        if (tile instanceof ILandingPadAttachable && ((ILandingPadAttachable) tile).canAttachToLandingPad(this.level, this.getBlockPos()))
        {
            connectedTiles.add((ILandingPadAttachable) tile);
            if (GalacticraftCore.isPlanetsLoaded && tile instanceof TileEntityLaunchController)
            {
                ((TileEntityLaunchController) tile).setAttachedPad(this);
            }
        }
    }

    @Override
    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.addCargo(stack, doAdd);
        }

        return EnumCargoLoadingState.NOTARGET;
    }

    @Override
    public RemovalResult removeCargo(boolean doRemove)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.removeCargo(doRemove);
        }

        return new RemovalResult(EnumCargoLoadingState.NOTARGET, ItemStack.EMPTY);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox()
    {
        return new AABB(getBlockPos().getX() - 1, getBlockPos().getY(), getBlockPos().getZ() - 1, getBlockPos().getX() + 2, getBlockPos().getY() + 0.4D, getBlockPos().getZ() + 2);
    }

    @Override
    public boolean isBlockAttachable(LevelReader world, BlockPos pos)
    {
        BlockEntity tile = world.getBlockEntity(pos);

        if (tile != null && tile instanceof ILandingPadAttachable)
        {
            return ((ILandingPadAttachable) tile).canAttachToLandingPad(world, this.getBlockPos());
        }

        return false;
    }

    @Override
    public IDockable getDockedEntity()
    {
        return this.dockedEntity;
    }

    @Override
    public void dockEntity(IDockable entity)
    {
        this.dockedEntity = entity;
    }
}
