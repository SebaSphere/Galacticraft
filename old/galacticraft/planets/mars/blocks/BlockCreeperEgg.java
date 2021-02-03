package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.entities.EvolvedCreeperEntity;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DragonEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockCreeperEgg extends DragonEggBlock implements IShiftDescription, ISortable
{
    protected static final VoxelShape DRAGON_EGG_AABB = Shapes.box(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D);

    public BlockCreeperEgg(Properties builder)
    {
        super(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return DRAGON_EGG_AABB;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit)
    {
        return ActionResultType.PASS;
    }

    @Override
    public void attack(BlockState state, Level worldIn, BlockPos pos, Player player)
    {
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion)
    {
        if (!world.isClientSide)
        {
            EvolvedCreeperEntity creeper = new EvolvedCreeperEntity(GCEntities.EVOLVED_CREEPER, world);
            creeper.setPos(pos.getX() + 0.5, pos.getY() + 3, pos.getZ() + 0.5);
            creeper.setChild(true);
            world.addFreshEntity(creeper);
        }

        world.removeBlock(pos, false);
        this.wasExploded(world, pos, explosion);
    }

    @Override
    public boolean dropFromExplosion(Explosion explose)
    {
        return false;
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player)
    {
        ItemStack stack = player.inventory.getSelected();
        if (stack.isEmpty())
        {
            return player.canDestroy(world.getBlockState(pos));
        }
        return stack.getItem() == MarsItems.STICKY_DESH_PICKAXE;
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter worldIn, BlockPos pos)
    {
        ItemStack stack = player.inventory.getSelected();
        if (stack != ItemStack.EMPTY && stack.getItem() == MarsItems.STICKY_DESH_PICKAXE)
        {
            return 0.2F;
        }
        return super.getDestroyProgress(state, player, worldIn, pos);
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
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.EGG;
    }
}
