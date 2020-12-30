package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.entities.EvolvedSkeletonEntity;
import micdoodle8.mods.galacticraft.core.entities.EvolvedSpiderEntity;
import micdoodle8.mods.galacticraft.core.entities.EvolvedZombieEntity;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlockNames;
import micdoodle8.mods.galacticraft.planets.venus.entities.SpiderQueenEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

public class TileEntityDungeonSpawnerVenus extends TileEntityDungeonSpawner<SpiderQueenEntity>
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + VenusBlockNames.VENUS_BOSS_SPAWNER)
    public static TileEntityType<TileEntityDungeonSpawnerVenus> TYPE;

    public TileEntityDungeonSpawnerVenus()
    {
        super(TYPE, SpiderQueenEntity.class);
    }

    @Override
    public List<Class<? extends MobEntity>> getDisabledCreatures()
    {
        List<Class<? extends MobEntity>> list = new ArrayList<Class<? extends MobEntity>>();
        list.add(EvolvedSkeletonEntity.class);
        list.add(EvolvedZombieEntity.class);
        list.add(EvolvedSpiderEntity.class);
        return list;
    }

    @Override
    public void playSpawnSound(Entity entity)
    {
        this.world.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), GCSounds.scaryScape, SoundCategory.AMBIENT, 9.0F, 1.4F);
    }
}
