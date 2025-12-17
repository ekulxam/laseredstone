package survivalblock.laseredstone.common.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DamageResistantComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.DamageTypeTags;

public class LaseredstoneItems {

    public static final Item HORIZONTAL_VERTICAL_MIRROR = register(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR);
    public static final Item HORIZONTAL_HORIZONTAL_MIRROR = register(LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR);
    public static final Item LASER = register(LaseredstoneBlocks.LASER);
    public static final Item RECEIVER = register(LaseredstoneBlocks.RECEIVER);
    public static final Item LENS = register(LaseredstoneBlocks.LENS);
    public static final Item DIFFUSER = register(LaseredstoneBlocks.DIFFUSER);

    private static Item register(Block block) {
        return Items.register(block, new Item.Settings().component(DataComponentTypes.DAMAGE_RESISTANT, new DamageResistantComponent(DamageTypeTags.IS_EXPLOSION)));
    }

    public static void init() {
        ItemGroupEvents.ModifyEntries addLaseredstoneItems = entries -> {
            entries.add(HORIZONTAL_VERTICAL_MIRROR);
            entries.add(HORIZONTAL_HORIZONTAL_MIRROR);
            entries.add(LASER);
            entries.add(RECEIVER);
            entries.add(LENS);
            entries.add(DIFFUSER);
        };
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(addLaseredstoneItems);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(addLaseredstoneItems);
    }
}
