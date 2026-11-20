# Test report — 2026-11-20

Statuses refer only to tests actually executed. Source-code presence is not proof of runtime behavior.

| Test | Status | Evidence / outstanding action |
|---|---|---|
| Physics helper: drag, gravity, distance limit | PASS | `./offline-test.sh`: eight offline numerical checks |
| Config JSON syntax of sample and localization files | PASS | `python json.loads` validates both languages, sample config and expanded mod descriptor |
| Minecraft 1.21.1 Gradle compilation | NOT EXECUTED | Missing Gradle dependencies/network |
| Minecraft 1.21.1 game launch | NOT EXECUTED | Requires client and remapped JAR |
| Ender pearl, walking/running/jumping comparisons | NOT EXECUTED | Compare actual/predicted impact in-game |
| Bow partial/half/full draw and changing angles | NOT EXECUTED | Compare actual/predicted impact in-game |
| Crossbow / snowball / egg / trident / potions / XP bottle | NOT EXECUTED | Validate individually in-game |
| Thin shapes, slabs, stairs, fences and panes | NOT EXECUTED | In-game tests required |
| Unloaded chunks and dimension change | NOT EXECUTED | Runtime test required |
| Third-person camera and GUI scaling | NOT EXECUTED | Runtime test required |
| Mod Menu Configure and navigation | NOT EXECUTED | Runtime test required |
| Save/Cancel/Reset persistence | NOT EXECUTED | Runtime test required |
| LAN/vanilla/Fabric multiplayer | NOT EXECUTED | Requires compatible test server |
| Performance / FPS profiling | NOT EXECUTED | Requires test client |

No accuracy measurements or render-success claims have been fabricated.
