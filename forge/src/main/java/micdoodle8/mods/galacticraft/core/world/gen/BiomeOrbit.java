package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.core.entities.EvolvedSpiderEntity;
import micdoodle8.mods.galacticraft.core.entities.EvolvedZombieEntity;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.common.BiomeDictionary;

import static net.minecraft.world.gen.surfacebuilders.SurfaceBuilder.STONE_STONE_GRAVEL_CONFIG;

public class BiomeOrbit extends BiomeGC
{
    public static final Biome space = new BiomeOrbit();

    private BiomeOrbit()
    {
        super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.NOPE, STONE_STONE_GRAVEL_CONFIG).precipitation(Biome.RainType.NONE).category(Category.NONE).depth(0.0F).scale(0.0F).temperature(0.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null), true);
        addSpawn(EntityClassification.MONSTER, new SpawnListEntry(GCEntities.EVOLVED_ZOMBIE, 10, 4, 4));
        addSpawn(EntityClassification.MONSTER, new SpawnListEntry(GCEntities.EVOLVED_SPIDER, 10, 4, 4));
    }

    @Override
    public void registerTypes(Biome b)
    {
        BiomeDictionary.addTypes(b, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.SPOOKY);
    }

    @Override
    public float getSpawningChance()
    {
        return 0.01F;
    }
}
