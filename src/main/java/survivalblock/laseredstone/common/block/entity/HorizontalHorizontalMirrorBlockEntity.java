package survivalblock.laseredstone.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
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
    public boolean isAcceptableDirection(Direction inputDirection, World world, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.get(MirrorBlock.FACING);
        return inputDirection == direction || inputDirection == direction.rotateYClockwise();
    }

    @Override
    public Direction getOutputDirection(World world, BlockPos blockPos, BlockState blockState) {
        if (inputDirection == null) {
            throw new IllegalStateException("inputDirection is null!");
        }
        Direction direction = blockState.get(MirrorBlock.FACING);
        return inputDirection == direction ? direction.rotateYCounterclockwise() : direction.getOpposite();
    }
}
