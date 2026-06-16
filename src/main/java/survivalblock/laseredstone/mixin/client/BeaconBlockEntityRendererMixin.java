package survivalblock.laseredstone.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
/*? if > 1.21.8 {*/
import net.minecraft.client.renderer.blockentity.state.BeaconRenderState;
import survivalblock.laseredstone.client.render.state.LaserBlockEntityRenderState;
/*? }*/
import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;

@Mixin(BeaconRenderer.class)
public class BeaconBlockEntityRendererMixin {

    //? if =1.21.8 {
    /*@ModifyExpressionValue(method = "render", at = @At(value = "CONSTANT", args = "intValue=2048"))
    private int useHeightInstead(int original, @Local(argsOnly = true) BlockEntity blockEntity, @Local BeaconBeamOwner.Section beamSegment) {
        return blockEntity instanceof LaserBlockEntity ? beamSegment.getHeight() : original;
    }
    *///?} else {
    @ModifyExpressionValue(method = "submit(Lnet/minecraft/client/renderer/blockentity/state/BeaconRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At(value = "CONSTANT", args = "intValue=2048"))
    private int useHeightInstead(int original, @Local(argsOnly = true) BeaconRenderState state, @Local BeaconRenderState.Section beamSegment) {
        return state instanceof LaserBlockEntityRenderState ? beamSegment.height() : original;
    }
     //?}
}
