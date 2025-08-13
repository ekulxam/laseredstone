package survivalblock.laseredstone.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class LaserInteractorBlockEntity extends BlockEntity {

    public LaserInteractorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * Called when an existing laser encounters an {@link LaserInteractorBlockEntity}
     * @param inputDirection the direction the laser is coming from
     * @param world the world
     * @param blockPos the {@link BlockPos} of this {@link LaserInteractorBlockEntity}
     * @param blockState the {@link BlockState} of this {@link LaserInteractorBlockEntity}
     * @param sender the {@link LaserBlockEntity} that the laser belongs to
     * @return whether the {@link LaserBlockEntity#distance} should be incremented (mirrors should return true so the laser ends inside them)
     */
    public abstract boolean receiveLaser(Direction inputDirection, World world, BlockPos blockPos, BlockState blockState, LaserBlockEntity sender);
}
