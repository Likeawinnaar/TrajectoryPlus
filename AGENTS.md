# Trajectory+ repository instructions for coding agents

## Current assignment
Implement issue #1: https://github.com/Likeawinnaar/TrajectoryPlus/issues/1
Port Trajectory+ to **all 15** target Minecraft releases listed in `porting/targets.json` and deliver individually compiled and validated Fabric JARs. Minecraft 1.21.1 must remain supported.

## Rules
1. Read all existing sources, gradle files, client entrypoint, GUI, rendering, tests, and resources before editing.
2. Maintain a shared core for prediction maths and config, and version-specific adapters for Fabric/Minecraft APIs that break between releases.
3. Do not use imaginary dependency versions. Query actual Fabric Maven and Mod Menu artifact metadata, pin versions per target and verify that each compilation succeeds.
4. Minecraft 26.1+ needs Java 25, Mojang official mappings and current Loom. The 1.21.x code uses Yarn. Use separate compatibility builds when needed; do not replace the baseline with source that cannot build older targets.
5. Keep original Fabric mod id `trajectoryplus`, mod icon, and translation resources. Keep bow predictions as **impact marker only**; other projectiles may display flight paths.
6. Make a dedicated CI build job/artifact for every exact target, with a separately versioned runtime jar (never a dev or sources jar). CI must fail on failed compilation, missing jar or wrong mod metadata.
7. Validate `fabric.mod.json` inside each built JAR and exact Minecraft dependency. Run tests. Do not declare in-game testing successful without actually running Minecraft.
8. Never claim a requested version is released or compatible until a green CI build exists. Check `porting/targets.json` for the full list, and update statuses only when verified.
9. Keep PRs reviewable; describe real failures and blocked versions rather than skipping them.

## Deliverables
- Working source compatibility layers for all targets.
- `./gradlew` or equivalent repeatable build entrypoint per target.
- GitHub Actions matrix for per-target testing and separate downloadable artifacts.
- Evidence of successful build for each listed version, metadata checks and clean changelog.
- Documentation on each target's Java version, Fabric Loader, Fabric API, Mod Menu and tested support.

## Porting checklist
- [x] Minecraft 1.21.2: port, compile, verify JAR metadata, upload artifact
- [x] Minecraft 1.21.3: port, compile, verify JAR metadata, upload artifact
- [x] Minecraft 1.21.4: port, compile, verify JAR metadata, upload artifact
- [x] Minecraft 1.21.5: port, compile, verify JAR metadata, upload artifact
- [x] Minecraft 1.21.6: port, compile, verify JAR metadata, upload artifact
- [x] Minecraft 1.21.7: port, compile, verify JAR metadata, upload artifact
- [x] Minecraft 1.21.8: port, compile, verify JAR metadata, upload artifact
- [x] Minecraft 1.21.9: port, compile, verify JAR metadata, upload artifact
- [x] Minecraft 1.21.10: port, compile, verify JAR metadata, upload artifact
- [x] Minecraft 1.21.11: port, compile, verify JAR metadata, upload artifact
- [x] Minecraft 26.1: port, compile, verify JAR metadata, upload artifact
- [x] Minecraft 26.1.1: port, compile, verify JAR metadata, upload artifact
- [x] Minecraft 26.1.2: port, compile, verify JAR metadata, upload artifact
- [x] Minecraft 26.2: port, compile, verify JAR metadata, upload artifact
- [x] Minecraft 26.3: port, compile, verify JAR metadata, upload artifact
