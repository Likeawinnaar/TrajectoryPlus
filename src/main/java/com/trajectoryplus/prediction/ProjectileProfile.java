package com.trajectoryplus.prediction;

import com.google.gson.annotations.SerializedName;

/** Vanilla-inspired parameters for MC 1.21.1. Random divergence cannot be predicted. */
public record ProjectileProfile(Kind kind, double speed, double gravity, double airDrag,
                                double waterDrag, double pitchOffset, boolean arrowLike) {
    public enum Kind {
        @SerializedName("enderPearl") ENDER_PEARL("enderPearl"),
        @SerializedName("bow") BOW("bow"),
        @SerializedName("crossbow") CROSSBOW("crossbow"),
        @SerializedName("snowball") SNOWBALL("snowball"),
        @SerializedName("egg") EGG("egg"),
        @SerializedName("trident") TRIDENT("trident"),
        @SerializedName("splashPotion") SPLASH_POTION("splashPotion"),
        @SerializedName("lingeringPotion") LINGERING_POTION("lingeringPotion"),
        @SerializedName("experienceBottle") EXPERIENCE_BOTTLE("experienceBottle");
        public final String key;
        Kind(String key) { this.key = key; }
    }

    public static ProjectileProfile forKind(Kind kind, double bowPower) {
        return switch (kind) {
            case ENDER_PEARL -> new ProjectileProfile(kind, 1.5, .03, .99, .8, 0, false);
            case BOW -> new ProjectileProfile(kind, 3.0 * bowPower, .05, .99, .6, 0, true);
            case CROSSBOW -> new ProjectileProfile(kind, 3.15, .05, .99, .6, 0, true);
            case SNOWBALL, EGG -> new ProjectileProfile(kind, 1.5, .03, .99, .8, 0, false);
            case TRIDENT -> new ProjectileProfile(kind, 2.5, .05, .99, .99, 0, true);
            case SPLASH_POTION, LINGERING_POTION -> new ProjectileProfile(kind, .5, .05, .99, .8, -20, false);
            case EXPERIENCE_BOTTLE -> new ProjectileProfile(kind, .7, .07, .99, .8, -20, false);
        };
    }
}
