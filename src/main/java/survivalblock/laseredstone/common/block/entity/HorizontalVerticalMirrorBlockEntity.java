package survivalblock.laseredstone.common.block.entity;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import survivalblock.laseredstone.common.block.HorizontalVerticalMirrorBlock;
import survivalblock.laseredstone.common.block.MirrorBlock;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;

public class HorizontalVerticalMirrorBlockEntity extends ActualMirrorBlockEntity {

    public HorizontalVerticalMirrorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public HorizontalVerticalMirrorBlockEntity(BlockPos pos, BlockState state) {
        this(LaseredstoneBlockEntityTypes.HORIZONTAL_VERTICAL_MIRROR, pos, state);
    }

    @Override
    public boolean isAcceptableDirection(Direction inputDirection, Level world, BlockPos blockPos, BlockState blockState) {
        Direction vertical = HorizontalVerticalMirrorBlock.getVerticalDirection(blockState).getOpposite();
        if (inputDirection == vertical) {
            return true;
        }
        Direction direction = blockState.getValue(MirrorBlock.FACING);
        return direction == inputDirection;
    }

    @Override
    public Direction getOutputDirection(Level world, BlockPos blockPos, BlockState blockState) {
        if (inputDirection == null) {
            throw new IllegalStateException("inputDirection is null!");
        }
        if (inputDirection.getAxis() == Direction.Axis.Y) {
            return blockState.getValue(MirrorBlock.FACING).getOpposite();
        }
        return HorizontalVerticalMirrorBlock.getVerticalDirection(blockState);
    }
}
