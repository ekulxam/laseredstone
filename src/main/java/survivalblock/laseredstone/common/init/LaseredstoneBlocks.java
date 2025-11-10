package survivalblock.laseredstone.common.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import survivalblock.laseredstone.common.Laseredstone;
import survivalblock.laseredstone.common.block.DiffuserBlock;
import survivalblock.laseredstone.common.block.HorizontalHorizontalMirrorBlock;
import survivalblock.laseredstone.common.block.LaserBlock;
import survivalblock.laseredstone.common.block.HorizontalVerticalMirrorBlock;
import survivalblock.laseredstone.common.block.LensBlock;
import survivalblock.laseredstone.common.block.ReceiverBlock;

import java.util.function.Function;

public class LaseredstoneBlocks {

    public static final Block HORIZONTAL_VERTICAL_MIRROR = registerBlock(
            "horizontal_vertical_mirror",
            HorizontalVerticalMirrorBlock::new,
            AbstractBlock.Settings.create().nonOpaque()
                    .sounds(BlockSoundGroup.GLASS)
                    .mapColor(MapColor.OFF_WHITE)
                    .requiresTool()
                    .strength(5.0F)
                    .allowsSpawning(Blocks::never)
    );

    public static final Block HORIZONTAL_HORIZONTAL_MIRROR = registerBlock(
            "horizontal_horizontal_mirror",
            HorizontalHorizontalMirrorBlock::new,
            AbstractBlock.Settings.copy(HORIZONTAL_VERTICAL_MIRROR)
    );

    public static final Block LASER = registerBlock(
            "laser",
            LaserBlock::new,
            AbstractBlock.Settings.create()
                    .requiresTool()
                    .strength(10.0F)
                    .allowsSpawning(Blocks::never)
    );

    public static final Block RECEIVER = registerBlock(
            "receiver",
            ReceiverBlock::new,
            AbstractBlock.Settings.copy(LASER).mapColor(MapColor.BLACK)
    );

    public static final Block LENS = registerBlock(
            "lens",
            LensBlock::new,
            AbstractBlock.Settings.copy(RECEIVER).nonOpaque()
    );

    public static final Block DIFFUSER = registerBlock(
            "diffuser",
            DiffuserBlock::new,
            AbstractBlock.Settings.copy(RECEIVER).nonOpaque()
    );

    @SuppressWarnings("SameParameterValue")
    private static <T extends Block> T registerBlock(String name, Function<AbstractBlock.Settings, T> blockFromSettings, AbstractBlock.Settings settings) {
        Identifier id = Laseredstone.id(name);
        return Registry.register(Registries.BLOCK, id, blockFromSettings.apply(settings.registryKey(RegistryKey.of(RegistryKeys.BLOCK, id))));
    }

    public static void init() {

    }
}
