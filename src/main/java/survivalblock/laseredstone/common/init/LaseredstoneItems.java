package survivalblock.laseredstone.common.init;

//~ if >=26 'itemgroup.v1.ItemGroupEvents' -> 'creativetab.v1.CreativeModeTabEvents' {
//~ if >=26 'ItemGroupEvents' -> 'CreativeModeTabEvents' {
//~ if >=26 'ModifyEntries' -> 'ModifyOutput' {
//~ if >=26 'modifyEntriesEvent' -> 'modifyOutputEvent' {
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DamageResistant;
import net.minecraft.world.level.block.Block;

import java.util.function.BiFunction;
import java.util.function.Function;

public class LaseredstoneItems {

    public static final Item HORIZONTAL_VERTICAL_MIRROR = register(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR);
    public static final Item HORIZONTAL_HORIZONTAL_MIRROR = register(LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR);
    public static final Item LASER = register(LaseredstoneBlocks.LASER);
    public static final Item RECEIVER = register(LaseredstoneBlocks.RECEIVER);
    public static final Item LENS = register(LaseredstoneBlocks.LENS);
    public static final Item DIFFUSER = register(LaseredstoneBlocks.DIFFUSER);

    private static Item register(Block block) {
        return registerBlock(block, new Item.Properties()
                //? if <26 {
                .component(DataComponents.DAMAGE_RESISTANT, new DamageResistant(DamageTypeTags.IS_EXPLOSION))
                //?} else {
                /*.delayedComponent(DataComponents.DAMAGE_RESISTANT, context -> new DamageResistant(context.getOrThrow(DamageTypeTags.IS_EXPLOSION)))
                *///?}
        );
    }

    private static Item registerBlock(final Block block, final Item.Properties properties) {
        return registerBlock(block, BlockItem::new, properties);
    }

    private static ResourceKey<Item> blockIdToItemId(final ResourceKey<Block> blockName) {
        //~ if >=1.21.11 'location' -> 'identifier'
        return ResourceKey.create(Registries.ITEM, blockName.location());
    }

    private static Item registerBlock(final Block block, final BiFunction<Block, Item.Properties, Item> itemFactory, final Item.Properties properties) {
        return registerItem(
                blockIdToItemId(block.builtInRegistryHolder().key()),
                p -> itemFactory.apply(block, p),
                properties.useBlockDescriptionPrefix()/*? >=26 {*/ /*.requiredFeatures(block.requiredFeatures()) *//*?}*/
        );
    }

    private static Item registerItem(final ResourceKey<Item> key, final Function<Item.Properties, Item> itemFactory, final Item.Properties properties) {
        Item item = itemFactory.apply(properties.setId(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }

        return Registry.register(BuiltInRegistries.ITEM, key, item);
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
//~}
//~}
//~}
//~}