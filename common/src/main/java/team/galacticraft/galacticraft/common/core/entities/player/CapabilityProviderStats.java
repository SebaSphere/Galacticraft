package team.galacticraft.galacticraft.common.core.entities.player;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.lang.ref.WeakReference;

public class CapabilityProviderStats implements ICapabilitySerializable<CompoundTag>
{
    private ServerPlayer owner;
    private final LazyOptional<GCPlayerStats> holder = LazyOptional.create(() -> new StatsCapability(new WeakReference<>(this.owner)));

    public CapabilityProviderStats(ServerPlayer owner)
    {
        this.owner = owner;
    }

//    @Override
//    public boolean hasCapability(Capability<?> capability, Direction facing)
//    {
//        return capability == GCCapabilities.GC_STATS_CAPABILITY;
//    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        if (cap == GCCapabilities.GC_STATS_CAPABILITY)
        {
            return GCCapabilities.GC_STATS_CAPABILITY.orEmpty(cap, holder);
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        this.holder.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")).saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        this.holder.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")).loadNBTData(nbt);
    }
}
