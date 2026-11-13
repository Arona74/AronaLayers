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
| 2 | Move gen into AronaLayers (pilot 1.21.1, then the other versions) | AronaLayers | pilot done 2026-11-13 (1.21.1 Fabric + NeoForge, see notes); fan-out to 1.20.1, 1.21.11, 26.1.2, 26.2, 26.3 todo |
| 3 | Port extras into AronaLayers, Fabric + NeoForge | AronaLayers | todo |
| 4 | Config: one registry, new tab order, global vs world scope | AronaLayers | todo |
| 5 | Generation config stored in world data | AronaLayers | todo |
| 6 | Generation settings button on the Create World screen | AronaLayers | todo |
| 7 | Cleanup, README, CI, retire the two old mods | all three | todo |

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
- Python rewrites must keep CRLF line endings.
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
