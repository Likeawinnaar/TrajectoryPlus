# Trajectory+ test report

[GitHub Actions run 45147458326](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326) passed for all 15 port targets and the
Minecraft 1.21.1 baseline at source commit `15a96b827f76`. Each job
compiled against its own target, ran the physics and trajectory regression tests,
verified the runtime JAR's mod metadata, entrypoints, icon and translations, and
started Minecraft under Xvfb with that packaged JAR. Artifacts expire according to
the workflow's retention setting. Use a later successful run if a download has expired.

| Minecraft | Java | Gradle | Loom | Fabric Loader | Fabric API | Mod Menu | Actions job | Runtime artifact |
| --- | ---: | --- | --- | --- | --- | --- | --- | --- |
| 1.21.1 | 21 | 8.14.3 | 1.11.8 | 0.16.14 | `0.116.7+1.21.1` | 11.0.4 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268562) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14803909742) |
| 1.21.2 | 21 | 8.14.3 | 1.11.8 | 0.20.1 | `0.106.1+1.21.2` | 12.0.1 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268571) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14805255974) |
| 1.21.3 | 21 | 8.14.3 | 1.11.8 | 0.20.1 | `0.114.1+1.21.3` | 12.0.1 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268613) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14805051450) |
| 1.21.4 | 21 | 8.14.3 | 1.11.8 | 0.20.1 | `0.119.4+1.21.4` | 13.0.4 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268492) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14804024211) |
| 1.21.5 | 21 | 8.14.3 | 1.11.8 | 0.20.1 | `0.128.2+1.21.5` | 14.0.2 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268634) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14805455585) |
| 1.21.6 | 21 | 8.14.3 | 1.11.8 | 0.20.1 | `0.128.2+1.21.6` | 15.0.2 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268491) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14804881656) |
| 1.21.7 | 21 | 8.14.3 | 1.11.8 | 0.20.1 | `0.129.0+1.21.7` | 15.0.2 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268608) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14804841796) |
| 1.21.8 | 21 | 8.14.3 | 1.11.8 | 0.20.1 | `0.136.1+1.21.8` | 15.0.2 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268522) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14805076852) |
| 1.21.9 | 21 | 8.14.3 | 1.11.8 | 0.20.1 | `0.134.1+1.21.9` | 16.0.1 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268794) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14804723030) |
| 1.21.10 | 21 | 8.14.3 | 1.11.8 | 0.20.1 | `0.138.4+1.21.10` | 16.0.1 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268702) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14804946743) |
| 1.21.11 | 21 | 9.4.1 | 1.16.3 | 0.20.1 | `0.141.6+1.21.11` | 17.0.1 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268557) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14805251069) |
| 26.1 | 25 | 9.8.2 | 1.19.4 | 0.20.1 | `0.145.1+26.1` | 18.0.2 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268655) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14804662008) |
| 26.1.1 | 25 | 9.8.2 | 1.19.4 | 0.20.1 | `0.145.4+26.1.1` | 18.0.2 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268555) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14804677790) |
| 26.1.2 | 25 | 9.8.2 | 1.19.4 | 0.20.1 | `0.155.3+26.1.2` | 18.0.2 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268587) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14805540613) |
| 26.2 | 25 | 9.8.2 | 1.19.4 | 0.20.1 | `0.161.0+26.2` | 20.0.3 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268511) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14805006872) |
| 26.3 | 25 | 9.8.2 | 1.19.4 | 0.20.1 | `0.166.2+26.3` | 21.0.1 | [Passed](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/job/137600268521) | [JAR](https://github.com/Likeawinnaar/TrajectoryPlus/actions/runs/45147458326/artifacts/14805525737) |

## Gameplay checks

The startup check confirms that Fabric initializes Trajectory+ and that Minecraft
loads its GUI assets. The regression tests exercise each projectile profile
against a simulated wall, verify bow impact-marker-only geometry, and check
distance and unloaded-chunk limits.

A local Minecraft 26.3 check with the packaged JAR displayed all nine projectile
previews in a creative superflat world. It confirmed the bow's impact-only marker,
ender-pearl rendering in first and third person, the P toggle, and navigation
through the Mod Menu settings. Projectile accuracy against actual hits and
in-world visuals on the other ported versions remain unverified.
