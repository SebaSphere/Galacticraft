package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GCBlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;

import com.google.common.collect.Lists;

public class TileEntityBuggyFuelerSingle extends BlockEntity implements TickableBlockEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.BUGGY_FUELING_PAD)
    public static BlockEntityType<TileEntityBuggyFuelerSingle> TYPE;

    private int corner = 0;

    public TileEntityBuggyFuelerSingle()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (!this.level.isClientSide && this.corner == 0)
        {
            List<BlockEntity> attachedLaunchPads = Lists.newArrayList();

            for (int x = this.getBlockPos().getX() - 1; x < this.getBlockPos().getX() + 2; x++)
            {
                for (int z = this.getBlockPos().getZ() - 1; z < this.getBlockPos().getZ() + 2; z++)
                {
                    BlockEntity tile = this.level.getBlockEntity(new BlockPos(x, this.getBlockPos().getY(), z));

                    if (tile instanceof TileEntityBuggyFuelerSingle && !tile.isRemoved() && ((TileEntityBuggyFuelerSingle) tile).corner == 0)
                    {
                        attachedLaunchPads.add(tile);
                    }
                }
            }

            if (attachedLaunchPads.size() == 9)
            {
                for (BlockEntity tile : attachedLaunchPads)
                {
                    this.level.removeBlockEntity(tile.getBlockPos());
                    ((TileEntityBuggyFuelerSingle) tile).corner = 1;
                }

                this.level.setBlock(this.getBlockPos(), GCBlocks.FULL_BUGGY_FUELING_PAD.defaultBlockState(), 2);
            }
        }
    }
}