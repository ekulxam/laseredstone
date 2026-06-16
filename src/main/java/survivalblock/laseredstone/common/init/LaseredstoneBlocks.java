package survivalblock.laseredstone.common.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
/*? if <1.21.11 {*/
/*import net.minecraft.resources.Identifier;*/
/*? } else {*/
import net.minecraft.resources.ResourceLocation;
/*? } */
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
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
            BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .sound(SoundType.GLASS)
                    .mapColor(MapColor.QUARTZ)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F)
                    .isValidSpawn(Blocks::never)
    );

    public static final Block HORIZONTAL_HORIZONTAL_MIRROR = registerBlock(
            "horizontal_horizontal_mirror",
            HorizontalHorizontalMirrorBlock::new,
            BlockBehaviour.Properties.ofFullCopy(HORIZONTAL_VERTICAL_MIRROR)
    );

    public static final Block LASER = registerBlock(
            "laser",
            LaserBlock::new,
            BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(10.0F)
                    .isValidSpawn(net.minecraft.world.level.block.Blocks::never)
    );

    public static final Block RECEIVER = registerBlock(
            "receiver",
            ReceiverBlock::new,
            BlockBehaviour.Properties.ofFullCopy(LASER).mapColor(MapColor.COLOR_BLACK)
    );

    public static final Block LENS = registerBlock(
            "lens",
            LensBlock::new,
            BlockBehaviour.Properties.ofFullCopy(RECEIVER).noOcclusion()
    );

    public static final Block DIFFUSER = registerBlock(
            "diffuser",
            DiffuserBlock::new,
            BlockBehaviour.Properties.ofFullCopy(RECEIVER).noOcclusion()
    );

    @SuppressWarnings("SameParameterValue")
    private static <T extends Block> T registerBlock(String name, Function<BlockBehaviour.Properties, T> blockFromSettings, BlockBehaviour.Properties settings) {
        /*? if <1.21.11 {*//*Identifier*//*?} *//*? else {*/ ResourceLocation /*?}*/ id = Laseredstone.id(name);
        return Registry.register(BuiltInRegistries.BLOCK, id, blockFromSettings.apply(settings.setId(ResourceKey.create(Registries.BLOCK, id))));
    }

    public static void init() {

    }
}
