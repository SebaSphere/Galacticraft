package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRefinery;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlockNames;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.BlockTelepadFake;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.ShortRangeTelepadHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.ContainerShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.network.PacketSimpleAsteroids;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TileEntityShortRangeTelepad extends TileBaseElectricBlock implements IMultiBlock, IInventoryDefaults, ISidedInventory, INamedContainerProvider
{
    public enum EnumTelepadSearchResult
    {
        VALID,
        NOT_FOUND,
        TOO_FAR,
        WRONG_DIM,
        TARGET_DISABLED
    }

    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + AsteroidBlockNames.SHORT_RANGE_TELEPAD)
    public static TileEntityType<TileEntityShortRangeTelepad> TYPE;

    public static final int MAX_TELEPORT_TIME = 150;
    public static final int TELEPORTER_RANGE = 256;
    public static final int ENERGY_USE_ON_TELEPORT = 2500;

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int address = -1;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean addressValid = false;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int targetAddress = -1;
    public EnumTelepadSearchResult targetAddressResult = EnumTelepadSearchResult.NOT_FOUND;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int teleportTime = 0;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public String owner = "";
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean teleporting;
    private AxisAlignedBB renderAABB;

    public TileEntityShortRangeTelepad()
    {
        super(TYPE);
        this.storage.setMaxExtract(ConfigManagerCore.hardMode.get() ? 115 : 50);
        this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    public int canTeleportHere()
    {
        if (this.world.isRemote)
        {
            return -1;
        }

        this.setAddress(this.address);
        this.setTargetAddress(this.targetAddress);

        if (!this.addressValid)
        {
            return 1;
        }

        if (this.getEnergyStoredGC() < ENERGY_USE_ON_TELEPORT)
        {
            return 2;
        }

        return 0; // GOOD
    }

    @Override
    public void tick()
    {
        if (this.ticks % 40 == 0 && !this.world.isRemote)
        {
            this.setAddress(this.address);
            this.setTargetAddress(this.targetAddress);
        }

        if (!this.world.isRemote)
        {
            if (!this.getDisabled(0) && this.targetAddressResult == EnumTelepadSearchResult.VALID && (this.ticks % 5 == 0 || teleporting))
            {
                List containedEntities = this.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(),
                        this.getPos().getX() + 1, this.getPos().getY() + 2, this.getPos().getZ() + 1));

                if (containedEntities.size() > 0 && this.getEnergyStoredGC() >= ENERGY_USE_ON_TELEPORT)
                {
                    ShortRangeTelepadHandler.TelepadEntry entry = ShortRangeTelepadHandler.getLocationFromAddress(this.targetAddress);

                    if (entry != null)
                    {
                        teleporting = true;
                    }
                }
                else
                {
                    teleporting = false;
                }
            }

            if (this.teleporting)
            {
                this.teleportTime++;

                if (teleportTime >= MAX_TELEPORT_TIME)
                {
                    ShortRangeTelepadHandler.TelepadEntry entry = ShortRangeTelepadHandler.getLocationFromAddress(this.targetAddress);

                    BlockVec3 finalPos = (entry == null) ? null : entry.position;

                    if (finalPos != null)
                    {
                        TileEntity tileAt = finalPos.getTileEntity(this.world);
                        List<LivingEntity> containedEntities = this.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(),
                                this.getPos().getX() + 1, this.getPos().getY() + 2, this.getPos().getZ() + 1));

                        if (tileAt instanceof TileEntityShortRangeTelepad)
                        {
                            TileEntityShortRangeTelepad destTelepad = (TileEntityShortRangeTelepad) tileAt;
                            int teleportResult = destTelepad.canTeleportHere();
                            if (teleportResult == 0)
                            {
                                for (LivingEntity e : containedEntities)
                                {
                                    e.setPosition(finalPos.x + 0.5F, finalPos.y + 0.08F, finalPos.z + 0.5F);
//                                    this.world.updateEntityWithOptionalForce(e, true); TODO Still necessary?
                                    if (e instanceof ServerPlayerEntity)
                                    {
                                        ((ServerPlayerEntity) e).connection.setPlayerLocation(finalPos.x, finalPos.y, finalPos.z, e.rotationYaw, e.rotationPitch);
                                    }
                                    GalacticraftCore.packetPipeline.sendToDimension(new PacketSimpleAsteroids(PacketSimpleAsteroids.EnumSimplePacketAsteroids.C_TELEPAD_SEND, GCCoreUtil.getDimensionType(this.world), new Object[]{finalPos, e.getEntityId()}), GCCoreUtil.getDimensionType(this.world));
                                }

                                if (containedEntities.size() > 0)
                                {
                                    this.storage.setEnergyStored(this.storage.getEnergyStoredGC() - ENERGY_USE_ON_TELEPORT);
                                    destTelepad.storage.setEnergyStored(this.storage.getEnergyStoredGC() - ENERGY_USE_ON_TELEPORT);
                                }
                            }
                            else
                            {
                                switch (teleportResult)
                                {
                                case -1:
                                    for (LivingEntity e : containedEntities)
                                    {
                                        if (e instanceof PlayerEntity)
                                        {
                                            e.sendMessage(new StringTextComponent("Cannot Send client-LogicalSide")); // No need for translation, since this should never happen
                                        }
                                    }
                                    break;
                                case 1:
                                    for (LivingEntity e : containedEntities)
                                    {
                                        if (e instanceof PlayerEntity)
                                        {
                                            e.sendMessage(new StringTextComponent("Target address invalid")); // No need for translation, since this should never happen
                                        }
                                    }
                                    break;
                                case 2:
                                    for (LivingEntity e : containedEntities)
                                    {
                                        if (e instanceof PlayerEntity)
                                        {
                                            e.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.message.target_no_energy")));
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }

                    this.teleportTime = 0;
                    this.teleporting = false;
                }
            }
            else
            {
                this.teleportTime = Math.max(--this.teleportTime, 0);
            }
        }

        super.tick();
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);

        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.getInventory());

        if (GCCoreUtil.getEffectiveSide() == LogicalSide.SERVER)
        {
            this.setAddress(nbt.getInt("Address"));
        }
        this.targetAddress = nbt.getInt("TargetAddress");
        this.owner = nbt.getString("Owner");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        ItemStackHelper.saveAllItems(nbt, this.getInventory());

        nbt.putInt("TargetAddress", this.targetAddress);
        nbt.putInt("Address", this.address);
        nbt.putString("Owner", this.owner);
        return nbt;
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList)
    {
        super.addExtraNetworkedData(networkedList);
        networkedList.add(targetAddressResult.ordinal());
    }

    @Override
    public void readExtraNetworkedData(ByteBuf dataStream)
    {
        super.readExtraNetworkedData(dataStream);
        targetAddressResult = EnumTelepadSearchResult.values()[dataStream.readInt()];
    }

    @Override
    public double getPacketRange()
    {
        return 24.0D;
    }

    @Override
    public ActionResultType onActivated(PlayerEntity entityPlayer)
    {
        if (!world.isRemote)
        {
            NetworkHooks.openGui((ServerPlayerEntity) entityPlayer, this, buf -> buf.writeBlockPos(pos));
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onCreate(World world, BlockPos placedPosition)
    {
        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(placedPosition, positions);
        for (BlockPos vecToAdd : positions)
        {
            ((BlockTelepadFake) AsteroidBlocks.SHORT_RANGE_TELEPAD_DUMMY).makeFakeBlock(world, vecToAdd, placedPosition, AsteroidBlocks.SHORT_RANGE_TELEPAD_DUMMY.getDefaultState().with(BlockTelepadFake.TOP, vecToAdd.getY() == placedPosition.getY() + 2));
        }
    }

    @Override
    public BlockMulti.EnumBlockMultiType getMultiType()
    {
        //Not actually used - maybe this shouldn't be an IMultiBlock at all?
        return EnumBlockMultiType.MINER_BASE;
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int buildHeight = this.world.getHeight() - 1;
        for (int y = 0; y < 3; y += 2)
        {
            if (placedPosition.getY() + y > buildHeight)
            {
                return;
            }
            for (int x = -1; x <= 1; x++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    if (x == 0 && y == 0 && z == 0)
                    {
                        continue;
                    }
                    positions.add(placedPosition.add(x, y, z));
                }
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockPos thisBlock = getPos();
        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            BlockState stateAt = this.world.getBlockState(pos);

            if (stateAt.getBlock() == AsteroidBlocks.SHORT_RANGE_TELEPAD_DUMMY)
            {
                this.world.destroyBlock(pos, false);
            }
        }
        this.world.destroyBlock(thisBlock, true);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(getPos().getX() - 1, getPos().getY(), getPos().getZ() - 1, getPos().getX() + 2, getPos().getY() + 4, getPos().getZ() + 2);
        }
        return this.renderAABB;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Constants.RENDERDISTANCE_MEDIUM;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0};
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity par1EntityPlayer)
    {
        return this.world.getTileEntity(getPos()) == this && par1EntityPlayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
    {
        return slotID == 0 && ItemElectricBase.isElectricItem(itemStack.getItem());
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction)
    {
        return index == 0;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction)
    {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public EnumSet<Direction> getElectricalOutputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return !this.getDisabled(0);
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return Direction.UP;
//        return Direction.byIndex((this.getBlockMetadata() & 3) + 2);
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            switch (index)
            {
            case 0:
                this.disabled = disabled;
                this.disableCooldown = 10;
                if (world != null && !world.isRemote)
                {
                    ShortRangeTelepadHandler.addShortRangeTelepad(this);
                }
                break;
            default:
                break;
            }
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        switch (index)
        {
        case 0:
            return this.disabled;
        default:
            break;
        }

        return true;
    }

    public void setAddress(int address)
    {
        if (this.world != null && address != this.address)
        {
            ShortRangeTelepadHandler.removeShortRangeTeleporter(this);
        }

        this.address = address;

        if (this.address >= 0)
        {
            ShortRangeTelepadHandler.TelepadEntry entry = ShortRangeTelepadHandler.getLocationFromAddress(this.address);
            this.addressValid = entry == null || (this.world != null && (entry.dimensionID == GCCoreUtil.getDimensionType(this.world) && entry.position.x == this.getPos().getX() && entry.position.y == this.getPos().getY() && entry.position.z == this.getPos().getZ()));
        }
        else
        {
            this.addressValid = false;
        }

        if (this.world != null && !this.world.isRemote)
        {
            ShortRangeTelepadHandler.addShortRangeTelepad(this);
        }
    }

    public boolean updateTarget()
    {
        if (this.targetAddress >= 0 && !this.world.isRemote)
        {
            this.targetAddressResult = EnumTelepadSearchResult.NOT_FOUND;

            ShortRangeTelepadHandler.TelepadEntry addressResult = ShortRangeTelepadHandler.getLocationFromAddress(this.targetAddress);

            if (addressResult != null)
            {
                if (GCCoreUtil.getDimensionType(this.world) == addressResult.dimensionID)
                {
                    double distance = this.getDistanceSq(addressResult.position.x + 0.5F, addressResult.position.y + 0.5F, addressResult.position.z + 0.5F);

                    if (distance < Math.pow(TELEPORTER_RANGE, 2))
                    {
                        if (!addressResult.enabled)
                        {
                            this.targetAddressResult = EnumTelepadSearchResult.TARGET_DISABLED;
                            return false;
                        }

                        this.targetAddressResult = EnumTelepadSearchResult.VALID;
                        return true;
                    }
                    else
                    {
                        this.targetAddressResult = EnumTelepadSearchResult.TOO_FAR;
                        return false;
                    }
                }
                else
                {
                    this.targetAddressResult = EnumTelepadSearchResult.WRONG_DIM;
                    return false;
                }
            }
            else
            {
                this.targetAddressResult = EnumTelepadSearchResult.NOT_FOUND;
                return false;
            }
        }
        else
        {
            this.targetAddressResult = EnumTelepadSearchResult.NOT_FOUND;
            return false;
        }
    }

    public void setTargetAddress(int address)
    {
        this.targetAddress = address;
        this.updateTarget();
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public String getOwner()
    {
        return this.owner;
    }

    @OnlyIn(Dist.CLIENT)
    public String getReceivingStatus()
    {
        if (!this.addressValid)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.invalid_address");
        }

        if (this.getEnergyStoredGC() <= 0.0F)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.no_energy");
        }

        if (this.getEnergyStoredGC() <= ENERGY_USE_ON_TELEPORT)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.not_enough_energy");
        }

        if (this.getDisabled(0))
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.disabled");
        }

        return EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.message.receiving_active");
    }

    @OnlyIn(Dist.CLIENT)
    public String getSendingStatus()
    {
        if (!this.addressValid)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.invalid_target_address");
        }

        if (this.targetAddressResult == EnumTelepadSearchResult.TOO_FAR)
        {
            return EnumColor.RED + GCCoreUtil.translateWithFormat("gui.message.telepad_too_far", TELEPORTER_RANGE);
        }

        if (this.targetAddressResult == EnumTelepadSearchResult.WRONG_DIM)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.telepad_wrong_dim");
        }

        if (this.targetAddressResult == EnumTelepadSearchResult.NOT_FOUND)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.telepad_not_found");
        }

        if (this.targetAddressResult == EnumTelepadSearchResult.TARGET_DISABLED)
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.message.telepad_target_disabled");
        }

        if (this.getEnergyStoredGC() <= 0.0F)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.no_energy");
        }

        if (this.getEnergyStoredGC() <= ENERGY_USE_ON_TELEPORT)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.not_enough_energy");
        }

        if (this.getDisabled(0))
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.disabled");
        }

        return EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.message.sending_active");
    }

    @OnlyIn(Dist.CLIENT)
    public Vector3 getParticleColor(Random rand, boolean sending)
    {
        float teleportTimeScaled = Math.min(1.0F, this.teleportTime / (float) TileEntityShortRangeTelepad.MAX_TELEPORT_TIME);
        float f;
        f = rand.nextFloat() * 0.6F + 0.4F;

        if (sending && this.targetAddressResult != EnumTelepadSearchResult.VALID)
        {
            return new Vector3(f, f * 0.3F, f * 0.3F);
        }

        if (!sending && !this.addressValid)
        {
            return new Vector3(f, f * 0.3F, f * 0.3F);
        }

        if (this.getEnergyStoredGC() < ENERGY_USE_ON_TELEPORT)
        {
            return new Vector3(f, f * 0.6F, f * 0.3F);
        }

        float r = f * 0.3F;
        float g = f * (0.3F + (teleportTimeScaled * 0.7F));
        float b = f * (1.0F - (teleportTimeScaled * 0.7F));

        return new Vector3(r, g, b);
    }

    @Override
    public Direction getFront()
    {
        return Direction.NORTH;
    }

    @Override
    public Container createMenu(int containerId, PlayerInventory playerInv, PlayerEntity player)
    {
        return new ContainerShortRangeTelepad(containerId, playerInv, this);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.short_range_telepad");
    }
}
