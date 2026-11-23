"""Launch the packaged JAR with Fabric in a virtual display and check startup."""

import os
from pathlib import Path
import re
import signal
import subprocess
import sys
import time

ASSET_ATLAS = re.compile(r"Created: .*textures/atlas/gui\.png")
CRASH = re.compile(r"Minecraft has crashed|Crash report saved to|Exception in thread \"Render thread\"")


def smoke(minecraft):
    log = Path("build/client-smoke-launch.log")
    config = Path("build/client-smoke/config/trajectoryplus.json")
    command = [
        "xvfb-run", "-a", "-s", "-screen 0 1280x720x24",
        "gradle", "--no-daemon", "--console=plain", f"-Pminecraft_version={minecraft}",
        "runProductionClient",
    ]
    env = dict(os.environ, ALSOFT_DRIVERS="null", LIBGL_ALWAYS_SOFTWARE="1")
    with log.open("w") as output:
        process = subprocess.Popen(command, start_new_session=True, stdout=output,
                                   stderr=subprocess.STDOUT, env=env)
        try:
            deadline = time.monotonic() + 300
            ready_at = None
            while time.monotonic() < deadline:
                contents = log.read_text(errors="replace")
                if process.poll() is not None or CRASH.search(contents):
                    raise RuntimeError("Minecraft exited during startup")
                if config.is_file() and ASSET_ATLAS.search(contents):
                    ready_at = ready_at or time.monotonic()
                    if time.monotonic() - ready_at >= 10:
                        print(f"Minecraft {minecraft} initialized Fabric, Trajectory+, and GUI assets.")
                        return
                time.sleep(1)
            raise RuntimeError("Timed out before Minecraft completed startup")
        except RuntimeError:
            print(log.read_text(errors="replace"), file=sys.stderr)
            raise
        finally:
            if process.poll() is None:
                os.killpg(process.pid, signal.SIGTERM)
                try:
                    process.wait(timeout=10)
                except subprocess.TimeoutExpired:
                    os.killpg(process.pid, signal.SIGKILL)
                    process.wait()


if __name__ == "__main__":
    smoke(sys.argv[1])
