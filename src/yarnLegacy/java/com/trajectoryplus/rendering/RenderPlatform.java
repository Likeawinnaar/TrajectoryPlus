package com.trajectoryplus.rendering;

import com.trajectoryplus.config.TrajectoryConfig;
import com.trajectoryplus.prediction.TrajectoryPredictor;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class RenderPlatform {
    private RenderPlatform() {}

    public static KeyBinding registerToggle() {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding("key.trajectoryplus.toggle",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, "category.trajectoryplus"));
    }

    public static void registerRenderer(Supplier<TrajectoryPredictor.Result> result, Supplier<TrajectoryConfig> config) {
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            if (context.matrixStack() == null || context.consumers() == null) return;
            TrajectoryGeometry.draw(result.get(), config.get(), new YarnLines(context.matrixStack().peek(),
                    context.consumers().getBuffer(RenderLayer.getLines()), context.camera().getPos()));
        });
    }
}
