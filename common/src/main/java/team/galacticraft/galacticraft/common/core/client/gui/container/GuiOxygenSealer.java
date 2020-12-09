package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.GalacticraftCore;
import team.galacticraft.galacticraft.core.blocks.BlockThermalAir;
import team.galacticraft.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import team.galacticraft.galacticraft.core.energy.EnergyDisplayHelper;
import team.galacticraft.galacticraft.core.fluid.OxygenPressureProtocol;
import team.galacticraft.galacticraft.core.inventory.ContainerOxygenSealer;
import team.galacticraft.galacticraft.core.network.PacketSimple;
import team.galacticraft.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.core.tile.TileEntityOxygenSealer;
import team.galacticraft.galacticraft.core.util.EnumColor;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiOxygenSealer extends GuiContainerGC<ContainerOxygenSealer>
{
    private static final ResourceLocation sealerTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/oxygen_sealer.png");

    private final TileEntityOxygenSealer sealer;
    private Button buttonDisable;

    private final GuiElementInfoRegion oxygenInfoRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 112, (this.height - this.imageHeight) / 2 + 24, 56, 9, new ArrayList<String>(), this.width, this.height, this);
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 112, (this.height - this.imageHeight) / 2 + 37, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiOxygenSealer(ContainerOxygenSealer container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerOxygenSealer(playerInv, sealer), playerInv, new TranslationTextComponent("container.oxygen_sealer"));
        this.sealer = container.getSealer();
        this.imageHeight = 200;
    }

    @Override
    protected void init()
    {
        super.init();
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.0"));
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 32, (this.height - this.imageHeight) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height, this));
        List<String> oxygenSlotDesc = new ArrayList<String>();
        oxygenSlotDesc.add(I18n.get("gui.oxygen_slot.desc.0"));
        oxygenSlotDesc.add(I18n.get("gui.oxygen_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 9, (this.height - this.imageHeight) / 2 + 26, 18, 18, oxygenSlotDesc, this.width, this.height, this));
        List<String> ambientThermalDesc = new ArrayList<String>();
        ambientThermalDesc.add(I18n.get("gui.thermal_slot.desc.0"));
        ambientThermalDesc.add(I18n.get("gui.thermal_slot.desc.1"));
        ambientThermalDesc.add(I18n.get("gui.thermal_slot.desc.2"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 55, (this.height - this.imageHeight) / 2 + 26, 18, 18, ambientThermalDesc, this.width, this.height, this));
        List<String> oxygenDesc = new ArrayList<String>();
        oxygenDesc.add(I18n.get("gui.oxygen_storage.desc.0"));
        oxygenDesc.add(EnumColor.YELLOW + I18n.get("gui.oxygen_storage.desc.1") + ": " + ((int) Math.floor(this.sealer.getOxygenStored()) + " / " + (int) Math.floor(this.sealer.getMaxOxygenStored())));
        this.oxygenInfoRegion.tooltipStrings = oxygenDesc;
        this.oxygenInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 112;
        this.oxygenInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 23;
        this.oxygenInfoRegion.parentWidth = this.width;
        this.oxygenInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.oxygenInfoRegion);
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + I18n.get("gui.energy_storage.desc.1") + ": " + ((int) Math.floor(this.sealer.getEnergyStoredGC()) + " / " + (int) Math.floor(this.sealer.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 112;
        this.electricInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 36;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        this.buttons.add(this.buttonDisable = new Button(this.width / 2 - 38, this.height / 2 - 30 + 21, 76, 20, I18n.get("gui.button.enableseal"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.minecraft.level), new Object[]{this.sealer.getBlockPos(), 0}));
        }));
    }

    @Override
    protected void renderLabels(int par1, int par2)
    {
        this.font.draw(this.title.getColoredString(), 8, 10, 4210752);
        GCCoreUtil.drawStringRightAligned(I18n.get("gui.message.in") + ":", 99, 26, 4210752, this.font);
        GCCoreUtil.drawStringRightAligned(I18n.get("gui.message.in") + ":", 99, 38, 4210752, this.font);
        String status = I18n.get("gui.message.status") + ": " + this.getStatus();
        this.buttonDisable.active = this.sealer.disableCooldown == 0;
        this.buttonDisable.setMessage(this.sealer.disabled ? I18n.get("gui.button.enableseal") : I18n.get("gui.button.disableseal"));
        this.font.draw(status, this.imageWidth / 2 - this.font.width(status) / 2, 50, 4210752);
        int adjustedOxygenPerTick = this.sealer.oxygenPerTick * 20;
        if (this.sealer.disabled || this.sealer.getEnergyStoredGC() < this.sealer.storage.getMaxExtract())
        {
            adjustedOxygenPerTick = 0;
        }
        status = I18n.get("gui.oxygen_use.desc") + ": " + adjustedOxygenPerTick + I18n.get("gui.per_second");
        this.font.draw(status, this.imageWidth / 2 - this.font.width(status) / 2, 60, 4210752);
        status = I18n.get("gui.message.thermal_status") + ": " + this.getThermalStatus();
        this.font.draw(status, this.imageWidth / 2 - this.font.width(status) / 2, 70, 4210752);
        //		status = ElectricityDisplay.getDisplay(this.sealer.ueWattsPerTick * 20, ElectricUnit.WATT);
        //		this.font.drawString(status, this.xSize / 2 - this.font.getStringWidth(status) / 2, 70, 4210752);
        //		status = ElectricityDisplay.getDisplay(this.sealer.getVoltage(), ElectricUnit.VOLTAGE);
        //		this.font.drawString(status, this.xSize / 2 - this.font.getStringWidth(status) / 2, 80, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 90 + 3, 4210752);
    }

    private String getThermalStatus()
    {
        BlockState stateAbove = this.sealer.getLevel().getBlockState(this.sealer.getBlockPos().above());

        if (stateAbove.getBlock() instanceof BlockThermalAir)
        {
            if (stateAbove.getValue(BlockThermalAir.THERMAL))
            {
                return EnumColor.DARK_GREEN + I18n.get("gui.status.on");
            }
        }

        if (this.sealer.thermalControlEnabled())
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.not_available");
        }

        return EnumColor.DARK_RED + I18n.get("gui.status.off");
    }

    private String getStatus()
    {
        BlockPos blockPosAbove = this.sealer.getBlockPos().above();
        Block blockAbove = this.sealer.getLevel().getBlockState(blockPosAbove).getBlock();
        BlockState state = this.sealer.getLevel().getBlockState(blockPosAbove);

        if (!(blockAbove.isAir(state, this.sealer.getLevel(), blockPosAbove)) && !OxygenPressureProtocol.canBlockPassAir(this.sealer.getLevel(), state, blockPosAbove, Direction.UP))
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.sealerblocked");
        }

