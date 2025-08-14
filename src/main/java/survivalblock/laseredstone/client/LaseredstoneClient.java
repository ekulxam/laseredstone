package survivalblock.laseredstone.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import survivalblock.laseredstone.client.render.LaserBlockEntityRenderer;
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;
import survivalblock.laseredstone.common.init.LaseredstoneBlocks;

public class LaseredstoneClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.putBlock(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(LaseredstoneBlocks.LENS, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(LaseredstoneBlocks.LASER, BlockRenderLayer.CUTOUT_MIPPED);
        BlockRenderLayerMap.putBlock(LaseredstoneBlocks.RECEIVER, BlockRenderLayer.CUTOUT_MIPPED);

        ColorProviderRegistry.BLOCK.register(((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return LaserBlockEntity.DEFAULT_COLOR;
            }
            return world.getBlockEntityRenderData(pos) instanceof Integer integer ?
                    integer :
                    LaserBlockEntity.DEFAULT_COLOR;
        }), LaseredstoneBlocks.LASER);

        BlockEntityRendererFactories.register(LaseredstoneBlockEntityTypes.LASER, LaserBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(LaseredstoneBlockEntityTypes.HORIZONTAL_VERTICAL_MIRROR, LaserBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(LaseredstoneBlockEntityTypes.HORIZONTAL_HORIZONTAL_MIRROR, LaserBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(LaseredstoneBlockEntityTypes.LENS, LaserBlockEntityRenderer::new);
    }
}
