package micdoodle8.mods.galacticraft.core.energy.tile;

import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public abstract class TileBaseUniversalConductor extends TileBaseConductor implements net.minecraftforge.energy.IEnergyStorage
{
    protected boolean isAddedToEnergyNet;
    private final float IC2surplusJoules = 0F;

    public TileBaseUniversalConductor(BlockEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void onNetworkChanged()
    {
    }

    @Override
    public BlockEntity[] getAdjacentConnections()
    {
        return EnergyUtil.getAdjacentPowerConnections(this);
    }

    //Update ticks only required if IC2 is loaded
//    @RuntimeInterface(clazz = "net.minecraft.util.ITickable", modID = CompatibilityManager.modidIC2, deobfName = "func_73660_a")
//    public void update()
//    {
//        if (!this.isAddedToEnergyNet)
//        {
//            if (!this.world.isRemote)
//            {
//            	this.initIC();
//            }
//
//            this.isAddedToEnergyNet = true;
//        }
//    }

//    @Override
//    public void invalidate()
//    {
//        this.IC2surplusJoules = 0F;
//        this.unloadTileIC2();
//        super.invalidate();
//    }
//
//    @Override
//    public void onChunkUnload()
//    {
//        this.unloadTileIC2();
//        super.onChunkUnload();
//    }

//    protected void initIC()
//    {
//        if (EnergyConfigHandler.isIndustrialCraft2Loaded() && !this.world.isRemote)
//        {
//            try
//            {
//                Object o = CompatibilityManager.classIC2tileEventLoad.getConstructor(IEnergyTile.class).newInstance(this);
//
//                if (o != null && o instanceof Event)
//                {
//                    MinecraftForge.EVENT_BUS.post((Event) o);
//                }
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void unloadTileIC2()
//    {
//        if (this.isAddedToEnergyNet && this.world != null)
//        {
//            if (!this.world.isRemote && EnergyConfigHandler.isIndustrialCraft2Loaded())
//            {
//                try
//                {
//                    Object o = CompatibilityManager.classIC2tileEventUnload.getConstructor(IEnergyTile.class).newInstance(this);
//
//                    if (o != null && o instanceof Event)
//                    {
//                        MinecraftForge.EVENT_BUS.post((Event) o);
//                    }
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//
//            this.isAddedToEnergyNet = false;
//        }
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = CompatibilityManager.modidIC2)
//    public double getDemandedEnergy()
//    {
//        if (this.getNetwork() == null)
//        {
//            return 0.0;
//        }
//
//        if (this.IC2surplusJoules < 0.001F)
//        {
//            this.IC2surplusJoules = 0F;
//            return this.getNetwork().getRequest(this) / EnergyConfigHandler.IC2_RATIO;
//        }
//
//        this.IC2surplusJoules = this.getNetwork().produce(this.IC2surplusJoules, true, 1, this);
//        if (this.IC2surplusJoules < 0.001F)
//        {
//            this.IC2surplusJoules = 0F;
//            return this.getNetwork().getRequest(this) / EnergyConfigHandler.IC2_RATIO;
//        }
//        return 0D;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = CompatibilityManager.modidIC2)
//    public double injectEnergy(Direction directionFrom, double amount, double voltage)
//    {
//        TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.world, directionFrom);
//        int tier = ((int) voltage > 120) ? 2 : 1;
//        if (tile instanceof IEnergySource && ((IEnergySource) tile).getOfferedEnergy() >= 128)
//        {
//            tier = 2;
//        }
//        float convertedEnergy = (float) amount * EnergyConfigHandler.IC2_RATIO;
//        float surplus = this.getNetwork().produce(convertedEnergy, true, tier, this, tile);
//
//        if (surplus >= 0.001F)
//        {
//            this.IC2surplusJoules = surplus;
//        }
//        else
//        {
//            this.IC2surplusJoules = 0F;
//        }
//
//        return 0D;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = CompatibilityManager.modidIC2)
//    public int getSinkTier()
//    {
//        return 3;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergyAcceptor", modID = CompatibilityManager.modidIC2)
//    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, Direction side)
//    {
//        //Don't add connection to IC2 grid if it's a Galacticraft tile
//        if (emitter instanceof IElectrical || emitter instanceof IConductor)
//        {
//            return false;
//        }
//
//        //Don't make connection with IC2 wires [don't want risk of multiple connections + there is a graphical glitch in IC2]
//        try
//        {
//            if (EnergyUtil.clazzIC2Cable != null && EnergyUtil.clazzIC2Cable.isInstance(emitter))
//            {
//                return false;
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        return true;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergyEmitter", modID = CompatibilityManager.modidIC2)
//    public boolean emitsEnergyTo(IEnergyAcceptor receiver, Direction side)
//    {
//        //Don't add connection to IC2 grid if it's a Galacticraft tile
//        if (receiver instanceof IElectrical || receiver instanceof IConductor)
//        {
//            return false;
//        }
//
//        //Don't make connection with IC2 wires [don't want risk of multiple connections + there is a graphical glitch in IC2]
//        try
//        {
//            if (EnergyUtil.clazzIC2Cable != null && EnergyUtil.clazzIC2Cable.isInstance(receiver))
//            {
//                return false;
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        return true;
//    }

    //ForgeEnergy
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        if (this.getNetwork() == null || EnergyConfigHandler.disableFEInput.get())
        {
            return 0;
        }

        float receiveGC = (float) (maxReceive * EnergyConfigHandler.RF_RATIO.get());
        float sentGC = receiveGC - this.getNetwork().produce(receiveGC, !simulate, 1);
        return Mth.floor(sentGC / EnergyConfigHandler.RF_RATIO.get());
    }

    //ForgeEnergy OR BuildCraft (method name clash!)
    @Override
    public boolean canReceive()
    {
        return !EnergyConfigHandler.disableBuildCraftInput.get() || !EnergyConfigHandler.disableFEInput.get();
    }

    //ForgeEnergy
    @Override
    public int getEnergyStored()
    {
        return 0;
    }

    //ForgeEnergy
    @Override
    public int getMaxEnergyStored()
    {
        if (this.getNetwork() == null || EnergyConfigHandler.disableFEInput.get())
        {
            return 0;
        }

        return Mth.floor(this.getNetwork().getRequest(this) / EnergyConfigHandler.RF_RATIO.get());
    }

    //ForgeEnergy
    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        return 0;
    }

    //ForgeEnergy
    @Override
    public boolean canExtract()
    {
        return false;
    }

    //Buildcraft 7
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IMjReceiver", modID = CompatibilityManager.modBCraftEnergy)
//    public long getPowerRequested()
//    {
//        if (this.getNetwork() == null || EnergyConfigHandler.disableBuildCraftInput)
//        {
//            return 0L;
//        }
//
//        return (long) (this.getNetwork().getRequest(this) / EnergyConfigHandler.BC8_INTERNAL_RATIO);
//    }
//
//    //Buildcraft 7
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IMjReceiver", modID = CompatibilityManager.modBCraftEnergy)
//    public long receivePower(long microJoules, boolean simulate)
//    {
//        if (this.getNetwork() == null || EnergyConfigHandler.disableBuildCraftInput)
//        {
//            return microJoules;
//        }
//        float receiveGC = microJoules * EnergyConfigHandler.BC8_INTERNAL_RATIO;
//        float sentGC = receiveGC - this.getNetwork().produce(receiveGC, !simulate, 1);
//        return (long) (sentGC / EnergyConfigHandler.BC8_INTERNAL_RATIO);
//    }
//
//    //Buildcraft 7
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IMjReceiver", modID = CompatibilityManager.modBCraftEnergy)
//    public boolean canConnect(@Nonnull IMjConnector other)
//    {
//        return true;
//    }
//
//    @RuntimeInterface(clazz = "cofh.redstoneflux.api.IEnergyReceiver", modID = "")
//    public int receiveEnergy(Direction from, int maxReceive, boolean simulate)
//    {
//        if (this.getNetwork() == null)
//        {
//            return 0;
//        }
//        float receiveGC = maxReceive * EnergyConfigHandler.RF_RATIO;
//        float sentGC = receiveGC - this.getNetwork().produce(receiveGC, !simulate, 1);
//        return MathHelper.floor(sentGC / EnergyConfigHandler.RF_RATIO);
//    }
//
//    @RuntimeInterface(clazz = "cofh.redstoneflux.api.IEnergyProvider", modID = "")
//    public int extractEnergy(Direction from, int maxExtract, boolean simulate)
//    {
//        return 0;
//    }
//
//    @RuntimeInterface(clazz = "cofh.redstoneflux.api.IEnergyHandler", modID = "")
//    public boolean canConnectEnergy(Direction from)
//    {
//        //Do not form wire-to-wire connections with EnderIO conduits
//        TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.world, from);
//        try
//        {
//            if (EnergyUtil.clazzMekCable != null && EnergyUtil.clazzMekCable.isInstance(tile))
//            {
//                return false;
//            }
//        }
//        catch (Exception e)
//        {
//        }
//        return true;
//    }
//
//    @RuntimeInterface(clazz = "cofh.redstoneflux.api.IEnergyHandler", modID = "")
//    public int getEnergyStored(Direction from)
//    {
//        return 0;
//    }
//
//    @RuntimeInterface(clazz = "cofh.redstoneflux.api.IEnergyHandler", modID = "")
//    public int getMaxEnergyStored(Direction from)
//    {
//        if (this.getNetwork() == null)
//        {
//            return 0;
//        }
//
//        return MathHelper.floor(this.getNetwork().getRequest(this) / EnergyConfigHandler.RF_RATIO);
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = CompatibilityManager.modidMekanism)
//    public double transferEnergyToAcceptor(Direction side, double amount)
//    {
//        if (EnergyConfigHandler.disableMekanismInput)
//        {
//            return 0.0;
//        }
//
//        if (!this.canReceiveEnergy(LogicalSide))
//        {
//            return 0.0;
//        }
//
//        return amount - this.getNetwork().produce((float) amount * EnergyConfigHandler.MEKANISM_RATIO, true, 1, this) / EnergyConfigHandler.MEKANISM_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = CompatibilityManager.modidMekanism)
//    public double acceptEnergy(Direction side, double amount, boolean simulate)
//    {
//        if (EnergyConfigHandler.disableMekanismInput)
//        {
//            return 0.0;
//        }
//
//        if (!this.canReceiveEnergy(LogicalSide))
//        {
//            return 0.0;
//        }
//
//        return amount - this.getNetwork().produce((float) amount * EnergyConfigHandler.MEKANISM_RATIO, !simulate, 1, this) / EnergyConfigHandler.MEKANISM_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = CompatibilityManager.modidMekanism)
//    public boolean canReceiveEnergy(Direction side)
//    {
//        if (this.getNetwork() == null)
//        {
//            return false;
//        }
//
//        TileEntity te = new BlockVec3(this).getTileEntityOnSide(this.world, side);
//        try
//        {
//            if (EnergyUtil.clazzMekCable != null && EnergyUtil.clazzMekCable.isInstance(te))
//            {
//                return false;
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        return true;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = CompatibilityManager.modidMekanism)
//    public double getEnergy()
//    {
//        return 0;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = CompatibilityManager.modidMekanism)
//    public void setEnergy(double energy)
//    {
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = CompatibilityManager.modidMekanism)
//    public double getMaxEnergy()
//    {
//        if (this.getNetwork() == null)
//        {
//            return 0;
//        }
//        return this.getNetwork().getRequest(this) / EnergyConfigHandler.MEKANISM_RATIO;
//    }

//    @Override
//    public boolean hasCapability(Capability<?> capability, Direction facing)
//    {
//        if (capability == CapabilityEnergy.ENERGY)
//        {
//            return true;
//        }
//        if (EnergyConfigHandler.isBuildcraftLoaded() && (capability == MjAPI.CAP_RECEIVER || capability == MjAPI.CAP_CONNECTOR))
//        {
//            TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.world, facing);
//            return !(CompatibilityManager.classBCTransportPipeTile.isInstance(tile));
//        }
//        return super.hasCapability(capability, facing);
//    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing)
    {
//        if (capability == CapabilityEnergy.ENERGY)
//        {
//            if (EnergyUtil.clazzEnderIOCable == null) return (T) this;
//            TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.world, facing);
//            return EnergyUtil.clazzEnderIOCable.isInstance(tile) ? null : (T) this;
//        }
//        if (EnergyConfigHandler.isBuildcraftLoaded() && (capability == MjAPI.CAP_RECEIVER || capability == MjAPI.CAP_CONNECTOR))
//        {
//            TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.world, facing);
//            if (!(CompatibilityManager.classBCTransportPipeTile.isInstance(tile)))
//            {
//                return (T) this;
//            }
//        }
        return super.getCapability(capability, facing);
    }
}
