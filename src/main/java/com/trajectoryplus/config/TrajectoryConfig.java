package com.trajectoryplus.config;

import com.trajectoryplus.prediction.ProjectileProfile;
import java.util.EnumMap;
import java.util.Map;

/** JSON-backed user settings. Public for Gson, validated at load/save boundaries. */
public final class TrajectoryConfig {
    public int configVersion = 1;
    public boolean enabled = true;
    public boolean showTrajectory = true;
    public boolean showImpactMarker = true;
    public LineStyle lineStyle = LineStyle.SOLID;
    public String lineColor = "#00D9FF";
    public double lineOpacity = .85;
    public int lineThickness = 2;
    public MarkerStyle impactMarkerStyle = MarkerStyle.CROSS;
    public String impactMarkerColor = "#00D9FF";
    public double impactMarkerOpacity = .85;
    public int impactMarkerSize = 4;
    public int maxDistance = 256;
    public int maxTicks = 200;
    public Map<ProjectileProfile.Kind, Boolean> projectiles = new EnumMap<>(ProjectileProfile.Kind.class);

    public enum LineStyle { SOLID, DASHED, DOTTED }
    public enum MarkerStyle { CROSS, SQUARE, CIRCLE }

    public TrajectoryConfig() {
        for (ProjectileProfile.Kind kind : ProjectileProfile.Kind.values()) projectiles.put(kind, true);
    }

    public TrajectoryConfig copy() {
        TrajectoryConfig other = new TrajectoryConfig();
        other.configVersion = configVersion;
        other.enabled = enabled;
        other.showTrajectory = showTrajectory;
        other.showImpactMarker = showImpactMarker;
        other.lineStyle = lineStyle;
        other.lineColor = lineColor;
        other.lineOpacity = lineOpacity;
        other.lineThickness = lineThickness;
        other.impactMarkerStyle = impactMarkerStyle;
        other.impactMarkerColor = impactMarkerColor;
        other.impactMarkerOpacity = impactMarkerOpacity;
        other.impactMarkerSize = impactMarkerSize;
        other.maxDistance = maxDistance;
        other.maxTicks = maxTicks;
        other.projectiles = new EnumMap<>(ProjectileProfile.Kind.class);
        other.projectiles.putAll(projectiles);
        return other;
    }

    public void validate() {
        configVersion = 1;
        if (lineStyle == null) lineStyle = LineStyle.SOLID;
        if (impactMarkerStyle == null) impactMarkerStyle = MarkerStyle.CROSS;
        lineColor = sanitizeColor(lineColor);
        impactMarkerColor = sanitizeColor(impactMarkerColor);
        lineOpacity = clampFinite(lineOpacity, .05, 1, .85);
        impactMarkerOpacity = clampFinite(impactMarkerOpacity, .05, 1, .85);
        lineThickness = Math.clamp(lineThickness, 1, 4);
        impactMarkerSize = Math.clamp(impactMarkerSize, 1, 16);
        maxDistance = Math.clamp(maxDistance, 16, 512);
        maxTicks = Math.clamp(maxTicks, 20, 400);
        Map<ProjectileProfile.Kind, Boolean> supplied = projectiles;
        projectiles = new EnumMap<>(ProjectileProfile.Kind.class);
        if (supplied != null) projectiles.putAll(supplied);
        for (ProjectileProfile.Kind kind : ProjectileProfile.Kind.values()) {
            projectiles.putIfAbsent(kind, true);
            if (projectiles.get(kind) == null) projectiles.put(kind, true);
        }
    }

    public boolean allows(ProjectileProfile.Kind type) { return projectiles.getOrDefault(type, true); }

    public static String sanitizeColor(String color) {
        if (color == null || !color.matches("#[0-9a-fA-F]{6}")) return "#00D9FF";
        return color.toUpperCase(java.util.Locale.ROOT);
    }
    public static double clampFinite(double value, double min, double max, double fallback) {
        return Double.isFinite(value) ? Math.max(min, Math.min(max, value)) : fallback;
    }
}
