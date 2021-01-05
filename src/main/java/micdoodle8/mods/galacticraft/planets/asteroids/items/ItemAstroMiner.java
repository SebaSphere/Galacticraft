package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.dimension.DimensionSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFake;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.AstroMinerEntity;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemAstroMiner extends Item implements IHoldableItem, ISortable
{
    public ItemAstroMiner(Item.Properties properties)
    {
        super(properties);
//        this.setMaxDamage(0);
//        this.setMaxStackSize(1);
//        this.setUnlocalizedName(assetName);
        //this.setTextureName("arrow");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }


    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
    {
        TileEntity tile = null;
        PlayerEntity playerIn = context.getPlayer();
        World worldIn = context.getWorld();
        BlockPos pos = context.getPos();

        if (playerIn == null)
        {
            return ActionResultType.PASS;
        }
        else
        {
            final Block id = worldIn.getBlockState(pos).getBlock();

            if (id == GCBlocks.MULTI_BLOCK)
            {
                tile = worldIn.getTileEntity(pos);

                if (tile instanceof TileEntityFake)
                {
                    tile = ((TileEntityFake) tile).getMainBlockTile();
                }
            }

            if (id == AsteroidBlocks.FULL_ASTRO_MINER_BASE)
            {
                tile = worldIn.getTileEntity(pos);
            }

            if (tile instanceof TileEntityMinerBase)
            {
                //Don't open GUI on client
                if (worldIn.isRemote)
                {
                    return ActionResultType.FAIL;
                }

                if (worldIn.dimension instanceof DimensionSpaceStation)
                {
                    playerIn.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.message.astro_miner7.fail")));
                    return ActionResultType.FAIL;
                }

                if (((TileEntityMinerBase) tile).getLinkedMiner() != null)
                {
                    playerIn.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.message.astro_miner.fail")));
                    return ActionResultType.FAIL;
                }

                //Gives a chance for any loaded Astro Miner to link itself
                if (((TileEntityMinerBase) tile).ticks < 15)
                {
                    return ActionResultType.FAIL;
                }

                ServerPlayerEntity playerMP = (ServerPlayerEntity) playerIn;
                GCPlayerStats stats = GCPlayerStats.get(playerIn);

                int astroCount = stats.getAstroMinerCount();
                if (astroCount >= ConfigManagerPlanets.astroMinerMax.get() && (!playerIn.abilities.isCreativeMode))
                {
                    playerIn.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.message.astro_miner2.fail")));
                    return ActionResultType.FAIL;
                }

                if (!((TileEntityMinerBase) tile).spawnMiner(playerMP))
                {
                    playerIn.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.message.astro_miner1.fail") + " " + GCCoreUtil.translate(AstroMinerEntity.blockingBlock.toString())));
                    return ActionResultType.FAIL;
                }

                if (!playerIn.abilities.isCreativeMode)
                {
                    stats.setAstroMinerCount(stats.getAstroMinerCount() + 1);
                    playerIn.getHeldItem(context.getHand()).shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
//    {
//        //TODO
//    }

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
    public boolean shouldCrouch(PlayerEntity player)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}
