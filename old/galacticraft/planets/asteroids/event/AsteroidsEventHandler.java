package micdoodle8.mods.galacticraft.planets.asteroids.event;

import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler.ThermalArmorEvent;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AsteroidsEventHandler
{
    @SubscribeEvent
    public void onThermalArmorEvent(ThermalArmorEvent event)
    {
        if (event.armorStack.isEmpty())
        {
            event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.REMOVE);
        }
        else
        {
            switch (event.armorIndex)
            {
            case 0:
                if (event.armorStack.getItem() == AsteroidsItems.THERMAL_HELMET)
                {
                    event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.ADD);
                }
            case 1:
                if (event.armorStack.getItem() == AsteroidsItems.THERMAL_CHESTPLATE)
                {
                    event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.ADD);
                }
            case 2:
                if (event.armorStack.getItem() == AsteroidsItems.THERMAL_LEGGINGS)
                {
                    event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.ADD);
                }
            case 3:
                if (event.armorStack.getItem() == AsteroidsItems.THERMAL_BOOTS)
                {
                    event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.ADD);
                }
            }
        }
    }
}
