package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.locale.Language;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.dimension.Dimension;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.io.Charsets;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GCCoreUtil
{
    public static int nextID = 0;
    private static boolean deobfuscated;
    private static final String lastLang = "";
    public static boolean langDisable;
    private static MinecraftServer serverCached;

    static
    {
        try
        {
            Class.forName("net.minecraft.world.World");
            deobfuscated = true;
        }
        catch (final Exception e)
        {
            deobfuscated = false;
//            e.printStackTrace();
        }
    }

    public static boolean isDeobfuscated()
    {
        return deobfuscated;
    }

//    public static void openBuggyInv(ServerPlayerEntity player, IInventory buggyInv, int type)
//    {
//        player.getNextWindowId();
//        player.closeContainer();
//        int id = player.currentWindowId;
//        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_PARACHEST_GUI, GCCoreUtil.getDimensionID(player.world), new Object[] { id, 0, 0 }), player);
//        player.openContainer = new ContainerBuggy(player.inventory, buggyInv, type, player);
//        player.openContainer.windowId = id;
//        player.openContainer.addListener(player);
//    }
//
//    public static void openParachestInv(ServerPlayerEntity player, EntityLanderBase landerInv)
//    {
//        player.getNextWindowId();
//        player.closeContainer();
//        int windowId = player.currentWindowId;
//        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_PARACHEST_GUI, GCCoreUtil.getDimensionID(player.world), new Object[] { windowId, 1, landerInv.getEntityId() }), player);
//        player.openContainer = new ContainerParaChest(player.inventory, landerInv, player);
//        player.openContainer.windowId = windowId;
//        player.openContainer.addListener(player);
//    }

//    public static int nextInternalID()
//    {
//        GCCoreUtil.nextID++;
//        return GCCoreUtil.nextID - 1;
//    }
//
//    public static void registerGalacticraftCreature(Class<? extends Entity> clazz, String name, int back, int fore)
//    {
//        registerGalacticraftNonMobEntity(clazz, name, 80, 3, true);
//        int nextEggID = getNextValidID();
//        if (nextEggID < 65536)
//        {
//            ResourceLocation resourcelocation = new ResourceLocation(Constants.MOD_ID_CORE, name);
////            name = Constants.MOD_ID_CORE + "." + name;
//            EntityList.ENTITY_EGGS.put(resourcelocation, new EntityList.EntityEggInfo(resourcelocation, back, fore));
//        }
//    }
//
//    public static int getNextValidID()
//    {
//        int eggID = 255;
//
//        //Non-global entity IDs - for egg ID purposes - can be greater than 255
//        //The spawn egg will have this metadata.  Metadata up to 65535 is acceptable (see potions).
//
//        do
//        {
//            eggID++;
//        }
//        while (net.minecraftforge.registries.GameData.getEntityRegistry().getValue(eggID) != null);
//
//        return eggID;
//    }
//
//    public static void registerGalacticraftNonMobEntity(Class<? extends Entity> var0, String var1, int trackingDistance, int updateFreq, boolean sendVel)
//    {
//        ResourceLocation registryName = new ResourceLocation(Constants.MOD_ID_CORE, var1);
//        EntityRegistry.registerModEntity(registryName, var0, var1, nextInternalID(), GalacticraftCore.instance, trackingDistance, updateFreq, sendVel);
//    }

//    public static void registerGalacticraftItem(String key, Item item)
//    {
//        registerGalacticraftItem(key, new ItemStack(item));
//    }

//    public static void registerGalacticraftItem(String key, Item item, int metadata)
//    {
//        registerGalacticraftItem(key, new ItemStack(item, 1, metadata));
//    }

//    public static void registerGalacticraftItem(String key, ItemStack stack)
//    {
//        GalacticraftCore.itemList.add(stack);
//    }

//    public static void registerGalacticraftBlock(String key, Block block)
//    {
//        GalacticraftCore.blocksList.add(block);
//    }

    public static String translate(String key)
    {
        String result = Language.getInstance().getElement(key);
        int comment = result.indexOf('#');
        String ret = (comment > 0) ? result.substring(0, comment).trim() : result;
        for (int i = 0; i < key.length(); ++i)
        {
            Character c = key.charAt(i);
            if (Character.isUpperCase(c))
            {
                System.err.println(ret);
            }
        }
        return ret;
    }

    public static List<String> translateWithSplit(String key)
    {
        String translated = translate(key);
        int comment = translated.indexOf('#');
        translated = (comment > 0) ? translated.substring(0, comment).trim() : translated;
        return Arrays.asList(translated.split("\\$"));
    }

    public static String translateWithFormat(String key, Object... values)
    {
        String translation = Language.getInstance().getElement(key);
        String result;

        try
        {
            result = String.format(translation, values);
        }
        catch (IllegalFormatException var5)
        {
            result = "Format error: " + translation;
        }

        int comment = result.indexOf('#');
        String ret = (comment > 0) ? result.substring(0, comment).trim() : result;
        for (int i = 0; i < key.length(); ++i)
        {
            Character c = key.charAt(i);
            if (Character.isUpperCase(c))
            {
                System.err.println(ret);
            }
        }
        return ret;
    }

    public static void drawStringRightAligned(String string, int x, int y, int color, Font fontRendererObj)
    {
        fontRendererObj.draw(string, x - fontRendererObj.width(string), y, color);
    }

    public static void drawStringCentered(String string, int x, int y, int color, Font fontRendererObj)
    {
        fontRendererObj.draw(string, x - fontRendererObj.width(string) / 2, y, color);
    }

    public static String lowerCaseNoun(String string)
    {
        net.minecraft.client.resources.language.Language l = Minecraft.getInstance().getLanguageManager().getSelected();
        if (l.getCode().equals("de_DE"))
        {
            return string;
        }
        return GCCoreUtil.translate(string).toLowerCase();
    }

    @Nullable
    public static InputStream supplementEntityKeys(InputStream inputstream, String assetprefix) throws IOException
    {
        ArrayList<String> langLines = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
        String line;
        String supplemented = "entity." + assetprefix.toLowerCase() + ".";

        //TODO:  We could also load en_US here and have any language keys not in the other lang set to the en_US value

        while ((line = br.readLine()) != null)
        {
            line = line.trim();
            if (!line.isEmpty())
            {
                langLines.add(line);
                if (line.startsWith("entity."))
                {
                    langLines.add(supplemented + line.substring(7));
                }
            }
        }

        ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputstream, Charsets.UTF_8.newEncoder()));
        for (String outLine : langLines)
        {
            writer.write(outLine + "\n");
        }
        writer.close();

        return new ByteArrayInputStream(outputstream.toByteArray());
    }

