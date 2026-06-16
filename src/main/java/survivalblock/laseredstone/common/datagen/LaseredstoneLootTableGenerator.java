package survivalblock.laseredstone.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import survivalblock.laseredstone.common.init.LaseredstoneBlocks;

import java.util.concurrent.CompletableFuture;

public class LaseredstoneLootTableGenerator extends FabricBlockLootTableProvider {

    protected LaseredstoneLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        add(LaseredstoneBlocks.LASER, this::laserDrops);
        dropSelf(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR);
        dropSelf(LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR);
        dropSelf(LaseredstoneBlocks.RECEIVER);
        dropSelf(LaseredstoneBlocks.LENS);
        dropSelf(LaseredstoneBlocks.DIFFUSER);
    }

    public LootTable.Builder laserDrops(Block drop) {
        return LootTable.lootTable()
                .withPool(
                        this.applyExplosionCondition(
                                drop,
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(drop)
                                                        .apply(
                                                                CopyComponentsFunction./*? =1.21.8 {*/ copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY) /*?} else {*/ /*copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY) *//*?}*/
                                                                        .include(DataComponents.DYED_COLOR)
                                                        )
                                        )
                        )
                );
    }
}
