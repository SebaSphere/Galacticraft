package micdoodle8.mods.galacticraft.planets.asteroids;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.client.FluidTexturesGC;
import micdoodle8.mods.galacticraft.planets.asteroids.client.fx.ParticleTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.*;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityBeamReceiverRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityBeamReflectorRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityMinerBaseRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityShortRangeTelepadRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.event.AsteroidsEventHandlerClient;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReflector;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static micdoodle8.mods.galacticraft.planets.asteroids.client.fx.AsteroidParticles.TELEPAD_DOWN;
import static micdoodle8.mods.galacticraft.planets.asteroids.client.fx.AsteroidParticles.TELEPAD_UP;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AsteroidsModuleClient implements IPlanetsModuleClient
{
    @Override
    public void init(FMLCommonSetupEvent event)
    {
//        RenderingRegistry.registerEntityRenderingHandler(EntitySmallAsteroid.class, (EntityRendererManager manager) -> new RenderSmallAsteroid(manager));
//        RenderingRegistry.registerEntityRenderingHandler(EntityGrapple.class, (EntityRendererManager manager) -> new RenderGrapple(manager));
//        RenderingRegistry.registerEntityRenderingHandler(EntityEntryPod.class, (EntityRendererManager manager) -> new RenderEntryPod(manager));
//        RenderingRegistry.registerEntityRenderingHandler(EntityTier3Rocket.class, (EntityRendererManager manager) -> new RenderTier3Rocket(manager));
//        RenderingRegistry.registerEntityRenderingHandler(EntityAstroMiner.class, (EntityRendererManager manager) -> new RenderAstroMiner(manager));
        MinecraftForge.EVENT_BUS.register(this);

        AsteroidsEventHandlerClient clientEventHandler = new AsteroidsEventHandlerClient();
        MinecraftForge.EVENT_BUS.register(clientEventHandler);
        FluidTexturesGC.init();
//        AsteroidsModuleClient.registerBlockRenderers();

        RenderType cutout = RenderType.getCutout();
        RenderTypeLookup.setRenderLayer(AsteroidBlocks.ASTRO_MINER_BASE, cutout);
        RenderTypeLookup.setRenderLayer(AsteroidBlocks.FULL_ASTRO_MINER_BASE, cutout);
        RenderTypeLookup.setRenderLayer(AsteroidBlocks.WALKWAY, cutout);
        RenderTypeLookup.setRenderLayer(AsteroidBlocks.WIRE_WALKWAY, cutout);
        RenderTypeLookup.setRenderLayer(AsteroidBlocks.FLUID_PIPE_WALKWAY, cutout);
        RenderType transluscent = RenderType.getTranslucent();
        RenderTypeLookup.setRenderLayer(AsteroidBlocks.DENSE_ICE, transluscent);

//          RenderingRegistry.registerEntityRenderingHandler(EntityAstroMiner.class, (RenderManager manager) -> new RenderAstroMiner());
        ClientRegistry.bindTileEntityRenderer(TileEntityBeamReflector.TYPE, TileEntityBeamReflectorRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityBeamReceiver.TYPE, TileEntityBeamReceiverRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityMinerBase.TYPE, TileEntityMinerBaseRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityShortRangeTelepad.TYPE, TileEntityShortRangeTelepadRenderer::new);

//        if (ModList.get().isLoaded("craftguide"))
//        {
//            CraftGuideIntegration.register();
//        }
        ClientProxyCore.setCustomModel(AsteroidsItems.TIER_3_ROCKET.getRegistryName(), modelToWrap -> new ItemModelRocketT3(modelToWrap));
        ClientProxyCore.setCustomModel(AsteroidsItems.TIER_3_ROCKET_18_INVENTORY.getRegistryName(), modelToWrap -> new ItemModelRocketT3(modelToWrap));
        ClientProxyCore.setCustomModel(AsteroidsItems.TIER_3_ROCKET_36_INVENTORY.getRegistryName(), modelToWrap -> new ItemModelRocketT3(modelToWrap));
        ClientProxyCore.setCustomModel(AsteroidsItems.TIER_3_ROCKET_54_INVENTORY.getRegistryName(), modelToWrap -> new ItemModelRocketT3(modelToWrap));
        ClientProxyCore.setCustomModel(AsteroidsItems.CREATIVE_TIER_3_ROCKET.getRegistryName(), modelToWrap -> new ItemModelRocketT3(modelToWrap));
        ClientProxyCore.setCustomModel(AsteroidsItems.GRAPPLE.getRegistryName(), modelToWrap -> new ItemModelGrapple(modelToWrap));
        ClientProxyCore.setCustomModel(AsteroidsItems.ASTRO_MINER.getRegistryName(), modelToWrap -> new ItemModelAstroMiner(modelToWrap));
        ClientProxyCore.setCustomModel(AsteroidBlocks.SHORT_RANGE_TELEPAD.getRegistryName(), modelToWrap -> new ItemModelTelepad(modelToWrap));
        ClientProxyCore.setCustomModel(AsteroidBlocks.ENERGY_BEAM_RECEIVER.getRegistryName(), modelToWrap -> new ItemModelBeamReceiver(modelToWrap));
        ClientProxyCore.setCustomModel(AsteroidBlocks.ENERGY_BEAM_REFLECTOR.getRegistryName(), modelToWrap -> new ItemModelBeamReflector(modelToWrap));
    }

//    @Override
//    public void registerVariants()
//    {
//        addPlanetVariants("asteroids_block", "asteroids_block", "asteroid_rock_1", "asteroid_rock_2", "ore_aluminum_asteroids", "ore_ilmenite_asteroids", "ore_iron_asteroids", "asteroid_deco", "titanium_block");
//        addPlanetVariants("thermal_padding", "thermal_padding", "thermal_chestplate", "thermal_leggings", "thermal_boots");
//        addPlanetVariants("item_basic_asteroids", "item_basic_asteroids", "engine_t2", "rocket_fins_t2", "shard_iron", "shard_titanium", "ingot_titanium", "compressed_titanium", "thermal_cloth", "beam_core", "dust_titanium");
//        addPlanetVariants("walkway", "walkway", "walkway_wire", "walkway_pipe");
//        addPlanetVariants("strange_seed", "strange_seed", "strange_seed1");
//
//        Item receiver = Item.getItemFromBlock(AsteroidBlocks.beamReceiver);
//        ModelResourceLocation modelResourceLocation = new ModelResourceLocation("galacticraftplanets:beam_receiver", "inventory");
//        ModelLoader.setCustomModelResourceLocation(receiver, 0, modelResourceLocation);
//
//        Item reflector = Item.getItemFromBlock(AsteroidBlocks.beamReflector);
//        modelResourceLocation = new ModelResourceLocation("galacticraftplanets:beam_reflector", "inventory");
//        ModelLoader.setCustomModelResourceLocation(reflector, 0, modelResourceLocation);
//
//        Item teleporter = Item.getItemFromBlock(AsteroidBlocks.shortRangeTelepad);
//        modelResourceLocation = new ModelResourceLocation("galacticraftplanets:telepad_short", "inventory");
//        ModelLoader.setCustomModelResourceLocation(teleporter, 0, modelResourceLocation);
//
//        modelResourceLocation = new ModelResourceLocation("galacticraftplanets:grapple", "inventory");
//        ModelLoader.setCustomModelResourceLocation(AsteroidsItems.grapple, 0, modelResourceLocation);
//
//        modelResourceLocation = new ModelResourceLocation("galacticraftplanets:rocket_t3", "inventory");
//        for (int i = 0; i < 5; ++i)
//        {
//            ModelLoader.setCustomModelResourceLocation(AsteroidsItems.tier3Rocket, i, modelResourceLocation);
//        }
//
//        modelResourceLocation = new ModelResourceLocation("galacticraftplanets:astro_miner", "inventory");
//        ModelLoader.setCustomModelResourceLocation(AsteroidsItems.astroMiner, 0, modelResourceLocation);
//    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onModelBakeEvent(ModelBakeEvent event)
    {
//        replaceModelDefault(event, "beam_receiver", "block/receiver.obj", ImmutableList.of("Main", "Receiver", "Ring"), ItemModelBeamReceiver.class, TRSRTransformation.identity(), "inventory", "facing=up", "facing=down", "facing=north", "facing=west", "facing=east", "facing=south");
//        replaceModelDefault(event, "beam_reflector", "block/reflector.obj", ImmutableList.of("Base", "Axle", "EnergyBlaster", "Ring"), ItemModelBeamReflector.class, TRSRTransformation.identity(), "inventory", "normal");
//        replaceModelDefault(event, "telepad_short", "block/telepad_short.obj", ImmutableList.of("Top", "Bottom", "Connector"), ItemModelTelepad.class, TRSRTransformation.identity(), "inventory", "normal");
//        replaceModelDefault(event, "grapple", "grapple.obj", ImmutableList.of("Grapple"), ItemModelGrapple.class, TRSRTransformation.identity());
//        replaceModelDefault(event, "rocket_t3", "tier3rocket.obj", ImmutableList.of("Boosters", "Cube", "NoseCone", "Rocket"), ItemModelRocketT3.class, TRSRTransformation.identity());
//        replaceModelDefault(event, "astro_miner", "astro_miner_inv.obj", ImmutableList.of("Hull_Center"), ItemModelAstroMiner.class, TRSRTransformation.identity());

//        RenderAstroMiner.updateModels(event.getModelLoader());
//        RenderEntryPod.updateModels(event.getModelLoader());
//        RenderGrapple.updateModel();
//        RenderTier3Rocket.updateModels(event.getModelLoader());
//        TileEntityBeamReflectorRenderer.updateModels(event.getModelLoader());
//        TileEntityBeamReceiverRenderer.updateModels();
//        TileEntityMinerBaseRenderer.updateModels(event.getModelLoader());
//        TileEntityShortRangeTelepadRenderer.updateModels(event.getModelLoader());
    }

//    private void replaceModelDefault(ModelBakeEvent event, String resLoc, String objLoc, List<String> visibleGroups, Class<? extends ModelTransformWrapper> clazz, IModelState parentState, String... variants)
//    {
//        ClientUtil.replaceModel(GalacticraftPlanets.ASSET_PREFIX, event, resLoc, objLoc, visibleGroups, clazz, parentState, variants);
//    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void loadTextures(TextureStitchEvent.Pre event)
    {
        registerTexture(event, "entity/astro_miner_base");
        registerTexture(event, "entity/energy_beam_reflector");
        registerTexture(event, "entity/energy_beam_receiver");
        registerTexture(event, "entity/short_range_telepad/base");
        registerTexture(event, "entity/short_range_telepad/input");
        registerTexture(event, "entity/grapple");
        registerTexture(event, "entity/tier_3_rocket");
        registerTexture(event, "entity/astro_miner/main");
        registerTexture(event, "entity/astro_miner/off");
        registerTexture(event, "entity/astro_miner/fx");
        registerTexture(event, "entity/entry_pod/pod");
        registerTexture(event, "block/argon");
        registerTexture(event, "block/atmospheric_gases");
        registerTexture(event, "block/carbondioxide");
        registerTexture(event, "block/helium");
        registerTexture(event, "block/liquid_argon");
        registerTexture(event, "block/liquid_methane");
        registerTexture(event, "block/liquid_nitrogen");
        registerTexture(event, "block/liquid_oxygen");
        registerTexture(event, "block/methane");
        registerTexture(event, "block/nitrogen");
    }

    private static void registerTexture(TextureStitchEvent.Pre event, String texture)
    {
        event.addSprite(new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + texture));
    }

