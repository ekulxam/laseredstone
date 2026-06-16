package survivalblock.laseredstone.mixin;

import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BeaconBeamOwner.class)
public interface BeamEmitterMixin {

	@Mixin(BeaconBeamOwner.Section.class)
	interface BeamSegmentMixin {
		@Accessor("height")
		void laseredstone$setHeight(int height);
	}
}