package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.core.entities.EvolvedSpiderEntity;
import micdoodle8.mods.galacticraft.core.entities.EvolvedZombieEntity;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.common.BiomeDictionary;

import static net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder.CONFIG_STONE;

public class BiomeOrbit extends BiomeGC
{
    public static final Biome space = new BiomeOrbit();

    private BiomeOrbit()
    {
        super((new Biome.BiomeBuilder()).surfaceBuilder(SurfaceBuilder.NOPE, CONFIG_STONE).precipitation(Biome.RainType.NONE).category(Category.NONE).depth(0.0F).scale(0.0F).temperature(0.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null), true);
        addSpawn(EntityClassification.MONSTER, new SpawnerData(GCEntities.EVOLVED_ZOMBIE, 10, 4, 4));
        addSpawn(EntityClassification.MONSTER, new SpawnerData(GCEntities.EVOLVED_SPIDER, 10, 4, 4));
    }

    @Override
    public void registerTypes(Biome b)
    {
        BiomeDictionary.addTypes(b, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.SPOOKY);
    }

    @Override
    public float getCreatureProbability()
    {
        return 0.01F;
    }
}
