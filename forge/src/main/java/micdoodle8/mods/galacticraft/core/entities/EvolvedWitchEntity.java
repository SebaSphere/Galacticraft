package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.level.Level;

public class EvolvedWitchEntity extends Witch implements IEntityBreathable
{
    public EvolvedWitchEntity(EntityType<? extends EvolvedWitchEntity> type, Level worldIn)
    {
        super(type, worldIn);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

//    protected void addRandomDrop()
//    {
//        switch (this.rand.nextInt(10))
//        {
//        case 0:
//        case 1:
//        case 9:
//            //Dehydrated carrot
//            this.entityDropItem(new ItemStack(GCItems.foodItem, 1, 1), 0.0F);
//            break;
//        case 2:
//        case 3:
//            this.entityDropItem(new ItemStack(Blocks.GLOWSTONE, 1), 0.0F);
//            break;
//        case 4:
//        case 5:
//            this.entityDropItem(new ItemStack(GCItems.basicItem, 1, 20), 0.0F);
//            break;
//        case 6:
//            //Oxygen tank half empty or less
//            this.entityDropItem(new ItemStack(GCItems.oxTankHeavy, 1, 901 + this.rand.nextInt(900)), 0.0F);
//            break;
//        case 7:
//            this.dropItem(GCItems.oxMask, 1);
//            break;
//        case 8:
//            this.dropItem(GCItems.oxygenVent, 1);
//            break;
//        }
//    }
//
//    @Override
//    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
//    {
//        super.dropLoot(wasRecentlyHit, lootingModifier, source);
//        if (wasRecentlyHit && this.rand.nextFloat() < 0.025F + (float)lootingModifier * 0.015F)
//        {
//            this.addRandomDrop();
//        }
//    } TODO Loot
}