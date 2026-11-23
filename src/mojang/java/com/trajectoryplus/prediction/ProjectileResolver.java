package com.trajectoryplus.prediction;

import com.trajectoryplus.config.TrajectoryConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import com.trajectoryplus.prediction.TrajectoryPredictor.Launch;

/** Recognizes only projectiles actually available to the local player. */
public final class ProjectileResolver {
    public Launch resolve(LocalPlayer player, TrajectoryConfig settings) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            ProjectileProfile.Kind kind = kindFor(stack);
            if (kind == null || !settings.allows(kind)) continue;
            double pull = 1;
            if (kind == ProjectileProfile.Kind.BOW) {
                if (!player.isUsingItem() || player.getUsedItemHand() != hand) continue;
                int used = player.getTicksUsingItem();
                pull = BowItem.getPowerForTime(Math.max(0, used));
                if (pull <= .02) continue;
            }
            if (kind == ProjectileProfile.Kind.TRIDENT &&
                    (!player.isUsingItem() || player.getUsedItemHand() != hand || player.getTicksUsingItem() < 10)) continue;
            if (kind == ProjectileProfile.Kind.CROSSBOW) {
                if (!CrossbowItem.isCharged(stack)) continue;
                ChargedProjectiles loaded = stack.get(DataComponents.CHARGED_PROJECTILES);
                if (loaded == null || loaded.isEmpty()) continue;
                // Firework rockets and multi-projectile spread require separate prediction support.
                if (loaded.contains(Items.FIREWORK_ROCKET)) continue;
            }
            return TrajectoryPredictor.launch(kind, pull, player.getXRot(), player.getYRot(),
                    MojangWorld.point(player.getEyePosition()), MojangWorld.point(player.getDeltaMovement()), player.onGround());
        }
        return null;
    }

    private ProjectileProfile.Kind kindFor(ItemStack stack) {
        if (stack.is(Items.ENDER_PEARL)) return ProjectileProfile.Kind.ENDER_PEARL;
        if (stack.is(Items.BOW)) return ProjectileProfile.Kind.BOW;
        if (stack.is(Items.CROSSBOW)) return ProjectileProfile.Kind.CROSSBOW;
        if (stack.is(Items.SNOWBALL)) return ProjectileProfile.Kind.SNOWBALL;
        if (stack.is(Items.EGG)) return ProjectileProfile.Kind.EGG;
        if (stack.is(Items.TRIDENT)) return ProjectileProfile.Kind.TRIDENT;
        if (stack.is(Items.SPLASH_POTION)) return ProjectileProfile.Kind.SPLASH_POTION;
        if (stack.is(Items.LINGERING_POTION)) return ProjectileProfile.Kind.LINGERING_POTION;
        if (stack.is(Items.EXPERIENCE_BOTTLE)) return ProjectileProfile.Kind.EXPERIENCE_BOTTLE;
        return null;
    }

}
