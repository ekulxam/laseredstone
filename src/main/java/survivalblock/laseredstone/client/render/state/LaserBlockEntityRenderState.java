//? if >=1.21.9 {
package survivalblock.laseredstone.client.render.state;

import net.minecraft.client.renderer.blockentity.state.BeaconRenderState;
import net.minecraft.core.Direction;

import java.util.List;
import java.util.Map;

public class LaserBlockEntityRenderState extends BeaconRenderState {

    public Map<Direction, Integer> directionToDistanceMap;
    public Map<Direction, List<Section>> directionToSegmentsMap;
    public boolean overcharged = false;
    public int color;
}
//?}