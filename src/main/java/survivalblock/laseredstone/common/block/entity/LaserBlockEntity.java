package survivalblock.laseredstone.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeamEmitter;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import survivalblock.laseredstone.common.block.LaserBlock;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;
import survivalblock.laseredstone.common.init.LaseredstoneTags;
import survivalblock.laseredstone.common.world.DelayedDamager;
import survivalblock.laseredstone.mixin.BeamEmitterMixin;

import java.util.List;

public class LaserBlockEntity extends LaserInteractorBlockEntity implements BeamEmitter {

    public static final int DEFAULT_COLOR = 0xFFFFFFFF;
    public static final DyedColorComponent DEFAULT_DYE_COMPONENT = new DyedColorComponent(DEFAULT_COLOR);

    public static final int MAX_DISTANCE = 16;
    public static final int DEFAULT_DISTANCE = -1;

    protected int distance = DEFAULT_DISTANCE;
    protected int color = DEFAULT_COLOR;

    protected boolean updateRequired;

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

    public void updateLaser() {
        this.updateRequired = true;
    }

    public boolean canLaser(World world, BlockPos blockPos, BlockState blockState) {
        final boolean statePowered = blockState.get(LaserBlock.POWERED);

        if (!this.updateRequired) {
            return statePowered;
        }

        this.updateRequired = false;

        final boolean powered = world.isReceivingRedstonePower(blockPos);

        if (powered != statePowered) {
            world.setBlockState(blockPos, blockState.with(LaserBlock.POWERED, powered), Block.NOTIFY_LISTENERS);
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
            if (blockEntity.distance > DEFAULT_DISTANCE) {
                blockEntity.spawnDustParticles(world, blockPos);
            }
            blockEntity.distance = DEFAULT_DISTANCE;
            blockEntity.currentOutputDirection = null;
            return;
        }
        boolean wasOff = blockEntity.distance == DEFAULT_DISTANCE;
        Direction direction = blockEntity.getOutputDirection(world, blockPos, blockState);
        blockEntity.currentOutputDirection = direction;
        Vec3i vec3i = direction.getVector();
        BlockPos mirrorPos;
        BlockState mirrorState;
        for (int i = 1; i <= MAX_DISTANCE; i++) {
            blockEntity.distance = i - 1;
            mirrorPos = blockPos.add(vec3i.multiply(i));
            mirrorState = world.getBlockState(mirrorPos);
            if (mirrorState.isIn(LaseredstoneTags.ALWAYS_DENIES_LASERS)) {
                break;
            }
            if (world.getBlockEntity(mirrorPos) instanceof LaserInteractorBlockEntity interactor) {
                if (interactor.receiveLaser(direction, world, mirrorPos, mirrorState, blockEntity)) {
                    blockEntity.distance++;
                }
                break;
            }
            if (mirrorState.getOpacity() >= 15 && !mirrorState.isIn(LaseredstoneTags.ALWAYS_ALLOWS_LASERS)) {
                break;
            }
        }
        if (wasOff) {
            blockEntity.spawnDustParticles(world, blockPos);
        }
        if (blockEntity.isOvercharged() && !world.isClient()) {
            Vec3d center = blockPos.toCenterPos();
            Box box = expandInOneDirection(new Box(center.subtract(0.125), center.add(0.125)), Vec3d.of(vec3i).multiply(blockEntity.distance + 0.375));

            DelayedDamager.submitDamagingBox(box);
        }
    }

    public static float getDamage(Entity entity) {
        return entity instanceof LivingEntity living ? getLivingDamage(living) : 2;
    }

    public static float getLivingDamage(LivingEntity living) {
        return Math.max(2, (living.getMaxHealth() + living.getAbsorptionAmount()) * 0.25F);
    }

    public static Box expandInOneDirection(Box box, Vec3d vec3d) {
        // there must be a better way to do this, right?
        double x = vec3d.getX();
        double y = vec3d.getY();
        double z = vec3d.getZ();
        if (x > 0) {
            box = box.withMaxX(box.maxX + x);
        } else if (x < 0) {
            box = box.withMinX(box.minX + x);
        }
        if (y > 0) {
            box = box.withMaxY(box.maxY + y);
        } else if (y < 0) {
            box = box.withMinY(box.minY + y);
        }
        if (z > 0) {
            box = box.withMaxZ(box.maxZ + z);
        } else if (z < 0) {
            box = box.withMinZ(box.minZ + z);
        }
        return box;
    }

    public void spawnDustParticles(World world, BlockPos blockPos) {
        if (!world.isClient) {
            return;
        }
        if (this.currentOutputDirection == null) {
            return;
        }
        if (this.distance <= DEFAULT_DISTANCE) {
            return;
        }
        Vec3i vec3i = currentOutputDirection.getVector();
        BlockPos pos;
        for (int i = 1; i <= this.distance; i++) {
            pos = blockPos.add(vec3i.multiply(i));
            Vec3d vec3d = pos.toCenterPos();
            world.addParticleClient(new DustParticleEffect(this.color, 1.0F), vec3d.getX(), vec3d.getY(), vec3d.getZ(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public @Nullable Object getRenderData() {
        return this.color;
    }

    // if there was an annotation for cursed methods, this method would have it
    public @Nullable Object superGetRenderData() {
        return super.getRenderData();
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this); // TODO: sync laser (eventually)
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
        if (this.distance <= DEFAULT_DISTANCE || this.currentOutputDirection == null) {
            return List.of();
        }
        BeamSegment beam = new BeamSegment(this.color);
        ((BeamEmitterMixin.BeamSegmentMixin) beam).laseredstone$setHeight(this.distance + 1);
        return List.of(beam);
    }

    public @Nullable Direction getCurrentOutputDirection() {
        return this.currentOutputDirection;
    }

    public int getColor() {
        return this.color;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setOvercharged(boolean overcharged) {
    }

    public boolean isOvercharged() {
        return false;
    }
}
