package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicItem;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.HangingSchematicEntity;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.item.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.*;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import javax.annotation.Nullable;

public class ItemSchematic extends Item implements ISchematicItem, ISortable
{
    public ItemSchematic(Item.Properties builder)
    {
        super(builder);
//        super(EntityHangingSchematic.class);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setMaxStackSize(1);
//        this.setUnlocalizedName(assetName);
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            for (int i = 0; i < 2; i++)
//            {
//                list.add(new ItemStack(this, 1, i));
//            }
//        }
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

//    @Override
//    public int getMetadata(int par1)
//    {
//        return par1;
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if (stack.getItem() == GCItems.MOON_BUGGY_SCHEMATIC)
        {
            tooltip.add(new TextComponent(GCCoreUtil.translate("schematic.moonbuggy")));
        }
        else if (stack.getItem() == GCItems.TIER_2_ROCKET_SCHEMATIC)
        {
            tooltip.add(new TextComponent(GCCoreUtil.translate("schematic.rocket_t2")));

            if (!GalacticraftCore.isPlanetsLoaded)
            {
                tooltip.add(new TextComponent(EnumColor.DARK_AQUA + "\"Galacticraft: Planets\" Not Installed!"));
            }
        }
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.SCHEMATIC;
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
        BlockPos blockpos = context.getClickedPos().relative(context.getClickedFace());
        Direction facing = context.getClickedFace();

        if (facing != Direction.DOWN && facing != Direction.UP && context.getPlayer().mayUseItemAt(blockpos, facing, stack))
        {
            HangingSchematicEntity entityhanging = this.createEntity(context.getLevel(), blockpos, facing, this.getIndex(stack.getDamageValue()));

            if (entityhanging != null && entityhanging.survives())
            {
                if (!context.getLevel().isClientSide)
                {
                    entityhanging.playPlacementSound();
                    context.getLevel().addFreshEntity(entityhanging);
                    entityhanging.sendToClient(context.getLevel(), blockpos);
                }

                stack.shrink(1);
            }

            return ActionResultType.SUCCESS;
        }
        else
        {
            return ActionResultType.FAIL;
        }
    }

    private HangingSchematicEntity createEntity(Level worldIn, BlockPos pos, Direction clickedSide, int index)
    {
        return new HangingSchematicEntity(GCEntities.HANGING_SCHEMATIC, worldIn, pos, clickedSide, index);
    }

    /**
     * Higher tiers should override - see ItemSchematicTier2 for example
     **/
    protected int getIndex(int damage)
    {
        return damage;
    }

    /**
     * Make sure the number of these will match the index values
     */
    public static void registerSchematicItems()
    {
        SchematicRegistry.registerSchematicItem(new ItemStack(GCItems.MOON_BUGGY_SCHEMATIC, 1));
        SchematicRegistry.registerSchematicItem(new ItemStack(GCItems.TIER_2_ROCKET_SCHEMATIC, 1));
    }

    /**
     * Make sure the order of these will match the index values
     */
    @OnlyIn(Dist.CLIENT)
    public static void registerTextures()
    {
        SchematicRegistry.registerTexture(new ResourceLocation(Constants.MOD_ID_CORE, "textures/item/moon_buggy_schematic.png"));
        SchematicRegistry.registerTexture(new ResourceLocation(Constants.MOD_ID_CORE, "textures/item/tier_2_rocket_schematic.png"));
    }

    @Override
    public String getDescriptionId(ItemStack stack)
    {
        return "item.galacticraftcore.schematic";
    }
}
