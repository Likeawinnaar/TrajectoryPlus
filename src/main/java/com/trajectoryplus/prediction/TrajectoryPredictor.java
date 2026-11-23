package com.trajectoryplus.prediction;

import com.trajectoryplus.config.TrajectoryConfig;
import java.util.ArrayList;
import java.util.List;

/** Simulates one movement segment per tick, using the client's collision and fluid queries. */
public final class TrajectoryPredictor {
    public interface World {
        boolean loaded(Point point);
        Point impact(Point from, Point to);
        boolean water(Point point);
    }

    public record Launch(ProjectileProfile profile, Point position, Point velocity) {}

    public enum StopReason { IMPACT, TIME_LIMIT, DISTANCE_LIMIT, UNLOADED_CHUNK, NONE }

    public record Result(List<Point> path, Point impact, StopReason reason, ProjectileProfile.Kind kind) {}

    private static final Result EMPTY = new Result(List.of(), null, StopReason.NONE, null);
    private Result current = EMPTY;

    public Result current() {
        return current;
    }

    public void tick(World world, Launch launch, TrajectoryConfig settings) {
        current = launch == null || !settings.shouldPredict() ? EMPTY : simulate(world, launch, settings);
    }

    public static Launch launch(ProjectileProfile.Kind kind, double pull, float pitch, float yaw,
                                Point eye, Point movement, boolean onGround) {
        ProjectileProfile profile = ProjectileProfile.forKind(kind, pull);
        double p = Math.toRadians(pitch + profile.pitchOffset());
        double y = Math.toRadians(yaw);
        Point direction = new Point(-Math.sin(y) * Math.cos(p), -Math.sin(p), Math.cos(y) * Math.cos(p));
        Point velocity = direction.multiply(profile.speed())
                .add(movement.x(), onGround ? 0 : movement.y(), movement.z());
        Point origin = eye.add(0, -.1, 0);
        if (!profile.arrowLike()) origin = origin.add(direction.multiply(.16));
        return new Launch(profile, origin, velocity);
    }

    private Result simulate(World world, Launch launch, TrajectoryConfig settings) {
        Point pos = launch.position();
        Point velocity = launch.velocity();
        ProjectileProfile profile = launch.profile();
        List<Point> points = new ArrayList<>(Math.min(settings.maxTicks + 1, 256));
        points.add(pos);
        double traversed = 0;
        for (int tick = 0; tick < settings.maxTicks; tick++) {
            double length = velocity.length();
            if (!Double.isFinite(length)) break;
            double remaining = settings.maxDistance - traversed;
            boolean atLimit = length >= remaining;
            Point next = pos.add(atLimit ? velocity.multiply(PhysicsMath.remainingFraction(remaining, length)) : velocity);
            if (!world.loaded(next)) {
                return new Result(List.copyOf(points), null, StopReason.UNLOADED_CHUNK, profile.kind());
            }
            Point hit = world.impact(pos, next);
            if (hit != null) {
                points.add(hit);
                return new Result(List.copyOf(points), hit, StopReason.IMPACT, profile.kind());
            }
            points.add(next);
            if (atLimit) {
                return new Result(List.copyOf(points), null, StopReason.DISTANCE_LIMIT, profile.kind());
            }
            traversed += length;
            pos = next;
            double drag = world.water(pos) ? profile.waterDrag() : profile.airDrag();
            velocity = new Point(
                    PhysicsMath.dragComponent(velocity.x(), drag),
                    PhysicsMath.verticalVelocity(velocity.y(), drag, profile.gravity()),
                    PhysicsMath.dragComponent(velocity.z(), drag));
        }
        return new Result(List.copyOf(points), null, StopReason.TIME_LIMIT, profile.kind());
    }
}
