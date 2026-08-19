package survivalblock.laseredstone.common.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import survivalblock.atmosphere.registrar.BlockEntityTypeRegistrant;
import survivalblock.laseredstone.common.Laseredstone;
import survivalblock.laseredstone.common.block.entity.*;

public final class LaseredstoneBlockEntityTypes {
    private LaseredstoneBlockEntityTypes() {
    }

    private static final BlockEntityTypeRegistrant REGISTRANT = new BlockEntityTypeRegistrant(Laseredstone.MOD_ID);

    public static final BlockEntityType<LaserBlockEntity> LASER =
            REGISTRANT.register("laser", LaserBlockEntity::new, LaseredstoneBlocks.LASER);

    public static final BlockEntityType<HorizontalVerticalMirrorBlockEntity> HORIZONTAL_VERTICAL_MIRROR =
            REGISTRANT.register("horizontal_vertical_mirror", HorizontalVerticalMirrorBlockEntity::new, LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR);

    public static final BlockEntityType<HorizontalHorizontalMirrorBlockEntity> HORIZONTAL_HORIZONTAL_MIRROR =
            REGISTRANT.register("horizontal_horizontal_mirror", HorizontalHorizontalMirrorBlockEntity::new, LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR);

    public static final BlockEntityType<ReceiverBlockEntity> RECEIVER =
            REGISTRANT.register("receiver", ReceiverBlockEntity::new, LaseredstoneBlocks.RECEIVER);

    public static final BlockEntityType<LensBlockEntity> LENS =
            REGISTRANT.register("lens", LensBlockEntity::new, LaseredstoneBlocks.LENS);

    public static final BlockEntityType<DiffuserBlockEntity> DIFFUSER =
            REGISTRANT.register("diffuser", DiffuserBlockEntity::new, LaseredstoneBlocks.DIFFUSER);

    public static void init() {
        // NO-OP
    }
}
