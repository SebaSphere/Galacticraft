package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.api.item.IHoldableItemCustom;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.entities.FlagEntity;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.block.SoundType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemFlag extends Item implements IHoldableItemCustom, ISortable
{
    public int placeProgress;

    public ItemFlag(Item.Properties properties)
    {
        super(properties);
//        this.setMaxDamage(0);
//        this.setMaxStackSize(1);
//        this.setUnlocalizedName(assetName);
        //this.setTextureName("arrow");
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entity, int timeLeft)
    {
        final int useTime = this.getUseDuration(stack) - timeLeft;

        boolean placed = false;

        if (!(entity instanceof PlayerEntity))
        {
            return;
        }

        PlayerEntity player = (PlayerEntity) entity;

        final RayTraceResult var12 = rayTrace(worldIn, player, RayTraceContext.FluidMode.ANY);

        float var7 = useTime / 20.0F;
        var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;

        if (var7 > 1.0F)
        {
            var7 = 1.0F;
        }

        if (var7 == 1.0F && var12 != null && var12.getType() == RayTraceResult.Type.BLOCK)
        {
            BlockRayTraceResult blockResult = (BlockRayTraceResult) var12;
            final BlockPos pos = blockResult.getPos();

            if (!worldIn.isRemote)
            {
                final FlagEntity flag = new FlagEntity(worldIn, pos.getX() + 0.5F, pos.getY() + 1.0F, pos.getZ() + 0.5F, (int) (entity.rotationYaw - 90));

                if (worldIn.getEntitiesWithinAABB(FlagEntity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 3, pos.getZ() + 1)).isEmpty())
                {
                    worldIn.addEntity(flag);
//                    flag.setType(stack.getDamage());
                    flag.setOwner(PlayerUtil.getName(player));
                    worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundType.METAL.getBreakSound(), SoundCategory.BLOCKS, SoundType.METAL.getVolume(), SoundType.METAL.getPitch() + 2.0F);
                    placed = true;
                }
                else
                {
                    entity.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.flag.already_placed")));
                }
            }

            if (placed)
            {
                final int var2 = this.getInventorySlotContainItem(player, stack);

                if (var2 >= 0 && !player.abilities.isCreativeMode)
                {
                    player.inventory.mainInventory.get(var2).shrink(1);
                }
            }
        }
    }

    private int getInventorySlotContainItem(PlayerEntity player, ItemStack stack)
    {
        for (int var2 = 0; var2 < player.inventory.mainInventory.size(); ++var2)
        {
            if (!player.inventory.mainInventory.get(var2).isEmpty() && player.inventory.mainInventory.get(var2).isItemEqual(stack))
            {
                return var2;
            }
        }

        return -1;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack)
    {
        return UseAction.NONE;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand)
    {
        playerIn.setActiveHand(hand);
        return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(hand));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

//    @Override
//    public String getUnlocalizedName(ItemStack itemStack)
//    {
//        return "item.flag";
//    }

    /*@Override
    public IIcon getIconFromDamage(int damage)
    {
        return super.getIconFromDamage(damage);
    }*/

    @Override
    public boolean shouldHoldLeftHandUp(PlayerEntity player)
    {
        return true;
    }

    @Override
    public boolean shouldHoldRightHandUp(PlayerEntity player)
    {
        return true;
    }

    @Override
    public Vector3 getLeftHandRotation(PlayerEntity player)
    {
        return new Vector3((float) Math.PI + 1.3F, 0.5F, (float) Math.PI / 5.0F);
    }

    @Override
    public Vector3 getRightHandRotation(PlayerEntity player)
    {
        return new Vector3((float) Math.PI + 1.3F, -0.5F, (float) Math.PI / 5.0F);
    }

    @Override
    public boolean shouldCrouch(PlayerEntity player)
    {
        return false;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}
