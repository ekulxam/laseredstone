package survivalblock.laseredstone.common.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import survivalblock.atmosphere.registrar.BlockRegistrant;
import survivalblock.laseredstone.common.Laseredstone;
import survivalblock.laseredstone.common.block.DiffuserBlock;
import survivalblock.laseredstone.common.block.HorizontalHorizontalMirrorBlock;
import survivalblock.laseredstone.common.block.LaserBlock;
import survivalblock.laseredstone.common.block.HorizontalVerticalMirrorBlock;
import survivalblock.laseredstone.common.block.LensBlock;
import survivalblock.laseredstone.common.block.ReceiverBlock;

public final class LaseredstoneBlocks {
    private LaseredstoneBlocks() {
    }

    private static final BlockRegistrant REGISTRANT = new BlockRegistrant(Laseredstone.MOD_ID);

    public static final Block HORIZONTAL_VERTICAL_MIRROR = REGISTRANT.register(
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

    public static final Block HORIZONTAL_HORIZONTAL_MIRROR = REGISTRANT.register(
            "horizontal_horizontal_mirror",
            HorizontalHorizontalMirrorBlock::new,
            BlockBehaviour.Properties.ofFullCopy(HORIZONTAL_VERTICAL_MIRROR)
    );

    public static final Block LASER = REGISTRANT.register(
            "laser",
            LaserBlock::new,
            BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(10.0F)
                    .isValidSpawn(net.minecraft.world.level.block.Blocks::never)
    );

    public static final Block RECEIVER = REGISTRANT.register(
            "receiver",
            ReceiverBlock::new,
            BlockBehaviour.Properties.ofFullCopy(LASER).mapColor(MapColor.COLOR_BLACK)
    );

    public static final Block LENS = REGISTRANT.register(
            "lens",
            LensBlock::new,
            BlockBehaviour.Properties.ofFullCopy(RECEIVER).noOcclusion()
    );

    public static final Block DIFFUSER = REGISTRANT.register(
            "diffuser",
            DiffuserBlock::new,
            BlockBehaviour.Properties.ofFullCopy(RECEIVER).noOcclusion()
    );

    public static void init() {

    }
}
