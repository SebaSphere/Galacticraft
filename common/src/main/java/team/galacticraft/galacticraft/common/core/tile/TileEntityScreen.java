package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.GalacticraftRegistry;
import team.galacticraft.galacticraft.common.api.tile.ITileClientUpdates;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.blocks.BlockScreen;
import team.galacticraft.galacticraft.common.core.client.screen.DrawGameScreen;
import team.galacticraft.galacticraft.common.core.tick.TickHandlerClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
//import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileEntityScreen extends TileEntityAdvanced implements ITileClientUpdates
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.screen)
    public static BlockEntityType<TileEntityScreen> TYPE;

    public static float FRAMEBORDER = 0.098F;  //used for rendering
    public int imageType;
    public DrawGameScreen screen;
    public boolean connectedUp;
    public boolean connectedDown;
    public boolean connectedLeft;
    public boolean connectedRight;
    public int connectionsUp;
    public int connectionsDown;
    public int connectionsLeft;
    public int connectionsRight;
    public boolean isMultiscreen;

    public int screenOffsetx = 0;
    public int screenOffsetz = 0;

    private final int requiresUpdate = 0;
    private boolean doneClientUpdate = false;
    //Used on client EnvType only
    public boolean refreshOnUpdate = false;
    private AABB renderAABB;
    private static final boolean LOGGING = false;

    public TileEntityScreen()
    {
        super(TYPE);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    public void setConnectedUp(boolean connectedUpL)
    {
        this.connectedUp = connectedUpL;
    }

    public void setConnectedDown(boolean connectedDownL)
    {
        this.connectedDown = connectedDownL;
    }

    public void setConnectedLeft(boolean connectedLeftL)
    {
        this.connectedLeft = connectedLeftL;
    }

    public void setConnectedRight(boolean connectedRightL)
    {
        this.connectedRight = connectedRightL;
    }

    public BlockEntity setVerifiedConnectedUp(BlockVec3 vec, Direction direction)
    {
        BlockEntity tileUp = vec.getTileEntityOnSide(this.level, 1);
        this.setConnectedUp(tileUp instanceof TileEntityScreen && tileUp.getBlockState().getValue(BlockScreen.FACING) == direction && !tileUp.isRemoved());
        return tileUp;
    }

    public BlockEntity setVerifiedConnectedDown(BlockVec3 vec, Direction direction)
    {
        BlockEntity tileDown = vec.getTileEntityOnSide(this.level, 0);
        this.setConnectedDown(tileDown instanceof TileEntityScreen && tileDown.getBlockState().getValue(BlockScreen.FACING) == direction && !tileDown.isRemoved());
        return tileDown;
    }

    public BlockEntity setVerifiedConnectedLeft(BlockVec3 vec, Direction direction)
    {
        Direction side = this.getLeft(direction);
        BlockEntity tileLeft = vec.getTileEntityOnSide(this.level, side);
        this.setConnectedLeft(tileLeft instanceof TileEntityScreen && tileLeft.getBlockState().getValue(BlockScreen.FACING) == direction && !tileLeft.isRemoved());
        return tileLeft;
    }

    public BlockEntity setVerifiedConnectedRight(BlockVec3 vec, Direction direction)
    {
        Direction side = this.getRight(direction);
        BlockEntity tileRight = vec.getTileEntityOnSide(this.level, side);
        this.setConnectedRight(tileRight instanceof TileEntityScreen && tileRight.getBlockState().getValue(BlockScreen.FACING) == direction && !tileRight.isRemoved());
        return tileRight;
    }

    @Override
    public void onLoad()
    {
        if (this.level.isClientSide)
        {
            this.clientOnLoad();
            this.screen = new DrawGameScreen(1.0F, 1.0F, this);
        }
    }

    @Override
    public void buildDataPacket(int[] data)
    {
        data[0] = this.imageType;
        int connectedFlags = 0;
        if (this.connectedUp)
        {
            connectedFlags += 8;
        }
        if (this.connectedDown)
        {
            connectedFlags += 4;
        }
        if (this.connectedLeft)
        {
            connectedFlags += 2;
        }
        if (this.connectedRight)
        {
            connectedFlags += 1;
        }
        data[1] = connectedFlags;
    }

    public Direction getFront()
    {
        return this.getFacing(this.level.getBlockState(getBlockPos()));
    }

    private Direction getFacing(BlockState state)
    {
        return state.getValue(BlockScreen.FACING);
    }

    /**
     * Call when a screen (which maybe part of a multiscreen) is either
     * broken or rotated.
     *
     * @param state The state of the screen prior to breaking or rotation
     */
    public void breakScreen(BlockState state)
    {
        BlockVec3 vec = new BlockVec3(this);
        Direction direction = state.getValue(BlockScreen.FACING);
        BlockEntity tile;
        Direction facingRight = getFacing(state).getClockWise();

        int left = this.connectionsLeft;
        int right = this.connectionsRight;
        int up = this.connectionsUp;
        int down = this.connectionsDown;

        boolean doUp = this.connectedUp;
        boolean doDown = this.connectedDown;
        boolean doLeft = this.connectedLeft;
        boolean doRight = this.connectedRight;

        for (int x = -left; x <= right; x++)
        {
            for (int z = -up; z <= down; z++)
            {
                if (x == 0 && z == 0)
                {
                    this.resetToSingle();
                }
                else
                {
                    BlockVec3 newVec = vec.clone().modifyPositionFromSide(facingRight, x).modifyPositionFromSide(Direction.DOWN, z);
                    tile = newVec.getTileEntity(this.level);
                    if (tile instanceof TileEntityScreen && tile.getBlockState().getValue(BlockScreen.FACING) == direction)
                    {
                        ((TileEntityScreen) tile).resetToSingle();
                    }
                }
            }
        }

        //TODO  Try to generate largest screen possible out of remaining blocks

        this.setConnectedDown(false);
        this.setConnectedUp(false);
        this.setConnectedLeft(false);
        this.setConnectedRight(false);

        if (doUp)
        {
            tile = vec.getTileEntityOnSide(this.level, 1);
            if (tile instanceof TileEntityScreen && tile.getBlockState().getValue(BlockScreen.FACING) == direction && !tile.isRemoved())
            {
                if (doLeft)
                {
                    ((TileEntityScreen) tile).setConnectedLeft(true);
                }
                if (doRight)
                {
                    ((TileEntityScreen) tile).setConnectedRight(true);
                }
                ((TileEntityScreen) tile).setConnectedUp(true);
//				if (doLeft) ((TileEntityScreen)tile).connectedLeft = true;
//				if (doRight) ((TileEntityScreen)tile).connectedRight = true;
//				((TileEntityScreen)tile).connectedUp = true;
                ((TileEntityScreen) tile).refreshConnections(true);
            }
        }
        if (doDown)
        {
            tile = vec.getTileEntityOnSide(this.level, 0);
            if (tile instanceof TileEntityScreen && tile.getBlockState().getValue(BlockScreen.FACING) == direction && !tile.isRemoved())
            {
                if (doLeft)
                {
                    ((TileEntityScreen) tile).setConnectedLeft(true);
                }
                if (doRight)
                {
                    ((TileEntityScreen) tile).setConnectedRight(true);
                }
                ((TileEntityScreen) tile).setConnectedDown(true);
//				if (doLeft) ((TileEntityScreen)tile).connectedLeft = true;
//				if (doRight) ((TileEntityScreen)tile).connectedRight = true;
//				((TileEntityScreen)tile).connectedDown = true;
                ((TileEntityScreen) tile).refreshConnections(true);
            }
        }
        if (doLeft)
        {
            tile = vec.getTileEntityOnSide(this.level, this.getLeft(direction));
            if (tile instanceof TileEntityScreen && tile.getBlockState().getValue(BlockScreen.FACING) == direction && !tile.isRemoved())
            {
                if (doUp)
                {
                    ((TileEntityScreen) tile).setConnectedUp(true);
                }
                if (doDown)
                {
                    ((TileEntityScreen) tile).setConnectedDown(true);
                }
                ((TileEntityScreen) tile).setConnectedLeft(true);
//				if (doUp) ((TileEntityScreen)tile).connectedUp = true;
//				if (doDown) ((TileEntityScreen)tile).connectedDown = true;
//				((TileEntityScreen)tile).connectedLeft = true;
                ((TileEntityScreen) tile).refreshConnections(true);
            }
        }
        if (doRight)
        {
            tile = vec.getTileEntityOnSide(this.level, this.getRight(direction));
            if (tile instanceof TileEntityScreen && tile.getBlockState().getValue(BlockScreen.FACING) == direction && !tile.isRemoved())
            {
                if (doUp)
                {
                    ((TileEntityScreen) tile).setConnectedUp(true);
                }
                if (doDown)
                {
                    ((TileEntityScreen) tile).setConnectedDown(true);
                }
                ((TileEntityScreen) tile).setConnectedRight(true);
//				if (doUp) ((TileEntityScreen)tile).connectedUp = true;
//				if (doDown) ((TileEntityScreen)tile).connectedDown = true;
//				((TileEntityScreen)tile).connectedRight = true;
                ((TileEntityScreen) tile).refreshConnections(true);
            }
        }
    }

    /**
     * Check whether the screen can sustain 'multi-screen' connections on each of its 4 sides
     * (note: this can be called recursively from inside itself)
     *
     * @param doScreen If true, build a new multi-screen if connections are found
     */
    public void refreshConnections(boolean doScreen)
    {
        if (LOGGING)
        {
            this.log("Starting connection check");
        }

        BlockState iblockstate = this.level.getBlockState(this.worldPosition);
        Direction facing = iblockstate.getValue(BlockScreen.FACING);

//        int meta = this.getBlockMetadata();
        if (facing.getAxis() == Direction.Axis.Y)
        {
            //TODO System.out.println("Up/down oriented screens cannot be multiscreen");
            this.resetToSingle();
            return;
        }

        BlockEntity tileUp = null;
        BlockEntity tileDown = null;
        BlockEntity tileLeft = null;
        BlockEntity tileRight = null;
        BlockVec3 vec = new BlockVec3(this);

        //First, basic check that a neighbour is there and in the same orientation
        if (this.connectedUp)
        {
            tileUp = this.setVerifiedConnectedUp(vec, facing);
        }
        if (this.connectedDown)
        {
            tileDown = this.setVerifiedConnectedDown(vec, facing);
        }
        if (this.connectedLeft)
        {
            tileLeft = this.setVerifiedConnectedLeft(vec, facing);
        }
        if (this.connectedRight)
        {
            tileRight = this.setVerifiedConnectedRight(vec, facing);
        }

        //Now test whether a connection can be sustained with that other tile
        if (this.connectedUp)
        {
            this.setConnectedUp(this.tryConnectUp((TileEntityScreen) tileUp));
        }

        if (this.connectedDown)
        {
            this.setConnectedDown(this.tryConnectDown((TileEntityScreen) tileDown));
        }

        if (this.connectedLeft)
        {
            this.setConnectedLeft(this.tryConnectLeft((TileEntityScreen) tileLeft));
        }

        if (this.connectedRight)
        {
            this.setConnectedRight(this.tryConnectRight((TileEntityScreen) tileRight));
        }
        if (LOGGING)
        {
            this.log("Ending connection check");
        }
        if (doScreen)
        {
            this.checkScreenSize();
            this.setChanged();
        }
    }

//	@Override
//    public boolean canUpdate()
//    {
//        return false;
//    }

    /**
     * Cycle through different screen contents
     */
    public void changeChannel()
    {
        if (!this.level.isClientSide)
        {
            if (++this.imageType >= GalacticraftRegistry.getMaxScreenTypes())
            {
                this.imageType = 0;
            }

            boolean flag = false;

            if (!this.connectedRight && this.canJoinRight())
            {
                this.joinRight();
                flag = true;
            }
            else if (!this.connectedLeft && this.canJoinLeft())
            {
                this.joinLeft();
                flag = true;
            }
            else if (!this.connectedUp && this.canJoinUp())
            {
                this.joinUp();
                flag = true;
            }
            else if (!this.connectedDown && this.canJoinDown())
            {
                this.joinDown();
                flag = true;
            }

            this.doneClientUpdate = false;
            this.refreshConnections(true);
            this.setChanged();

            if (!this.doneClientUpdate)
            {
                this.updateAllInDimension();
            }
        }
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.imageType = nbt.getInt("type");
        this.connectionsDown = nbt.getInt("connectionsDown");
        this.connectionsUp = nbt.getInt("connectionsUp");
        this.connectionsLeft = nbt.getInt("connectionsLeft");
        this.connectionsRight = nbt.getInt("connectionsRight");
        this.isMultiscreen = nbt.getBoolean("multiscreen");
        this.setConnectedUp(this.connectionsUp > 0);
        this.setConnectedDown(this.connectionsDown > 0);
        this.setConnectedLeft(this.connectionsLeft > 0);
        this.setConnectedRight(this.connectionsRight > 0);
//        this.connectedUp = (this.connectionsUp > 0);
//        this.connectedDown = (this.connectionsDown > 0);
//        this.connectedLeft = (this.connectionsLeft > 0);
//        this.connectedRight = (this.connectionsRight > 0);
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putInt("type", this.imageType);
        nbt.putInt("connectionsDown", this.connectionsDown);
        nbt.putInt("connectionsUp", this.connectionsUp);
        nbt.putInt("connectionsLeft", this.connectionsLeft);
        nbt.putInt("connectionsRight", this.connectionsRight);
        nbt.putBoolean("multiscreen", this.isMultiscreen);
        return nbt;
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }

    public void checkScreenSize()
    {
        if (LOGGING)
        {
            this.log("Checking screen size");
        }
        int up = 0;
        int down = 0;
        int left = 0;
        int right = 0;
        Direction facing = this.getBlockState().getValue(BlockScreen.FACING);

        BlockVec3 vec = new BlockVec3(this);
        TileEntityScreen tile = this;
        while (up < 8)
        {
            if (tile.connectedUp)
            {
                up++;
                BlockEntity newTile = vec.getTileEntityOnSide(this.level, 1);
                if (newTile instanceof TileEntityScreen)
                {
                    tile = (TileEntityScreen) newTile;
                    vec.translate(0, 1, 0);
                }
                else
                {
                    System.out.println("Debug - connected up to a non-screen tile");
//    				tile.connectedUp = false;
                    this.setConnectedUp(false);
                    tile.setChanged();
                    up--;
                    break;
                }
            }
            else
            {
                break;
            }
        }

        vec = new BlockVec3(this);
        tile = this;
        while (down < 8 - up)
        {
            if (tile.connectedDown)
            {
                down++;
                BlockEntity newTile = vec.getTileEntityOnSide(this.level, 0);
                if (newTile instanceof TileEntityScreen)
                {
                    tile = (TileEntityScreen) newTile;
                    vec.translate(0, -1, 0);
                }
                else
                {
                    System.out.println("Debug - connected down to a non-screen tile");
//    				tile.connectedDown = false;
                    this.setConnectedDown(false);
                    tile.setChanged();
                    down--;
                    break;
                }
            }
            else
            {
                break;
            }
        }

        vec = new BlockVec3(this);
        tile = this;
        Direction leftside = this.getLeft(facing);
        while (left < ((up + down == 0) ? 1 : 8))
        {
            if (tile.connectedLeft)
            {
                left++;
                BlockEntity newTile = vec.getTileEntityOnSide(this.level, leftside);
                if (newTile instanceof TileEntityScreen)
                {
                    tile = (TileEntityScreen) newTile;
                    vec = vec.newVecSide(leftside);
                }
                else
                {
                    System.out.println("Debug - connected left to a non-screen tile");
//    				tile.connectedLeft = false;
                    this.setConnectedLeft(false);
                    tile.setChanged();
                    left--;
                    break;
                }
            }
            else
            {
                break;
            }
        }

        vec = new BlockVec3(this);
        tile = this;
        Direction rightside = this.getRight(facing);
        while (right < ((up + down == 0) ? 1 : 8) - left)
        {
            if (tile.connectedRight)
            {
                right++;
                BlockEntity newTile = vec.getTileEntityOnSide(this.level, rightside);
                if (newTile instanceof TileEntityScreen)
                {
                    tile = (TileEntityScreen) newTile;
                    vec = vec.newVecSide(rightside);
                }
                else
                {
                    System.out.println("Debug - connected right to a non-screen tile");
//    				tile.connectedRight = false;
                    this.setConnectedRight(false);
                    tile.setChanged();
                    right--;
                    break;
                }
            }
            else
            {
                break;
            }
        }

        if (LOGGING)
        {
            this.log("Screen size check midpoint " + up + " " + down + " " + left + " " + right + " ");
        }

        vec = new BlockVec3(this);
        BlockEntity newtile = vec.getTileEntityOnSide(this.level, 1);
        TileEntityScreen tileUp = (newtile instanceof TileEntityScreen) ? (TileEntityScreen) newtile : null;
        newtile = vec.getTileEntityOnSide(this.level, 0);
        TileEntityScreen tileDown = (newtile instanceof TileEntityScreen) ? (TileEntityScreen) newtile : null;
        newtile = vec.getTileEntityOnSide(this.level, leftside);
        TileEntityScreen tileLeft = (newtile instanceof TileEntityScreen) ? (TileEntityScreen) newtile : null;
        newtile = vec.getTileEntityOnSide(this.level, rightside);
        TileEntityScreen tileRight = (newtile instanceof TileEntityScreen) ? (TileEntityScreen) newtile : null;
        //Prevent 3 x 1 and longer
        if (left + right == 0 && up + down >= 1)
        {
            if (up > 0 && !tileUp.connectedUp)  //No need for null check if up > 0
            {
                up = 1;
                down = 0;
            }
            else
            {
                up = 0;
                if (tileDown != null && !tileDown.connectedDown)
                {
                    down = 1;
                }
                else
                {
                    down = 0;
                }
            }
        }
        if (up + down == 0 && left + right >= 1)
        {
            if (left > 0 && !tileLeft.connectedLeft)  //No need for null check if right > 0
            {
                if (right == 0 || tileRight == null || tileRight.connectionsLeft == 0)
                {
                    left = 1;
                    right = 0;
                }
                else
                {
                    left = 0;
                    right = 1;
                }
            }
            else
            {
                left = 0;
                if (tileRight != null && !tileRight.connectedRight)
                {
                    right = 1;
                }
                else
                {
                    right = 0;
                }
            }
        }

        if (up == 0)
        {
            this.setConnectedUp(false);
            if (tileUp != null)
            {
                tileUp.setConnectedDown(false);
            }
//    		this.connectedUp = false;
//    		if (tileUp != null) tileUp.connectedDown = false;
        }
        if (down == 0)
        {
            this.setConnectedDown(false);
            if (tileUp != null)
            {
                tileUp.setConnectedUp(false);
            }
//    		this.connectedDown = false;
//    		if (tileDown != null) tileDown.connectedUp = false;
        }
        if (left == 0)
        {
            this.setConnectedLeft(false);
            if (tileUp != null)
            {
                tileUp.setConnectedRight(false);
            }
//    		this.connectedLeft = false;
//    		if (tileLeft != null) tileLeft.connectedRight = false;
        }
        if (right == 0)
        {
            this.setConnectedRight(false);
            if (tileUp != null)
            {
                tileUp.setConnectedLeft(false);
            }
//    		this.connectedRight = false;
//    		if (tileRight != null) tileRight.connectedLeft = false;
        }

        if (LOGGING)
        {
            this.log("Finished screen size check");
        }

        this.checkWholeScreen(up, down, left, right);
    }

    /**
     * After figuring out the screen edges (overall screen dimensions)
     * check that the screen is a whole A x B rectangle with no tiles missing
     * <p>
     * If it is whole, set all tiles in the screen to match this screen type
     *
     * @param up    Number of blocks the screen edge is away from this in the up direction
     * @param down  Number of blocks the screen edge is away from this in the down direction
     * @param left  Number of blocks the screen edge is away from this in the left direction
     * @param right Number of blocks the screen edge is away from this in the right direction
     * @return True if the screen was whole
     */
    private boolean checkWholeScreen(int up, int down, int left, int right)
    {
        if (up + down + left + right == 0 || up < 0 || down < 0 || left < 0 || right < 0)
        {
            this.doneClientUpdate = true;
            this.resetToSingle();
            return true;
        }

        //System.out.println("Checking screen size at "+this.getPos().getX()+","+this.getPos().getZ()+": Up "+up+" Dn "+down+" Lf "+left+" Rg "+right);

        boolean screenWhole = true;
        boolean existingScreen = false;
        int barrierUp = up;
        int barrierDown = down;
        int barrierLeft = left;
        int barrierRight = right;

        Direction facing = this.getBlockState().getValue(BlockScreen.FACING);
        BlockVec3 vec = new BlockVec3(this);
        ArrayList<TileEntityScreen> screenList = new ArrayList<TileEntityScreen>();

//		int side = this.getRight(facing);
        Direction side = getFront().getClockWise();

        for (int x = -left; x <= right; x++)
        {
            for (int z = -up; z <= down; z++)
            {
                BlockVec3 newVec = vec.clone().modifyPositionFromSide(side, x).modifyPositionFromSide(Direction.DOWN, z);
                BlockEntity tile = newVec.getTileEntity(this.level);
                if (tile instanceof TileEntityScreen && tile.getBlockState().getValue(BlockScreen.FACING) == facing && !tile.isRemoved())
                {
                    TileEntityScreen screenTile = (TileEntityScreen) tile;
                    screenList.add(screenTile);

                    if (screenTile.isMultiscreen)
                    {
                        if (screenTile.connectionsUp > z + up)
                        {
                            barrierUp = -z - 1;
                            existingScreen = true;
                        }
                        if (screenTile.connectionsDown > down - z)
                        {
                            barrierDown = z - 1;
                            existingScreen = true;
                        }
                        if (screenTile.connectionsLeft > x + left)
                        {
                            barrierLeft = -x - 1;
                            existingScreen = true;
                        }
                        if (screenTile.connectionsRight > right - x)
                        {
                            barrierRight = x - 1;
                            existingScreen = true;
                        }
                    }
                }
                else
                {
                    screenWhole = false;
                }
            }
        }

        if (!screenWhole)
        {
            for (TileEntityScreen scr : screenList)
            {
                scr.resetToSingle();
            }

            return false;
        }

        if (existingScreen)
        {
            return this.checkWholeScreen(barrierUp, barrierDown, barrierLeft, barrierRight);
        }

        DrawGameScreen newScreen = null;
        boolean serverside = true;
        BlockEntity bottomLeft = vec.clone().modifyPositionFromSide(side, -left).modifyPositionFromSide(Direction.DOWN, down).getTileEntity(this.level);
        if (this.level.isClientSide)
        {
            if (bottomLeft instanceof TileEntityScreen)  //It always will be if reached this far
            {
                newScreen = ((TileEntityScreen) bottomLeft).screen;
                if (!newScreen.check(1.0F + left + right, 1.0F + up + down))
                {
                    newScreen = new DrawGameScreen(1.0F + left + right, 1.0F + up + down, bottomLeft);
                }
            }
            serverside = false;
        }

        Iterator<TileEntityScreen> it = screenList.iterator();
        for (int x = -left; x <= right; x++)
        {
            for (int z = -up; z <= down; z++)
            {
                TileEntityScreen screenTile = it.next();
                screenTile.screenOffsetx = x + left;
                screenTile.screenOffsetz = z + up;
                screenTile.screen = newScreen;
                screenTile.connectionsLeft = x + left;
                screenTile.connectionsRight = right - x;
                screenTile.connectionsUp = z + up;
                screenTile.connectionsDown = down - z;
                screenTile.isMultiscreen = true;
                screenTile.refreshOnUpdate = false;
                if (serverside)
                {
                    screenTile.imageType = this.imageType;
                    screenTile.setChanged();
                    screenTile.updateAllInDimension();
                }
                screenTile.refreshConnections(false);
            }
        }

        this.connectionsUp = up;
        this.connectionsDown = down;
        this.connectionsLeft = left;
        this.connectionsRight = right;

        return true;
    }

    /**
     * Reset the screen to a 1x1 size, not part of a 'multi-screen'
     */
    public void resetToSingle()
    {
        this.screenOffsetx = 0;
        this.screenOffsetz = 0;
        this.connectionsUp = 0;
        this.connectionsDown = 0;
        this.connectionsLeft = 0;
        this.connectionsRight = 0;
        this.isMultiscreen = false;
//    	this.connectedDown = this.connectedLeft = this.connectedRight = this.connectedUp = false;
        this.setConnectedLeft(false);
        this.setConnectedRight(false);
        this.setConnectedUp(false);
        this.setConnectedDown(false);
        this.refreshOnUpdate = false;
        this.setChanged();
        if (this.level.isClientSide)
        {
            this.screen = new DrawGameScreen(1.0F, 1.0F, this);
        }
        else
        {
            this.updateAllInDimension();
        }
    }

    /**
     * Get the Minecraft direction which is on the left EnvType
     * for the block orientation given by metadata
     */
    private Direction getLeft(Direction direction)
    {
        return direction.getCounterClockWise();
//        switch (meta)
//        {
//        case 2:
//            return 4;
//        case 3:
//            return 5;
//        case 4:
//            return 3;
//        case 5:
//            return 2;
//        }
//        return 4;
    }

    /**
     * Get the Minecraft direction which is on the right EnvType
     * for the block orientation given by metadata
     */
    private Direction getRight(Direction direction)
    {
        return direction.getClockWise();
//        switch (direction)
//        {
//        case NORTH:
//            return EAST;
//        case SOUTH:
//            return WEST;
//        case WEST:
//            return NORTH;
//        case EAST:
//            return SOUTH;
//        }
//        return EAST;
    }

    private boolean canJoinRight()
    {
        Direction dir = this.getBlockState().getValue(BlockScreen.FACING);
        BlockEntity te = new BlockVec3(this).getTileEntityOnSide(this.level, this.getRight(dir));
        if (!(te instanceof TileEntityScreen))
        {
            return false;
        }
        TileEntityScreen screenTile = (TileEntityScreen) te;
        if (screenTile.getBlockState().getValue(BlockScreen.FACING) != dir)
        {
            return false;
        }
        if (screenTile.connectionsUp != this.connectionsUp)
        {
            return false;
        }
        if (screenTile.connectionsDown != this.connectionsDown)
        {
            return false;
        }
        if (this.connectionsUp + this.connectionsDown > 0)
        {
            return true;
        }
        if (this.connectionsLeft > 0)
        {
            return false;
        }
        return screenTile.connectionsRight <= 0;
    }


    private boolean canJoinLeft()
    {
        Direction dir = this.getBlockState().getValue(BlockScreen.FACING);
        BlockEntity te = new BlockVec3(this).getTileEntityOnSide(this.level, this.getLeft(dir));
        if (!(te instanceof TileEntityScreen))
        {
            return false;
        }
        TileEntityScreen screenTile = (TileEntityScreen) te;
        if (screenTile.getBlockState().getValue(BlockScreen.FACING) != dir)
        {
            return false;
        }
        if (screenTile.connectionsUp != this.connectionsUp)
        {
            return false;
        }
        if (screenTile.connectionsDown != this.connectionsDown)
        {
            return false;
        }
        if (this.connectionsUp + this.connectionsDown > 0)
        {
            return true;
        }
        if (this.connectionsRight > 0)
        {
            return false;
        }
        return screenTile.connectionsLeft <= 0;
    }

    private boolean canJoinUp()
    {
        Direction dir = this.getBlockState().getValue(BlockScreen.FACING);
        BlockEntity te = new BlockVec3(this).getTileEntityOnSide(this.level, 1);
        if (!(te instanceof TileEntityScreen))
        {
            return false;
        }
        TileEntityScreen screenTile = (TileEntityScreen) te;
        if (screenTile.getBlockState().getValue(BlockScreen.FACING) != dir)
        {
            return false;
        }
        if (screenTile.connectionsLeft != this.connectionsLeft)
        {
            return false;
        }
        if (screenTile.connectionsRight != this.connectionsRight)
        {
            return false;
        }
        if (this.connectionsLeft + this.connectionsRight > 0)
        {
            return true;
        }
        if (this.connectionsDown > 0)
        {
            return false;
        }
        return screenTile.connectionsUp <= 0;
    }

    private boolean canJoinDown()
    {
        Direction facing = this.getBlockState().getValue(BlockScreen.FACING);
        BlockEntity te = new BlockVec3(this).getTileEntityOnSide(this.level, 0);
        if (!(te instanceof TileEntityScreen))
        {
            return false;
        }
        TileEntityScreen screenTile = (TileEntityScreen) te;
        if (screenTile.getBlockState().getValue(BlockScreen.FACING) != facing)
        {
            return false;
        }
        if (screenTile.connectionsLeft != this.connectionsLeft)
        {
            return false;
        }
        if (screenTile.connectionsRight != this.connectionsRight)
        {
            return false;
        }
        if (this.connectionsLeft + this.connectionsRight > 0)
        {
            return true;
        }
        if (this.connectionsUp > 0)
        {
            return false;
        }
        return screenTile.connectionsDown <= 0;
    }

    private void joinRight()
    {
        Direction facing = this.getBlockState().getValue(BlockScreen.FACING);
        Direction side = this.getRight(facing);
        BlockVec3 vec = new BlockVec3(this);
        for (int z = -this.connectionsUp; z <= this.connectionsDown; z++)
        {
            BlockEntity tile;
            BlockVec3 newVec = vec.clone().modifyPositionFromSide(Direction.DOWN, z);
            if (z == 0)
            {
                tile = this;
            }
            else
            {
                tile = newVec.getTileEntity(this.level);
            }
            if (tile instanceof TileEntityScreen && tile.getBlockState().getValue(BlockScreen.FACING) == facing && !tile.isRemoved())
            {
                TileEntityScreen screenTile = (TileEntityScreen) tile;
//				screenTile.connectedRight = true;
                screenTile.setConnectedRight(true);
                BlockEntity te2 = newVec.getTileEntityOnSide(this.level, side);
                if (te2 instanceof TileEntityScreen && te2.getBlockState().getValue(BlockScreen.FACING) == facing && !te2.isRemoved())
                {
                    screenTile.tryConnectRight((TileEntityScreen) te2);
                }
            }
        }
    }

    private void joinLeft()
    {
        Direction facing = this.getBlockState().getValue(BlockScreen.FACING);
        Direction side = this.getLeft(facing);
        BlockVec3 vec = new BlockVec3(this);
        for (int z = -this.connectionsUp; z <= this.connectionsDown; z++)
        {
            BlockEntity tile;
            BlockVec3 newVec = vec.clone().modifyPositionFromSide(Direction.DOWN, z);
            if (z == 0)
            {
                tile = this;
            }
            else
            {
                tile = newVec.getTileEntity(this.level);
            }
            if (tile instanceof TileEntityScreen && tile.getBlockState().getValue(BlockScreen.FACING) == facing && !tile.isRemoved())
            {
                TileEntityScreen screenTile = (TileEntityScreen) tile;
//				screenTile.connectedLeft = true;
                screenTile.setConnectedLeft(true);
                BlockEntity te2 = newVec.getTileEntityOnSide(this.level, side);
                if (te2 instanceof TileEntityScreen && te2.getBlockState().getValue(BlockScreen.FACING) == facing && !te2.isRemoved())
                {
                    screenTile.tryConnectLeft((TileEntityScreen) te2);
                }
            }
        }
    }

    private void joinUp()
    {
        Direction facing = this.getBlockState().getValue(BlockScreen.FACING);
//    	EnumFacing EnvType = EnumFacing.getFront(this.getRight(facing));
        Direction side = getFront().getClockWise();
        BlockVec3 vec = new BlockVec3(this);
        for (int x = -this.connectionsLeft; x <= this.connectionsRight; x++)
        {
            BlockEntity tile;
            BlockVec3 newVec = vec.clone().modifyPositionFromSide(side, x);
            if (x == 0)
            {
                tile = this;
            }
            else
            {
                tile = newVec.getTileEntity(this.level);
            }
            if (tile instanceof TileEntityScreen && tile.getBlockState().getValue(BlockScreen.FACING) == facing && !tile.isRemoved())
            {
                TileEntityScreen screenTile = (TileEntityScreen) tile;
//				screenTile.connectedUp = true;
                screenTile.setConnectedUp(true);
                BlockEntity te2 = newVec.getTileEntityOnSide(this.level, 1);
                if (te2 instanceof TileEntityScreen && te2.getBlockState().getValue(BlockScreen.FACING) == facing && !te2.isRemoved())
                {
                    screenTile.tryConnectUp((TileEntityScreen) te2);
                }
            }
        }
    }

    private void joinDown()
    {
        Direction facing = this.getBlockState().getValue(BlockScreen.FACING);
//    	EnumFacing EnvType = EnumFacing.getFront(this.getRight(facing));
        Direction side = getFront().getClockWise();
        BlockVec3 vec = new BlockVec3(this);
        for (int x = -this.connectionsLeft; x <= this.connectionsRight; x++)
        {
            BlockEntity tile;
            BlockVec3 newVec = vec.clone().modifyPositionFromSide(side, x);
            if (x == 0)
            {
                tile = this;
            }
            else
            {
                tile = newVec.getTileEntity(this.level);
            }
            if (tile instanceof TileEntityScreen && tile.getBlockState().getValue(BlockScreen.FACING) == facing && !tile.isRemoved())
            {
                TileEntityScreen screenTile = (TileEntityScreen) tile;
//				screenTile.connectedDown = true;
                screenTile.setConnectedDown(true);
                BlockEntity te2 = newVec.getTileEntityOnSide(this.level, 0);
                if (te2 instanceof TileEntityScreen && te2.getBlockState().getValue(BlockScreen.FACING) == facing && !te2.isRemoved())
                {
                    screenTile.tryConnectDown((TileEntityScreen) te2);
                }
            }
        }
    }

    private boolean tryConnectUp(TileEntityScreen screenTile)
    {
        if (screenTile.connectedDown)
        {
            return true;        //No checks?
        }

//		screenTile.connectedDown = true;
//		if (this.connectedLeft) screenTile.connectedLeft = true;
//		if (this.connectedRight) screenTile.connectedRight = true;
        screenTile.setConnectedDown(true);
        BlockVec3 vec = null;
        Direction facing = this.getBlockState().getValue(BlockScreen.FACING);
        if (this.connectedLeft)
        {
            vec = new BlockVec3(screenTile);
            screenTile.setVerifiedConnectedLeft(vec, facing);
        }
        if (this.connectedRight)
        {
            if (vec == null)
            {
                vec = new BlockVec3(screenTile);
            }
            screenTile.setVerifiedConnectedRight(vec, facing);
        }
        screenTile.refreshConnections(false);
        //Undo if the neighbour could not maintain the same left-right connections
        if ((this.connectedLeft ^ screenTile.connectedLeft) || (this.connectedRight ^ screenTile.connectedRight))
        {
            screenTile.setConnectedDown(false);
//			screenTile.connectedDown = false;
            return false;
        }

        return true;
    }

    private boolean tryConnectDown(TileEntityScreen screenTile)
    {
        if (screenTile.connectedUp)
        {
            return true;        //No checks?
        }

//		screenTile.connectedUp = true;
//		if (this.connectedLeft) screenTile.connectedLeft = true;
//		if (this.connectedRight) screenTile.connectedRight = true;
        screenTile.setConnectedUp(true);
        BlockVec3 vec = null;
        Direction facing = this.getBlockState().getValue(BlockScreen.FACING);
        if (this.connectedLeft)
        {
            vec = new BlockVec3(screenTile);
            screenTile.setVerifiedConnectedLeft(vec, facing);
        }
        if (this.connectedRight)
        {
            if (vec == null)
            {
                vec = new BlockVec3(screenTile);
            }
            screenTile.setVerifiedConnectedRight(vec, facing);
        }
        screenTile.refreshConnections(false);
        //Undo if the neighbour could not maintain the same left-right connections
        if ((this.connectedLeft ^ screenTile.connectedLeft) || (this.connectedRight ^ screenTile.connectedRight))
        {
            screenTile.setConnectedUp(false);
//			screenTile.connectedUp = false;
            return false;
        }

        return true;
    }

    private boolean tryConnectLeft(TileEntityScreen screenTile)
    {
        if (screenTile.connectedRight)
        {
            return true;        //No checks?
        }

        if ((screenTile.connectedUp && !this.connectedUp) || (screenTile.connectedDown && !this.connectedDown))
        {
            return false;
        }

//		screenTile.connectedRight = true;
//		if (this.connectedUp) screenTile.connectedUp = true;
//		if (this.connectedDown) screenTile.connectedDown = true;
        screenTile.setConnectedRight(true);
        BlockVec3 vec = null;
        Direction facing = this.getBlockState().getValue(BlockScreen.FACING);
        if (this.connectedUp)
        {
            vec = new BlockVec3(screenTile);
            screenTile.setVerifiedConnectedUp(vec, facing);
        }
        if (this.connectedDown)
        {
            if (vec == null)
            {
                vec = new BlockVec3(screenTile);
            }
            screenTile.setVerifiedConnectedDown(vec, facing);
        }
        screenTile.refreshConnections(false);
        //Undo if the neighbour could not maintain the same up-down connections
        if ((this.connectedUp ^ screenTile.connectedUp) || (this.connectedDown ^ screenTile.connectedDown))
        {
            screenTile.setConnectedRight(false);
//			screenTile.connectedRight = false;
            return false;
        }

        return true;
    }

    private boolean tryConnectRight(TileEntityScreen screenTile)
    {
        if (screenTile.connectedLeft)
        {
            return true;        //No checks?
        }

        if ((screenTile.connectedUp && !this.connectedUp) || (screenTile.connectedDown && !this.connectedDown))
        {
            return false;
        }

//		screenTile.connectedLeft = true;
//		if (this.connectedUp) screenTile.connectedUp = true;
//		if (this.connectedDown) screenTile.connectedDown = true;
        screenTile.setConnectedLeft(true);
        BlockVec3 vec = null;
        Direction facing = this.getBlockState().getValue(BlockScreen.FACING);
        if (this.connectedUp)
        {
            vec = new BlockVec3(screenTile);
            screenTile.setVerifiedConnectedUp(vec, facing);
        }
        if (this.connectedDown)
        {
            if (vec == null)
            {
                vec = new BlockVec3(screenTile);
            }
            screenTile.setVerifiedConnectedDown(vec, facing);
        }
        screenTile.refreshConnections(false);
        //Undo if the neighbour could not maintain the same up-down connections
        if ((this.connectedUp ^ screenTile.connectedUp) || (this.connectedDown ^ screenTile.connectedDown))
        {
            screenTile.setConnectedLeft(false);
//			screenTile.connectedLeft = false;
            return false;
        }

        return true;
    }

    private void log(String msg)
    {
        String connections = "";
        String strSide = "S";
        if (this.connectedUp)
        {
            connections = "U";
        }
        if (this.connectedDown)
        {
            connections += "D";
        }
        if (this.connectedLeft)
        {
            connections += "L";
        }
        if (this.connectedRight)
        {
            connections += "R";
        }
        if (this.level.isClientSide)
        {
            strSide = "C";
        }
        //System.out.println(strSide + ":" + msg + " at "+this.getPos().getX()+","+this.getPos().getZ()+" "+connections);
    }

    @Environment(EnvType.CLIENT)
    public void refreshNextTick(boolean b)
    {
        this.refreshOnUpdate = true;
        TickHandlerClient.screenConnectionsUpdateList.add(this);
    }

    @Override
    public double getPacketRange()
    {
        return 0;
    }

    @Override
    public int getPacketCooldown()
    {
        return 0;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return false;
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void updateClient(List<Object> data)
    {
        int screenType = (Integer) data.get(1);
        int flags = (Integer) data.get(2);
        this.imageType = screenType;
        this.connectedUp = (flags & 8) != 0;
        this.connectedDown = (flags & 4) != 0;
        this.connectedLeft = (flags & 2) != 0;
        this.connectedRight = (flags & 1) != 0;
        this.refreshNextTick(true);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public AABB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AABB(worldPosition, worldPosition.offset(1, 1, 1));
        }
        return this.renderAABB;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public double getViewDistance()
    {
        return Constants.RENDERDISTANCE_MEDIUM;
    }
}
