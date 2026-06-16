package survivalblock.laseredstone.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import survivalblock.laseredstone.client.render.LaserBlockEntityRenderer;
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;
import survivalblock.laseredstone.common.init.LaseredstoneBlocks;

public class LaseredstoneClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.putBlock(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(LaseredstoneBlocks.LENS, ChunkSectionLayer.CUTOUT);
        ChunkSectionLayer mipmapped = ChunkSectionLayer./*? <1.21.11 {*/ CUTOUT_MIPPED /*?} else {*/ /*CUTOUT *//*?}*/;
        BlockRenderLayerMap.putBlock(LaseredstoneBlocks.LASER, mipmapped);
        BlockRenderLayerMap.putBlock(LaseredstoneBlocks.RECEIVER, mipmapped);
        BlockRenderLayerMap.putBlock(LaseredstoneBlocks.DIFFUSER, mipmapped);

        ColorProviderRegistry.BLOCK.register(((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return LaserBlockEntity.DEFAULT_COLOR;
            }
            return world.getBlockEntityRenderData(pos) instanceof Integer integer ?
                    integer :
                    LaserBlockEntity.DEFAULT_COLOR;
        }), LaseredstoneBlocks.LASER, LaseredstoneBlocks.DIFFUSER);

        BlockEntityRenderers.register(LaseredstoneBlockEntityTypes.LASER, LaserBlockEntityRenderer::new);
        BlockEntityRenderers.register(LaseredstoneBlockEntityTypes.HORIZONTAL_VERTICAL_MIRROR, LaserBlockEntityRenderer::new);
        BlockEntityRenderers.register(LaseredstoneBlockEntityTypes.HORIZONTAL_HORIZONTAL_MIRROR, LaserBlockEntityRenderer::new);
        BlockEntityRenderers.register(LaseredstoneBlockEntityTypes.LENS, LaserBlockEntityRenderer::new);
        BlockEntityRenderers.register(LaseredstoneBlockEntityTypes.DIFFUSER, LaserBlockEntityRenderer::new);
    }
}
