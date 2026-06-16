package survivalblock.laseredstone.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
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
    public boolean canLaser(Level world, BlockPos blockPos, BlockState blockState) {
        return this.deflectionTicks > 0 && this.inputDirection != null;
    }

    @Override
    public void tick(Level world, BlockPos blockPos, net.minecraft.world.level.block.state.BlockState blockState) {
        this.decrementDeflectionTicks();
        super.tick(world, blockPos, blockState);
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

    public abstract boolean isAcceptableDirection(Direction inputDirection, Level world, BlockPos blockPos, BlockState blockState);

    @Override
    public abstract Direction getOutputDirection(Level world, BlockPos blockPos, BlockState blockState);

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag nbt = super.getUpdateTag(registries);
        nbt.putInt("deflectionTicks", this.deflectionTicks);
        if (this.inputDirection != null) {
            nbt.store("inputDirection", Direction.CODEC, this.inputDirection);
        }
        return nbt;
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);

        if (view.contains("deflectionTicks")) {
            this.deflectionTicks = view.getIntOr("deflectionTicks", 0);
        }
        if (view.contains("inputDirection")) {
            this.inputDirection = view.read("inputDirection", Direction.CODEC).orElse(null);
        }
    }

    @Override
    public boolean receiveLaser(Direction inputDirection, Level world, BlockPos blockPos, BlockState blockState, LaserBlockEntity sender) {
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
        this.setOvercharged(sender.isOvercharged());
        return true;
    }

    @Override
    public boolean shouldSaveColor() {
        return false;
    }
}
