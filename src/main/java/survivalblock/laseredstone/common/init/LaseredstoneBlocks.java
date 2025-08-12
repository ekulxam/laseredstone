package survivalblock.laseredstone.common.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import survivalblock.laseredstone.common.Laseredstone;
import survivalblock.laseredstone.common.block.LaserBlock;
import survivalblock.laseredstone.common.block.MirrorBlock;

import java.util.function.Function;

public class LaseredstoneBlocks {

    public static final Block HORIZONTAL_VERTICAL_MIRROR = registerBlock(
            "horizontal_vertical_mirror",
            MirrorBlock::new,
            AbstractBlock.Settings.create().nonOpaque()
    );

    public static final Block LASER = registerBlock(
            "laser",
            LaserBlock::new,
            AbstractBlock.Settings.create()
    );

    /*public static final BlockEntityType<AmarongCoreBlockEntity> AMARONG_CORE_BLOCK_ENTITY = registerBlockEntity(
            "amarong_core_block_entity",
            BlockEntityType.Builder.create(AmarongCoreBlockEntity::new, AMARONG_CORE).build()
    );

     */

    @SuppressWarnings("SameParameterValue")
    private static <T extends Block> T registerBlock(String name, Function<AbstractBlock.Settings, T> blockFromSettings, AbstractBlock.Settings settings) {
        Identifier id = Laseredstone.id(name);
        return Registry.register(Registries.BLOCK, id, blockFromSettings.apply(settings.registryKey(RegistryKey.of(RegistryKeys.BLOCK, id))));
    }

    public static void init() {

    }
}
