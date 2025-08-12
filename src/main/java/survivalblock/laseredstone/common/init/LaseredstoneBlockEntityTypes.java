package survivalblock.laseredstone.common.init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import survivalblock.laseredstone.common.Laseredstone;
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;
import survivalblock.laseredstone.common.block.entity.MirrorBlockEntity;

public class LaseredstoneBlockEntityTypes {

    public static final BlockEntityType<LaserBlockEntity> LASER =
            register("laser", LaserBlockEntity::new, LaseredstoneBlocks.LASER);

    public static final BlockEntityType<LaserBlockEntity> MIRROR =
            register("mirror", MirrorBlockEntity::new, LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR);

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
