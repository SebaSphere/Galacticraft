package micdoodle8.mods.galacticraft.planets.venus.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.block.Blocks;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;

public class BlockTorchWeb extends Block implements IShearable, IShiftDescription, ISortable
{
    protected static final VoxelShape AABB_WEB = VoxelShapes.create(0.35, 0.0, 0.35, 0.65, 1.0, 0.65);
    protected static final VoxelShape AABB_WEB_TORCH = VoxelShapes.create(0.35, 0.25, 0.35, 0.65, 1.0, 0.65);

    public BlockTorchWeb(Properties builder)
    {
        super(builder);
    }

//    @Override
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        list.add(new ItemStack(this, 1, 0));
//        list.add(new ItemStack(this, 1, 1));
//    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        if (state.getBlock() == VenusBlocks.WEB_TORCH)
        {
            return AABB_WEB_TORCH;
        }

        return AABB_WEB;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        if (state.getBlock() == VenusBlocks.WEB_TORCH)
        {
            return 15;
        }

        return 0;
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IBlockAccess worldIn, BlockPos pos)
//    {
//        return null;
//    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        if (world.getBlockState(pos).isReplaceable(context) && this.canBlockStay(world, pos))
        {
            return super.getStateForPlacement(context);
        }
        return world.getBlockState(pos);
    }

    private boolean canBlockStay(World world, BlockPos pos)
    {
        BlockState blockUp = world.getBlockState(pos.up());
        return blockUp.getMaterial().isSolid() || blockUp.getBlock() == VenusBlocks.WEB_STRING;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        this.checkAndDropBlock(worldIn, pos);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
    {
        this.checkAndDropBlock(worldIn, pos);
    }

    protected void checkAndDropBlock(World world, BlockPos pos)
    {
        if (!this.canBlockStay(world, pos))
        {
            world.destroyBlock(pos, true);
        }
    }

//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        return Item.getItemFromBlock(Blocks.AIR);
//    }

//    @Override
//    public int quantityDropped(Random rand)
//    {
//        return 0;
//    }

    @Override
    public boolean isShearable(ItemStack item, IWorldReader world, BlockPos pos)
    {
        return true;
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IWorld world, BlockPos pos, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this, 1));
        return ret;
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return BlockItem.BLOCK_TO_ITEM.get(VenusBlocks.WEB_TORCH) == stack.getItem();
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public BlockRenderLayer getRenderLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(WEB_TYPE, EnumWebType.byMetadata(meta));
//    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}
