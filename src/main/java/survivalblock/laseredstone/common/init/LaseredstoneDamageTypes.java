package survivalblock.laseredstone.common.init;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import survivalblock.laseredstone.common.Laseredstone;

public class LaseredstoneDamageTypes {

    public static final RegistryKey<DamageType> LASER = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Laseredstone.id("laser"));

    public static void bootstrap(Registerable<DamageType> registerable) {
        registerable.register(LASER, new DamageType("laseredstone.laser", 0.1F));
    }

    public static <T> RegistryEntry<T> getFromWorld(World world, RegistryKey<T> key) {
        return getFromDRM(world.getRegistryManager(), key);
    }

    public static <T> RegistryEntry<T> getFromDRM(DynamicRegistryManager drm, RegistryKey<T> key) {
        return drm.getEntryOrThrow(key);
    }
}
