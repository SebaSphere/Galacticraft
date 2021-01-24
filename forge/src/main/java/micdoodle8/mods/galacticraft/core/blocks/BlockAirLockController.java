package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.client.gui.container.GuiAirLockController;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import javax.annotation.Nullable;

public class BlockAirLockController extends BlockAdvancedTile implements IShiftDescription, ISortable
{
    public BlockAirLockController(Properties builder)
    {
        super(builder);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(worldIn, pos, state, placer, stack);

        BlockEntity tile = worldIn.getBlockEntity(pos);

        if (tile instanceof TileEntityAirLockController && placer instanceof Player)
        {
            ((TileEntityAirLockController) tile).ownerName = PlayerUtil.getName(((Player) placer));
        }
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityAirLockController();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side)
    {
        return true;
    }

    @Override
    public InteractionResult onMachineActivated(Level world, BlockPos pos, BlockState state, Player playerIn, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        BlockEntity tile = world.getBlockEntity(pos);

        if (tile instanceof TileEntityAirLockController && world.isClientSide)
        {
            Minecraft.getInstance().setScreen(new GuiAirLockController((TileEntityAirLockController) tile));
//            NetworkHooks.openGui((ServerPlayerEntity) playerIn, getContainer(state, world, pos), buf -> buf.writeBlockPos(pos));
//            playerIn.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }


    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock())
        {
            BlockEntity tile = worldIn.getBlockEntity(pos);

            if (tile instanceof TileEntityAirLockController)
            {
                ((TileEntityAirLockController) tile).unsealAirLock();
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    @Nullable
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos)
    {
        return null;
    }

    @Override
    public String getShiftDescription(ItemStack item)
    {
        return GCCoreUtil.translate(this.getDescriptionId() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack item)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }
}
