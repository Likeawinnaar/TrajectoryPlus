package com.trajectoryplus.rendering;

import com.trajectoryplus.config.TrajectoryConfig;
import com.trajectoryplus.prediction.TrajectoryPredictor;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
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
            TrajectoryGeometry.draw(result.get(), config.get(), new YarnLines(context.matrices().peek(),
                    context.consumers().getBuffer(RenderLayer.getLines()), camera));
        });
    }
}
