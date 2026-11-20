# Build report — 2026-11-20

Environment: Linux development container, OpenJDK 21.0.12, no preinstalled Gradle, no cached Fabric or Minecraft libraries. DNS/network access to `maven.fabricmc.net`, `services.gradle.org` and Modrinth was unavailable.

- Fabric 1.21.1 Gradle project **created**.
- Fabric/Yarn/API and Mod Menu coordinates checked against published source docs; dependency download **not attempted by Gradle** because the environment cannot resolve download hosts.
- Gradle build **ATTEMPTED** in the container on 2026-11-20; it failed before compilation because the Gradle distribution download could not resolve `services.gradle.org` (curl exit status 6).
- Minecraft client **NOT LAUNCHED**.
- Remapped Minecraft JAR **NOT CREATED**. No JAR is described as compiled or tested.
- Pure Java numerical smoke test: see `TEST_REPORT.md`.

Run `gradlew.bat build` on a Windows machine with internet and JDK 21. If compilation fails, examine the first compiler diagnostic; resolve it before proceeding to launch and in-game validation. Re-run both `build` and `runClient` after fixes.

Gradle bootstrap scripts fetch Gradle 8.12. The source folder can be zipped without downloaded binaries.

## Alternative build automation

`.github/workflows/build-jar.yml` was added to attempt the real Gradle build in GitHub Actions (Java 21). This workflow has not been executed in the current environment; its success is not guaranteed. See `BUILD_JAR_GITHUB.md`.
