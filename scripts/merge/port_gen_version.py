"""Merge phase 2 fan-out: copy one Minecraft version of the generator mod into this repo.

usage: python scripts/merge/port_gen_version.py <version folder, e.g. v1_21_11> [<gen repo>]

Run from this repo's root. The gen repo defaults to ../AronaLayers-gen and must be on branch
`merge` at commit a124056 (only files tracked by git are copied).

It copies the version's own sources (vX/common, vX/fabric, vX/neoforge) and its mixin config, with
the same renames the 1.21.1 pilot used. The shared sources (root src/) were copied by the pilot and
are not touched. Existing files are never overwritten. Bytes are copied as they are, so line
endings are kept.

What is left to do by hand for the version is listed in docs/MERGE_PLAN.md ("Recipe for the other
versions").
"""
import os
import subprocess
import sys

OLD = "io/arona74/aronalayersgen/"
NEW = "io/arona74/aronalayers/gen/"
BASE = "io/arona74/aronalayers/"

# Already copied for every version by the pilot (the shared ModBlocks registers the block).
ALREADY_THERE = {"block/PowderSnowLayerBlock.java"}

# Ordered text replacements for Java sources.
JAVA = [
    ("io.arona74.aronalayersgen.api", "io.arona74.aronalayers.api"),
    ("io.arona74.aronalayersgen.block.LayerBlock", "io.arona74.aronalayers.custom_blocks.ConvertingLayerBlock"),
    ("io.arona74.aronalayersgen.block", "io.arona74.aronalayers.custom_blocks"),
    ("io.arona74.aronalayersgen.mixin.EntityTouchingPowderSnowAccessor", "io.arona74.aronalayers.mixin.EntityTouchingPowderSnowAccessor"),
    ("io.arona74.aronalayersgen", "io.arona74.aronalayers.gen"),
    ("assets/aronalayersgen/lang/en_us.json", "assets/aronalayers_config/lang/en_us.json"),
    ("assets/aronalayersgen/", "assets/aronalayers/"),
    ("config.aronalayersgen.", "config.aronalayers."),
    ("aronalayersgen$", "aronalayers$"),
]


def main():
    if len(sys.argv) < 2:
        sys.exit(__doc__)
    version = sys.argv[1].strip("/\\")
    gen = sys.argv[2] if len(sys.argv) > 2 else "../AronaLayers-gen"

    def tracked(prefix):
        out = subprocess.run(["git", "-C", gen, "ls-files", prefix], capture_output=True, text=True, check=True).stdout
        return [line for line in out.splitlines() if line]

    def read(path):
        with open(os.path.join(gen, path), "rb") as f:
            return f.read()

    def write(path, data):
        if os.path.exists(path):
            print("  kept (exists):", path)
            return
        os.makedirs(os.path.dirname(path), exist_ok=True)
        with open(path, "wb") as f:
            f.write(data)

    def java(data):
        text = data.decode("utf-8")
        for old, new in JAVA:
            text = text.replace(old, new)
        return text.encode("utf-8")

    for part in ("common", "fabric", "neoforge"):
        root = f"{version}/{part}/src/main/java"
        count = 0
        for path in tracked(root + "/"):
            rel = path[len(root) + 1:]
            if not rel.startswith(OLD) or not rel.endswith(".java"):
                sys.exit("unexpected file " + path)
            inner = rel[len(OLD):]
            if inner in ALREADY_THERE:
                continue
            write(os.path.join(root, NEW + inner), java(read(path)))
            count += 1
        print(root, count)

    # The mixin config. The powder snow accessor is in the shared aronalayers.mixins.json now.
    config = f"{version}/common/src/main/resources/aronalayersgen.mixins.json"
    data = read(config).replace(b"io.arona74.aronalayersgen.mixin", b"io.arona74.aronalayers.gen.mixin")
    for line_end in (b"\r\n", b"\n"):
        data = data.replace(b'    "EntityTouchingPowderSnowAccessor",' + line_end, b"")
    if b"EntityTouchingPowderSnowAccessor" in data:
        sys.exit("could not remove EntityTouchingPowderSnowAccessor from the mixin config")
    write(f"{version}/common/src/main/resources/aronalayers.gen.mixins.json", data)

    print("Not copied (see the recipe): fabric.mod.json, neoforge.mods.toml, build.gradle, and under")
    print(f"{version}/common/src/main/resources: " + ", ".join(
        p[len(f"{version}/common/src/main/resources/"):] for p in tracked(f"{version}/common/src/main/resources/")
        if not p.endswith("aronalayersgen.mixins.json")))


if __name__ == "__main__":
    main()
