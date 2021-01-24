package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockTier3TreasureChest;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import java.util.Random;

import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_TREASURE;

public class RoomTreasureVenus extends SizedPieceVenus
{
    public static ResourceLocation VENUSCHEST = new ResourceLocation(Constants.MOD_ID_CORE, "dungeon_tier_3");
//    public static final ResourceLocation TABLE_TIER_3_DUNGEON = LootTables.register(VENUSCHEST);

    public RoomTreasureVenus(StructureManager templateManager, CompoundTag nbt)
    {
        super(CVENUS_DUNGEON_TREASURE, nbt);
    }

    public RoomTreasureVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, Direction entranceDir)
    {
        this(configuration, rand, blockPosX, blockPosZ, rand.nextInt(4) + 6, configuration.getRoomHeight(), rand.nextInt(4) + 6, entranceDir);
    }

    public RoomTreasureVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(CVENUS_DUNGEON_TREASURE, configuration, sizeX, sizeY, sizeZ, entranceDir.getOpposite());
        this.setOrientation(Direction.SOUTH);
        int yPos = configuration.getYPosition();

        this.boundingBox = new BoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos + this.sizeY, blockPosZ + this.sizeZ);
    }

    @Override
    public boolean postProcess(LevelAccessor worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, BoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
    {
        for (int i = 0; i <= this.sizeX; i++)
        {
            for (int j = 0; j <= this.sizeY; j++)
            {
                for (int k = 0; k <= this.sizeZ; k++)
                {
                    if (i == 0 || i == this.sizeX || j == 0 || j == this.sizeY || k == 0 || k == this.sizeZ)
                    {
                        boolean placeBlock = true;
                        if (getDirection().getAxis() == Direction.Axis.Z)
                        {
                            int start = (this.boundingBox.x1 - this.boundingBox.x0) / 2 - 1;
                            int end = (this.boundingBox.x1 - this.boundingBox.x0) / 2 + 1;
                            if (i > start && i <= end && j < 3 && j > 0)
                            {
                                if (getDirection() == Direction.SOUTH && k == 0)
                                {
                                    placeBlock = false;
                                }
                                else if (getDirection() == Direction.NORTH && k == this.sizeZ)
                                {
                                    placeBlock = false;
                                }
                            }
                        }
                        else
                        {
                            int start = (this.boundingBox.z1 - this.boundingBox.z0) / 2 - 1;
                            int end = (this.boundingBox.z1 - this.boundingBox.z0) / 2 + 1;
                            if (k > start && k <= end && j < 3 && j > 0)
                            {
                                if (getDirection() == Direction.EAST && i == 0)
                                {
                                    placeBlock = false;
                                }
                                else if (getDirection() == Direction.WEST && i == this.sizeX)
                                {
                                    placeBlock = false;
                                }
                            }
                        }
                        if (placeBlock)
                        {
                            DungeonConfigurationVenus venusConfig = this.configuration;
                            this.placeBlock(worldIn, j == 0 || j == this.sizeY ? venusConfig.getBrickBlockFloor() : this.configuration.getBrickBlock(), i, j, k, boundingBox);
                        }
                        else
                        {
                            this.placeBlock(worldIn, Blocks.AIR.defaultBlockState(), i, j, k, boundingBox);
                        }
                    }
                    else if ((i == 1 && k == 1) || (i == 1 && k == this.sizeZ - 1) || (i == this.sizeX - 1 && k == 1) || (i == this.sizeX - 1 && k == this.sizeZ - 1))
                    {
                        this.placeBlock(worldIn, Blocks.GLOWSTONE.defaultBlockState(), i, j, k, boundingBox);
                    }
                    else if (i == this.sizeX / 2 && j == 1 && k == this.sizeZ / 2)
                    {
                        BlockPos blockpos = new BlockPos(this.getWorldX(i, k), this.getWorldY(j), this.getWorldZ(i, k));
                        if (boundingBox.isInside(blockpos))
                        {
                            worldIn.setBlock(blockpos, VenusBlocks.TIER_3_TREASURE_CHEST.defaultBlockState().setValue(BlockTier3TreasureChest.FACING, this.getDirection().getOpposite()), 2);
                            TileEntityTreasureChest treasureChest = (TileEntityTreasureChest) worldIn.getBlockEntity(blockpos);
                            if (treasureChest != null)
                            {
//                                ResourceLocation chesttype = TABLE_TIER_3_DUNGEON;
//                                if (worldIn.dimension instanceof IGalacticraftDimension)
//                                {
//                                    chesttype = ((IGalacticraftDimension)worldIn.dimension).getDungeonChestType();
//                                } TODO Loot tables
//                                treasureChest.setLootTable(chesttype, random.nextLong());
                            }
                        }
                    }
                    else
                    {
                        this.placeBlock(worldIn, Blocks.AIR.defaultBlockState(), i, j, k, boundingBox);
                    }
                }
            }
        }

        return true;
    }

    @Override
    public PieceVenus getNextPiece(DungeonStartVenus startPiece, Random rand)
    {
        if (startPiece.attachedComponents.size() > 2)
        {
            StructurePiece component = startPiece.attachedComponents.get(startPiece.attachedComponents.size() - 3);
            if (component instanceof RoomBossVenus)
            {
                BlockPos blockpos = new BlockPos(this.getWorldX(this.sizeX / 2, this.sizeZ / 2), this.getWorldY(1), this.getWorldZ(this.sizeX / 2, this.sizeZ / 2));
                ((RoomBossVenus) component).setChestPos(new BlockPos(blockpos));
            }
        }
        return null;
    }
}