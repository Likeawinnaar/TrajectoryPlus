package com.trajectoryplus.prediction;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public record MojangWorld(ClientLevel level, LocalPlayer player) implements TrajectoryPredictor.World {
    public static Point point(Vec3 vector) {
        return new Point(vector.x, vector.y, vector.z);
    }

    public static Vec3 vector(Point point) {
        return new Vec3(point.x(), point.y(), point.z());
    }

    @Override
    public boolean loaded(Point point) {
        return level.isLoaded(BlockPos.containing(point.x(), point.y(), point.z()));
    }

    @Override
    public Point impact(Point from, Point to) {
        var hit = level.clip(new ClipContext(vector(from), vector(to), ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE, player));
        return hit.getType() == HitResult.Type.BLOCK ? point(hit.getLocation()) : null;
    }

    @Override
    public boolean water(Point point) {
        return level.getFluidState(BlockPos.containing(point.x(), point.y(), point.z())).is(FluidTags.WATER);
    }
}
