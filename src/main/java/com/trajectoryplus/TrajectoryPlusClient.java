package com.trajectoryplus;

import com.trajectoryplus.config.ConfigManager;
import com.trajectoryplus.prediction.TrajectoryPredictor;
import com.trajectoryplus.rendering.TrajectoryRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/** Client-only Fabric entry point. Never changes packets, physics or server state. */
public final class TrajectoryPlusClient implements ClientModInitializer {
    public static ConfigManager CONFIG;
    public static KeyBinding TOGGLE_KEY;
    private final TrajectoryPredictor predictor = new TrajectoryPredictor();
    private final TrajectoryRenderer renderer = new TrajectoryRenderer();

    @Override
    public void onInitializeClient() {
        CONFIG = new ConfigManager();
        TOGGLE_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.trajectoryplus.toggle", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P, "category.trajectoryplus"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TOGGLE_KEY.wasPressed()) {
                var config = CONFIG.get().copy();
                config.enabled = !config.enabled;
                CONFIG.update(config);
            }
            predictor.tick(client.world, client.player, CONFIG.get());
        });
        WorldRenderEvents.AFTER_ENTITIES.register(context ->
                renderer.render(context, predictor.current(), CONFIG.get()));
    }
}
