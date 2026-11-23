package com.trajectoryplus.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/** Since 1.21.6, vanilla renders the background in its own GUI stratum. */
abstract class SettingsScreenBase extends Screen {
    protected SettingsScreenBase(Text title) {
        super(title);
    }
}
