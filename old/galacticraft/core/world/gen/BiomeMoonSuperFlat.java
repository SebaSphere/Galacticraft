package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;

public class BiomeMoonSuperFlat extends BiomeMoon
{
    public static final BiomeMoonSuperFlat moonBiomeSuperFlat = new BiomeMoonSuperFlat();

    BiomeMoonSuperFlat()
    {
        super((new BiomeBuilder()).surfaceBuilder(SurfaceBuilder.DEFAULT, new SurfaceBuilderBaseConfiguration(GCBlocks.MOON_TURF.defaultBlockState(), GCBlocks.MOON_DIRT.defaultBlockState(), GCBlocks.MOON_DIRT.defaultBlockState())).precipitation(RainType.NONE).category(Category.NONE).depth(0.7F).scale(0.0F).temperature(0.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null), true);
        addDefaultFeatures();
    }

    @Override
    public float getCreatureProbability()
    {
        return 0.1F;
    }
}
