package micdoodle8.mods.galacticraft.core.client.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiElementTexturedButton extends AbstractWidget
{
    private final ResourceLocation texture;
    private final int bWidth, bHeight;

    public GuiElementTexturedButton(int par2, int par3, int par4, int par5, ResourceLocation texture, int width, int height)
    {
        super(par2, par3, par4, par5, "");
        this.texture = texture;
        this.bWidth = width;
        this.bHeight = height;
    }

    @Override
    public void render(int mouseX, int mouseY, float partial)
    {
        if (this.visible)
        {
            Minecraft minecraft = Minecraft.getInstance();
            final Font var4 = minecraft.font;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            minecraft.textureManager.bindTexture(this.texture);
            this.blit(this.x, this.y, 0, 0, this.bWidth, this.bHeight);
            int var6 = 14737632;

            if (!this.active)
            {
                var6 = -6250336;
            }
            else if (this.isHovered())
            {
                var6 = 16777120;
            }

            this.drawCenteredString(var4, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, var6);
        }
    }
}
