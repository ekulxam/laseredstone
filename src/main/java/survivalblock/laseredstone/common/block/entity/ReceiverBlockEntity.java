package survivalblock.laseredstone.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import survivalblock.laseredstone.common.block.ReceiverBlock;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;

import static survivalblock.laseredstone.common.block.entity.MirrorBlockEntity.MAX_DEFLECTION_TICKS;

public class ReceiverBlockEntity extends LaserInteractorBlockEntity {

    protected int receiveTicks = 0;

    public ReceiverBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public ReceiverBlockEntity(BlockPos pos, BlockState state) {
        super(LaseredstoneBlockEntityTypes.RECEIVER, pos, state);
    }

    @Override
    public boolean receiveLaser(Direction inputDirection, World world, BlockPos blockPos, BlockState blockState, LaserBlockEntity sender) {
        this.receiveTicks = MAX_DEFLECTION_TICKS;
        return false;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, ReceiverBlockEntity blockEntity) {
        if (blockEntity.receiveTicks > 0) {
            blockEntity.receiveTicks--;
        } else {
            if (blockEntity.receiveTicks != 0) {
                blockEntity.receiveTicks = 0;
            }
        }
        boolean shouldBePowered = blockEntity.receiveTicks > 0;
        if (blockState.get(ReceiverBlock.POWERED) != shouldBePowered) {
            blockState = blockState.cycle(ReceiverBlock.POWERED);
            world.setBlockState(blockPos, blockState, Block.NOTIFY_ALL);
        }
    }
}
