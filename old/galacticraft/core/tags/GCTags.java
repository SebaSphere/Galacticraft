package micdoodle8.mods.galacticraft.core.tags;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class GCTags
{
    public static final Tag<Block> ALUMINUM_ORES = new BlockTags.Wrapper(new ResourceLocation("forge", "ores/aluminum"));
    public static final Tag<Block> CHEESE_ORES = new BlockTags.Wrapper(new ResourceLocation("forge", "ores/cheese"));
    public static final Tag<Block> COPPER_ORES = new BlockTags.Wrapper(new ResourceLocation("forge", "ores/copper"));
    public static final Tag<Block> SAPPHIRE_ORES = new BlockTags.Wrapper(new ResourceLocation("forge", "ores/sapphire"));
    public static final Tag<Block> SILICON_ORES = new BlockTags.Wrapper(new ResourceLocation("forge", "ores/silicon"));
    public static final Tag<Block> TIN_ORES = new BlockTags.Wrapper(new ResourceLocation("forge", "ores/tin"));

    public static final Tag<Block> ALUMINUM_STORAGE_BLOCKS = new BlockTags.Wrapper(new ResourceLocation("forge", "storage_blocks/aluminum"));
    public static final Tag<Block> COPPER_STORAGE_BLOCKS = new BlockTags.Wrapper(new ResourceLocation("forge", "storage_blocks/copper"));
    public static final Tag<Block> SILICON_STORAGE_BLOCKS = new BlockTags.Wrapper(new ResourceLocation("forge", "storage_blocks/silicon"));
    public static final Tag<Block> TIN_STORAGE_BLOCKS = new BlockTags.Wrapper(new ResourceLocation("forge", "storage_blocks/tin"));
    
    public static final Tag<Block> DESH_ORES = new BlockTags.Wrapper(new ResourceLocation("forge", "ores/desh"));
    public static final Tag<Block> ILMENITE_ORES = new BlockTags.Wrapper(new ResourceLocation("forge", "ores/ilmenite"));
    
    public static final Tag<Block> LEAD_STORAGE_BLOCKS = new BlockTags.Wrapper(new ResourceLocation("forge", "storage_blocks/lead"));
    public static final Tag<Block> DESH_STORAGE_BLOCKS = new BlockTags.Wrapper(new ResourceLocation("forge", "storage_blocks/desh"));

    public static final Tag<Item> ALUMINUM_ORES_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "ores/aluminum"));
    public static final Tag<Item> CHEESE_ORES_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "ores/cheese"));
    public static final Tag<Item> COPPER_ORES_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "ores/copper"));
    public static final Tag<Item> SAPPHIRE_ORES_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "ores/sapphire"));
    public static final Tag<Item> SILICON_ORES_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "ores/silicon"));
    public static final Tag<Item> TIN_ORES_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "ores/tin"));
    
    public static final Tag<Item> ALUMINUM_STORAGE_BLOCKS_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "storage_blocks/aluminum"));
    public static final Tag<Item> COPPER_STORAGE_BLOCKS_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "storage_blocks/copper"));
    public static final Tag<Item> SILICON_STORAGE_BLOCKS_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "storage_blocks/silicon"));
    public static final Tag<Item> TIN_STORAGE_BLOCKS_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "storage_blocks/tin"));

    public static final Tag<Item> ALUMINUM_INGOTS = new ItemTags.Wrapper(new ResourceLocation("forge", "ingots/aluminum"));
    public static final Tag<Item> COPPER_INGOTS = new ItemTags.Wrapper(new ResourceLocation("forge", "ingots/copper"));
    public static final Tag<Item> METEORIC_IRON_INGOTS = new ItemTags.Wrapper(new ResourceLocation("forge", "ingots/meteoric_iron"));
    public static final Tag<Item> TIN_INGOTS = new ItemTags.Wrapper(new ResourceLocation("forge", "ingots/tin"));
    
    public static final Tag<Item> DESH_ORES_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "ores/desh"));
    public static final Tag<Item> ILMENITE_ORES_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "ores/ilmenite"));
    
    public static final Tag<Item> LEAD_STORAGE_BLOCKS_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "storage_blocks/lead"));
    public static final Tag<Item> DESH_STORAGE_BLOCKS_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "storage_blocks/desh"));
    
    public static final Tag<Item> LEAD_INGOTS = new ItemTags.Wrapper(new ResourceLocation("forge", "ingots/lead"));
    public static final Tag<Item> DESH_INGOTS = new ItemTags.Wrapper(new ResourceLocation("forge", "ingots/desh"));

    public static final Tag<Item> PLATES = new ItemTags.Wrapper(new ResourceLocation("forge", "plates"));
    public static final Tag<Item> ALUMINUM_PLATES = new ItemTags.Wrapper(new ResourceLocation("forge", "plates/aluminum"));
    public static final Tag<Item> BRONZE_PLATES = new ItemTags.Wrapper(new ResourceLocation("forge", "plates/bronze"));
    public static final Tag<Item> COPPER_PLATES = new ItemTags.Wrapper(new ResourceLocation("forge", "plates/copper"));
    public static final Tag<Item> IRON_PLATES = new ItemTags.Wrapper(new ResourceLocation("forge", "plates/iron"));
    public static final Tag<Item> METEORIC_IRON_PLATES = new ItemTags.Wrapper(new ResourceLocation("forge", "plates/meteoric_iron"));
    public static final Tag<Item> STEEL_PLATES = new ItemTags.Wrapper(new ResourceLocation("forge", "plates/steel"));
    public static final Tag<Item> TIN_PLATES = new ItemTags.Wrapper(new ResourceLocation("forge", "plates/tin"));

    public static final Tag<Item> WAFERS = new ItemTags.Wrapper(new ResourceLocation("forge", "wafers"));
    public static final Tag<Item> ADVANCED_WAFERS = new ItemTags.Wrapper(new ResourceLocation("forge", "wafers/advanced"));
    public static final Tag<Item> BASIC_WAFERS = new ItemTags.Wrapper(new ResourceLocation("forge", "wafers/basic"));
    public static final Tag<Item> SOLAR_WAFERS = new ItemTags.Wrapper(new ResourceLocation("forge", "wafers/solar"));

    public static final Tag<Item> PARACHUTES = new ItemTags.Wrapper(new ResourceLocation(Constants.MOD_ID_CORE, "parachutes"));
}