package survivalblock.laseredstone.common.init;

//~ if >=26 'itemgroup.v1.ItemGroupEvents' -> 'creativetab.v1.CreativeModeTabEvents' {
//~ if >=26 'ItemGroupEvents' -> 'CreativeModeTabEvents' {
//~ if >=26 'ModifyEntries' -> 'ModifyOutput' {
//~ if >=26 'modifyEntriesEvent' -> 'modifyOutputEvent' {
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DamageResistant;
import net.minecraft.world.level.block.Block;
import survivalblock.atmosphere.registrar.ItemRegistrant;
import survivalblock.laseredstone.common.Laseredstone;

public final class LaseredstoneItems {
    private LaseredstoneItems() {
    }

    private static final ItemRegistrant REGISTRANT = new ItemRegistrant(Laseredstone.MOD_ID);

    public static final Item HORIZONTAL_VERTICAL_MIRROR = register(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR);
    public static final Item HORIZONTAL_HORIZONTAL_MIRROR = register(LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR);
    public static final Item LASER = register(LaseredstoneBlocks.LASER);
    public static final Item RECEIVER = register(LaseredstoneBlocks.RECEIVER);
    public static final Item LENS = register(LaseredstoneBlocks.LENS);
    public static final Item DIFFUSER = register(LaseredstoneBlocks.DIFFUSER);

    private static Item register(Block block) {
        return REGISTRANT.register(
                block,
                new Item.Properties()
                        .useBlockDescriptionPrefix()
                        /*? >=26 {*/ .requiredFeatures(block.requiredFeatures()) /*?}*/
                        //? if <26 {
                        /*.component(DataComponents.DAMAGE_RESISTANT, new DamageResistant(DamageTypeTags.IS_EXPLOSION))
                        *///?} else {
                        .delayedComponent(DataComponents.DAMAGE_RESISTANT, context -> new DamageResistant(context.getOrThrow(DamageTypeTags.IS_EXPLOSION)))
                        //?}
        );
    }

    public static void init() {
        CreativeModeTabEvents.ModifyOutput addLaseredstoneItems = entries -> {
            entries.accept(HORIZONTAL_VERTICAL_MIRROR);
            entries.accept(HORIZONTAL_HORIZONTAL_MIRROR);
            entries.accept(LASER);
            entries.accept(RECEIVER);
            entries.accept(LENS);
            entries.accept(DIFFUSER);
        };
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(addLaseredstoneItems);
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(addLaseredstoneItems);
    }
}
//~}
//~}
//~}
//~}