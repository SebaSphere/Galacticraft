package micdoodle8.mods.galacticraft.core.network;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Satellite;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.api.item.EnumExtendedInventorySlot;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.tile.ITileClientUpdates;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkProvider;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.advancement.GCTriggers;
import micdoodle8.mods.galacticraft.core.client.FootprintRenderer;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.dimension.SpaceStationWorldData;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseConductor;
import micdoodle8.mods.galacticraft.core.entities.BuggyEntity;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProvider;
import micdoodle8.mods.galacticraft.core.entities.IControllableEntity;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.inventory.ContainerBuggy;
import micdoodle8.mods.galacticraft.core.inventory.ContainerExtendedInventory;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSchematic;
import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.*;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.core.wrappers.FlagData;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import micdoodle8.mods.galacticraft.core.wrappers.ScheduledDimensionChange;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SRespawnPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;

public class PacketSimple extends PacketBase implements micdoodle8.mods.galacticraft.core.network.IPacket<INetHandler>, IGCMsg
{
    public enum EnumSimplePacket
    {
        // SERVER
        S_RESPAWN_PLAYER(LogicalSide.SERVER, String.class),
        S_TELEPORT_ENTITY(LogicalSide.SERVER, DimensionType.class),
        S_IGNITE_ROCKET(LogicalSide.SERVER),
        S_OPEN_SCHEMATIC_PAGE(LogicalSide.SERVER, Integer.class, Integer.class, Integer.class, Integer.class),
        S_OPEN_FUEL_GUI(LogicalSide.SERVER, String.class),
        S_UPDATE_SHIP_YAW(LogicalSide.SERVER, Float.class),
        S_UPDATE_SHIP_PITCH(LogicalSide.SERVER, Float.class),
        S_SET_ENTITY_FIRE(LogicalSide.SERVER, Integer.class),
        S_BIND_SPACE_STATION_ID(LogicalSide.SERVER, DimensionType.class),
        S_UNLOCK_NEW_SCHEMATIC(LogicalSide.SERVER),
        S_UPDATE_DISABLEABLE_BUTTON(LogicalSide.SERVER, BlockPos.class, Integer.class),
        S_ON_FAILED_CHEST_UNLOCK(LogicalSide.SERVER, Integer.class),
        S_RENAME_SPACE_STATION(LogicalSide.SERVER, String.class, Integer.class),
        S_OPEN_EXTENDED_INVENTORY(LogicalSide.SERVER),
        S_ON_ADVANCED_GUI_CLICKED_INT(LogicalSide.SERVER, Integer.class, BlockPos.class, Integer.class),
        S_ON_ADVANCED_GUI_CLICKED_STRING(LogicalSide.SERVER, Integer.class, BlockPos.class, String.class),
        S_UPDATE_SHIP_MOTION_Y(LogicalSide.SERVER, Integer.class, Boolean.class),
        S_START_NEW_SPACE_RACE(LogicalSide.SERVER, Integer.class, String.class, FlagData.class, Vector3.class, String[].class),
        S_REQUEST_FLAG_DATA(LogicalSide.SERVER, String.class),
        S_INVITE_RACE_PLAYER(LogicalSide.SERVER, String.class, Integer.class),
        S_REMOVE_RACE_PLAYER(LogicalSide.SERVER, String.class, Integer.class),
        S_ADD_RACE_PLAYER(LogicalSide.SERVER, String.class, Integer.class),
        S_COMPLETE_CBODY_HANDSHAKE(LogicalSide.SERVER, String.class),
        S_REQUEST_GEAR_DATA1(LogicalSide.SERVER, UUID.class),
        S_REQUEST_GEAR_DATA2(LogicalSide.SERVER, UUID.class),
        S_REQUEST_OVERWORLD_IMAGE(LogicalSide.SERVER),
        S_REQUEST_MAP_IMAGE(LogicalSide.SERVER, Integer.class, Integer.class, Integer.class),
        S_REQUEST_PLAYERSKIN(LogicalSide.SERVER, String.class),
        S_BUILDFLAGS_UPDATE(LogicalSide.SERVER, Integer.class),
        S_CONTROL_ENTITY(LogicalSide.SERVER, Integer.class),
        S_NOCLIP_PLAYER(LogicalSide.SERVER, Boolean.class),
        S_REQUEST_DATA(LogicalSide.SERVER, DimensionType.class, BlockPos.class),
        S_UPDATE_CHECKLIST(LogicalSide.SERVER, CompoundNBT.class),
        S_REQUEST_MACHINE_DATA(LogicalSide.SERVER, BlockPos.class),
        S_REQUEST_CONTAINER_SLOT_REFRESH(LogicalSide.SERVER, Integer.class),
        S_ROTATE_ROCKET(LogicalSide.SERVER, Integer.class, Float.class, Float.class),
        // CLIENT
        C_AIR_REMAINING(LogicalSide.CLIENT, Integer.class, Integer.class, String.class),
        C_UPDATE_DIMENSION_LIST(LogicalSide.CLIENT, String.class, String.class, Boolean.class),
        C_SPAWN_SPARK_PARTICLES(LogicalSide.CLIENT, BlockPos.class),
        C_UPDATE_GEAR_SLOT(LogicalSide.CLIENT, UUID.class, Integer.class, Integer.class, Integer.class),
        C_CLOSE_GUI(LogicalSide.CLIENT),
        C_RESET_THIRD_PERSON(LogicalSide.CLIENT),
        C_UPDATE_SPACESTATION_LIST(LogicalSide.CLIENT, Integer[].class),
        //        C_UPDATE_SPACESTATION_DATA(LogicalSide.CLIENT, Integer.class, CompoundNBT.class),
        C_UPDATE_SPACESTATION_CLIENT_ID(LogicalSide.CLIENT, String.class),
        C_UPDATE_PLANETS_LIST(LogicalSide.CLIENT, Integer[].class),
        C_UPDATE_CONFIGS(LogicalSide.CLIENT, Integer.class, Double.class, Integer.class, Integer.class, Integer.class, String.class, Float.class, Float.class, Float.class, Float.class, Integer.class, String[].class),
        C_UPDATE_STATS(LogicalSide.CLIENT, Integer.class, String.class, Integer.class, String.class, Integer.class, String.class, Integer.class, String.class, Integer.class, String.class, Integer.class, Integer.class), //Note: Integer, PANELTYPES_LENGTH * <String, Integer>, Integer - see StatsCapability.getMiscNetworkedStats()
        C_ADD_NEW_SCHEMATIC(LogicalSide.CLIENT, Integer.class),
        C_UPDATE_SCHEMATIC_LIST(LogicalSide.CLIENT, Integer[].class),
        C_PLAY_SOUND_BOSS_DEATH(LogicalSide.CLIENT, Float.class),
        C_PLAY_SOUND_EXPLODE(LogicalSide.CLIENT),
        C_PLAY_SOUND_BOSS_LAUGH(LogicalSide.CLIENT),
        C_PLAY_SOUND_BOW(LogicalSide.CLIENT),
        C_UPDATE_OXYGEN_VALIDITY(LogicalSide.CLIENT, Boolean.class),
        C_OPEN_PARACHEST_GUI(LogicalSide.CLIENT, Integer.class, Integer.class, Integer.class),
        C_UPDATE_WIRE_BOUNDS(LogicalSide.CLIENT, BlockPos.class),
        C_OPEN_SPACE_RACE_GUI(LogicalSide.CLIENT),
        C_UPDATE_SPACE_RACE_DATA(LogicalSide.CLIENT, Integer.class, String.class, FlagData.class, Vector3.class, String[].class),
        C_OPEN_JOIN_RACE_GUI(LogicalSide.CLIENT, Integer.class),
        C_UPDATE_FOOTPRINT_LIST(LogicalSide.CLIENT, Long.class, Footprint[].class),
        C_UPDATE_DUNGEON_DIRECTION(LogicalSide.CLIENT, Float.class),
        C_FOOTPRINTS_REMOVED(LogicalSide.CLIENT, Long.class, BlockVec3.class),
        C_UPDATE_STATION_SPIN(LogicalSide.CLIENT, Float.class, Boolean.class),
        C_UPDATE_STATION_DATA(LogicalSide.CLIENT, Double.class, Double.class),
        C_UPDATE_STATION_BOX(LogicalSide.CLIENT, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class),
        C_UPDATE_THERMAL_LEVEL(LogicalSide.CLIENT, Integer.class, Boolean.class),
        C_DISPLAY_ROCKET_CONTROLS(LogicalSide.CLIENT),
        C_GET_CELESTIAL_BODY_LIST(LogicalSide.CLIENT),
        C_UPDATE_ENERGYUNITS(LogicalSide.CLIENT, Integer.class),
        C_RESPAWN_PLAYER(LogicalSide.CLIENT, String.class, Integer.class, String.class, Integer.class),
        C_UPDATE_TELEMETRY(LogicalSide.CLIENT, BlockPos.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, String.class),
        C_SEND_PLAYERSKIN(LogicalSide.CLIENT, String.class, String.class, String.class, String.class),
        C_SEND_OVERWORLD_IMAGE(LogicalSide.CLIENT, Integer.class, Integer.class, byte[].class),
        C_RECOLOR_PIPE(LogicalSide.CLIENT, BlockPos.class),
        C_RECOLOR_ALL_GLASS(LogicalSide.CLIENT, Integer.class, Integer.class, Integer.class),  //Number of integers to match number of different blocks of PLAIN glass individually instanced and registered in GCBlocks
        C_UPDATE_MACHINE_DATA(LogicalSide.CLIENT, BlockPos.class, Integer.class, Integer.class, Integer.class, Integer.class),
        C_SPAWN_HANGING_SCHEMATIC(LogicalSide.CLIENT, BlockPos.class, Integer.class, Integer.class, Integer.class),
        C_LEAK_DATA(LogicalSide.CLIENT, BlockPos.class, Integer[].class);

