package survivalblock.laseredstone.common.init;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import survivalblock.laseredstone.common.Laseredstone;

public class LaseredstoneTags {

    public static final TagKey<Block> ALWAYS_DENIES_LASERS = TagKey.of(RegistryKeys.BLOCK, Laseredstone.id("always_denies_lasers"));
    public static final TagKey<Block> ALWAYS_ALLOWS_LASERS = TagKey.of(RegistryKeys.BLOCK, Laseredstone.id("always_allows_lasers"));

    public static final TagKey<Item> LASER_POWERER = TagKey.of(RegistryKeys.ITEM, Laseredstone.id("laser_powerer"));

    public static final TagKey<DamageType> BYPASSES_CREATIVE_MODE = TagKey.of(RegistryKeys.DAMAGE_TYPE, Laseredstone.id("bypasses_creative_mode"));

    public static final TagKey<EntityType<?>> LASER_PROOF = TagKey.of(RegistryKeys.ENTITY_TYPE, Laseredstone.id("laser_proof"));
}
