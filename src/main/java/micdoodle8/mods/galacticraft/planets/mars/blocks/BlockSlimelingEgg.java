package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntitySlimelingEgg;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSlimelingEgg extends Block implements IShiftDescription, ISortable
{
    //    private IIcon[] icons;
    public static final BooleanProperty CRACKED = BooleanProperty.create("cracked");
    protected static final VoxelShape AABB = VoxelShapes.create(0.25, 0.0, 0.25, 0.75, 0.625, 0.75);

    public BlockSlimelingEgg(Properties builder)
    {
        super(builder);
        this.setDefaultState(stateContainer.getBaseState().with(CRACKED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return AABB;
    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

//    @Override
//    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
//    {
//        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
//    }

//    @Override
//    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
//    {
//        Block block = par1World.getBlock(par2, par3 - 1, par4);
//        return block.isSideSolid(par1World, par2, par3, par4, ForgeDirection.UP);
//    }

    private ActionResultType beginHatch(World world, BlockPos pos, PlayerEntity player, int time)
    {
        BlockState state = world.getBlockState(pos);

        if (!state.get(CRACKED))
        {
            world.setBlockState(pos, state.with(CRACKED, true), 2);

            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof TileEntitySlimelingEgg)
            {
                ((TileEntitySlimelingEgg) tile).timeToHatch = world.rand.nextInt(30 + time) + time;
                ((TileEntitySlimelingEgg) tile).lastTouchedPlayerUUID = player.getUniqueID().toString();
            }

            world.markBlockRangeForRenderUpdate(pos, this.getDefaultState(), state); // Forces block render update. Better way to do this?

            return ActionResultType.SUCCESS;
        }
        else
        {
            world.markBlockRangeForRenderUpdate(pos, this.getDefaultState(), state); // Forces block render update. Better way to do this?
            return ActionResultType.PASS;
        }
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid)
    {
        ItemStack currentStack = player.getHeldItemMainhand();
        if (currentStack != ItemStack.EMPTY && currentStack.getItem() instanceof PickaxeItem)
        {
            return world.removeBlock(pos, false);
        }
        else if (player.abilities.isCreativeMode)
        {
            return world.removeBlock(pos, false);
        }
        else
        {
            beginHatch(world, pos, player, 0);
            return false;
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
    {
        return beginHatch(worldIn, pos, playerIn, 20);
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, @Nullable ItemStack currentStack)
    {
        if (!currentStack.isEmpty() && currentStack.getItem() instanceof PickaxeItem)
        {
            player.addStat(Stats.BLOCK_MINED.get(this));
            player.addExhaustion(0.025F);
            spawnAsEntity(worldIn, pos, getItem(worldIn, pos, state));
//            this.dropBlockAsItem(worldIn, pos, state.getBlock().getStateFromMeta(state.getBlock().getMetaFromState(state) % 3), 0);
            if (currentStack.getItem() == MarsItems.DESH_PICKAXE && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, currentStack) > 0)
            {
                ItemStack itemstack = new ItemStack(MarsItems.STICKY_DESH_PICKAXE, 1/*, currentStack.getDamage()*/);
                itemstack.setDamage(currentStack.getDamage());
                if (currentStack.getTag() != null)
                {
                    itemstack.setTag(currentStack.getTag().copy());
                }
                player.inventory.setInventorySlotContents(player.inventory.currentItem, itemstack);
            }
        }
    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public IIcon getIcon(int LogicalSide, int metadata)
    {
        return this.icons[metadata % 6];
    }*/

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
//    public Item getItemDropped(int meta, Random random, int par3)
//    {
//        return Item.getItemFromBlock(this);
//    }

//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return getMetaFromState(state);
//    }

//    @Override
//    public int quantityDropped(int meta, int fortune, Random random)
//    {
//        return 1;
//    }

//    @Override
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        for (int var4 = 0; var4 < EnumEggColor.values().length; ++var4)
//        {
//            list.add(new ItemStack(this, 1, var4));
//        }
//    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntitySlimelingEgg();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
//        int metadata = state.getBlock().getMetaFromState(state);
//        EnumEggColor color = state.get(BlockSlimelingEgg.EGG_COLOR);
//
//        if (color)
//        {
//            return new ItemStack(Item.getItemFromBlock(this), 1, 0);
//        }
//        if (metadata == 4)
//        {
//            return new ItemStack(Item.getItemFromBlock(this), 1, 1);
//        }
//        if (metadata == 5)
//        {
//            return new ItemStack(Item.getItemFromBlock(this), 1, 2);
//        }
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate("block.galacticraftplanets.slimeling_egg.description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(EGG_COLOR, EnumEggColor.values()[meta % 3]).with(BROKEN, meta - 3 >= 0);
//    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(CRACKED);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.EGG;
    }
}
