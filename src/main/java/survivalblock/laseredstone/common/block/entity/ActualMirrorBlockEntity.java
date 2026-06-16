package survivalblock.laseredstone.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
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
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag nbt = super.getUpdateTag(registries);
        nbt.putBoolean("overcharged", this.overcharged);
        return nbt;
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);

        if (view.contains("overcharged")) {
            this.setOvercharged(view.getBooleanOr("overcharged", false));
        }
    }

    @Override
    public @Nullable Object getRenderData() {
        return this.superGetRenderData();
    }
}
