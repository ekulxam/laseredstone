package survivalblock.laseredstone.client;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.function.Consumers;
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;

import java.util.Map;
import java.util.function.Consumer;

public class LaserBlockEntityRenderer<T extends LaserBlockEntity> extends BeaconBlockEntityRenderer<T> {

    protected static final Map<Direction, Consumer<MatrixStack>> DIRECTION_TRANSFORMS = Util.make(ImmutableMap.<Direction, Consumer<MatrixStack>>builder(), builder -> {
        builder.put(Direction.UP, Consumers.nop());
        builder.put(Direction.DOWN, matrixStack -> matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180)));
        builder.put(Direction.NORTH, matrixStack -> matrixStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90)));
        builder.put(Direction.SOUTH, matrixStack -> matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90)));
        builder.put(Direction.EAST, matrixStack -> matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(90)));
        builder.put(Direction.WEST, matrixStack -> matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90)));
    }).build();

    public LaserBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(T laser, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        Direction output = laser.getCurrentOutputDirection();
        if (output == null) {
            return;
        }
        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        DIRECTION_TRANSFORMS.get(output).accept(matrices);
        matrices.translate(-0.5, -0.5, -0.5);
        super.render(laser, tickProgress, matrices, vertexConsumers, light, overlay, cameraPos);
        matrices.pop();
    }
}
