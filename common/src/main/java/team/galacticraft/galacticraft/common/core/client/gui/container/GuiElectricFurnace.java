package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import team.galacticraft.galacticraft.core.energy.EnergyDisplayHelper;
import team.galacticraft.galacticraft.core.inventory.ContainerDeconstructor;
import team.galacticraft.galacticraft.core.inventory.ContainerElectricFurnace;
import team.galacticraft.galacticraft.core.tile.TileEntityElectricFurnace;
import team.galacticraft.galacticraft.core.util.EnumColor;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiElectricFurnace extends GuiContainerGC<ContainerElectricFurnace>
{
    private static final ResourceLocation electricFurnaceTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/electric_furnace.png");
    private static final ResourceLocation arcFurnaceTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/electric_arc_furnace.png");
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion(0, 0, 56, 9, null, 0, 0, this);

    private TileEntityElectricFurnace furnace;
    private final ResourceLocation texture;

    public GuiElectricFurnace(ContainerElectricFurnace container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerElectricFurnace(playerInv, furnace), playerInv, new TranslationTextComponent(furnace.getTierGC() == 1 ? "tile.machine.2" : "tile.machine.7"));
        this.furnace = container.getFurnace();
        texture = furnace.tierGC == 2 ? arcFurnaceTexture : electricFurnaceTexture;
    }

    @Override
    protected void init()
    {
        super.init();
        this.electricInfoRegion.tooltipStrings = new ArrayList<String>();
        this.electricInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 39;
        this.electricInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 52;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.0"));
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 7, (this.height - this.imageHeight) / 2 + 48, 18, 18, batterySlotDesc, this.width, this.height, this));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void renderLabels(int par1, int par2)
    {
        this.font.draw(this.title.getColoredString(), 45, 6, 4210752);
        String displayText = "";

        if (this.furnace.processTicks > 0)
        {
            displayText = EnumColor.BRIGHT_GREEN + I18n.get("gui.status.running");
        }
        else
        {
            displayText = EnumColor.ORANGE + I18n.get("gui.status.idle");
        }

        this.font.draw(I18n.get("gui.message.status") + ": ", 97, 52, 4210752);
        this.font.draw(this.furnace.getGUIstatus(null, displayText, true), 107, 62, 4210752);
//		this.font.drawString("" + this.tileEntity.storage.getMaxExtract(), 97, 56, 4210752);
//		this.font.drawString("Voltage: " + (int) (this.tileEntity.getVoltage() * 1000.0F), 97, 68, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void renderBg(float par1, int par2, int par3)
    {
        this.minecraft.textureManager.bind(this.texture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int containerWidth = (this.width - this.imageWidth) / 2;
        int containerHeight = (this.height - this.imageHeight) / 2;
        this.blit(containerWidth, containerHeight, 0, 0, this.imageWidth, this.imageHeight);
        int scale;

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.furnace.getEnergyStoredGC(), this.furnace.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.furnace.processTicks > 0)
        {
            scale = (int) ((double) this.furnace.processTicks / (double) this.furnace.processTimeRequired * 23);
            this.blit(containerWidth + 78, containerHeight + 24, 176, 0, 23 - scale, 15);
        }

        if (this.furnace.getEnergyStoredGC() > 0)
        {
            scale = this.furnace.getScaledElecticalLevel(54);
            this.blit(containerWidth + 40, containerHeight + 53, 176, 15, scale, 7);
            this.blit(containerWidth + 26, containerHeight + 52, 176, 22, 11, 10);
        }
    }
}
