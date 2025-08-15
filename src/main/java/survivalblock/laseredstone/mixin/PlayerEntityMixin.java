package survivalblock.laseredstone.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.laseredstone.common.init.LaseredstoneTags;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Shadow public abstract boolean isCreative();

    @ModifyExpressionValue(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean bypassesCreativeMode(boolean original, ServerWorld serverWorld, DamageSource source, float amount) {
        return original ||
                (source.isIn(LaseredstoneTags.BYPASSES_CREATIVE_MODE) &&
                        this.isCreative());
    }
}
