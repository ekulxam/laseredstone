package survivalblock.laseredstone.common.init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import survivalblock.laseredstone.common.Laseredstone;
import survivalblock.laseredstone.common.block.entity.HorizontalHorizontalMirrorBlockEntity;
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;
import survivalblock.laseredstone.common.block.entity.HorizontalVerticalMirrorBlockEntity;
import survivalblock.laseredstone.common.block.entity.MirrorBlockEntity;
import survivalblock.laseredstone.common.block.entity.ReceiverBlockEntity;

public class LaseredstoneBlockEntityTypes {

    public static final BlockEntityType<LaserBlockEntity> LASER =
            register("laser", LaserBlockEntity::new, LaseredstoneBlocks.LASER);

    public static final BlockEntityType<HorizontalVerticalMirrorBlockEntity> HORIZONTAL_VERTICAL_MIRROR =
            register("horizontal_vertical_mirror", HorizontalVerticalMirrorBlockEntity::new, LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR);

    public static final BlockEntityType<HorizontalHorizontalMirrorBlockEntity> HORIZONTAL_HORIZONTAL_MIRROR =
            register("horizontal_horizontal_mirror", HorizontalHorizontalMirrorBlockEntity::new, LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR);

    public static final BlockEntityType<ReceiverBlockEntity> RECEIVER =
            register("receiver", ReceiverBlockEntity::new, LaseredstoneBlocks.RECEIVER);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Laseredstone.id(name), FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    public static void init() {

    }
}
