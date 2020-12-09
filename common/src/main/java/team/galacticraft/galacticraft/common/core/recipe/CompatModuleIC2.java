package team.galacticraft.galacticraft.common.core.recipe;
//
//import ic2.api.item.IC2Items;
//import ic2.api.recipe.Recipes;
//import team.galacticraft.galacticraft.core.GCBlocks;
//import team.galacticraft.galacticraft.core.GCItems;
//import team.galacticraft.galacticraft.core.blocks.BlockEnclosed;
//import team.galacticraft.galacticraft.core.util.RecipeUtil;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.crafting.FurnaceRecipes;
//import net.minecraft.nbt.CompoundNBT;
//
//public class CompatModuleIC2
//{
//    public static void addIndustrialCraft2Recipes()
//    {
//        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCItems.ic2compat, 1, 1), new ItemStack(GCItems.basicItem, 1, 5), 1.0F);
//        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(GCItems.ic2compat, 1, 2), new ItemStack(GCItems.basicItem, 1, 5), 1.0F);
//        Recipes.macerator.addRecipe(Recipes.inputFactory.forStack(new ItemStack(GCBlocks.basicBlock, 1, 7), 1), null, false, new ItemStack(GCItems.ic2compat, 2, 2));
//        Recipes.macerator.addRecipe(Recipes.inputFactory.forStack(new ItemStack(GCItems.basicItem, 1, 5), 1), null, false, new ItemStack(GCItems.ic2compat, 1, 0));
//        Recipes.macerator.addRecipe(Recipes.inputFactory.forStack(new ItemStack(GCItems.basicItem, 1, 8), 1), null, false, new ItemStack(GCItems.ic2compat, 1, 0));
//        ItemStack dustSmallIron = IC2Items.getItem("dust", "small_iron").copy();
//        ItemStack dustStone = IC2Items.getItem("dust", "stone").copy();
//        CompoundNBT amountTag = new CompoundNBT();
//        amounttag.putInt("amount", 1000);
//        ItemStack dustSmallTitanium = new ItemStack(GCItems.ic2compat, 1, 7);
//        CompoundNBT heatTag1 = new CompoundNBT();
//        heatTag1.setInteger("minHeat", 2000);
//        Recipes.centrifuge.addRecipe(Recipes.inputFactory.forStack(new ItemStack(GCItems.ic2compat, 1, 1), 1), heatTag1, false, new ItemStack [] { dustSmallTitanium, new ItemStack(GCItems.ic2compat, 1, 0) });
//        CompoundNBT heatTag2 = new CompoundNBT();
//        heatTag2.setInteger("minHeat", 750);
//        Recipes.centrifuge.addRecipe(Recipes.inputFactory.forStack(new ItemStack(GCItems.ic2compat, 1, 2), 1), heatTag1, false, new ItemStack [] { dustSmallIron, new ItemStack(GCItems.ic2compat, 1, 0), dustStone });
//        dustSmallIron = dustSmallIron.copy();
//        dustSmallIron.setCount(2);
//        Recipes.oreWashing.addRecipe(Recipes.inputFactory.forStack(new ItemStack(GCItems.ic2compat, 1, 2), 1), amountTag, false, new ItemStack [] { new ItemStack(GCItems.ic2compat, 1, 1), dustSmallIron, dustStone });
//
//        RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.IC2_COPPER_CABLE.getMeta()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("cable", "type:copper,insulation:0"), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
//        RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.IC2_GOLD_CABLE.getMeta()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("cable", "type:gold,insulation:1"), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
//        RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.IC2_HV_CABLE.getMeta()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("cable", "type:iron,insulation:1"), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
//        RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.IC2_GLASS_FIBRE_CABLE.getMeta()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("cable", "type:glass,insulation:0"), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
//        RecipeUtil.addRecipe(new ItemStack(GCBlocks.sealableBlock, 1, BlockEnclosed.EnumEnclosedBlockType.IC2_LV_CABLE.getMeta()), new Object[] { "XYX", 'Y', RecipeUtil.getIndustrialCraftItem("cable", "type:tin,insulation:1"), 'X', new ItemStack(GCBlocks.basicBlock, 1, 4) });
//    }
//}
