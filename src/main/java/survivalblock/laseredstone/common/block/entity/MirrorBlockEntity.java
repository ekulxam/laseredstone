package survivalblock.laseredstone.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
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

    @Override
    public void tick(World world, BlockPos blockPos, BlockState blockState) {
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

    public abstract boolean isAcceptableDirection(Direction inputDirection, World world, BlockPos blockPos, BlockState blockState);

    @Override
    public abstract Direction getOutputDirection(World world, BlockPos blockPos, BlockState blockState);

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        NbtCompound nbt = super.toInitialChunkDataNbt(registries);
        nbt.putInt("deflectionTicks", this.deflectionTicks);
        if (this.inputDirection != null) {
            nbt.put("inputDirection", Direction.CODEC, this.inputDirection);
        }
        return nbt;
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);

        if (view.contains("deflectionTicks")) {
            this.deflectionTicks = view.getInt("deflectionTicks", 0);
        }
        if (view.contains("inputDirection")) {
            this.inputDirection = view.read("inputDirection", Direction.CODEC).orElse(null);
        }
    }

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
        this.setOvercharged(sender.isOvercharged());
        return true;
    }

    @Override
    public boolean shouldSaveColor() {
        return false;
    }
}
