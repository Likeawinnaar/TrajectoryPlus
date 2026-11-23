# Changelog

## Unreleased

- Added per-version Fabric builds with exact Minecraft metadata, using Yarn on 1.21.x and official Minecraft names on 26.x.
- Added individual runtime JAR verification and client startup checks in GitHub Actions.
- Shared trajectory prediction and impact geometry across ports and capped collision checks at the configured prediction distance.

- Added the user-provided bow-and-crosshair artwork as Trajectory+'s in-game mod icon (128×128 transparent PNG).
- Bow prediction is **impact-marker only**: the predicted location remains visible, but the trajectory line no longer obstructs aiming.
- Added a ready-to-edit Modrinth project description.

## 1.0.0 — development candidate (2026-11-20)

- Added client-side mod entrypoint and P toggle.
- Implemented discrete 3D path simulation and block voxel raycasting for nine projectile categories.
- Added three line patterns and three impact marker shapes.
- Implemented configuration pages, Mod Menu entrypoint, JSON validation and English/Dutch language strings.
- Added Java numerical smoke test and release-readiness checklists.
- **Not a published release: Minecraft Gradle compilation, launch and gameplay tests remain unverified.**
