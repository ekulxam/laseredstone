package survivalblock.laseredstone.common.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;
import survivalblock.laseredstone.common.init.LaseredstoneDamageTypes;
import survivalblock.laseredstone.common.init.LaseredstoneGameRules;
import survivalblock.laseredstone.common.init.LaseredstoneTags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Ampflower
 **/
public final class DelayedDamager {

	private DelayedDamager() {
	}

	/**
	 * Maintain a list of boxes that are currently actively damaging entities.
	 *
	 * @implNote ThreadLocal in use for DimThread support.
	 */
	private static final ThreadLocal<Set<AABB>> damagingBoxes = ThreadLocal.withInitial(HashSet::new);

	public static void submitDamagingBox(final AABB box) {
		damagingBoxes.get().add(box);
	}

	public static void damageTick(final ServerLevel world) {
		final Set<AABB> boxes = damagingBoxes.get();
		if (boxes.isEmpty()) {
			return;
		}

		final Collection<Entity> entities = getVulnerableCandidates(world);
		if (entities.isEmpty()) {
			return;
		}

		DamageSource source = new DamageSource(LaseredstoneDamageTypes.getFromWorld(world, LaseredstoneDamageTypes.LASER));

		final AABB[] boxArray = boxes.toArray(AABB[]::new);

		final AABB filter = encompassing(boxes);
		final boolean multi = world.getGameRules()./*? <1.21.11 {*/ getBoolean /*?} else {*/ /*get *//*?}*/(LaseredstoneGameRules.MULTI_DAMAGE);

		for (final Entity entity : entities) {
			final AABB bounding = entity.getBoundingBox();
			// Low-pass filter.
			if (!bounding.intersects(filter)) {
				continue;
			}

			for (final AABB box : boxArray) {
				if (!bounding.intersects(box)) {
					continue;
				}
				entity.hurtServer(world, source, LaserBlockEntity.getDamage(entity));
				if (!multi) {
					break;
				}
			}
		}

		boxes.clear();
	}

	private static AABB encompassing(final Iterable<AABB> boxes) {
		final Iterator<AABB> itr = boxes.iterator();
		final AABB first = itr.next();

		if (!itr.hasNext()) {
			return first;
		}

		double minX = first.minX;
		double minY = first.minY;
		double minZ = first.minZ;
		double maxX = first.maxX;
		double maxY = first.maxY;
		double maxZ = first.maxZ;

		while (itr.hasNext()) {
			final AABB box = itr.next();
			minX = Math.min(minX, box.minX);
			minY = Math.min(minY, box.minY);
			minZ = Math.min(minZ, box.minZ);
			maxX = Math.max(maxX, box.maxX);
			maxY = Math.max(maxY, box.maxY);
			maxZ = Math.max(maxZ, box.maxZ);
		}

		return new AABB(
				minX,
				minY,
				minZ,
				maxX,
				maxY,
				maxZ
		);
	}

	private static Collection<Entity> getVulnerableCandidates(final ServerLevel world) {
		final ArrayList<Entity> list = new ArrayList<>();

		for (final Entity entity : world.getAllEntities()) {
			if (DelayedDamager.isVulnerableCandidate(entity)) {
				list.add(entity);
			}
		}

		return list;
	}

	public static boolean isVulnerableCandidate(final Entity entity) {
		return entity != null
				&& !entity.isInvulnerable()
				&& !entity/*? <26 {*/ .getType() /*?}*/.is(LaseredstoneTags.LASER_PROOF)
				&& entity.isAlive();
	}
}
