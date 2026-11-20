package com.trajectoryplus.prediction;

import com.trajectoryplus.config.TrajectoryConfig;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import java.util.List;

/** Never spawns an entity or loads chunks. One simulation iteration is one game tick. */
public final class TrajectoryPredictor {
    public enum StopReason { IMPACT, TIME_LIMIT, DISTANCE_LIMIT, UNLOADED_CHUNK, NONE }
    public record Result(List<Vec3d> path, Vec3d impact, StopReason reason) {
        public boolean hasImpact() { return reason == StopReason.IMPACT && impact != null; }
    }
    private static final Result EMPTY = new Result(List.of(), null, StopReason.NONE);
    private final CollisionResolver collision = new CollisionResolver();
    private final ProjectileResolver resolver = new ProjectileResolver();
    private Result cached = EMPTY;
    private long lastNanos;

    public Result current() { return cached; }
    public long lastMicros() { return lastNanos / 1000; }
    public void clear() { cached = EMPTY; }

    public void tick(ClientWorld world, ClientPlayerEntity player, TrajectoryConfig settings) {
        if (world == null || player == null || !settings.enabled ||
                (!settings.showTrajectory && !settings.showImpactMarker)) { clear(); return; }
        ProjectileResolver.Launch launch = resolver.resolve(player, settings);
        if (launch == null) { clear(); return; }
        long startNanos = System.nanoTime();
        cached = simulate(world, player, launch, settings);
        lastNanos = System.nanoTime() - startNanos;
    }

    private Result simulate(ClientWorld world, ClientPlayerEntity player,
                            ProjectileResolver.Launch launch, TrajectoryConfig settings) {
        Vec3d pos = launch.position();
        Vec3d velocity = launch.velocity();
        List<Vec3d> points = new ArrayList<>(Math.min(settings.maxTicks, 256));
        points.add(pos);
        double traversed = 0;
        for (int tick = 0; tick < settings.maxTicks; tick++) {
            if (!Double.isFinite(pos.x + pos.y + pos.z + velocity.x + velocity.y + velocity.z)) break;
            Vec3d next = pos.add(velocity);
            BlockPos nextBlock = BlockPos.ofFloored(next);
            if (!world.isChunkLoaded(nextBlock.getX() >> 4, nextBlock.getZ() >> 4))
                return new Result(List.copyOf(points), null, StopReason.UNLOADED_CHUNK);
            Vec3d hit = collision.impact(world, player, pos, next);
            if (hit != null) {
                points.add(hit);
                return new Result(List.copyOf(points), hit, StopReason.IMPACT);
            }
            double length = next.distanceTo(pos);
            if (traversed + length >= settings.maxDistance) {
                Vec3d end = pos.add(velocity.multiply(PhysicsMath.remainingFraction(settings.maxDistance - traversed, length)));
                points.add(end);
                return new Result(List.copyOf(points), null, StopReason.DISTANCE_LIMIT);
            }
            points.add(next);
            traversed += length;
            pos = next;
            boolean inWater = world.getFluidState(BlockPos.ofFloored(pos)).isIn(FluidTags.WATER);
            double drag = inWater ? launch.profile().waterDrag() : launch.profile().airDrag();
            // Minecraft projectiles generally move, then apply drag, then gravity.
            velocity = new Vec3d(
                    PhysicsMath.dragComponent(velocity.x, drag),
                    PhysicsMath.verticalVelocity(velocity.y, drag, launch.profile().gravity()),
                    PhysicsMath.dragComponent(velocity.z, drag));
        }
        return new Result(List.copyOf(points), null, StopReason.TIME_LIMIT);
    }
}
