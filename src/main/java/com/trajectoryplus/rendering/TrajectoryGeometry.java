package com.trajectoryplus.rendering;

import com.trajectoryplus.config.TrajectoryConfig;
import com.trajectoryplus.prediction.Point;
import com.trajectoryplus.prediction.ProjectileProfile;
import com.trajectoryplus.prediction.TrajectoryPredictor;

public final class TrajectoryGeometry {
    @FunctionalInterface
    public interface Lines {
        void line(Point start, Point end, int argb, float width);
    }

    private TrajectoryGeometry() {}

    public static void draw(TrajectoryPredictor.Result result, TrajectoryConfig config, Lines lines) {
        if (!config.enabled || result.path().size() < 2) return;
        if (config.showTrajectory && result.kind() != ProjectileProfile.Kind.BOW) {
            int argb = color(config.lineColor, config.lineOpacity);
            for (int i = 1; i < result.path().size(); i++) {
                segment(result.path().get(i - 1), result.path().get(i), config, argb, lines);
            }
        }
        if (config.showImpactMarker && result.impact() != null) {
            marker(result.impact(), config, lines);
        }
    }

    private static void segment(Point start, Point end, TrajectoryConfig config, int argb, Lines lines) {
        double distance = end.subtract(start).length();
        if (distance < 0.00001) return;
        if (config.lineStyle == TrajectoryConfig.LineStyle.SOLID) {
            lines.line(start, end, argb, config.lineThickness);
            return;
        }
        boolean dashed = config.lineStyle == TrajectoryConfig.LineStyle.DASHED;
        double cell = dashed ? .48 : .24;
        double active = dashed ? .62 : .20;
        int count = Math.min(48, Math.max(1, (int) Math.ceil(distance / cell)));
        for (int i = 0; i < count; i++) {
            lines.line(start.lerp(end, i / (double) count), start.lerp(end, (i + active) / count),
                    argb, config.lineThickness);
        }
    }

    private static void marker(Point hit, TrajectoryConfig config, Lines lines) {
        double size = config.impactMarkerSize * .05;
        int argb = color(config.impactMarkerColor, config.impactMarkerOpacity);
        switch (config.impactMarkerStyle) {
            case CROSS -> {
                lines.line(hit.add(-size, 0, 0), hit.add(size, 0, 0), argb, 2);
                lines.line(hit.add(0, -size, 0), hit.add(0, size, 0), argb, 2);
                lines.line(hit.add(0, 0, -size), hit.add(0, 0, size), argb, 2);
            }
            case SQUARE -> {
                Point a = hit.add(-size, 0, -size);
                Point b = hit.add(size, 0, -size);
                Point c = hit.add(size, 0, size);
                Point d = hit.add(-size, 0, size);
                lines.line(a, b, argb, 2);
                lines.line(b, c, argb, 2);
                lines.line(c, d, argb, 2);
                lines.line(d, a, argb, 2);
            }
            case CIRCLE -> {
                for (int i = 0; i < 20; i++) {
                    double a = i * Math.PI / 10;
                    double b = (i + 1) * Math.PI / 10;
                    lines.line(hit.add(Math.cos(a) * size, 0, Math.sin(a) * size),
                            hit.add(Math.cos(b) * size, 0, Math.sin(b) * size), argb, 2);
                }
            }
        }
    }

    private static int color(String hex, double opacity) {
        return ((int) Math.round(255 * opacity) << 24) | Integer.parseInt(hex.substring(1), 16);
    }
}
