@echo off
setlocal enabledelayedexpansion
set "ROOT=%~dp0"
set "GRADLE_ZIP=%ROOT%.gradle-bootstrap\gradle-8.12-bin.zip"
set "GRADLE_HOME=%ROOT%.gradle-bootstrap\gradle-8.12"
if not exist "%GRADLE_HOME%\bin\gradle.bat" (
  if not exist "%ROOT%.gradle-bootstrap" mkdir "%ROOT%.gradle-bootstrap"
  echo Downloading Gradle 8.12 from official Gradle distribution service...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $u='https://services.gradle.org/distributions/gradle-8.12-bin.zip'; Invoke-WebRequest -Uri $u -OutFile '%GRADLE_ZIP%'; Expand-Archive -LiteralPath '%GRADLE_ZIP%' -DestinationPath '%ROOT%.gradle-bootstrap' -Force"
  if errorlevel 1 (echo Gradle download failed. Internet access required.& exit /b 1)
)
call "%GRADLE_HOME%\bin\gradle.bat" -p "%ROOT%" %*
exit /b %errorlevel%
