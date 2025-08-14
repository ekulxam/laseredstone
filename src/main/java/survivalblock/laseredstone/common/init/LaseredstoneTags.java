package survivalblock.laseredstone.common.init;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import survivalblock.laseredstone.common.Laseredstone;

public class LaseredstoneTags {

    public static final TagKey<Block> ALWAYS_DENIES_LASERS = TagKey.of(RegistryKeys.BLOCK, Laseredstone.id("always_denies_lasers"));
    public static final TagKey<Block> ALWAYS_ALLOWS_LASERS = TagKey.of(RegistryKeys.BLOCK, Laseredstone.id("always_allows_lasers"));
}
