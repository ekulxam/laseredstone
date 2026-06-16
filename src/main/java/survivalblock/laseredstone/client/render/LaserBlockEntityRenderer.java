package survivalblock.laseredstone.client.render;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
//? if >=1.21.9 {
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import net.minecraft.client.gui.components.debug.DebugScreenEntries;
//import net.minecraft.client.renderer.SubmitNodeCollector;
//import net.minecraft.client.renderer.blockentity.state.BeaconRenderState;
//import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
//import net.minecraft.client.renderer.rendertype.RenderTypes;
//import net.minecraft.client.renderer.state.CameraRenderState;
//import net.minecraft.util.Util;
//import org.jetbrains.annotations.Nullable;
//? }
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.function.Consumers;
/*? >=1.21.9 {*/ /*import survivalblock.laseredstone.client.render.state.LaserBlockEntityRenderState; *//*?}*/
import survivalblock.laseredstone.common.block.entity.LaserBlockEntity;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static survivalblock.laseredstone.common.block.entity.LaserBlockEntity.expandInOneDirection;

public class LaserBlockEntityRenderer<T extends LaserBlockEntity> extends BeaconRenderer<T> {

    protected static final Map<Direction, Consumer<PoseStack>> DIRECTION_TRANSFORMS = Util.make(ImmutableMap.<Direction, Consumer<PoseStack>>builder(), builder -> {
        builder.put(Direction.UP, Consumers.nop());
        builder.put(Direction.DOWN, matrixStack -> matrixStack.mulPose(Axis.XP.rotationDegrees(180)));
        builder.put(Direction.NORTH, matrixStack -> matrixStack.mulPose(Axis.XN.rotationDegrees(90)));
        builder.put(Direction.SOUTH, matrixStack -> matrixStack.mulPose(Axis.XP.rotationDegrees(90)));
        builder.put(Direction.EAST, matrixStack -> matrixStack.mulPose(Axis.ZN.rotationDegrees(90)));
        builder.put(Direction.WEST, matrixStack -> matrixStack.mulPose(Axis.ZP.rotationDegrees(90)));
    }).build();

