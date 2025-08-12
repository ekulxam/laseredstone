package survivalblock.laseredstone.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class LaserInteracterBlockEntity extends BlockEntity {

    public LaserInteracterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract boolean canLaser(World world, BlockPos blockPos, BlockState blockState);

    public abstract void receiveLaser(Direction inputDirection, World world, BlockPos blockPos, BlockState blockState, LaserBlockEntity sender);
}
