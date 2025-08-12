package survivalblock.laseredstone.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;
import survivalblock.laseredstone.common.init.LaseredstoneBlocks;

public class LaseredstoneClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.putBlock(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR, BlockRenderLayer.TRANSLUCENT);
    }
}
