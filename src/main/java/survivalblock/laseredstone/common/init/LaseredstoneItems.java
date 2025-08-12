package survivalblock.laseredstone.common.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;

public class LaseredstoneItems {

    public static final Item HORIZONTAL_VERTICAL_MIRROR = Items.register(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR);
    public static final Item LASER = Items.register(LaseredstoneBlocks.LASER);

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
            entries.add(HORIZONTAL_VERTICAL_MIRROR);
            entries.add(LASER);
        });
    }
}
