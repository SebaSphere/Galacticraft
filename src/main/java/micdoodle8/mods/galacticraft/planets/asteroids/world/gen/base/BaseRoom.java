package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.entities.EntityHangingSchematic;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.items.ItemOilCanister;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCargoLoader;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCrafting;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidTank;
import micdoodle8.mods.galacticraft.planets.PlanetFluids;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.BaseDeck.EnumBaseType;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.block.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potions;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.state.properties.Half;
import net.minecraft.tileentity.BrewingStandTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static micdoodle8.mods.galacticraft.planets.asteroids.world.gen.AsteroidFeatures.CBASE_ROOM;

public class BaseRoom extends SizedPiece
{
    private EnumRoomType type;
    private boolean nearEnd;
    private boolean farEnd;
    private int deckTier;

    public BaseRoom(TemplateManager templateManager, CompoundNBT nbt)
    {
        super(CBASE_ROOM, nbt);
    }

    public BaseRoom(BaseConfiguration configuration, Random rand, int blockPosX, int yPos, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir, EnumRoomType roomType, boolean near, boolean far, int deckTier)
    {
        super(CBASE_ROOM, configuration, sizeX, sizeY, sizeZ, entranceDir.getOpposite());
        this.setCoordBaseMode(this.direction);
        this.type = roomType;
        this.nearEnd = near;
        this.farEnd = far;
        this.deckTier = deckTier;

        this.boundingBox = new MutableBoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos + this.sizeY, blockPosZ + this.sizeZ);
    }

    @Override
    protected void writeStructureToNBT(CompoundNBT tagCompound)
    {
        super.writeStructureToNBT(tagCompound);

        int details = this.deckTier + (this.nearEnd ? 16 : 0) + (this.farEnd ? 32 : 0);
        tagCompound.putInt("brT", type.ordinal());
        tagCompound.putInt("brD", details);
    }

    @Override
    protected void readStructureFromNBT(CompoundNBT tagCompound)
    {
        super.readStructureFromNBT(tagCompound);
        try
        {
            int typeNo = tagCompound.getInt("brT");
            if (typeNo < EnumRoomType.values().length)
            {
                this.type = EnumRoomType.values()[typeNo];
            }
            else
            {
                this.type = EnumRoomType.EMPTY;
            }

            int details = tagCompound.getInt("brD");
            this.deckTier = details & 15;
            this.nearEnd = (details & 16) == 16;
            this.farEnd = (details & 32) == 32;
        }
        catch (Exception e)
        {
            System.err.println("Failed to read Abandoned Base configuration from NBT");
            System.err.println(tagCompound.toString());
        }
    }

    @Override
    public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
    {
        BlockState blockAir = Blocks.AIR.getDefaultState();
//        Block blockStair = GCBlocks.moonStoneStairs;

        boolean axisEW = getDirection().getAxis() == Direction.Axis.X;
        int maxX = axisEW ? this.sizeZ : this.sizeX;
        int maxZ = axisEW ? this.sizeX : this.sizeZ;
        int randomInt = randomIn.nextInt(99);
        for (int xx = 0; xx <= maxX; xx++)
        {
            boolean near = this.nearEnd && xx == maxX;
            boolean far = this.farEnd && xx == 0;
            for (int yy = 0; yy <= this.sizeY; yy++)
            {
                for (int zz = 0; zz <= maxZ; zz++)
                {
                    //5 outer walls of the room
                    if (xx == 0 || xx == maxX || yy == 0 || yy == this.sizeY || zz == maxZ)
                    {
                        boolean xEntrance = maxX > 6 ? (xx > 2 && xx < maxX - 2) : (xx > 1 && xx < maxX - 1);
                        if (this.type.blockEntrance != null && yy == 0 && zz == 0 && xEntrance && this.configuration.getDeckType() != EnumBaseType.TUNNELER)
                        {
                            this.setBlockState(worldIn, this.type.blockEntrance, xx, yy, zz, mutableBoundingBoxIn);
                            this.setBlockState(worldIn, this.configuration.getWallBlock(), xx, yy - 1, zz, mutableBoundingBoxIn);
                        }
                        //Shave the top and bottom corners
                        else if (!((zz == maxZ || near || far) && (yy == 0 && (this.deckTier & 1) == 1 || yy == this.sizeY && (this.deckTier & 2) == 2 || (zz == maxZ && (near || far)))) || zz == 0 && yy == 0)
                        {
                            this.setBlockState(worldIn, this.configuration.getWallBlock(), xx, yy, zz, mutableBoundingBoxIn);
                        }
                        //Special case, fill in some corners on hangardeck top deck
                        else if (yy == this.sizeY && (this.deckTier & 2) == 2 && this.configuration.isHangarDeck() && zz < 3)
                        {
                            if (xx == 0 || xx == maxX)
                            {
                                this.setBlockState(worldIn, this.configuration.getWallBlock(), xx, yy, zz, mutableBoundingBoxIn);
                            }
                        }
                    }
                    else
                    {
                        //Room internals
                        if ((xx > 1 && xx < maxX - 1) && (zz > 0 && zz < maxZ - 1) || (yy > 1 && yy < this.sizeY - 1) || this.type.doEntryWallsToo)
                        {
                            BlockPos blockpos = new BlockPos(this.getXWithOffset(xx, zz), this.getYWithOffset(yy), this.getZWithOffset(xx, zz));
                            if (mutableBoundingBoxIn.isVecInside(blockpos))
                            {
                                this.buildRoomContents(worldIn, xx, yy, zz, maxX - 1, maxZ - 1, blockpos, randomInt);
                            }
                        }
                        else if (this.configuration.getDeckType() == EnumBaseType.TUNNELER && (yy == 1 || yy == this.sizeY - 1))
                        {
                            int meta = 1;
                            if (xx == 1)
                            {
                                meta = 3;
                            }
                            else if (xx == maxX - 1)
                            {
                                meta = 2;
                            }
                            else if (zz == 0)
                            {
                                meta = 0;
                            }
                            if (this.direction == Direction.NORTH)
                            {
                                meta ^= 3 ^ meta / 2;
                            }
                            else if (this.direction == Direction.SOUTH)
                            {
                                meta ^= 2 + meta / 2;
                            }
                            else if (this.direction == Direction.EAST)
                            {
                                meta ^= 1;
                            }
                            meta += (yy == 1) ? 0 : 4;
//                            this.setBlockState(worldIn, blockStair.getStateFromMeta(meta), xx, yy, zz, mutableBoundingBoxIn); TODO Stairs
                        }
                        else if (yy == 1 && this.type.blockFloor != null)
                        {
                            boolean xEntrance = maxX > 6 ? (xx > 2 && xx < maxX - 2) : (xx > 1 && xx < maxX - 1);
                            if (zz == 0 && (this.type.blockEntrance != null && xEntrance))
                            {
                                this.setBlockState(worldIn, blockAir, xx, yy, zz, mutableBoundingBoxIn);
                            }
                            else
                            {
                                this.setBlockState(worldIn, this.type.blockFloor, xx, yy, zz, mutableBoundingBoxIn);
                            }
                        }
                        else
                        {
                            this.setBlockState(worldIn, blockAir, xx, yy, zz, mutableBoundingBoxIn);
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Room contents boundaries are:
     * x from 1 to maxX
     * y from 1 to this.sizeY - 1
     * z from 1 to maxZ
     */
    private void buildRoomContents(IWorld worldIn, int x, int y, int z, int maxX, int maxZ, BlockPos blockpos, int randomInt)
    {
        BlockState state = Blocks.AIR.getDefaultState();
        int semirand = ((blockpos.getY() * 379 + blockpos.getX()) * 373 + blockpos.getZ()) * 7 & 15;

        int facing = 0;
        int facing1 = 0;
        int facing2 = 0;
        int facingLamp = 2;
        int facingScreen = 2;
        switch (this.direction)
        {
        case WEST:
            facing1 = 3;
            facing2 = 2;
            facingLamp = 3;
            facingScreen = 5;
            break;
        case EAST:
            facing = 2;
            facing1 = 1;
            facing2 = 0;
            facingLamp = 2;
            facingScreen = 4;
            break;
        case NORTH:
            facing = 3;
            facing1 = 2;
            facing2 = 1;
            facingLamp = 5;
            facingScreen = 2;
            break;
        case SOUTH:
            facing = 1;
            facing1 = 0;
            facing2 = 3;
            facingLamp = 4;
            facingScreen = 3;
        }

        //Offset from centre - used for some rooms
        int ox = maxX / 2 - 3;
        int xx = x - ox;

//        switch (this.type)
//        {
//        case EMPTY:
//            //Pillars in corners
//            if ((z == 1 || z == maxZ - 1) && (x == 2 || x == maxX - 1))
//            {
//                state = GCBlocks.wallGC.getStateFromMeta(2);
//            }
//            else if (y == 1)
//            {
//                //Some random Netherwart
//                if (semirand < 2)
//                {
//                    state = AsteroidBlocks.spaceWart.getStateFromMeta(semirand);
//                }
//            }
//            break;
//        case STORE:
//            if (maxX >= 6 && y == 1 && maxZ >= 5)
//            {
//                //An actual landing pad (indoors!)
//                if (xx >= 3 && xx <= 5 && z >= 2 && z <= 4)
//                {
//                    state = GCBlocks.landingPad.getDefaultState();
//                }
//                else if (xx == 2)
//                {
//                    //Cargo loaders and unloaders, these will contain treasure items but only accessible when powered
//                    switch (z)
//                    {
//                    case 2:
//                        state = GCBlocks.cargoLoader.getStateFromMeta(facing);
//                        break;
//                    case 3:
//                        state = GCBlocks.aluminumWire.getStateFromMeta(1);
//                        break;
//                    case 4:
//                        state = GCBlocks.cargoLoader.getStateFromMeta(4 + (facing ^ 2));
//                        break;
//                    default:
//                    }
//                }
//            }
//            break;
//        case POWER:
//            facing = (facing + 1) % 4;
//            switch (y)
//            {
//            case 1:
//                //Layer 1: wall blocks with sealable alu wire in the centre
//                if (z == 3 && xx >= 4 && x <= maxX - 2) state = GCBlocks.sealableBlock.getStateFromMeta(14);
//                else if (xx >= 3 && z > 1 && (z < maxZ - 1 || z == 4)) state = this.configuration.getWallBlock();
//                break;
//            case 2:
//                //Layer 2: tier 1 storage and alu wire and a switch
//                if (z == 2)
//                {
//                    if (xx == 3) state = GCBlocks.machineTiered.getStateFromMeta(0 + facing);
//                    else if (xx > 3 && x < maxX - 2) state = GCBlocks.aluminumWire.getStateFromMeta(0);
//                    else if (x == maxX - 2) state = GCBlocks.aluminumWire.getStateFromMeta(2);
//                    else if ((xx == 3 || x == maxX - 1)) state = AsteroidBlocks.blockMinerBase.getDefaultState();
//                }
//                else if (z == 3 && x == maxX - 2) state = GCBlocks.aluminumWire.getStateFromMeta(0);
//                //An industrial looking frame for the whole structure
//                else if (z == 4 && (xx == 3 || x == maxX - 1)) state = AsteroidBlocks.blockMinerBase.getDefaultState();
//                break;
//            case 3:
//                //Layer 3: tier 2 storage and alu wire
//                if (z == 2 && xx == 4) state = GCBlocks.aluminumWire.getStateFromMeta(0);
//                else if (z == 3 && x == maxX - 2) state = GCBlocks.machineTiered.getStateFromMeta(8 + (facing ^ 2));
//                else if (z == 3 && x == maxX - 1) state = GCBlocks.aluminumWire.getStateFromMeta(1);
//                else if (z == 3 && xx >= 4 && x < maxX - 2) state = GCBlocks.aluminumWire.getStateFromMeta(0);
//                //An industrial looking frame for the whole structure
//                else if ((z == 2 || z == 4) && (xx == 3 || x == maxX - 1)) state = AsteroidBlocks.blockMinerBase.getDefaultState();
//                break;
//            case 4:
//                //Layer 4: tier 2 storage and alu wire
//                if (z == 3)
//                {
//                if (x == maxX - 3) state = GCBlocks.machineTiered.getStateFromMeta(8 + facing);
//                else if (x == maxX - 1 || x == maxX - 2) state = GCBlocks.aluminumWire.getStateFromMeta(1);
//                }
//                //An industrial looking frame for the whole structure
//                else if ((z == 2 || z == 4) && xx >= 3 && x < maxX) state = AsteroidBlocks.blockMinerBase.getDefaultState();
//                break;
//            }
//            break;
//        case ENGINEERING:
//            if (y == 1)
//            {
//                int closerz = (maxZ <= 5) ? 2 : 3;
//                if ((z == maxZ - 2 && x == maxX / 2 + 1 || (z == closerz && (x == 2 || x == maxX - 1) && maxX > 5)) && maxZ > 3)
//                {
//                    state = GCBlocks.nasaWorkbench.getDefaultState();
//                }
//                else
//                {
//                    state = AsteroidBlocks.asteroidDeco.getDefaultState();
//                }
//            }
//            break;
//        case MEDICAL:
//            int zTable = maxZ - 3;
//            int zTank = maxZ - 1;
//            int zLight = zTable;
//            if (zTable <= 1)
//            {
//                if (zTable < 0)
//                {
//                    //Too small to build the room
//                    break;
//                }
//
//                //Small medical room: bring everything closer together
//                zTable++;
//                zLight++;
//                zTank = maxZ;
//            }
//            if (y == 1)
//            {
//                //Operating table at y == 1
//                if (z == zTable && x <= maxX - 1 && x >= maxX - 3)
//                {
//                    state = GCBlocks.crafting.getStateFromMeta(1);
//                }
//                else
//                    //Tiled floor
//                    state = Blocks.IRON_TRAPDOOR.getDefaultState();
//            }
//            else if (y == 2 || y == 3)
//            {
//                //Operating table at y == 2
//                if (y == 2 && z == zTable && x <= maxX - 1 && x >= maxX - 3)
//                {
//                    state = GCBlocks.landingPad.getDefaultState();
//                }
//                else if (z == zTank)
//                {
//                    //Fluid tanks, 2 tanks high - these will be filled with Bacterial Sludge below
//                    if ((maxX - x) % 2 == 1)
//                    {
//                        state = GCBlocks.fluidTank.getDefaultState();
//                    }
//                }
//            }
//            else if ((y == 4 || y == 5) && (z == zLight || z == zLight - 1))
//            {
//                //Lighting
//                if (x == maxX)
//                    state = GCBlocks.concealedDetector.getStateFromMeta(8 + facing + (this.configuration.getDeckType() == EnumBaseType.HUMANOID ? 0 : 4));
//                else if (x == maxX - 1)
//                    state = GCBlocks.arcLamp.getStateFromMeta(facingLamp);
//            }
//            break;
//        case CREW:
//            if (y == 1)
//            {
//                if (x == 2 || x == maxX - 1)
//                {
//                    if ((z % 2) == 1)
//                    {
//                        state = GCBlocks.wallGC.getStateFromMeta((z == 1) ? 3 : 2);
//                    }
//                }
//                else
//                {
//                    state = Blocks.CARPET.getStateFromMeta(7);
//                }
//            } else if (y == 2)
//            {
//                if (x == 2 || x == maxX - 1)
//                {
//                    if (z == 1)
//                    {
//                        state = Blocks.BREWING_STAND.getDefaultState();
//                    }
//                    else if (z > 2 && z < maxZ)
//                    {
//                        state = GCBlocks.landingPad.getStateFromMeta(1);
//                    }
//                }
//            }
//            break;
//        case CONTROL:
//            if (y == 1)
//            {
//                if (x == maxX / 2 + 1 && z == maxZ - 2)
//                {
//                    state = AsteroidBlocks.asteroidDeco.getDefaultState();
//                }
//                else
//                {
//                    state = GCBlocks.slabGCHalf.getStateFromMeta(6);
//                }
//            }
//            else if (y <= 3)
//            {
//                if ((x == 1 || x == maxX) && !(z == 0 || z == maxZ))
//                {
//                    state = GCBlocks.telemetry.getDefaultState();
//                }
//                else if ((x == 2 || x == maxX - 1) && !(z == 0 || z == maxZ))
//                {
//                    state = GCBlocks.screen.getStateFromMeta((x == 2 ? facingLamp : facingLamp ^ 1));
//                }
//                else if (z == maxZ && x > 2 && x < maxX - 1)
//                {
//                    state = GCBlocks.screen.getStateFromMeta(facingScreen);
//                }
//                else if (x == maxX / 2 + 1 && z == maxZ - 2 && y == 2)
//                {
//                    state = GCBlocks.landingPad.getStateFromMeta(1);
//                }
//            }
//            break;
//        case CRYO:
//            boolean xEntrance = maxX > 5 ? (x > 2 && x < maxX - 1) : (x > 1 && x < maxX);
//            boolean highEntrance = this.configuration.isHangarDeck() && this.configuration.getDeckType() == EnumBaseType.AVIAN;
//            if (y == 1)
//            {
//                //Build a dark plinth for it all at y == 1
//                if (z == 1 && x > 1 && x < maxX)
//                {
//                    state = GCBlocks.slabGCHalf.getStateFromMeta(6);
//                }
//                else if (z != 0 || !xEntrance || highEntrance)
//                {
//                    state = AsteroidBlocks.asteroidDeco.getDefaultState();
//                }
//            }
//            else if (z == 0 && (y > (highEntrance ? 5 : 3) || !xEntrance))
//            {
//                //Dark ceiling and entrance wall
//                state = AsteroidBlocks.asteroidDeco.getDefaultState();
//            }
//            else if (y <= 4 && (z == maxZ || x == 1 || x == maxX))
//            {
//                //Around the walls: Cryo Chambers alternated with dark blocks
//                if (z == maxZ && x % 2 == 0 && x < maxX)
//                {
//                    if (y == 2)
//                    {
//                        state = MarsBlocks.machine.getStateFromMeta(BlockMachineMars.CRYOGENIC_CHAMBER_METADATA + facing1);
//                    }
//                }
//                else if (z < maxZ && z > 1 && (maxZ - z) % 2 == 0)
//                {
//                    if (y == 2)
//                    {
//                        state = MarsBlocks.machine.getStateFromMeta(BlockMachineMars.CRYOGENIC_CHAMBER_METADATA + (x == 1 ? facing : facing2));
//                    }
//                }
//                else
//                {
//                    state = AsteroidBlocks.asteroidDeco.getDefaultState();
//                }
//            }
//            else if (y > 5 || y == this.configuration.getRoomHeight() || (z == maxZ || x == 1 || x == maxX))
//            {
//                //Dark top section of walls and ceiling
//                state = AsteroidBlocks.asteroidDeco.getDefaultState();
//            }
//            break;
//        default:
//        } TODO Base room gen

        worldIn.setBlockState(blockpos, state, 2);
        if (state.getBlock() instanceof ITileEntityProvider)
        {
            TileEntity tile = worldIn.getTileEntity(blockpos);

            if (tile instanceof IMultiBlock)
            {
                List<BlockPos> positions = new LinkedList<>();
                ((IMultiBlock) tile).getPositions(blockpos, positions);
                for (BlockPos pos : positions)
                {
                    worldIn.setBlockState(pos, GCBlocks.MULTI_BLOCK.getDefaultState().with(BlockMulti.MULTI_TYPE, ((IMultiBlock) tile).getMultiType()), 2);
                }
            }

            if (tile instanceof TileEntityFluidTank)
            {
                ((TileEntityFluidTank) tile).fill(null, new FluidStack(PlanetFluids.LIQUID_BACTERIAL_SLUDGE.getFluid(), 16000), IFluidHandler.FluidAction.EXECUTE);
            }
            else if (tile instanceof TileEntityCargoLoader)
            {
                TileEntityCargoLoader loader = (TileEntityCargoLoader) tile;
                loader.locked = true;
                //Looks like the food supplies have gone off!
                loader.addCargo(new ItemStack(Items.POISONOUS_POTATO, 64), true);
                loader.addCargo(new ItemStack(Items.POISONOUS_POTATO, 64), true);
                loader.addCargo(new ItemStack(Items.POISONOUS_POTATO, 64), true);
                loader.addCargo(new ItemStack(Items.POISONOUS_POTATO, 64), true);
                loader.addCargo(new ItemStack(Items.ROTTEN_FLESH, 64), true);
                loader.addCargo(new ItemStack(GCItems.flagPole, semirand % 31 + 2), true);
                loader.addCargo(new ItemStack(MarsItems.slimelingCargo, semirand % 2 + 1), true);  //Slimeling Inventory Bag
                loader.addCargo(new ItemStack(AsteroidsItems.thermalCloth, semirand % 23 + 41), true); //Thermal cloth
                loader.addCargo(ItemOilCanister.createEmptyCanister(1), true);
                loader.addCargo(ItemOilCanister.createEmptyCanister(1), true);
                loader.addCargo(ItemOilCanister.createEmptyCanister(1), true);
                loader.addCargo(ItemOilCanister.createEmptyCanister(1), true);
                loader.addCargo(ItemOilCanister.createEmptyCanister(1), true);
                loader.addCargo(this.configuration.getDeckType().treasure.copy(), true);
            }
            else if (tile instanceof TileEntityCrafting)
            {
                switch (semirand % 4)
                {
                case 0:
                    break;
                case 1:
                    ((TileEntityCrafting) tile).setInventorySlotContents(1, new ItemStack(Items.IRON_INGOT));
                    ((TileEntityCrafting) tile).setInventorySlotContents(3, new ItemStack(Items.IRON_INGOT));
                    break;
                case 2:
                    //Creeper or Zombie head
                    int slot = semirand % 9;
                    ((TileEntityCrafting) tile).setInventorySlotContents(slot, new ItemStack((semirand % 13 < 6) ? Items.CREEPER_HEAD : Items.ZOMBIE_HEAD));
                    break;
                case 3:
                    ((TileEntityCrafting) tile).setInventorySlotContents(0, new ItemStack(Items.IRON_INGOT));
                    ((TileEntityCrafting) tile).setInventorySlotContents(1, new ItemStack(Items.IRON_INGOT));
                    ((TileEntityCrafting) tile).setInventorySlotContents(3, new ItemStack(Items.IRON_INGOT));
                    ((TileEntityCrafting) tile).setInventorySlotContents(4, new ItemStack(Items.STICK));
                    ((TileEntityCrafting) tile).setInventorySlotContents(7, new ItemStack(Items.STICK));
                    break;
                }
            }
            else if (tile instanceof BrewingStandTileEntity)
            {
                BrewingStandTileEntity stand = (BrewingStandTileEntity) tile;
                stand.setInventorySlotContents(0, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.POISON));
                stand.setInventorySlotContents(1, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WEAKNESS));
                stand.setInventorySlotContents(2, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.HARMING));
            }
            else if (tile instanceof TileEntityEnergyStorageModule)
            {
                TileEntityEnergyStorageModule store = (TileEntityEnergyStorageModule) tile;
                if (semirand % 3 == 1)
                {
                    ItemStack stack = new ItemStack(GCItems.battery, 1);
                    stack.setDamage(100);
                    store.setInventorySlotContents(1, stack);
                }
            }
        }

        if (this.type == EnumRoomType.ENGINEERING && y == (this.configuration.getRoomHeight() <= 4 ? 2 : 3) && z == maxZ && (x == 3 || x == 6))
        {
            Direction hangingDirection = this.direction;
            if (hangingDirection == Direction.WEST || hangingDirection == Direction.EAST)
            {
                //Apparently we have our North and our South reversed ?  So we don't want the opposite for North and South!
                hangingDirection = hangingDirection.getOpposite();
            }
            EntityHangingSchematic entityhanging = new EntityHangingSchematic(GCEntities.HANGING_SCHEMATIC, (World) worldIn, blockpos, hangingDirection, x / 3 - 1);
            worldIn.addEntity(entityhanging);
            entityhanging.setSendToClient();
        }
    }


    public enum EnumRoomType
    {
        ENGINEERING(AsteroidBlocks.asteroidDeco.getDefaultState(), AsteroidBlocks.asteroidDeco.getDefaultState(), false),
        POWER(null, null, false),
        STORE(null, null, false),
        EMPTY(null, null, false),
        MEDICAL(Blocks.IRON_TRAPDOOR.getDefaultState(), Blocks.IRON_TRAPDOOR.getDefaultState().with(TrapDoorBlock.HALF, Half.TOP), false),
        CREW(null, null, false),
        CRYO(AsteroidBlocks.asteroidDeco.getDefaultState(), AsteroidBlocks.asteroidDeco.getDefaultState(), true),
        CONTROL(AsteroidBlocks.asteroidDeco.getDefaultState()/* GCBlocks.slabGCHalf.getDefaultState() TODO Asteroids slab */, AsteroidBlocks.asteroidDeco.getDefaultState(), false);

        public final BlockState blockFloor;
        public final BlockState blockEntrance;
        public final boolean doEntryWallsToo;

        EnumRoomType(BlockState floorBlock, BlockState entrance, boolean doWalls)
        {
            this.blockFloor = floorBlock;
            this.blockEntrance = entrance;
            this.doEntryWallsToo = doWalls;
        }
    }
}