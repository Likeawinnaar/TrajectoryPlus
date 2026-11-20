#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p build/offline-test
javac -d build/offline-test src/main/java/com/trajectoryplus/prediction/PhysicsMath.java src/test/java/com/trajectoryplus/PhysicsMathSelfTest.java
java -cp build/offline-test com.trajectoryplus.PhysicsMathSelfTest
