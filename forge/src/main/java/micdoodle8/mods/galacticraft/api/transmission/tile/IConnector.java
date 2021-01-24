package micdoodle8.mods.galacticraft.api.transmission.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import net.minecraft.core.Direction;

/**
 * Applied to TileEntities that can connect to an electrical OR oxygen network.
 *
 * @author Calclavia, micdoodle8
 */
public interface IConnector
{

    /**
     * @return If the connection is possible.
     */
    boolean canConnect(Direction direction, NetworkType type);
}
