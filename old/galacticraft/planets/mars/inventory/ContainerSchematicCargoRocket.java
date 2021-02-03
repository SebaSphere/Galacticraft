package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.inventory.SlotRocketBenchResult;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityMethaneSynthesizer;
import micdoodle8.mods.galacticraft.planets.mars.util.RecipeUtilMars;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerSchematicCargoRocket extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsContainerNames.SCHEMATIC_CARGO_ROCKET)
    public static MenuType<ContainerSchematicCargoRocket> TYPE;

    public InventorySchematicCargoRocket craftMatrix = new InventorySchematicCargoRocket(this);
    public Container craftResult = new ResultContainer();
    private final Level world;

    public ContainerSchematicCargoRocket(int containerId, Inventory playerInv)
    {
        super(TYPE, containerId);
        int change = 27;
        this.world = playerInv.player.level;
        this.addSlot(new SlotRocketBenchResult(playerInv.player, this.craftMatrix, this.craftResult, 0, 142, 69 + change));
        int var6;
        int var7;

        // Cone
        this.addSlot(new SlotSchematicCargoRocket(this.craftMatrix, 1, 48, -9 + change, playerInv.player));

        this.addSlot(new SlotSchematicCargoRocket(this.craftMatrix, 2, 48, -9 + 18 + change, playerInv.player));

        // Body
        for (var6 = 0; var6 < 3; ++var6)
        {
            this.addSlot(new SlotSchematicCargoRocket(this.craftMatrix, 3 + var6, 39, -7 + var6 * 18 + 16 + 18 + change, playerInv.player));
        }

        // Body Right
        for (var6 = 0; var6 < 3; ++var6)
        {
            this.addSlot(new SlotSchematicCargoRocket(this.craftMatrix, 6 + var6, 57, -7 + var6 * 18 + 16 + 18 + change, playerInv.player));
        }

        // Left fins
        this.addSlot(new SlotSchematicCargoRocket(this.craftMatrix, 9, 21, 63 + change, playerInv.player));
        this.addSlot(new SlotSchematicCargoRocket(this.craftMatrix, 10, 21, 81 + change, playerInv.player));

        // Engine
        this.addSlot(new SlotSchematicCargoRocket(this.craftMatrix, 11, 48, 81 + change, playerInv.player));

        // Right fins
        this.addSlot(new SlotSchematicCargoRocket(this.craftMatrix, 12, 75, 63 + change, playerInv.player));
        this.addSlot(new SlotSchematicCargoRocket(this.craftMatrix, 13, 75, 81 + change, playerInv.player));

        // Addons
        for (int var8 = 0; var8 < 3; var8++)
        {
            this.addSlot(new SlotSchematicCargoRocket(this.craftMatrix, 14 + var8, 93 + var8 * 26, -15 + change, playerInv.player));
        }

        change = 9;

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlot(new Slot(playerInv, var7 + var6 * 9 + 9, 8 + var7 * 18, 129 + var6 * 18 + change));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlot(new Slot(playerInv, var6, 8 + var6 * 18, 18 + 169 + change));
        }

        this.slotsChanged(this.craftMatrix);
    }

    @Override
    public void removed(Player par1EntityPlayer)
    {
        super.removed(par1EntityPlayer);

        if (!this.world.isClientSide)
        {
            for (int var2 = 1; var2 < this.craftMatrix.getContainerSize(); ++var2)
            {
                final ItemStack var3 = this.craftMatrix.removeItemNoUpdate(var2);

                if (!var3.isEmpty())
                {
                    par1EntityPlayer.spawnAtLocation(var3, 0.0F);
                }
            }
        }
    }

    @Override
    public void slotsChanged(Container par1IInventory)
    {
        this.craftResult.setItem(0, RecipeUtilMars.findMatchingCargoRocketRecipe(this.craftMatrix));
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot var3 = this.slots.get(par1);

        if (var3 != null && var3.hasItem())
        {
            final ItemStack var4 = var3.getItem();
            var2 = var4.copy();

            boolean done = false;
            if (par1 <= 16)
            {
                if (!this.moveItemStackTo(var4, 17, 53, false))
                {
                    return ItemStack.EMPTY;
                }

                if (par1 == 0)
                {
                    var3.onQuickCraft(var4, var2);
                }
            }
            else
            {
                for (int i = 1; i < 14; i++)
                {
                    Slot testSlot = this.slots.get(i);
                    if (!testSlot.hasItem() && testSlot.mayPlace(var2))
                    {
                        if (!this.mergeOneItem(var4, i, i + 1, false))
                        {
                            return ItemStack.EMPTY;
                        }
                        done = true;
                        break;
                    }
                }

                if (!done)
                {
                    if (var2.getItem() == Item.byBlock(Blocks.CHEST) && !this.slots.get(14).hasItem())
                    {
                        if (!this.mergeOneItem(var4, 14, 15, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (var2.getItem() == Item.byBlock(Blocks.CHEST) && !this.slots.get(15).hasItem())
                    {
                        if (!this.mergeOneItem(var4, 15, 16, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (var2.getItem() == Item.byBlock(Blocks.CHEST) && !this.slots.get(16).hasItem())
                    {
                        if (!this.mergeOneItem(var4, 16, 17, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (par1 >= 17 && par1 < 44)
                    {
                        if (!this.moveItemStackTo(var4, 44, 53, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (par1 >= 44 && par1 < 53)
                    {
                        if (!this.moveItemStackTo(var4, 17, 44, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.moveItemStackTo(var4, 17, 53, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (var4.isEmpty())
            {
                var3.set(ItemStack.EMPTY);
            }
            else
            {
                var3.setChanged();
            }

            if (var4.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            var3.onTake(par1EntityPlayer, var4);
        }

        return var2;
    }

    protected boolean mergeOneItem(ItemStack par1ItemStack, int par2, int par3, boolean par4)
    {
        boolean flag1 = false;
        if (!par1ItemStack.isEmpty())
        {
            Slot slot;
            ItemStack slotStack;

            for (int k = par2; k < par3; k++)
            {
                slot = this.slots.get(k);
                slotStack = slot.getItem();

                if (slotStack.isEmpty())
                {
                    ItemStack stackOneItem = par1ItemStack.copy();
                    stackOneItem.setCount(1);
                    par1ItemStack.shrink(1);
                    slot.set(stackOneItem);
                    slot.setChanged();
                    flag1 = true;
                    break;
                }
            }
        }

        return flag1;
    }
}