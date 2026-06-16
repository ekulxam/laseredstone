package survivalblock.laseredstone.common.init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import survivalblock.laseredstone.common.Laseredstone;
import survivalblock.laseredstone.common.block.entity.*;

public class LaseredstoneBlockEntityTypes {

    public static final BlockEntityType<LaserBlockEntity> LASER =
            register("laser", LaserBlockEntity::new, LaseredstoneBlocks.LASER);

    public static final BlockEntityType<HorizontalVerticalMirrorBlockEntity> HORIZONTAL_VERTICAL_MIRROR =
            register("horizontal_vertical_mirror", HorizontalVerticalMirrorBlockEntity::new, LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR);

    public static final BlockEntityType<HorizontalHorizontalMirrorBlockEntity> HORIZONTAL_HORIZONTAL_MIRROR =
            register("horizontal_horizontal_mirror", HorizontalHorizontalMirrorBlockEntity::new, LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR);

    public static final BlockEntityType<ReceiverBlockEntity> RECEIVER =
            register("receiver", ReceiverBlockEntity::new, LaseredstoneBlocks.RECEIVER);

    public static final BlockEntityType<LensBlockEntity> LENS =
            register("lens", LensBlockEntity::new, LaseredstoneBlocks.LENS);

    public static final BlockEntityType<DiffuserBlockEntity> DIFFUSER =
            register("diffuser", DiffuserBlockEntity::new, LaseredstoneBlocks.DIFFUSER);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Laseredstone.id(name), FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    public static void init() {

    }
}
