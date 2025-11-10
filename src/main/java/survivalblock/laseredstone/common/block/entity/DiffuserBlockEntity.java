package survivalblock.laseredstone.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import survivalblock.laseredstone.common.block.DiffuserBlock;
import survivalblock.laseredstone.common.block.ReceiverBlock;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class DiffuserBlockEntity extends MirrorBlockEntity {

    public static final Direction[] VANILLA_DIRECTIONS = new Direction[]{Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
    protected final EnumMap<Direction, Integer> directionToDistanceMap = new EnumMap<>(Direction.class);

    public DiffuserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public DiffuserBlockEntity(BlockPos pos, BlockState state) {
        this(LaseredstoneBlockEntityTypes.DIFFUSER, pos, state);
    }

    @Override
    public boolean isAcceptableDirection(Direction inputDirection, World world, BlockPos blockPos, BlockState blockState) {
        return blockState.get(DiffuserBlock.FACING) == inputDirection;
    }

    @Override
    public Direction getOutputDirection(World world, BlockPos blockPos, BlockState blockState) {
        return blockState.get(DiffuserBlock.FACING);
    }

    @SuppressWarnings("RedundantMethodOverride")
    @Override
    public boolean isOvercharged() {
        return false;
    }

    @Override
    public Map<Direction, Integer> getDirectionToDistanceMap() {
        return this.directionToDistanceMap;
    }

    @Override
    public void tick(World world, BlockPos blockPos, BlockState blockState) {
        this.decrementDeflectionTicks();
        Direction realInput = blockState.get(DiffuserBlock.FACING).getOpposite();
        List<Direction> outputDirections = Arrays.stream(VANILLA_DIRECTIONS).filter(direction -> direction != realInput).toList();
        for (Direction direction : outputDirections) {
            this.currentOutputDirection = direction;

            if (this.directionToDistanceMap.containsKey(direction)) {
                Integer distance = this.directionToDistanceMap.get(direction);

                if (distance == null) {
                    distance = DEFAULT_DISTANCE;
                }

                this.distance = distance;
            } else {
                this.distance = DEFAULT_DISTANCE;
            }

            this.tickLaser(world, blockPos, blockState, () -> direction);

            if (this.currentOutputDirection != null) {
                this.directionToDistanceMap.put(direction, this.distance);
            } else {
                this.directionToDistanceMap.remove(direction);
            }
        }
        boolean shouldBePowered = this.canLaser(world, blockPos, blockState);
        if (blockState.get(DiffuserBlock.POWERED) != shouldBePowered) {
            blockState = blockState.cycle(ReceiverBlock.POWERED);
            world.setBlockState(blockPos, blockState, Block.NOTIFY_ALL);
            ReceiverBlockEntity.update(world, blockPos, blockState);
        }
    }

    @Override
    public boolean shouldSaveColor() {
        return true;
    }
}
