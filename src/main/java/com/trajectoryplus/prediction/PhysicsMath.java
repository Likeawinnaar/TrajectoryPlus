package com.trajectoryplus.prediction;

/** Pure-Java helpers; covered by a test that does not require Minecraft dependencies. */
public final class PhysicsMath {
    private PhysicsMath() {}

    public static double dragComponent(double velocity, double drag) {
        return velocity * drag;
    }

    public static double verticalVelocity(double velocity, double drag, double gravity) {
        return dragComponent(velocity, drag) - gravity;
    }

    public static double remainingFraction(double remaining, double segmentLength) {
        if (!(remaining > 0) || !(segmentLength > 0)) return 0;
        return Math.min(1, remaining / segmentLength);
    }
}
