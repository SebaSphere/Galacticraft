package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerSchematicBuggy extends Container
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.SCHEMATIC_BUGGY)
    public static ContainerType<ContainerSchematicBuggy> TYPE;

    public InventoryBuggyBench craftMatrix = new InventoryBuggyBench(this);
    public IInventory craftResult = new CraftResultInventory();
    private final World world;

    public ContainerSchematicBuggy(int containerId, PlayerInventory playerInv)
    {
        super(TYPE, containerId);
        final int change = 27;
        this.world = playerInv.player.world;
        this.addSlot(new SlotRocketBenchResult(playerInv.player, this.craftMatrix, this.craftResult, 0, 142, 79 + change));
        int var6;
        int var7;

        // Body
        for (var6 = 0; var6 < 4; ++var6)
        {
            for (var7 = 0; var7 < 3; ++var7)
            {
                this.addSlot(new SlotBuggyBench(this.craftMatrix, var7 * 4 + var6 + 1, 39 + var7 * 18, 14 + var6 * 18 + change, playerInv.player));
            }
        }

        for (var6 = 0; var6 < 2; ++var6)
        {
            for (var7 = 0; var7 < 2; ++var7)
            {
                this.addSlot(new SlotBuggyBench(this.craftMatrix, var7 * 2 + var6 + 13, 21 + var7 * 72, 14 + var6 * 54 + change, playerInv.player));
            }
        }

        // Addons
        for (int var8 = 0; var8 < 3; var8++)
        {
            this.addSlot(new SlotBuggyBench(this.craftMatrix, 17 + var8, 93 + var8 * 26, -15 + change, playerInv.player));
        }

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlot(new Slot(playerInv, var7 + var6 * 9 + 9, 8 + var7 * 18, 111 + var6 * 18 + change));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlot(new Slot(playerInv, var6, 8 + var6 * 18, 169 + change));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    @Override
    public void onContainerClosed(PlayerEntity par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);

        if (!this.world.isRemote)
        {
            for (int var2 = 1; var2 < this.craftMatrix.getSizeInventory(); ++var2)
            {
                final ItemStack slot = this.craftMatrix.removeStackFromSlot(var2);

                if (!slot.isEmpty())
                {
                    par1EntityPlayer.entityDropItem(slot, 0.0F);
                }
            }
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        this.craftResult.setInventorySlotContents(0, RecipeUtil.findMatchingBuggy(this.craftMatrix));
    }

    @Override
    public boolean canInteractWith(PlayerEntity par1EntityPlayer)
    {
        return true;
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(par1);
        final int b = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            final ItemStack var4 = slot.getStack();
            var2 = var4.copy();

            if (par1 < b - 36)
            {
                if (!this.mergeItemStack(var4, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }

                if (par1 == 0)
                {
                    slot.onSlotChange(var4, var2);
                }
            }
            else
            {
                Item i = var4.getItem();
                if (i == GCItems.TIER_1_HEAVY_DUTY_PLATE || i == GCItems.BUGGY_WHEEL || i == GCItems.BUGGY_SEAT || i == GCItems.BUGGY_STORAGE_BOX)
                {
                    for (int j = 1; j < 20; j++)
                    {
                        if (this.inventorySlots.get(j).isItemValid(var4))
                        {
                            this.mergeOneItem(var4, j, j + 1, false);
                        }
                    }
                }
                else
                {
                    if (par1 < b - 9)
                    {
                        if (!this.mergeItemStack(var4, b - 9, b, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else
                    {
                        if (!this.mergeItemStack(var4, b - 36, b - 9, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if (var4.getCount() == 0)
            {
                slot.putStack(ItemStack.EMPTY);
            }

            if (var4.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onSlotChanged();
            slot.onTake(par1EntityPlayer, var4);
        }

        return var2;
    }


    protected boolean mergeOneItem(ItemStack par1ItemStack, int par2, int par3, boolean par4)
    {
        boolean flag1 = false;
        if (par1ItemStack.getCount() > 0)
        {
            Slot slot;
            ItemStack slotStack;

            for (int k = par2; k < par3; k++)
            {
                slot = this.inventorySlots.get(k);
                slotStack = slot.getStack();

                if (slotStack.isEmpty())
                {
                    ItemStack stackOneItem = par1ItemStack.copy();
                    stackOneItem.setCount(1);
                    par1ItemStack.shrink(1);
                    slot.putStack(stackOneItem);
                    slot.onSlotChanged();
                    flag1 = true;
                    break;
                }
            }
        }

        return flag1;
    }
}
