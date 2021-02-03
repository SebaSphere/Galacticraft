package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlockNames;
import micdoodle8.mods.galacticraft.planets.venus.client.fx.VenusParticles;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Random;

public class TileEntitySpout extends BlockEntity implements TickableBlockEntity
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + VenusBlockNames.VAPOR_SPOUT)
    public static BlockEntityType<TileEntitySpout> TYPE;

    private final Random rand = new Random(System.currentTimeMillis());

    public TileEntitySpout()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (this.level.isClientSide)
        {
            if (rand.nextInt(400) == 0)
            {
                BlockState stateAbove = this.level.getBlockState(this.getBlockPos().above());
                if (stateAbove.getBlock().isAir(this.level.getBlockState(this.getBlockPos().above()), this.level, this.getBlockPos().above()))
                {
                    double posX = (double) worldPosition.getX() + 0.45 + rand.nextDouble() * 0.1;
                    double posY = (double) worldPosition.getY() + 1.0;
                    double posZ = (double) worldPosition.getZ() + 0.45 + rand.nextDouble() * 0.1;
                    for (int i = 0; i < 4 + rand.nextInt(4); ++i)
                    {
                        level.addParticle(VenusParticles.ACID_VAPOR, posX, posY, posZ, rand.nextDouble() * 0.5 - 0.25, rand.nextDouble() * 0.5 + 0.5, rand.nextDouble() * 0.5 - 0.25);
                    }
                }
            }
        }
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }
}
