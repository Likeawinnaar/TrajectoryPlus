package com.trajectoryplus.mixin;

import com.trajectoryplus.rendering.RenderPlatform;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Minecraft 1.21.9's Fabric rendering API does not expose a world render event. */
@Mixin(DebugRenderer.class)
abstract class DebugRendererMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void trajectoryplus$draw(MatrixStack matrices, Frustum frustum, VertexConsumerProvider.Immediate consumers,
                                    double x, double y, double z, boolean late, CallbackInfo ci) {
        if (!late) RenderPlatform.draw(matrices, consumers, new Vec3d(x, y, z));
    }
}
