package com.trajectoryplus.rendering;

import com.trajectoryplus.config.TrajectoryConfig;
import com.trajectoryplus.prediction.TrajectoryPredictor;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayers;
import com.trajectoryplus.prediction.Point;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public final class RenderPlatform {
    private RenderPlatform() {}

    public static KeyBinding registerToggle() {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding("key.trajectoryplus.toggle", GLFW.GLFW_KEY_P,
                KeyBinding.Category.create(Identifier.of("trajectoryplus", "controls"))));
    }

    public static void registerRenderer(Supplier<TrajectoryPredictor.Result> result, Supplier<TrajectoryConfig> config) {
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
            if (context.matrices() == null || context.consumers() == null) return;
            var camera = context.worldState().cameraRenderState.pos;
            var cameraPoint = new Point(camera.x, camera.y, camera.z);
            var matrix = context.matrices().peek();
            var vertices = context.consumers().getBuffer(RenderLayers.lines());
            TrajectoryGeometry.draw(result.get(), config.get(), (start, end, argb, width) -> {
                Point delta = end.subtract(start);
                double length = delta.length();
                if (length < 0.00001) return;
                Point normal = delta.multiply(1 / length);
                for (Point position : new Point[] {start, end}) {
                    Point relative = position.subtract(cameraPoint);
                    vertices.vertex(matrix, (float) relative.x(), (float) relative.y(), (float) relative.z())
                            .color(argb).normal(matrix, (float) normal.x(), (float) normal.y(), (float) normal.z())
                            .lineWidth(width);
                }
            });
        });
    }
}