    /*? >=1.21.9 {*/ /*@SuppressWarnings("unused") *//*?}*/
    public LaserBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        super(/*? =1.21.8 {*/ ctx /*?}*/);
    }

    //? if >=1.21.9 {
    /*@Override
    public BeaconRenderState createRenderState() {
        return new LaserBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, BeaconRenderState beaconBlockEntityRenderState, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlayCommand) {
        super.extractRenderState(blockEntity, beaconBlockEntityRenderState, tickProgress, cameraPos, crumblingOverlayCommand);
        if (beaconBlockEntityRenderState instanceof LaserBlockEntityRenderState laserState) {
            updateLaserRenderState(blockEntity, laserState, tickProgress, cameraPos);
        }
    }

    @SuppressWarnings("unused")
    public static <T extends LaserBlockEntity> void updateLaserRenderState(T laser, LaserBlockEntityRenderState state, float tickProgress, Vec3 cameraPos) {
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
                                                beamSegment -> new BeaconRenderState.Section(
                                                        beamSegment.getColor(),
                                                        beamSegment.getHeight()
                                                )
                                        ).toList()
                        )
                );
        state.overcharged = laser.isOvercharged();
        state.color = laser.getColor();
    }
    *///?}

    @Override
    public void /*? if >=1.21.9 { */ /*submit*/ /*?} else {*/ render /*?}*/(/*? =1.21.8 {*/ T laser, float tickProgress, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, Vec3 cameraPos /*?} else {*/
    /*BeaconRenderState blockEntityRenderState, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState *//*?}*/) {
        //? if >=1.21.9 {
        /*if (!(blockEntityRenderState instanceof LaserBlockEntityRenderState state)) {
            return;
        }
        *///?}
        /*? =1.21.8 {*/ Map<Direction, Integer> directionToDistanceMap = laser.getDirectionToDistanceMap(); /*?}*/
        if (/*? >=1.21.9 {*/ /*state. *//*?}*/directionToDistanceMap.isEmpty()) {
            return;
        }

        //? if =1.21.8 {
        Map<Direction, List<BeaconBeamOwner.Section>> directionToSegmentsMap = directionToDistanceMap
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> laser.getBeamSegments(entry.getKey(), entry.getValue())
                        )
                );
        //?}

        for (Map.Entry<Direction, Integer> entry : /*? >=1.21.9 {*/ /*state. *//*?}*/directionToDistanceMap.entrySet()) {
            Direction output = entry.getKey();
            matrices.pushPose();
            matrices.translate(0.5, 0.5, 0.5);
            DIRECTION_TRANSFORMS.get(output).accept(matrices);
            matrices.translate(-0.5, -0.5, -0.5);
            //? if =1.21.8 {
            laser.setRenderingSegments(directionToSegmentsMap.get(output));
            //?} else {
            /*state.sections = state.directionToSegmentsMap.get(output);
             *///?}
            super./* ? if > 1.21.8 {*?*/ /*submit*/ /*?} else {*/ render /*?}*/(/*? =1.21.8 {*/ laser, tickProgress, matrices, vertexConsumers, light, overlay, cameraPos /*?} else {*/ /*state, matrices, queue, cameraState *//*?}*/);
            /*? =1.21.8 {*/ laser.setRenderingSegments(null); /*?}*/
            matrices.popPose();

            if (/*? =1.21.8 {*/ laser.isOvercharged() /*?} else {*/ /*state.overcharged *//*?}*/ && Minecraft.getInstance()./*? =1.21.8 {*/ getEntityRenderDispatcher().shouldRenderHitBoxes() /*?} else {*/ /*debugEntries.isCurrentlyEnabled(DebugScreenEntries.ENTITY_HITBOXES) *//*?}*/) {
                BlockPos blockPos = /*? =1.21.8 {*/ laser.getBlockPos() /*?} else {*/ /*state.blockPos *//*?}*/;
                Vec3 center = blockPos.getCenter();
                AABB box = expandInOneDirection(new AABB(center.subtract(0.125), center.add(0.125)), Vec3.atLowerCornerOf(output.getUnitVec3i()).scale(entry.getValue() + 0.375));
                matrices.pushPose();
                matrices.translate(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
                int color = /*? =1.21.8 {*/ laser.getColor() /*?} else {*/ /*state.color *//*?}*/;
                float red = ARGB.red(color);
                float green = ARGB.green(color);
                float blue = ARGB.blue(color);
                //? if =1.21.8 {
                ShapeRenderer.renderLineBox(matrices, vertexConsumers.getBuffer(RenderType.LINES), box, red, green, blue, 1);
                 //?} else {
                /*queue.submitCustomGeometry(matrices, /^? <1.21.11 {^/ RenderLayer /^?} else {^/ /^RenderTypes ^//^?}^/.LINES, (matricesEntry, vertexConsumer) -> drawBox(matricesEntry, vertexConsumer, box, red, green, blue, 1));
                *///?}
                matrices.popPose();
            }
        }
    }

    //? if >1.21.8 {
    /*public void drawBox(PoseStack.Pose entry, VertexConsumer vertexConsumers, AABB box, float red, float green, float blue, float alpha) {
        //? if <1.21.11
        VertexRendering.drawBox(entry, vertexConsumers, box, red, green, blue, 1);
        //? if >=1.21.11 {
        /^float width = Math.max(2.5F, Minecraft.getInstance().getWindow().getWidth() / 1920.0F * 2.5F);
        float f = (float) box.minX;
        float g = (float) box.minY;
        float h = (float) box.minZ;
        float i = (float) box.maxX;
        float j = (float) box.maxY;
        float k = (float) box.maxZ;
        vertexConsumers.addVertex(entry, f, g, h).setColor(red, green, blue, alpha).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, i, g, h).setColor(red, green, blue, alpha).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, f, g, h).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, f, j, h).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, f, g, h).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, f, g, k).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, i, g, h).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, i, j, h).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, i, j, h).setColor(red, green, blue, alpha).setNormal(entry, -1.0F, 0.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, f, j, h).setColor(red, green, blue, alpha).setNormal(entry, -1.0F, 0.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, f, j, h).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, f, j, k).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, f, j, k).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, -1.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, f, g, k).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, -1.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, f, g, k).setColor(red, green, blue, alpha).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, i, g, k).setColor(red, green, blue, alpha).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, i, g, k).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, 0.0F, -1.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, i, g, h).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, 0.0F, -1.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, f, j, k).setColor(red, green, blue, alpha).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, i, j, k).setColor(red, green, blue, alpha).setNormal(entry, 1.0F, 0.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, i, g, k).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, i, j, k).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, 1.0F, 0.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, i, j, h).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(width);
        vertexConsumers.addVertex(entry, i, j, k).setColor(red, green, blue, alpha).setNormal(entry, 0.0F, 0.0F, 1.0F).setLineWidth(width);
        ^///?}
    }
    *///?}
}
