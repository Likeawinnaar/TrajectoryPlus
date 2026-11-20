# Build Trajectory+ JAR without installing Gradle yourself

A GitHub Actions build job is provided in `.github/workflows/build-jar.yml`.

1. Create a GitHub repository and upload the contents of this project folder (including the hidden `.github` folder).
2. Open the repository **Actions** tab and select **Build Trajectory+ JAR**.
3. Run the workflow on your branch using **Run workflow** (or push to `main`/`master`).
4. If compilation succeeds, download the **TrajectoryPlus-1.0.0-jar** artifact from the job page and unzip it. The expected file is `trajectoryplus-1.0.0.jar`.

This workflow builds using Java 21 and downloads the Gradle / Fabric dependencies in the GitHub Actions runner. Successful completion is **not verified here**. A compile error in the source will also cause the GitHub job to fail; read the job logs rather than assuming a JAR was built.
