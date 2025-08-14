package survivalblock.laseredstone.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
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
    public boolean isAcceptableDirection(Direction inputDirection, World world, BlockPos blockPos, BlockState blockState) {
        return blockState.get(LensBlock.FACING).getAxis() == inputDirection.getAxis();
    }

    @Override
    public Direction getOutputDirection(World world, BlockPos blockPos, BlockState blockState) {
        return blockState.get(LensBlock.FACING);
    }

    @Override
    public boolean receiveLaser(Direction inputDirection, World world, BlockPos blockPos, BlockState blockState, LaserBlockEntity sender) {
        boolean success = super.receiveLaser(inputDirection, world, blockPos, blockState, sender);
        this.overcharged = true;
        return success;
    }

    @Override
    public boolean isOvercharged() {
        return true;
    }
}
