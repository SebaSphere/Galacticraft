package micdoodle8.mods.galacticraft.core.items;

import Rarity;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemHoeGC extends HoeItem implements ISortable
{
    public ItemHoeGC(Item.Properties builder)
    {
        super(EnumItemTierGC.STEEL, 0.0F, builder);
//        this.setUnlocalizedName(assetName);
        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.TOOLS;
    }
}
