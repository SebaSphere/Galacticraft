package micdoodle8.mods.galacticraft.planets.mars.items;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemArmorMars extends ArmorItem implements ISortable
{
//    private final ArmorMaterial material;

    public ItemArmorMars(EquipmentSlot slotType, Item.Properties properties)
    {
        super(EnumArmorMars.ARMOR_DESH, slotType, properties);
//        this.material = par2EnumArmorMaterial;
    }

    /*@Override
    public Item setUnlocalizedName(String par1Str)
    {
//        super.setTextureName(par1Str);
        super.setUnlocalizedName(par1Str);
        return this;
    }*/

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type)
    {
        if (this.getMaterial() == EnumArmorMars.ARMOR_DESH)
        {
            if (stack.getItem() == MarsItems.DESH_HELMET)
            {
                return GalacticraftPlanets.TEXTURE_PREFIX + "textures/models/armor/desh_layer_1.png";
            }
            else if (stack.getItem() == MarsItems.DESH_CHESTPLATE || stack.getItem() == MarsItems.DESH_BOOTS)
            {
                return GalacticraftPlanets.TEXTURE_PREFIX + "textures/models/armor/desh_layer_2.png";
            }
            else if (stack.getItem() == MarsItems.DESH_LEGGINGS)
            {
                return GalacticraftPlanets.TEXTURE_PREFIX + "textures/models/armor/desh_layer_3.png";
            }
        }

        return null;
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack stack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.ARMOR;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair)
    {
        return repair.getItem() == MarsItems.DESH_INGOT;
    }
}
