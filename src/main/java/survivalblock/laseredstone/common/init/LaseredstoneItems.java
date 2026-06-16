package survivalblock.laseredstone.common.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DamageResistant;
import net.minecraft.world.level.block.Block;

public class LaseredstoneItems {

    public static final Item HORIZONTAL_VERTICAL_MIRROR = register(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR);
    public static final Item HORIZONTAL_HORIZONTAL_MIRROR = register(LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR);
    public static final Item LASER = register(LaseredstoneBlocks.LASER);
    public static final Item RECEIVER = register(LaseredstoneBlocks.RECEIVER);
    public static final Item LENS = register(LaseredstoneBlocks.LENS);
    public static final Item DIFFUSER = register(LaseredstoneBlocks.DIFFUSER);

    private static Item register(Block block) {
        return Items.registerBlock(block, new Item.Properties().component(DataComponents.DAMAGE_RESISTANT, new DamageResistant(DamageTypeTags.IS_EXPLOSION)));
    }

    public static void init() {
        ItemGroupEvents.ModifyEntries addLaseredstoneItems = entries -> {
            entries.accept(HORIZONTAL_VERTICAL_MIRROR);
            entries.accept(HORIZONTAL_HORIZONTAL_MIRROR);
            entries.accept(LASER);
            entries.accept(RECEIVER);
            entries.accept(LENS);
            entries.accept(DIFFUSER);
        };
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(addLaseredstoneItems);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(addLaseredstoneItems);
    }
}
