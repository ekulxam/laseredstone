package survivalblock.laseredstone.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.ItemTags;
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
		pack.addProvider((fabricDataOutput, completableFuture) -> new FabricTagProvider.ItemTagProvider(fabricDataOutput, completableFuture) {
			@Override
			protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
				valueLookupBuilder(ItemTags.DYEABLE).add(LaseredstoneItems.LASER);
			}
		});
		pack.addProvider(((fabricDataOutput, completableFuture) -> new FabricTagProvider.BlockTagProvider(fabricDataOutput, completableFuture) {
			@Override
			protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
				valueLookupBuilder(BlockTags.PICKAXE_MINEABLE).add(
						LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR,
						LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR,
						LaseredstoneBlocks.LASER,
						LaseredstoneBlocks.RECEIVER);
				valueLookupBuilder(LaseredstoneTags.ALWAYS_ALLOWS_LASERS)
						.add(Blocks.BEDROCK);
				valueLookupBuilder(LaseredstoneTags.ALWAYS_DENIES_LASERS)
						.addOptional(Blocks.TINTED_GLASS);
			}
		}));
		pack.addProvider((fabricDataOutput, completableFuture) -> new FabricTagProvider<DamageType>(fabricDataOutput, RegistryKeys.DAMAGE_TYPE, completableFuture) {
			@Override
			protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
				builder(DamageTypeTags.BYPASSES_COOLDOWN)
						.add(LaseredstoneDamageTypes.LASER);

				builder(DamageTypeTags.BYPASSES_ARMOR)
						.add(LaseredstoneDamageTypes.LASER);

				builder(DamageTypeTags.BYPASSES_EFFECTS)
						.add(LaseredstoneDamageTypes.LASER);
			}
		});
		pack.addProvider(LaseredstoneLootTableGenerator::new);
		pack.addProvider(LaseredstoneRecipeGenerator::new);
		pack.addProvider(LaseredstoneDynamicRegistryGenerator::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.DAMAGE_TYPE, LaseredstoneDamageTypes::bootstrap);
	}
}
