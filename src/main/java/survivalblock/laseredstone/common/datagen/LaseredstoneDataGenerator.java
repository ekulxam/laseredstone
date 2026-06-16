package survivalblock.laseredstone.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import survivalblock.laseredstone.common.init.LaseredstoneBlocks;
import survivalblock.laseredstone.common.init.LaseredstoneDamageTypes;
import survivalblock.laseredstone.common.init.LaseredstoneItems;
import survivalblock.laseredstone.common.init.LaseredstoneTags;

public class LaseredstoneDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(LaseredstoneModelGenerator::new);
		pack.addProvider(LaseredstoneEnUsLangGenerator::new);
		pack.addProvider((fabricDataOutput, completableFuture) -> new FabricTagsProvider.ItemTagsProvider(fabricDataOutput, completableFuture) {
			@Override
			protected void addTags(HolderLookup.Provider wrapperLookup) {
                //~ if >=26 'DYEABLE' -> 'CAULDRON_CAN_REMOVE_DYE' {
				valueLookupBuilder(ItemTags.CAULDRON_CAN_REMOVE_DYE).add(LaseredstoneItems.LASER);
                valueLookupBuilder(LaseredstoneTags.LASER_POWERER).add(Items.BEACON);
                //~}
			}
		});
		pack.addProvider(((fabricDataOutput, completableFuture) -> new FabricTagsProvider.BlockTagsProvider(fabricDataOutput, completableFuture) {
			@Override
			protected void addTags(HolderLookup.Provider wrapperLookup) {
				valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(
						LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR,
						LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR,
						LaseredstoneBlocks.LASER,
						LaseredstoneBlocks.RECEIVER,
                        LaseredstoneBlocks.LENS,
                        LaseredstoneBlocks.DIFFUSER);
				valueLookupBuilder(LaseredstoneTags.ALWAYS_ALLOWS_LASERS)
						.add(Blocks.BEDROCK);
				valueLookupBuilder(LaseredstoneTags.ALWAYS_DENIES_LASERS)
						.addOptional(Blocks.TINTED_GLASS);
			}
		}));
		pack.addProvider((fabricDataOutput, completableFuture) -> new FabricTagsProvider<DamageType>(fabricDataOutput, Registries.DAMAGE_TYPE, completableFuture) {
			@Override
			protected void addTags(HolderLookup.Provider wrapperLookup) {
				builder(DamageTypeTags.BYPASSES_COOLDOWN)
						.add(LaseredstoneDamageTypes.LASER);

				builder(DamageTypeTags.BYPASSES_ARMOR)
						.add(LaseredstoneDamageTypes.LASER);

				builder(DamageTypeTags.BYPASSES_EFFECTS)
						.add(LaseredstoneDamageTypes.LASER);

				builder(LaseredstoneTags.BYPASSES_CREATIVE_MODE)
						.add(LaseredstoneDamageTypes.LASER);
			}
		});
		pack.addProvider((fabricDataOutput, completableFuture) -> new FabricTagsProvider.EntityTypeTagsProvider(fabricDataOutput, completableFuture) {
			@Override
			protected void addTags(final HolderLookup.Provider wrapperLookup) {
				valueLookupBuilder(LaseredstoneTags.LASER_PROOF).add(
						EntityType.BLOCK_DISPLAY,
						EntityType.ITEM_DISPLAY,
						EntityType.TEXT_DISPLAY,
						EntityType.INTERACTION
				);
			}
		});
		pack.addProvider(LaseredstoneLootTableGenerator::new);
		pack.addProvider(LaseredstoneRecipeGenerator::new);
		pack.addProvider(LaseredstoneDynamicRegistryGenerator::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.DAMAGE_TYPE, LaseredstoneDamageTypes::bootstrap);
	}
}
