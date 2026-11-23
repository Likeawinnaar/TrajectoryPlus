package com.trajectoryplus.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

final class ScreenPlatform {
    private ScreenPlatform() {}

    static void open(Minecraft client, Screen screen) {
        client.setScreen(screen);
    }
}
