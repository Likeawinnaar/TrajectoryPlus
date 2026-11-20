package com.trajectoryplus.integration;

import com.trajectoryplus.gui.TrajectorySettingsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/** The Mod Menu Configure button opens the real native settings screen. */
public final class TrajectoryModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return TrajectorySettingsScreen::new;
    }
}
