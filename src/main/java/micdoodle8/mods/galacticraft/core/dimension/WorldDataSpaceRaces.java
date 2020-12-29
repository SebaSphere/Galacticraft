package micdoodle8.mods.galacticraft.core.dimension;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

public class WorldDataSpaceRaces extends WorldSavedData
{
    public static final String saveDataID = "GCSpaceRaceData";

    public WorldDataSpaceRaces()
    {
        super(WorldDataSpaceRaces.saveDataID);
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        SpaceRaceManager.loadSpaceRaces(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        return SpaceRaceManager.saveSpaceRaces(nbt);
    }

    public static WorldDataSpaceRaces initWorldData(ServerWorld world)
    {
        return world.getSavedData().getOrCreate(WorldDataSpaceRaces::new, saveDataID);
    }

    @Override
    public boolean isDirty()
    {
        return true;
    }
}
