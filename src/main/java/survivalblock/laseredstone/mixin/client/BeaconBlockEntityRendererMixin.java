package survivalblock.laseredstone.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.BeamEmitter;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
/*? >=1.21.9 {*/ /*import net.minecraft.client.render.block.entity.state.BeaconBlockEntityRenderState; *//*?}*/
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
/*? >=1.21.9 {*/ /*import survivalblock.laseredstone.client.render.state.LaserBlockEntityRenderState; *//*?}*/
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;

@Mixin(BeaconBlockEntityRenderer.class)
public class BeaconBlockEntityRendererMixin {

    //? if =1.21.8 {
    @ModifyExpressionValue(method = "render", at = @At(value = "CONSTANT", args = "intValue=2048"))
    private int useHeightInstead(int original, @Local(argsOnly = true)BlockEntity blockEntity, @Local BeamEmitter.BeamSegment beamSegment) {
        return blockEntity instanceof LaserBlockEntity ? beamSegment.getHeight() : original;
    }
    //?} else {
    /*@ModifyExpressionValue(method = "render(Lnet/minecraft/client/render/block/entity/state/BeaconBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At(value = "CONSTANT", args = "intValue=2048"))
    private int useHeightInstead(int original, @Local(argsOnly = true) BeaconBlockEntityRenderState state, @Local BeaconBlockEntityRenderState.BeamSegment beamSegment) {
        return state instanceof LaserBlockEntityRenderState ? beamSegment.height() : original;
    }
     *///?}
}
