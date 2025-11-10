package survivalblock.laseredstone.common.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.client.data.BlockModelDefinitionCreator;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ModelIds;
import net.minecraft.client.data.VariantsBlockModelDefinitionCreator;
import net.minecraft.client.render.item.tint.DyeTintSource;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;
import survivalblock.laseredstone.common.init.LaseredstoneBlocks;
import survivalblock.laseredstone.common.init.LaseredstoneItems;

import static net.minecraft.client.data.BlockStateModelGenerator.*;

public class LaseredstoneModelGenerator extends FabricModelProvider {

	private static final BlockStateVariantMap<ModelVariantOperator> NORTH_DEFAULT_ROTATION_OPERATIONS = BlockStateVariantMap.operations(Properties.FACING)
			.register(Direction.DOWN, ROTATE_X_90)
			.register(Direction.UP, ROTATE_X_270)
			.register(Direction.NORTH, NO_OP)
			.register(Direction.SOUTH, ROTATE_Y_180)
			.register(Direction.WEST, ROTATE_Y_270)
			.register(Direction.EAST, ROTATE_Y_90);

	public LaseredstoneModelGenerator(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		Identifier hvModel = ModelIds.getBlockModelId(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR);
		blockStateModelGenerator.blockStateCollector.accept(createHVBlockState(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR, hvModel));
		blockStateModelGenerator.registerItemModel(LaseredstoneItems.HORIZONTAL_VERTICAL_MIRROR, hvModel);

		Identifier hhModel = ModelIds.getBlockModelId(LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR);
		blockStateModelGenerator.blockStateCollector.accept(createHHBlockState(LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR, hvModel));
		blockStateModelGenerator.registerItemModel(LaseredstoneItems.HORIZONTAL_HORIZONTAL_MIRROR, hvModel);

		Identifier laserModel = ModelIds.getBlockModelId(LaseredstoneBlocks.LASER);
		registerOrientableRedstone(blockStateModelGenerator, LaseredstoneBlocks.LASER, laserModel);
		blockStateModelGenerator.registerTintedItemModel(LaseredstoneBlocks.LASER, laserModel, new DyeTintSource(LaserBlockEntity.DEFAULT_COLOR));

		registerRedstone(blockStateModelGenerator, LaseredstoneBlocks.RECEIVER);
		registerOrientableWithItem(blockStateModelGenerator, LaseredstoneBlocks.LENS);
        registerOrientableRedstone(blockStateModelGenerator, LaseredstoneBlocks.DIFFUSER, ModelIds.getBlockModelId(LaseredstoneBlocks.DIFFUSER));
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
	}

	public static BlockModelDefinitionCreator createHVBlockState(Block stairsBlock, Identifier modelId) {
		WeightedVariant straightModel = createWeightedVariant(modelId);
		return VariantsBlockModelDefinitionCreator.of(stairsBlock)
				.with(
						BlockStateVariantMap.models(Properties.HORIZONTAL_FACING, Properties.BLOCK_HALF)
								.register(Direction.EAST, BlockHalf.BOTTOM, straightModel)
								.register(Direction.WEST, BlockHalf.BOTTOM, straightModel.apply(ROTATE_Y_180))
								.register(Direction.SOUTH, BlockHalf.BOTTOM, straightModel.apply(ROTATE_Y_90))
								.register(Direction.NORTH, BlockHalf.BOTTOM, straightModel.apply(ROTATE_Y_270))
								.register(Direction.EAST, BlockHalf.TOP, straightModel.apply(ROTATE_X_180))
								.register(Direction.WEST, BlockHalf.TOP, straightModel.apply(ROTATE_X_180).apply(ROTATE_Y_180))
								.register(Direction.SOUTH, BlockHalf.TOP, straightModel.apply(ROTATE_X_180).apply(ROTATE_Y_90))
								.register(Direction.NORTH, BlockHalf.TOP, straightModel.apply(ROTATE_X_180).apply(ROTATE_Y_270))
				);
	}

	public static BlockModelDefinitionCreator createHHBlockState(Block stairsBlock, Identifier modelId) {
		WeightedVariant straightModel = createWeightedVariant(modelId).apply(ROTATE_X_90);
		return VariantsBlockModelDefinitionCreator.of(stairsBlock)
				.with(
						BlockStateVariantMap.models(Properties.HORIZONTAL_FACING)
								.register(Direction.EAST, straightModel)
								.register(Direction.WEST, straightModel.apply(ROTATE_Y_180))
								.register(Direction.SOUTH, straightModel.apply(ROTATE_Y_90))
								.register(Direction.NORTH, straightModel.apply(ROTATE_Y_270))
				);
	}

	public static void registerOrientableWithItem(BlockStateModelGenerator generator, Block orientable) {
		Identifier modelId = ModelIds.getBlockModelId(orientable);
		WeightedVariant weightedVariant = createWeightedVariant(modelId);
		generator.blockStateCollector
				.accept(
						VariantsBlockModelDefinitionCreator.of(orientable, weightedVariant)
								./*? =1.21.8 {*/ coordinate /*?} else {*/ /*apply *//*?}*/(NORTH_DEFAULT_ROTATION_OPERATIONS)
				);
		generator.registerItemModel(orientable.asItem(), modelId);
	}

	public static void registerOrientableRedstone(BlockStateModelGenerator generator, Block orientable, Identifier modelId) {
		WeightedVariant weightedVariant = createWeightedVariant(modelId);
		WeightedVariant weightedVariant2 = createWeightedVariant(ModelIds.getBlockSubModelId(orientable, "_on"));
		generator.blockStateCollector
				.accept(
						VariantsBlockModelDefinitionCreator.of(orientable)
								.with(createBooleanModelMap(Properties.POWERED, weightedVariant2, weightedVariant))
								./*? =1.21.8 {*/ coordinate /*?} else {*/ /*apply *//*?}*/(NORTH_DEFAULT_ROTATION_OPERATIONS)
				);
	}

	public static void registerRedstone(BlockStateModelGenerator generator, Block block) {
		Identifier modelId = ModelIds.getBlockModelId(block);
		WeightedVariant weightedVariant = createWeightedVariant(modelId);
		WeightedVariant weightedVariant2 = createWeightedVariant(ModelIds.getBlockSubModelId(block, "_on"));
		generator.blockStateCollector
				.accept(VariantsBlockModelDefinitionCreator.of(block).with(createBooleanModelMap(Properties.POWERED, weightedVariant2, weightedVariant)));
		generator.registerItemModel(block.asItem(), modelId);
	}
}
