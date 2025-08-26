package survivalblock.laseredstone.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.block.OrientationHelper;
import net.minecraft.world.block.WireOrientation;
import survivalblock.laseredstone.common.block.ReceiverBlock;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;

import java.util.EnumMap;

import static survivalblock.laseredstone.common.block.entity.MirrorBlockEntity.MAX_DEFLECTION_TICKS;

public class ReceiverBlockEntity extends LaserInteractorBlockEntity {

    public final EnumMap<Direction, Integer> directionToReceiveTicks = new EnumMap<>(Direction.class);
    public boolean mapChanged = false;

    public ReceiverBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public ReceiverBlockEntity(BlockPos pos, BlockState state) {
        this(LaseredstoneBlockEntityTypes.RECEIVER, pos, state);
    }

    @Override
    public boolean receiveLaser(Direction inputDirection, World world, BlockPos blockPos, BlockState blockState, LaserBlockEntity sender) {
        Integer receiveTicks = this.directionToReceiveTicks.get(inputDirection);
        if (receiveTicks == null || receiveTicks != MAX_DEFLECTION_TICKS) {
            this.directionToReceiveTicks.put(inputDirection, MAX_DEFLECTION_TICKS);
            this.mapChanged = true;
        }
        return false;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, ReceiverBlockEntity blockEntity) {
        EnumMap<Direction, Integer> temporary = new EnumMap<>(Direction.class);
        blockEntity.directionToReceiveTicks.forEach((direction, integer) -> {
            if (integer > 0) {
                integer--;
                blockEntity.mapChanged = true;
            } else if (integer != 0) {
                integer = 0;
                blockEntity.mapChanged = true;
            }
            temporary.put(direction, integer);
        });
        blockEntity.directionToReceiveTicks.putAll(temporary);
        if (world.getTime() % 200 == 0) {
            temporary.forEach((direction, integer) -> {
                if (integer == 0) {
                    blockEntity.directionToReceiveTicks.remove(direction);
                }
            });
        }
        if (!blockEntity.mapChanged) {
            return;
        }
        blockEntity.mapChanged = false;
        if (world.isClient()) {
            return;
        }
        boolean shouldBePowered = false;
        for (Direction direction : Direction.values()) {
            Integer receiveTicks = blockEntity.directionToReceiveTicks.get(direction);
            if (receiveTicks == null) {
                continue;
            }
            if (receiveTicks > 0) {
                shouldBePowered = true;
                break;
            }
        }
        if (blockState.get(ReceiverBlock.POWERED) != shouldBePowered) {
            blockState = blockState.cycle(ReceiverBlock.POWERED);
            world.setBlockState(blockPos, blockState, Block.NOTIFY_ALL);
        }
        update(world, blockPos, blockState);
    }

    protected static void update(World world, BlockPos blockPos, BlockState blockState) {
        Block block = blockState.getBlock();
        for (Direction direction : Direction.values()) {
            world.updateNeighbors(blockPos.offset(direction), block);
        }
    }
}
