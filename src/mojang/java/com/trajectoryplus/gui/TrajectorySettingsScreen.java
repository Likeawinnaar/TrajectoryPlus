package com.trajectoryplus.gui;

import com.trajectoryplus.TrajectoryPlusClient;
import com.trajectoryplus.config.TrajectoryConfig;
import com.trajectoryplus.prediction.ProjectileProfile;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

/** Staged settings: Done commits, Cancel/Escape discards, Reset reverts staged values. */
public final class TrajectorySettingsScreen extends Screen {
    private final Screen parent;
    private TrajectoryConfig draft;
    private int page;
    private boolean badLineColor;
    private boolean badMarkerColor;
    private static final String[] COLORS = {
            "#00D9FF", "#FFFFFF", "#00FF80", "#FFFF00", "#FF9900", "#FF4040", "#B388FF", "#000000"
    };

    public TrajectorySettingsScreen(Screen parent) {
        this(parent, TrajectoryPlusClient.CONFIG.get().copy(), 0);
    }

    private TrajectorySettingsScreen(Screen parent, TrajectoryConfig draft, int page) {
        super(Component.translatable("trajectoryplus.title"));
        this.parent = parent;
        this.draft = draft;
        this.page = page;
    }

    @Override
    protected void init() {
        int center = width / 2;
        int full = Math.min(460, width - 24);
        int x = center - full / 2;
        int column = (full - 8) / 2;
        int y = Math.max(44, height / 2 - 102);
        addRenderableWidget(Button.builder(Component.translatable("trajectoryplus.page.previous"), btn -> switchPage(-1))
                .bounds(x, 22, Math.min(110, column), 20).build());
        addRenderableWidget(Button.builder(Component.translatable("trajectoryplus.page.next"), btn -> switchPage(1))
                .bounds(x + full - Math.min(110, column), 22, Math.min(110, column), 20).build());
        switch (page) {
            case 0 -> drawGeneral(x, y, column);
            case 1 -> drawProjectiles(x, y, column);
            case 2 -> drawVisuals(x, y, column);
            case 3 -> drawAdvanced(x, y, column);
            default -> { }
        }
        int bottom = height - 27;
        int bw = Math.min(116, Math.max(62, (full - 12) / 3));
        int start = center - (3 * bw + 12) / 2;
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> done())
                .bounds(start, bottom, bw, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), b -> onClose())
                .bounds(start + bw + 6, bottom, bw, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("trajectoryplus.reset"), b -> {
            draft = new TrajectoryConfig();
            openPage(page);
        }).bounds(start + 2 * (bw + 6), bottom, bw, 20).build());
    }

    private void drawGeneral(int x, int y, int w) {
        toggle(x, y, w, "enabled", () -> draft.enabled, v -> draft.enabled = v);
        toggle(x + w + 8, y, w, "showTrajectory", () -> draft.showTrajectory, v -> draft.showTrajectory = v);
        toggle(x, y + 29, w, "showImpactMarker", () -> draft.showImpactMarker, v -> draft.showImpactMarker = v);
        cycle(x + w + 8, y + 29, w, "lineStyle", draft.lineStyle, TrajectoryConfig.LineStyle.values(),
                v -> draft.lineStyle = v);
        slider(x, y + 59, w, "lineOpacity", .05, 1, draft.lineOpacity, v -> draft.lineOpacity = v, true);
        slider(x + w + 8, y + 59, w, "lineThickness", 1, 4, draft.lineThickness,
                v -> draft.lineThickness = (int) Math.round(v), false);
        slider(x, y + 89, w, "maxDistance", 16, 512, draft.maxDistance,
                v -> draft.maxDistance = (int) Math.round(v), false);
    }

    private void drawProjectiles(int x, int y, int w) {
        int i = 0;
        for (ProjectileProfile.Kind kind : ProjectileProfile.Kind.values()) {
            int posX = x + (i % 2) * (w + 8);
            int posY = y + (i / 2) * 28;
            boolean allowed = draft.allows(kind);
            toggle(posX, posY, w, "projectile." + kind.key, () -> draft.allows(kind),
                    v -> draft.projectiles.put(kind, v));
            i++;
        }
    }

    private void drawVisuals(int x, int y, int w) {
        cycle(x, y, w, "lineStyle", draft.lineStyle, TrajectoryConfig.LineStyle.values(),
                v -> draft.lineStyle = v);
        cycle(x + w + 8, y, w, "impactMarkerStyle", draft.impactMarkerStyle,
                TrajectoryConfig.MarkerStyle.values(), v -> draft.impactMarkerStyle = v);
        slider(x, y + 28, w, "lineOpacity", .05, 1, draft.lineOpacity, v -> draft.lineOpacity = v, true);
        slider(x + w + 8, y + 28, w, "impactMarkerOpacity", .05, 1, draft.impactMarkerOpacity,
                v -> draft.impactMarkerOpacity = v, true);
        slider(x, y + 56, w, "lineThickness", 1, 4, draft.lineThickness,
                v -> draft.lineThickness = (int) Math.round(v), false);
        slider(x + w + 8, y + 56, w, "impactMarkerSize", 1, 16, draft.impactMarkerSize,
                v -> draft.impactMarkerSize = (int) Math.round(v), false);
        colorField(x, y + 98, w, true);
        colorField(x + w + 8, y + 98, w, false);
    }

    private void drawAdvanced(int x, int y, int w) {
        slider(x, y, w, "maxDistance", 16, 512, draft.maxDistance,
                v -> draft.maxDistance = (int) Math.round(v), false);
        slider(x + w + 8, y, w, "maxTicks", 20, 400, draft.maxTicks,
                v -> draft.maxTicks = (int) Math.round(v), false);
        addRenderableWidget(Button.builder(Component.translatable("trajectoryplus.keybinds"), b ->
                minecraft.setScreen(new KeyBindsScreen(this, minecraft.options)))
                .bounds(x, y + 42, w, 20).build());
    }

    private void colorField(int x, int y, int w, boolean line) {
        String current = line ? draft.lineColor : draft.impactMarkerColor;
        String key = line ? "lineColor" : "impactMarkerColor";
        addRenderableWidget(Button.builder(Component.translatable("trajectoryplus.palette"), b -> {
            String value = line ? draft.lineColor : draft.impactMarkerColor;
            int next = 0;
            for (int i = 0; i < COLORS.length; i++) {
                if (COLORS[i].equalsIgnoreCase(value)) { next = (i + 1) % COLORS.length; break; }
            }
            if (line) draft.lineColor = COLORS[next]; else draft.impactMarkerColor = COLORS[next];
            openPage(page);
        }).bounds(x, y, w, 20).build());
        EditBox field = new EditBox(font, x, y + 26, w, 20,
                Component.translatable("trajectoryplus.setting." + key));
        field.setMaxLength(7);
        field.setValue(current);
        field.setResponder(value -> {
            boolean valid = value.matches("#[0-9a-fA-F]{6}");
            if (line) {
                badLineColor = !valid;
                if (valid) draft.lineColor = value.toUpperCase(Locale.ROOT);
            } else {
                badMarkerColor = !valid;
                if (valid) draft.impactMarkerColor = value.toUpperCase(Locale.ROOT);
            }
        });
        addRenderableWidget(field);
    }

    private void toggle(int x, int y, int w, String key, BooleanSupplier value, Consumer<Boolean> setter) {
        Button button = Button.builder(boolLabel(key, value.getAsBoolean()), b -> {
            setter.accept(!value.getAsBoolean());
            b.setMessage(boolLabel(key, value.getAsBoolean()));
        }).bounds(x, y, w, 20).build();
        addRenderableWidget(button);
    }

    private static Component boolLabel(String key, boolean bool) {
        return Component.translatable("trajectoryplus.setting." + key).copy()
                .append(": ").append(Component.translatable(bool ? "options.on" : "options.off"));
    }

    private <E extends Enum<E>> void cycle(int x, int y, int w, String key, E initial,
                                           E[] values, Consumer<E> setter) {
        Button button = Button.builder(optionLabel(key, initial.name()), b -> {
            E current = initial;
            // Find current value from the button label's click count, managed with local state.
            int index = 0;
            for (int i = 0; i < values.length; i++) if (values[i] == current) index = i;
            E next = values[(index + 1) % values.length];
            setter.accept(next);
            b.setMessage(optionLabel(key, next.name()));
            // Rebuild same page so further clicks use the latest staged value.
            openPage(page);
        }).bounds(x, y, w, 20).build();
        addRenderableWidget(button);
    }

    private static Component optionLabel(String key, String enumName) {
        return Component.translatable("trajectoryplus.setting." + key).copy()
                .append(": ").append(Component.translatable("trajectoryplus.value." + enumName.toLowerCase(Locale.ROOT)));
    }

    private void slider(int x, int y, int w, String key, double min, double max,
                        double initial, DoubleConsumer consumer, boolean asPercent) {
        addRenderableWidget(new AbstractSliderButton(x, y, w, 20, Component.empty(), (initial - min) / (max - min)) {
            { updateMessage(); }
            @Override protected void updateMessage() {
                String display = asPercent ? Math.round((min + value * (max - min)) * 100) + "%"
                        : String.valueOf(Math.round(min + value * (max - min)));
                setMessage(Component.translatable("trajectoryplus.setting." + key).copy().append(": " + display));
            }
            @Override protected void applyValue() {
                consumer.accept(min + value * (max - min));
                updateMessage();
            }
        });
    }

    private void switchPage(int amount) { openPage(Math.floorMod(page + amount, 4)); }
    private void openPage(int index) {
        minecraft.setScreen(new TrajectorySettingsScreen(parent, draft, index));
    }

    private void done() {
        if (badLineColor || badMarkerColor) return;
        TrajectoryPlusClient.CONFIG.update(draft);
        minecraft.setScreen(parent);
    }

    @Override
    public void onClose() { minecraft.setScreen(parent); }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        extractBackground(context, mouseX, mouseY, delta);
        super.extractRenderState(context, mouseX, mouseY, delta);
        context.centeredText(font, title, width / 2, 8, 0xFFFFFFFF);
        context.centeredText(font,
                Component.translatable("trajectoryplus.page." + page), width / 2, 28, 0xFFBBBBBB);
        if (page == 2) {
            int full = Math.min(460, width - 24);
            int x = width / 2 - full / 2;
            int y = Math.max(44, height / 2 - 102) + 88;
            context.text(font, Component.translatable("trajectoryplus.setting.lineColor"),
                    x, y, 0xFFFFFFFF);
            context.text(font, Component.translatable("trajectoryplus.setting.impactMarkerColor"),
                    x + (full + 8) / 2, y, 0xFFFFFFFF);
        }
        if (badLineColor || badMarkerColor) {
            context.centeredText(font, Component.translatable("trajectoryplus.error.hex"),
                    width / 2, height - 40, 0xFFFF6666);
        }
    }
}
