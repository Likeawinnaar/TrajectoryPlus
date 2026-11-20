# Trajectory+

**See where your projectiles are predicted to land before you throw or shoot.**

Trajectory+ is a lightweight, client-side Minecraft Fabric mod for estimating the flight of supported projectiles and showing their predicted impact location in the Minecraft world.

> **Development candidate:** The mod is under active development. Prediction accuracy and in-game behavior may differ from actual results, and features require testing with the release build.

## Features

### Projectile prediction

Trajectory+ includes prediction code for:

- Ender pearls
- Bows
- Crossbows loaded with arrows
- Snowballs
- Eggs
- Tridents
- Splash potions
- Lingering potions
- Experience bottles

### Predicted landing and impact points

- **Bow:** A small impact marker is shown **without a trajectory line** to keep aiming clear.
- **Other supported projectiles:** A simulated curved flight path and predicted impact marker can be displayed.
- Predictions update with camera direction, player movement, and applicable charging/draw state.
- Uses individual projectile speed, drag and gravity parameters.

### Configuration through Mod Menu

Options represented in the code include:

- Enable or disable Trajectory+
- Enable or disable trajectory lines and impact markers
- Marker shape, color, opacity and size
- Trajectory color, opacity, line thickness and style
- Per-projectile enable/disable settings
- Maximum prediction distance
- A configurable toggle key (default: P)

### Client-side

- Designed for Minecraft Java Edition **1.21.1 Fabric**
- Does not require a server-side installation
- Does not change projectile physics or automatically aim/shoot
- Does not add blocks, items, accounts, or online services

## Dependencies

- Fabric Loader compatible with Minecraft 1.21.1
- Fabric API
- Mod Menu (for configuration)

## Limitations

Trajectory+ provides **estimates, not guaranteed hits or teleport locations**. Random projectile spread, moving entities, player changes, unloaded chunks, server lag, plugins, and server physics differences can all affect results.

Special cases such as firework crossbows and multiple-projectile spread are not fully predicted. Some servers may restrict projectile prediction mods; please follow server rules.

**Status:** This text documents the features represented in the source project. Check the actual release JAR and in-game functionality before publishing a production-ready Modrinth release.

---

*Trajectory+ — Predict the impact, not just the path.*
