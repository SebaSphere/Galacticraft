package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlockNames;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;

public class TileEntityMinerBaseSingle extends TileEntity implements ITickableTileEntity
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + AsteroidBlockNames.ASTRO_MINER_BASE)
    public static TileEntityType<TileEntityMinerBaseSingle> TYPE;

    private int corner = 0;

    public TileEntityMinerBaseSingle()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (!this.world.isRemote && this.corner == 0)
        {
            final ArrayList<TileEntity> attachedBaseBlocks = new ArrayList<TileEntity>();

            final int thisX = this.getPos().getX();
            final int thisY = this.getPos().getY();
            final int thisZ = this.getPos().getZ();

            boolean success = true;
            SEARCH:
            for (int x = 0; x < 2; x++)
            {
                for (int y = 0; y < 2; y++)
                {
                    for (int z = 0; z < 2; z++)
                    {
                        BlockPos pos = new BlockPos(x + thisX, y + thisY, z + thisZ);
                        final TileEntity tile = this.world.isBlockLoaded(pos) ? this.world.getTileEntity(pos) : null;

                        if (tile instanceof TileEntityMinerBaseSingle && !tile.isRemoved() && ((TileEntityMinerBaseSingle) tile).corner == 0)
                        {
                            attachedBaseBlocks.add(tile);
                        }
                        else
                        {
                            success = false;
                            break SEARCH;
                        }
                    }
                }
            }

            if (success)
            {
                TileEntityMinerBase.addNewMinerBase(GCCoreUtil.getDimensionType(this), this.getPos());
                for (final TileEntity tile : attachedBaseBlocks)
                {
                    ((TileEntityMinerBaseSingle) tile).corner = 1;
                    this.world.removeBlock(this.getPos(), false);
                }
                //Don't try setting a new block with a TileEntity, because new tiles can
                //get removed after the end of this tileEntity.tick() tick - setting a new block
                //here would automatically remove this tile.
                //
                //(It's because if this tileEntity is now invalid, World.updateEntities() removes
                // *any* tileEntity at this position - see call to Chunk.removeTileEntity(pos)nee!)
                //
                //Equally if any others of these TileEntityMinerBaseSingle are ticked AFTER this
                //in the same server tick tick, then those new ones will also be removed
                //because their TileEntityMinerBaseSingle is now, inevitably, invalid!
            }
        }
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }
}