        private final LogicalSide targetSide;
        private final Class<?>[] decodeAs;

        EnumSimplePacket(LogicalSide targetSide, Class<?>... decodeAs)
        {
            this.targetSide = targetSide;
            this.decodeAs = decodeAs;
        }

        public LogicalSide getTargetSide()
        {
            return this.targetSide;
        }

        public Class<?>[] getDecodeClasses()
        {
            return this.decodeAs;
        }
    }

    private EnumSimplePacket type;
    private List<Object> data;
    static private String spamCheckString;
    static private final Map<ServerPlayerEntity, GameType> savedSettings = new HashMap<>();

    public PacketSimple()
    {
        super();
    }

    public PacketSimple(EnumSimplePacket packetType, DimensionType dimID, Object[] data)
    {
        this(packetType, dimID, Arrays.asList(data));
    }

    public PacketSimple(EnumSimplePacket packetType, World world, Object[] data)
    {
        this(packetType, GCCoreUtil.getDimensionType(world), Arrays.asList(data));
    }

    public PacketSimple(EnumSimplePacket packetType, DimensionType dimID, List<Object> data)
    {
        super(dimID);

        this.type = packetType;
        this.data = data;
    }

    public static void encode(final PacketSimple message, final PacketBuffer buf)
    {
        buf.writeInt(message.type.ordinal());
        NetworkUtil.writeUTF8String(buf, message.getDimensionID().getRegistryName().toString());

        try
        {
            NetworkUtil.encodeData(buf, message.data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static PacketSimple decode(PacketBuffer buf)
    {
        EnumSimplePacket type = EnumSimplePacket.values()[buf.readInt()];
        DimensionType dim = DimensionType.byName(new ResourceLocation(NetworkUtil.readUTF8String(buf)));
        ArrayList<Object> data = null;

        try
        {
            if (type.getDecodeClasses().length > 0)
            {
                data = NetworkUtil.decodeData(type.getDecodeClasses(), buf);
            }
            if (buf.readableBytes() > 0 && buf.writerIndex() < 0xfff00)
            {
                GCLog.severe("Galacticraft packet length problem for packet type " + type.toString());
            }
        }
        catch (Exception e)
        {
            System.err.println("[Galacticraft] Error handling simple packet type: " + type.toString() + " " + buf.toString());
            e.printStackTrace();
            throw e;
        }
        return new PacketSimple(type, dim, data);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClientSide(PlayerEntity player)
    {
        ClientPlayerEntity playerBaseClient = null;
        GCPlayerStatsClient stats = null;

        if (player instanceof ClientPlayerEntity)
        {
            playerBaseClient = (ClientPlayerEntity) player;
            stats = GCPlayerStatsClient.get(playerBaseClient);
        }
        else
        {
            if (type != EnumSimplePacket.C_UPDATE_SPACESTATION_LIST && type != EnumSimplePacket.C_UPDATE_PLANETS_LIST && type != EnumSimplePacket.C_UPDATE_CONFIGS)
            {
                return;
            }
        }

        switch (this.type)
        {
        case C_AIR_REMAINING:
            if (String.valueOf(this.data.get(2)).equals(String.valueOf(PlayerUtil.getName(player))))
            {
                TickHandlerClient.airRemaining = (Integer) this.data.get(0);
                TickHandlerClient.airRemaining2 = (Integer) this.data.get(1);
            }
            break;
        case C_UPDATE_DIMENSION_LIST:
            if (String.valueOf(this.data.get(0)).equals(PlayerUtil.getName(player)))
            {
                String dimensionList = (String) this.data.get(1);
                if (ConfigManagerCore.enableDebug.get())
                {
                    if (!dimensionList.equals(PacketSimple.spamCheckString))
                    {
                        GCLog.info("DEBUG info: " + dimensionList);
                        PacketSimple.spamCheckString = dimensionList;
                    }
                }
                final String[] destinations = dimensionList.split("\\?");
                List<CelestialBody> possibleCelestialBodies = Lists.newArrayList();
                Map<DimensionType, Map<String, GuiCelestialSelection.StationDataGUI>> spaceStationData = Maps.newHashMap();
//                Map<String, String> spaceStationNames = Maps.newHashMap();
//                Map<String, Integer> spaceStationIDs = Maps.newHashMap();
//                Map<String, Integer> spaceStationHomes = Maps.newHashMap();

                for (String str : destinations)
                {
                    CelestialBody celestialBody = WorldUtil.getReachableCelestialBodiesForName(str);

                    if (celestialBody == null && str.contains("$"))
                    {
                        String[] values = str.split("\\$");

                        DimensionType homePlanetID = DimensionType.byName(new ResourceLocation(values[4]));

                        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
                        {
                            if (satellite.getParentPlanet().getDimensionType() == homePlanetID)
                            {
                                celestialBody = satellite;
                                break;
                            }
                        }

                        if (!spaceStationData.containsKey(homePlanetID))
                        {
                            spaceStationData.put(homePlanetID, new HashMap<String, GuiCelestialSelection.StationDataGUI>());
                        }

                        spaceStationData.get(homePlanetID).put(values[1], new GuiCelestialSelection.StationDataGUI(values[2], DimensionType.byName(new ResourceLocation(values[3]))));

//                        spaceStationNames.put(values[1], values[2]);
//                        spaceStationIDs.put(values[1], Integer.parseInt(values[3]));
//                        spaceStationHomes.put(values[1], Integer.parseInt(values[4]));
                    }

                    if (celestialBody != null)
                    {
                        possibleCelestialBodies.add(celestialBody);
                    }
                }

                if (Minecraft.getInstance().world != null)
                {
                    if (!(Minecraft.getInstance().currentScreen instanceof GuiCelestialSelection))
                    {
                        GuiCelestialSelection gui = new GuiCelestialSelection(false, possibleCelestialBodies, (Boolean) this.data.get(2));
                        gui.spaceStationMap = spaceStationData;
//                        gui.spaceStationNames = spaceStationNames;
//                        gui.spaceStationIDs = spaceStationIDs;
                        Minecraft.getInstance().displayGuiScreen(gui);
                    }
                    else
                    {
                        ((GuiCelestialSelection) Minecraft.getInstance().currentScreen).possibleBodies = possibleCelestialBodies;
                        ((GuiCelestialSelection) Minecraft.getInstance().currentScreen).spaceStationMap = spaceStationData;
//                        ((GuiCelestialSelection) Minecraft.getInstance().currentScreen).spaceStationNames = spaceStationNames;
//                        ((GuiCelestialSelection) Minecraft.getInstance().currentScreen).spaceStationIDs = spaceStationIDs;
                    }
                }
            }
            break;
        case C_SPAWN_SPARK_PARTICLES:
//            BlockPos pos = (BlockPos) this.data.get(0);
//            Minecraft mc = Minecraft.getInstance();
//
//            for (int i = 0; i < 4; i++)
//            {
//                if (mc.getRenderViewEntity() != null && mc.effectRenderer != null && mc.world != null)
//                {
//                    final ParticleSparks fx = new ParticleSparks(mc.world, pos.getX() - 0.15 + 0.5, pos.getY() + 1.2, pos.getZ() + 0.15 + 0.5, mc.world.rand.nextDouble() / 20 - mc.world.rand.nextDouble() / 20, mc.world.rand.nextDouble() / 20 - mc.world.rand.nextDouble() / 20);
//
//                    mc.effectRenderer.addEffect(fx);
//                }
//            } TODO
            break;
        case C_UPDATE_GEAR_SLOT:
            int subtype = (Integer) this.data.get(3);
            UUID gearUUID = (UUID) this.data.get(0);

            PlayerEntity gearDataPlayer = player.world.getPlayerByUuid(gearUUID);

            if (gearDataPlayer != null)
            {
                PlayerGearData gearData = ClientProxyCore.playerItemData.get(PlayerUtil.getName(gearDataPlayer));
                UUID gearDataPlayerID = gearDataPlayer.getUniqueID();

                if (gearData == null)
                {
                    gearData = new PlayerGearData(player);
                    if (!ClientProxyCore.gearDataRequests.contains(gearDataPlayerID))
                    {
                        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_GEAR_DATA1, getDimensionID(), new Object[]{gearDataPlayerID}));
                        ClientProxyCore.gearDataRequests.add(gearDataPlayerID);
                    }
                }
                else
                {
                    ClientProxyCore.gearDataRequests.remove(gearDataPlayerID);
                }

                EnumExtendedInventorySlot type = EnumExtendedInventorySlot.values()[(Integer) this.data.get(2)];
                GCPlayerHandler.EnumModelPacketType typeChange = GCPlayerHandler.EnumModelPacketType.values()[(Integer) this.data.get(1)];

                switch (type)
                {
                case MASK:
                    gearData.setMask(subtype);
                    break;
                case GEAR:
                    gearData.setGear(subtype);
                    break;
                case LEFT_TANK:
                    gearData.setLeftTank(subtype);
                    break;
                case RIGHT_TANK:
                    gearData.setRightTank(subtype);
                    break;
                case PARACHUTE:
                    if (typeChange == GCPlayerHandler.EnumModelPacketType.ADD)
                    {
                        String name;

                        if (subtype != -1)
                        {
                            name = ItemParaChute.names[subtype];
                            gearData.setParachute(new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/" + name + ".png"));
                        }
                    }
                    else
                    {
                        gearData.setParachute(null);
                    }
                    break;
                case FREQUENCY_MODULE:
                    gearData.setFrequencyModule(subtype);
                    break;
                case THERMAL_HELMET:
                    gearData.setThermalPadding(0, subtype);
                    break;
                case THERMAL_CHESTPLATE:
                    gearData.setThermalPadding(1, subtype);
                    break;
                case THERMAL_LEGGINGS:
                    gearData.setThermalPadding(2, subtype);
                    break;
                case THERMAL_BOOTS:
                    gearData.setThermalPadding(3, subtype);
                    break;
                case SHIELD_CONTROLLER:
                    gearData.setShieldController(subtype);
                    break;
                default:
                    break;
                }

                ClientProxyCore.playerItemData.put(gearUUID, gearData);
            }

            break;
        case C_CLOSE_GUI:
            Minecraft.getInstance().displayGuiScreen(null);
            break;
        case C_RESET_THIRD_PERSON:
            Minecraft.getInstance().gameSettings.thirdPersonView = stats.getThirdPersonView();
            break;
        case C_UPDATE_SPACESTATION_LIST:
            WorldUtil.decodeSpaceStationListClient(data);
            break;
//        case C_UPDATE_SPACESTATION_DATA:
//            SpaceStationWorldData var4 = SpaceStationWorldData.getMPSpaceStationData(player.world, (Integer) this.data.get(0), player);
//            var4.readFromNBT((CompoundNBT) this.data.get(1));
//            break; TODO ?
        case C_UPDATE_SPACESTATION_CLIENT_ID:
            ClientProxyCore.clientSpaceStationID = WorldUtil.stringToSpaceStationData((String) this.data.get(0));
            break;
        case C_UPDATE_PLANETS_LIST:
            WorldUtil.decodePlanetsListClient(data);
            break;
//        case C_UPDATE_CONFIGS:
//            ConfigManagerCore.saveClientConfigOverrideable.get()();
//            ConfigManagerCore.setConfigOverride.get()(data);
//            break;
        case C_ADD_NEW_SCHEMATIC:
            final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID((Integer) this.data.get(0));
            if (!stats.getUnlockedSchematics().contains(page))
            {
                stats.getUnlockedSchematics().add(page);
            }
            break;
        case C_UPDATE_SCHEMATIC_LIST:
            for (Object o : this.data)
            {
                Integer schematicID = (Integer) o;

                if (schematicID != -2)
                {
                    Collections.sort(stats.getUnlockedSchematics());

                    if (!stats.getUnlockedSchematics().contains(SchematicRegistry.getMatchingRecipeForID(schematicID)))
                    {
                        stats.getUnlockedSchematics().add(SchematicRegistry.getMatchingRecipeForID(schematicID));
                    }
                }
            }
            break;
        case C_PLAY_SOUND_BOSS_DEATH:
            player.playSound(GCSounds.bossDeath, 10.0F, (Float) this.data.get(0));
            break;
        case C_PLAY_SOUND_EXPLODE:
            player.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 10.0F, 0.7F);
            break;
        case C_PLAY_SOUND_BOSS_LAUGH:
            player.playSound(GCSounds.bossLaugh, 10.0F, 0.2F);
            break;
        case C_PLAY_SOUND_BOW:
            player.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 10.0F, 0.2F);
            break;
        case C_UPDATE_OXYGEN_VALIDITY:
            stats.setOxygenSetupValid((Boolean) this.data.get(0));
            break;
        case C_OPEN_PARACHEST_GUI:
//            switch ((Integer) this.data.get(1)) TODO
//            {
//            case 0:
//                if (player.getRidingEntity() instanceof EntityBuggy)
//                {
//                    Minecraft.getInstance().displayGuiScreen(new GuiBuggy(player.inventory, (EntityBuggy) player.getRidingEntity(), ((EntityBuggy) player.getRidingEntity()).getType()));
//                    player.openContainer.windowId = (Integer) this.data.get(0);
//                }
//                break;
//            case 1:
//                int entityID = (Integer) this.data.get(2);
//                Entity entity = player.world.getEntityByID(entityID);
//
//                if (entity != null && entity instanceof IInventorySettable)
//                {
//                    Minecraft.getInstance().displayGuiScreen(new GuiParaChest(player.inventory, (IInventorySettable) entity));
//                }
//
//                player.openContainer.windowId = (Integer) this.data.get(0);
//                break;
//            }
//            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_CONTAINER_SLOT_REFRESH, GCCoreUtil.getDimensionID(player.world), new Object[] { player.openContainer.windowId }));
            break;
        case C_UPDATE_WIRE_BOUNDS:
            TileEntity tile = player.world.getTileEntity((BlockPos) this.data.get(0));

            if (tile instanceof TileBaseConductor)
            {
                ((TileBaseConductor) tile).adjacentConnections = null;
//                player.world.getBlockState(tile.getPos()).getBlock().setBlockBoundsBasedOnState(player.world, tile.getPos()); TODO
            }
            break;
        case C_OPEN_SPACE_RACE_GUI:
//            if (Minecraft.getInstance().currentScreen == null)
//            {
//                TickHandlerClient.spaceRaceGuiScheduled = false;
//                player.openGui(GalacticraftCore.instance, GuiIdsCore.SPACE_RACE_START, player.world, (int) player.getPosX(), (int) player.getPosY(), (int) player.getPosZ());
//            }
//            else
//            {
//                TickHandlerClient.spaceRaceGuiScheduled = true;
//            } TODO
            break;
        case C_UPDATE_SPACE_RACE_DATA:
            Integer teamID = (Integer) this.data.get(0);
            String teamName = (String) this.data.get(1);
            FlagData flagData = (FlagData) this.data.get(2);
            Vector3 teamColor = (Vector3) this.data.get(3);
            List<String> playerList = new ArrayList<String>();

            for (int i = 4; i < this.data.size(); i++)
            {
                String playerName = (String) this.data.get(i);
                ClientProxyCore.flagRequestsSent.remove(playerName);
                playerList.add(playerName);
            }

            SpaceRace race = new SpaceRace(playerList, teamName, flagData, teamColor);
            race.setSpaceRaceID(teamID);
            SpaceRaceManager.addSpaceRace(race);
            break;
        case C_OPEN_JOIN_RACE_GUI:
            stats.setSpaceRaceInviteTeamID((Integer) this.data.get(0));
//            player.openGui(GalacticraftCore.instance, GuiIdsCore.SPACE_RACE_JOIN, player.world, (int) player.getPosX(), (int) player.getPosY(), (int) player.getPosZ()); TODO
            break;
        case C_UPDATE_DUNGEON_DIRECTION:
            stats.setDungeonDirection((Float) this.data.get(0));
            break;
        case C_UPDATE_FOOTPRINT_LIST:
            List<Footprint> printList = new ArrayList<Footprint>();
            long chunkKey = (Long) this.data.get(0);
            for (int i = 1; i < this.data.size(); i++)
            {
                Footprint print = (Footprint) this.data.get(i);
                if (!print.owner.equals(player.getName()))
                {
                    printList.add(print);
                }
            }
            FootprintRenderer.setFootprints(chunkKey, printList);
            break;
        case C_FOOTPRINTS_REMOVED:
            long chunkKey0 = (Long) this.data.get(0);
            BlockVec3 position = (BlockVec3) this.data.get(1);
            List<Footprint> footprintList = FootprintRenderer.footprints.get(chunkKey0);
            List<Footprint> toRemove = new ArrayList<Footprint>();

            if (footprintList != null)
            {
                for (Footprint footprint : footprintList)
                {
                    if (footprint.position.x > position.x && footprint.position.x < position.x + 1 &&
                            footprint.position.z > position.z && footprint.position.z < position.z + 1)
                    {
                        toRemove.add(footprint);
                    }
                }
            }

            if (!toRemove.isEmpty())
            {
                footprintList.removeAll(toRemove);
                FootprintRenderer.footprints.put(chunkKey0, footprintList);
            }
            break;
        case C_UPDATE_STATION_SPIN:
//            if (playerBaseClient.world.getDimension() instanceof DimensionSpaceStation)
//            {
//                ((DimensionSpaceStation) playerBaseClient.world.getDimension()).getSpinManager().setSpinRate((Float) this.data.get(0), (Boolean) this.data.get(1));
//            }
            break;
        case C_UPDATE_STATION_DATA:
//            if (playerBaseClient.world.getDimension() instanceof DimensionSpaceStation)
//            {
//                ((DimensionSpaceStation) playerBaseClient.world.getDimension()).getSpinManager().setSpinCentre((Double) this.data.get(0), (Double) this.data.get(1));
//            }
            break;
        case C_UPDATE_STATION_BOX:
//            if (playerBaseClient.world.getDimension() instanceof DimensionSpaceStation)
//            {
//                ((DimensionSpaceStation) playerBaseClient.world.getDimension()).getSpinManager().setSpinBox((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2), (Integer) this.data.get(3), (Integer) this.data.get(4), (Integer) this.data.get(5));
//            }
            break;
        case C_UPDATE_THERMAL_LEVEL:
            stats.setThermalLevel((Integer) this.data.get(0));
            stats.setThermalLevelNormalising((Boolean) this.data.get(1));
            break;
        case C_DISPLAY_ROCKET_CONTROLS:
//            player.sendMessage(new StringTextComponent(GameSettings.getKeymessage(KeyHandlerClient.spaceKey.getKeyCode()) + "  - " + GCCoreUtil.translate("gui.rocket.launch")));
//            player.sendMessage(new StringTextComponent(GameSettings.getKeymessage(KeyHandlerClient.leftKey.getKeyCode()) + " / " + GameSettings.getKeymessage(KeyHandlerClient.rightKey.getKeyCode()) + "  - " + GCCoreUtil.translate("gui.rocket.turn")));
//            player.sendMessage(new StringTextComponent(GameSettings.getKeymessage(KeyHandlerClient.accelerateKey.getKeyCode()) + " / " + GameSettings.getKeymessage(KeyHandlerClient.decelerateKey.getKeyCode()) + "  - " + GCCoreUtil.translate("gui.rocket.updown")));
//            player.sendMessage(new StringTextComponent(GameSettings.getKeymessage(KeyHandlerClient.openFuelGui.getKeyCode()) + "       - " + GCCoreUtil.translate("gui.rocket.inv"))); TODO
            break;
        case C_GET_CELESTIAL_BODY_LIST:
            String str = "";

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredPlanets().values())
            {
                str = str.concat(cBody.getUnlocalizedName() + ";");
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredMoons().values())
            {
                str = str.concat(cBody.getUnlocalizedName() + ";");
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredSatellites().values())
            {
                str = str.concat(cBody.getUnlocalizedName() + ";");
            }

            for (SolarSystem solarSystem : GalaxyRegistry.getRegisteredSolarSystems().values())
            {
                str = str.concat(solarSystem.getUnlocalizedName() + ";");
            }

            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_COMPLETE_CBODY_HANDSHAKE, getDimensionID(), new Object[]{str}));
            break;
        case C_UPDATE_ENERGYUNITS:
//            CommandGCEnergyUnits.handleParamClientside((Integer) this.data.get(0)); TODO Commands
            break;
        case C_RESPAWN_PLAYER:
//            final Dimension dimension = WorldUtil.getProviderForNameClient((String) this.data.get(0));
//            final DimensionType dimID = GCCoreUtil.getDimensionID(dimension);
//            if (ConfigManagerCore.enableDebug.get())
//            {
//                GCLog.info("DEBUG: Client receiving respawn packet for dim " + dimID);
//            }
//            int par2 = (Integer) this.data.get(1);
//            String par3 = (String) this.data.get(2);
//            int par4 = (Integer) this.data.get(3);
//            WorldUtil.forceRespawnClient(dimID, par2, par3, par4); TODO
            break;
        case C_UPDATE_STATS:
//            stats.setBuildFlags((Integer) this.data.get(0));
//            BlockPanelLighting.updateClient(this.data); TODO
            break;
        case C_UPDATE_TELEMETRY:
            tile = player.world.getTileEntity((BlockPos) this.data.get(0));
            if (tile instanceof TileEntityTelemetry)
            {
                ((TileEntityTelemetry) tile).receiveUpdate(data, this.getDimensionID());
            }
            break;
        case C_SEND_PLAYERSKIN:
            String strName = (String) this.data.get(0);
            String s1 = (String) this.data.get(1);
            String s2 = (String) this.data.get(2);
            String strUUID = (String) this.data.get(3);
            GameProfile gp = PlayerUtil.getOtherPlayerProfile(strName);
            if (gp == null)
            {
                gp = PlayerUtil.makeOtherPlayerProfile(strName, strUUID);
            }
            gp.getProperties().put("textures", new Property("textures", s1, s2));
            break;
        case C_SEND_OVERWORLD_IMAGE:
            try
            {
                int cx = (Integer) this.data.get(0);
                int cz = (Integer) this.data.get(1);
                byte[] bytes = (byte[]) this.data.get(2);
                MapUtil.receiveOverworldImageCompressed(cx, cz, bytes);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            break;
        case C_RECOLOR_PIPE:
            TileEntity tileEntity = player.world.getTileEntity((BlockPos) this.data.get(0));
            if (tileEntity instanceof TileEntityFluidPipe)
            {
                TileEntityFluidPipe pipe = (TileEntityFluidPipe) tileEntity;
                pipe.getNetwork().split(pipe);
                pipe.setNetwork(null);
            }
            break;
        case C_RECOLOR_ALL_GLASS:
//            BlockSpaceGlass.updateGlassColors((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2)); TODO
            break;
        case C_UPDATE_MACHINE_DATA:
            TileEntity tile3 = player.world.getTileEntity((BlockPos) this.data.get(0));
            if (tile3 instanceof ITileClientUpdates)
            {
                ((ITileClientUpdates) tile3).updateClient(this.data);
            }
            break;
        case C_LEAK_DATA:
            TileEntity tile4 = player.world.getTileEntity((BlockPos) this.data.get(0));
            if (tile4 instanceof TileEntityOxygenSealer)
            {
                ((ITileClientUpdates) tile4).updateClient(this.data);
            }
            break;
        case C_SPAWN_HANGING_SCHEMATIC:
//            EntityHangingSchematic entity = new EntityHangingSchematic(player.world, (BlockPos) this.data.get(0), Direction.byIndex((Integer) this.data.get(2)), (Integer) this.data.get(3));
//            ((ClientWorld)player.world).addEntityToWorld((Integer) this.data.get(1), entity); TODO
            break;
        default:
            break;
        }
    }

    @Override
    public void handleServerSide(PlayerEntity player)
    {
        final ServerPlayerEntity playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

        if (playerBase == null)
        {
            return;
        }

        final MinecraftServer server = playerBase.server;
        final GCPlayerStats stats = GCPlayerStats.get(playerBase);

        switch (this.type)
        {
        case S_RESPAWN_PLAYER:
            playerBase.connection.sendPacket(new SRespawnPacket(player.dimension, WorldInfo.byHashing(player.world.getWorldInfo().getSeed()), player.world.getWorldInfo().getGenerator(), playerBase.interactionManager.getGameType()));
            break;
        case S_TELEPORT_ENTITY:
            TickHandlerServer.scheduleNewDimensionChange(new ScheduledDimensionChange(playerBase, (DimensionType) PacketSimple.this.data.get(0)));
            stats.setUsingPlanetSelectionGui(false);
            break;
        case S_IGNITE_ROCKET:
            if (!player.world.isRemote && player.isAlive() && player.getRidingEntity() != null && player.getRidingEntity().isAlive() && player.getRidingEntity() instanceof EntityTieredRocket)
            {
                final EntityTieredRocket ship = (EntityTieredRocket) player.getRidingEntity();

                if (ship.launchPhase != EnumLaunchPhase.LANDING.ordinal())
                {
                    if (ship.hasValidFuel())
                    {
                        ItemStack stack2 = stats.getExtendedInventory().getStackInSlot(4);

                        if (stack2 != ItemStack.EMPTY && stack2.getItem() instanceof ItemParaChute || stats.getLaunchAttempts() > 0)
                        {
                            ship.igniteCheckingCooldown();
                            stats.setLaunchAttempts(0);
                        }
                        else if (stats.getChatCooldown() == 0 && stats.getLaunchAttempts() == 0)
                        {
                            player.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.rocket.warning.noparachute")));
                            stats.setChatCooldown(80);
                            stats.setLaunchAttempts(1);
                        }
                    }
                    else if (stats.getChatCooldown() == 0)
                    {
                        player.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.rocket.warning.nofuel")));
                        stats.setChatCooldown(250);
                    }
                }
            }
            break;
        case S_OPEN_SCHEMATIC_PAGE:
            if (player != null)
            {
                final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID((Integer) this.data.get(0));
                NetworkHooks.openGui((ServerPlayerEntity) player, page.getContainerProvider(player));

//                player.openGui(GalacticraftCore.instance, page.getGuiID(), player.world, (Integer) this.data.get(1), (Integer) this.data.get(2), (Integer) this.data.get(3)); TODO
            }
            break;
        case S_OPEN_FUEL_GUI:
            if (player.getRidingEntity() instanceof BuggyEntity)
            {
                INamedContainerProvider container = new SimpleNamedContainerProvider((w, p, pl) -> new ContainerBuggy(w, p, ((BuggyEntity) player.getRidingEntity()).getBuggyType()), new TranslationTextComponent("container.buggy"));
                NetworkHooks.openGui((ServerPlayerEntity) player, container);
            }
            else if (player.getRidingEntity() instanceof EntitySpaceshipBase)
            {
//                player.openGui(GalacticraftCore.instance, GuiIdsCore.ROCKET_INVENTORY, player.world, (int) player.getPosX(), (int) player.getPosY(), (int) player.getPosZ()); TODO
            }
            break;
        case S_UPDATE_SHIP_YAW:
            if (player.getRidingEntity() instanceof EntitySpaceshipBase)
            {
                final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.getRidingEntity();

                if (ship != null)
                {
                    ship.rotationYaw = (Float) this.data.get(0);
                }
            }
            break;
        case S_UPDATE_SHIP_PITCH:
            if (player.getRidingEntity() instanceof EntitySpaceshipBase)
            {
                final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.getRidingEntity();

                if (ship != null)
                {
                    ship.rotationPitch = (Float) this.data.get(0);
                }
            }
            break;
        case S_SET_ENTITY_FIRE:
            Entity entity = player.world.getEntityByID((Integer) this.data.get(0));

            if (entity instanceof LivingEntity)
            {
                entity.setFire(3);
            }
            break;
        case S_BIND_SPACE_STATION_ID:
            DimensionType homeID = (DimensionType) this.data.get(0);
            if ((!stats.getSpaceStationDimensionData().containsKey(homeID) || stats.getSpaceStationDimensionData().get(homeID) == DimensionType.THE_NETHER || stats.getSpaceStationDimensionData().get(homeID) == DimensionType.OVERWORLD)
                    && !ConfigManagerCore.disableSpaceStationCreation.get())
            {
                if (playerBase.abilities.isCreativeMode || WorldUtil.getSpaceStationRecipe(homeID).matches(playerBase, true))
                {
                    GCTriggers.CREATE_SPACE_STATION.trigger(playerBase);
//                    WorldUtil.bindSpaceStationToNewDimension(playerBase.world, playerBase, homeID);
                    DimensionType createdStation = WorldUtil.createNewSpaceStation(playerBase.getUniqueID(), false);
                    SpaceStationWorldData.getStationData(player.world.getServer(), createdStation.getRegistryName(), homeID, player);
//                    dimNames.put(newID, "Space Station " + newID);
                    stats.getSpaceStationDimensionData().put(homeID, createdStation);
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, GCCoreUtil.getDimensionType(player.world), new Object[] { WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData()) }), playerBase);
                }
            }
            break;
        case S_UNLOCK_NEW_SCHEMATIC:
            final Container container = player.openContainer;

            if (container instanceof ContainerSchematic)
            {
                final ContainerSchematic schematicContainer = (ContainerSchematic) container;

                ItemStack stack = schematicContainer.craftMatrix.getStackInSlot(0);

                if (!stack.isEmpty())
                {
                    final ISchematicPage page = SchematicRegistry.getMatchingRecipeForItemStack(stack);

                    if (page != null)
                    {
                        SchematicRegistry.unlockNewPage(playerBase, stack);
                        SpaceRaceManager.teamUnlockSchematic(playerBase, stack);
                        stack.shrink(1);

                        schematicContainer.craftMatrix.setInventorySlotContents(0, stack);
                        schematicContainer.craftMatrix.markDirty();

                        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_ADD_NEW_SCHEMATIC, getDimensionID(), new Object[]{page.getPageID()}), playerBase);
                    }
                }
            }
            break;
        case S_UPDATE_DISABLEABLE_BUTTON:
            final TileEntity tileAt = player.world.getTileEntity((BlockPos) this.data.get(0));

            if (tileAt instanceof IDisableableMachine)
            {
                final IDisableableMachine machine = (IDisableableMachine) tileAt;

                machine.setDisabled((Integer) this.data.get(1), !machine.getDisabled((Integer) this.data.get(1)));
            }
            break;
        case S_ON_FAILED_CHEST_UNLOCK:
            if (stats.getChatCooldown() == 0)
            {
                player.sendMessage(new StringTextComponent(GCCoreUtil.translateWithFormat("gui.chest.warning.wrongkey", this.data.get(0))));
                stats.setChatCooldown(100);
            }
            break;
        case S_RENAME_SPACE_STATION:
