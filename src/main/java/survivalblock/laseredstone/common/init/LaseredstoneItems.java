package survivalblock.laseredstone.common.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;

public class LaseredstoneItems {

    public static final Item HORIZONTAL_VERTICAL_MIRROR = Items.register(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR);
    public static final Item HORIZONTAL_HORIZONTAL_MIRROR = Items.register(LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR);
    public static final Item LASER = Items.register(LaseredstoneBlocks.LASER);
    public static final Item RECEIVER = Items.register(LaseredstoneBlocks.RECEIVER);

    public static void init() {
        ItemGroupEvents.ModifyEntries addLaseredstoneItems = entries -> {
            entries.add(HORIZONTAL_VERTICAL_MIRROR);
            entries.add(HORIZONTAL_HORIZONTAL_MIRROR);
            entries.add(LASER);
            entries.add(RECEIVER);
        };
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(addLaseredstoneItems);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(addLaseredstoneItems);
    }
}
