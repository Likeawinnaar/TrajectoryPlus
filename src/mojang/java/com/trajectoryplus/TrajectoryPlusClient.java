package com.trajectoryplus;

import com.trajectoryplus.config.ConfigManager;
import com.trajectoryplus.prediction.MojangWorld;
import com.trajectoryplus.prediction.ProjectileResolver;
import com.trajectoryplus.prediction.TrajectoryPredictor;
import com.trajectoryplus.rendering.MojangRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.platform.InputConstants;

public final class TrajectoryPlusClient implements ClientModInitializer {
    public static ConfigManager CONFIG;
    private final TrajectoryPredictor predictor = new TrajectoryPredictor();
    private final ProjectileResolver resolver = new ProjectileResolver();
    private volatile MojangRenderer.Frame frame;

    @Override
    public void onInitializeClient() {
        CONFIG = new ConfigManager();
        var category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("trajectoryplus", "controls"));
        var toggle = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.trajectoryplus.toggle",
                InputConstants.KEY_P, category));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggle.consumeClick()) {
                var config = CONFIG.get().copy();
                config.enabled = !config.enabled;
                CONFIG.update(config);
            }
            var config = CONFIG.get();
            var launch = client.level == null || client.player == null || !config.shouldPredict()
                    ? null : resolver.resolve(client.player, config);
            predictor.tick(new MojangWorld(client.level, client.player), launch, config);
            frame = new MojangRenderer.Frame(predictor.current(), config, client.level);
        });
        MojangRenderer.register(() -> frame);
    }
}
