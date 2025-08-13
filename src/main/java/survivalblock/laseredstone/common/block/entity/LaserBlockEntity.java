package survivalblock.laseredstone.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BeamEmitter;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import survivalblock.laseredstone.common.block.LaserBlock;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;
import survivalblock.laseredstone.mixin.BeamEmitterMixin;

import java.util.List;

public class LaserBlockEntity extends LaserInteractorBlockEntity implements BeamEmitter {

    public static final int DEFAULT_COLOR = 0xFFFFFFFF;
    public static final DyedColorComponent DEFAULT_DYE_COMPONENT = new DyedColorComponent(DEFAULT_COLOR);

    public static final int MAX_DISTANCE = 16;
    protected int distance = -1;
    protected int color = DEFAULT_COLOR;

    // for rendering
    protected @Nullable Direction currentOutputDirection = null;

    public LaserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public LaserBlockEntity(BlockPos pos, BlockState state) {
        this(LaseredstoneBlockEntityTypes.LASER, pos, state);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putInt("color", this.color);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        this.color = view.getInt("color", DEFAULT_COLOR);
    }

    public boolean canLaser(World world, BlockPos blockPos, BlockState blockState) {
        boolean powered = world.isReceivingRedstonePower(blockPos);
        if (powered != blockState.get(LaserBlock.POWERED)) {
            blockState = blockState.cycle(LaserBlock.POWERED);
            world.setBlockState(blockPos, blockState, Block.NOTIFY_ALL);
        }
        return powered;
    }

    @Override
    public boolean receiveLaser(Direction inputDirection, World world, BlockPos blockPos, BlockState blockState, LaserBlockEntity sender) {
        if (!world.isClient()) {
            Vec3d explosion = blockPos.toCenterPos();
            world.breakBlock(blockPos, false);
            world.createExplosion(null, explosion.x, explosion.y, explosion.z, 6F, World.ExplosionSourceType.NONE);
        }
        return false;
    }

    @SuppressWarnings("unused")
    public Direction getOutputDirection(World world, BlockPos blockPos, BlockState blockState) {
        return blockState.get(LaserBlock.FACING);
    }

    public static <T extends LaserBlockEntity> void tick(World world, BlockPos blockPos, BlockState blockState, T blockEntity) {
        if (blockEntity instanceof MirrorBlockEntity mirrorBlockEntity) {
            mirrorBlockEntity.decrementDeflectionTicks();
        }
        if (!blockEntity.canLaser(world, blockPos, blockState)) {
            blockEntity.currentOutputDirection = null;
            blockEntity.distance = -1;
            return;
        }
        Direction direction = blockEntity.getOutputDirection(world, blockPos, blockState);
        blockEntity.currentOutputDirection = direction;
        Vec3i vec3i = direction.getVector();
        BlockPos mirrorPos;
        BlockState mirrorState;
        for (int i = 1; i <= MAX_DISTANCE; i++) {
            blockEntity.distance = i - 1;
            mirrorPos = blockPos.add(vec3i.multiply(i));
            mirrorState = world.getBlockState(mirrorPos);
            if (world.getBlockEntity(mirrorPos) instanceof LaserInteractorBlockEntity interactor) {
                if (interactor.receiveLaser(direction, world, mirrorPos, mirrorState, blockEntity)) {
                    blockEntity.distance++;
                }
                break;
            }
            if (mirrorState.getOpacity() >= 15 && !mirrorState.isOf(Blocks.BEDROCK)) {
                break;
            }
        }
        if (!world.isClient) {
            return;
        }
        if (world.getTime() % 20 == 0) {
            for (int i = 1; i <= blockEntity.distance; i++) {
                mirrorPos = blockPos.add(vec3i.multiply(i));
                Vec3d vec3d = mirrorPos.toCenterPos();
                world.addParticleClient(new DustParticleEffect(blockEntity.color, 1.0F), vec3d.getX(), vec3d.getY(), vec3d.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public @Nullable Object getRenderData() {
        return this.color;
    }

    public @Nullable Object superGetRenderData() {
        return super.getRenderData();
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createNbt(registries);
    }

    public boolean shouldSaveColor() {
        return true;
    }

    @Override
    protected void addComponents(ComponentMap.Builder builder) {
        if (this.shouldSaveColor()) {
            builder.add(DataComponentTypes.DYED_COLOR, new DyedColorComponent(ColorHelper.zeroAlpha(this.color)));
        }
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        if (this.shouldSaveColor()) {
            this.color = ColorHelper.fullAlpha(components.getOrDefault(DataComponentTypes.DYED_COLOR, DEFAULT_DYE_COMPONENT).rgb());
        }
    }

    @Override
    public List<BeamSegment> getBeamSegments() {
        if (this.distance <= -1 || this.currentOutputDirection == null) {
            return List.of();
        }
        BeamSegment beam = new BeamSegment(this.color);
        ((BeamEmitterMixin.BeamSegmentMixin) beam).laseredstone$setHeight(this.distance + 1);
        return List.of(beam);
    }

    public @Nullable Direction getCurrentOutputDirection() {
        return this.currentOutputDirection;
    }
}
