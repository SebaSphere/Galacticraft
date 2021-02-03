package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlockNames;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntitySolarArrayModule extends TileEntitySolarTransmitter
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + VenusBlockNames.SOLAR_ARRAY_MODULE)
    public static BlockEntityType<TileEntitySolarArrayModule> TYPE;

    public TileEntitySolarArrayModule()
    {
        super(TYPE);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        return type == NetworkType.SOLAR_MODULE && direction.getAxis() != Direction.Axis.Y;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return false;
    }

    @Override
    public CompoundTag save(CompoundTag compound)
    {
        return super.save(compound);
    }

    @Override
    public void load(CompoundTag compound)
    {
        super.load(compound);
    }

    @Override
    public boolean canTransmit()
    {
        return true;
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }
}
