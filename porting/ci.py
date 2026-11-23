"""Resolve exact toolchains and check the packaged Fabric runtime JAR."""

import argparse
import json
from pathlib import Path
from zipfile import ZipFile

MANIFEST = json.loads(Path(__file__).with_name("targets.json").read_text())
BASELINE = MANIFEST["baseline"]["minecraft"]
TARGETS = {
    target["minecraft"]: target
    for target in [MANIFEST["baseline"], *MANIFEST["versions"]]
}


def mod_version(minecraft):
    if minecraft == BASELINE:
        return "1.0.0"
    return f'{MANIFEST["mod_version"]}+mc{minecraft}'


def jar_name(minecraft):
    return f"trajectoryplus-{mod_version(minecraft)}.jar"


def matrix():
    return {"include": [
        {"minecraft": version, "java": target["java"], "gradle": target["gradle"], "jar": jar_name(version)}
        for version, target in TARGETS.items()
    ]}


def verify(minecraft, path):
    if path.name != jar_name(minecraft):
        raise ValueError(f"Expected {jar_name(minecraft)}, got {path.name}")
    with ZipFile(path) as jar:
        corrupt = jar.testzip()
        if corrupt:
            raise ValueError(f"Corrupt JAR member: {corrupt}")
        metadata = json.loads(jar.read("fabric.mod.json"))
        expected = {
            "id": "trajectoryplus",
            "version": mod_version(minecraft),
            "environment": "client",
            "name": "Trajectory+",
            "icon": "assets/trajectoryplus/icon.png",
        }
        for key, value in expected.items():
            if metadata.get(key) != value:
                raise ValueError(f"Expected {key}={value!r}, got {metadata.get(key)!r}")
        if metadata["depends"]["minecraft"] != minecraft:
            raise ValueError(f"Incorrect Minecraft dependency: {metadata['depends']['minecraft']}")
        if metadata["depends"]["java"] != f'>={TARGETS[minecraft]["java"]}':
            raise ValueError(f"Incorrect Java dependency: {metadata['depends']['java']}")
        if not jar.read(metadata["icon"]).startswith(b"\x89PNG\r\n\x1a\n"):
            raise ValueError("The mod icon must be a PNG")
        for language in ("en_us", "nl_nl"):
            json.loads(jar.read(f"assets/trajectoryplus/lang/{language}.json"))
        for entrypoint in ("client", "modmenu"):
            classes = metadata["entrypoints"][entrypoint]
            if not classes:
                raise ValueError(f"Missing {entrypoint} entrypoint")
            for name in classes:
                class_file = jar.read(name.replace(".", "/") + ".class")
                if not class_file.startswith(b"\xca\xfe\xba\xbe"):
                    raise ValueError(f"Invalid compiled class: {name}")
    print(f"Verified {path.name} for Minecraft {minecraft}")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    commands = parser.add_subparsers(dest="command", required=True)
    commands.add_parser("matrix")
    gradle = commands.add_parser("gradle")
    gradle.add_argument("minecraft", choices=TARGETS)
    check = commands.add_parser("verify")
    check.add_argument("minecraft", choices=TARGETS)
    check.add_argument("jar", type=Path)
    args = parser.parse_args()
    if args.command == "matrix":
        print("matrix=" + json.dumps(matrix(), separators=(",", ":")))
    elif args.command == "gradle":
        print(TARGETS[args.minecraft]["gradle"])
    else:
        verify(args.minecraft, args.jar)


if __name__ == "__main__":
    main()