//    public static void loadLanguage(String langIdentifier, String assetPrefix, File source)
//    {
//        if (!lastLang.equals(langIdentifier))
//        {
//            langDisable = false;
//        }
//        if (langDisable) return;
//        String langFile = "assets/" + assetPrefix + "/lang/" + langIdentifier.substring(0, 3).toLowerCase() + langIdentifier.substring(3).toUpperCase() + ".lang";
//        InputStream stream = null;
//        ZipFile zip = null;
//        try
//        {
//            if (source.isDirectory() && (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"))
//            {
//                stream = new FileInputStream(new File(source.toURI().resolve(langFile).getPath()));
//            }
//            else
//            {
//                zip = new ZipFile(source);
//                ZipEntry entry = zip.getEntry(langFile);
//                if(entry == null) throw new FileNotFoundException();
//                stream = zip.getInputStream(entry);
//            }
//            LanguageMap.inject(GCCoreUtil.supplementEntityKeys(stream, assetPrefix));
//        }
//        catch(FileNotFoundException fnf)
//        {
//            langDisable = true;
//        }
//        catch(Exception ignore) { }
//        finally
//        {
//            if (stream != null) IOUtils.closeQuietly(stream);
//            try
//            {
//                if (zip != null) zip.close();
//            }
//            catch (IOException ignore) {}
//        }
//    } TODO ?

    public static DimensionType getDimensionType(LevelReader world)
    {
        return world.getDimension().getType();
    }

    public static DimensionType getDimensionType(Dimension dimension)
    {
        return dimension.getType();
    }

    public static DimensionType getDimensionType(BlockEntity tileEntity)
    {
        return tileEntity.getLevel().dimension.getType();
    }

    public static Iterable<ServerLevel> getWorldServerList()
    {
        MinecraftServer server = getServer();
        if (server != null)
        {
            return server.getAllLevels();
        }
        return null;
    }

    public static Iterable<ServerLevel> getWorldServerList(Level world)
    {
        if (world instanceof ServerLevel)
        {
            return ((ServerLevel) world).getServer().getAllLevels();
        }
        return GCCoreUtil.getWorldServerList();
    }

    public static void sendToAllDimensions(EnumSimplePacket packetType, Object[] data)
    {
        for (ServerLevel world : GCCoreUtil.getWorldServerList())
        {
            DimensionType id = getDimensionType(world);
            GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(packetType, id, data), id);
        }
    }

    public static void sendToAllAround(PacketSimple packet, Level world, DimensionType dimID, BlockPos pos, double radius)
    {
        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.5D;
        double z = pos.getZ() + 0.5D;
        double r2 = radius * radius;
        for (Player playerMP : world.players())
        {
            if (playerMP.dimension == dimID)
            {
                final double dx = x - playerMP.getX();
                final double dy = y - playerMP.getY();
                final double dz = z - playerMP.getZ();

                if (dx * dx + dy * dy + dz * dz < r2)
                {
                    GalacticraftCore.packetPipeline.sendTo(packet, (ServerPlayer) playerMP);
                }
            }
        }
    }

    /**
     * Call this to obtain a seeded random which will be the SAME on
     * client and server.  This means EntityItems won't jump position, for example.
     */
    public static Random getRandom(BlockPos pos)
    {
        long blockSeed = ((pos.getY() << 28) + pos.getX() + 30000000 << 28) + pos.getZ() + 30000000;
        return new Random(blockSeed);
    }

    /**
     * Returns the angle of the compass (0 - 360 degrees) needed to reach the given position offset
     */
    public static float getAngleForRelativePosition(double nearestX, double nearestZ)
    {
        return ((float) Mth.atan2(nearestX, -nearestZ) * Constants.RADIANS_TO_DEGREES + 360F) % 360F;
    }

    /**
     * Custom getEffectiveSide method, covering more cases than FMLCommonHandler
     */
    public static LogicalSide getEffectiveSide()
    {
        if (EffectiveSide.get() == LogicalSide.SERVER || Thread.currentThread().getName().startsWith("Netty Epoll Server IO"))
        {
            return LogicalSide.SERVER;
        }

        return LogicalSide.CLIENT;
    }

    public static MinecraftServer getServer()
    {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null)
        {
            // This may be called from a different thread e.g. MapUtil
            // If on a different thread, FMLCommonHandler.instance().getMinecraftServerInstance() can return null on LAN servers
            // (I think because the FMLCommonHandler wrongly picks the client proxy if it's not in the Integrated Server thread)
            return serverCached;
        }
        return server;
    }

    public static ItemStack getMatchingItemEitherHand(Player player, Item item)
    {
        ItemStack stack = player.inventory.getItem(player.inventory.selected);
        if (stack != null && stack.getItem() == item)
        {
            return stack;
        }
        stack = player.inventory.offhand.get(0);
        if (stack != null && stack.getItem() == item)
        {
            return stack;
        }
        return null;
    }

    //For performance
    public static List<BlockPos> getPositionsAdjoining(BlockPos pos)
    {
        LinkedList<BlockPos> result = new LinkedList<>();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (y > 0)
        {
            result.add(new BlockPos(x, y - 1, z));
        }
        if (y < 255)
        {
            result.add(new BlockPos(x, y + 1, z));
        }
        result.add(new BlockPos(x, y, z - 1));
        result.add(new BlockPos(x, y, z + 1));
        result.add(new BlockPos(x - 1, y, z));
        result.add(new BlockPos(x + 1, y, z));
        return result;
    }

    //For performance
    public static void getPositionsAdjoining(int x, int y, int z, List<BlockPos> result)
    {
        result.clear();
        if (y > 0)
        {
            result.add(new BlockPos(x, y - 1, z));
        }
        if (y < 255)
        {
            result.add(new BlockPos(x, y + 1, z));
        }
        result.add(new BlockPos(x, y, z - 1));
        result.add(new BlockPos(x, y, z + 1));
        result.add(new BlockPos(x - 1, y, z));
        result.add(new BlockPos(x + 1, y, z));
    }

    public static void getPositionsAdjoiningLoaded(int x, int y, int z, List<BlockPos> result, Level world)
    {
        result.clear();
        if (y > 0)
        {
            result.add(new BlockPos(x, y - 1, z));
        }
        if (y < 255)
        {
            result.add(new BlockPos(x, y + 1, z));
        }
        BlockPos pos = new BlockPos(x, y, z - 1);
        if ((z & 15) > 0 || world.hasChunkAt(pos))
        {
            result.add(pos);
        }
        pos = new BlockPos(x, y, z + 1);
        if ((z & 15) < 15 || world.hasChunkAt(pos))
        {
            result.add(pos);
        }
        pos = new BlockPos(x - 1, y, z);
        if ((x & 15) > 0 || world.hasChunkAt(pos))
        {
            result.add(pos);
        }
        pos = new BlockPos(x + 1, y, z);
        if ((x & 15) < 15 || world.hasChunkAt(pos))
        {
            result.add(pos);
        }
    }

    //For performance
    public static void getPositionsAdjoining(BlockPos pos, List<BlockPos> result)
    {
        result.clear();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (y > 0)
        {
            result.add(new BlockPos(x, y - 1, z));
        }
        if (y < 255)
        {
            result.add(new BlockPos(x, y + 1, z));
        }
        result.add(new BlockPos(x, y, z - 1));
        result.add(new BlockPos(x, y, z + 1));
        result.add(new BlockPos(x - 1, y, z));
        result.add(new BlockPos(x + 1, y, z));
    }

    public static void spawnItem(Level world, BlockPos pos, ItemStack stack)
    {
        float var = 0.7F;
        while (!stack.isEmpty())
        {
            double dx = world.random.nextFloat() * var + (1.0F - var) * 0.5D;
            double dy = world.random.nextFloat() * var + (1.0F - var) * 0.5D;
            double dz = world.random.nextFloat() * var + (1.0F - var) * 0.5D;
            ItemEntity entityitem = new ItemEntity(world, pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz, stack.split(world.random.nextInt(21) + 10));

            entityitem.setPickUpDelay(10);

            world.addFreshEntity(entityitem);
        }
    }

    public static void notifyStarted(MinecraftServer server)
    {
        serverCached = server;
    }
}
