package survivalblock.laseredstone.common.block.entity;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import survivalblock.laseredstone.common.block.MirrorBlock;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;

public class HorizontalHorizontalMirrorBlockEntity extends ActualMirrorBlockEntity {

    public HorizontalHorizontalMirrorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public HorizontalHorizontalMirrorBlockEntity(BlockPos pos, BlockState state) {
        this(LaseredstoneBlockEntityTypes.HORIZONTAL_HORIZONTAL_MIRROR, pos, state);
    }

    @Override
    public boolean isAcceptableDirection(Direction inputDirection, Level world, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.getValue(MirrorBlock.FACING);
        return inputDirection == direction || inputDirection == direction.getClockWise();
    }

    @Override
    public Direction getOutputDirection(Level world, BlockPos blockPos, BlockState blockState) {
        if (inputDirection == null) {
            throw new IllegalStateException("inputDirection is null!");
        }
        Direction direction = blockState.getValue(MirrorBlock.FACING);
        return inputDirection == direction ? direction.getCounterClockWise() : direction.getOpposite();
    }
}
