package survivalblock.laseredstone.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.DirectionTransformation;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import survivalblock.laseredstone.common.block.entity.HorizontalVerticalMirrorBlockEntity;
import survivalblock.laseredstone.common.block.entity.MirrorBlockEntity;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;

import java.util.Map;

public class HorizontalVerticalMirrorBlock extends MirrorBlock {

    public static final EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;

    private static final Map<Direction, VoxelShape> STRAIGHT_BOTTOM_SHAPES = VoxelShapes.createHorizontalFacingShapeMap(STRAIGHT_SHAPE);
    private static final Map<Direction, VoxelShape> STRAIGHT_TOP_SHAPES = VoxelShapes.createHorizontalFacingShapeMap(
            VoxelShapes.transform(STRAIGHT_SHAPE, DirectionTransformation.INVERT_Y)
    );

    public HorizontalVerticalMirrorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(HALF, BlockHalf.BOTTOM).with(WATERLOGGED, false));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        boolean bottom = isBottom(state);
        Direction direction = state.get(FACING);
        return (bottom ? STRAIGHT_BOTTOM_SHAPES : STRAIGHT_TOP_SHAPES).get(direction);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide();
        BlockPos blockPos = ctx.getBlockPos();
        FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing())
                .with(HALF, direction != Direction.DOWN && (direction == Direction.UP || !(ctx.getHitPos().y - blockPos.getY() > 0.5)) ? BlockHalf.BOTTOM : BlockHalf.TOP)
                .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    public static boolean isBottom(BlockState state) {
        return state.get(HALF) == BlockHalf.BOTTOM;
    }

    public static Direction getVerticalDirection(BlockState state) {
        return getVerticalDirection(isBottom(state));
    }

    public static Direction getVerticalDirection(boolean bottom) {
        return bottom ? Direction.UP : Direction.DOWN;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HALF);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HorizontalVerticalMirrorBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, LaseredstoneBlockEntityTypes.HORIZONTAL_VERTICAL_MIRROR, HorizontalVerticalMirrorBlockEntity::tick);
    }

}
