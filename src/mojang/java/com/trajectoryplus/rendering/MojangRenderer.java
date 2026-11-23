package com.trajectoryplus.rendering;

import com.trajectoryplus.config.TrajectoryConfig;
import com.trajectoryplus.prediction.MojangWorld;
import com.trajectoryplus.prediction.TrajectoryPredictor;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.gizmos.Gizmos;

public final class MojangRenderer {
    public record Frame(TrajectoryPredictor.Result result, TrajectoryConfig config, ClientLevel level) {}

    private MojangRenderer() {}

    public static void register(Supplier<Frame> latestFrame) {
        LevelRenderEvents.BEFORE_GIZMOS.register(context -> {
            Frame frame = latestFrame.get();
            if (frame == null || frame.level() == null || frame.result().path().size() < 2) return;
            // The vanilla gizmo pipeline applies the camera transform and depth tests these world-space lines.
            try (var collection = GizmoPlatform.collect(context.levelRenderer())) {
                TrajectoryGeometry.draw(frame.result(), frame.config(), (start, end, argb, width) ->
                        Gizmos.line(MojangWorld.vector(start), MojangWorld.vector(end), argb, width));
            }
        });
    }
}
