package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.tile.TileEntityCrafting;
import team.galacticraft.galacticraft.common.core.util.GCLog;
import team.galacticraft.galacticraft.common.core.util.RecipeUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
//import net.minecraftforge.registries.ObjectHolder;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ContainerCrafting extends AbstractContainerMenu
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.CRAFTING)
    public static MenuType<ContainerCrafting> TYPE;

    public TileEntityCrafting tileCrafting;
    public PersistantInventoryCrafting craftMatrix;
    public Container craftResult = new ResultContainer();
    private final NonNullList<ItemStack> memory = NonNullList.withSize(9, ItemStack.EMPTY);

    public ContainerCrafting(int containerId, Inventory playerInv, TileEntityCrafting crafting)
    {
        super(TYPE, containerId);
        this.tileCrafting = crafting;
        this.craftMatrix = crafting != null ? this.tileCrafting.craftMatrix : null;
        if (this.craftMatrix != null)
        {
            this.craftMatrix.eventHandler = this;
        }
        this.addSlot(new SlotCraftingMemory(playerInv.player, this.craftMatrix, this.craftResult, 0, 124, 33, this.tileCrafting));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                this.addSlot(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        for (int k = 0; k < 3; ++k)
        {
            for (int i1 = 0; i1 < 9; ++i1)
            {
                this.addSlot(new Slot(playerInv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l)
        {
            this.addSlot(new Slot(playerInv, l, 8 + l * 18, 142));
        }

        this.slotsChanged(this.craftMatrix);
    }

    @Override
    public NonNullList<ItemStack> getItems()
    {
        NonNullList<ItemStack> list = NonNullList.create();

        for (int i = 0; i < this.slots.size(); ++i)
        {
            list.add(this.slots.get(i).getItem());
        }

        //Override this method to trick vanilla networking into carrying our memory at end of its packets
        for (int i = 0; i < 9; i++)
        {
            list.add(this.tileCrafting.memory.get(i));
        }

        return list;
    }

    @Override
    public void setAll(List<ItemStack> list)
    {
        for (int i = 0; i < list.size(); ++i)
        {
            if (i < 46)
            {
                this.getSlot(i).set(list.get(i));
            }
            else if (i < 55)
            //Read memory clientside from the end of the vanilla packet, see getInventory()
            {
                this.tileCrafting.memory.set(i - 46, list.get(i));
            }
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void slotsChanged(Container inventoryIn)
    {
        Level world = tileCrafting.getLevel();
        Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, this.craftMatrix, world);
        this.craftResult.setItem(0, optional.isPresent() ? optional.get().assemble(this.craftMatrix) : ItemStack.EMPTY);
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void removed(Player playerIn)
    {
        super.removed(playerIn);
        if (!playerIn.level.isClientSide)
        {
            craftMatrix.eventHandler = null;
        }
    }

    @Override
    public boolean stillValid(Player playerIn)
    {
        return this.tileCrafting.stillValid(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem() && !slot.getItem().isEmpty())
        {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                if (!this.moveItemStackTo(itemstack1, 10, 46, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            }
            else if (index < 10)
            {
                if (!this.moveItemStackTo(itemstack1, 10, 46, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.matchesCrafting(itemstack1))
            {
                if (!this.mergeToCrafting(itemstack1, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 10 && index < 37)
            {
                if (!this.moveItemStackTo(itemstack1, 37, 46, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 37 && index < 46)
            {
                if (!this.moveItemStackTo(itemstack1, 10, 37, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    private boolean mergeToCrafting(ItemStack stack, boolean b)
    {
        if (stack.isEmpty())
        {
            return false;
        }
        List<Slot> acceptSlots = new LinkedList<>();
        List<Integer> acceptQuantity = new LinkedList<>();
        int minQuantity = 64;
        int acceptTotal = 0;
        for (int i = 1; i < 10; i++)
        {
            Slot slot = this.slots.get(i);

            if (slot != null)
            {
                ItemStack target = slot.getItem();
                ItemStack target2 = this.memory.get(i - 1);
                if (target2.isEmpty())
                {
                    continue;
                }
                if (target.isEmpty())
                {
                    target = target2.copy();
                }
                if (matchingStacks(stack, target))
                {
                    acceptSlots.add(slot);
                    int availSpace = target.getMaxStackSize() - target.getCount();
                    acceptQuantity.add(availSpace);
                    acceptTotal += availSpace;
                    if (availSpace < minQuantity)
                    {
                        minQuantity = availSpace;
                    }
                }
            }
        }

        //First fill any empty slots
        for (Slot slot : acceptSlots)
        {
            ItemStack target = slot.getItem();
            if (target.isEmpty())
            {
                ItemStack target2 = this.memory.get(slot.index - 1);
                this.craftMatrix.setItem(slot.index - 1, target2.copy());
                stack.shrink(1);
                if (stack.isEmpty())
                {
                    return false;
                }
            }
        }

        //The stack more than exceeds what the crafting inventory requires
        if (stack.getCount() >= acceptTotal)
        {
            if (acceptTotal == 0)
            {
                return false;
            }

            for (Slot slot : acceptSlots)
            {
                ItemStack target = slot.getItem();
                stack.shrink(target.getMaxStackSize() - target.getCount());
                target.setCount(target.getMaxStackSize());
                slot.setChanged();
            }
            return true;
        }

        int uneven = 0;
        for (int q : acceptQuantity)
        {
            uneven += q - minQuantity;
        }

        //Use the whole stack to try to even up the neediest slots
        if (stack.getCount() <= uneven)
        {
            do
            {
                Slot neediest = null;
                int smallestStack = 64;
                for (Slot slot : acceptSlots)
                {
                    ItemStack target = slot.getItem();
                    if (target.getCount() < smallestStack)
                    {
                        smallestStack = target.getCount();
                        neediest = slot;
                    }
                }
                neediest.getItem().grow(1);
                stack.shrink(1);
            }
            while (!stack.isEmpty());
            for (Slot slot : acceptSlots)
            {
                slot.setChanged();
            }
            return true;
        }

        //Use some of the stack to even things up
        if (uneven > 0)
        {
            int targetSize = stack.getMaxStackSize() - minQuantity;
            for (Slot slot : acceptSlots)
            {
                ItemStack target = slot.getItem();
                stack.shrink(targetSize - target.getCount());
                acceptTotal -= targetSize - target.getCount();
                target.setCount(targetSize);
                slot.setChanged();
            }
        }

        //Spread the remaining stack over all slots evenly
        int average = stack.getCount() / acceptSlots.size();
        int modulus = stack.getCount() - average * acceptSlots.size();
        for (Slot slot : acceptSlots)
        {
            if (slot != null)
            {
                ItemStack target = slot.getItem();
                int transfer = average;
                if (modulus > 0)
                {
                    transfer++;
                    modulus--;
                }
                if (transfer > stack.getCount())
                {
                    transfer = stack.getCount();
                }
                stack.shrink(transfer);
                target.grow(transfer);
                if (target.getCount() > target.getMaxStackSize())
                {
                    GCLog.info("Shift clicking - slot " + slot.index + " wanted more than it could accept:" + target.getCount());
                    stack.grow(target.getCount() - target.getMaxStackSize());
                    target.setCount(target.getMaxStackSize());
                }
                slot.setChanged();
                if (stack.isEmpty())
                {
                    break;
                }
            }
        }

        return true;
    }

    private boolean matchesCrafting(ItemStack itemstack1)
    {
        if (this.tileCrafting.overrideMemory(itemstack1, this.memory))
        {
            return true;
        }

        for (int i = 0; i < 9; i++)
        {
            if (matchingStacks(itemstack1, this.tileCrafting.getMemory(i)) && (this.craftMatrix.getItem(i).isEmpty() || this.craftMatrix.getItem(i).getCount() < itemstack1.getMaxStackSize()))
            {
                for (int j = 0; j < 9; j++)
                {
                    this.memory.set(j, this.tileCrafting.getMemory(j));
                }
                return true;
            }
        }
        return false;
    }

    private boolean matchingStacks(ItemStack stack, ItemStack target)
    {
        return !target.isEmpty() && target.getItem() == stack.getItem() /*&& (!stack.getHasSubtypes() || stack.getMetadata() == target.getMetadata())*/ && RecipeUtil.areItemStackTagsEqual(stack, target) && (!target.isStackable() || target.getCount() < target.getMaxStackSize());
    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in
     * is null for the initial slot that was double-clicked.
     */
    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot p_94530_2_)
    {
        return p_94530_2_.container != this.craftResult && super.canTakeItemForPickAll(stack, p_94530_2_);
    }
}