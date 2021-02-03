package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotCompressor extends Slot
{
    public SlotCompressor(Container par1iInventory, int par2, int par3, int par4)
    {
        super(par1iInventory, par2, par3, par4);
    }

    @Override
    public boolean mayPlace(ItemStack par1ItemStack)
    {
        return par1ItemStack.getItem() instanceof ItemOxygenTank;
    }
}