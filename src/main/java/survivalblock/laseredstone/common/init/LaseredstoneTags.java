package survivalblock.laseredstone.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import survivalblock.laseredstone.common.Laseredstone;

public class LaseredstoneTags {

    public static final TagKey<Block> ALWAYS_DENIES_LASERS = TagKey.create(Registries.BLOCK, Laseredstone.id("always_denies_lasers"));
    public static final TagKey<Block> ALWAYS_ALLOWS_LASERS = TagKey.create(Registries.BLOCK, Laseredstone.id("always_allows_lasers"));

    public static final TagKey<Item> LASER_POWERER = TagKey.create(Registries.ITEM, Laseredstone.id("laser_powerer"));

    public static final net.minecraft.tags.TagKey<net.minecraft.world.damagesource.DamageType> BYPASSES_CREATIVE_MODE = TagKey.create(Registries.DAMAGE_TYPE, Laseredstone.id("bypasses_creative_mode"));

    public static final TagKey<EntityType<?>> LASER_PROOF = TagKey.create(Registries.ENTITY_TYPE, Laseredstone.id("laser_proof"));
}
