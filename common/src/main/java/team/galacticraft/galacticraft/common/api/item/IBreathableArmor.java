package team.galacticraft.galacticraft.common.api.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Implement into Armor items
 */
public interface IBreathableArmor
{
    /**
     * @param gearType The gear type to be handled
     * @return true if the passed gear type should be handled by this armor item
     */
    boolean handleGearType(EnumGearType gearType);

    /**
     * Determines if armor item is currently valid for breathing
     *
     * @param helmetInSlot  The armor itemstack
     * @param playerWearing The player wearing the armor
     * @param type          The oxygen gear type @see EnumGearType
     * @return true if this armor item is valid for the provided oxygen gear
     * type
     */
    boolean canBreathe(ItemStack helmetInSlot, Player playerWearing, EnumGearType type);

    enum EnumGearType
    {
        HELMET,
        GEAR,
        TANK1,
        TANK2
    }
}
