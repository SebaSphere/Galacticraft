package team.galacticraft.galacticraft.common.core.world.gen;

import team.galacticraft.galacticraft.common.api.world.BiomeGC;
import team.galacticraft.galacticraft.core.GCBlocks;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;

public class BiomeMoonSuperFlat extends BiomeMoon
{
    public static final BiomeMoonSuperFlat moonBiomeSuperFlat = new BiomeMoonSuperFlat();

    BiomeMoonSuperFlat()
    {
        super((new BiomeBuilder()).surfaceBuilder(SurfaceBuilder.DEFAULT, new SurfaceBuilderBaseConfiguration(GCBlocks.moonTurf.defaultBlockState(), GCBlocks.moonDirt.defaultBlockState(), GCBlocks.moonDirt.defaultBlockState())).precipitation(Precipitation.NONE).biomeCategory(BiomeCategory.NONE).depth(0.7F).scale(0.0F).temperature(0.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null), true);
        addDefaultFeatures();
    }

    @Override
    public float getCreatureProbability()
    {
        return 0.1F;
    }
}
