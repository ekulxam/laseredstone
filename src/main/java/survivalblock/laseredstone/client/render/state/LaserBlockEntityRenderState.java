//? if >=1.21.9 {
package survivalblock.laseredstone.client.render.state;

import net.minecraft.client.render.block.entity.state.BeaconBlockEntityRenderState;
import net.minecraft.util.math.Direction;

public class LaserBlockEntityRenderState extends BeaconBlockEntityRenderState {

    public Direction currentOutputDirection;
    public boolean overcharged = false;
    public int distance;
    public int color;
}
//?}