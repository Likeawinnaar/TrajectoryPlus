package com.trajectoryplus.prediction;

public record Point(double x, double y, double z) {
    public Point add(double dx, double dy, double dz) {
        return new Point(x + dx, y + dy, z + dz);
    }

    public Point add(Point other) {
        return add(other.x, other.y, other.z);
    }

    public Point subtract(Point other) {
        return add(-other.x, -other.y, -other.z);
    }

    public Point multiply(double factor) {
        return new Point(x * factor, y * factor, z * factor);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Point lerp(Point end, double t) {
        return add(end.subtract(this).multiply(t));
    }
}
