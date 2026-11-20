# Required eight-phase completion report

| Phase | Status | Main created files | Build / test evidence | Remaining work |
|---|---|---|---|---|
| 1. Project setup | Partially Completed | `build.gradle`, `gradle.properties`, `settings.gradle`, `gradlew*`, `fabric.mod.json` | Static configuration only | Run real Gradle build |
| 2. Ender pearl prediction | Partially Completed | `ProjectileResolver`, `TrajectoryPredictor`, `CollisionResolver`, renderers | Source implemented; no in-game test | Verify vanilla accuracy |
| 3. Bow prediction | Partially Completed | `ProjectileResolver`, `ProjectileProfile` | Source implemented; no in-game test | Verify variable draw and arrow launch |
| 4. Additional projectiles | Partially Completed | `ProjectileProfile`, `ProjectileResolver` | Source profiles added | Validate individually; firework/multishot unsupported |
| 5. Mod Menu and settings | Partially Completed | `TrajectorySettingsScreen`, `TrajectoryModMenu`, `ConfigManager`, languages | API structure inspected; no client launch | Verify every setting and persistence in-game |
| 6. Optimization | Partially Completed | `TrajectoryPredictor`, renderers | Tick budget and cached result written | Measure FPS/CPU and improve if necessary |
| 7. Quality assurance | Partially Completed | `PhysicsMathSelfTest`, `TEST_REPORT.md` | Eight offline helper checks and JSON validation | Build/client/in-game tests |
| 8. Release preparation | Partially Completed | `README.md`, `CHANGELOG.md`, `BUILD_REPORT.md` | Documentation written | Rebuild, test, verify Modrinth eligibility before release |

Every phase was attempted in order. None is marked Completed without real end-to-end verification.
