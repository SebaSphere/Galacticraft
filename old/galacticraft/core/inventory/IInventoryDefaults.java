package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;

/*
 * IInventory providing defaults for the eight methods
 * which we normally don't use in tiles and entities.
 *
 * (It's not necessary to use the fields system where
 * we use a Container for the inventory.)
 *
 * Override .hasCustomName() if true is required.
 */
public interface IInventoryDefaults extends WorldlyContainer
{
    //We don't use these because we use forge containers
    @Override
    default void startOpen(Player player)
    {
    }

    //We don't use these because we use forge containers
    @Override
    default void stopOpen(Player player)
    {
    }

//    @Override
//    public default int getField(int id)
//    {
//        return 0;
//    }
//
//    @Override
//    public default void setField(int id, int value)
//    {
//    }
//
//    @Override
//    public default int getFieldCount()
//    {
//        return 0;
//    }

    @Override
    default void clearContent()
    {

    }

    /**
     * Override this and return true IF the inventory .getName() is
     * ALREADY a localized name e.g. by GCCoreUtil.translate()
     *
     **/
//    @Override
//    public default boolean hasCustomName()
//    {
//        return false;
//    }

//    @Override
//    public default ITextComponent getDisplayName()
//    {
//        return this.hasCustomName() ? new StringTextComponent(this.getName()) : new TranslationTextComponent(this.getName(), new Object[0]);
//    }
}
