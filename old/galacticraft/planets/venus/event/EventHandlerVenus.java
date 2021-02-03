package micdoodle8.mods.galacticraft.planets.venus.event;

import micdoodle8.mods.galacticraft.api.entity.ILaserTrackableFast;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import micdoodle8.mods.galacticraft.planets.PlanetFluids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.venus.items.VenusItems;
import micdoodle8.mods.galacticraft.planets.venus.VenusModule;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityLaserTurret;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;

public class EventHandlerVenus
{
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START)
        {
            ((ServerLevel) event.world).getEntities()
                    .filter((e) -> e.ticksExisted % 20 == 1 && e instanceof LivingEntity && event.world.isMaterialInBB(e.getBoundingBox().grow(-0.1D, -0.4D, -0.1D), PlanetFluids.ACID_MATERIAL))
                    .forEach((e) -> e.attackEntityFrom(DamageSourceGC.acid, 3.0F));
        }
    }

    @SubscribeEvent
    public void onThermalArmorEvent(GCPlayerHandler.ThermalArmorEvent event)
    {
        if (event.armorStack.isEmpty())
        {
            event.setArmorAddResult(GCPlayerHandler.ThermalArmorEvent.ArmorAddResult.REMOVE);
        }
//        else if (event.armorStack.getItem() == VenusItems.thermalPaddingTier2 && event.armorStack.getDamage() == event.armorIndex)
//        {
//            event.setArmorAddResult(GCPlayerHandler.ThermalArmorEvent.ArmorAddResult.ADD);
//        }
        else
        {
            switch (event.armorIndex)
            {
            case 0:
                if (event.armorStack.getItem() == VenusItems.TIER_2_THERMAL_HELMET)
                {
                    event.setArmorAddResult(GCPlayerHandler.ThermalArmorEvent.ArmorAddResult.ADD);
                }
            case 1:
                if (event.armorStack.getItem() == VenusItems.TIER_2_THERMAL_CHESTPLATE)
                {
                    event.setArmorAddResult(GCPlayerHandler.ThermalArmorEvent.ArmorAddResult.ADD);
                }
            case 2:
                if (event.armorStack.getItem() == VenusItems.TIER_2_THERMAL_LEGGINGS)
                {
                    event.setArmorAddResult(GCPlayerHandler.ThermalArmorEvent.ArmorAddResult.ADD);
                }
            case 3:
                if (event.armorStack.getItem() == VenusItems.TIER_2_THERMAL_BOOTS)
                {
                    event.setArmorAddResult(GCPlayerHandler.ThermalArmorEvent.ArmorAddResult.ADD);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntitySpawned(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof ILaserTrackableFast)
        {
            for (BlockEntity tile : event.getEntity().getEntityWorld().loadedTileEntityList)
            {
                if (tile instanceof TileEntityLaserTurret)
                {
                    ((TileEntityLaserTurret) tile).trackEntity(event.getEntity());
                }
            }
        }
    }
}