package team.galacticraft.galacticraft.common.core.wrappers;

import team.galacticraft.galacticraft.core.entities.player.GCPlayerHandler;
import team.galacticraft.galacticraft.core.util.PlayerUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class PlayerGearData
{
    private final Player player;
    private int mask;
    private int gear;
    private int leftTank;
    private int rightTank;
    private final int[] thermalPadding;
    private ResourceLocation parachute;
    private int frequencyModule;
    private int shieldController;

    public PlayerGearData(Player player)
    {
        this(player, GCPlayerHandler.GEAR_NOT_PRESENT, GCPlayerHandler.GEAR_NOT_PRESENT, GCPlayerHandler.GEAR_NOT_PRESENT, GCPlayerHandler.GEAR_NOT_PRESENT, GCPlayerHandler.GEAR_NOT_PRESENT,
                new int[]{GCPlayerHandler.GEAR_NOT_PRESENT, GCPlayerHandler.GEAR_NOT_PRESENT, GCPlayerHandler.GEAR_NOT_PRESENT, GCPlayerHandler.GEAR_NOT_PRESENT});
    }

    public PlayerGearData(Player player, int mask, int gear, int leftTank, int rightTank, int frequencyModule, int[] thermalPadding)
    {
        this.player = player;
        this.mask = mask;
        this.gear = gear;
        this.leftTank = leftTank;
        this.rightTank = rightTank;
        this.frequencyModule = frequencyModule;
        this.thermalPadding = thermalPadding;
    }

    public int getMask()
    {
        return this.mask;
    }

    public void setMask(int mask)
    {
        this.mask = mask;
    }

    public int getGear()
    {
        return this.gear;
    }

    public void setGear(int gear)
    {
        this.gear = gear;
    }

    public int getLeftTank()
    {
        return this.leftTank;
    }

    public void setLeftTank(int leftTank)
    {
        this.leftTank = leftTank;
    }

    public int getRightTank()
    {
        return this.rightTank;
    }

    public void setRightTank(int rightTank)
    {
        this.rightTank = rightTank;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public ResourceLocation getParachute()
    {
        return this.parachute;
    }

    public void setParachute(ResourceLocation parachute)
    {
        this.parachute = parachute;
    }

    public int getFrequencyModule()
    {
        return this.frequencyModule;
    }

    public void setFrequencyModule(int frequencyModule)
    {
        this.frequencyModule = frequencyModule;
    }

    public int getThermalPadding(int slot)
    {
        if (slot >= 0 && slot < this.thermalPadding.length)
        {
            return this.thermalPadding[slot];
        }

        return -1;
    }

    public void setThermalPadding(int slot, int thermalPadding)
    {
        if (slot >= 0 && slot < this.thermalPadding.length)
        {
            this.thermalPadding[slot] = thermalPadding;
        }
    }

    public int getShieldController()
    {
        return shieldController;
    }

    public void setShieldController(int shieldController)
    {
        this.shieldController = shieldController;
    }

    @Override
    public int hashCode()
    {
        return PlayerUtil.getName(this.player).hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof PlayerGearData)
        {
            return PlayerUtil.getName(((PlayerGearData) obj).player).equals(PlayerUtil.getName(this.player));
        }

        return false;
    }
}
