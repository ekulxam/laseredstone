package survivalblock.laseredstone.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import survivalblock.laseredstone.common.block.LensBlock;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;

public class LensBlockEntity extends MirrorBlockEntity {

    public LensBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public LensBlockEntity(BlockPos pos, BlockState state) {
        this(LaseredstoneBlockEntityTypes.LENS, pos, state);
    }

    @Override
    public boolean isAcceptableDirection(Direction inputDirection, Level world, BlockPos blockPos, BlockState blockState) {
        return blockState.getValue(LensBlock.FACING).getAxis() == inputDirection.getAxis();
    }

    @Override
    public Direction getOutputDirection(Level world, BlockPos blockPos, BlockState blockState) {
        return blockState.getValue(LensBlock.FACING);
    }

    @Override
    public boolean isOvercharged() {
        return true;
    }

    @Override
    public @Nullable Object getRenderData() {
        return this.superGetRenderData();
    }
}
