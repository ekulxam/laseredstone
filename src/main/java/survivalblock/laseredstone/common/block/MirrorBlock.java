package survivalblock.laseredstone.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.DirectionTransformation;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;
import survivalblock.laseredstone.common.block.entity.MirrorBlockEntity;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;

import java.util.Map;

public class MirrorBlock extends BlockWithEntity {

    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;
    public static final EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final VoxelShape STRAIGHT_SHAPE = VoxelShapes.union(
            Block.createColumnShape(16.0, 0.0, 4.0),
            Block.createCuboidShape(0.0, 4.0, 0.0, 16.0, 8.0, 12.0),
            Block.createCuboidShape(0.0, 8.0, 0.0, 16.0, 12.0, 8.0),
            Block.createCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 4.0)
    );

    private static final Map<Direction, VoxelShape> STRAIGHT_BOTTOM_SHAPES = VoxelShapes.createHorizontalFacingShapeMap(STRAIGHT_SHAPE);
    private static final Map<Direction, VoxelShape> STRAIGHT_TOP_SHAPES = VoxelShapes.createHorizontalFacingShapeMap(
            VoxelShapes.transform(STRAIGHT_SHAPE, DirectionTransformation.INVERT_Y)
    );

    protected final MapCodec<MirrorBlock> codec = MapCodec.unit(this);

    public MirrorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(HALF, BlockHalf.BOTTOM).with(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends MirrorBlock> getCodec() {
        return codec;
    }

    @Override
    protected boolean hasSidedTransparency(BlockState state) {
        return true;
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

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        Direction direction = state.get(FACING);
        Direction.Axis axis = direction.getAxis();
        if (mirror == BlockMirror.LEFT_RIGHT && axis == Direction.Axis.Z ||
            mirror == BlockMirror.FRONT_BACK && axis == Direction.Axis.X) {
            return state.rotate(BlockRotation.CLOCKWISE_180);
        }
        return state;
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
        builder.add(FACING, HALF, WATERLOGGED);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MirrorBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, LaseredstoneBlockEntityTypes.MIRROR, MirrorBlockEntity::tick);
    }

}
