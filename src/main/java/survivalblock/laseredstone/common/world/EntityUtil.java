package survivalblock.laseredstone.common.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;
import survivalblock.laseredstone.common.init.LaseredstoneDamageTypes;
import survivalblock.laseredstone.common.init.LaseredstoneTags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Ampflower
 **/
public final class EntityUtil {

	/**
	 * Maintain a list of boxes that are currently actively damaging entities.
	 *
	 * @implNote ThreadLocal in use for DimThread support.
	 */
	private static final ThreadLocal<Set<Box>> damagingBoxes = ThreadLocal.withInitial(HashSet::new);

	public static void submitDamagingBox(final Box box) {
		damagingBoxes.get().add(box);
	}

	public static void damageTick(final ServerWorld world) {
		final Set<Box> boxes = damagingBoxes.get();
		if (boxes.isEmpty()) {
			return;
		}

		final Collection<Entity> entities = getVulnerableCandidates(world);
		if (entities.isEmpty()) {
			return;
		}

		DamageSource source = new DamageSource(LaseredstoneDamageTypes.getFromWorld(world, LaseredstoneDamageTypes.LASER));

		final Box[] boxArray = boxes.toArray(Box[]::new);

		final Box filter = encompassing(boxes);

		for (final Entity entity : entities) {
			final Box bounding = entity.getBoundingBox();
			// Low-pass filter.
			if (!bounding.intersects(filter)) {
				continue;
			}

			for (final Box box : boxArray) {
				if (!bounding.intersects(box)) {
					continue;
				}
				entity.damage(world, source, LaserBlockEntity.getDamage(entity));
				break;
			}
		}

		boxes.clear();
	}

	private static Box encompassing(final Iterable<Box> boxes) {
		final Iterator<Box> itr = boxes.iterator();
		final Box first = itr.next();

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
			final Box box = itr.next();
			minX = Math.min(minX, box.minX);
			minY = Math.min(minY, box.minY);
			minZ = Math.min(minZ, box.minZ);
			maxX = Math.max(maxX, box.maxX);
			maxY = Math.max(maxY, box.maxY);
			maxZ = Math.max(maxZ, box.maxZ);
		}

		return new Box(
				minX,
				minY,
				minZ,
				maxX,
				maxY,
				maxZ
		);
	}

	private static Collection<Entity> getVulnerableCandidates(final ServerWorld world) {
		final ArrayList<Entity> list = new ArrayList<>();

		for (final Entity entity : world.iterateEntities()) {
			if (EntityUtil.isVulnerableCandidate(entity)) {
				list.add(entity);
			}
		}

		return list;
	}

	public static boolean isVulnerableCandidate(final Entity entity) {
		return entity != null
				&& !entity.isInvulnerable()
				&& !entity.getType().isIn(LaseredstoneTags.LASER_PROOF)
				// && !invulnerableEntities.contains(entity.getType())
				&& entity.isAlive();
	}
}