//        if (RedstoneUtil.isBlockReceivingRedstone(this.sealer.getWorldObj(), this.sealer.getPos()))
//        {
//            return EnumColor.DARK_RED + I18n.get("gui.status.off");
//        }

        if (this.sealer.disabled)
        {
            return EnumColor.ORANGE + I18n.get("gui.status.disabled");
        }

        if (this.sealer.getEnergyStoredGC() == 0)
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.missingpower");
        }

        if (this.sealer.getEnergyStoredGC() < this.sealer.storage.getMaxExtract())
        {
            return EnumColor.ORANGE + I18n.get("gui.status.missingpower");
        }

        if (this.sealer.getOxygenStored() < 1)
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.missingoxygen");
        }

        if (this.sealer.calculatingSealed)
        {
            return EnumColor.ORANGE + I18n.get("gui.status.checking_seal") + "...";
        }

        int threadCooldown = this.sealer.getScaledThreadCooldown(25);

        if (threadCooldown < 15)
        {
            if (threadCooldown < 4)
            {
                String elipsis = "";
                for (int i = 0; i < (23 - threadCooldown) % 4; i++)
                {
                    elipsis += ".";
                }

                return EnumColor.ORANGE + I18n.get("gui.status.check_starting") + elipsis;
            }
            else
            {
                return EnumColor.ORANGE + I18n.get("gui.status.check_pending");
            }
        }
        else
        {
            if (!this.sealer.sealed)
            {
                return EnumColor.DARK_RED + I18n.get("gui.status.unsealed");
            }
            else
            {
                return EnumColor.DARK_GREEN + I18n.get("gui.status.sealed");
            }
        }
    }

    @Override
    protected void renderBg(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GuiOxygenSealer.sealerTexture);
        final int var5 = (this.width - this.imageWidth) / 2;
        final int var6 = (this.height - this.imageHeight) / 2;
        this.blit(var5, var6 + 5, 0, 0, this.imageWidth, this.imageHeight);

        if (this.sealer != null)
        {
            List<String> oxygenDesc = new ArrayList<String>();
            oxygenDesc.add(I18n.get("gui.oxygen_storage.desc.0"));
            oxygenDesc.add(EnumColor.YELLOW + I18n.get("gui.oxygen_storage.desc.1") + ": " + ((int) Math.floor(this.sealer.getOxygenStored()) + " / " + (int) Math.floor(this.sealer.getMaxOxygenStored())));
            this.oxygenInfoRegion.tooltipStrings = oxygenDesc;

            List<String> electricityDesc = new ArrayList<String>();
            electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
            EnergyDisplayHelper.getEnergyDisplayTooltip(this.sealer.getEnergyStoredGC(), this.sealer.getMaxEnergyStoredGC(), electricityDesc);
//			electricityDesc.add(EnumColor.YELLOW + I18n.get("gui.energy_storage.desc.1") + ": " + ((int) Math.floor(this.sealer.getEnergyStoredGC()) + " / " + (int) Math.floor(this.sealer.getMaxEnergyStoredGC())));
            this.electricInfoRegion.tooltipStrings = electricityDesc;

            int scale = this.sealer.getCappedScaledOxygenLevel(54);
            this.blit(var5 + 113, var6 + 24, 197, 7, Math.min(scale, 54), 7);
            scale = this.sealer.getScaledElecticalLevel(54);
            this.blit(var5 + 113, var6 + 37, 197, 0, Math.min(scale, 54), 7);
            scale = 25 - this.sealer.getScaledThreadCooldown(25);
            this.blit(var5 + 148, var6 + 60, 176, 14, 10, 27);
            if (scale != 0)
            {
                this.blit(var5 + 149, var6 + 61 + scale, 186, 14, 8, 25 - scale);
            }

            if (this.sealer.getEnergyStoredGC() > 0)
            {
                this.blit(var5 + 99, var6 + 36, 176, 0, 11, 10);
            }

            if (this.sealer.getOxygenStored() > 0)
            {
                this.blit(var5 + 100, var6 + 23, 187, 0, 10, 10);
            }
        }
    }
}
