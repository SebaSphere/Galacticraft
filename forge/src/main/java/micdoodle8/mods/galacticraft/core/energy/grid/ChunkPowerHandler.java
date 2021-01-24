package micdoodle8.mods.galacticraft.core.energy.grid;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class ChunkPowerHandler
{
    private static boolean initiated = false;

    public static void initiate()
    {
        if (!ChunkPowerHandler.initiated)
        {
            ChunkPowerHandler.initiated = true;
            MinecraftForge.EVENT_BUS.register(new ChunkPowerHandler());
        }
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event)
    {
        if (!event.getWorld().isRemote() && event.getChunk() instanceof LevelChunk)
        {
            try
            {
                ArrayList<Object> tileList = Lists.newArrayList();
                tileList.addAll(((LevelChunk) event.getChunk()).getBlockEntities().values());

                for (Object o : tileList)
                {
                    if (o instanceof BlockEntity)
                    {
                        BlockEntity tile = (BlockEntity) o;

                        if (tile instanceof INetworkConnection)
                        {
//                            ((INetworkConnection) tile).refresh(); TODO
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            if (ConfigManagerCore.retrogenOil.get() && GCCoreUtil.getDimensionType(event.getWorld()) == DimensionType.OVERWORLD)
            {
                EventHandlerGC.retrogenOil(event.getWorld(), event.getChunk());
            }
        }
    }
}
