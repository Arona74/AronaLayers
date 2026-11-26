# Merge plan: AronaLayers-gen + AronaLayers-extras into AronaLayers

Written 2026-11-13. This file is the hand-off between sessions: each session does one phase, ticks
it in the status table, and adds what it learned under "Notes from finished phases".

## Goal

One mod, id `aronalayers`, Fabric (6 versions) and NeoForge (5 versions, no 1.20.1), containing:

- the layer blocks (this repo today),
- terrain generation with layers (from `C:\GITHUB\AronaLayers-gen`, mod id `aronalayersgen`, 2.0.0a6),
- vanilla mechanics for layers (from `C:\GITHUB\AronaLayers-extras`, mod id `aronalayersextras`, 1.3.0, Fabric only today).

Conquest Reforged (CR) support is dropped everywhere. One config screen, based on the gen screen.
Generation settings become per-world data, editable from the Create World screen.

## Status

| # | Phase | Repo it happens in | Status |
|---|-------|--------------------|--------|
| 0 | Decisions confirmed by the user | — | done 2026-11-13 |
| 1 | Remove Conquest Reforged from gen | AronaLayers-gen | done 2026-11-13, gen branch `merge`, commit a124056 (see notes) |
| 2 | Move gen into AronaLayers (pilot 1.21.1, then the other versions) | AronaLayers | done 2026-11-13: pilot (1.21.1) and fan-out (1.20.1, 1.21.11, 26.1.2, 26.2, 26.3), see notes |
| 3 | Port extras into AronaLayers, Fabric + NeoForge | AronaLayers | done 2026-11-14: pilot (1.21.1) and fan-out (1.20.1, 1.21.11, 26.1.2, 26.2, 26.3), entry points folded, see notes |
| 4 | Config: one registry, new tab order, global vs world scope | AronaLayers | done 2026-11-15: two files, six tabs, `trees_on_layers` split, see notes |
| 5 | Generation config stored in world data | AronaLayers | done 2026-11-16: `<world>/aronalayers/generation.json`, `/aronalayers reload`, see notes |
| 6 | Generation settings button on the Create World screen | AronaLayers | done 2026-11-22: "Layers…" on the More tab, draft written as the new world's `generation.json`, see notes |
| 7 | Cleanup, README, CI, retire the two old mods | all three | done 2026-11-26: jars `aronalayers-<loader>-1.0.0-alpha.2+mc…` (also collected in `build/libs`), metadata, CI workflow removed, READMEs, LSL, see notes |

Phases 1 to 3 change no behaviour for a non-CR player; 4 to 6 are the new features. Each phase ends
with all 11 jars building, so the user can commit after every phase (the user commits, never Claude).

## What the analysis found

### Sizes

| Mod | Shared Java | Per-version Java | Notes |
|-----|-------------|------------------|-------|
| AronaLayers | ~40 files | Compat, CompatBlock, Platform, datagen per version | ~50,000 generated JSON files |
| gen | ~14,000 lines, 28 mixins + 1 client | Compat, 7 mixins, config-screen seams, 4 model classes per loader | `LayerPlacementHelper` alone is 3,013 lines |
| extras | ~1,300 lines, 7 mixins | Compat, 2 mixins (3 on 1.20.1) | flat layout `vX/`, no loader split |

Gen already uses the same nested layout as this repo (`vX/common`, `vX/fabric`, `vX/neoforge`, root
`src/main`, `src/fabric`, `src/neoforge`), the same Loom and ModDevGradle versions, and already runs
on NeoForge with native models. The move in phase 2 is therefore mostly mechanical.

### Name collisions if gen moved into the same packages

Both mods have `Compat`, `platform.Platform`, `block.LayerBlock`, `ModBlocks` and a per-version
`ClientSetup`. Phase 2 avoids them by moving gen to `io.arona74.aronalayers.gen.*` and extras to
`io.arona74.aronalayers.gameplay.*`, each keeping its own Compat and Platform. Merging the Compat and
Platform classes is optional work for phase 7, not a prerequisite.

### Gen's own blocks

`aronalayersgen:deepslate_layer` and `moss_layer` duplicate blocks this mod already has: delete them
and map to `aronalayers:*`. `powder_snow_layer`, `zinc_ore_layer` and `deepslate_zinc_ore_layer`
have no equivalent here: they move into this mod's `ModBlocks` and datagen under the `aronalayers`
namespace. Gen's reflection on `Layer3DBlock` and its `isModLoaded("aronalayers")` gates become
direct calls.

### Conquest Reforged footprint (to remove)

- Gen: 21 Java files mention it; `LayerPlacementHelper` (39 references), `LayerConfig` (37),
  `PlantMappingRegistry` (30) and `PlantConversionHelper` (22) hold most of it. Whole classes go:
  `EnhancedRockRegistry`, `EnhancedFoliageRegistry`, `RockMappingRegistry`, `FoliageMappingRegistry`,
  `NbtTreeRegistry`, `NbtTreeInjector`, and the six `cr_*.json` resources, `nbt_trees/`,
  `schematic_trees/`. Options that go: everything declared with `.mods("conquest")` (rocks, extra
  foliage, `replace_sea_grass`, `cr_nbt_trees`, its vanilla fallback), plus `place_wet_sand`.
  To check file by file in the session: `NbtTreeFeatureMixin`, `NbtTreeTemplateFeatureMixin`,
  `TellusProceduralTreeMixin`, `SaplingBlockGrowMixin` each mention CR once and may still be needed
  for trees on layers with TerraForged or Tellus.
- Extras: the wet sand feature is CR only and goes entirely (`WetSandHandler`, `enableWetSands`,
  `WetSandMappings`). The `conquest:*` ids in the grass, mycelium, sheep and falling handlers go, as
  does the `_rocks` suffix rule. Extras still uses the old `vanillalayerplus:*` ids: they become
  direct references to this mod's blocks.

### Extras on NeoForge

Extras needs very little from Fabric: a level tick end event, a player block break event, a creative
tab hook, the config directory, and Mod Menu + Cloth Config for its screen. The first four become
`Platform` methods with a NeoForge implementation per version (`LevelTickEvent.Post`,
`BlockEvent.BreakEvent`, `BuildCreativeModeTabContentsEvent`, `FMLPaths`). Cloth Config is dropped:
the options move to the gen-style registry. The six small ore items move to `aronalayers:small_*`;
their recipes, advancements and item models should come from this repo's datagen instead of
hand-copied files per version.

Overlap to resolve: this mod's `grass_layer` already has its own random tick and decay check, and
sand layers already fall (`FallingLayerBlock`). The extras handlers must be reconciled with those so
the behaviour is not applied twice.

### Config today

- Gen: `config/aronalayersgen/layer_config.json`. Every key is a `ConfigOption` declared in
  `LayerConfig` with default, range, tab, group, `Apply` (LIVE, NEW_CHUNKS, RESTART,
  RESOURCE_RELOAD) and `Side` (SERVER, CLIENT, BOTH). Values live in static fields read by worldgen
  threads. Tabs: WORLDGEN, STRUCTURES, VISUALS, GAMEPLAY, DEBUG. The screen uses vanilla widgets,
  with per-version seams (`ScreenCompat`, `OptionRows`, `PreviewPanel`; 1.20.1 has its own screen
  class). Five mapping JSON files sit next to it.
- Extras: `config/aronalayersextras.json`, a plain Gson object, 7 toggles and 2 collections.

Two things in the gen config do not fit "generation settings belong to the world":

1. `trees_on_layers` and `3D_layer_injection` are in the WORLDGEN tab but apply on RESTART, and
   `trees_on_layers` is read on the client too (stem models). A per-world value cannot require a
   restart or be unknown to a remote client. Each needs splitting into a global part (register the
   models) and a per-world part (generate that way), or an honest runtime check.
2. Static fields are fine with one server per JVM, but they must be reloaded at every world start
   and reset at world close, and nothing may read a generation value before a world is open.

### Licence

This repo is CC-BY-NC-4.0 (derived from Vanilla+ Layers by Fellter). Gen and extras are MIT, by the
user alone. Decided: both are kept, see the next section.

## Decisions to confirm (phase 0)

The user confirmed recommendations 1 to 5 on 2026-11-13 (no id migration, plain copy with the old
repos kept as archives, global mapping files, LayersApi moved and LSL updated, new names). Licence
(decided the same day): keep both. Blocks code and assets (everything derived from Vanilla+ Layers)
stay CC-BY-NC-4.0; the `gen` and `gameplay` packages stay MIT. Phase 7 ships both licence texts
(`LICENSE` + `LICENSE-MIT`), a README section naming which folders are under which, and metadata
licence `CC-BY-NC-4.0 AND MIT`. Phase 2 and 3 copy gen's and extras' MIT `LICENSE` in as
`LICENSE-MIT` right away.

Third-party code: gen's `THIRD_PARTY_NOTICES.md` was deleted in gen commit 5296078 (2.0.0a4,
2026-11-04) together with the code it covered, the Distant Horizons exact layer heights adapted
from Geostep (MIT), which moved to LSL. No such file exists in gen or extras today. If the user
knows of other borrowed code still in gen, a notices file must be recreated here in phase 2.

1. **Old ids in existing worlds** (`aronalayersgen:powder_snow_layer`, the two zinc layers,
   `aronalayersextras:small_*`). Recommended: no migration, as decided for the blocks rename. The
   alternative is registry aliases, cheap on 1.21+ and awkward on 1.20.1.
2. **Git history of gen and extras.** Recommended: plain copy, naming the source commit in the
   commit message; the old repos stay as archives. Alternative: `git subtree` import.
3. **Mapping files** (`al_block_mappings.json`, `plant_mappings.json`, cleanup and elevation
   lists). Recommended: stay global in `config/aronalayers/` for now; per-world copies only if needed
   later.
4. **LSL** calls `io.arona74.aronalayersgen.api.LayersApi` by reflection. Recommended: move the class
   to `io.arona74.aronalayers.api.LayersApi` and update LSL to try the new name first. The CR `layer`
   1-4 detection in that API goes with the rest of CR.
5. **Names.** Recommended: display name "AronaLayers", jars `aronalayers-<loader>-<ver>+mc<mc>.jar`
   (today `aronalayers-blocks-…`), version 1.0.0-alpha.2 after phase 3.
6. **1.20.1** stays Fabric only.

## Phases

### Phase 1 — Remove Conquest Reforged from gen (in the gen repo)

Done in place because the gen repo has a known-good build and test setup to compare against.

