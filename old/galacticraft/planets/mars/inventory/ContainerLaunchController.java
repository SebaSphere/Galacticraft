package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.inventory.SlotSpecific;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerLaunchController extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsContainerNames.LAUNCH_CONTROLLER)
    public static MenuType<ContainerLaunchController> TYPE;

    private final TileEntityLaunchController launchController;

    public ContainerLaunchController(int containerId, Inventory playerInv, TileEntityLaunchController launchController)
    {
        super(TYPE, containerId);
        this.launchController = launchController;
        this.launchController.checkDestFrequencyValid();

        this.addSlot(new SlotSpecific(this.launchController, 0, 152, 105, IItemElectric.class));

        int var6;
        int var7;

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlot(new Slot(playerInv, var7 + var6 * 9 + 9, 8 + var7 * 18, 127 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlot(new Slot(playerInv, var6, 8 + var6 * 18, 185));
        }

        this.launchController.startOpen(playerInv.player);
    }

    public TileEntityLaunchController getLaunchController()
    {
        return launchController;
    }

    @Override
    public void removed(Player entityplayer)
    {
        super.removed(entityplayer);
        this.launchController.stopOpen(entityplayer);
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.launchController.stillValid(par1EntityPlayer);
    }

    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot slot = this.slots.get(par1);
        final int b = this.slots.size();

        if (slot != null && slot.hasItem())
        {
            final ItemStack stack = slot.getItem();
            var2 = stack.copy();

            if (par1 == 0)
            {
                if (!this.moveItemStackTo(stack, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (EnergyUtil.isElectricItem(stack.getItem()))
                {
                    if (!this.moveItemStackTo(stack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    if (par1 < b - 9)
                    {
                        if (!this.moveItemStackTo(stack, b - 9, b, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.moveItemStackTo(stack, b - 36, b - 9, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stack.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (stack.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, stack);
        }

        return var2;
    }
}