//            final SpaceStationWorldData ssdata = SpaceStationWorldData.getStationData(playerBase.world, (Integer) this.data.get(1), playerBase);
//
//            if (ssdata != null && ssdata.getOwner().equalsIgnoreCase(PlayerUtil.getName(player)))
//            {
//                ssdata.setSpaceStationName((String) this.data.get(0));
//                ssdata.setDirty(true);
//            } TODO
            break;
        case S_OPEN_EXTENDED_INVENTORY:
            INamedContainerProvider containerExtended = new SimpleNamedContainerProvider((w, p, pl) -> new ContainerExtendedInventory(w, p, stats.getExtendedInventory()), new TranslationTextComponent("container.cargo_loader"));
            NetworkHooks.openGui((ServerPlayerEntity) player, containerExtended);
            break;
        case S_ON_ADVANCED_GUI_CLICKED_INT:
            TileEntity tile1 = player.world.getTileEntity((BlockPos) this.data.get(1));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.redstoneActivation = (Integer) this.data.get(2) == 1;
                }
                break;
            case 1:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.playerDistanceActivation = (Integer) this.data.get(2) == 1;
                }
                break;
            case 2:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.playerDistanceSelection = (Integer) this.data.get(2);
                }
                break;
            case 3:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.playerNameMatches = (Integer) this.data.get(2) == 1;
                }
                break;
            case 4:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.invertSelection = (Integer) this.data.get(2) == 1;
                }
                break;
            case 5:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.lastHorizontalModeEnabled = airlockController.horizontalModeEnabled;
                    airlockController.horizontalModeEnabled = (Integer) this.data.get(2) == 1;
                }
                break;
            case 6:
                if (tile1 instanceof IBubbleProvider)
                {
                    IBubbleProvider distributor = (IBubbleProvider) tile1;
                    distributor.setBubbleVisible((Integer) this.data.get(2) == 1);
                }
                break;
            default:
                break;
            }
            break;
        case S_ON_ADVANCED_GUI_CLICKED_STRING:
            TileEntity tile2 = player.world.getTileEntity((BlockPos) this.data.get(1));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile2 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile2;
                    airlockController.playerToOpenFor = (String) this.data.get(2);
                }
                break;
            default:
                break;
            }
            break;
        case S_UPDATE_SHIP_MOTION_Y:
            int entityID = (Integer) this.data.get(0);
            boolean up = (Boolean) this.data.get(1);

            Entity entity2 = player.world.getEntityByID(entityID);

            if (entity2 instanceof EntityAutoRocket)
            {
                EntityAutoRocket autoRocket = (EntityAutoRocket) entity2;
//                autoRocket.motionY += up ? 0.02F : -0.02F; TODO
            }

            break;
        case S_START_NEW_SPACE_RACE:
            Integer teamID = (Integer) this.data.get(0);
            String teamName = (String) this.data.get(1);
            FlagData flagData = (FlagData) this.data.get(2);
            Vector3 teamColor = (Vector3) this.data.get(3);
            List<String> playerList = new ArrayList<String>();

            for (int i = 4; i < this.data.size(); i++)
            {
                playerList.add((String) this.data.get(i));
            }

            boolean previousData = SpaceRaceManager.getSpaceRaceFromID(teamID) != null;

            SpaceRace newRace = new SpaceRace(playerList, teamName, flagData, teamColor);

            if (teamID > 0)
            {
                newRace.setSpaceRaceID(teamID);
            }

            SpaceRaceManager.addSpaceRace(newRace);

            if (previousData)
            {
                SpaceRaceManager.sendSpaceRaceData(server, null, SpaceRaceManager.getSpaceRaceFromPlayer(PlayerUtil.getName(playerBase)));
            }
            break;
        case S_REQUEST_FLAG_DATA:
            SpaceRaceManager.sendSpaceRaceData(server, playerBase, SpaceRaceManager.getSpaceRaceFromPlayer((String) this.data.get(0)));
            break;
        case S_INVITE_RACE_PLAYER:
            ServerPlayerEntity playerInvited = PlayerUtil.getPlayerBaseServerFromPlayerUsername(server, (String) this.data.get(0), true);
            if (playerInvited != null)
            {
                Integer teamInvitedTo = (Integer) this.data.get(1);
                SpaceRace race = SpaceRaceManager.getSpaceRaceFromID(teamInvitedTo);

                if (race != null)
                {
                    GCPlayerStats.get(playerInvited).setSpaceRaceInviteTeamID(teamInvitedTo);
                    String dA = EnumColor.DARK_AQUA.getCode();
                    String bG = EnumColor.BRIGHT_GREEN.getCode();
                    String dB = EnumColor.PURPLE.getCode();
                    String teamNameTotal = "";
                    String[] teamNameSplit = race.getTeamName().split(" ");
                    for (String teamNamePart : teamNameSplit)
                    {
                        teamNameTotal = teamNameTotal.concat(dB + teamNamePart + " ");
                    }
                    playerInvited.sendMessage(new StringTextComponent(dA + GCCoreUtil.translateWithFormat("gui.space_race.chat.invite_received", bG + PlayerUtil.getName(player) + dA) + "  " + GCCoreUtil.translateWithFormat("gui.space_race.chat.to_join", teamNameTotal, EnumColor.AQUA + "/joinrace" + dA)).setStyle(new Style().setColor(TextFormatting.DARK_AQUA)));
                }
            }
            break;
        case S_REMOVE_RACE_PLAYER:
            Integer teamInvitedTo = (Integer) this.data.get(1);
            SpaceRace race = SpaceRaceManager.getSpaceRaceFromID(teamInvitedTo);

            if (race != null)
            {
                String playerToRemove = (String) this.data.get(0);

                if (!race.getPlayerNames().remove(playerToRemove))
                {
                    player.sendMessage(new StringTextComponent(GCCoreUtil.translateWithFormat("gui.space_race.chat.not_found", playerToRemove)));
                }
                else
                {
                    SpaceRaceManager.onPlayerRemoval(server, playerToRemove, race);
                }
            }
            break;
        case S_ADD_RACE_PLAYER:
            Integer teamToAddPlayer = (Integer) this.data.get(1);
            SpaceRace spaceRaceToAddPlayer = SpaceRaceManager.getSpaceRaceFromID(teamToAddPlayer);

            if (spaceRaceToAddPlayer != null)
            {
                String playerToAdd = (String) this.data.get(0);

                if (!spaceRaceToAddPlayer.getPlayerNames().contains(playerToAdd))
                {
                    SpaceRace oldRace = null;
                    while ((oldRace = SpaceRaceManager.getSpaceRaceFromPlayer(playerToAdd)) != null)
                    {
                        SpaceRaceManager.removeSpaceRace(oldRace);
                    }

                    spaceRaceToAddPlayer.getPlayerNames().add(playerToAdd);
                    SpaceRaceManager.sendSpaceRaceData(server, null, spaceRaceToAddPlayer);

                    for (String member : spaceRaceToAddPlayer.getPlayerNames())
                    {
                        ServerPlayerEntity memberObj = PlayerUtil.getPlayerForUsernameVanilla(server, member);

                        if (memberObj != null)
                        {
                            memberObj.sendMessage(new StringTextComponent(EnumColor.DARK_AQUA + GCCoreUtil.translateWithFormat("gui.space_race.chat.add_success", EnumColor.BRIGHT_GREEN + playerToAdd + EnumColor.DARK_AQUA)).setStyle(new Style().setColor(TextFormatting.DARK_AQUA)));
                        }
                    }
                }
                else
                {
                    player.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.space_race.chat.already_part")).setStyle(new Style().setColor(TextFormatting.DARK_RED)));
                }
            }
            break;
        case S_COMPLETE_CBODY_HANDSHAKE:
            String completeList = (String) this.data.get(0);
            List<String> clientObjects = Arrays.asList(completeList.split(";"));
            List<String> serverObjects = Lists.newArrayList();
            String missingObjects = "";

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredPlanets().values())
            {
                serverObjects.add(cBody.getUnlocalizedName());
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredMoons().values())
            {
                serverObjects.add(cBody.getUnlocalizedName());
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredSatellites().values())
            {
                serverObjects.add(cBody.getUnlocalizedName());
            }

            for (SolarSystem solarSystem : GalaxyRegistry.getRegisteredSolarSystems().values())
            {
                serverObjects.add(solarSystem.getUnlocalizedName());
            }

            for (String str : serverObjects)
            {
                if (!clientObjects.contains(str))
                {
                    missingObjects = missingObjects.concat(str + "\n");
                }
            }

            if (missingObjects.length() > 0)
            {
                playerBase.connection.disconnect(new StringTextComponent("Missing Galacticraft Celestial Objects:\n\n " + missingObjects));
            }

            break;
        case S_REQUEST_GEAR_DATA1:
        case S_REQUEST_GEAR_DATA2:
            UUID id = (UUID) this.data.get(0);
            if (id != null)
            {
                PlayerEntity otherPlayer = player.world.getPlayerByUuid(id);
                if (otherPlayer instanceof ServerPlayerEntity)
                {
                    GCPlayerHandler.checkGear((ServerPlayerEntity) otherPlayer, GCPlayerStats.get(otherPlayer), true);
                }
            }
            break;
        case S_BUILDFLAGS_UPDATE:
            stats.setBuildFlags((Integer) this.data.get(0));
            break;
        case S_REQUEST_OVERWORLD_IMAGE:
            MapUtil.sendOverworldToClient(playerBase);
            break;
        case S_REQUEST_MAP_IMAGE:
            int dim = (Integer) this.data.get(0);
            int cx = (Integer) this.data.get(1);
            int cz = (Integer) this.data.get(2);
            MapUtil.sendOrCreateMap(WorldUtil.getProviderForDimensionServer(DimensionType.getById(dim)).getWorld(), cx, cz, playerBase);
            break;
        case S_REQUEST_PLAYERSKIN:
            String strName = (String) this.data.get(0);
            ServerPlayerEntity playerRequested = server.getPlayerList().getPlayerByUsername(strName);

            //Player not online
            if (playerRequested == null)
            {
                return;
            }

            GameProfile gp = playerRequested.getGameProfile();
            if (gp == null)
            {
                return;
            }

            Property property = (Property) Iterables.getFirst(gp.getProperties().get("textures"), (Object) null);
            if (property == null)
            {
                return;
            }
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_SEND_PLAYERSKIN, getDimensionID(), new Object[]{strName, property.getValue(), property.getSignature(), playerRequested.getUniqueID().toString()}), playerBase);
            break;
        case S_CONTROL_ENTITY:
            if (player.getRidingEntity() != null && player.getRidingEntity() instanceof IControllableEntity)
            {
                ((IControllableEntity) player.getRidingEntity()).pressKey((Integer) this.data.get(0));
            }
            break;
        case S_NOCLIP_PLAYER:
            boolean noClip = (Boolean) this.data.get(0);
            if (player instanceof GCEntityPlayerMP)
            {
                GalacticraftCore.proxy.player.setNoClip((ServerPlayerEntity) player, noClip);
                if (noClip == false)
                {
                    player.fallDistance = 0.0F;
//                    ((ServerPlayerEntity)player).connection.floatingTickCount = 0; TODO
                }
            }
            else if (player instanceof ServerPlayerEntity)
            {
                ServerPlayerEntity emp = ((ServerPlayerEntity) player);
                try
                {
                    Field f = emp.interactionManager.getClass().getDeclaredField(GCCoreUtil.isDeobfuscated() ? "gameType" : "field_73091_c");
                    f.setAccessible(true);
                    if (noClip == false)
                    {
                        emp.fallDistance = 0.0F;
//                        emp.connection.floatingTickCount = 0; TODO
                        GameType gt = savedSettings.get(emp);
                        if (gt != null)
                        {
                            savedSettings.remove(emp);
                            f.set(emp.interactionManager, gt);
                        }
                    }
                    else
                    {
                        savedSettings.put(emp, emp.interactionManager.getGameType());
                        f.set(emp.interactionManager, GameType.SPECTATOR);
                    }
                }
                catch (Exception ee)
                {
                    ee.printStackTrace();
                }
            }
            break;
        case S_REQUEST_DATA:
            ServerWorld worldServer = server.getWorld((DimensionType) this.data.get(0));
            TileEntity requestedTile = worldServer.getTileEntity((BlockPos) this.data.get(1));
            if (requestedTile instanceof INetworkProvider)
            {
                if (((INetworkProvider) requestedTile).getNetwork() instanceof FluidNetwork)
                {
                    FluidNetwork network = (FluidNetwork) ((INetworkProvider) requestedTile).getNetwork();
                    network.addUpdate(playerBase);
                }
            }
            break;
        case S_UPDATE_CHECKLIST:
            for (Hand enumhand : Hand.values())
            {
                ItemStack stack = player.getHeldItem(enumhand);
                if (stack.getItem() == GCItems.PRELAUNCH_CHECKLIST)
                {
                    CompoundNBT tagCompound = stack.getTag();
                    if (tagCompound == null)
                    {
                        tagCompound = new CompoundNBT();
                    }
                    CompoundNBT tagCompoundRead = (CompoundNBT) this.data.get(0);
                    tagCompound.put("checklistData", tagCompoundRead);
                    stack.setTag(tagCompound);
                }
            }
            break;
        case S_REQUEST_MACHINE_DATA:
            TileEntity tile3 = player.world.getTileEntity((BlockPos) this.data.get(0));
            if (tile3 instanceof ITileClientUpdates)
            {
                ((ITileClientUpdates) tile3).sendUpdateToClient(playerBase);
            }
            break;
        case S_REQUEST_CONTAINER_SLOT_REFRESH:
            // It seems as though "Update slot" packets sent internally on the minecraft network packets are sent and
            // received before our custom gui open packets are handled. This causes slots to not update, because from
            // the client's perspective the gui isn't open yet. Sending this to the server causes all slots to be updated
            // server -> client
            if (player.openContainer.windowId == (Integer) this.data.get(0))
            {
//                for (int i = 0; i < player.openContainer.inventoryItemStacks.size(); ++i)
//                {
//                    player.openContainer.inventoryItemStacks.set(i, ItemStack.EMPTY);
//                } TODO
            }
            break;
        case S_ROTATE_ROCKET:
            Integer entityId = (Integer) this.data.get(0);
            if (entityId > 0)
            {
                Entity e = player.world.getEntityByID(entityId);
                if (e != null)
                {
                    e.rotationPitch = (float) this.data.get(1);
                    e.rotationYaw = (float) this.data.get(2);
                }
            }
        default:
            break;
        }
    }

    /*
     *
     * BEGIN "net.minecraft.network.Packet" IMPLEMENTATION
     *
     * This is for handling server->client packets before the player has joined the world
     *
     */

    @Override
    public void readPacketData(PacketBuffer var1)
    {
        this.decodeInto(var1);
    }

    @Override
    public void writePacketData(PacketBuffer var1)
    {
        this.encodeInto(var1);
    }

    public static void handle(final PacketSimple message, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT)
            {
                message.handleClientSide(Minecraft.getInstance().player);
            }
            else
            {
                message.handleServerSide(ctx.get().getSender());
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public boolean shouldSkipErrors()
    {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void processPacket(INetHandler handler)
    {
        if (this.type != EnumSimplePacket.C_UPDATE_SPACESTATION_LIST && this.type != EnumSimplePacket.C_UPDATE_PLANETS_LIST && this.type != EnumSimplePacket.C_UPDATE_CONFIGS)
        {
            return;
        }

        if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT)
        {
            this.handleClientSide(Minecraft.getInstance().player);
        }
    }

    /*
     *
     * END "net.minecraft.network.Packet" IMPLEMENTATION
     *
     * This is for handling server->client packets before the player has joined the world
     *
     */
}
