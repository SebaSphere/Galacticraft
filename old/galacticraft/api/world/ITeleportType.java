package micdoodle8.mods.galacticraft.api.world;

import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import java.util.Random;

/**
 * Implement into WorldProvider for customizing spawning players and other
 * entities into space dimension
 * <p/>
 * You can also create a separate class, implement it there, then register it in @GalacticraftRegistry
 */
public interface ITeleportType
{
    /**
     * This method is used to determine if a player will open parachute upon
     * entering the dimension
     *
     * @return whether player will set parachute open upon entering this
     * dimension
     */
    boolean useParachute();

    /**
     * Gets the player spawn location when entering this dimension
     *
     * @param world  The world to be spawned into
     * @param player The player to be teleported
     * @return a vector3 object containing the coordinates to be spawned into
     * the world with
     */
    Vector3D getPlayerSpawnLocation(ServerLevel world, ServerPlayer player);

    /**
     * Gets the entity (non-player) spawn location when entering this dimension
     *
     * @param world  The world to be spawned into
     * @param entity the non-player entity to be teleported
     * @return a vector3 object containing the coordinates to be spawned into
     * the world with
     */
    Vector3D getEntitySpawnLocation(ServerLevel world, Entity entity);

    /**
     * Gets the parachest spawn location when entering this dimension. Return
     * null for no parachest spawn
     *
     * @param world  The world to be spawned into
     * @param player The player being teleported
     * @param rand   The random instance
     * @return a vector3 object containing the coordinates to be spawned into
     * the world with. Return null for no spawn
     */
    Vector3D getParaChestSpawnLocation(ServerLevel world, ServerPlayer player, Random rand);

    /**
     * Called when player is transferred to a space dimension
     *
     * @param newWorld         The world object of the entered world
     * @param player           The player that has transferred dimensions
     * @param ridingAutoRocket If the player is riding an auto rocket. Do not spawn in
     *                         landers if so.
     */
    void onSpaceDimensionChanged(Level newWorld, ServerPlayer player, boolean ridingAutoRocket);


    /**
     * Used by Asteroids Survival game mode to set up the initial lander inventory
     *
     * @param player
     */
    void setupAdventureSpawn(ServerPlayer player);
}
