package com.trajectoryplus.prediction;

import com.trajectoryplus.config.TrajectoryConfig;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

/** Recognizes only projectiles actually available to the local player. */
public final class ProjectileResolver {
    public record Launch(ProjectileProfile profile, Vec3d position, Vec3d velocity) {}

    public Launch resolve(ClientPlayerEntity player, TrajectoryConfig settings) {
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            ProjectileProfile.Kind kind = kindFor(stack);
            if (kind == null || !settings.allows(kind)) continue;
            double pull = 1;
            if (kind == ProjectileProfile.Kind.BOW) {
                if (!player.isUsingItem() || player.getActiveHand() != hand) continue;
                int used = stack.getMaxUseTime(player) - player.getItemUseTimeLeft();
                pull = BowItem.getPullProgress(Math.max(0, used));
                if (pull <= .02) continue;
            }
            if (kind == ProjectileProfile.Kind.TRIDENT &&
                    (!player.isUsingItem() || player.getActiveHand() != hand || player.getItemUseTime() < 10)) continue;
            if (kind == ProjectileProfile.Kind.CROSSBOW) {
                if (!CrossbowItem.isCharged(stack)) continue;
                ChargedProjectilesComponent loaded = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
                if (loaded == null || loaded.isEmpty()) continue;
                // Firework rockets and multi-projectile spread require separate prediction support.
                if (loaded.contains(Items.FIREWORK_ROCKET)) continue;
            }
            ProjectileProfile profile = ProjectileProfile.forKind(kind, pull);
            Vec3d direction = direction(player.getPitch() + (float) profile.pitchOffset(), player.getYaw());
            Vec3d velocity = direction.multiply(profile.speed());
            Vec3d movement = player.getVelocity();
            velocity = velocity.add(movement.x, player.isOnGround() ? 0 : movement.y, movement.z);
            Vec3d origin = player.getEyePos().add(0, -.1, 0).add(direction.multiply(.16));
            if (profile.arrowLike()) origin = player.getEyePos().add(0, -.1, 0);
            return new Launch(profile, origin, velocity);
        }
        return null;
    }

    private ProjectileProfile.Kind kindFor(ItemStack stack) {
        if (stack.isOf(Items.ENDER_PEARL)) return ProjectileProfile.Kind.ENDER_PEARL;
        if (stack.isOf(Items.BOW)) return ProjectileProfile.Kind.BOW;
        if (stack.isOf(Items.CROSSBOW)) return ProjectileProfile.Kind.CROSSBOW;
        if (stack.isOf(Items.SNOWBALL)) return ProjectileProfile.Kind.SNOWBALL;
        if (stack.isOf(Items.EGG)) return ProjectileProfile.Kind.EGG;
        if (stack.isOf(Items.TRIDENT)) return ProjectileProfile.Kind.TRIDENT;
        if (stack.isOf(Items.SPLASH_POTION)) return ProjectileProfile.Kind.SPLASH_POTION;
        if (stack.isOf(Items.LINGERING_POTION)) return ProjectileProfile.Kind.LINGERING_POTION;
        if (stack.isOf(Items.EXPERIENCE_BOTTLE)) return ProjectileProfile.Kind.EXPERIENCE_BOTTLE;
        return null;
    }

    private static Vec3d direction(float pitch, float yaw) {
        double p = Math.toRadians(pitch);
        double y = Math.toRadians(yaw);
        return new Vec3d(-Math.sin(y) * Math.cos(p), -Math.sin(p), Math.cos(y) * Math.cos(p));
    }
}
