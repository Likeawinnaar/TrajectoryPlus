package com.trajectoryplus.rendering;

import com.trajectoryplus.config.TrajectoryConfig;
import com.trajectoryplus.prediction.TrajectoryPredictor;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public final class RenderPlatform {
    private static Supplier<TrajectoryPredictor.Result> result;
    private static Supplier<TrajectoryConfig> config;

    private RenderPlatform() {}

    public static KeyBinding registerToggle() {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding("key.trajectoryplus.toggle", GLFW.GLFW_KEY_P,
                KeyBinding.Category.create(Identifier.of("trajectoryplus", "controls"))));
    }

    public static void registerRenderer(Supplier<TrajectoryPredictor.Result> prediction, Supplier<TrajectoryConfig> settings) {
        result = prediction;
        config = settings;
    }

    public static void draw(MatrixStack matrices, VertexConsumerProvider.Immediate consumers, Vec3d camera) {
        if (result == null) return;
        TrajectoryGeometry.draw(result.get(), config.get(),
                new YarnLines(matrices.peek(), consumers.getBuffer(RenderLayer.getLines()), camera));
    }
}
