package com.trajectoryplus.prediction;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

/** Casts the whole movement segment against actual block voxel shapes. */
public final class CollisionResolver {
    public Vec3d impact(ClientWorld world, ClientPlayerEntity player, Vec3d from, Vec3d to) {
        BlockHitResult hit = world.raycast(new RaycastContext(
                from, to, RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE, player));
        return hit.getType() == HitResult.Type.BLOCK ? hit.getPos() : null;
    }
}
