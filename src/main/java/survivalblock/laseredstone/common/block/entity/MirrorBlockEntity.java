package survivalblock.laseredstone.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class MirrorBlockEntity extends LaserBlockEntity {

    public static final int MAX_DEFLECTION_TICKS = 2;

    protected int deflectionTicks = 0;
    @Nullable
    protected Direction inputDirection = null;

    public MirrorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public boolean canLaser(World world, BlockPos blockPos, BlockState blockState) {
        return this.deflectionTicks > 0 && this.inputDirection != null;
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

    public abstract boolean isAcceptableDirection(Direction inputDirection, World world, BlockPos blockPos, BlockState blockState);

    @Override
    public abstract Direction getOutputDirection(World world, BlockPos blockPos, BlockState blockState);

    @Override
    public boolean receiveLaser(Direction inputDirection, World world, BlockPos blockPos, BlockState blockState, LaserBlockEntity sender) {
        if (!this.isAcceptableDirection(inputDirection, world, blockPos, blockState)) {
            return false;
        }
        if (this.deflectionTicks == MAX_DEFLECTION_TICKS && inputDirection != this.inputDirection) {
            super.receiveLaser(inputDirection, world, blockPos, blockState, sender);
            return false;
        }
        this.deflectionTicks = MAX_DEFLECTION_TICKS;
        this.inputDirection = inputDirection;
        this.color = sender.color;
        this.overcharged = sender.isOvercharged();
        return true;
    }

    @Override
    public boolean shouldSaveColor() {
        return false;
    }

    @Override
    public @Nullable Object getRenderData() {
        return this.superGetRenderData();
    }
}
