package com.trajectoryplus;

import com.trajectoryplus.config.TrajectoryConfig;
import com.trajectoryplus.prediction.Point;
import com.trajectoryplus.prediction.ProjectileProfile.Kind;
import com.trajectoryplus.prediction.TrajectoryPredictor;
import com.trajectoryplus.prediction.TrajectoryPredictor.StopReason;
import com.trajectoryplus.rendering.TrajectoryGeometry;
import java.util.ArrayList;
import java.util.List;

/** Regression checks for the shared simulator and line geometry. */
public final class TrajectorySelfTest {
    private static final Point ORIGIN = new Point(0, 0, 0);

    private record TestWorld(double loadedUntil, double wall) implements TrajectoryPredictor.World {
        @Override
        public boolean loaded(Point point) {
            return point.z() <= loadedUntil;
        }

        @Override
        public Point impact(Point from, Point to) {
            if (from.z() >= wall || to.z() < wall) return null;
            return from.lerp(to, (wall - from.z()) / (to.z() - from.z()));
        }

        @Override
        public boolean water(Point point) {
            return false;
        }
    }

    public static void main(String[] args) {
        TrajectoryConfig config = new TrajectoryConfig();
        TrajectoryPredictor predictor = new TrajectoryPredictor();
        config.maxDistance = 16;
        for (Kind kind : Kind.values()) {
            var launch = TrajectoryPredictor.launch(kind, 1, 0, 0, ORIGIN, ORIGIN, true);
            predictor.tick(new TestWorld(32, 2), launch, config);
            require(predictor.current().kind() == kind, "Unexpected projectile kind");
            require(predictor.current().reason() == StopReason.IMPACT, kind + " should hit the wall");
            require(Math.abs(predictor.current().impact().z() - 2) < 1e-9, "Wrong wall intersection");
            List<Point> segmentStarts = new ArrayList<>();
            TrajectoryGeometry.draw(predictor.current(), config, (start, end, argb, width) -> segmentStarts.add(start));
            int expected = 3 + (kind == Kind.BOW ? 0 : predictor.current().path().size() - 1);
            require(segmentStarts.size() == expected, kind + " should draw the expected lines and impact marker");
        }

        var pearl = TrajectoryPredictor.launch(Kind.ENDER_PEARL, 1, 0, 0, ORIGIN, ORIGIN, true);
        predictor.tick(new TestWorld(32, 16.8), pearl, config);
        require(predictor.current().reason() == StopReason.DISTANCE_LIMIT, "Collision beyond distance limit");
        require(predictor.current().impact() == null, "Impact must be within distance limit");
        predictor.tick(new TestWorld(1, 2), pearl, config);
        require(predictor.current().reason() == StopReason.UNLOADED_CHUNK, "Should stop before an unloaded chunk");
        config.enabled = false;
        predictor.tick(new TestWorld(32, 2), pearl, config);
        require(predictor.current().path().isEmpty(), "Disabling prediction should clear the cached path");
        System.out.println("PASS: projectile trajectories, bow impact-only, and collision limits");
    }

    private static void require(boolean condition, String detail) {
        if (!condition) throw new AssertionError(detail);
    }
}
