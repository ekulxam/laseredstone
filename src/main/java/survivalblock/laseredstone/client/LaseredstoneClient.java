package survivalblock.laseredstone.client;

import net.fabricmc.api.ClientModInitializer;
//? <26
//import net.fabricmc.fabric.api.client.rendering.v1.ChunkSectionLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockTintSource;
//~ if >=26 'net.minecraft.world.level.BlockAndTintGetter' -> 'net.minecraft.client.renderer.block.BlockAndTintGetter'
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import survivalblock.laseredstone.client.render.LaserBlockEntityRenderer;
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;
import survivalblock.laseredstone.common.init.LaseredstoneBlocks;

import java.util.List;

public class LaseredstoneClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //? <26 {
        /*ChunkSectionLayerMap.putBlock(LaseredstoneBlocks.HORIZONTAL_VERTICAL_MIRROR, ChunkSectionLayer.CUTOUT);
        ChunkSectionLayerMap.putBlock(LaseredstoneBlocks.HORIZONTAL_HORIZONTAL_MIRROR, ChunkSectionLayer.CUTOUT);
        ChunkSectionLayerMap.putBlock(LaseredstoneBlocks.LENS, ChunkSectionLayer.CUTOUT);
        ChunkSectionLayer mipmapped = ChunkSectionLayer./^? <1.21.11 {^/ /^CUTOUT_MIPPED ^//^?} else {^/ CUTOUT /^?}^/;
        ChunkSectionLayerMap.putBlock(LaseredstoneBlocks.LASER, mipmapped);
        ChunkSectionLayerMap.putBlock(LaseredstoneBlocks.RECEIVER, mipmapped);
        ChunkSectionLayerMap.putBlock(LaseredstoneBlocks.DIFFUSER, mipmapped);
        *///?}

        registerBlockColors(LaserBlockEntity.DEFAULT_COLOR, ((state, world, pos) -> {
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

    private static void registerBlockColors(int defaultColor, BlockColorProvider provider, Block... blocks) {
        //? if <26 {
        /*BlockColorRegistry.BLOCK.register((state, world, pos, index) -> provider.color(state, world, pos), blocks);
        *///?} else {
        BlockColorRegistry.register(List.of(new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return defaultColor;
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter world, BlockPos pos) {
                return provider.color(state, world, pos);
            }
        }));
        //?}
    }

    @FunctionalInterface
    public interface BlockColorProvider {
        int color(BlockState state, BlockAndTintGetter world, BlockPos pos);
    }
}