//    public static void registerBlockRenderers()
//    {
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 0, "asteroids_block");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 1, "asteroid_rock_1");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 2, "asteroid_rock_2");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 3, "ore_aluminum_asteroids");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 4, "ore_ilmenite_asteroids");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 5, "ore_iron_asteroids");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 6, "asteroid_deco");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 7, "titanium_block");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockWalkway, 0, "walkway");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockWalkway, 1, "walkway_wire");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockWalkway, 2, "walkway_pipe");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockDenseIce);
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockMinerBase);
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.minerBaseFull);
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.thermalPadding, 0, "thermal_padding");  //helm
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.thermalPadding, 1, "thermal_chestplate");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.thermalPadding, 2, "thermal_leggings");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.thermalPadding, 3, "thermal_boots");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 5, "item_basic_asteroids");  //reinforced_plate_t3
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 1, "engine_t2");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 2, "rocket_fins_t2");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 3, "shard_iron");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 4, "shard_titanium");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 0, "ingot_titanium");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 6, "compressed_titanium");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 7, "thermal_cloth");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 8, "beam_core");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 9, "dust_titanium");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.heavyNoseCone, 0, "heavy_nose_cone");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.strangeSeed, 0, "strange_seed");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.strangeSeed, 1, "strange_seed1");
//    }

