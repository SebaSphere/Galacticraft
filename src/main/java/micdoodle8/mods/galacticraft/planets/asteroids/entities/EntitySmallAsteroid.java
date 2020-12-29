package micdoodle8.mods.galacticraft.planets.asteroids.entities;

import micdoodle8.mods.galacticraft.planets.mars.entities.TNTProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntitySmallAsteroid extends Entity
{
    private static final DataParameter<Float> SPIN_PITCH = EntityDataManager.createKey(EntitySmallAsteroid.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> SPIN_YAW = EntityDataManager.createKey(EntitySmallAsteroid.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> ASTEROID_TYPE = EntityDataManager.createKey(EntitySmallAsteroid.class, DataSerializers.VARINT);
    public float spinPitch;
    public float spinYaw;
    public int type;
    private boolean firstUpdate = true;

    public EntitySmallAsteroid(EntityType<? extends EntitySmallAsteroid> type, World worldIn)
    {
        super(type, worldIn);
//        this.setSize(1.0F, 1.0F);
//        this.isImmuneToFire = true;
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void baseTick()
    {
        if (!this.firstUpdate)
        {
            // Kill non-moving entities
            if (Math.abs(this.getPosX() - this.prevPosX) + Math.abs(this.getPosZ() - this.prevPosZ) <= 0)
            {
                this.remove();
            }

            // Remove entities far outside the build range, or too old (to stop accumulations)
            else if (this.getPosY() > 288D || this.getPosY() < -32D || this.ticksExisted > 3000)
            {
                this.remove();
            }
        }

        super.baseTick();

        if (!this.world.isRemote)
        {
            this.setSpinPitch(this.spinPitch);
            this.setSpinYaw(this.spinYaw);
            this.setAsteroidType(this.type);
            this.rotationPitch += this.spinPitch;
            this.rotationYaw += this.spinYaw;
        }
        else
        {
            this.rotationPitch += this.getSpinPitch();
            this.rotationYaw += this.getSpinYaw();
        }

        double sqrdMotion = this.getMotion().x * this.getMotion().x + this.getMotion().y * this.getMotion().y + this.getMotion().z * this.getMotion().z;

        if (sqrdMotion < 0.05)
        {
            // If the motion is too low (for some odd reason), speed it back up slowly.
//            this.motionX *= 1.001D;
//            this.motionY *= 1.001D;
//            this.motionZ *= 1.001D;
            this.setMotion(this.getMotion().x * 1.001, this.getMotion().y * 1.001, this.getMotion().z * 1.001);
        }

        this.move(MoverType.SELF, this.getMotion());
        this.firstUpdate = false;
    }

    @Override
    protected void registerData()
    {
        this.dataManager.register(SPIN_PITCH, 0.0F);
        this.dataManager.register(SPIN_YAW, 0.0F);
        this.dataManager.register(ASTEROID_TYPE, 0);
    }

    @Override
    protected void readAdditional(CompoundNBT nbt)
    {
    }

    public CompoundNBT writeToNBT(CompoundNBT compound)
    {
        return compound;
    }

    @Override
    protected void writeAdditional(CompoundNBT nbt)
    {
    }

    public float getSpinPitch()
    {
        return this.dataManager.get(SPIN_PITCH);
    }

    public float getSpinYaw()
    {
        return this.dataManager.get(SPIN_YAW);
    }

    public void setSpinPitch(float pitch)
    {
        this.dataManager.set(SPIN_PITCH, pitch);
    }

    public void setSpinYaw(float yaw)
    {
        this.dataManager.set(SPIN_YAW, yaw);
    }

    public int getAsteroidType()
    {
        return this.dataManager.get(ASTEROID_TYPE);
    }

    public void setAsteroidType(int type)
    {
        this.dataManager.set(ASTEROID_TYPE, type);
    }
}
