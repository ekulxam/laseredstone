package survivalblock.laseredstone.client.render;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.function.Consumers;
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;

import java.util.Map;
import java.util.function.Consumer;

import static survivalblock.laseredstone.common.block.entity.LaserBlockEntity.expandInOneDirection;

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
        if (laser.isOvercharged() && MinecraftClient.getInstance().getEntityRenderDispatcher().shouldRenderHitboxes()) {
            BlockPos blockPos = laser.getPos();
            Vec3d center = blockPos.toCenterPos();
            Box box = expandInOneDirection(new Box(center.subtract(0.125), center.add(0.125)), Vec3d.of(output.getVector()).multiply(laser.getDistance() + 0.375));
            matrices.push();
            matrices.translate(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
            int color = laser.getColor();
            float red = ColorHelper.getRedFloat(color);
            float green = ColorHelper.getGreenFloat(color);
            float blue = ColorHelper.getBlueFloat(color);
            VertexRendering.drawBox(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), box, red, green, blue, 1);
            matrices.pop();
        }
    }
}
