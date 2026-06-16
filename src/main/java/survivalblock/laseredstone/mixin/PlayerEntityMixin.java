package survivalblock.laseredstone.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.laseredstone.common.init.LaseredstoneTags;

@Mixin(Player.class)
public abstract class PlayerEntityMixin {

    @Shadow public abstract boolean isCreative();

    @ModifyExpressionValue(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean bypassesCreativeMode(boolean original, ServerLevel serverWorld, DamageSource source, float amount) {
        return original ||
                (source.is(LaseredstoneTags.BYPASSES_CREATIVE_MODE) &&
                        this.isCreative());
    }
}
