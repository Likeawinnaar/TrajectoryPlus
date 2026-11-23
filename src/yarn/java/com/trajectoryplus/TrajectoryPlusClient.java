package com.trajectoryplus;

import com.trajectoryplus.config.ConfigManager;
import com.trajectoryplus.prediction.TrajectoryPredictor;
import com.trajectoryplus.prediction.ProjectileResolver;
import com.trajectoryplus.prediction.YarnWorld;
import com.trajectoryplus.rendering.RenderPlatform;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;

/** Client-only Fabric entry point. Never changes packets, physics or server state. */
public final class TrajectoryPlusClient implements ClientModInitializer {
    public static ConfigManager CONFIG;
    private KeyBinding toggleKey;
    private final TrajectoryPredictor predictor = new TrajectoryPredictor();
    private final ProjectileResolver resolver = new ProjectileResolver();

    @Override
    public void onInitializeClient() {
        CONFIG = new ConfigManager();
        toggleKey = RenderPlatform.registerToggle();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                var config = CONFIG.get().copy();
                config.enabled = !config.enabled;
                CONFIG.update(config);
            }
            var config = CONFIG.get();
            var launch = client.world == null || client.player == null || !config.shouldPredict()
                    ? null : resolver.resolve(client.player, config);
            predictor.tick(new YarnWorld(client.world, client.player), launch, config);
        });
        RenderPlatform.registerRenderer(predictor::current, CONFIG::get);
    }
}
