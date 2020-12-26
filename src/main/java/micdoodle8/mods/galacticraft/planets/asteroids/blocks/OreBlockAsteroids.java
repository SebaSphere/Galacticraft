package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.storage.loot.LootContext;

import java.util.List;

public class OreBlockAsteroids extends Block implements IDetectableResource, ISortable
{
    public OreBlockAsteroids(Properties properties)
    {
        super(properties);
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch)
    {
        if (this == AsteroidBlocks.ILMENITE_ORE)
        {
            MathHelper.nextInt(RANDOM, 2, 3);
        }

        return super.getExpDrop(state, world, pos, fortune, silktouch);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
    {
//        if (this == AsteroidBlocks.oreIlmenite)
//        {
//            ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
//
//            int count = quantityDropped(state, fortune, ((World) world).rand);
//            for (int i = 0; i < count; i++)
//            {
//                ret.add(new ItemStack(AsteroidsItems.basicItem, 1, 3));
//            }
//            count = quantityDropped(state, fortune, ((World) world).rand);
//            for (int i = 0; i < count; i++)
//            {
//                ret.add(new ItemStack(AsteroidsItems.basicItem, 1, 4));
//            }
//            return ret;
//        } TODO Loot

        return super.getDrops(state, builder);
    }

    @Override
    public boolean isValueable(BlockState metadata)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.ORE;
    }
}
