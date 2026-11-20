#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
HOME_DIR="$ROOT/.gradle-bootstrap/gradle-8.12"
if [[ ! -x "$HOME_DIR/bin/gradle" ]]; then
  mkdir -p "$ROOT/.gradle-bootstrap"
  curl -fL --retry 2 'https://services.gradle.org/distributions/gradle-8.12-bin.zip' -o "$ROOT/.gradle-bootstrap/gradle-8.12-bin.zip"
  unzip -qo "$ROOT/.gradle-bootstrap/gradle-8.12-bin.zip" -d "$ROOT/.gradle-bootstrap"
fi
exec "$HOME_DIR/bin/gradle" -p "$ROOT" "$@"
