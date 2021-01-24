package micdoodle8.mods.galacticraft.core.dimension;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FlagData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpaceRaceManager
{
    private static final Set<SpaceRace> spaceRaces = Sets.newHashSet();

    public static SpaceRace addSpaceRace(SpaceRace spaceRace)
    {
        SpaceRaceManager.spaceRaces.remove(spaceRace);
        SpaceRaceManager.spaceRaces.add(spaceRace);
        return spaceRace;
    }

    public static void removeSpaceRace(SpaceRace race)
    {
        SpaceRaceManager.spaceRaces.remove(race);
    }

    public static void tick()
    {
        for (SpaceRace race : SpaceRaceManager.spaceRaces)
        {
            boolean playerOnline = false;

            for (ServerPlayer player : PlayerUtil.getPlayersOnline())
            {
                if (race.getPlayerNames().contains(PlayerUtil.getName(player)))
                {
                    CelestialBody body = GalaxyRegistry.getCelestialBodyFromDimensionID(player.level.getDimension().getType());

                    if (body != null)
                    {
                        if (!race.getCelestialBodyStatusList().containsKey(body))
                        {
                            race.setCelestialBodyReached(body);
                        }
                    }

                    playerOnline = true;
                }
            }

            if (playerOnline)
            {
                race.tick();
            }
        }
    }

    public static void loadSpaceRaces(CompoundTag nbt)
    {
        ListTag tagList = nbt.getList("SpaceRaceList", 10);

        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundTag nbt2 = tagList.getCompound(i);
            SpaceRace race = new SpaceRace();
            race.loadFromNBT(nbt2);
            SpaceRaceManager.spaceRaces.add(race);
        }
    }

    public static CompoundTag saveSpaceRaces(CompoundTag nbt)
    {
        ListTag tagList = new ListTag();

        for (SpaceRace race : SpaceRaceManager.spaceRaces)
        {
            CompoundTag nbt2 = new CompoundTag();
            race.saveToNBT(nbt2);
            tagList.add(nbt2);
        }

        nbt.put("SpaceRaceList", tagList);
        return nbt;
    }

    public static SpaceRace getSpaceRaceFromPlayer(String username)
    {
        for (SpaceRace race : SpaceRaceManager.spaceRaces)
        {
            if (race.getPlayerNames().contains(username))
            {
                return race;
            }
        }

        return null;
    }

    public static SpaceRace getSpaceRaceFromID(int teamID)
    {
        for (SpaceRace race : SpaceRaceManager.spaceRaces)
        {
            if (race.getSpaceRaceID() == teamID)
            {
                return race;
            }
        }

        return null;
    }

    public static void sendSpaceRaceData(MinecraftServer theServer, ServerPlayer toPlayer, SpaceRace spaceRace)
    {
        if (spaceRace != null)
        {
            List<Object> objList = new ArrayList<Object>();
            objList.add(spaceRace.getSpaceRaceID());
            objList.add(spaceRace.getTeamName());
            objList.add(spaceRace.getFlagData());
            objList.add(spaceRace.getTeamColor());
            objList.add(spaceRace.getPlayerNames().toArray(new String[spaceRace.getPlayerNames().size()]));

            if (toPlayer != null)
            {
                GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACE_RACE_DATA, GCCoreUtil.getDimensionType(toPlayer.level), objList), toPlayer);
                spaceRace.updatePlayerSchematics(toPlayer);
            }
            else
            {
                for (ServerLevel server : theServer.getAllLevels())
                {
                    GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACE_RACE_DATA, GCCoreUtil.getDimensionType(server), objList), GCCoreUtil.getDimensionType(server));
                }
            }
        }
    }

    public static ImmutableSet<SpaceRace> getSpaceRaces()
    {
        return ImmutableSet.copyOf(new HashSet<SpaceRace>(SpaceRaceManager.spaceRaces));
    }

    public static void onPlayerRemoval(MinecraftServer server, String player, SpaceRace race)
    {
        for (String member : race.getPlayerNames())
        {
            ServerPlayer memberObj = PlayerUtil.getPlayerForUsernameVanilla(server, member);

            if (memberObj != null)
            {
                memberObj.sendMessage(new TextComponent(EnumColor.DARK_AQUA + GCCoreUtil.translateWithFormat("gui.space_race.chat.remove_success", EnumColor.RED + player + EnumColor.DARK_AQUA)).setStyle(new Style().setColor(TextFormatting.DARK_AQUA)));
            }
        }

        List<String> playerList = new ArrayList<String>();
        playerList.add(player);
        SpaceRace newRace = SpaceRaceManager.addSpaceRace(new SpaceRace(playerList, SpaceRace.DEFAULT_NAME, new FlagData(48, 32), new Vector3(1, 1, 1)));
        ServerPlayer playerToRemove = PlayerUtil.getPlayerBaseServerFromPlayerUsername(server, player, true);

        if (playerToRemove != null)
        {
            SpaceRaceManager.sendSpaceRaceData(server, playerToRemove, newRace);
            SpaceRaceManager.sendSpaceRaceData(server, playerToRemove, race);
        }
    }

    public static void teamUnlockSchematic(ServerPlayer player, ItemStack stack)
    {
        SpaceRace race = SpaceRaceManager.getSpaceRaceFromPlayer(PlayerUtil.getName(player));
        if (race == null)
        {
            return;
        }
        MinecraftServer server = player.server;
        for (String member : race.getPlayerNames())
        {
            if (player.getName().getString().equalsIgnoreCase(member))
            {
                continue;
            }

            ServerPlayer memberObj = PlayerUtil.getPlayerForUsernameVanilla(server, member);
            if (memberObj != null)
            {
                SchematicRegistry.unlockNewPage(memberObj, stack);
            }
            else
            {
                race.addNewSchematic(member, stack);
            }
        }
    }
}
