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
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import survivalblock.laseredstone.common.init.LaseredstoneBlocks;
import survivalblock.laseredstone.common.init.LaseredstoneItems;

import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_X_180;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_Y_180;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_Y_270;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_Y_90;

public class LaseredstoneModelGenerator extends FabricModelProvider {

	public LaseredstoneModelGenerator(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		var model = ModelIds.getBlockModelId(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR);
		blockStateModelGenerator.blockStateCollector.accept(createStairsBlockState(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR, model));
		blockStateModelGenerator.registerItemModel(LaseredstoneItems.HORIZONTAL_VERTICAL_MIRROR, model);
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
	}

	public static BlockModelDefinitionCreator createStairsBlockState(Block stairsBlock, Identifier modelId) {
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
}
