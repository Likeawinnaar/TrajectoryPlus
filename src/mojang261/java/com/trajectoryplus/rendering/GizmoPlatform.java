package com.trajectoryplus.rendering;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.gizmos.Gizmos;

final class GizmoPlatform {
    private GizmoPlatform() {}

    static Gizmos.TemporaryCollection collect(LevelRenderer renderer) {
        return renderer.collectPerFrameGizmos();
    }
}
