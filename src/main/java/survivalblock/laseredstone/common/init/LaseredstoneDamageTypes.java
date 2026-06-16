package survivalblock.laseredstone.common.init;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import survivalblock.laseredstone.common.Laseredstone;


public class LaseredstoneDamageTypes {

    public static final ResourceKey<DamageType> LASER = ResourceKey.create(Registries.DAMAGE_TYPE, Laseredstone.id("laser"));

    public static void bootstrap(BootstrapContext<DamageType> registerable) {
        registerable.register(LASER, new DamageType("laseredstone.laser", 0.1F));
    }

    public static <T> Holder<T> getFromWorld(Level world, ResourceKey<T> key) {
        return getFromDRM(world.registryAccess(), key);
    }

    public static <T> Holder<T> getFromDRM(RegistryAccess drm, ResourceKey<T> key) {
        return drm.getOrThrow(key);
    }
}
