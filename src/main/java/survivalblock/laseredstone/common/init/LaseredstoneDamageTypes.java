package survivalblock.laseredstone.common.init;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import survivalblock.atmosphere.registrar.dynamic.DamageTypeRegistrant;
import survivalblock.laseredstone.common.Laseredstone;

public final class LaseredstoneDamageTypes {
    private LaseredstoneDamageTypes() {
    }

    public static final DamageTypeRegistrant REGISTRANT = new DamageTypeRegistrant(Laseredstone.MOD_ID);

    public static final ResourceKey<DamageType> LASER = REGISTRANT.register(
            "laser", new DamageType("laseredstone.laser", 0.1F)
    );

    public static <T> Holder<T> getFromWorld(Level world, ResourceKey<T> key) {
        return getFromDRM(world.registryAccess(), key);
    }

    public static <T> Holder<T> getFromDRM(RegistryAccess drm, ResourceKey<T> key) {
        return drm.getOrThrow(key);
    }
}
