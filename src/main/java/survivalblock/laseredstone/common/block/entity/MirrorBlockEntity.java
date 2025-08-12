package survivalblock.laseredstone.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import survivalblock.laseredstone.common.block.MirrorBlock;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;

public class MirrorBlockEntity extends LaserBlockEntity {

    protected int deflectionTicks = 0;
    @Nullable
    protected Direction inputDirection = null;

    public MirrorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public MirrorBlockEntity(BlockPos pos, BlockState state) {
        this(LaseredstoneBlockEntityTypes.MIRROR, pos, state);
    }

    @Override
    public boolean canLaser(World world, BlockPos blockPos, BlockState blockState) {
        return this.deflectionTicks > 0;
    }

    public void decrementDeflectionTicks() {
        if (this.deflectionTicks > 0) {
            this.deflectionTicks--;
            return;
        }
        if (this.inputDirection != null) {
            this.inputDirection = null;
        }
        if (this.deflectionTicks != 0) {
            this.deflectionTicks = 0;
        }
    }

    @SuppressWarnings("unused")
    public boolean isAcceptableDirection(Direction inputDirection, World world, BlockPos blockPos, BlockState blockState) {
        Direction vertical = MirrorBlock.getVerticalDirection(blockState).getOpposite();
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
        return MirrorBlock.getVerticalDirection(blockState);
    }

    @Override
    public void receiveLaser(Direction inputDirection, World world, BlockPos blockPos, BlockState blockState, LaserBlockEntity sender) {
        if (this.isAcceptableDirection(inputDirection, world, blockPos, blockState)) {
            this.deflectionTicks = 2;
            this.inputDirection = inputDirection;
            this.color = sender.color;
        }
    }
}
