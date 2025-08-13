package survivalblock.laseredstone.mixin;

import net.minecraft.block.entity.BeamEmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BeamEmitter.class)
public interface BeamEmitterMixin {

	@Mixin(BeamEmitter.BeamSegment.class)
	interface BeamSegmentMixin {
		@Accessor("height")
		void laseredstone$setHeight(int height);
	}
}