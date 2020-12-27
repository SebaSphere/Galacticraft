package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBase extends Item implements ISortable
{
    float smeltingXP = -1F;

    public ItemBase(Item.Properties properties)
    {
        super(properties);
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
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (stack != null && this == GCItems.TIER_1_HEAVY_DUTY_PLATE)
        {
            tooltip.add(new StringTextComponent(GCCoreUtil.translate("item.tier1.desc")));
        }
        else if (stack != null && this == GCItems.DUNGEON_FINDER)
        {
            tooltip.add(new StringTextComponent(EnumColor.RED + GCCoreUtil.translate("gui.creative_only.desc")));
        }
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }

    public Item setSmeltingXP(float f)
    {
        this.smeltingXP = f;
        return this;
    }

    @Override
    public float getSmeltingExperience(ItemStack item)
    {
        return this.smeltingXP;
    }
}
