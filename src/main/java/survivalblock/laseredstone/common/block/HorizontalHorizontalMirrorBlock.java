package survivalblock.laseredstone.common.block;


import com.mojang.math.OctahedralGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import survivalblock.laseredstone.common.block.entity.HorizontalHorizontalMirrorBlockEntity;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;

import java.util.Map;

public class HorizontalHorizontalMirrorBlock extends MirrorBlock {

    private static final VoxelShape SIDE_SHAPE = Shapes.rotate(STRAIGHT_SHAPE, OctahedralGroup.ROT_90_Z_POS);

    private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(SIDE_SHAPE);

    public HorizontalHorizontalMirrorBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return SHAPES.get(direction);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockPos = ctx.getClickedPos();
        FluidState fluidState = ctx.getLevel().getFluidState(blockPos);
        return this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection())
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HorizontalHorizontalMirrorBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, LaseredstoneBlockEntityTypes.HORIZONTAL_HORIZONTAL_MIRROR, HorizontalHorizontalMirrorBlockEntity::tick);
    }
}
