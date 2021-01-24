package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.inventory.ContainerCrafting;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCrafting;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockCrafting extends BlockAdvancedTile implements IShiftDescription, ISortable
{
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockCrafting(Properties builder)
    {
        super(builder);
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit)
    {
        ItemStack heldItem = playerIn.getItemInHand(hand);

        if (this.useWrench(worldIn, pos, playerIn, hand, heldItem, hit) == ActionResultType.SUCCESS)
        {
            return ActionResultType.SUCCESS;
        }

        if (playerIn.isShiftKeyDown())
        {
            if (this.onSneakMachineActivated(worldIn, pos, playerIn, hand, heldItem, hit) == ActionResultType.SUCCESS)
            {
                return ActionResultType.SUCCESS;
            }
        }

        if (!worldIn.isClientSide)
        {
            NetworkHooks.openGui((ServerPlayer) playerIn, getMenuProvider(state, worldIn, pos), buf -> buf.writeBlockPos(pos));
        }

        return ActionResultType.PASS;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos)
    {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        return tileentity instanceof MenuProvider ? (MenuProvider)tileentity : null;
    }

    @Override
    public InteractionResult onUseWrench(Level world, BlockPos pos, Player entityPlayer, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        this.rotate6Ways(world, pos);
        return ActionResultType.SUCCESS;
    }

    private void rotate6Ways(Level world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        Direction facing = state.getValue(FACING);
        if (facing == Direction.DOWN)
        {
            facing = Direction.UP;
        }
        else if (facing == Direction.UP)
        {
            facing = Direction.NORTH;
        }
        else if (facing == Direction.WEST)
        {
            facing = Direction.DOWN;
        }
        else
        {
            facing = facing.getClockWise();
        }
//        int metadata = this.getMetaFromState(world.getBlockState(pos));
//        int metaDir = ((metadata & 7) + 1) % 6;
//        //DOWN->UP->NORTH->*EAST*->*SOUTH*->WEST
//        //0->1 1->2 2->5 3->4 4->0 5->3
//        if (metaDir == 3) //after north
//        {
//            metaDir = 5;
//        }
//        else if (metaDir == 0)
//        {
//            metaDir = 3;
//        }
//        else if (metaDir == 5)
//        {
//            metaDir = 0;
//        }

        world.setBlock(pos, state.setValue(FACING, facing), 3);
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityCrafting();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, getFacingFromEntity(context.getLevel(), context.getClickedPos(), context.getPlayer()));
    }

    public static Direction getFacingFromEntity(Level worldIn, BlockPos clickedBlock, LivingEntity entityIn)
    {
        if (Mth.abs((float) entityIn.getX() - (float) clickedBlock.getX()) < 3.0F && Mth.abs((float) entityIn.getZ() - (float) clickedBlock.getZ()) < 3.0F)
        {
            double d0 = entityIn.getY() + (double) entityIn.getEyeHeight();

            if (d0 - (double) clickedBlock.getY() > 2.0D)
            {
                return Direction.UP;
            }

            if ((double) clickedBlock.getY() - d0 > 1.0D)
            {
                return Direction.DOWN;
            }
        }

        return entityIn.getDirection().getOpposite();
    }

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

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(FACING, Direction.byIndex(meta));
//    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

//    @Override
//    public void dropEntireInventory(World worldIn, BlockPos pos, BlockState state)
//    {
//        super.dropEntireInventory(worldIn, pos, state);
//        TileEntity tileEntity = worldIn.getTileEntity(pos);
//        if (tileEntity instanceof TileEntityCrafting)
//        {
//            ((TileEntityCrafting)tileEntity).dropHiddenOutputBuffer(worldIn, pos);
//        }
//    } TODO
}
