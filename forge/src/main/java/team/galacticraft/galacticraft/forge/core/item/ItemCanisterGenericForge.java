package team.galacticraft.galacticraft.forge.core.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.ItemFluidContainer;

public abstract class ItemCanisterGenericForge extends ItemFluidContainer
{
    public final static int VALID_CAPACITY = 1000;
    public final static int EMPTY_CAPACITY = VALID_CAPACITY + 1; // One more than bucket
//    private static final boolean isTELoaded = CompatibilityManager.isTELoaded();

    private ResourceLocation allowedFluid = null;

    public ItemCanisterGenericForge(Item.Properties builder)
    {
        super(builder, VALID_CAPACITY);
//        this.setMaxDamage(ItemCanisterGeneric.EMPTY);
//        this.setMaxStackSize(1);
//        this.setNoRepair();
//        this.setUnlocalizedName(assetName);
//        this.setContainerItem(GCItems.oilCanister);
//        this.setHasSubtypes(true);
        this.addProperty(new ResourceLocation(Constants.MOD_ID_CORE, "fluid_level"), new ItemPropertyFunction()
        {
            @Override
            public float call(ItemStack stack, World world, LivingEntity entity)
            {
                float damagePercentage;
                if (stack.getDamageValue() == EMPTY_CAPACITY)
                {
                    damagePercentage = 0.0F;
                }
                else
                {
                    damagePercentage = 1.0F - (stack.getDamageValue() - 1) / (float) VALID_CAPACITY;
                }
                return Math.min(Math.max(damagePercentage, 0.0F), 1.0F);
            }
        });
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt)
    {
        return new ItemCanisterGenericHandler(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
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

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        //Workaround for strange behaviour in TE Transposer
//        if (isTELoaded)
//        {
//            if (JavaUtil.instance.isCalledBy("thermalexpansion.block.machine.TileTransposer"))
//            {
//                return ItemStack.EMPTY;
//            }
//        }

        ItemStack stack = new ItemStack(this.getRecipeRemainder(), 1);
        stack.setDamageValue(ItemCanisterGeneric.EMPTY_CAPACITY);
        return stack;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

        if (ItemCanisterGenericForge.EMPTY_CAPACITY == stack.getDamageValue())
        {
            if (stack.getItem() != GCItems.oilCanister)
            {
                this.replaceEmptyCanisterItem(stack, GCItems.oilCanister);
            }
            stack.setTag(null);
        }
        else if (stack.getDamageValue() <= 0)
        {
            stack.setDamageValue(1);
        }
    }

    public ResourceLocation getAllowedFluid()
    {
        return this.allowedFluid;
    }

    public void setAllowedFluid(ResourceLocation name)
    {
        this.allowedFluid = name;
    }

    public int fill(ItemStack container, FluidStack resource, ActionType action)
    {
        if (resource == null || resource.isEmpty() || resource.getAmount() <= 0 || container == null || container.getDamageValue() <= 1 || !(container.getItem() instanceof ItemCanisterGeneric))
        {
            return 0;
        }
        ResourceLocation fluidName = resource.getFluid().getRegistryName();

        int capacityPlusOne = container.getDamageValue();
        if (capacityPlusOne <= 1)
        {
            if (capacityPlusOne < 1)
            {
                //It shouldn't be possible, but just in case, set this to a proper filled item
                container.setDamageValue(1);
            }
            return 0;
        }
        if (capacityPlusOne >= ItemCanisterGeneric.EMPTY_CAPACITY)
        {
            //Empty canister - find a new canister to match the fluid
            for (ItemCanisterGeneric i : GCItems.canisterTypes)
            {
                if (fluidName == i.allowedFluid)
                {
                    if (action.simulate())
                    {
                        return Math.min(resource.getAmount(), this.capacity);
                    }

                    this.replaceEmptyCanisterItem(container, i);
                    break;
                }
            }
            if (capacityPlusOne > ItemCanisterGeneric.EMPTY_CAPACITY)
            {
                //It shouldn't be possible, but just in case, set this to a proper empty item
                capacityPlusOne = ItemCanisterGeneric.EMPTY_CAPACITY;
                container.setDamageValue(capacityPlusOne);
            }
        }

        if (fluidName == ((ItemCanisterGeneric) container.getItem()).allowedFluid)
        {
            int added = Math.min(resource.getAmount(), capacityPlusOne - 1);
            if (action.execute() && added > 0)
            {
                container.setDamageValue(Math.max(1, container.getDamageValue() - added));
            }
            return added;
        }

        return 0;
    }

    public FluidStack drain(ItemStack container, int maxDrain, ActionType action)
    {
        if (this.allowedFluid == null || container.getDamageValue() >= ItemCanisterGeneric.EMPTY_CAPACITY)
        {
            return null;
        }

        FluidStack used = this.getFluid(container);
        if (used != null && used.getAmount() > maxDrain)
        {
            used.setAmount(maxDrain);
        }
        if (action.execute() && used != null && used.getAmount() > 0)
        {
            this.setNewDamage(container, container.getDamageValue() + used.getAmount());
        }
        return used;
    }

    protected void setNewDamage(ItemStack container, int newDamage)
    {
        newDamage = Math.min(newDamage, ItemCanisterGeneric.EMPTY_CAPACITY);
        container.setDamageValue(newDamage);
        if (newDamage == ItemCanisterGeneric.EMPTY_CAPACITY)
        {
            if (container.getItem() != GCItems.oilCanister)
            {
                this.replaceEmptyCanisterItem(container, GCItems.oilCanister);
                return;
            }
        }
    }

    private void replaceEmptyCanisterItem(ItemStack container, Item newItem)
    {
        try
        {
            Class itemStack = container.getClass();
            Field itemId = itemStack.getDeclaredField(GCCoreUtil.isDeobfuscated() ? "item" : "field_151002_e");
            itemId.setAccessible(true);
            itemId.set(container, newItem);
            Method forgeInit = itemStack.getDeclaredMethod("forgeInit");
            forgeInit.setAccessible(true);
            forgeInit.invoke(container);
        }
        catch (Exception ignore)
        {
        }
    }

    public FluidStack getFluid(ItemStack container)
    {
        ResourceLocation fluidName = ((ItemCanisterGeneric) container.getItem()).allowedFluid;
        if (fluidName == null || container.getDamageValue() >= ItemCanisterGeneric.EMPTY_CAPACITY)
        {
            return null;
        }

        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidName);
        if (fluid == null)
        {
            return null;
        }

        return FluidStack.create(fluid, ItemCanisterGeneric.EMPTY_CAPACITY - container.getDamageValue());
    }
}
