package survivalblock.laseredstone.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

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
}
