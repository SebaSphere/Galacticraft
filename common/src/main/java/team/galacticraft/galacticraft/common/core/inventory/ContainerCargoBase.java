package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.api.item.IItemElectric;
import team.galacticraft.galacticraft.common.api.tile.ILockable;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.energy.EnergyUtil;
import team.galacticraft.galacticraft.common.core.tile.TileEntityCargoBase;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.registries.ObjectHolder;

public class ContainerCargoBase extends AbstractContainerMenu
{
    public static class ContainerCargoLoader extends ContainerCargoBase
    {
    //    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.CARGO_LOADER)
        public static MenuType<ContainerCargoLoader> TYPE;

        public ContainerCargoLoader(int containerId, Inventory playerInv, TileEntityCargoBase cargoTile)
        {
            super(TYPE, containerId, playerInv, cargoTile);
        }
    }

    public static class ContainerCargoUnloader extends ContainerCargoBase
    {
    //    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.CARGO_UNLOADER)
        public static MenuType<ContainerCargoUnloader> TYPE;

        public ContainerCargoUnloader(int containerId, Inventory playerInv, TileEntityCargoBase cargoTile)
        {
            super(TYPE, containerId, playerInv, cargoTile);
        }
    }

    private final TileEntityCargoBase cargoTile;
    private boolean locked;

    public ContainerCargoBase(MenuType<?> type, int containerId, Inventory playerInv, TileEntityCargoBase cargoTile)
    {
        super(type, containerId);
        this.cargoTile = cargoTile;
        if (this.cargoTile instanceof ILockable)
        {
            this.locked = ((ILockable) this.cargoTile).getLocked();
        }

        this.addSlot(new SlotSpecific(cargoTile, 0, 10, 27, IItemElectric.class));

        int var6;
        int var7;

        for (var6 = 0; var6 < 2; ++var6)
        {
            for (var7 = 0; var7 < 7; ++var7)
            {
                this.addSlot(new Slot(cargoTile, var7 + var6 * 7 + 1, 38 + var7 * 18, 27 + var6 * 18));
            }
        }

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlot(new Slot(playerInv, var7 + var6 * 9 + 9, 8 + var7 * 18, 124 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlot(new Slot(playerInv, var6, 8 + var6 * 18, 66 + 116));
        }
    }

    public TileEntityCargoBase getCargoTile()
    {
        return cargoTile;
    }

    @Override
    public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, Player player)
    {
        if (this.locked && slotId > 0 && slotId < 15)
        {
            return ItemStack.EMPTY;
        }
        return super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public boolean stillValid(Player var1)
    {
        return this.cargoTile.stillValid(var1);
    }

    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par2)
    {
        ItemStack var3 = ItemStack.EMPTY;
        final Slot slot = this.slots.get(par2);

        if (slot != null && slot.hasItem())
        {
            final ItemStack var5 = slot.getItem();
            var3 = var5.copy();

            if (par2 < 15)
            {
                if ((this.locked && par2 > 0) || !this.moveItemStackTo(var5, 15, 51, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (EnergyUtil.isElectricItem(var5.getItem()))
                {
                    if (!this.moveItemStackTo(var5, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par2 < 42)
                {
                    if ((this.locked || !this.moveItemStackTo(var5, 1, 15, false)) && !this.moveItemStackTo(var5, 42, 51, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if ((this.locked || !this.moveItemStackTo(var5, 1, 15, false)) && !this.moveItemStackTo(var5, 15, 42, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (var5.getCount() == 0)
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (var5.getCount() == var3.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, var5);
        }

        return var3;
    }
}