//    private void addPlanetVariants(String name, String... variants)
//    {
//        Item itemBlockVariants = Item.REGISTRY.getObject(new ResourceLocation(Constants.MOD_ID_PLANETS, name));
//        ResourceLocation[] variants0 = new ResourceLocation[variants.length];
//        for (int i = 0; i < variants.length; ++i)
//        {
//            variants0[i] = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + variants[i]);
//        }
//        ModelBakery.registerItemVariants(itemBlockVariants, variants0);
//    }

//    @Override
//    public void getGuiIDs(List<Integer> idList)
//    {
//        idList.add(GuiIdsPlanets.MACHINE_ASTEROIDS);
//    }

//    @Override
//    public Object getGuiElement(LogicalSide LogicalSide, int ID, PlayerEntity player, World world, int x, int y, int z)
//    {
//        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
//
//        switch (ID)
//        {
//        case GuiIdsPlanets.MACHINE_ASTEROIDS:
//
//            if (tile instanceof TileEntityShortRangeTelepad)
//            {
//                return new GuiShortRangeTelepad(player.inventory, ((TileEntityShortRangeTelepad) tile));
//            }
//            if (tile instanceof TileEntityMinerBase)
//            {
//                return new GuiAstroMinerDock(player.inventory, (TileEntityMinerBase) tile);
//            }
//
//            break;
//        }
//
//        return null;
//    }

