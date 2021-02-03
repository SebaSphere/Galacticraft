package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidTank;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class BlockFluidTank extends Block implements IShiftDescription, ISortable
{
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    private static final VoxelShape BOUNDS = Shapes.box(0.05F, 0.0F, 0.05F, 0.95F, 1.0F, 0.95F);

    public BlockFluidTank(Properties builder)
    {
        super(builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(UP, false).setValue(DOWN, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return BOUNDS;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if (tile instanceof TileEntityFluidTank)
        {
            TileEntityFluidTank tank = (TileEntityFluidTank) tile;
//            tank.onBreak(); TODO Spill event needed?
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate(this.getDescriptionId() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(UP, DOWN);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        BlockState stateAbove = worldIn.getBlockState(currentPos.above());
        BlockState stateBelow = worldIn.getBlockState(currentPos.below());
        return stateIn.setValue(UP, stateAbove.getBlock() == this).setValue(DOWN, stateBelow.getBlock() == this);
    }

    //    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        BlockState stateAbove = worldIn.getBlockState(pos.up());
//        BlockState stateBelow = worldIn.getBlockState(pos.down());
//        return state.with(UP, stateAbove.getBlock() == this).with(DOWN, stateBelow.getBlock() == this);
//    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityFluidTank();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockReader worldIn, BlockPos pos)
//    {
//        this.setBlockBounds((float) BOUNDS.minX, (float) BOUNDS.minY, (float) BOUNDS.minZ, (float) BOUNDS.maxX, (float) BOUNDS.maxY, (float) BOUNDS.maxZ);
//    }
//
//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//    }
//
//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockReader worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getCollisionBoundingBox(worldIn, pos, state);
//    }
//
//    @Override
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getSelectedBoundingBox(worldIn, pos);
//    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit)
    {
        if (super.use(state, worldIn, pos, playerIn, hand, hit) == ActionResultType.SUCCESS)
        {
            return ActionResultType.SUCCESS;
        }

        if (hand == Hand.OFF_HAND)
        {
        	return ActionResultType.PASS;
        }

        ItemStack current = playerIn.inventory.getSelected();
        int slot = playerIn.inventory.selected;

        if (!current.isEmpty())
        {
            BlockEntity tile = worldIn.getBlockEntity(pos);

            if (tile instanceof TileEntityFluidTank)
            {
                TileEntityFluidTank tank = (TileEntityFluidTank) tile;

                LazyOptional<IFluidHandler> holder = tank.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
                if (holder.isPresent())
                {
                    FluidActionResult forgeResult = FluidUtil.interactWithFluidHandler(current, holder.orElse(null), playerIn);
                    if (forgeResult.isSuccess())
                    {
                        playerIn.inventory.setItem(slot, forgeResult.result);
                        if (playerIn.inventoryMenu != null)
                        {
                            playerIn.inventoryMenu.broadcastChanges();
                        }
                        return ActionResultType.SUCCESS;
                    }
                }

                return ActionResultType.PASS;
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public int getLightValue(BlockState state, BlockGetter world, BlockPos pos)
    {
        BlockEntity tile = world.getBlockEntity(pos);

        if (tile instanceof TileEntityFluidTank)
        {
            TileEntityFluidTank tank = (TileEntityFluidTank) tile;
            return tank.fluidTank.getFluid() == FluidStack.EMPTY || tank.fluidTank.getFluid().getAmount() == 0 ? 0 : tank.fluidTank.getFluid().getFluid().getAttributes().getLuminosity(tank.fluidTank.getFluid());
        }

        return 0;
    }
}
