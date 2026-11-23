package com.trajectoryplus;

import com.trajectoryplus.prediction.PhysicsMath;

/** Zero-dependency numerical smoke tests, not a Minecraft integration test. */
public final class PhysicsMathSelfTest {
    private static void near(double expected, double actual, double tolerance) {
        if (!Double.isFinite(actual) || Math.abs(expected - actual) > tolerance)
            throw new AssertionError("expected=" + expected + " actual=" + actual);
    }

    public static void main(String[] args) {
        near(.99, PhysicsMath.dragComponent(1, .99), 1e-12);
        near(.96, PhysicsMath.verticalVelocity(1, .99, .03), 1e-12);
        near(-.03, PhysicsMath.verticalVelocity(0, .99, .03), 1e-12);
        near(0, PhysicsMath.remainingFraction(-2, 3), 1e-12);
        near(.5, PhysicsMath.remainingFraction(2, 4), 1e-12);
        near(1, PhysicsMath.remainingFraction(10, 4), 1e-12);
        near(0, PhysicsMath.remainingFraction(1, 0), 1e-12);
        double vy = 0;
        for (int i = 0; i < 10; i++) vy = PhysicsMath.verticalVelocity(vy, .99, .03);
        if (vy >= 0) throw new AssertionError("Gravity should lower vertical velocity");
        System.out.println("PASS: eight offline physics assertions (not Minecraft in-game validation)");
    }
}
