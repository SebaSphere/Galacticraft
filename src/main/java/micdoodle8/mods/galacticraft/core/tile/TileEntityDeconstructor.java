package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.GCBlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachineBase;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.inventory.ContainerDeconstructor;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TileEntityDeconstructor extends TileBaseElectricBlock implements IInventoryDefaults, ISidedInventory, IMachineSides, INamedContainerProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.DECONSTRUCTOR)
    public static TileEntityType<TileEntityDeconstructor> TYPE;

    public static final float SALVAGE_CHANCE = 0.75F;
    public static final int PROCESS_TIME_REQUIRED_BASE = 250;
    public int processTimeRequired = PROCESS_TIME_REQUIRED_BASE;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int processTicks = 0;
    private final ItemStack producingStack = ItemStack.EMPTY;
    private long ticks;

    public static List<ItemStack> salvageable = new LinkedList<>();
    public static List<INasaWorkbenchRecipe> knownRecipes = new LinkedList<>();
    private int recursiveCount;

    static
    {
        initialiseItemList();
    }

    private static void initialiseItemList()
    {
//        if (GalacticraftCore.isPlanetsLoaded)
//        {
//            addSalvage(new ItemStack(AsteroidsItems.basicItem, 1, 5));  //T3 plate
//            addSalvage(new ItemStack(AsteroidsItems.basicItem, 1, 6));  //Compressed titanium
//            addSalvage(new ItemStack(AsteroidsItems.basicItem, 1, 0));  //Titanium ingot
//            addSalvage(new ItemStack(MarsItems.marsItemBasic, 1, 3));  //T2 plate
//            addSalvage(new ItemStack(MarsItems.marsItemBasic, 1, 5));  //Compressed desh
//            addSalvage(new ItemStack(MarsItems.marsItemBasic, 1, 2));  //Desh ingot
//        } TODO planets
        addSalvage(new ItemStack(GCItems.STEEL_POLE));
        addSalvage(new ItemStack(GCItems.TIER_1_HEAVY_DUTY_PLATE));
        addSalvage(new ItemStack(GCItems.METEORIC_IRON_INGOT, 1));  //Meteoric iron ingot
        addSalvage(new ItemStack(GCItems.COMPRESSED_STEEL, 1));  //Compressed steel
        addSalvage(new ItemStack(GCItems.COMPRESSED_ALUMINUM, 1));  //Compressed meteoric iron
        addSalvage(new ItemStack(GCItems.COMPRESSED_BRONZE, 1));  //Compressed bronze
        addSalvage(new ItemStack(GCItems.COMPRESSED_COPPER, 1));
        addSalvage(new ItemStack(GCItems.COMPRESSED_IRON, 1));
        addSalvage(new ItemStack(GCItems.COMPRESSED_METEORIC_IRON, 1));
        addSalvage(new ItemStack(GCItems.COMPRESSED_STEEL, 1));  //Compressed iron
        addSalvage(new ItemStack(GCItems.COMPRESSED_TIN, 1));
        addSalvage(new ItemStack(GCItems.ADVANCED_WAFER, 1));
        addSalvage(new ItemStack(GCItems.BASIC_WAFER, 1));
        addSalvage(new ItemStack(GCItems.SOLAR_WAFER, 1));
        addSalvage(new ItemStack(Items.IRON_INGOT));
        addSalvage(new ItemStack(Items.GOLD_INGOT));
        addSalvage(new ItemStack(Items.GOLD_NUGGET));
        addSalvage(new ItemStack(Items.DIAMOND));
    }

    public static void initialiseRecipeList()
    {
        knownRecipes.addAll(GalacticraftRegistry.getRocketT1Recipes());
        knownRecipes.addAll(GalacticraftRegistry.getBuggyBenchRecipes());
    }

    public static void initialiseRecipeListPlanets()
    {
        knownRecipes.addAll(GalacticraftRegistry.getRocketT2Recipes());
        knownRecipes.addAll(GalacticraftRegistry.getCargoRocketRecipes());
        knownRecipes.addAll(GalacticraftRegistry.getRocketT3Recipes());
        knownRecipes.addAll(GalacticraftRegistry.getAstroMinerRecipes());
    }

    public TileEntityDeconstructor()
    {
        super(TYPE);
        this.storage.setMaxExtract(ConfigManagerCore.hardMode.get() ? 90 : 75);
        this.setTierGC(2);
        this.inventory = NonNullList.withSize(11, ItemStack.EMPTY);
    }

    public static void addSalvage(ItemStack itemStack)
    {
        for (ItemStack inList : salvageable)
        {
            if (ItemStack.areItemsEqual(inList, itemStack))
            {
                return;
            }
        }
        salvageable.add(itemStack.copy());
    }

    public static boolean isSalvage(ItemStack stack)
    {
        for (ItemStack inList : salvageable)
        {
            if (ItemStack.areItemsEqual(inList, stack))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.world.isRemote)
        {
            boolean updateInv = false;

            if (this.hasEnoughEnergyToRun)
            {
                if (this.canDeconstruct())
                {
                    ++this.processTicks;

                    if ((this.processTicks * 5) % this.processTimeRequired == 5)
                    {
                        this.world.playSound(null, this.getPos(), GCSounds.deconstructor, SoundCategory.BLOCKS, 0.25F, this.world.rand.nextFloat() * 0.04F + 0.38F);
                    }

                    if (this.processTicks >= this.processTimeRequired)
                    {
                        this.processTicks = 0;
                        this.deconstruct();
                        updateInv = true;
                    }
                }
                else
                {
                    this.processTicks = 0;
                }
            }
            else
            {
                this.processTicks = 0;
            }

            if (updateInv)
            {
                this.markDirty();
            }
        }

        this.ticks++;
    }

    private boolean canDeconstruct()
    {
        return !this.getInventory().get(1).isEmpty();
    }

    public void deconstruct()
    {
        List<ItemStack> ingredients = new LinkedList<>();
        ItemStack input = this.getInventory().get(1).copy();
        input.setCount(1);
        ingredients.add(input);
        this.recursiveCount = 0;
        List<ItemStack> salvaged = this.getSalvageable(ingredients, null);
        salvaged = this.squashList(salvaged);
        salvaged = this.randomChanceList(salvaged);
        if (salvaged != null)
        {
            for (ItemStack output : salvaged)
            {
                this.addToOutputMatrix(output);
            }
        }
        this.decrStackSize(1, 1);
    }

    private List<ItemStack> getSalvageable(List<ItemStack> ingredients, ItemStack done)
    {
        if (ingredients == null || ingredients.isEmpty())
        {
            return null;
        }
        if (this.recursiveCount++ > 10)
        {
            return null;
        }

        List<ItemStack> ret = new LinkedList<>();
        for (ItemStack stack : ingredients)
        {
            if (isSalvage(stack))
            {
                ret.add(stack);
            }
            else
            {
                GCLog.debug("Trying to " + this.recursiveCount + " break down " + stack.toString());
                List<ItemStack> ingredients2 = this.getIngredients(stack);
                if (ingredients2 != null)
                {
                    if (done != null)
                    {
                        Iterator<ItemStack> it = ingredients2.iterator();
                        while (it.hasNext())
                        {
                            if (ItemStack.areItemStacksEqual(it.next(), done))
                            {
                                it.remove();  //prevent recursive A->{B}  B->{A} type recipe chains
                            }
                        }
                    }
                    List<ItemStack> recursive = this.getSalvageable(ingredients2, stack);
                    if (recursive != null && !recursive.isEmpty())
                    {
                        ret.addAll(recursive);
                    }
                }
            }
        }
        this.recursiveCount--;
        return ret;
    }

    private List<ItemStack> squashList(List<ItemStack> ingredients)
    {
        if (ingredients == null || ingredients.isEmpty())
        {
            return null;
        }
        List<ItemStack> ret = new LinkedList<>();
        for (ItemStack stack : ingredients)
        {
            boolean matched = false;
            for (ItemStack stack1 : ret)
            {
                if (ItemStack.areItemsEqual(stack, stack1))
                {
                    matched = true;
                    stack1.grow(stack.getCount());
                    break;
                }
            }
            if (!matched)
            {
                ret.add(stack);
            }
        }
        return ret;
    }

    private List<ItemStack> randomChanceList(List<ItemStack> ingredients)
    {
        if (ingredients == null || ingredients.isEmpty())
        {
            return null;
        }
        List<ItemStack> ret = new LinkedList<>();
        for (ItemStack stack : ingredients)
        {
            int count = stack.getCount();
            int result = 0;
            for (int i = 0; i < count; i++)
            {
                if (this.world.rand.nextFloat() < SALVAGE_CHANCE)
                {
                    result++;
                }
            }
            if (result > 0)
            {
                stack.setCount(result);
                ret.add(stack);
            }
        }
        return ret;
    }

    private List<ItemStack> getIngredients(ItemStack stack)
    {
        for (INasaWorkbenchRecipe recipe : knownRecipes)
        {
            ItemStack test = recipe.getRecipeOutput();
            if (ItemStack.areItemsEqual(test, stack) && test.getCount() == 1)
            {
                return toItemStackList(recipe.getRecipeInput().values());
            }
        }
//        for (IRecipe recipe : CraftingManager.REGISTRY)
//        {
//            ItemStack test = recipe.getRecipeOutput();
//            if (ItemStack.areItemsEqual(test, stack) && test.getCount() == 1)
//            {
//                if (recipe instanceof ShapedRecipe)
//                {
//                    return expandRecipeInputs(((ShapedRecipe) recipe).recipeItems);
//                }
//                else if (recipe instanceof ShapelessRecipe)
//                {
//                    return expandRecipeInputs(((ShapelessRecipe) recipe).recipeItems);
//                }
//                else if (recipe instanceof ShapedOreRecipe)
//                {
//                    return expandRecipeInputs(((ShapedOreRecipe) recipe).getIngredients());
//                }
//                else if (recipe instanceof ShapelessOreRecipe)
//                {
//                    return expandRecipeInputs(((ShapelessOreRecipe) recipe).getIngredients());
//                }
//            }
//        } TODO Deconstructor recipes
        return null;
    }

    private List<ItemStack> expandRecipeInputs(List<?> inputs)
    {
        List<ItemStack> ret = new LinkedList<>();
        for (Object input : inputs)
        {
            ItemStack toAdd = parseRecipeInput(input);
            if (toAdd != null && !toAdd.isEmpty())
            {
                ret.add(toAdd);
            }
        }
        return ret;
    }

    private ItemStack parseRecipeInput(Object input)
    {
        if (input instanceof Ingredient)
        {
            for (ItemStack obj : ((Ingredient) input).getMatchingStacks())
            {
                ItemStack ret = parseRecipeInput(obj);
                if (ret != null)
                {
                    return ret;
                }
            }
        }
//        else if (input instanceof ItemStack)
//        {
//            ItemStack stack = (ItemStack) input;
//            if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE)
//            {
//                return new ItemStack(stack.getItem(), stack.getCount(), 0);
//            }
//            else
//            {
//                return stack.copy();
//            }
//        }
//        else if (input instanceof String)
//        {
//            List<ItemStack> stacks = OreDictionary.getOres((String) input);
//            if (stacks.isEmpty())
//            {
//                return null;
//            }
//            for (ItemStack stack : stacks)
//            {
//                if (isSalvage(stack))
//                {
//                    return stack.copy();
//                }
//            }
//            return stacks.get(0) == null ? null : stacks.get(0).copy();
//        } TODO Deconstructor recipes
        else if (input instanceof Iterable)
        {
            for (Object obj : (Iterable) input)
            {
                ItemStack ret = parseRecipeInput(obj);
                if (ret != null)
                {
                    return ret;
                }
            }
        }

        return null;
    }

    private List<ItemStack> toItemStackList(Collection<ItemStack> inputs)
    {
        List<ItemStack> ret = new LinkedList<>();
        for (ItemStack o : inputs)
        {
            if (o != null && !o.isEmpty())
            {
                ret.add(o.copy());
            }
        }
        return ret;
    }

    private void addToOutputMatrix(ItemStack stack)
    {
        for (int i = 2; i < 11; i++)
        {
            if (this.getInventory().get(i).isEmpty())
            {
                this.getInventory().set(i, stack);
                return;
            }
            if (!(ItemStack.areItemsEqual(stack, this.getInventory().get(i))))
            {
                continue;
            }
            int size = this.getInventory().get(i).getCount();
            if (size + stack.getCount() < this.getInventoryStackLimit())
            {
                this.getInventory().get(i).grow(stack.getCount());
                return;
            }
            this.getInventory().get(i).setCount(this.getInventoryStackLimit());
            stack.shrink(this.getInventoryStackLimit() - size);
        }
        GCCoreUtil.spawnItem(this.world, this.getPos(), stack);
    }

    @Override
    public void read(CompoundNBT par1NBTTagCompound)
    {
        super.read(par1NBTTagCompound);
        this.processTicks = par1NBTTagCompound.getInt("smeltingTicks");

        this.readMachineSidesFromNBT(par1NBTTagCompound);  //Needed by IMachineSides
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putInt("smeltingTicks", this.processTicks);

        this.addMachineSidesToNBT(nbt);  //Needed by IMachineSides
        return nbt;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
    {
        if (slotID == 0)
        {
            return itemStack != null && !itemStack.isEmpty() && ItemElectricBase.isElectricItem(itemStack.getItem());
        }

        return slotID == 1;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        if (side == Direction.DOWN)
        {
            return new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10};
        }

        return new int[]{1};
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack par2ItemStack, Direction par3)
    {
        return slotID >= 2;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.processTicks > 0;
    }

    @Override
    public Direction getFront()
    {
        return BlockMachineBase.getFront(this.world.getBlockState(getPos()));
    }

    @Override
    public Direction getElectricInputDirection()
    {
        switch (this.getSide(MachineSide.ELECTRIC_IN))
        {
        case RIGHT:
            return getFront().rotateYCCW();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return Direction.UP;
        case BOTTOM:
            return Direction.DOWN;
        case LEFT:
        default:
            return getFront().rotateY();
        }
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    //------------------
    //Added these methods and field to implement IMachineSides properly 
    //------------------
    @Override
    public MachineSide[] listConfigurableSides()
    {
        return new MachineSide[]{MachineSide.ELECTRIC_IN};
    }

    @Override
    public Face[] listDefaultFaces()
    {
        return new Face[]{Face.LEFT};
    }

    private MachineSidePack[] machineSides;

    @Override
    public synchronized MachineSidePack[] getAllMachineSides()
    {
        if (this.machineSides == null)
        {
            this.initialiseSides();
        }

        return this.machineSides;
    }

    @Override
    public void setupMachineSides(int length)
    {
        this.machineSides = new MachineSidePack[length];
    }

    @Override
    public void onLoad()
    {
        this.clientOnLoad();
    }

    @Override
    public IMachineSidesProperties getConfigurationType()
    {
        return IMachineSidesProperties.TWOFACES_HORIZ;
    }
    //------------------END OF IMachineSides implementation

    @Override
    public Container createMenu(int containerId, PlayerInventory playerInv, PlayerEntity player)
    {
        return new ContainerDeconstructor(containerId, playerInv, this);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.deconstructor");
    }
}
