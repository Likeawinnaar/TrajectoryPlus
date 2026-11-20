package com.trajectoryplus.rendering;

import com.trajectoryplus.config.TrajectoryConfig;
import com.trajectoryplus.prediction.TrajectoryPredictor;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import java.util.List;

/** Emits camera-relative world-space line vertices into vanilla's depth-tested line layer. */
public final class TrajectoryRenderer {
    private final ImpactMarkerRenderer markerRenderer = new ImpactMarkerRenderer();

    public void render(WorldRenderContext context, TrajectoryPredictor.Result result, TrajectoryConfig config) {
        if (!config.enabled || result == null || result.path().size() < 2) return;
        MatrixStack stack = context.matrixStack();
        VertexConsumerProvider provider = context.consumers();
        if (stack == null || provider == null) return;
        Vec3d camera = context.camera().getPos();
        VertexConsumer consumer = provider.getBuffer(RenderLayer.getLines());
        stack.push();
        // Context consumers use camera-relative coordinates. The provided matrix already contains
        // the world-render transforms; do not apply the camera translation twice.
        int rgb = parseColor(config.lineColor);
        int alpha = (int) Math.round(config.lineOpacity * 255);
        if (config.showTrajectory) {
            List<Vec3d> points = result.path();
            for (int i = 1; i < points.size(); i++) {
                Vec3d a = points.get(i - 1);
                Vec3d b = points.get(i);
                renderSegment(stack, consumer, camera, a, b, rgb, alpha,
                        config.lineStyle, config.lineThickness);
            }
        }
        if (config.showImpactMarker && result.hasImpact())
            markerRenderer.draw(stack, consumer, camera, result.impact(), config);
        stack.pop();
    }

    public static int parseColor(String hex) {
        try { return Integer.parseInt(hex.substring(1), 16); }
        catch (RuntimeException e) { return 0x00D9FF; }
    }

    private void renderSegment(MatrixStack stack, VertexConsumer consumer, Vec3d camera,
                               Vec3d start, Vec3d end, int rgb, int alpha,
                               TrajectoryConfig.LineStyle style, int thickness) {
        double distance = start.distanceTo(end);
        if (distance < 0.00001) return;
        if (style == TrajectoryConfig.LineStyle.SOLID) {
            emitThickLine(stack, consumer, camera, start, end, rgb, alpha, thickness);
            return;
        }
        // Split each simulated tick segment into consistent world-distance dashes/dots.
        double cell = style == TrajectoryConfig.LineStyle.DASHED ? .48 : .24;
        double active = style == TrajectoryConfig.LineStyle.DASHED ? .62 : .20;
        int count = Math.min(48, Math.max(1, (int) Math.ceil(distance / cell)));
        for (int j = 0; j < count; j++) {
            double from = j / (double) count;
            double to = (j + active) / count;
            emitThickLine(stack, consumer, camera, start.lerp(end, from),
                    start.lerp(end, to), rgb, alpha, thickness);
        }
    }

    /** Thickness is an approximation: multiple small camera-plane offsets, not true pixels. */
    private void emitThickLine(MatrixStack stack, VertexConsumer out, Vec3d camera,
                               Vec3d a, Vec3d b, int rgb, int alpha, int thickness) {
        emitLine(stack, out, camera, a, b, rgb, alpha);
        for (int i = 1; i < thickness; i++) {
            double shift = .0018 * ((i + 1) / 2);
            double sign = (i & 1) == 0 ? 1 : -1;
            Vec3d offset = new Vec3d(shift * sign, 0, 0);
            emitLine(stack, out, camera, a.add(offset), b.add(offset), rgb, alpha);
        }
    }

    public static void emitLine(MatrixStack matrices, VertexConsumer out, Vec3d camera,
                                Vec3d a, Vec3d b, int rgb, int alpha) {
        Vec3d delta = b.subtract(a);
        if (delta.lengthSquared() < 1e-10) return;
        Vec3d normal = delta.normalize();
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int bl = rgb & 255;
        MatrixStack.Entry entry = matrices.peek();
        out.vertex(entry, (float) (a.x - camera.x), (float) (a.y - camera.y), (float) (a.z - camera.z))
                .color(r, g, bl, alpha).normal(entry, (float) normal.x, (float) normal.y, (float) normal.z);
        out.vertex(entry, (float) (b.x - camera.x), (float) (b.y - camera.y), (float) (b.z - camera.z))
                .color(r, g, bl, alpha).normal(entry, (float) normal.x, (float) normal.y, (float) normal.z);
    }
}
