package team.galacticraft.galacticraft.common.core.energy.tile;

import team.galacticraft.galacticraft.core.inventory.IInventoryDefaults;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;

public abstract class TileBaseElectricBlockWithInventory extends TileBaseElectricBlock implements IInventoryDefaults
{
    public TileBaseElectricBlockWithInventory(BlockEntityType<?> type)
    {
        super(type);
    }

//    @Override
//    public Direction getElectricInputDirection()
//    {
//        return Direction.byHorizontalIndex(((this.getBlockMetadata() & 3) + 1) % 4);
//    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getItem(0);
    }
}