//    @Override
//    public void addParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData)
//    {
//        Minecraft mc = Minecraft.getInstance();
//
//        if (mc != null && mc.getRenderViewEntity() != null && mc.particles != null)
//        {
//            double dX = mc.getRenderViewEntity().posX - position.x;
//            double dY = mc.getRenderViewEntity().posY - position.y;
//            double dZ = mc.getRenderViewEntity().posZ - position.z;
//            Particle particle = null;
//            double viewDistance = 64.0D;
//
//            if (dX * dX + dY * dY + dZ * dZ < viewDistance * viewDistance)
//            {
//                if (particleID.equals("portalBlue"))
//                {
//                    particle = new ParticleTelepad(mc.world, position, motion, (TileEntityShortRangeTelepad) extraData[0], (Boolean) extraData[1]);
//                }
//                else if (particleID.equals("cryoFreeze"))
//                {
//                    particle = new EntityCryoFX(mc.world, position, motion);
//                }
//            }
//
//            if (particle != null)
//            {
//                mc.particles.addEffect(particle);
//            }
//        }
//    }

    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(TELEPAD_DOWN, ParticleTelepad.DownFactory::new);
        Minecraft.getInstance().particles.registerFactory(TELEPAD_UP, ParticleTelepad.UpFactory::new);
    }
}
