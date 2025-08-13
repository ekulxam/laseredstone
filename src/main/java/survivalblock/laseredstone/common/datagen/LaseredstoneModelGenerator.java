package survivalblock.laseredstone.common.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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
import survivalblock.laseredstone.common.block.LaserBlock;
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;
import survivalblock.laseredstone.common.init.LaseredstoneBlocks;
import survivalblock.laseredstone.common.init.LaseredstoneItems;

import static net.minecraft.client.data.BlockStateModelGenerator.NO_OP;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_X_180;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_X_270;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_X_90;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_Y_180;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_Y_270;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_Y_90;

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
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
	}

	public static BlockModelDefinitionCreator createHVBlockState(Block stairsBlock, Identifier modelId) {
		WeightedVariant straightModel = BlockStateModelGenerator.createWeightedVariant(modelId);
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
		WeightedVariant straightModel = BlockStateModelGenerator.createWeightedVariant(modelId).apply(ROTATE_X_90);
		return VariantsBlockModelDefinitionCreator.of(stairsBlock)
				.with(
						BlockStateVariantMap.models(Properties.HORIZONTAL_FACING)
								.register(Direction.EAST, straightModel)
								.register(Direction.WEST, straightModel.apply(ROTATE_Y_180))
								.register(Direction.SOUTH, straightModel.apply(ROTATE_Y_90))
								.register(Direction.NORTH, straightModel.apply(ROTATE_Y_270))
				);
	}

	public static void registerOrientableRedstone(BlockStateModelGenerator generator, Block orientable, Identifier modelId) {
		WeightedVariant weightedVariant = BlockStateModelGenerator.createWeightedVariant(modelId);
		WeightedVariant weightedVariant2 = BlockStateModelGenerator.createWeightedVariant(ModelIds.getBlockSubModelId(orientable, "_on"));
		generator.blockStateCollector
				.accept(
						VariantsBlockModelDefinitionCreator.of(Blocks.OBSERVER)
								.with(BlockStateModelGenerator.createBooleanModelMap(Properties.POWERED, weightedVariant2, weightedVariant))
								.coordinate(NORTH_DEFAULT_ROTATION_OPERATIONS)
				);
	}
}
