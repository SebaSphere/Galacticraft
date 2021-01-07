package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import IStartFactory;
import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.world.gen.GCFeatures;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class StructureDungeon extends Structure<DungeonConfiguration>
{
//    private DungeonConfiguration configuration;

//    static
//    {
//        try
//        {
//            MapGenDungeon.initiateStructures();
//        }
//        catch (Throwable e)
//        {
//
//        }
//    }

    public StructureDungeon(Function<Dynamic<?>, ? extends DungeonConfiguration> func)
    {
        super(func);
    }

    @Override
    public String getStructureName()
    {
        return Constants.MOD_ID_CORE + ":gc_dungeon";
    }

    @Override
    public IStartFactory getStartFactory()
    {
        return StructureDungeon.Start::new;
    }

    @Override
    public int getSize()
    {
        return 12;
    }

    @Override
    public boolean canBeGenerated(BiomeManager biomeManagerIn, ChunkGenerator<?> generatorIn, Random randIn, int chunkX, int chunkZ, Biome biomeIn)
    {
        long dungeonPos = getDungeonPosForCoords(generatorIn, chunkX, chunkZ, ((IGalacticraftDimension) generatorIn.world.getDimension()).getDungeonSpacing());
        int i = (int) (dungeonPos >> 32);
        int j = (int) dungeonPos;  //Java automatically gives the 32 least significant bits
        return i == chunkX && j == chunkZ;
    }

    public static long getDungeonPosForCoords(ChunkGenerator<?> generator, int chunkX, int chunkZ, int spacing)
    {
        final int numChunks = spacing / 16;
        if (chunkX < 0)
        {
            chunkX -= numChunks - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= numChunks - 1;
        }

        int k = chunkX / numChunks;
        int l = chunkZ / numChunks;
        long seed = (long) k * 341873128712L + (long) l * 132897987541L + generator.world.getWorldInfo().getSeed() + (long) (10387340 + generator.world.getDimension().getType().getId());
        Random random = new Random();
        random.setSeed(seed);
        k = k * numChunks + random.nextInt(numChunks);
        l = l * numChunks + random.nextInt(numChunks);
        return (((long) k) << 32) + l;
    }

    /**
     * This returns an angle between 0 and 360 degrees.  0 degrees means due North from the current (x, z) position
     * Only provides meaningful results in worlds with dungeon generation using this class!
     */
    public static float directionToNearestDungeon(ServerWorld world, double xpos, double zpos)
    {
        int spacing = ((IGalacticraftDimension) world.getDimension()).getDungeonSpacing();
        if (spacing == 0)
        {
            return 0F;
        }
        int x = MathHelper.floor(xpos);
        int z = MathHelper.floor(zpos);
        int quadrantX = x % spacing;
        int quadrantZ = z % spacing;
        int searchOffsetX = quadrantX / (spacing / 2);  //0 or 1
        int searchOffsetZ = quadrantZ / (spacing / 2);  //0 or 1
        double nearestX = 0;
        double nearestZ = 0;
        double nearestDistance = Double.MAX_VALUE;
        for (int cx = searchOffsetX - 1; cx < searchOffsetX + 1; cx++)
        {
            for (int cz = searchOffsetZ - 1; cz < searchOffsetZ + 1; cz++)
            {
                long dungeonPos = getDungeonPosForCoords(world.getChunkProvider().getChunkGenerator(), (x + cx * spacing) / 16, (z + cz * spacing) / 16, spacing);
                int i = 2 + (((int) (dungeonPos >> 32)) << 4);
                int j = 2 + (((int) dungeonPos) << 4);  //Java automatically gives the 32 least significant bits
                double oX = i - xpos;
                double oZ = j - zpos;
                double distanceSq = oX * oX + oZ * oZ;
                if (distanceSq < nearestDistance)
                {
                    nearestDistance = distanceSq;
                    nearestX = oX;
                    nearestZ = oZ;
                }
            }
        }

        return GCCoreUtil.getAngleForRelativePosition(nearestX, nearestZ);
    }

//    @Override
//    protected StructureStart getStructureStart(int chunkX, int chunkZ)
//    {
//        return new MapGenDungeon.Start(this.world, this.rand, chunkX, chunkZ, this.configuration);
//    }

    @Nullable
    @Override
    public BlockPos findNearest(World worldIn, ChunkGenerator<? extends GenerationSettings> chunkGenerator, BlockPos pos, int radius, boolean p_211405_5_)
    {
        return null;
    }

    public static class Start extends StructureStart
    {
        //        private DungeonConfiguration configuration;
        DungeonStart startPiece;

        public Start(Structure<?> structure, int chunkX, int chunkZ, MutableBoundingBox boundsIn, int referenceIn, long seed)
        {
            super(structure, chunkX, chunkZ, boundsIn, referenceIn, seed);
//            this.configuration = configuration;
        }

        @Override
        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
        {
            DungeonConfiguration dungeonConfig = generator.getStructureConfig(biomeIn, GCFeatures.MOON_DUNGEON);
            startPiece = new DungeonStart((World) generator.world, dungeonConfig, rand, (chunkX << 4) + 2, (chunkZ << 4) + 2);
            startPiece.buildComponent(startPiece, this.components, rand);
            List<StructurePiece> list = startPiece.attachedComponents;

            while (!list.isEmpty())
            {
                int i = rand.nextInt(list.size());
                StructurePiece structurecomponent = list.remove(i);
                structurecomponent.buildComponent(startPiece, this.components, rand);
            }

            this.recalculateStructureSize();
        }

        public boolean isValid() {
            return true;
        }
    }

    public static void main(String[] args)
    {
//        Random rand = new Random();
//        Start start = new Start(null, rand, 0, 0, new DungeonConfiguration(null, 25, 8, 16, 5, 6, RoomBoss.class, RoomTreasure.class));

//        EventQueue.invokeLater(() ->
//        {
//            try
//            {
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            }
//            catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
//            {
//                ex.printStackTrace();
//            }
//
//            JFrame frame = new JFrame("Dungeon Test");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.add(new DungeonGenPanel(start.startPiece.componentBounds));
//            frame.pack();
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
    }

    public static class DungeonGenPanel extends JPanel
    {
        DungeonGenPanel(List<MutableBoundingBox> componentBounds)
        {
            int absMinX = Integer.MAX_VALUE;
            int absMinZ = Integer.MAX_VALUE;
            int absMaxX = Integer.MIN_VALUE;
            int absMaxZ = Integer.MIN_VALUE;
            for (MutableBoundingBox b : componentBounds)
            {
                if (b.minX < absMinX)
                {
                    absMinX = b.minX;
                }
                if (b.minZ < absMinZ)
                {
                    absMinZ = b.minZ;
                }
                if (b.maxX > absMaxX)
                {
                    absMaxX = b.maxX;
                }
                if (b.maxZ > absMaxZ)
                {
                    absMaxZ = b.maxZ;
                }
            }
            setLayout(new GridLayout(absMaxX - absMinX, absMaxZ - absMinZ, 0, 0));

            Color[] colors = new Color[]{Color.GREEN, Color.BLUE, Color.RED, Color.MAGENTA};
            List<List<JPanel>> cells = Lists.newArrayList();
            for (int row = 0; row < absMaxX - absMinX; row++)
            {
                List<JPanel> rowCells = Lists.newArrayList();
                for (int col = 0; col < absMaxZ - absMinZ; col++)
                {
                    JPanel cell = new JPanel()
                    {
                        @Override
                        public Dimension getPreferredSize()
                        {
                            return new Dimension(8, 8);
                        }
                    };
                    cell.setBackground(Color.GRAY);
                    add(cell);
                    rowCells.add(cell);
                }
                cells.add(rowCells);
            }

            int color = 0;
            // X is rows, Z is cols
            for (MutableBoundingBox bb : componentBounds)
            {
                color = ++color % colors.length;

                for (int i = bb.minX; i < bb.maxX; ++i)
                {
                    for (int j = bb.minZ; j < bb.maxZ; ++j)
                    {
                        cells.get(i - absMinX).get(j - absMinZ).setBackground(colors[color]);
                    }
                }
            }
        }
    }

}
