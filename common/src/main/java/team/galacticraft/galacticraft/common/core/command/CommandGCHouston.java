package team.galacticraft.galacticraft.common.core.command;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import com.mojang.authlib.GameProfile;
//
//import team.galacticraft.galacticraft.common.api.entity.IRocketType;
//import team.galacticraft.galacticraft.common.api.vector.Vector3;
//import team.galacticraft.galacticraft.common.api.world.IGalacticraftWorldProvider;
//import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
//import team.galacticraft.galacticraft.core.tick.TickHandlerServer;
//import team.galacticraft.galacticraft.core.util.ConfigManagerCore;
//import team.galacticraft.galacticraft.core.util.EnumColor;
//import team.galacticraft.galacticraft.core.util.GCCoreUtil;
//import team.galacticraft.galacticraft.core.util.PlayerUtil;
//import team.galacticraft.galacticraft.core.util.WorldUtil;
//import net.minecraft.command.CommandBase;
//import net.minecraft.command.CommandException;
//import net.minecraft.command.ICommandSender;
//import net.minecraft.command.WrongUsageException;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.util.NonNullList;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.ServerWorld;
//
//public class CommandGCHouston extends CommandBase
//{
//    private static List<ServerPlayerEntity> timerList = new LinkedList<>();
//
//    public static void reset()
//    {
//        timerList.clear();
//    }
//
//    @Override
//    public String getUsage(ICommandSender var1)
//    {
//        return "/" + this.getName() + " [<player>]";
//    }
//
//    @Override
//    public int getRequiredPermissionLevel()
//    {
//        return 0;
//    }
//
//    @Override
//    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
//    {
//        return true;
//    }
//
//    @Override
//    public String getName()
//    {
//        return "gchouston";
//    }
//
//    @Override
//    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
//    {
//        if (args.length == 1 && this.isOp(server, sender))
//        {
//            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
//        }
//        return null;
//    }
//
//    @Override
//    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
//    {
//        return par2 == 1;
//    }
//
//    private boolean isOp(MinecraftServer server, ICommandSender sender)
//    {
//        Entity entitySender = sender.getCommandSenderEntity();
//        if (entitySender == null)
//        {
//            return true;
//        }
//        else if (entitySender instanceof PlayerEntity)
//        {
//            GameProfile prof = ((PlayerEntity) entitySender).getGameProfile();
//            return server.getPlayerList().canSendCommands(prof);
//        }
//        return false;
//    }
//
//    @Override
//    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
//    {
//        ServerPlayerEntity playerBase = null;
//        boolean isOp = this.isOp(server, sender);
//
//        if (args.length < 2)
//        {
//            try
//            {
//                if (args.length == 0)
//                {
//                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
//                    if (!playerBase.abilities.isCreativeMode)
//                    {
//                        if (ConfigManagerCore.challengeMode.get() || !(playerBase.world.getDimension() instanceof IGalacticraftWorldProvider) )
//                        {
//                            CommandBase.notifyCommandListener(sender, this, "commands.gchouston.fail");
//                            return;
//                        }
//                    }
//
//                    if (timerList.contains(playerBase))
//                    {
//                        timerList.remove(playerBase);
//                    }
//                    else
//                    {
//                        timerList.add(playerBase);
//                        TickHandlerServer.timerHoustonCommand = 250;
//                        String msg = EnumColor.YELLOW + I18n.get("commands.gchouston.confirm.1") + " " + EnumColor.WHITE + I18n.get("commands.gchouston.confirm.2");
//                        CommandBase.notifyCommandListener(sender, this, msg);
//                        return;
//                    }
//                }
//                else
//                {
//                    if (!isOp)
//                    {
//                        CommandBase.notifyCommandListener(sender, this, "commands.gchouston.noop");
//                        return;
//                    }
//
//                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(args[0], true);
//                }
//
//                if (playerBase != null)
//                {
//                    DimensionType dimID = ConfigManagerCore.idDimensionOverworld.get();
//                    ServerWorld worldserver = server.getWorld(dimID);
//                    if (worldserver == null)
//                    {
//                        worldserver = server.getWorld(0);
//                        if (worldserver == null)
//                        {
//                            throw new Exception("/gchouston could not find Overworld.");
//                        }
//                        dimID = 0;
//                    }
//                    BlockPos spawnPoint = null;
//                    BlockPos bedPos = playerBase.getBedLocation(dimID);
//                    if (bedPos != null)
//                    {
//                        spawnPoint = PlayerEntity.getBedSpawnLocation(worldserver, bedPos, false);
//                    }
//                    if (spawnPoint == null)
//                    {
//                        spawnPoint = worldserver.getTopSolidOrLiquidBlock(worldserver.getSpawnPoint());
//                    }
//                    GCPlayerStats stats = GCPlayerStats.get(playerBase);
//                    stats.setRocketStacks(NonNullList.withSize(0, ItemStack.EMPTY));
//                    stats.setRocketType(IRocketType.EnumRocketType.DEFAULT);
//                    stats.setRocketItem(null);
//                    stats.setFuelLevel(0);
//                    stats.setCoordsTeleportedFromX(spawnPoint.getX());
//                    stats.setCoordsTeleportedFromZ(spawnPoint.getZ());
//                    stats.setUsingPlanetSelectionGui(false);
//                    playerBase.closeScreen();
//                    Vector3 spawnPos = new Vector3(spawnPoint.getX() + 0.5D, spawnPoint.getY() + 0.25D, spawnPoint.getZ() + 0.5D);
//
//                    try
//                    {
//                        WorldUtil.teleportEntitySimple(worldserver, dimID, playerBase, spawnPos);
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                        throw e;
//                    }
//
//                    CommandBase.notifyCommandListener(sender, this, "commands.gchouston.success", new Object[] { String.valueOf(EnumColor.GREY + "" + playerBase.getName()) });
//                }
//                else
//                {
//                    throw new Exception("Could not find player with name: " + args[0]);
//                }
//            }
//            catch (final Exception var6)
//            {
//                throw new CommandException(var6.getMessage(), new Object[0]);
//            }
//        }
//        else
//        {
//            throw new WrongUsageException(I18n.getWithFormat("commands.dimensiontp.too_many", this.getUsage(sender)), new Object[0]);
//        }
//    }
//}
