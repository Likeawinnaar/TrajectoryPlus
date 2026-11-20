package com.trajectoryplus.rendering;

import com.trajectoryplus.config.TrajectoryConfig;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

/** World-space, collision-point centered impact markers. */
public final class ImpactMarkerRenderer {
    public void draw(MatrixStack matrices, VertexConsumer vertices, Vec3d camera,
                     Vec3d hit, TrajectoryConfig config) {
        double size = config.impactMarkerSize * .05;
        int rgb = TrajectoryRenderer.parseColor(config.impactMarkerColor);
        int alpha = (int) Math.round(255 * config.impactMarkerOpacity);
        switch (config.impactMarkerStyle) {
            case CROSS -> {
                line(matrices, vertices, camera, hit.add(-size, 0, 0), hit.add(size, 0, 0), rgb, alpha);
                line(matrices, vertices, camera, hit.add(0, -size, 0), hit.add(0, size, 0), rgb, alpha);
                line(matrices, vertices, camera, hit.add(0, 0, -size), hit.add(0, 0, size), rgb, alpha);
            }
            case SQUARE -> {
                Vec3d a = hit.add(-size, 0, -size);
                Vec3d b = hit.add(size, 0, -size);
                Vec3d c = hit.add(size, 0, size);
                Vec3d d = hit.add(-size, 0, size);
                line(matrices, vertices, camera, a, b, rgb, alpha);
                line(matrices, vertices, camera, b, c, rgb, alpha);
                line(matrices, vertices, camera, c, d, rgb, alpha);
                line(matrices, vertices, camera, d, a, rgb, alpha);
            }
            case CIRCLE -> {
                for (int i = 0; i < 20; i++) {
                    double a = i * Math.PI / 10;
                    double b = (i + 1) * Math.PI / 10;
                    line(matrices, vertices, camera,
                            hit.add(Math.cos(a) * size, 0, Math.sin(a) * size),
                            hit.add(Math.cos(b) * size, 0, Math.sin(b) * size), rgb, alpha);
                }
            }
        }
    }

    private static void line(MatrixStack matrices, VertexConsumer vertices, Vec3d camera,
                             Vec3d a, Vec3d b, int rgb, int alpha) {
        TrajectoryRenderer.emitLine(matrices, vertices, camera, a, b, rgb, alpha);
    }
}
