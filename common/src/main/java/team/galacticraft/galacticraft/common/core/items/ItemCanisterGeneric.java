package team.galacticraft.galacticraft.common.core.items;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import team.galacticraft.galacticraft.common.compat.PlatformSpecific;
import team.galacticraft.galacticraft.common.compat.component.ComponentProvidingItem;
import team.galacticraft.galacticraft.common.compat.component.ComponentWrapper;
import team.galacticraft.galacticraft.common.compat.component.GCApiComponents;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.common.core.util.CompatibilityManager;

import java.util.Collections;
import java.util.function.Predicate;

public abstract class ItemCanisterGeneric extends ComponentProvidingItem
{
    public final static Fraction VALID_CAPACITY = Fraction.of(1, 1);
//    public final static int EMPTY_CAPACITY = VALID_CAPACITY + 1; // One more than bucket
    private static final boolean isTELoaded = CompatibilityManager.isTELoaded();

    private Predicate<FluidStack> allowed = s -> true;

    public ItemCanisterGeneric(Item.Properties builder)
    {
        super(builder/*, VALID_CAPACITY*/);
        builder.durability(VALID_CAPACITY.intValue() * 1000);

//        this.setMaxDamage(ItemCanisterGeneric.EMPTY);
//        this.setMaxStackSize(1);
//        this.setNoRepair();
//        this.setUnlocalizedName(assetName);
//        this.setContainerItem(GCItems.oilCanister);
//        this.setHasSubtypes(true);
//        this.addPropertyGetter(new ResourceLocation(Constants.MOD_ID_CORE, "fluid_level"), new ItemPropertyFunction() {
//            @Override
//            public float call(ItemStack stack, Level world, LivingEntity entity) {
//                float damagePercentage;
//                if (stack.getDamageValue() == EMPTY_CAPACITY) {
//                    damagePercentage = 0.0F;
//                } else {
//                    damagePercentage = 1.0F - (stack.getDamageValue() - 1) / (float)VALID_CAPACITY;
//                }
//                return Math.min(Math.max(damagePercentage, 0.0F), 1.0F);
//            }
//        }); //todo(marcus): forge method
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

//    @Override
//    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt)
//    {
//        return new ItemCanisterGenericHandler(stack);
//    }

    @Override
    @Environment(EnvType.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public ComponentWrapper<?>[] getComponents() {
        return new ComponentWrapper[]{GCApiComponents.getTank()};
    }

    @Override
    public Object[] createInstances() {
        return new Object[]{PlatformSpecific.createFluidInv(1, VALID_CAPACITY, Collections.singletonList(allowed))};
    }
//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            list.add(new ItemStack(this, 1, 1));
//        }
//    }

//    @Override
//    public ItemStack getContainerItem(ItemStack itemStack)
//    {
//        //Workaround for strange behaviour in TE Transposer
//        if (isTELoaded)
//        {
//            if (JavaUtil.instance.isCalledBy("thermalexpansion.block.machine.TileTransposer"))
//            {
//                return ItemStack.EMPTY;
//            }
//        }
//
//        ItemStack stack = new ItemStack(this.getRecipeRemainder(), 1);
//        stack.setDamageValue(ItemCanisterGeneric.EMPTY_CAPACITY);
//        return stack;
//    }

//    @Override
//    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected)
//    {
//        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
//
//        if (ItemCanisterGeneric.EMPTY_CAPACITY == stack.getDamageValue())
//        {
//            if (stack.getItem() != GCItems.oilCanister)
//            {
//                this.replaceEmptyCanisterItem(stack, GCItems.oilCanister);
//            }
//            stack.setTag(null);
//        }
//        else if (stack.getDamageValue() <= 0)
//        {
//            stack.setDamageValue(1);
//        }
//    }

    public void setAllowed(Predicate<FluidStack> function)
    {
        this.allowed = function;
    }

    public Predicate<FluidStack> getAllowed()
    {
        return this.allowed;
    }

//    public int fill(ItemStack container, FluidStack resource, ActionType action)
//    {
//        if (resource == null || resource.isEmpty() || resource.getAmount() <= 0 || container == null || container.getDamageValue() <= 1 || !(container.getItem() instanceof ItemCanisterGeneric))
//        {
//            return 0;
//        }
//        ResourceLocation fluidName = resource.getFluid().getRegistryName();
//
//        int capacityPlusOne = container.getDamageValue();
//        if (capacityPlusOne <= 1)
//        {
//            if (capacityPlusOne < 1)
//            {
//                //It shouldn't be possible, but just in case, set this to a proper filled item
//                container.setDamageValue(1);
//            }
//            return 0;
//        }
//        if (capacityPlusOne >= ItemCanisterGeneric.EMPTY_CAPACITY)
//        {
//            //Empty canister - find a new canister to match the fluid
//            for (ItemCanisterGeneric i : GCItems.canisterTypes)
//            {
//                if (fluidName == i.allowedFluid)
//                {
//                    if (action.simulate())
//                    {
//                        return Math.min(resource.getAmount(), this.capacity);
//                    }
//
//                    this.replaceEmptyCanisterItem(container, i);
//                    break;
//                }
//            }
//            if (capacityPlusOne > ItemCanisterGeneric.EMPTY_CAPACITY)
//            {
//                //It shouldn't be possible, but just in case, set this to a proper empty item
//                capacityPlusOne = ItemCanisterGeneric.EMPTY_CAPACITY;
//                container.setDamageValue(capacityPlusOne);
//            }
//        }
//
//        if (fluidName == ((ItemCanisterGeneric) container.getItem()).allowedFluid)
//        {
//            int added = Math.min(resource.getAmount(), capacityPlusOne - 1);
//            if (action.execute() && added > 0)
//            {
//                container.setDamageValue(Math.max(1, container.getDamageValue() - added));
//            }
//            return added;
//        }
//
//        return 0;
//    }
//
//    public FluidStack drain(ItemStack container, int maxDrain, ActionType action)
//    {
//        if (this.allowedFluid == null || container.getDamageValue() >= ItemCanisterGeneric.EMPTY_CAPACITY)
//        {
//            return null;
//        }
//
//        FluidStack used = this.getFluid(container);
//        if (used != null && used.getAmount() > maxDrain)
//        {
//            used.setAmount(maxDrain);
//        }
//        if (action.execute() && used != null && used.getAmount() > 0)
//        {
//            this.setNewDamage(container, container.getDamageValue() + used.getAmount());
//        }
//        return used;
//    }
//
//    protected void setNewDamage(ItemStack container, int newDamage)
//    {
//        newDamage = Math.min(newDamage, ItemCanisterGeneric.EMPTY_CAPACITY);
//        container.setDamageValue(newDamage);
//        if (newDamage == ItemCanisterGeneric.EMPTY_CAPACITY)
//        {
//            if (container.getItem() != GCItems.oilCanister)
//            {
//                this.replaceEmptyCanisterItem(container, GCItems.oilCanister);
//                return;
//            }
//        }
//    }
//
//    private void replaceEmptyCanisterItem(ItemStack container, Item newItem)
//    {
//        try
//        {
//            Class itemStack = container.getClass();
//            Field itemId = itemStack.getDeclaredField(GCCoreUtil.isDeobfuscated() ? "item" : "field_151002_e");
//            itemId.setAccessible(true);
//            itemId.set(container, newItem);
//            Method forgeInit = itemStack.getDeclaredMethod("forgeInit");
//            forgeInit.setAccessible(true);
//            forgeInit.invoke(container);
//        }
//        catch (Exception ignore)
//        {
//        }
//    }
//
//    public FluidStack getFluid(ItemStack container)
//    {
//        ResourceLocation fluidName = ((ItemCanisterGeneric) container.getItem()).allowedFluid;
//        if (fluidName == null || container.getDamageValue() >= ItemCanisterGeneric.EMPTY_CAPACITY)
//        {
//            return null;
//        }
//
//        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidName);
//        if (fluid == null)
//        {
//            return null;
//        }
//
//        return FluidStack.create(fluid, ItemCanisterGeneric.EMPTY_CAPACITY - container.getDamageValue());
//    }
}
