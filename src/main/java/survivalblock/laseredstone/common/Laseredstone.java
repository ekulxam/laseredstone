package survivalblock.laseredstone.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import survivalblock.laseredstone.common.init.LaseredstoneBlockEntityTypes;
import survivalblock.laseredstone.common.init.LaseredstoneBlocks;
import survivalblock.laseredstone.common.init.LaseredstoneItems;
import survivalblock.laseredstone.common.world.EntityUtil;

public class Laseredstone implements ModInitializer {
	public static final String MOD_ID = "laseredstone";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LaseredstoneBlocks.init();
		LaseredstoneItems.init();
		LaseredstoneBlockEntityTypes.init();

		ServerTickEvents.END_WORLD_TICK.register(EntityUtil::damageTick);
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}