#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MINECRAFT=1.21.1
for arg in "$@"; do
  case "$arg" in
    -Pminecraft_version=*) MINECRAFT="${arg#*=}" ;;
  esac
done
GRADLE_VERSION="$(python3 "$ROOT/porting/ci.py" gradle "$MINECRAFT")"
BOOTSTRAP="$ROOT/.gradle-bootstrap"
GRADLE_DIR="$BOOTSTRAP/gradle-$GRADLE_VERSION"
if [[ ! -x "$GRADLE_DIR/bin/gradle" ]]; then
  mkdir -p "$BOOTSTRAP"
  ZIP="$(mktemp "$BOOTSTRAP/gradle-$GRADLE_VERSION.XXXXXX")"
  trap 'rm -f "$ZIP"' EXIT
  curl -fL --retry 2 "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip" -o "$ZIP"
  unzip -qo "$ZIP" -d "$BOOTSTRAP"
  rm -f "$ZIP"
  trap - EXIT
fi
exec "$GRADLE_DIR/bin/gradle" -p "$ROOT" "$@"
