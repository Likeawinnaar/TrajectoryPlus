package com.trajectoryplus.prediction;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public record YarnWorld(ClientWorld world, ClientPlayerEntity player) implements TrajectoryPredictor.World {
    public static Point point(Vec3d vector) {
        return new Point(vector.x, vector.y, vector.z);
    }

    public static Vec3d vector(Point point) {
        return new Vec3d(point.x(), point.y(), point.z());
    }

    @Override
    public boolean loaded(Point point) {
        BlockPos block = BlockPos.ofFloored(point.x(), point.y(), point.z());
        return world.getChunkManager().isChunkLoaded(block.getX() >> 4, block.getZ() >> 4);
    }

    @Override
    public Point impact(Point from, Point to) {
        var hit = world.raycast(new RaycastContext(vector(from), vector(to),
                RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
        return hit.getType() == HitResult.Type.BLOCK ? point(hit.getPos()) : null;
    }

    @Override
    public boolean water(Point point) {
        return world.getFluidState(BlockPos.ofFloored(point.x(), point.y(), point.z())).isIn(FluidTags.WATER);
    }
}
