package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.entities.EvolvedCreeperEntity;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.DragonEggBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockCreeperEgg extends DragonEggBlock implements IShiftDescription, ISortable
{
    protected static final VoxelShape DRAGON_EGG_AABB = VoxelShapes.create(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D);

    public BlockCreeperEgg(Properties builder)
    {
        super(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return DRAGON_EGG_AABB;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
    {
        return ActionResultType.PASS;
    }

    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player)
    {
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion)
    {
        if (!world.isRemote)
        {
            EvolvedCreeperEntity creeper = new EvolvedCreeperEntity(GCEntities.EVOLVED_CREEPER, world);
            creeper.setPosition(pos.getX() + 0.5, pos.getY() + 3, pos.getZ() + 0.5);
            creeper.setChild(true);
            world.addEntity(creeper);
        }

        world.removeBlock(pos, false);
        this.onExplosionDestroy(world, pos, explosion);
    }

    @Override
    public boolean canDropFromExplosion(Explosion explose)
    {
        return false;
    }

    @Override
    public boolean canHarvestBlock(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack.isEmpty())
        {
            return player.canHarvestBlock(world.getBlockState(pos));
        }
        return stack.getItem() == MarsItems.STICKY_DESH_PICKAXE;
    }

    @Override
    public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos)
    {
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack != ItemStack.EMPTY && stack.getItem() == MarsItems.STICKY_DESH_PICKAXE)
        {
            return 0.2F;
        }
        return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
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

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.EGG;
    }
}
