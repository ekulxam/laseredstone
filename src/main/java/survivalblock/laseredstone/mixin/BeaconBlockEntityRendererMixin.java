package survivalblock.laseredstone.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.BeamEmitter;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;

@Mixin(BeaconBlockEntityRenderer.class)
public class BeaconBlockEntityRendererMixin {

    @ModifyExpressionValue(method = "render", at = @At(value = "CONSTANT", args = "intValue=2048"))
    private int useHeightInstead(int original, @Local(argsOnly = true)BlockEntity blockEntity, @Local BeamEmitter.BeamSegment beamSegment) {
        return blockEntity instanceof LaserBlockEntity ? beamSegment.getHeight() : original;
    }
}
