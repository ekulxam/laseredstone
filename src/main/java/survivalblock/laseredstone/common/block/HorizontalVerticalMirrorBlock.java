package survivalblock.laseredstone.common.block;

import com.mojang.math.OctahedralGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import survivalblock.laseredstone.common.block.entity.HorizontalVerticalMirrorBlockEntity;
import survivalblock.laseredstone.common.block.entity.MirrorBlockEntity;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;

import java.util.Map;

public class HorizontalVerticalMirrorBlock extends MirrorBlock {

    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    private static final Map<Direction, VoxelShape> STRAIGHT_BOTTOM_SHAPES = Shapes.rotateHorizontal(STRAIGHT_SHAPE);
    private static final Map<Direction, VoxelShape> STRAIGHT_TOP_SHAPES = Shapes.rotateHorizontal(
            Shapes.rotate(STRAIGHT_SHAPE, OctahedralGroup.INVERT_Y)
    );

    public HorizontalVerticalMirrorBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(HALF, Half.BOTTOM).setValue(WATERLOGGED, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        boolean bottom = isBottom(state);
        Direction direction = state.getValue(FACING);
        return (bottom ? STRAIGHT_BOTTOM_SHAPES : STRAIGHT_TOP_SHAPES).get(direction);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction direction = ctx.getClickedFace();
        BlockPos blockPos = ctx.getClickedPos();
        FluidState fluidState = ctx.getLevel().getFluidState(blockPos);
        return this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection())
                .setValue(HALF, direction != Direction.DOWN && (direction == Direction.UP || !(ctx.getClickLocation().y - blockPos.getY() > 0.5)) ? Half.BOTTOM : Half.TOP)
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    public static boolean isBottom(BlockState state) {
        return state.getValue(HALF) == Half.BOTTOM;
    }

    public static Direction getVerticalDirection(BlockState state) {
        return getVerticalDirection(isBottom(state));
    }

    public static Direction getVerticalDirection(boolean bottom) {
        return bottom ? Direction.UP : Direction.DOWN;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HorizontalVerticalMirrorBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, LaseredstoneBlockEntityTypes.HORIZONTAL_VERTICAL_MIRROR, HorizontalVerticalMirrorBlockEntity::tick);
    }

}
