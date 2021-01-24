package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEnergyStorageModule;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import javax.annotation.Nullable;

public class BlockEnergyStorageModule extends BlockMachineBase
{
    //    public static final EnumProperty<EnumTieredMachineType> TYPE = EnumProperty.create("type", EnumTieredMachineType.class);
    public static IMachineSidesProperties MACHINESIDES_RENDERTYPE = IMachineSidesProperties.TWOFACES_HORIZ;
    public static final EnumProperty SIDES = MACHINESIDES_RENDERTYPE.asProperty;
    public static final IntegerProperty FILL_VALUE = IntegerProperty.create("fill_value", 0, 33);

    public BlockEnergyStorageModule(Properties builder)
    {
        super(builder);
    }

//    @Override
//    protected void initialiseTypes()
//    {
//        this.types = EnumTieredMachineType.values;
//        this.typeBase = EnumTieredMachineType.values[0];
//    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
//        TileEntity tile = super.createTileEntity(state, world);
//        tile.setWorld(world); TODO Needed?
        return new TileEntityEnergyStorageModule.TileEntityEnergyStorageModuleT1();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.byHorizontalIndex(meta % 4);
//        EnumTieredMachineType type = (EnumTieredMachineType) typeBase.fromMetadata(meta);
//        return this.getDefaultState().with(FACING, enumfacing).with(TYPE, type);
//    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, FILL_VALUE, SIDES);
    }

//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        TileEntity tile = worldIn.getTileEntity(pos);
//        state = IMachineSides.addPropertyForTile(state, tile, MACHINESIDES_RENDERTYPE, SIDES);
//
//        if (!(tile instanceof TileEntityEnergyStorageModule))
//        {
//            return state.with(FILL_VALUE, 0);
//        }
//        int energyLevel = ((TileEntityEnergyStorageModule) tile).scaledEnergyLevel;
//        if (state.get(TYPE) == EnumTieredMachineType.STORAGE_CLUSTER) energyLevel += 17;
//        return state.with(FILL_VALUE, energyLevel);
//    }
}
