package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenDistributor;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenSealer;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDistributor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockOxygenSealer extends BlockAdvancedTile implements IShiftDescription, ISortable
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockOxygenSealer(Properties builder)
    {
        super(builder);
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public ActionResultType onMachineActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        if (!worldIn.isRemote)
        {
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, getContainer(state, worldIn, pos), buf -> buf.writeBlockPos(pos));
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider)tileentity : null;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing());
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityOxygenSealer();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        BlockPos ventSide = pos.up(1);
        Block testBlock = worldIn.getBlockState(ventSide).getBlock();
        if (testBlock == GCBlocks.BREATHEABLE_AIR || testBlock == GCBlocks.BRIGHT_BREATHEABLE_AIR)
        {
            TickHandlerServer.scheduleNewEdgeCheck(GCCoreUtil.getDimensionType(worldIn), pos);
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.byHorizontalIndex(meta);
//        return this.getDefaultState().with(FACING, enumfacing);
//    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }
}
