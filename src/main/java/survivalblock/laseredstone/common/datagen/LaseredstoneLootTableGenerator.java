package survivalblock.laseredstone.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.CopyComponentsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryWrapper;
import survivalblock.laseredstone.common.init.LaseredstoneBlocks;

import java.util.concurrent.CompletableFuture;

public class LaseredstoneLootTableGenerator extends FabricBlockLootTableProvider {

    protected LaseredstoneLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(LaseredstoneBlocks.LASER, this::laserDrops);
        addDrop(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR);
        addDrop(LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR);
        addDrop(LaseredstoneBlocks.RECEIVER);
        addDrop(LaseredstoneBlocks.LENS);
        addDrop(LaseredstoneBlocks.DIFFUSER);
    }

    public LootTable.Builder laserDrops(Block drop) {
        return LootTable.builder()
                .pool(
                        this.addSurvivesExplosionCondition(
                                drop,
                                LootPool.builder()
                                        .rolls(ConstantLootNumberProvider.create(1.0F))
                                        .with(
                                                ItemEntry.builder(drop)
                                                        .apply(
                                                                CopyComponentsLootFunction./*? =1.21.8 {*/ builder(CopyComponentsLootFunction.Source.BLOCK_ENTITY) /*?} else {*/ /*blockEntity(LootContextParameters.BLOCK_ENTITY) *//*?}*/
                                                                        .include(DataComponentTypes.DYED_COLOR)
                                                        )
                                        )
                        )
                );
    }
}
