package survivalblock.laseredstone.common.block.entity;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import survivalblock.laseredstone.common.block.LaserBlock;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;
import survivalblock.laseredstone.common.init.LaseredstoneTags;
import survivalblock.laseredstone.common.world.DelayedDamager;
import survivalblock.laseredstone.mixin.BeamEmitterMixin;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class LaserBlockEntity extends LaserInteractorBlockEntity implements BeaconBeamOwner {

    public static final int DEFAULT_COLOR = 0xFFFFFFFF;
    public static final DyedItemColor DEFAULT_DYE_COMPONENT = new DyedItemColor(DEFAULT_COLOR);

    public static final int MAX_DISTANCE = 16;
    public static final int DEFAULT_DISTANCE = -1;

    /*? =1.21.8 {*/ /*List<Section> renderSegments; *//*?}*/
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
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        view.putInt("color", this.color);
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        this.color = view.getIntOr("color", DEFAULT_COLOR);
    }

    public void updateLaser() {
        this.updateRequired = true;
    }

    public boolean canLaser(Level world, BlockPos blockPos, BlockState blockState) {
        final boolean statePowered = blockState.getValue(LaserBlock.POWERED);

        if (!this.updateRequired) {
            return statePowered;
        }

        this.updateRequired = false;

        final boolean powered = world.hasNeighborSignal(blockPos);

        if (powered != statePowered) {
            world.setBlock(blockPos, blockState.setValue(LaserBlock.POWERED, powered), Block.UPDATE_CLIENTS);
        }

        return powered;
    }

    @Override
    public boolean receiveLaser(Direction inputDirection, Level world, BlockPos blockPos, BlockState blockState, LaserBlockEntity sender) {
        if (!world.isClientSide()) {
            Vec3 explosion = blockPos.getCenter();
            world.destroyBlock(blockPos, true);
            world.explode(null, explosion.x, explosion.y, explosion.z, 6F, Level.ExplosionInteraction.NONE);
        }
        return false;
    }

    @SuppressWarnings("unused")
    public Direction getOutputDirection(Level world, BlockPos blockPos, BlockState blockState) {
        return blockState.getValue(LaserBlock.FACING);
    }

    public static <T extends LaserBlockEntity> void tick(Level world, BlockPos blockPos, BlockState blockState, T blockEntity) {
        blockEntity.tick(world, blockPos, blockState);
    }

    public void tick(Level world, BlockPos blockPos, BlockState blockState) {
        this.tickLaser(world, blockPos, blockState, () -> this.getOutputDirection(world, blockPos, blockState));
    }

    protected final void tickLaser(Level world, BlockPos blockPos, BlockState blockState, Supplier<Direction> direction) {
        if (!this.canLaser(world, blockPos, blockState)) {
            if (this.distance > DEFAULT_DISTANCE) {
                this.spawnDustParticles(world, blockPos);
            }
            this.distance = DEFAULT_DISTANCE;
            this.currentOutputDirection = null;
            return;
        }
        boolean wasOff = this.distance == DEFAULT_DISTANCE;
        this.currentOutputDirection = direction.get();
        Vec3i vec3i = this.currentOutputDirection.getUnitVec3i();
        BlockPos mirrorPos;
        BlockState mirrorState;
        for (int i = 1; i <= MAX_DISTANCE; i++) {
            this.distance = i - 1;
            mirrorPos = blockPos.offset(vec3i.multiply(i));
            mirrorState = world.getBlockState(mirrorPos);
            if (mirrorState.is(LaseredstoneTags.ALWAYS_DENIES_LASERS)) {
                break;
            }
            if (world.getBlockEntity(mirrorPos) instanceof LaserInteractorBlockEntity interactor) {
                if (interactor.receiveLaser(this.currentOutputDirection, world, mirrorPos, mirrorState, this)) {
                    this.distance++;
                }
                break;
            }
            //~ if >=26 'getLightBlock' -> 'getLightDampening'
            if (mirrorState.getLightDampening() >= 15 && !mirrorState.is(LaseredstoneTags.ALWAYS_ALLOWS_LASERS)) {
                break;
            }
        }
        if (wasOff) {
            this.spawnDustParticles(world, blockPos);
        }
        if (this.isOvercharged() && !world.isClientSide()) {
            Vec3 center = blockPos.getCenter();
            AABB box = expandInOneDirection(new AABB(center.subtract(0.125), center.add(0.125)), Vec3.atLowerCornerOf(vec3i).scale(this.distance + 0.375));

            DelayedDamager.submitDamagingBox(box);
        }
    }

    public static float getDamage(Entity entity) {
        return entity instanceof LivingEntity living ? getLivingDamage(living) : 2;
    }

    public static float getLivingDamage(LivingEntity living) {
        return Math.max(2, (living.getMaxHealth() + living.getAbsorptionAmount()) * 0.25F);
    }

    public static AABB expandInOneDirection(AABB box, Vec3 vec3d) {
        // there must be a better way to do this, right?
        double x = vec3d.x();
        double y = vec3d.y();
        double z = vec3d.z();
        if (x > 0) {
            box = box.setMaxX(box.maxX + x);
        } else if (x < 0) {
            box = box.setMinX(box.minX + x);
        }
        if (y > 0) {
            box = box.setMaxY(box.maxY + y);
        } else if (y < 0) {
            box = box.setMinY(box.minY + y);
        }
        if (z > 0) {
            box = box.setMaxZ(box.maxZ + z);
        } else if (z < 0) {
            box = box.setMinZ(box.minZ + z);
        }
        return box;
    }

    public void spawnDustParticles(Level world, BlockPos blockPos) {
        if (!world.isClientSide()) {
            return;
        }
        if (this.currentOutputDirection == null) {
            return;
        }
        if (this.distance <= DEFAULT_DISTANCE) {
            return;
        }
        Vec3i vec3i = currentOutputDirection.getUnitVec3i();
        BlockPos pos;
        for (int i = 1; i <= this.distance; i++) {
            pos = blockPos.offset(vec3i.multiply(i));
            Vec3 vec3d = pos.getCenter();
            world.addParticle(new DustParticleOptions(this.color, 1.0F), vec3d.x(), vec3d.y(), vec3d.z(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public @Nullable Object getRenderData() {
        return this.color;
    }

    // if there was an annotation for cursed methods, this method would have it
    @Contract(value = "violated")
    public @Nullable Object superGetRenderData() {
        return super.getRenderData();
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public boolean shouldSaveColor() {
        return true;
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        if (this.shouldSaveColor()) {
            builder.set(DataComponents.DYED_COLOR, new DyedItemColor(ARGB.transparent(this.color)));
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter dataComponentGetter) {
        if (this.shouldSaveColor()) {
            this.color = ARGB.opaque(dataComponentGetter.getOrDefault(DataComponents.DYED_COLOR, DEFAULT_DYE_COMPONENT).rgb());
        }
    }



    @Override
    public List<Section> getBeamSections() {
        /*? =1.21.8 {*/ /*if (this.renderSegments != null) return this.renderSegments; *//*?}*/
        return this.getBeamSegments(this.currentOutputDirection, this.distance);
    }

    public List<Section> getBeamSegments(Direction direction, int distance) {
        if (distance <= DEFAULT_DISTANCE || direction == null) {
            return List.of();
        }
        Section beam = new Section(this.color);
        ((BeamEmitterMixin.BeamSegmentMixin) beam).laseredstone$setHeight(distance + 1);
        return List.of(beam);
    }

    public Map<Direction, Integer> getDirectionToDistanceMap() {
        if (this.currentOutputDirection == null) {
            return ImmutableMap.of();
        }
        return ImmutableMap.of(this.currentOutputDirection, this.distance);
    }

    public int getColor() {
        return this.color;
    }

    public void setOvercharged(boolean overcharged) {
    }

    public boolean isOvercharged() {
        return false;
    }

    //? if =1.21.8 {
    /*public void setRenderingSegments(List<Section> beamSegments) {
        this.renderSegments = beamSegments;
    }
    *///?}
}
