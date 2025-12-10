package survivalblock.laseredstone.client.render;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.entity.BeamEmitter;
import net.minecraft.client.MinecraftClient;
/*? >=1.21.9 {*/ import net.minecraft.client.gui.hud.debug.DebugHudEntries; /*?}*/
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
//? if >=1.21.9 {
import net.minecraft.client.render.block.entity.state.BeaconBlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
//?}
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.function.Consumers;
import org.jetbrains.annotations.Nullable;
/*? >=1.21.9 {*/ import survivalblock.laseredstone.client.render.state.LaserBlockEntityRenderState; /*?}*/
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    /*? >=1.21.9 {*/ @SuppressWarnings("unused") /*?}*/
    public LaserBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(/*? =1.21.8 {*/ /*ctx *//*?}*/);
    }

    //? if >=1.21.9 {
    @Override
    public BeaconBlockEntityRenderState createRenderState() {
        return new LaserBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(T blockEntity, BeaconBlockEntityRenderState beaconBlockEntityRenderState, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand) {
        super.updateRenderState(blockEntity, beaconBlockEntityRenderState, tickProgress, cameraPos, crumblingOverlayCommand);
        if (beaconBlockEntityRenderState instanceof LaserBlockEntityRenderState laserState) {
            updateLaserRenderState(blockEntity, laserState, tickProgress, cameraPos);
        }
    }

    @SuppressWarnings("unused")
    public static <T extends LaserBlockEntity> void updateLaserRenderState(T laser, LaserBlockEntityRenderState state, float tickProgress, Vec3d cameraPos) {
        state.directionToDistanceMap = laser.getDirectionToDistanceMap();
        state.directionToSegmentsMap = state.directionToDistanceMap
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> laser.getBeamSegments(entry.getKey(), entry.getValue())
                                        .stream()
                                        .map(
                                                beamSegment -> new BeaconBlockEntityRenderState.BeamSegment(
                                                        beamSegment.getColor(),
                                                        beamSegment.getHeight()
                                                )
                                        ).toList()
                        )
                );
        state.overcharged = laser.isOvercharged();
        state.color = laser.getColor();
    }
    //?}

    @Override
    public void render(/*? =1.21.8 {*/ /*T laser, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos *//*?} else {*/
    BeaconBlockEntityRenderState blockEntityRenderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState /*?}*/) {
        //? if >=1.21.9 {
        if (!(blockEntityRenderState instanceof LaserBlockEntityRenderState state)) {
            return;
        }
        //?}
        /*? =1.21.8 {*/ /*Map<Direction, Integer> directionToDistanceMap = laser.getDirectionToDistanceMap(); *//*?}*/
        if (/*? >=1.21.9 {*/ state. /*?}*/directionToDistanceMap.isEmpty()) {
            return;
        }

        //? if =1.21.8 {
        /*Map<Direction, List<BeamEmitter.BeamSegment>> directionToSegmentsMap = directionToDistanceMap
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> laser.getBeamSegments(entry.getKey(), entry.getValue())
                        )
                );
        *///?}

        for (Map.Entry<Direction, Integer> entry : /*? >=1.21.9 {*/ state. /*?}*/directionToDistanceMap.entrySet()) {
            Direction output = entry.getKey();
            matrices.push();
            matrices.translate(0.5, 0.5, 0.5);
            DIRECTION_TRANSFORMS.get(output).accept(matrices);
            matrices.translate(-0.5, -0.5, -0.5);
            //? if =1.21.8 {
            /*laser.setRenderingSegments(directionToSegmentsMap.get(output));
            *///?} else {
            state.beamSegments = state.directionToSegmentsMap.get(output);
             //?}
            super.render(/*? =1.21.8 {*/ /*laser, tickProgress, matrices, vertexConsumers, light, overlay, cameraPos *//*?} else {*/ state, matrices, queue, cameraState /*?}*/);
            /*? =1.21.8 {*/ /*laser.setRenderingSegments(null); *//*?}*/
            matrices.pop();

            if (/*? =1.21.8 {*/ /*laser.isOvercharged() *//*?} else {*/ state.overcharged /*?}*/ && MinecraftClient.getInstance()./*? =1.21.8 {*/ /*getEntityRenderDispatcher().shouldRenderHitboxes() *//*?} else {*/ debugHudEntryList.isEntryVisible(DebugHudEntries.ENTITY_HITBOXES) /*?}*/) {
                BlockPos blockPos = /*? =1.21.8 {*/ /*laser.getPos() *//*?} else {*/ state.pos /*?}*/;
                Vec3d center = blockPos.toCenterPos();
                Box box = expandInOneDirection(new Box(center.subtract(0.125), center.add(0.125)), Vec3d.of(output.getVector()).multiply(entry.getValue() + 0.375));
                matrices.push();
                matrices.translate(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
                int color = /*? =1.21.8 {*/ /*laser.getColor() *//*?} else {*/ state.color /*?}*/;
                float red = ColorHelper.getRedFloat(color);
                float green = ColorHelper.getGreenFloat(color);
                float blue = ColorHelper.getBlueFloat(color);
                //? if =1.21.8 {
                /*VertexRendering.drawBox(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), box, red, green, blue, 1);
                 *///?} else {
                queue.submitCustom(matrices, RenderLayer.LINES, (matricesEntry, vertexConsumer) -> VertexRendering.drawBox(matricesEntry, vertexConsumer, box, red, green, blue, 1));
                //?}
                matrices.pop();
            }
        }
    }
}
