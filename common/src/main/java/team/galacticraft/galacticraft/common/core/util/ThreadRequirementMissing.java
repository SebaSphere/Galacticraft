package team.galacticraft.galacticraft.common.core.util;

import team.galacticraft.galacticraft.core.client.gui.screen.GuiMissingCore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;

public class ThreadRequirementMissing extends Thread
{
    private static LogicalSide threadSide;
    public static ThreadRequirementMissing INSTANCE;

    public ThreadRequirementMissing(LogicalSide threadSide)
    {
        super("Galacticraft Requirement Check Thread");
        this.setDaemon(true);
        ThreadRequirementMissing.threadSide = threadSide;
    }

    public static void beginCheck(LogicalSide threadSide)
    {
        INSTANCE = new ThreadRequirementMissing(threadSide);
        INSTANCE.start();
    }

    @Override
    public void run()
    {
//        if (!ModList.get().isLoaded("micdoodlecore"))
//        {
//            if (ThreadRequirementMissing.threadSide.isServer())
//            {
//                FMLCommonHandler.instance().getMinecraftServerInstance().logSevere("===================================================================");
//                FMLCommonHandler.instance().getMinecraftServerInstance().logSevere("MicdoodleCore not found in mods folder. Galacticraft will not load.");
//                FMLCommonHandler.instance().getMinecraftServerInstance().logSevere("===================================================================");
//            }
//            else
//            {
//                ThreadRequirementMissing.openGuiClient();
//            }
//        }
    }

    @Environment(EnvType.CLIENT)
    private static void openGuiClient()
    {
        Minecraft.getInstance().setScreen(new GuiMissingCore());
    }
}
