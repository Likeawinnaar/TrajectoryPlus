# Trajectory+ — aan de slag

**Belangrijk:** dit is de volledige ontwikkelmap van een Minecraft Fabric-mod. De code is nog **niet gecompileerd of in Minecraft getest**. Er zit daarom geen zogenaamd werkende JAR in.

## Bouwen op Windows 11 Home

1. Installeer JDK 21 en controleer in PowerShell: `java -version`.
2. Pak de ZIP uit in een normale map.
3. Open PowerShell in die map.
4. Voer `./gradlew.bat build` uit. De starter downloadt eenmalig Gradle 8.12. De build downloadt Fabric en Minecraft-afhankelijkheden. Internet is vereist.
5. Als de build slaagt, staat je modbestand in `build/libs/trajectoryplus-1.0.0.jar`.
6. Gebruik een Fabric 1.21.1-installatie met Fabric API en Mod Menu. Plaats de JAR in je `mods`-map.
7. Open **Mod Menu → Trajectory+ → Configure**. Druk op **P** om de voorspelling aan of uit te zetten.

De instellingen bestaan uit vier pagina's: Algemeen, Projectielen, Uiterlijk en Geavanceerd. Met **Done** sla je wijzigingen op; **Cancel** sluit zonder op te slaan.

## Wat is wel getest?

De numerieke hulpfuncties en JSON-bestanden zijn gecontroleerd. Minecraft zelf, de menu-integratie en de 3D-weergave zijn hier niet getest. Volledige voortgang vind je in `PHASE_STATUS.md`, `TEST_REPORT.md` en `BUILD_REPORT.md`.
