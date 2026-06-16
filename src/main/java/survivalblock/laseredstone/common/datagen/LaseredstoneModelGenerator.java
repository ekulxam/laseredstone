package survivalblock.laseredstone.common.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;

import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.core.Direction;
/*? if <1.21.11 {*/
/*import net.minecraft.resources.Identifier;*/
/*? } else {*/
import net.minecraft.resources.ResourceLocation;
/*? } */
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;
import survivalblock.laseredstone.common.init.LaseredstoneBlocks;
import survivalblock.laseredstone.common.init.LaseredstoneItems;

import static net.minecraft.client.data.models.BlockModelGenerators.*;

public class LaseredstoneModelGenerator extends FabricModelProvider {

	private static final PropertyDispatch<VariantMutator> NORTH_DEFAULT_ROTATION_OPERATIONS = PropertyDispatch.modify(BlockStateProperties.FACING)
			.select(Direction.DOWN, X_ROT_90)
			.select(Direction.UP, X_ROT_270)
			.select(Direction.NORTH, NOP)
			.select(Direction.SOUTH, Y_ROT_180)
			.select(Direction.WEST, Y_ROT_270)
			.select(Direction.EAST, Y_ROT_90);

	public LaseredstoneModelGenerator(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
		/*? if <1.21.11 {*//*Identifier*//*?} *//*? else {*/ ResourceLocation /*?}*/ hvModel = ModelLocationUtils.getModelLocation(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR);
		blockStateModelGenerator.blockStateOutput.accept(createHVBlockState(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR, hvModel));
		blockStateModelGenerator.registerSimpleItemModel(LaseredstoneItems.HORIZONTAL_VERTICAL_MIRROR, hvModel);

		/*? if <1.21.11 {*//*Identifier*//*?} *//*? else {*/ ResourceLocation /*?}*/ hhModel = ModelLocationUtils.getModelLocation(LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR);
		blockStateModelGenerator.blockStateOutput.accept(createHHBlockState(LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR, hvModel));
		blockStateModelGenerator.registerSimpleItemModel(LaseredstoneItems.HORIZONTAL_HORIZONTAL_MIRROR, hvModel);

		/*? if <1.21.11 {*//*Identifier*//*?} *//*? else {*/ ResourceLocation /*?}*/ laserModel = ModelLocationUtils.getModelLocation(LaseredstoneBlocks.LASER);
		registerOrientableRedstone(blockStateModelGenerator, LaseredstoneBlocks.LASER, laserModel);
		blockStateModelGenerator.registerSimpleTintedItemModel(LaseredstoneBlocks.LASER, laserModel, new Dye(LaserBlockEntity.DEFAULT_COLOR));

		registerRedstone(blockStateModelGenerator, LaseredstoneBlocks.RECEIVER);
		registerOrientableWithItem(blockStateModelGenerator, LaseredstoneBlocks.LENS);
        registerOrientableRedstone(blockStateModelGenerator, LaseredstoneBlocks.DIFFUSER, ModelLocationUtils.getModelLocation(LaseredstoneBlocks.DIFFUSER));
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {

	}

	public static BlockModelDefinitionGenerator createHVBlockState(Block stairsBlock, /*? if <1.21.11 {*//*Identifier*//*?} *//*? else {*/ ResourceLocation /*?}*/ modelId) {
		MultiVariant straightModel = plainVariant(modelId);
		return MultiVariantGenerator.dispatch(stairsBlock)
				.with(
						PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF)
								.select(Direction.EAST, Half.BOTTOM, straightModel)
								.select(Direction.WEST, Half.BOTTOM, straightModel.with(Y_ROT_180))
								.select(Direction.SOUTH, Half.BOTTOM, straightModel.with(Y_ROT_90))
								.select(Direction.NORTH, Half.BOTTOM, straightModel.with(Y_ROT_270))
								.select(Direction.EAST, Half.TOP, straightModel.with(X_ROT_180))
								.select(Direction.WEST, Half.TOP, straightModel.with(X_ROT_180).with(Y_ROT_180))
								.select(Direction.SOUTH, Half.TOP, straightModel.with(X_ROT_180).with(Y_ROT_90))
								.select(Direction.NORTH, Half.TOP, straightModel.with(X_ROT_180).with(Y_ROT_270))
				);
	}

	public static BlockModelDefinitionGenerator createHHBlockState(Block stairsBlock, /*? if <1.21.11 {*//*Identifier*//*?} *//*? else {*/ ResourceLocation /*?}*/ modelId) {
		MultiVariant straightModel = plainVariant(modelId).with(X_ROT_90);
		return MultiVariantGenerator.dispatch(stairsBlock)
				.with(
						PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING)
								.select(Direction.EAST, straightModel)
								.select(Direction.WEST, straightModel.with(Y_ROT_180))
								.select(Direction.SOUTH, straightModel.with(Y_ROT_90))
								.select(Direction.NORTH, straightModel.with(Y_ROT_270))
				);
	}

	public static void registerOrientableWithItem(BlockModelGenerators generator, Block orientable) {
		/*? if <1.21.11 {*//*Identifier*//*?} *//*? else {*/ ResourceLocation /*?}*/ modelId = ModelLocationUtils.getModelLocation(orientable);
		MultiVariant weightedVariant = plainVariant(modelId);
		generator.blockStateOutput
				.accept(
						MultiVariantGenerator.dispatch(orientable, weightedVariant)
								./*? =1.21.8 {*/ with /*?} else {*/ /*with *//*?}*/(NORTH_DEFAULT_ROTATION_OPERATIONS)
				);
		generator.registerSimpleItemModel(orientable.asItem(), modelId);
	}

	public static void registerOrientableRedstone(BlockModelGenerators generator, Block orientable, /*? if <1.21.11 {*//*Identifier*//*?} *//*? else {*/ ResourceLocation /*?}*/ modelId) {
		MultiVariant weightedVariant = plainVariant(modelId);
		MultiVariant weightedVariant2 = plainVariant(ModelLocationUtils.getModelLocation(orientable, "_on"));
		generator.blockStateOutput
				.accept(
						MultiVariantGenerator.dispatch(orientable)
								.with(createBooleanModelDispatch(BlockStateProperties.POWERED, weightedVariant2, weightedVariant))
								./*? =1.21.8 {*/ with /*?} else {*/ /*with *//*?}*/(NORTH_DEFAULT_ROTATION_OPERATIONS)
				);
	}

	public static void registerRedstone(BlockModelGenerators generator, Block block) {
		/*? if <1.21.11 {*//*Identifier*//*?} *//*? else {*/ ResourceLocation /*?}*/ modelId = ModelLocationUtils.getModelLocation(block);
		MultiVariant weightedVariant = plainVariant(modelId);
		MultiVariant weightedVariant2 = plainVariant(ModelLocationUtils.getModelLocation(block, "_on"));
		generator.blockStateOutput
				.accept(MultiVariantGenerator.dispatch(block).with(createBooleanModelDispatch(BlockStateProperties.POWERED, weightedVariant2, weightedVariant)));
		generator.registerSimpleItemModel(block.asItem(), modelId);
	}
}
