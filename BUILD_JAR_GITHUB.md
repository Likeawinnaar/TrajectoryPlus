# Building Trajectory+ JARs

GitHub Actions runs an independent build for each exact Minecraft version in
[`porting/targets.json`](porting/targets.json), including the 1.21.1 baseline.
Each job compiles against its pinned dependencies, runs the prediction tests,
checks the runtime JAR's version and resources, and launches Minecraft with
that JAR under a virtual display. Successful jobs upload their own runtime JAR.

Open the [Actions run list](https://github.com/Likeawinnaar/TrajectoryPlus/actions/workflows/build-jar.yml),
select a run, and download the artifact for your exact Minecraft version.
Unzip the download, then copy the enclosed JAR to your instance's `mods` directory.
See [installation instructions](MODRINTH_DESCRIPTION.md#installation) for dependencies.

To build locally, install Python 3 and the JDK shown in the manifest, then run:

```sh
./gradlew -Pminecraft_version=1.21.4 clean build
./gradlew -Pminecraft_version=26.3 clean build
```

Use Java 21 for the first command and Java 25 for the second. Each invocation
selects the target's pinned Gradle and Loom versions. Choose `gradlew.bat` on
Windows with the Python launcher installed. Omitting `-Pminecraft_version`
builds the 1.21.1 baseline. Find the runtime JAR in `build/libs/`; the sources
JAR is for development.

## Verification scope

The startup test launches Minecraft with the remapped Yarn runtime JAR or the
officially named 26.x runtime JAR. It checks that the mod initializes and GUI
assets finish loading. The simulation tests cover the supported projectile
profiles, block-impact boundaries, and the bow's impact-only geometry.
Visual checks in a playable world are tracked separately from startup.
