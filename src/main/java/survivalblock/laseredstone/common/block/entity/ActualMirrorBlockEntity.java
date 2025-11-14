package survivalblock.laseredstone.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public abstract class ActualMirrorBlockEntity extends MirrorBlockEntity {

    protected boolean overcharged = false;

    public ActualMirrorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void setOvercharged(boolean overcharged) {
        this.overcharged = overcharged;
    }

    @Override
    public boolean isOvercharged() {
        return this.overcharged;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        NbtCompound nbt = super.toInitialChunkDataNbt(registries);
        nbt.putBoolean("overcharged", this.overcharged);
        return nbt;
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);

        if (view.contains("overcharged")) {
            this.setOvercharged(view.getBoolean("overcharged", false));
        }
    }

    @Override
    public @Nullable Object getRenderData() {
        return this.superGetRenderData();
    }
}
