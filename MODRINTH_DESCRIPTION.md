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

- Built for Minecraft Java Edition with Fabric
- Does not require a server-side installation
- Does not change projectile physics or automatically aim/shoot
- Does not add blocks, items, accounts, or online services

## Installation

1. Select the Trajectory+ JAR for the exact Minecraft release in your Fabric instance. Use the
   [GitHub Actions builds](https://github.com/Likeawinnaar/TrajectoryPlus/actions/workflows/build-jar.yml)
   to download a successful build for that version.
2. Install Fabric Loader, Fabric API, and Mod Menu for the same Minecraft release. The
   [build manifest](porting/targets.json) lists the dependency versions used by CI.
3. Put the Trajectory+ JAR, Fabric API, and Mod Menu in the instance's `mods` directory.
   Use Java 21 for Minecraft 1.21.x and Java 25 for 26.x.
4. Open **Mods → Trajectory+ → Configure** to change settings. The default toggle is **P**.

## Limitations

Trajectory+ provides **estimates, not guaranteed hits or teleport locations**. Random projectile spread, moving entities, player changes, unloaded chunks, server lag, plugins, and server physics differences can all affect results.

Special cases such as firework crossbows and multiple-projectile spread are not fully predicted. Some servers may restrict projectile prediction mods; please follow server rules.

**Status:** This text documents the features represented in the source project. Check the actual release JAR and in-game functionality before publishing a production-ready Modrinth release.

---

*Trajectory+ — Predict the impact, not just the path.*
