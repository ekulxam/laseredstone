package survivalblock.laseredstone.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
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
    public boolean isAcceptableDirection(Direction inputDirection, World world, BlockPos blockPos, BlockState blockState) {
        Direction vertical = HorizontalVerticalMirrorBlock.getVerticalDirection(blockState).getOpposite();
        if (inputDirection == vertical) {
            return true;
        }
        Direction direction = blockState.get(MirrorBlock.FACING);
        return direction == inputDirection;
    }

    @Override
    public Direction getOutputDirection(World world, BlockPos blockPos, BlockState blockState) {
        if (inputDirection == null) {
            throw new IllegalStateException("inputDirection is null!");
        }
        if (inputDirection.getAxis() == Direction.Axis.Y) {
            return blockState.get(MirrorBlock.FACING).getOpposite();
        }
        return HorizontalVerticalMirrorBlock.getVerticalDirection(blockState);
    }
}
