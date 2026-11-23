@echo off
setlocal
set "ROOT=%~dp0"
set "MINECRAFT=1.21.1"
for %%A in (%*) do call :target "%%~A"
for /f "delims=" %%V in ('py -3 "%ROOT%porting\ci.py" gradle "%MINECRAFT%"') do set "GRADLE_VERSION=%%V"
if not defined GRADLE_VERSION exit /b 1
set "GRADLE_DIR=%ROOT%.gradle-bootstrap\gradle-%GRADLE_VERSION%"
if not exist "%GRADLE_DIR%\bin\gradle.bat" (
  if not exist "%ROOT%.gradle-bootstrap" mkdir "%ROOT%.gradle-bootstrap"
  powershell -NoProfile -Command "$ErrorActionPreference='Stop'; $zip=Join-Path $env:TEMP ([guid]::NewGuid().ToString()+'.zip'); try { Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile $zip; Expand-Archive -LiteralPath $zip -DestinationPath '%ROOT%.gradle-bootstrap' -Force } finally { Remove-Item -LiteralPath $zip -ErrorAction SilentlyContinue }"
  if errorlevel 1 exit /b 1
)
call "%GRADLE_DIR%\bin\gradle.bat" -p "%ROOT%" %*
exit /b %errorlevel%

:target
set "ARG=%~1"
if defined NEXT_IS_MINECRAFT (
  set "MINECRAFT=%ARG%"
  set "NEXT_IS_MINECRAFT="
) else if "%ARG%"=="-Pminecraft_version" (
  set "NEXT_IS_MINECRAFT=1"
) else if "%ARG:~0,20%"=="-Pminecraft_version=" (
  set "MINECRAFT=%ARG:~20%"
)
exit /b 0
