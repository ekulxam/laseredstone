package survivalblock.laseredstone.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import survivalblock.laseredstone.common.init.LaseredstoneBlocks;
import survivalblock.laseredstone.common.init.LaseredstoneItems;

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
			}
		}));
	}
}
