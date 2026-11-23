package com.trajectoryplus.rendering;

import com.trajectoryplus.prediction.Point;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

/** Emits camera-relative vertices for the pre-1.21.11 vanilla line format. */
public final class YarnLines implements TrajectoryGeometry.Lines {
    private final MatrixStack.Entry matrix;
    private final VertexConsumer vertices;
    private final Point camera;

    public YarnLines(MatrixStack.Entry matrix, VertexConsumer vertices, Vec3d camera) {
        this.matrix = matrix;
        this.vertices = vertices;
        this.camera = new Point(camera.x, camera.y, camera.z);
    }

    @Override
    public void line(Point start, Point end, int argb, float width) {
        Point delta = end.subtract(start);
        double length = delta.length();
        if (length < 0.00001) return;
        Point normal = delta.multiply(1 / length);
        Point relativeStart = start.subtract(camera);
        Point relativeEnd = end.subtract(camera);
        emit(relativeStart, normal, argb);
        emit(relativeEnd, normal, argb);
        // Older line layers have a fixed pixel width. Offset adjacent copies slightly for thicker lines.
        for (int i = 1; i < width; i++) {
            double offset = .0018 * ((i + 1) / 2) * ((i & 1) == 0 ? 1 : -1);
            emit(relativeStart.add(offset, 0, 0), normal, argb);
            emit(relativeEnd.add(offset, 0, 0), normal, argb);
        }
    }

    private void emit(Point point, Point normal, int argb) {
        vertices.vertex(matrix, (float) point.x(), (float) point.y(), (float) point.z())
                .color((argb >>> 16) & 255, (argb >>> 8) & 255, argb & 255, argb >>> 24)
                .normal(matrix, (float) normal.x(), (float) normal.y(), (float) normal.z());
    }
}