1. Baseline first: on 1.21.1 Fabric, with a fixed seed and no CR, record layer block counts for a few
   chunks (`clone … filtered <block> force` counting, see gen's NeoForge hand-off memory).
2. Delete the CR-only classes, resources and options listed above; then work through
   `LayerPlacementHelper`, `PlantMappingRegistry`, `PlantConversionHelper`, `PlantGrounding`,
   `LayerConfig`, `BlockMappingRegistry`, `ConfigLoader`, and the mixins.
3. Removed option keys go into `OBSOLETE_KEYS` so old files load without warnings.
4. Remove the lang keys and preview images of removed options.

Done when: 11 jars build, `python scripts/verify_mixins.py` passes, the baseline counts are
unchanged, a server starts on one obfuscated and one 26.x version.
Model: strongest model for step 2 (interleaved logic in a 3,000-line class); no subagents.

### Phase 2 — Move gen into AronaLayers

Pilot (one session, strongest model): 1.21.1 Fabric and NeoForge.

1. Copy gen's shared sources to `src/main/java/io/arona74/aronalayers/gen/…`, loader sources to
   `src/fabric` and `src/neoforge`, version sources to `v1_21_1/{common,fabric,neoforge}`; rename
   the package; mixin config becomes `aronalayers.gen.mixins.json` (gen has no mixin plugin any more).
   Source: gen branch `merge` at commit a124056; name it in the commit message.
2. Call gen's init from this mod's entry points; drop gen's own entry points and metadata; add its
   mixin config, Mod Menu entry point and client setup to `fabric.mod.json` / `neoforge.mods.toml`.
3. Blocks: delete the two duplicates, move powder snow and zinc layers into `ModBlocks` + datagen,
   update `al_block_mappings.json`, replace `Layer3D` reflection with direct calls.
4. Build files: bring over the Mod Menu stub exclusion, and for later versions the `sharedOverridden`
   staging list (26.3, and 1.20.1's config screen). Bring `scripts/verify_mixins.py`.
5. Lang keys `config.aronalayersgen.*` → `config.aronalayers.*`; config directory
   `config/aronalayers/`, importing an existing `config/aronalayersgen/` once.
6. `breaks` entries so the merged mod refuses to load next to `aronalayersgen` or `aronalayersextras`.

Fan-out (second session): versions 1.20.1, 1.21.11, 26.1.2, 26.2, 26.3. One subagent per version on
a cheaper model, each given the pilot diff as its recipe and touching only its own `vX/` folder.
Edits can run in parallel; Gradle builds must run one at a time (Loom cache locks). The orchestrating
session reviews, builds and runs the server tests.

Done when: 11 jars build, verify_mixins passes, datagen output differs only by the three moved
blocks, server test with worldgen layers on each version, client check by the user on 1.21.1.

### Phase 3 — Port extras

1. Copy to `io.arona74.aronalayers.gameplay`, without wet sand and without CR ids; handlers refer to
   `ModBlocks` fields instead of id strings.
2. Reconcile with existing block behaviour (grass layer tick, falling layers).
3. Platform additions per loader and version: level tick end, block break, config dir, tab entries.
4. Small ore items into `ModItems`, resources via datagen.
5. The seven remaining settings become `ConfigOption`s in the GAMEPLAY tab (the registry exists
   since phase 2); import `config/aronalayersextras.json` once.
6. Review the three `mixin/compat` classes (`MushroomVanillaMixin`, `StateWithMixin`,
   `BlockStateUpdateShapeMixin`): they may duplicate what gen's plant grounding already does.

Pilot 1.21.1 on both loaders with the strongest model; other versions by cheaper subagents, as in
phase 2. Server tests: grass spreads onto a dirt layer, sheep eat a grass layer, layers fall with
sand, ore layer drops with and without Silk Touch (`tick sprint` where available).

### Phase 4 — Config restructure

1. `ConfigTab` gets a scope: GLOBAL or WORLD. New order: VISUALS, GAMEPLAY, then the generation tabs
   (TERRAIN, PLANTS & TREES, STRUCTURES — the old WORLDGEN tab is too long once it is the "new world"
   page), then DEBUG.
2. Two files: `config/aronalayers/config.json` (global options) and
   `config/aronalayers/generation_defaults.json` (WORLD options: the defaults for new worlds).
3. Split the mixed options found in the analysis (`trees_on_layers`, `3D_layer_injection`).
4. Screen: generation tabs carry a "defaults for new worlds" heading instead of the seam warning
   when opened from the title screen.

Shared code mostly; the per-version seams should not change. One session, strongest model, then a
build of all versions.

### Phase 5 — Generation config in world data

1. Storage: `<world>/aronalayers/generation.json`, same format as the defaults file, hand-editable.
2. Lifecycle: load before any chunk can generate (Fabric `SERVER_STARTING`, NeoForge
   `ServerAboutToStartEvent`; verify both fire before spawn chunk generation on every version),
   reset to "no world" at server stopped. A world without the file gets a copy of the current
   defaults, so existing worlds keep generating as before.
3. In a world, the screen's generation tabs edit that world's file (host or singleplayer only, with
   the existing seam warning); outside a world they edit the defaults. Remote clients see them
   read-only, as today.
4. Dedicated servers: the file appears in the world folder at first start; add a reload command.
5. Audit every read of a WORLD-scoped field from the client or from block bootstrap paths
   (`GroundingFlags` shows this class of trap already exists).

One session, strongest model. Test: two worlds with different settings in one client session, and a
dedicated server.

### Phase 6 — Create World screen

1. A "Layers…" button on the Create World screen opening the config screen in new-world mode: only
   generation tabs, draft seeded from the defaults.
2. The draft is held until the world is created, written as that world's `generation.json` at server
   start, and discarded if creation is cancelled.
3. Implemented as a mixin or screen-init event on `CreateWorldScreen`, shared by both loaders;
   expect a per-version seam (tab layout classes differ between 1.20.1, 1.21.x and 26.x).

Pilot 1.21.1, then cheaper subagents per version. The user checks each version's screen in a client.

### Phase 7 — Cleanup and retirement

README (features, config, licence, credits), mod metadata, CI (jar names, verify_mixins step), final
READMEs in the two old repos pointing here, LSL update, optional merge of the duplicate Compat and
Platform classes, memory files updated.

## Rules for every session

- Never run `git commit`; the user commits.
- JDK: 26.x needs JDK 25; `runDatagen` / `runServer` for 1.20.1 and 1.21.1 need JDK 21.
- Never create or copy `eula.txt`.
- Run `scripts/verify_mixins.py` before calling a phase done: a wrong mixin target still builds.
- Foreign-mod mixins need both an intermediary-named and a Mojang-named selector.
- Python rewrites must keep CRLF line endings. Files are mixed CRLF / LF; never `sed -i` a tracked
  file (Git Bash's sed turns CRLF into LF), and count `\r` bytes rather than `grep -c $'\r$'`.
- Pilot on 1.21.1 (both loaders) before touching the other versions.

## Notes from finished phases

### Phase 1 (2026-11-13)

Done in the gen working tree, not committed. 11 jars build, `verify_mixins.py` passes on all 11,
servers started on 1.21.1 Fabric (world generation) and 26.1.2 NeoForge.

**The baseline cannot be exact, and how to measure it anyway.** Two runs of the *same* code on the
same seed give different worlds: forceloading a 16x16 area gave 230 of 256 chunks different, layer
counts off by up to 5%. Worldgen results depend on the order chunks are generated in (trees, ore
blobs and this mod all read neighbouring chunks). What works, in `gen/scripts/worldgen_baseline/`:

- `gen_seq.sh`: fresh world, seed 7474, chunks forceloaded one at a time in a fixed order, each
  waited for, with `JAVA_TOOL_OPTIONS=-Dmax.bg.threads=1` and random ticks off.
- `mca_hist.py`: reads the region files and writes a hash per chunk plus the count of every block
  state (so layer heights are compared, which `clone … filtered` counting cannot do).
- `mca_diff.py before1 before2 after`: two before-runs give the remaining noise.

Area used: rectangles `-20,-24,10,22` (snowy plains, snowy taiga, plains, forest) and `-3,-3,6,6`
(old growth pine taiga at spawn), 256 chunks, 1.21.1 Fabric, AronaLayers: Blocks alpha.1, gen's
`run/config` as it was. Even so about 45 to 60 chunks differ between two runs of the same code,
all on the edge of the area or in the taiga; use two runs on each side of a change.

Layer blocks in the 256 chunks, two runs before and two after (the JSON files are next to the
scripts as `phase1_*.json`):

| Block | before 1 | before 2 | after 1 | after 2 |
|-------|----------|----------|---------|---------|
| `aronalayers:grass_layer` | 16914 | 16914 | 16949 | 16914 |
| `aronalayers:podzol_layer` | 5557 | 5570 | 5413 | 5692 |
| `aronalayers:dirt_layer` | 3444 | 3444 | 3442 | 3444 |
| `aronalayers:sand_layer` | 2872 | 2872 | 2872 | 2872 |
| `aronalayers:coarse_dirt_layer` | 2852 | 2839 | 2957 | 2717 |
| `aronalayers:gravel_layer` | 440 | 440 | 441 | 440 |
| `aronalayers:clay_layer` | 200 | 200 | 200 | 200 |
| `aronalayers:stone_layer` | 177 | 177 | 177 | 177 |
| `aronalayers:mossy_cobblestone_layer` | 97 | 97 | 100 | 97 |
| `aronalayers:dripstone_layer` | 68 | 68 | 68 | 68 |
| granite / diorite / andesite / copper ore / coal ore layers | 13 / 9 / 8 / 4 / 1 | same | same | same |
| `minecraft:snow` (all heights) | 36160 | 36182 | 36193 | 36207 |
| `minecraft:snow_block` | 25962 | 25962 | 25962 | 25962 |

Reading: 175 chunks are identical block for block in all four runs. The spread between the two
after-runs (59 chunks) is as large as between before and after (51 to 64), and podzol and coarse
dirt move with the giant spruces of the taiga, in both directions. Nine edge chunks differ the
same way in both after-runs; they were opened one by one and differ only in dripstone clusters and
ore blobs underground, never in a layer block. So: no change found, to the precision the method
allows. It does not prove equality in the noisy taiga chunks.

**What was removed.** Classes `EnhancedRockRegistry`, `EnhancedFoliageRegistry`,
`RockMappingRegistry`, `FoliageMappingRegistry`, `NbtTreeRegistry`, `NbtTreeInjector`,
`PlantConversionHelper`; the six `cr_*.json`; `scripts/nbt_trees_guide.md`,
`convert_schematics.py`, `remove_air_from_nbt.py`; 15 options with their lang keys and preview
images (rocks, extra foliage, `cr_nbt_trees` + fallback, `place_wet_sand`, `replace_sea_grass`,
`debug_log_rocks` / `_foliage` / `_nbt_trees`), all listed in `OBSOLETE_KEYS`. README updated.

**Answers to the plan's open points, and things phase 2 must know:**

- The four mixins to check (`NbtTreeFeatureMixin`, `NbtTreeTemplateFeatureMixin`,
  `TellusProceduralTreeMixin`, `SaplingBlockGrowMixin`) did nothing but CR NBT trees: all deleted,
  with 26.3's own `NbtTreeFeatureMixin`. Trees on layers under TerraForged and Tellus go through
  other mixins. Gen now has **no foreign-mod mixin** and **no mixin plugin**
  (`AronaLayersMixinPlugin` only gated those two; deleted, `"plugin"` line removed from the six
  mixin configs). Phase 2 step 1 "with its plugin" no longer applies. 26.3's `sharedOverridden`
  list is down to `FractionalSurfaceSampler` and `TreeSoilMixin`.
- `OBSOLETE_KEYS` used to drive the backend-switch migration too; adding keys to it would have
  turned `layer_injection` on for anyone with `place_rocks: true`. The migration now reads its own
  `BACKEND_SWITCH_KEYS`.
- `replace_sea_grass` was marked CR-only but also decided whether seagrass is shifted onto the
  layer with AronaLayers. It is now always shifted (the old default).
- `plant_mappings.json` is now a `"plants"` list (only the keys were ever used without CR). Old
  files with a `"mappings"` object still load: their keys are read. Class name
  `PlantMappingRegistry` kept.
- `LayerPlacementHelper.ModBackend` / `getBackend()` / `isConquestReforged()` are gone;
  `hasLayerBlocks()` is the one `isModLoaded("aronalayers")` gate left there for phase 2 to
  replace. The CR `layer` 1-4 property is no longer recognised anywhere (placement, LayersApi,
  plant grounding), nor is `offset_toggle`.
- The dirt path replacement log moved from `debug_log_foliage` to `debug_log_structure`.
- Left in place, now unused: `Platform.onServerTickEnd` (11 files; phase 3 needs a tick hook) and
  the NBT helpers plus `chunkPosAsLong` in the six `Compat` classes. Drop or reuse them in phase 2.
- **Not deleted:** the folders `nbt_trees/` and `schematic_trees/` at the gen repo root. They are
  git-ignored local data (about 4 MB) that git cannot bring back, so they are the user's to delete.
- Not tested: a client (config screen without the Rocks, Foliage and Saplings groups), and
  1.20.1, 1.21.11, 26.2, 26.3 beyond building and the mixin check.

### Phase 2 pilot (2026-11-13)

Gen (branch `merge`, commit a124056) is in this repo for 1.21.1 Fabric and NeoForge. Not
committed. The other nine jars still build, without terrain generation.

**Where things went.**

| Gen | Here |
|-----|------|
| `io.arona74.aronalayersgen.*` (root `src/main`, `src/fabric`, `src/neoforge`, `v1_21_1/*`) | `io.arona74.aronalayers.gen.*`, same folders |
| `api.LayersApi` | `io.arona74.aronalayers.api.LayersApi` (decision 4; LSL not updated yet) |
| `block.LayerBlock` | `custom_blocks.ConvertingLayerBlock` (takes the full block's id) |
| `block.PowderSnowLayerBlockBase`, per-version `block.PowderSnowLayerBlock` | `custom_blocks`, the per-version class in **all six** `vX/common` |
| `mixin.EntityTouchingPowderSnowAccessor` | `io.arona74.aronalayers.mixin`, listed in the shared `aronalayers.mixins.json` |
| `ModBlocks` | deleted; the three blocks are in `block.ModBlocks` |
| `aronalayersgen.mixins.json` | `v1_21_1/common/src/main/resources/aronalayers.gen.mixins.json` |
| `assets/aronalayersgen/lang/en_us.json` | `assets/aronalayers_config/lang/en_us.json`, keys `config.aronalayers.*` |
| `assets/aronalayersgen/textures/gui/config/` | `assets/aronalayers/textures/gui/config/` |
| the four mapping and list JSON files at the jar root | same names, same place |
| `LICENSE` | `LICENSE-MIT`, in the two 1.21.1 jars next to `LICENSE` |
| `scripts/verify_mixins.py`, `scripts/worldgen_baseline/*` | same paths; verify_mixins now checks every mixin config in a jar |

**Decisions taken in the pilot, to confirm or change in the fan-out session.**

- **Gen keeps its own entry points** (plan step 2 said to call gen from this mod's and drop them):
  `gen.fabric.AronaLayersGenFabric` / `AronaLayersGenFabricClient` are extra entries in
  `fabric.mod.json`, after this mod's so the blocks exist first, and
  `gen.neoforge.AronaLayersGenNeoForge` is a second `@Mod("aronalayers")` class. Reason: the shared
  entry points are compiled by all 11 projects, and nine of them have no gen code yet. It also
  keeps every MIT file under a `gen` folder. Folding them into the main entry points is a
  ten-line change once every version is ported, if still wanted.
- **The language file has its own namespace folder**, `assets/aronalayers_config/lang/`. This mod's
  `assets/aronalayers/lang/en_us.json` is written by datagen, and a hand-made file at the same path
  would replace it (first copy wins in `processResources`). Minecraft merges the language files
  of every namespace, so the keys are plain `config.aronalayers.*`.
- **The three moved blocks keep their snow-style shape** (`layers` only, no facing, no
  waterlogging) and their behaviour: powder snow layer and zinc ore layers are
  `aronalayers:powder_snow_layer`, `zinc_ore_layer`, `deepslate_zinc_ore_layer`. Their blockstates
  and models are the hand-made files from gen, in root `src/main/resources` (version-independent,
  like the grass layer models already there). Datagen writes their loot tables (a zinc layer
  drops itself once whatever its height, as in gen), tags (pickaxe, needs iron tool) and names.
  They are not in a creative tab, as before. Making the zinc layers real `LayerBlock`s is
  possible later but changes what generates.
- **They are registered on all six versions already** (the shared `ModBlocks`). Outside 1.21.1
  they have models but no loot table, tags or name until that version's datagen is extended, and
  on 1.21.4+ no item model (`items/*.json`, per version in gen).
- **Config folder**: `config/aronalayers/`. `gen.config.ConfigFiles` copies
  `config/aronalayersgen/` there once (when the new folder does not exist; the old one is left).
  A mappings file imported that way still says `aronalayersgen:<block>`; `ConfigLoader` reads
  those as `aronalayers:<block>`.
- `Layer3D` uses `Layer3DBlock` directly; `LayerPlacementHelper.hasLayerBlocks()` and the three
  `.mods("aronalayers")` on options are gone. `AronaLayersGen.MOD_ID` is kept, equal to
  `AronaLayers.MOD_ID`, so per-version files need no edit for it.
- Unused helpers removed from 1.21.1's gen `Compat`: the NBT helpers, `readCompressedNbt`,
  `chunkPosAsLong`, `blockSettings`, `itemSettings`. `Platform.onServerTickEnd` is kept for phase 3.
- `breaks` (Fabric) and `incompatible` (NeoForge) for `aronalayersgen` and `aronalayersextras`, any
  version. Note that extras is not merged before phase 3: until then this jar refuses to load
  next to it.
- Not done on purpose: display name, jar names and version (decision 5, phase 7 with CI), the
  metadata licence field, README. The Fabric API and loader versions are this repo's (0.102.0 and
  0.16.5 on 1.21.1; gen built against 0.104.0 and 0.16.9).

**Temporary, to remove in the fan-out.** Each of the nine other `build.gradle` files has a block
marked `TEMPORARY (docs/MERGE_PLAN.md, phase 2)` that leaves the gen and api packages, the Mod Menu
stand-ins and gen's resources out of that project. Porting a version starts by deleting it.

**Recipe for the other versions** (one version at a time; only `vX/` files change, except where
noted):

1. `python scripts/merge/port_gen_version.py vX` copies `vX/common`, `vX/fabric`, `vX/neoforge`
   sources from gen into the `gen` package with the pilot's renames, and writes
   `aronalayers.gen.mixins.json`.
2. Delete the `TEMPORARY` block in `vX/fabric/build.gradle` and `vX/neoforge/build.gradle`. In the
   Fabric one add the Mod Menu exclusions (`jar` and `sourcesJar`, see 1.21.1) and `LICENSE-MIT` in
   both (`from([..., ...])`: with two arguments and a closure Gradle takes the closure as a third
   source and reports a circular dependency).
3. Version specials from gen's build files: 26.3's `sharedOverridden` staging
   (`gen/injection/FractionalSurfaceSampler.java`, `gen/mixin/TreeSoilMixin.java`, new paths) and
   1.20.1's own config screen; 1.20.1 may need gen's Modrinth Maven repository (gen's root
   `build.gradle`) if it still compiles against a TerraForged jar.
4. `fabric.mod.json`: the two gen entry points, the `modmenu` entry, `aronalayers.gen.mixins.json`,
   `breaks`. `neoforge.mods.toml`: the second `[[mixins]]` and the two `incompatible` blocks.
5. In `vX/common/.../gen/Compat.java` remove the helpers listed above (they refer to nothing
   else); `ModBlocks`, `blockSettings` and `itemSettings` have no caller left.
6. Datagen (`vX/fabric/.../datagen`): the three small additions of 1.21.1 in
   `ModBlockTagProvider`, `ModLootTableProvider`, `ModLanguageProvider`; on 1.21.4+ also the item
   models of the three blocks (gen's `items/*.json`). Run `runDatagen` and check the diff holds
   nothing else.
7. Build, `python scripts/verify_mixins.py vX`, server start with a new world on both loaders.

When all five are done: delete `scripts/merge/`, and decide on the entry points (first decision
above).

**What was checked.** All 11 jars build (JDK 25), `verify_mixins.py` passes on all
11 (1.21.1: 30 mixins on Fabric, 27 on NeoForge). 1.21.1 datagen changed only the three blocks'
loot tables, the pickaxe and needs-iron-tool tags, and three names.

- 1.21.1 Fabric server, the phase 1 method and area (`gen_seq.sh`, seed 7474, 256 chunks, gen's
  dev config imported from `config/aronalayersgen/`), one run, against phase 1's two after-runs:

  | Block | phase 1 after 1 | after 2 | merged mod |
  |-------|-----------------|---------|------------|
  | `aronalayers:grass_layer` | 16949 | 16914 | 16914 |
  | `aronalayers:podzol_layer` | 5413 | 5692 | 5688 |
  | `aronalayers:dirt_layer` | 3442 | 3444 | 3444 |
  | `aronalayers:sand_layer` | 2872 | 2872 | 2872 |
  | `aronalayers:coarse_dirt_layer` | 2957 | 2717 | 2721 |
  | gravel / clay / stone / mossy cobblestone / dripstone layers | 441 / 200 / 177 / 100 / 68 | 440 / 200 / 177 / 97 / 68 | 440 / 200 / 177 / 97 / 68 |
  | granite / diorite / andesite / copper ore / coal ore layers | 13 / 9 / 8 / 4 / 1 | same | same |
  | `minecraft:snow` (all heights) | 36193 | 36207 | 36142 |
  | `minecraft:snow_block` | 25962 | 25962 | 25962 |

  Of the 197 chunks identical in both phase 1 after-runs, 15 differ, all on the edge of the
  area. Totals outside the range of the two after-runs are spruce leaves (-117, with the snow on
  them, -51), ore blobs and dripstone; among layer blocks only podzol +1 and coarse dirt -1. Same
  kind and size of noise as phase 1 found between two runs of one build: no change found.
- 1.21.1 NeoForge server: starts with both `@Mod` classes, new world, 52 chunks generated, 9,139
  layer blocks in them (podzol, coarse dirt, grass, dirt, mossy cobblestone, sand).
- On the Fabric server: `powder_snow_layer` places, and turns into powder snow at 8 layers; a zinc
  ore layer at 8 stays a layer without Create; both zinc tags apply; mining one with an iron
  pickaxe drops the layer; 6 data packs as before.

Client: the user tested 1.21.1 NeoForge on 2026-11-13, "seems fine". The Fabric client is untested.

Not checked: the Fabric client (Mod Menu button, its plant grounding and stem models), Create
installed, a
NeoForge install next to the old gen jar (the `incompatible` entries), and the port script on a
real version.

### Phase 2 fan-out (2026-11-13)

Gen is now in all 11 jars. Not committed. Done with the recipe above, one Sonnet subagent per
version for the edits (each confined to its `vX/` folder, no Gradle), builds, datagen and servers
run by the orchestrating session one at a time. `scripts/merge/` is deleted (it is in the pilot
commit if ever needed again).

**What each version needed beyond the recipe.**

- In every version the only hand edit to the copied sources was the `gen/Compat.java` cleanup, as
  in the pilot; nothing referred to gen's `ModBlocks`, `LayerBlock`, `deepslate_layer` or
  `moss_layer`. 26.2 and 26.3 also bring `LeafLitterBlockMixin` (theirs only).
- 1.20.1 Fabric and 26.3 (both loaders) stage root `src/main/java` through a `stageSharedJava` Sync
  task with a `sharedOverridden` list, as gen did: 1.20.1 leaves out
  `gen/client/config/AronaConfigScreen.java`, 26.3 `gen/injection/FractionalSurfaceSampler.java`
  and `gen/mixin/TreeSoilMixin.java`; `../common` supplies those versions. The existing excludes
  (1.20.1 `MixinAxeItem`, 26.3 `mixin/tool/**`) stay as plain source-set excludes, which still apply
  to the staged copy.
- 1.20.1 does not compile against gen's ReTerraForged jar (`libs/`, never in git): no source imports
  it, RTF support is by reflection. Not brought over.
- On 1.21.11, 26.1.2, 26.2, 26.3 the three blocks' item model definitions (`items/*.json`) come from
  datagen: `ModModelProvider` calls `registerSimpleItemModel(block, aronalayers:item/<name>)`,
  which writes only the item definition, pointing at the hand-made model in root resources.
- Gen's own metadata differs from the pilot pattern only by `minecraft: ~X` (this repo keeps its
  exact version), its `breaks aronalayersextras <1.3.0` (now `*`) and, on 1.20.1, `java >=17` and
  `fabricloader >=0.15.0` (this repo's values kept). gen's `gradle.properties` match this repo's
  except 1.21.11 Fabric API (gen 0.141.5, here 0.141.6).
- `scripts/worldgen_baseline/mca_hist.py` now reads 26.3 palettes (a plain id string, or
  `{id, properties}`). On 26.x the overworld region files are in
  `<world>/dimensions/minecraft/overworld/region`: pass that folder as the world dir.

**What was checked.** All 11 jars build (JDK 25); `verify_mixins.py` passes on all 11 (Fabric 30 on
1.20.1, 31 on 1.21.11 / 26.1.2 / 26.2, 28 on 26.3 (no tool mixins); NeoForge 28). Every jar holds
`LICENSE-MIT` and the gen classes and no `com/terraformersmc` class (sources jars too). Datagen on
each of the five versions changed only: three loot tables, the pickaxe and needs-iron-tool tags,
three names, and on 1.21.4+ three `items/*.json`.

Server on each of the nine new jars: `gen_seq.sh`, fresh world (seed 7474, deleted afterwards,
`server.properties` restored), the 36 chunks `-3,-3,6,6` (old growth pine taiga), no error in the
log, layer blocks in those chunks:

| Version | Fabric | NeoForge |
|---------|--------|----------|
| 1.20.1 | 7601 | — |
| 1.21.11 | 7712 | 7711 |
| 26.1.2 | 7714 | 7718 |
| 26.2 | 7720 | 7715 |
| 26.3 | 7987 | 7986 |

(podzol, coarse dirt, grass, mossy cobblestone, dirt layers; 26.3's rewritten worldgen gives
a slightly different taiga.) The servers wrote a default `run/config/aronalayers/` in their run
folders.

Not checked: any client outside 1.21.1 NeoForge (config screen, notably 1.20.1's own; Mod Menu;
stem and grounded plant models), Create installed, the `breaks` / `incompatible` entries next to
the old jars.

**Entry points folded (the pilot's first decision, reversed).** Gen has no entry point of its own
any more: `fabric.AronaLayersFabric` calls `AronaLayersGen.init()` after `AronaLayers.init()`,
`fabric.AronaLayersFabricClient` calls `gen.fabric.ClientSetup.init()` (the former
`AronaLayersGenFabricClient`, now a plain class like NeoForge's), and `neoforge.AronaLayersNeoForge`
calls `AronaLayersGen.init()` and, on the client, `gen.neoforge.ClientSetup.init(modBus, container)`.
`AronaLayersGenFabric` and `AronaLayersGenNeoForge` are deleted and the six `fabric.mod.json` list
only this mod's entry points (plus `modmenu`). The MIT code stays under the `gen` folders; the
entry points are this repo's.

`v1_21_11/neoforge/build.gradle` had a stray `\r\r\n` from the pilot commit, so git stored it as
binary (`-text`, CRLF in the blob). Fixed in the working tree; it is now identical to 1.21.1's.
(Resolved: since the phase 2 fan-out commit the index holds it as LF, `git ls-files --eol` shows
`i/lf`; nothing more to do.)

**After the user's client tests (2026-11-14).** Mod Menu works on 1.20.1 and 1.21.1 Fabric;
grounded plants and tree stems work on 1.20.1. Two fixes:

- 1.20.1 config screen: the option list painted dirt over its own area and over the bands above
  and below it, across the left half (1.20.1's `AbstractSelectionList` does), hiding the first tab
  buttons. `v1_20_1/.../OptionRows` turns that off and draws a translucent panel like the preview's.
- Trees on layers did nothing on FreeTerraForged / ReTerraForged worlds (every version, since
  before the merge): those mods paste most trees from templates (`TemplateFeature.paste`), not
  `TreeFeature`, so `TreeShiftMixin` never saw them. Now `{Ftf,Rtf}TemplateTreePlacementMixin`
  records the origin `TreePlacement.canPlaceAt` accepts and `{Ftf,Rtf}TemplateTreeShiftMixin`
  moves `paste`'s origin up through `shiftTreeOrigin` (see `gen.injection.TemplateTreeHook`). The
  mods also extend the trunk down to the ground, refilling the freed cell, so `clearTrunkGaps` now
  clears a log of the trunk's own kind there too. `GenMixinPlugin` (new `"plugin"` in the six
  `aronalayers.gen.mixins.json`) lists these four mixins only when their mod is installed, so no
  "could not load class" warnings at start. Selectors are by name (`paste*`, `canPlaceAt`,
  `@At("LOAD")`) because the descriptors hold the mods' own classes; `require = 0`.
  Checked on a 1.21.1 Fabric server with FreeTerraForged 1.0.0022 (spawn region, seed 7474): of
  292 template trees, the cell they were raised from holds a layer for 240, the trunk's log for
  52 (no layer there); before, a log for all 292. A plain world: no warning, same layer count.
  Client tests by the user (2026-11-14): FTF on 1.21.1 NeoForge and a vanilla world on 26.1.2,
  trees and huge mushrooms raised onto layers; the 1.20.1 config screen is fine. Not checked:
  ReTerraForged.

### Phase 3 pilot (2026-11-14)

Extras (branch `main`, commit 9678b43, 1.3.0) is in this repo for 1.21.1 Fabric and NeoForge, as
`io.arona74.aronalayers.gameplay`. Not committed. The nine other jars build without it.

**Where things went.**

| Extras | Here |
|--------|------|
| `AronaLayersExtras` | `gameplay.AronaLayersGameplay.init()` |
| `GrassSpreadHandler`, `MyceliumSpreadHandler` | `gameplay.LayerSpreadHandler` (one scan for both) |
| `LayerFallHandler` | `gameplay.LayerFallHandler` |
| `ore.OreLayerDrops` | `gameplay.ore.OreLayerDrops` |
| `ore.SmallOreItems` | six fields in `item.ModItems`, ids `aronalayers:small_*` |
| `mixin.EatGrassGoalMixin`, `FallingBlockMixin`, `OreLayerDropMixin` | `gameplay.mixin`, in root `src/main` |
| per-version `mixin.SpreadableBlockMixin` | `vX/common/…/gameplay/mixin/SpreadableBlockMixin` |
| per-version `Compat` | `vX/common/…/gameplay/Compat` (game rules, enchantment levels, bottom Y) |
| `aronalayersextras.mixins.json` | `vX/common/src/main/resources/aronalayers.gameplay.mixins.json`, `defaultRequire` 1 (extras had 0) |
| `ModConfig`, `config/aronalayersextras.json` | seven options in `gen.LayerConfig`, Gameplay tab |
| item models, recipes, advancements, names (hand-made, per version) | datagen; the shape the six models shared is `assets/aronalayers/models/item/small_ore.json` in root resources |
| `LICENSE` | same text as the `LICENSE-MIT` already here |

Not ported: `WetSandHandler` and its two settings, every `conquest:*` id and the `_rocks` rule,
`SheepGrassEatingHandler` and the per-version `SheepEntityMixin` (see sheep below), the three
`mixin/compat` classes, `ModMenuIntegration`, `ClothConfigScreenFactory`, the icon.

**Reconciled with what the layers already do** (plan step 2).

- *Grass.* Extras cancelled the whole random tick of a vanilla grass block (to stop its decay),
  which also stopped it spreading, and its own handler spread grass onto dirt blocks instead. With
  the option off, both spread. Now `SpreadableBlockMixin` cancels only the tick that would turn
  the block into dirt (`canBeGrass` false), a grass block spreads onto dirt blocks by itself as in
  vanilla, and the handler covers what vanilla cannot: grass layer → dirt layer and dirt block,
  grass block → dirt layer.
- *The grass layer's own decay* (its random tick: under a full fluid block, or 8 high under a
  covering block) is unchanged. The anonymous class in `ModBlocks` became
  `custom_blocks.GrassLayerBlock` so the handler can ask the same question
  (`GrassLayerBlock.canBeGrass`) before it turns a dirt layer into grass or mycelium; it also
  skips layers with water above, and only turns a dirt *block* into grass or mycelium with air
  above (extras checked nothing for mycelium). Otherwise the handler converts and the next
  random tick reverts, for ever.
- *Sheep.* Extras converted the layer in `EatBlockGoal.tick`, then again in `Sheep.ate`
  (ignoring mobGriefing the second time), and vanilla then went on to the block under the sheep:
  a grass layer lying on a grass block cost both and fed the sheep twice. Now one mixin
  (`EatGrassGoalMixin`, `tick` at HEAD) does it and cancels vanilla on that tick; the `Sheep.ate`
  mixin is gone, and with it a per-version class. It also compares with `adjustedTickDelay(4)`
  like vanilla (extras compared with 4, before the counter was decreased), plays the break
  effect, and eats an 8-high grass layer the sheep stands *on* when the space at its feet is air.
- *Falling.* Every layer already falls by itself when nothing holds it (`LayerBlock.tick`), and
  sand layers fall like sand (`FallingLayerBlock`). The option adds: layers resting on sand, red
  sand or gravel go with it even when a block beside them would hold them. The layer's own
  scheduled tick then finds air and does nothing (checked: one block lands, no item). What falls
  is now decided from the block, not its id: a `LayerBlock` with `facing=down` (extras would have
  dropped layers set against a wall or ceiling), a `Layer3DBlock` with a floor part, the
  snow-style layers, and the ids of `additional_falling_blocks`.
- *Ore drops.* Sneak-mining takes one layer off a `LayerBlock` and pops the layer item, without
  going through the loot table: an ore layer would have handed out layers whatever the tool.
  `LayerBlock.oneLayerDrop` is a hook the gameplay code sets, so that one layer mined off an ore
  layer drops one layer's small ores (the layer item with Silk Touch). Nothing changes for the
  other layers. Ancient debris, the 3D ore layers and the zinc ore layers keep their loot table.

**Platform hooks** (plan step 3; 1.21.1 only so far).

- `gen.platform.Platform`: `onServerTickEnd(Runnable)` became `onLevelTickEnd(Consumer<ServerLevel>)`;
  new `onPlayerBlockBreak(BlockBreakListener)` (the interface is shared, in root
  `src/main/…/gen/platform`) and `fakePlayer(ServerLevel)`. `configDir()` was already there.
  NeoForge's `BlockEvent.BreakEvent` fires *before* the block is removed and can still be
  cancelled: the listener is deferred through the server's task queue (`TickTask`) and runs only
  if the block is gone.
- `platform.Platform` (this mod's own): `addToCreativeTab(tab, Supplier<List<Item>>)`. On NeoForge
  it is a mod-bus event, which that class already listens to; a supplier because on NeoForge the
  call comes before the items exist. The six items are added to vanilla's Ingredients tab.

**Config** (plan step 5). In the Gameplay tab, all `Apply.LIVE`, server side:

| `aronalayersextras.json` | `layer_config.json` | Default |
|--------------------------|---------------------|---------|
| `enableGrassSpreading` | `grass_spreading` | true |
| `enableMyceliumSpreading` | `mycelium_spreading` | true |
| `enableSheepEatingGrassLayers` | `sheep_eat_grass_layers` | true |
| `preventGrassDecay` | `prevent_grass_decay` | true |
| `enableLayersFallWithSand` | `layers_fall_with_sand` | true |
| `AdditionalFallingBlocks` | `additional_falling_blocks` | [] |
| `enableSmallOreDrops` | `small_ore_drops` | true |

`LayerConfig.loadConfig` reads `config/aronalayersextras.json` as long as `layer_config.json`
does not hold `grass_spreading` yet, that is once; the old file is left where it is.
`enableWetSands` and `WetSandMappings` are ignored.

**The three `mixin/compat` classes** (plan step 6): none is ported. `MushroomVanillaMixin` and
1.20.1's `BlockStateUpdateShapeMixin` only guarded a crash in a Conquest Reforged block.
`StateWithMixin` made `setValue` with a property the state does not have return the state
unchanged, for every block of every mod, for the same crash; it would now hide real bugs. Gen's
plant grounding does not depend on it (gen ran without extras): its `setValue` calls are on wall
and pane states, for properties those states have. The plant offset extras once had already
moved to gen in 1.3.0, so nothing overlaps.

**Decisions taken in the pilot, to confirm or change.**

- **A test command ships with the mod**: `/algdebug gameplay mine <pos> [silk_touch|fortune|sneak|sneak_silk_touch]`
  and `/algdebug gameplay anchor <pos>|clear` (`gameplay.command.GameplayDebugCommand`, permission
  level 2, next to gen's `/algdebug` commands). A dedicated server with nobody on it cannot test
  these features otherwise: spreading runs around players, and drops and block-break events need
  a player with a tool. `mine` uses the loader's fake player; `anchor` makes spreading run around
  a position. If it should not ship, delete the class, `Platform.fakePlayer`, `Compat.enchant`
  and the anchors in `LayerSpreadHandler`; the test script then has nothing to drive.
- **`prevent_grass_decay` keeps extras' default, true**: someone who only had the blocks mod now
  gets grass blocks that stay grass under a solid block. It does not apply to grass layers (their
  decay is this mod's own, unchanged).
- **Vanilla grass blocks spread onto dirt at vanilla's pace again** (see Grass above), instead of
  extras' pace near players only.
- **The gameplay code uses gen's `Platform` and `LayerConfig`** rather than a third `Platform`
  and config class; its `Compat` is its own, since extras' per-version one could be cut down
  as it was.
- Spreading looks columns up from the `MOTION_BLOCKING` heightmap instead of from the build
  limit (same result: nothing above it is solid or grass), skips chunks that are not loaded
  instead of loading them, and uses the level's random source.
- Known and left as it is: a sheep grazing on a *dirt* layer eats the grass block under it, as
  vanilla sheep do through snow. With many sheep an eaten layer therefore often ends up on dirt.

**Temporary, to remove in the fan-out.**

- Each of the nine other `build.gradle` files has a block marked
  `TEMPORARY (docs/MERGE_PLAN.md, phase 3)` that leaves the gameplay package out.
- `gameplay.AronaLayersGameplay::init` is an extra `main` entry in 1.21.1's `fabric.mod.json`, and
  `src/neoforge/…/gameplay/neoforge/GameplayNeoForge` is a second `@Mod` class, because the
  shared entry points are compiled by all 11 projects.
- Shared code already in all 11 jars: the six items are registered everywhere (outside 1.21.1
  without model, name or recipe), and the seven options show in every config screen and file
  (outside 1.21.1 they do nothing yet).

**Recipe for the other versions** (only `vX/` files change, except the last step):

1. Delete the `TEMPORARY … phase 3` block in `vX/fabric/build.gradle` and `vX/neoforge/build.gradle`.
2. `vX/common/src/main/java/io/arona74/aronalayers/gameplay/Compat.java`: take extras'
   `vX/src/main/java/io/arona74/aronalayersextras/Compat.java`, new package, and keep only
   `bottomY`, `randomTickSpeed`, `doMobGriefing`, `doBlockDrops`, `fortuneLevel`, `silkTouchLevel`
   (with their private helper). Add `enchant(ServerLevel, ItemStack, boolean silkTouch, int fortune)`
   as in 1.21.1 (on 1.20.1 enchantments are objects: `tool.enchant(Enchantments.SILK_TOUCH, 1)`,
   `Enchantments.BLOCK_FORTUNE`).
3. `vX/common/…/gameplay/mixin/SpreadableBlockMixin.java`: copy 1.21.1's. On 26.x the target is
   `SpreadingSnowyBlock`. Check with `javap -p` that it has
   `private static boolean canBeGrass(BlockState, LevelReader, BlockPos)`.
4. `vX/common/src/main/resources/aronalayers.gameplay.mixins.json`: copy 1.21.1's;
   `compatibilityLevel` and any `overwrites` block as in that version's `aronalayers.gen.mixins.json`.
5. `vX/{fabric,neoforge}/…/gen/platform/Platform.java`: replace `onServerTickEnd` by
   `onLevelTickEnd`, add `onPlayerBlockBreak` and `fakePlayer`, as in 1.21.1. Fabric's event is
   `ServerTickEvents.END_WORLD_TICK` up to 1.21.11 and `END_LEVEL_TICK` from 26.1.2. On NeoForge
   check `LevelTickEvent.Post`, `BlockEvent.BreakEvent`, `MinecraftServer.tell(TickTask)` and
   `FakePlayerFactory.getMinecraft` against that version's jars.
6. `vX/{fabric,neoforge}/…/platform/Platform.java`: `addToCreativeTab`, as in 1.21.1. Fabric from
   26.1.2: `CreativeModeTabEvents.modifyOutputEvent(tab)` (see extras' `Compat.addToIngredientsTab`).
7. `fabric.mod.json` and `neoforge.mods.toml`: add `aronalayers.gameplay.mixins.json`. No entry
   point (last step).
8. Datagen (`vX/fabric/…/datagen`): the additions of 1.21.1 in `ModModelProvider` (six models
   with parent `aronalayers:item/small_ore`), `ModRecipeProvider`, `ModLanguageProvider`. On
   1.21.11 and 26.x the item definitions (`items/small_*.json`) come from datagen too, as for the
   three blocks of phase 2. Run `runDatagen`; the diff must hold six names, six models, six
   recipes, six advancements (and six item definitions there). Compare one recipe and one
   advancement with extras' hand-made file of that version.
9. The shared classes name these vanilla members; check them on the version (`javap -p`) and
   move a class to `vX/common` only if one differs: `EatBlockGoal` (`mob`, `level`,
   `eatAnimationTick`, `canUse`, `tick`), `Goal.adjustedTickDelay`, `FallingBlockEntity`
   (`fall`, `blockState`, `dropItem`, the `BlockState.canBeReplaced(BlockPlaceContext)` call in
   `tick`), the six-argument `Block.dropResources`, `ServerPlayer.gameMode.destroyBlock`,
   `ChunkAccess.getHeight(Heightmap.Types, int, int)`, `ServerChunkCache.getChunkNow`.
10. Build, `python scripts/verify_mixins.py vX`, then on both loaders
    `bash scripts/gameplay_test/gameplay_test.sh vX:<loader> <throwaway world> <log>` and compare
    with "Expected" below. The script restores `server.properties`; delete `<run>/<world>`
    afterwards. It may need adapting: 1.20.1 has no `tick sprint` (raise `randomTickSpeed`
    instead, spreading scales with it; sheep need real time), 1.21.11 and later renamed game
    rules (`doDaylightCycle`) and pause an empty server after 60 s.

When all five are done: `fabric.AronaLayersFabric` and `neoforge.AronaLayersNeoForge` call
`AronaLayersGameplay.init()` after `AronaLayersGen.init()`; delete `GameplayNeoForge` and the
`::init` entry of 1.21.1's `fabric.mod.json`.

**What was checked.** All 11 jars build (JDK 25). `verify_mixins.py` passes on all 11 (1.21.1:
34 mixins on Fabric, 31 on NeoForge, four more than before; the others unchanged). The nine other
jars hold no `gameplay` class. 1.21.1 datagen changed only: six names in `en_us.json`, six item
models, six recipes, six advancements; the two recipes and advancements compared (coal, raw
copper) are the same JSON as extras' apart from the namespace.

Server tests, `scripts/gameplay_test/gameplay_test.sh` (flat world, set-ups at y=200, 36,000
ticks sprinted), 1.21.1 Fabric and NeoForge, default settings. **Expected**, and what both gave:

| Test | Result on both loaders |
|------|------------------------|
| A: one grass layer among 24 dirt layers, 5 dirt blocks beside | 24 of 24 grass layers; dirt blocks → grass blocks, 5 of 5 on NeoForge, 4 of 5 on Fabric (the fifth had not been reached yet) |
| B: one grass block among 24 dirt layers | 24 of 24 grass layers |
| C: one mycelium layer among 24 dirt layers | 24 of 24 mycelium layers |
| D: 25 dirt layers, no source | 25 dirt layers |
| Sheep, 2-high grass layers on grass blocks, after 1,500 ticks | 2 layers eaten with the grass block under them intact, on each loader (Fabric: one more where both were eaten, a second bite); never a block eaten under an uneaten layer |
| Sheep, after 36,000 ticks | 4 of 9 layers eaten in each pen (2-high, and 8-high on stone), 8 sheep alive |
| F1: sand under a full layer and a 3-high layer, both also held from the side | sand, full layer, low layer landed stacked; nothing left above; no item |
| F2: sand under a layer set against a wall | the sand fell, the layer stayed |
| F3: two low layers, a block next to where the upper one lands | the upper one is placed above the lower one, no item |
| F4: the fake player breaks sand under a layer held from the side | the layer is where the sand was; only the sand item |
| F5: oak slab on sand | stays (falls when listed in `additional_falling_blocks`, first Fabric run) |
| F6: gravel under a 3D layer | both landed, the 3D layer on the gravel |
| Coal ore layer, 5 layers | 5 Small Coal; with Silk Touch 5 coal ore layers, no experience |
| Deepslate copper ore layer, 8 layers | 27 and 19 Small Raw Copper (16 to 40 expected) |
| Redstone ore layer, 8 layers | 4 redstone and experience |
| Diamond ore layer, 4 layers | 4 Small Diamond and experience; with Fortune III 16 on Fabric, 4 on NeoForge (4 to 16 expected) |
| Iron ore layer, 6 layers, sneaking | 5 layers left and 1 Small Raw Iron; with Silk Touch 1 iron ore layer |
| Stone layer (3) and ancient debris layer (2) | 3 stone layers, 2 ancient debris layers: untouched |

A first Fabric run had a `config/aronalayersextras.json` with `enableMyceliumSpreading` false,
`preventGrassDecay` false and `AdditionalFallingBlocks` `["minecraft:oak_slab"]`: the log says
it was imported, `layer_config.json` got those values, test C stayed at 24 dirt layers and F5's
slab fell. On the next run, with the old file still there and the values set back by hand, it
was not imported again. The run folders are back as they were (`server.properties`, throwaway
worlds deleted, the test file removed), except that both `run/config/aronalayers/layer_config.json`
now hold the seven new keys at their defaults.

Not checked: any client (Gameplay tab with its three new groups, the small ores in the
Ingredients tab and their models, the recipe book); a real player breaking sand or an ore layer
(the fake player goes through the same game code, but not through the network); grass block
decay and the grass layer's own random tick (blocks only tick randomly near a real player);
Fortune on redstone; the other nine versions beyond building.

### Phase 3 fan-out (2026-11-14)

The gameplay package is in all 11 jars. Not committed. 1.21.11 was done in this session as the
model for 26.x, then one Sonnet subagent per version (1.20.1, 26.1.2, 26.2, 26.3) for the edits,
confined to its `vX/` folder, no Gradle; builds, datagen and servers run by the orchestrating
session one at a time. The pilot's five decisions were confirmed by the user on 2026-11-15: the
test command ships, `prevent_grass_decay` defaults to true, vanilla grass blocks spread at
vanilla's pace, one `Platform` and one config (`LayerConfig`) for everything, and spreading scans
down from the heightmap (starting one block above it on every version, see the 26.3 fix below).

**Entry points folded, temporary pieces gone.** `fabric.AronaLayersFabric` and
`neoforge.AronaLayersNeoForge` call `AronaLayersGameplay.init()` after `AronaLayersGen.init()`;
`GameplayNeoForge` is deleted, 1.21.1's `fabric.mod.json` lists only `AronaLayersFabric` again, and
no `TEMPORARY (docs/MERGE_PLAN.md, phase 3)` block is left in any `build.gradle`.

**What each version needed beyond the recipe.**

| | Compat | `SpreadableBlockMixin` | gen `Platform` | creative tab | gameplay mixins json | datagen |
|---|---|---|---|---|---|---|
| 1.20.1 (Fabric) | `getMinBuildHeight`, `GameRules.RULE_*` with `getInt`/`getBoolean`, enchantments as objects (`Enchantments.BLOCK_FORTUNE`, `tool.enchant(Enchantment, int)`) | `SpreadingSnowyDirtBlock.canBeGrass` | as 1.21.1 (`END_WORLD_TICK`) | `ItemGroupEvents` | `JAVA_17` | old API: `Consumer<FinishedRecipe>`, `new ResourceLocation`; `ItemModelGenerators.output` is private, so the six models go through `generateFlatItem` with an anonymous `ModelTemplate` that swaps in the whole item's texture; recipes in `recipes/`, advancements in `advancements/` |
| 1.21.11 | `getMinY`, `gamerules.GameRules.RANDOM_TICK_SPEED` / `MOB_GRIEFING` / `BLOCK_DROPS` with `get`; `doMobGriefing` only on a `ServerLevel` | as 1.21.1 | NeoForge `MinecraftServer.schedule` (was `tell`) | as 1.21.1 | as 1.21.1 | `Identifier`; models through `img.modelOutput`, item definitions through `img.itemModelOutput.accept(item, ItemModelUtils.plainModel(model))`; recipe `save(output, "aronalayers:…")` |
| 26.1.2, 26.2 | as 1.21.11 | target `SpreadingSnowyBlock`, `canBeGrass` renamed `canStayAlive` | Fabric `END_LEVEL_TICK`; NeoForge `BlockEvent.BreakEvent` is now `event.level.block.BreakBlockEvent` | Fabric `CreativeModeTabEvents.modifyOutputEvent` | `JAVA_25`, `overwrites.requireAnnotations` as the gen json | as 1.21.11, but `TextureMapping.getItemTexture` returns a `Material` |
| 26.3 | as 1.21.11 | as 26.1.2 | as 26.1.2 | as 26.1.2 | as 26.1.2 | as 26.1.2, recipes through the provider's own `shaped(category, result)` |

No shared class had to move to `vX/common`: every vanilla member the shared code names (recipe
step 9) exists with the same signature on all six versions.

**Two bugs found by the servers, fixed in shared or every-version code.**

- *26.x Fabric: no entity could be added to a level* (summon, falling sand, sheep: "unexpected
  error", then a crash on the first falling block). Cause: `gen.platform.Platform.fakePlayer`
  returned `FakePlayer.get(level)` as a `ServerPlayer`; verifying that method loads
  `ServerPlayer` and up to `Entity`, and `GenMixinPlugin` loads `Platform` (`isModLoaded`) before
  the mixins are applied, so `Entity` was defined without Fabric API's lifecycle mixin
  (`ClassCastException … EntityLoadDataSetter`). It showed only on 26.x Fabric, but the pattern is
  in all 11: each `gen.platform.Platform` now calls a nested `FakePlayers` class, which is verified
  only when the test command runs. Rule for later: `gen.platform.Platform` (both loaders) must
  not name a Minecraft class in a way the verifier has to load (a subtype returned or passed as
  its supertype).
- *26.3: grass and mycelium did not spread from layers* (only from a grass block, onto its eight
  neighbours). `ChunkAccess.getHeight` is the Y of the highest motion-blocking block (it is
  `Level.getHeight` that adds 1), and from 26.3 a low layer no longer counts as motion-blocking, so
  the column scan started on the block under the layer. `LayerSpreadHandler` now starts one block
  higher, on every version. Gen reads `OCEAN_FLOOR` / `WORLD_SURFACE` heightmaps in many places;
  whether 26.3's change matters there was not looked at.

**What was checked.** All 11 jars build (JDK 25). `verify_mixins.py` passes on all 11, four more
mixins than after phase 2 everywhere: Fabric 34 on 1.20.1 and 1.21.1, 35 on 1.21.11 / 26.1.2 /
26.2, 32 on 26.3; NeoForge 31 on 1.21.1, 32 on the others. Datagen on each of the five versions
changed only six names, six item models, six recipes, six advancements, and on 1.21.11 and 26.x
six item definitions; the raw copper recipe, advancement and item definition are the same JSON as
extras' hand-made file of that version apart from the namespace (26.1.2's recipe leaves out
`"count": 1`, the default; 1.20.1's advancement lists its two requirements in the other order).

`gameplay_test.sh` now sets `pause-when-empty-seconds=0`, sends both `doDaylightCycle` and
`advance_time`, and on 1.20.1 (no `tick sprint`) waits 75 s for the short run and 2 minutes at
`randomTickSpeed 300` for the long one. Last run, after both fixes, on all 11 (fresh flat world,
deleted afterwards, `server.properties` restored, the only ERROR line is vanilla's
`No key layers in MapLike[{}]` about the flat world's empty generator settings):

| Test | All 11 |
|------|--------|
| A, B, C, D | 25 grass layers, 24 of 24, 25 mycelium layers, 25 dirt layers, except a cell or two not reached yet (B 23 of 24 on 26.2 and 26.3 NeoForge, C 24 on 26.1.2 NeoForge); A's dirt blocks all grass except 1.21.11 Fabric (3 of 5) |
| Sheep | 8 alive, 4 of 9 layers eaten in each pen (1.20.1 too, with its shorter run); never a block eaten under an uneaten layer |
| F1-F6 | all as expected, the slab stays, only F4's sand item |
| Ores | coal 5 → 5 Small Coal, Silk Touch 5 layers; copper 8 → 25 to 32; redstone 8 → 4 or 5 with experience; diamond 4 → 4, Fortune III 4 to 16; iron sneaking → 1 Small Raw Iron and 5 layers left, with Silk Touch 1 layer; stone and ancient debris layers untouched |

The three 26.x Fabric servers of the first run crashed (the entity bug above) and left a crash
report each in `v26_x/fabric/run/crash-reports/`.

Client test by the user (2026-11-15): 26.3, the small ores and the config screen look fine. Not
checked: a client on the other versions (Gameplay tab, small ores in the Ingredients tab and
their models, recipe book); a real player breaking sand or an ore layer; Fortune on redstone.

### Phase 4 (2026-11-15)

Done in one session, shared code only, no subagents. Not committed. Decided with the user before
coding: the scope table below, the `trees_on_layers` split, `layer_config.json` **deleted** once
carried over, the ⚠ kept on the generation tab labels.

**Scopes and tabs.** `ConfigTab` has a `scope` (`ConfigTab.Scope.GLOBAL` / `WORLD`, replacing the
`worldgen` flag; `tab.generation()` is the test). Order: Visuals, Gameplay, ⚠ Terrain,
⚠ Plants & Trees, ⚠ Structures, Debug. The screen opens on Visuals the first time.

| Scope | File | Tabs and groups |
|-------|------|-----------------|
| GLOBAL | `config/aronalayers/config.json` | Visuals (trunks, posts, grounding), Gameplay (snow break, grass, falling, ores), Debug |
| WORLD | `config/aronalayers/generation_defaults.json` | Terrain (layer generation, 3D layers, layer count, snow, surfaces, swamps), Plants & Trees (plants, trees), Structures |

A fresh start writes 30 GLOBAL and 46 WORLD options. Both files keep the old format (`_comment`,
`_section_<tab>`, `_comment_<key>` from en_us.json); the WORLD file is the format phase 5 reuses for
`<world>/aronalayers/generation.json`. Until then every world generates from
`generation_defaults.json` (its `_comment` and the screen say so), so nothing changes for worlds.
`LayerConfig.load(scope, …)` / `write(scope, …)` take the scope, for phase 5 to point WORLD at a
world's file. A key of the other scope found in a file is warned about and kept at the end.

**The two mixed options.**

- `trees_on_layers` is now WORLD (Plants & Trees, server, new chunks): generated trees, huge
  mushrooms and chorus start one block higher, saplings and mushrooms grow on the layer
  (`LayerPlacementHelper.shiftTreeOrigin`, `soilUnderTrunk`, `mushroomSoil`, `liftChorusPlant`,
  both `TreeSoilMixin`). New GLOBAL `trunks_on_layers` (Visuals, both sides, restart): the trunk
  drawn down to the layer, with outline and collision (`ClientSetup`, every
  `GroundedPlantModelPlugin`, `PlantGrounding.isStandingTrunk` / `trunkFooting` / `clipLowered`).
  `LayerConfig.trunksOnLayers()` = `Compat.STEM_MODELS && TRUNKS_ON_LAYERS`; `treesOnLayers()` =
  `trunksOnLayers() && TREES_ON_LAYERS`, so without trunks trees are never shifted (they would
  float); on screen `trees_on_layers` requires `trunks_on_layers`. The only per-version edits of
  the phase: `treesOnLayers()` → `trunksOnLayers()` in the five NeoForge `ClientSetup` and the 11
  `GroundedPlantModelPlugin` (one token each), and 1.20.1's own `AronaConfigScreen` (same three
  lines as the shared one: `lastTab`, the heading row).
- `3D_layer_injection` needed no global part: its `RESTART` dated from gen's first alpha, where it
  gated a start-up step that no longer exists; `Layer3D.isActive()` reads it per chunk and the 3D
  blocks are always registered. The three 3D options are WORLD, new chunks, in their own group;
  `LayerConfig.onValuesChanged()` clears `Layer3D`'s excluded-blocks cache
  (`Layer3D.invalidateExcluded()`).

**Carrying old files over** (`LayerConfig.loadConfig`). After `ConfigFiles` has copied an old
`config/aronalayersgen/` (unchanged), a file that does not exist yet is made from
`layer_config.json`: config.json from the defaults, then `aronalayersextras.json` (only while
`layer_config.json` has no `grass_spreading`, phase 3's rule), then the GLOBAL keys of
`layer_config.json`, the old `trees_on_layers` also becoming `trunks_on_layers`, unknown keys kept at
the end; generation_defaults.json from the defaults and the WORLD keys, with the backend-switch
migration. Obsolete keys are dropped. When both files exist and nothing failed,
`layer_config.json` is deleted. If it cannot be read, or holds an invalid value for the scope being
created, that file is not written and the old one is kept, so the next start tries again.

**Screen.** On a generation tab the heading row and the preview text are the seam warning in a
world, and "Defaults for new worlds" outside one (`ConfigWidgets.generationHeading()` /
`generationText()`, lang `config.aronalayers.new_world_defaults.1-3`; the text says that until
worlds keep their own copy, existing worlds also use them; phase 5 drops that sentence). The
preview also names the tab's section. `tab_worldgen.png` is now `tab_terrain.png`;
`trunks_on_layers.png` is a copy of `trees_on_layers.png`; Plants & Trees has no tab image. The
remote tooltip now says "edit the server's files in config/aronalayers/".

**For phase 5** (step 5's audit): `plant_injection` (WORLD) is also read at runtime, on the client
too, by `PlantPlacementHook.lookThroughLayers` (plant `canSurvive` on a layer, `|| plant_grounding`).
A remote client will not know the world's value; let that check rely on `plant_grounding` alone
on the client, or sync the value. `tree_injection` also governs sapling growth through layers
(`TreeFeatureMixin.validTreePos`), server side only, so WORLD is fine.

**What was checked.**

- All 11 jars build (JDK 25); `verify_mixins.py` passes on all 11, counts unchanged from phase 3.
- Worldgen, 1.21.1 Fabric, `gen_seq.sh` seed 7474, chunks `-3,-3,6,6`, the run folder's dev config:
  7579 layer blocks before the change and 7579 after (the after run also carried that
  `layer_config.json` over: all 79 keys in the two files with the same values, the four unknown
  `dh_*` keys kept in config.json, the old file deleted). Podzol / coarse dirt 4375 / 2786 before,
  4388 / 2773 after: the run-to-run noise phase 2 saw; grass, dirt, mossy cobblestone identical.
- Config start test on all 11 servers (throwaway world, deleted; `server.properties` and the
  original config folder put back): with no `config/aronalayers/` the server writes config.json
  (30 options) and generation_defaults.json (46) and no `layer_config.json`; with only a crafted
  `layer_config.json` (19 non-default values across both scopes, `trees_on_layers` false, an
  obsolete and an unknown key) every value lands in the right file, `trunks_on_layers` is false,
  the unknown key is kept in config.json, the obsolete one dropped, the old file deleted.
- `gameplay_test.sh` on 1.21.1 Fabric and NeoForge and 26.3 Fabric and NeoForge, each run folder's
  own dev config (carried over to the two files at that start): every line within phase 3's
  "Expected" (A-D spread, 8 sheep and 4 of 9 eaten in each pen, F1-F6, coal 5 / Silk Touch 5
  layers, copper 24 to 32, redstone 4-5, diamond 4 and Fortune III 4 to 16, iron sneaking 1 and 5
  left, stone and ancient debris untouched); 26.3 Fabric left one dirt layer and one dirt block of
  test A not reached yet, as seen before.
- Every run folder is back as it was: its original `config/aronalayers/` (still with
  `layer_config.json`, which the next start carries over), `server.properties`, no test world.

Not checked: a client (see below); a `layer_config.json` with an invalid value (code path only);
the Mod Menu / `/algconfig` screen on a remote server.

**For the user to check in a client** (1.20.1, 1.21.1, 26.3): tab order and that "⚠ Plants & Trees"
fits its button (80 px at most; vanilla scrolls a label that does not fit); from the title screen
the three generation tabs start with "Defaults for new worlds" and the preview says the same,
inside a world they show the seam warning; Visuals has "Trunks on layers" first, Plants & Trees
has Trees On Layers greyed out with a "requires" line when Trunks is off; Terrain has the
"3D layers" group with no restart note; Done writes the two files; an existing
`layer_config.json` in a client's config folder is carried over and deleted.

Client test by the user (2026-11-15) on 1.20.1, 1.21.1 and 26.3: fine but for one thing on 26.3,
the text rows of the option list (group headings, list labels, the generation heading) drawn at
the left instead of centred. Older than phase 4: from 1.21.11 `StringWidget` has no
`alignCenter()` and always draws at the left (1.21.1 centres by default), so 1.21.11, 26.1.2 and
26.2 did the same. Fixed with a new seam, `ScreenCompat.centeredText(width, height, text, font)`:
a plain `StringWidget` on 1.20.1 and 1.21.1, on the four later versions a `StringWidget` whose
`visitLines` draws through `ActiveTextCollector.accept(TextAlignment.CENTER, …)`. The shared
`AronaConfigScreen.fillRows` and `ConfigWidgets.label` use it; 1.20.1's own screen keeps its plain
`StringWidget`. 11 jars build, verify_mixins passes. Checked in a 26.3 client by the user
(2026-11-15): centred.

### Phase 5 (2026-11-16)

Done in one session, no subagents. Not committed. Decided with the user before coding: plants
stand on layers whatever the options (A), a key missing from a world's file takes the value in
`generation_defaults.json` (B), the reload command covers `config.json` too except restart options
(C), a remote client's generation tabs show only where the server keeps them (D), and the Folder
button opens the world's folder on a generation tab in a world (E).

**Storage and lifecycle.** Each world has `<world>/aronalayers/generation.json`, the format of
`generation_defaults.json` with its own `_comment`. `gen.WorldLifecycle` (shared) registers two new
`gen.platform.Platform` hooks:

| | Fabric (6 versions) | NeoForge (5 versions) |
|---|---|---|
| `onServerStarting` → `LayerConfig.openWorld(server.getWorldPath(LevelResource.ROOT))` | `ServerLifecycleEvents.SERVER_STARTING` | `ServerAboutToStartEvent` |
| `onServerStopped` → `LayerConfig.closeWorld()` | `SERVER_STOPPED` | `ServerStoppedEvent` |

These two methods are the only per-version edit of the phase: each loader and version has its own
`Platform`; the event classes have the same names on every version (checked in each version's
jar). The server is only passed on as `Consumer.accept(Object)`, so verifying `Platform` (loaded by
`GenMixinPlugin`) still loads no Minecraft class. Why it is early enough: Fabric API fires
`SERVER_STARTING` in `MinecraftServer.runServer` just before `initServer()` (all six Fabric API
versions, read in the jars), and NeoForge posts `ServerAboutToStartEvent` two lines above
`this.loadLevel()` in both `DedicatedServer.initServer` and `IntegratedServer.initServer` (all five);
levels, and so chunks, spawn chunks included, only come from `loadLevel`. Checked on a server per
version by the log order, see below.

- `openWorld`: built-in defaults, then `generation_defaults.json`, then the world's file over them,
  parsed before anything is applied. No file: the copy of the defaults is written (log
  `[Config] Created …, a copy of generation_defaults.json`). Then the file is rewritten in the
  current layout (unknown keys kept), unless it could not be read or held an invalid value. The
  `layer_injection / plant_injection / tree_injection` log line moved here from `AronaLayersGen.init`.
- `closeWorld`: the WORLD fields go back to `generation_defaults.json`. Outside a world they hold
  the defaults; while a world is open the defaults file is not read.
- Both, and a new start in case a stop failed, clear the static worldgen state of one world
  (`WorldLifecycle.resetWorldState`): `RandomStateHolder`, `RTFCompat.resetWorldState`,
  `LayerBackend.clear` (all three also cleared by `ChunkMapMixin.onClose`, as before),
  `PreStructureHeightmapStorage.clear` (both snapshot maps, keyed by chunk position only),
  `TellusCompat.resetWorldState` (chunks awaiting a rerun, counters, one-time log flags),
  `LayerPlacementHelper.resetWorldState` (swamp cache keyed by the old server's `Biome` objects,
  debug counters); the gameplay test anchors are cleared at stop by `AronaLayersGameplay`. The
  worldgen ThreadLocals are cleared as each chunk or tree finishes and are not touched;
  `placingShiftedTree` stays true on its thread if a tree placement throws (not changed).
  Layer3D's excluded-blocks cache and plant grounding are reset by `onValuesChanged()` after every
  load.

**Reads of WORLD fields outside worldgen** (plan step 5).

- `plant_injection` in `PlantPlacementHook.lookThroughLayers`, `lookThroughLayersForSturdyFace`
  and `lookThroughLayerForColumnPlant` (plant, leaf litter, column plant, bamboo, mushroom,
  dripstone survival), which the client runs too: the look-through is now always on (only
  `GroundingFlags.ready` is checked), so client and server agree whatever the world's value.
  `plant_injection` keeps its generation meaning only (plants moved up onto layers); its
  description says plants can stand on layers either way. Behaviour change: with both
  `plant_injection` and `plant_grounding` off, plants can now be placed on layers.
- `tree_injection` (`TreeFeatureMixin.validTreePos`) and `treesOnLayers()` (`shiftTreeOrigin`,
  `soilUnderTrunk`, `mushroomSoil`, both `TreeSoilMixin`): server only, they follow the open world.
- `ChunkDebugCommand`, `TellusDebugCommand`: server thread, report the open world's values.
- Block bootstrap and `GenMixinPlugin` read no WORLD field; `PlantGrounding` reads only GLOBAL ones.

**Screen.** `ConfigWidgets.generation()` says what the generation tabs edit:

| State | Generation tabs | Done / `ConfigDraft.commit` writes |
|---|---|---|
| singleplayer or LAN host (`LayerConfig.worldOpen()`) | editable, seam warning | `config.json` if a GLOBAL value changed, the world's `generation.json` if a WORLD one did |
| outside a world | editable, "Defaults for new worlds" (2 lines, the phase 4 sentence about existing worlds dropped) | `config.json` and/or `generation_defaults.json` |
| remote server | heading "Set by the server" only, no rows; the preview says the file is `aronalayers/generation.json` in the server's world folder and to run `/aronalayers reload`; Reset Tab inactive | `config.json` only |

`LayerConfig.save(Set<Scope>)` writes only the scopes that changed (it wrote both before). On a
generation tab in a world, Folder opens `<world>/aronalayers/` (its own tooltip); elsewhere
`config/aronalayers/`. 1.20.1's own `AronaConfigScreen` got the same three lines.

**Reload command.** `/aronalayers reload`, permission level 2 (`gen.command.ConfigCommand`), not
under `/algconfig` (a client command on both loaders). `LayerConfig.reloadFiles()` parses
`config.json`, `generation_defaults.json` and the open world's `generation.json` before applying
anything; a file that cannot be read or an invalid value changes nothing and is reported. RESTART
options keep their running value and are listed. Then `onValuesChanged()`
(`PlantGrounding.invalidate`, `Layer3D.invalidateExcluded`). Files are not rewritten; the mapping
files are not reloaded.

**What was checked.**

- All 11 jars build (JDK 25); `verify_mixins.py` passes on all 11, counts unchanged from phase 4.
- Worldgen, 1.21.1 Fabric, `gen_seq.sh` seed 7474, chunks `-3,-3,6,6`, the run folder's dev config
  (its `layer_config.json` carried over at each start, so the world's `generation.json` is a copy
  of that): 7578 layer blocks before the change, 7579 and 7578 in two runs after. The second
  after-run has the same grass (271), dirt (58) and mossy cobblestone (89) layers as the before-run;
  podzol / coarse dirt 4423 / 2737 before, 4397 / 2801 and 4402 / 2758 after. The two after-runs
  differ from each other in 24 of 36 chunks (ore blobs, leaves), as many as before vs after: the
  run-to-run noise of earlier phases. Both plant options are on in that config, so the
  always-on look-through changed nothing there.
- Start test on all 11 dedicated servers (throwaway world, seed 7474, deleted; `server.properties`
  and `config/aronalayers/` put back): first start writes `<world>/aronalayers/generation.json`
  (46 options, values equal to `generation_defaults.json`); with the file deleted, the next start
  of the same world writes it again, the same text. In every log the hook's line
  (`[Config] World generation settings from …`) comes before the first
  `[ChunkMap Mixin] Capturing NoiseConfig` (ChunkMap construction, before any chunk of that level)
  and before the first chunk line (`[Layers] minecraft:overworld: layers from …` / `Preparing …`);
  `World closed` at stop; no ERROR.
- Reload, 1.21.1 Fabric and 26.3 NeoForge, fresh world: 2×2 chunks at 0,0 generated with the
  copied defaults (863 / 872 layers); a non-number in `structure_footprint_below_margin` →
  "AronaLayers settings not reloaded, nothing changed: structure_footprint_below_margin in
  generation.json: expected a number"; fixed, plus `layer_injection` false → "reloaded, changed:
  layer_injection". 2×2 chunks then generated 30 chunks out in four directions: 0 layers at
  30,0 / 0,30 / 0,-30, which in a control run (same steps, `layer_injection` left on) got 741 /
  782 / 951 (Fabric) and 743 / 879 / 955 (NeoForge). (-30,0 and -30,-30 have no layers even in
  the control on Fabric; chunks near spawn were already generated before the reload.)
- `gameplay_test.sh`, 1.21.1 Fabric: every line within phase 3's "Expected" (A-D spread 25 / 24 /
  25 / 25 and the 5 dirt blocks, 8 sheep and 4 of 9 eaten in each pen, F1-F6, coal 5 / Silk Touch 5
  layers, copper 24, redstone 4, diamond 4 and Fortune III 12, iron sneaking 1 and 5 left, stone and
  ancient debris untouched); the flat world got its `generation.json` too.

Not checked: a client (below); a world whose `generation.json` is unreadable at start (code path:
logged, values from the defaults, file left alone); TerraForged or Tellus worlds; the reload
command on an integrated server.

**For the user to check in a client** (1.20.1, 1.21.1, 26.3):

- Two singleplayer worlds in one session: create world 1, change a generation option (say
  Terrain → Swamps → Reduce Swamp Layers on), Done, leave; create world 2. World 2's tabs show
  the defaults (option off), world 1's `saves/<world 1>/aronalayers/generation.json` has it on,
  `config/aronalayers/generation_defaults.json` does not. Reopen world 1: the option is on again.
  The log names each world's file (`[Config] World generation settings from …`) and says
  `World closed` on leaving.
- Generation tabs inside a world: the seam warning; Folder opens `saves/<world>/aronalayers/`
  (tooltip says so); a GLOBAL change alone (Visuals) does not touch the world's file.
- Outside a world (title screen, Mod Menu): "Defaults for new worlds", the preview text has two
  sentences, no longer the one about existing worlds; Done writes `generation_defaults.json`; a
  world created afterwards gets the new values.
- Joining a dedicated server: the three generation tabs show only "Set by the server", the
  preview names `aronalayers/generation.json` and `/aronalayers reload`; Reset Tab is greyed
  there; Visuals and Debug still edit the client's `config.json`, Gameplay is read-only as before.
- Plants placed by hand on a layer (flower on a grass layer) stay, in singleplayer and on the
  server, and do not vanish client-side when a neighbouring block is broken.

Client test by the user (2026-11-16) on 1.20.1, 1.21.1 and 26.3: everything above is fine (two
worlds in one session, the generation tabs inside and outside a world, plants on layers). The
dedicated-server item was then checked on 1.21.1 Fabric (dev client joining a dev server,
offline mode, throwaway world, run folder put back afterwards): the generation tabs show only the
"Set by the server" text, and a flower on a grass layer stays when a block next to it is broken.
A remote client on the other versions was not tried.

### Phase 6 (2026-11-22)

Done in one session, no subagents. Not committed. Decided with the user before coding: the button
on the More tab (A), a Re-create copies the old world's `generation.json` (B), Cancel on the
Layers screen drops only that visit's changes and keeps an earlier Done (C), the generation tabs
drop the ⚠ in new-world mode (D).

**The button.** One shared mixin, no per-version code: `gen.mixin.CreateWorldMoreTabMixin`,
`@Mixin(targets = "…CreateWorldScreen$MoreTab")` (a private inner class, so a string target),
extends `GridLayoutTab` and adds "Layers…" (210 px, as vanilla's three) at row 3 of the tab's
`layout` at the end of its constructor; the screen lays it out, on resize too. Checked with javap
on the six Fabric jars and the five patched NeoForge jars: on every version `MoreTab` extends
`GridLayoutTab`, takes only the outer `CreateWorldScreen`, fills rows 0-2 of a one-column grid
(Game Rules, Experiments, Data Packs), and `GridLayoutTab.layout` / `GridLayout.addChild(T, int,
int)` exist. The tab bar itself differs (26.2 on: `MenuTabBar extends TabNavigationBar`), which
the mixin does not touch. Loom remaps the string target on the obfuscated versions (1.21.1:
`net/minecraft/class_525$class_8094`); `verify_mixins.py` now also checks `targets` strings (an
intermediary `class_N` name on obfuscated Fabric, a class present in the Minecraft jar elsewhere).
Both new mixins are in each version's `aronalayers.gen.mixins.json` `client` list.

**The draft.** `gen.config.NewWorldDraft` (shared, no Minecraft type, synchronized: the integrated
server takes it on its own thread) holds, for one Create World screen (its owner, a weak
reference): the WORLD values stored by Done, the world a Re-create copies, the folder Create was
pressed for. `gen.mixin.CreateWorldScreenMixin` (shared; every target has the same name on every
version and the handlers take no argument that differs):

| Hook | Effect |
|---|---|
| `<init>` TAIL | new owner, everything else dropped; the Re-create source handed over only if `createFromExisting` made this screen |
| `popScreen` HEAD (Cancel, Escape, and a failed creation: pack copy failure, or 1.21.11+ a `CreateWorldCallback` returning false) | draft dropped |
| `onCreate` HEAD | armed for `getUiState().getTargetFolder()`, the folder `createNewWorld` uses on every version |
| `createTempDataPackDirFromExistingWorld(Path, Minecraft)` HEAD (called by Re-create only, with `<old world>/datapacks`) and `createFromExisting` HEAD | the old world's folder, for the next screen (the Path `createFromExisting` gets is a temp copy) |

`LayerConfig.openWorld` calls `NewWorldDraft.take(worldDir)` first, which always empties the
holder and returns something only if it was armed for that folder name. For a world with no
`generation.json` yet, the stored values are written as its file (log `[Config] Created …, with the
settings chosen on the Create World screen`); without stored values, a Re-create's file is the old
world's `generation.json` read over the defaults (log `a copy of <old>/aronalayers/generation.json
(the world it re-creates)`); otherwise the copy of the defaults as before. `generation_defaults.json`
is only read. A world that already has its file ignores the draft.

So: a cancelled creation drops the draft (`popScreen`); a new Create World screen never sees an
older draft (new owner); a world opened next gets nothing (not armed, or another folder); a
creation that fails before the server starts leaves an armed draft that the next screen or server
start clears, and could only reach a folder of that name without a file; a server that fails after
`openWorld` has already written the draft as that world's file.

**The screen.** `AronaConfigScreen.openForNewWorld(createWorldScreen)` (the shared screen and
1.20.1's copy): a `ConfigDraft.forNewWorld(values, recreated)` (target `NEW_WORLD` or
`RECREATED_WORLD`; `commit()` refuses it) seeded from the stored values, else
`LayerConfig.newWorldValues(source)` (built-in defaults, `generation_defaults.json`, the re-created
world's file; staged, nothing applied). Only the three generation tabs, opening on Terrain, labels
without ⚠ (`config.aronalayers.tab.<tab>.plain`); `lastTab` of the normal screen untouched. Heading
"Settings for this new world" and preview text from `ConfigWidgets.Generation.NEW_WORLD` /
`RECREATED_WORLD` (lang `config.aronalayers.new_world.1-3`, `recreated_world.1-3`). The
generation helpers of `ConfigWidgets` (`generation()`, `showsOptions`, `folder`, `folderTooltip`,
`generationHeading`, `generationText`, new `tabLabel`, `hasFolder`) are now instance methods reading
the draft's target, so no static says which mode a screen is in. Folder is inactive (tooltip: the
folder is made when the world is created). Reset Tab resets to the built-in defaults, as elsewhere.
Done and Escape store the WORLD values for that Create World screen and return to it (which then
opens on its first tab, as after vanilla's Game Rules); Cancel returns without storing.

Statics: everything of one Create World screen is in `NewWorldDraft` and reset as above; the
screen's `showAdvanced` stays a session preference; `ConfigWidgets` and `ConfigPreview` have none.

**What was checked.**

- All 11 jars build (JDK 25); `verify_mixins.py` passes on all 11, with the two new mixins in each
  (the `targets` string checked too).
- A dev client on every one of the 11 jars, driven by a temporary test driver (a `Minecraft.tick`
  mixin and a reflection driver, deleted afterwards; each run in its own throwaway `run/client6`,
  deleted), pressing the real buttons: 24 checks, 0 failures on each. In one client session:
  (A) More tab shows Game Rules, Experiments, Data Packs, Layers…; Layers… opens the screen in
  new-world mode, tabs Terrain / Plants & Trees / Structures, Folder inactive, heading "Settings
  for this new world"; the draft starts from the defaults; `reduce_swamp_layers` on, Done, back on
  the same Create World screen; Layers… again shows it on; `skip_snowy_biomes` on, Cancel; Create:
  the world's `generation.json` has `reduce_swamp_layers` true and `skip_snowy_biomes` false, and the
  running value is true. Leave (vanilla's Save and Quit path): "World closed". (B) a changed draft,
  Done, then the Create World screen cancelled; a new screen creates a world: defaults. (C) a
  second world without the button: defaults. (R) Re-create of world A from the world list: the
  screen knows A's folder, the draft is a re-created world starting from A's values, Cancel on the
  Layers screen, Create: the new world's file is a copy of A's (`reduce_swamp_layers` true). Then
  `generation_defaults.json` byte for byte as before the session.
- No ERROR in the client logs but vanilla's offline "Failed to retrieve profile key pair".

Not checked: a creation that fails (pack copy, server start); the 1.21.11+ Realms create flow;
how the button and screen look (the driver reads widgets, it does not see them).

**JDK paths** on this machine are now `jdk-25.0.5.8-hotspot` (the 25.0.4.7 folder holds only `lib`)
and `jdk-21.0.13.9-hotspot`.

**For the user to check in a client** (each version: 1.20.1, 1.21.1, 1.21.11, 26.1.2, 26.2, 26.3,
both loaders where there are two): Create World → More tab: "Layers…" under Data Packs, same width,
tooltip; it opens a screen with three tabs (no ⚠), the heading "Settings for this new world" centred,
the preview text, Folder greyed with its tooltip; change an option, Done, back on the Create World
screen; Create; `saves/<world>/aronalayers/generation.json` has the change,
`config/aronalayers/generation_defaults.json` does not; resize the window on the More tab (the button
stays in place); Re-create that world (Edit → Re-Create) and open Layers…: the change is there and
the text says the settings start from the world being re-created.

Client test by the user (2026-11-22) on 1.21.1 and 26.3: all fine. The other versions were checked
only by the automated client run above.

### Phase 7 (2026-11-26)

Done in one session, no subagents. Not committed (the user commits each repo). Decided with the user
before changing anything: version 1.0.0-alpha.2; gen's final README on its GitHub default branch
`AronaLayersGen` (not `merge`); a banner on top of each old README with the old text kept below; LSL
updated in this session; the duplicate Compat and Platform classes left as they are; the carry-over of
the old config files kept until 1.0.0 final.

**Names and metadata.** `settings.gradle` `rootProject.name` and `gradle.properties`
`archives_base_name` are `aronalayers`, `mod_version=1.0.0-alpha.2`: jars
`aronalayers-<loader>-1.0.0-alpha.2+mc<mc>.jar`, licence files in the jar `LICENSE_aronalayers` and
`LICENSE-MIT_aronalayers`. No module `build.gradle` changed; nothing else (CI glob, verify_mixins, test scripts)
depended on the old name. The 22 old `aronalayers-blocks-*` jars were deleted from `build/libs`. In the
six `fabric.mod.json`: name "AronaLayers", a description covering blocks, generation and gameplay,
`contact.homepage`, licence `CC-BY-NC-4.0 AND MIT`, `suggests modmenu`. In the five `neoforge.mods.toml`:
the same display name, description and licence. `breaks` / `incompatible` for `aronalayersgen` and
`aronalayersextras` kept.

**Jars in one folder** (asked by the user after the phase): the root `build.gradle` has a
`collectJars` Sync task that every module's `build` is finalized by; it puts each module's release jar
(`remapJar` on the remapping Loom, `jar` elsewhere; no sources jars) in the root `build/libs/`, and
removes jars of an older version from it. It takes the files without depending on their tasks, so
`:vX:<loader>:build` still builds only that module. Checked: a full build gives the 11 jars there,
byte for byte the modules' own.

**CI removed.** The user builds the jars locally and publishes releases by hand, so
`.github/workflows/build.yml` (build on every push, jars as a download) was deleted on their decision;
`verify_mixins.py` is run by Claude after code changes, as in every phase. It now fails on a module
with no jar (it printed and passed before).

**READMEs.** This repo's README is the merged one: versions table, install, features (blocks,
generation, live world), the three config files and per-world `generation.json`, the "Layers…" button,
dedicated servers and `/aronalayers reload`, mapping files, every option per tab (defaults taken from
the files a fresh server writes), commands, upgrading from the old mods, LSL and `LayersApi`, building,
licence split, credits. The licence section lists what is MIT: the `gen` and `gameplay` packages,
`api/LayersApi`, the gen blocks that moved into the blocks packages (`ConvertingLayerBlock`,
`PowderSnowLayerBlockBase`, each version's `PowderSnowLayerBlock`, `EntityTouchingPowderSnowAccessor`,
the powder snow and zinc layer blockstates and models), `models/item/small_ore.json`,
`assets/aronalayers_config/`, `textures/gui/config/`, the four mapping files and `scripts/`.
Gen (`AronaLayersGen`, CRLF kept) and extras (`main`) READMEs start with an archive banner: now part of
AronaLayers, what carries over, ids not converted, Conquest Reforged and wet sand gone with a link to
the last release (gen: its releases page, the last tag is 2.0.0a5 although `gradle.properties` says
2.0.0a6; extras: 1.3.0). Gen's local checkout is now on `AronaLayersGen`; its `merge` branch is
unchanged.

**LSL** (`C:\Users\alexa\OneDrive\Documents\GitHub\lsl`, `main`): `LayerInfo` tries
`io.arona74.aronalayers.api.LayersApi` first, then the old `io.arona74.aronalayersgen.api.LayersApi`;
`VoxyGate.groundPlants` counts `aronalayersgen` loaded or the API found (the blocks-only AronaLayers
before alpha.2 has neither, and no plant grounding); `LslConfig.importFromGen` reads `dh_*` from
`config/aronalayers/config.json`, then `config/aronalayers/layer_config.json`, then
`config/aronalayersgen/layer_config.json`; the six `fabric.mod.json` suggest `aronalayers`; README.
`GenDh` (gen up to 2.0.0a3's own DH support) is unchanged. wgaccel already used the new names.

**Left as they are.** Per version: three Compat classes (blocks, gen, gameplay) and two Platform
classes per loader (blocks, gen). They share no method except a min-Y helper; merging the Platforms
would make `GenMixinPlugin` load the blocks Platform, which names Fabric impl classes and Minecraft
types: the phase 3 crash. Carry-over code (`ConfigFiles` copying `config/aronalayersgen/`,
`ConfigLoader` reading `aronalayersgen:` ids, `LayerConfig` turning `layer_config.json` and
`aronalayersextras.json` into the two files, `OBSOLETE_KEYS`): to delete at 1.0.0 final. No
`TODO`/`TEMPORARY` marker and no unused Compat or Platform method was found.

**What was checked.**

- All 11 jars build (JDK 25) under the new names; `verify_mixins.py` passes on all 11 (Fabric 36 on
  1.20.1 and 1.21.1, 37 on 1.21.11 / 26.1.2 / 26.2, 34 on 26.3; NeoForge 33 on 1.21.1, 34 on the
  others).
- The metadata read back from each built jar: id, name, expanded version, licence, homepage,
  suggests, breaks / incompatible, both licence files.
- A dedicated server on each of the 11 (JDK 21 for 1.20.1 and 1.21.1), throwaway world, no
  `config/aronalayers/` so the default files are written: the mod listed as 1.0.0-alpha.2, the
  world's `generation.json` written, `/aronalayers reload` answered "settings reloaded: nothing
  changed", "World closed" at stop, no line with "error" in any log. Each run folder's
  `server.properties` and `config/aronalayers/` (still with its `layer_config.json`) put back, the
  world deleted.
- LSL builds with the changes.

Not checked: a client of the renamed jars (only the name in
the mod list changed); LSL in game with the merged mod; how the READMEs render on GitHub.
