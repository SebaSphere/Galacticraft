package team.galacticraft.galacticraft.common.core.command;
//
//import team.galacticraft.galacticraft.core.util.ConfigManagerCore;
//import team.galacticraft.galacticraft.core.util.PlayerUtil;
//import net.minecraft.command.CommandBase;
//import net.minecraft.command.CommandException;
//import net.minecraft.command.ICommandSender;
//import net.minecraft.command.WrongUsageException;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.util.text.StringTextComponent;
//
//public class CommandKeepDim extends CommandBase
//{
//    @Override
//    public String getUsage(ICommandSender var1)
//    {
//        return "/" + this.getName() + " <dimension id>";
//    }
//
//    @Override
//    public int getRequiredPermissionLevel()
//    {
//        return 4;
//    }
//
//    @Override
//    public String getName()
//    {
//        return "gckeeploaded";
//    }
//
//    @Override
//    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
//    {
//        ServerPlayerEntity playerBase;
//
//        if (args.length > 1)
//        {
//            throw new WrongUsageException("Too many command arguments! Usage: " + this.getUsage(sender), new Object[0]);
//        }
//        else
//        {
//            try
//            {
//                playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
//
//                if (playerBase != null)
//                {
//                    DimensionType dimID;
//
//                    if (args.length == 0)
//                    {
//                        dimID = playerBase.dimension;
//                    }
//                    else
//                    {
//                        try
//                        {
//                            dimID = CommandBase.parseInt(args[0]);
//                        }
//                        catch (Exception e)
//                        {
//                            throw new WrongUsageException("Needs a dimension number! Usage: " + this.getUsage(sender), new Object[0]);
//                        }
//                    }
//
//                    if (ConfigManagerCore.setLoaded.get()(dimID))
//                    {
//                        playerBase.sendMessage(new StringTextComponent("[GCKeepLoaded] Successfully set dimension " + dimID + " to load staticly"));
//                    }
//                    else
//                    {
//                        if (ConfigManagerCore.setUnloaded.get()(dimID))
//                        {
//                            playerBase.sendMessage(new StringTextComponent("[GCKeepLoaded] Successfully set dimension " + dimID + " to not load staticly"));
//                        }
//                        else
//                        {
//                            playerBase.sendMessage(new StringTextComponent("[GCKeepLoaded] Failed to set dimension as not static"));
//                        }
//                    }
//                }
//            }
//            catch (final Exception var6)
//            {
//                throw new CommandException(var6.getMessage(), new Object[0]);
//            }
//        }
//    }
//}
