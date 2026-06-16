package survivalblock.laseredstone.common.block.entity;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import survivalblock.laseredstone.common.block.DiffuserBlock;
import survivalblock.laseredstone.common.block.ReceiverBlock;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class DiffuserBlockEntity extends MirrorBlockEntity {

    public static final Direction[] VANILLA_DIRECTIONS = new Direction[]{Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};

    public static final Codec<Map<Direction, Integer>> OUTPUTS_CODEC = Codec.simpleMap(Direction.CODEC, Codec.INT, StringRepresentable.keys(VANILLA_DIRECTIONS)).codec();

    protected final EnumMap<Direction, Integer> directionToDistanceMap = new EnumMap<>(Direction.class);

    public DiffuserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public DiffuserBlockEntity(BlockPos pos, BlockState state) {
        this(LaseredstoneBlockEntityTypes.DIFFUSER, pos, state);
    }

    @Override
    public boolean isAcceptableDirection(Direction inputDirection, Level world, BlockPos blockPos, BlockState blockState) {
        return blockState.getValue(DiffuserBlock.FACING) == inputDirection;
    }

    @Override
    public Direction getOutputDirection(Level world, BlockPos blockPos, BlockState blockState) {
        return blockState.getValue(DiffuserBlock.FACING);
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
    public void tick(Level world, BlockPos blockPos, BlockState blockState) {
        this.decrementDeflectionTicks();
        Direction realInput = blockState.getValue(DiffuserBlock.FACING).getOpposite();
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
        if (blockState.getValue(DiffuserBlock.POWERED) != shouldBePowered) {
            blockState = blockState.cycle(ReceiverBlock.POWERED);
            world.setBlock(blockPos, blockState, Block.UPDATE_ALL);
            ReceiverBlockEntity.update(world, blockPos, blockState);
        }
    }

    @Override
    public boolean shouldSaveColor() {
        return true;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag nbt = super.getUpdateTag(registries);
        nbt.store("directionToDistanceMap", OUTPUTS_CODEC, this.directionToDistanceMap);
        return nbt;
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);

        if (view.contains("directionToDistanceMap")) {
            view.read("directionToDistanceMap", OUTPUTS_CODEC).ifPresent(map -> {
                this.directionToDistanceMap.clear();
                this.directionToDistanceMap.putAll(map);
            });
        }
    }
}
