package survivalblock.laseredstone.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public abstract class MirrorBlock extends BlockWithEntity {

    protected static final VoxelShape STRAIGHT_SHAPE = VoxelShapes.union(
            Block.createColumnShape(16.0, 0.0, 4.0),
            Block.createCuboidShape(0.0, 4.0, 0.0, 16.0, 8.0, 12.0),
            Block.createCuboidShape(0.0, 8.0, 0.0, 16.0, 12.0, 8.0),
            Block.createCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 4.0)
    );

    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    protected final MapCodec<MirrorBlock> codec = MapCodec.unit(this);

    protected MirrorBlock(Settings settings) {
        super(settings);
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

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

}
