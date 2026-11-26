# AronaLayers

Layer blocks for Minecraft: stackable blocks two pixels tall, like snow layers, for most vanilla
blocks, plus **3D layers** for stair-like steps and cliff faces. The world generator lays them over
the terrain so slopes read as smooth gradients instead of staircases, and the game treats them like
the blocks they stand for: grass spreads onto them, sheep graze them, they fall with sand, ore
layers drop small ores.

One mod for Fabric and NeoForge. It replaces three earlier mods, now archived:
AronaLayers: Blocks (this repository's earlier name),
[AronaLayers-gen](https://github.com/Arona74/AronaLayers-gen) and
[AronaLayers-extras](https://github.com/Arona74/AronaLayers-extras).

## Supported versions

| Minecraft | Fabric Loader | Fabric API | NeoForge | Java |
|-----------|---------------|------------|----------|------|
| 1.20.1 | 0.15.11+ | 0.92.2+ | — | 17 |
| 1.21.1 | 0.16.5+ | 0.102.0+ | 21.1.265+ | 21 |
| 1.21.11 | 0.19.3+ | 0.141.6+ | 21.11.48+ | 21 |
| 26.1.2 | 0.19.3+ | 0.154.2+ | 26.1.2.126+ | 25 |
| 26.2 | 0.19.3+ | 0.156.0+ | 26.2.0.93+ | 25 |
| 26.3 | 0.19.5+ | 0.162.0+ | 26.3.0.102+ | 25 |

Each jar is for one loader and one Minecraft version: `aronalayers-<loader>-<version>+mc<minecraft>.jar`.

## Installation

1. Install Fabric Loader and Fabric API, or NeoForge, for your Minecraft version.
2. Put the AronaLayers jar for your loader and version in the `mods` folder, on **both client and
   server**: the blocks are registered on both sides, and plant grounding changes outlines and
   collision, which both sides must agree on.
3. Optional: [Mod Menu](https://modrinth.com/mod/modmenu) on Fabric, for a config button in the mod
   list (NeoForge has one built in). Without it, `/algconfig` opens the same screen.
4. Optional terrain sources: [ReTerraForged](https://github.com/racoonman2/ReTerraForged) or
   FreeTerraForged, or [Tellus](https://modrinth.com/mod/tellus) for real-world terrain. Vanilla
   terrain, and worldgen mods built on it (Terralith, Tectonic, Lithosphere…), need nothing extra.

On 1.20.1 with Sodium 0.5, plants are drawn lowered only with
[Indium](https://modrinth.com/mod/indium) installed (the log says so otherwise).

**Coming from the old mods:** remove AronaLayers: Blocks, `aronalayersgen-…` and
`aronalayersextras-…` first; AronaLayers refuses to start next to the last two. See
[Upgrading](#upgrading-from-aronalayers-gen-and-aronalayers-extras).

## Features

### Blocks

- **Layers** for most vanilla blocks: stone and its variants, ores, dirt, grass, sand, gravel, snow,
  terracotta, concrete powder, nether and end blocks, and more. Eight heights each; they can be
  placed against walls and ceilings and waterlogged; they burn, oxidise, strip and till like their
  full block, sand, red sand and concrete powder layers fall like sand, and any layer with nothing
  holding it falls.
- **3D layers** (`<name>_3d_layer`) for the hard materials (stone-like blocks, all ores, ancient
  debris, colored terracotta) plus snow, moss and coarse dirt: a floor layer and a wall layer merged
  into one block. At 4/4 it is a stair.
- **Powder snow layer** and **zinc ore layers** (`zinc_ore_layer`, `deepslate_zinc_ore_layer`, for
  terrain with Create's zinc ore), snow-style layers without facing.
- **Small ores** (`aronalayers:small_coal`, `small_raw_iron`, `small_raw_copper`, `small_raw_gold`,
  `small_diamond`, `small_emerald`): eight craft into the normal drop.

A 3D layer has these block state properties:

| Property | Values | Meaning |
|---|---|---|
| `horizontal_facing` | `north`, `south`, `east`, `west` | the side the wall layer is against |
| `vertical_layers` | `0`–`8` | height of the floor layer; `0` is a pure wall layer |
| `horizontal_layers` | `1`–`8` | thickness of the wall layer |
| `waterlogged` | `true`, `false` | |

If either value reaches 8 the block is a full cube and turns into the regular layer at 8 layers. For
world generation, use `Layer3DBlock#getState(verticalLayers, horizontalLayers, facing)`: it also
accepts `horizontalLayers = 0`, which gives the regular floor layer.

### World generation

Layers are placed on the surface as chunks generate, their height taken from a **terrain source**:

| Source | How the layer height is found |
|--------|-------------------------------|
| **Vanilla** | Vanilla decides terrain from a continuous density field and rounds it to whole blocks. The mod reconstructs that field and reads the discarded fraction back, so the layer depth is measured, not guessed. Works with any worldgen built on vanilla's density functions (Terralith, Tectonic, Lithosphere…), whatever their cell sizes and world height. |
| **ReTerraForged / FreeTerraForged** | The mod's own cell height data. |
| **Tellus** | The fractional part of the real-world elevation; land cover decides water and snow. Also in Tellus's direct Distant Horizons LODs, on Tellus builds with an LOD surface hook. |

The mod picks the source per dimension. Columns a carver cut into after the noise stage (cave mouths,
ravine floors) keep vanilla's surface.

Around that:

- **Plants and trees**: plants standing where a layer goes are moved up onto it; trees, huge
  mushrooms and chorus plants generate on top of the layer; saplings grow on layers.
- **Structures**: no layers inside structures, under ceilings, or where a structure raised or lowered
  the ground, with several finer detection modes (see Structures below).
- **Snow**: snowy biomes can be skipped, or covered with vanilla snow layers on snow blocks.
- **Water**: waterlogged layers on ocean and river floors; fewer layers in swamps, puddles kept.
- **3D layers on cliffs** (experimental, TerraForged terrain): floor layers next to a step become 3D
  layers, and hard cliff faces get wall pieces.

What gets which layer is set by `al_block_mappings.json` (full block → layer block).

### In the live world

- **Plant grounding**: plants, and decorations such as lanterns, candles, flower pots and carpets,
  standing on a partial layer are drawn on its surface, with their outline and collision. Plants are
  detected, vanilla and modded alike, not listed. Applies to generated and hand-placed plants.
- **Trunks and posts on layers**: logs, mushroom stems and chorus roots, and fences, walls, panes and
  iron bars, are drawn down to the layer they stand on.
- **Grass and mycelium spreading** onto dirt layers (and from grass layers onto dirt blocks), at
  vanilla's light rules.
- **Sheep** graze grass layers and regrow their wool.
- **Grass blocks don't decay** under a solid block (optional).
- **Layers fall with sand and gravel** under them, when it falls or when a player breaks it; other
  blocks can be added to the list.
- **Small ore drops**: an ore layer mined without Silk Touch drops small ores and experience per
  layer, with Fortune; Silk Touch keeps the layers. Sneak-mining takes one layer off.
- **Breaking a snow layer** leaves the mapped layer of the ground under it.

## Configuration

Everything is in one screen: **Mod Menu** or the NeoForge mod list, or `/algconfig` in game. It has
six tabs: Visuals, Gameplay, Terrain, Plants & Trees, Structures, Debug. The settings live in three
kinds of file:

| File | Holds | Edited from |
|------|-------|-------------|
| `config/aronalayers/config.json` | Visuals, Gameplay and Debug: settings for every world | the screen, anywhere |
| `config/aronalayers/generation_defaults.json` | Terrain, Plants & Trees and Structures: the **defaults for new worlds** | the screen, outside a world |
| `<world>/aronalayers/generation.json` | that world's generation settings | the screen, in that world (singleplayer or LAN host) |

Every world has its own `generation.json`. It is made from `generation_defaults.json` the first time
the world is opened (existing worlds included), and a setting missing from it takes its value from
the defaults file. Changing a generation setting only affects chunks generated afterwards.

Each file explains itself: every key has a `_comment_<key>` line next to it, refreshed at each start.
The mod rewrites the files at start and on save, keeping your values.

**New worlds.** On the Create World screen, the **More** tab has a **Layers…** button. It opens the
three generation tabs for the world being created, starting from the defaults (or, for Re-Create,
from the world being re-created). Done keeps the choice for that world only; the defaults file is
not touched.

**Dedicated servers.** The world's file is `<server>/<world>/aronalayers/generation.json`, and
`config/aronalayers/config.json` holds the rest. After editing them, run `/aronalayers reload`. A
client connected to a server sees "Set by the server" on the generation tabs; Visuals and Debug
still edit the client's own `config.json`.

**Mapping files**, global, in `config/aronalayers/`, written on first start:

| File | What it lists |
|------|---------------|
| `al_block_mappings.json` | full block → layer block, for generation and snow breaking |
| `plant_mappings.json` | plants moved up onto a layer placed under them (`plants` list) |
| `second_pass_cleanup_blocks.json` | blocks that trigger the second-pass cleanup |
| `structure_elevation_blocks.json` | blocks that mark structure-raised terrain |

A mapping naming a block that does not exist is skipped and logged as
`Layer block not found in registry`.

### Options

The defaults below are what a fresh install writes. "Needs a restart" and the requirements between
options are in each key's comment and in the screen.

#### Visuals

| Key | Default | What it does |
|---|---|---|
| `trunks_on_layers` | `true` | Logs, huge mushroom stems and chorus roots standing on a layer are drawn down to it, with outline and collision. Trees On Layers needs it. Restart. |
| `posts_on_layers` | `true` | Fences, walls, panes and iron bars on a layer are drawn down to it, and animals cannot slip under them. Restart. |
| `posts_on_layers_mode` | `"LOWER"` | `LOWER`: the whole post is lowered onto the layer. `EXTEND`: the post reaches down, its top stays level with its neighbours. Restart. |
| `lowered_posts_reach_up` | `false` | A lowered fence or wall reaches back up to a block standing on it. |
| `plant_grounding` | `true` | Plants on a partial layer sit on its surface instead of floating. |
| `plant_grounding_shapes` | `true` | Also lower their outline and collision. `false` = visual only. |
| `ground_decorations` | `true` | Also lower lanterns, floor torches, candles, flower pots, carpets, pressure plates, brewing stands and cakes. |
| `plant_grounding_force_blocks` | `["minecraft:light_gray_petals"]` | Block ids to ground although detection rejects them. |
| `plant_grounding_exclude_blocks` | `[]` | Block ids never to ground. |

#### Gameplay

| Key | Default | What it does |
|---|---|---|
| `break_snow_layers_to_mapped_layers` | `true` | Breaking a snow layer leaves the mapped layer of the block below, one lower. |
| `grass_spreading` | `true` | Grass spreads onto dirt layers in the light, from grass layers and grass blocks. |
| `mycelium_spreading` | `true` | Mycelium layers spread onto dirt layers and dirt blocks, at any light level. |
| `sheep_eat_grass_layers` | `true` | Sheep graze the grass layer they stand on, which becomes a dirt layer. |
| `prevent_grass_decay` | `true` | A grass block under a solid block stays grass. |
| `layers_fall_with_sand` | `true` | Layers resting on sand, red sand or gravel fall with it. |
| `additional_falling_blocks` | `[]` | Other block ids that fall with the sand or gravel under them, such as `somemod:pebbles`. |
| `small_ore_drops` | `true` | Ore layers mined without Silk Touch drop small ores and experience per layer. |

#### Debug

`debug_logging` (`false`) is the master switch. The per-topic switches below it (`true` by default)
only log while it is on: `debug_log_chunk_init`, `debug_log_rtf`, `debug_log_structure`,
`debug_log_skip_elevated`, `debug_log_structure_no_layers`, `debug_log_second_pass`,
`debug_log_plants`, `debug_log_correction`, `debug_log_snow`, `debug_log_tree_soil`,
`debug_log_vanilla`, `debug_log_tellus`.

#### Terrain (per world)

| Key | Default | What it does |
|---|---|---|
| `layer_injection` | `true` | Master switch for layer generation. |
| `injection_mode` | `"POST_FEATURES"` | `POST_FEATURES`: after structures, cleaner. `CARVERS`: before them. Distant Horizons' own generation only gets layers with `POST_FEATURES`. |
| `3D_layer_injection` | `false` | Experimental, TerraForged terrain: 3D layers next to steps and wall pieces on hard cliff faces. `/algdebug wall3d` explains a cell's result. |
| `3D_layer_cross_chunk` | `false` | Let 3D layers read the wall they lean against in the next chunk. |
| `3D_layer_excluded_blocks` | `["minecraft:snow_block"]` | Materials that never get a 3D layer. |
| `tellus_reduce_layer_count` | `true` | Tellus: one layer less per column (`false` follows the real elevation more closely). |
| `fractional_surface_reduce_layer_count` | `false` | Vanilla: one layer less per column, as the TerraForged path does. |
| `fractional_surface_min_gradient` | `0.0` | Vanilla: smallest density change per block at which the reconstructed surface is trusted. Raise only with `/algdebug` in front of you. |
| `fractional_surface_max_disagreement` | `1.0` | Vanilla: share of a chunk's columns that may disagree with the density field before the chunk is left alone. |
| `fractional_surface_exact_interpolation` | `false` | Vanilla: interpolate like vanilla's `NoiseChunk`, at the cost of a rebuild per chunk. |
| `skip_snowy_biomes` | `false` | No layers in snowy biomes. |
| `improve_snowy_biomes` | `false` | In snowy biomes every layer is a vanilla snow layer at full depth, on a snow block. |
| `super_snowy_mode` | `"ALL_SNOWY_BIOMES"` | Where that applies: every biome cold enough to snow, or `CONTROLLED_BY_LIST`. |
| `super_snowy_biomes` | the vanilla snowy biomes | The biome ids for `CONTROLLED_BY_LIST`. |
| `underwater_layers` | `true` | Waterlogged layers on ocean and river floors. |
| `replace_dirt_path` | `true` | A dirt path under a layer becomes the layer's full block. |
| `reduce_swamp_layers` | `false` | Fewer layers in swamp biomes. |
| `reduce_swamp_layers_value` | `2` | How many eighths fewer. |
| `swamp_puddles_as_water` | `true` | Leave swamp puddles as water. |

#### Plants & Trees (per world)

| Key | Default | What it does |
|---|---|---|
| `plant_injection` | `true` | A plant listed in `plant_mappings.json` standing where a layer goes is moved up onto it. Plants can stand on layers either way. |
| `tree_injection` | `true` | Trees can generate through layer blocks. |
| `trees_on_layers` | `true` | Generated trees, huge mushrooms and chorus plants stand on the layer, saplings grow on it. Needs `trunks_on_layers`. |

#### Structures (per world)

| Key | Default | What it does |
|---|---|---|
| `structure_injection` | `true` | No layers inside structure pieces. |
| `cross_chunk_structure_detection` | `false` | Also check structures starting in neighbouring chunks. Turn off while pre-generating. |
| `structure_footprint_check` | `false` | No layers within a structure's whole footprint (plazas, paths between buildings). |
| `structure_footprint_below_margin` | `8` | Blocks below the structure still covered. |
| `structure_footprint_above_margin` | `4` | Blocks above the structure still covered. |
| `structure_injection_extra_bounds` | `false` | Widen structure boxes by a margin. |
| `structure_injection_extra_bounds_distance` | `4` | The margin, in blocks. |
| `enclosed_space_check` | `true` | No layers under a ceiling (caves, buildings; tree canopies don't count). |
| `enclosed_space_height` | `7` | How far up to look for one. |
| `structure_cleanup` | `false` | After generation, remove layers under a ceiling or on structure materials. |
| `structure_no_layers` | `false` | Remove layers where a structure raised the ground by 2+ blocks (heightmap-based). |
| `second_pass_cleanup` | `false` | Taper the layers next to removed ones, so edges are not abrupt. |
| `conservative_surface_heightmap` | `true` | TerraForged: skip columns whose surface differs from the terrain's own height. |
| `conservative_surface_tolerance_up` / `_down` | `0` / `0` | How many blocks of difference are allowed. |
| `conservative_surface_fallback` | `true` | Place a fixed layer on such a column instead of none. |
| `conservative_surface_fallback_value_up` / `_down` | `0` / `4` | That layer's height (0 = none) where the ground was raised / lowered. |
| `structure_skip_elevated` | `false` | TerraForged: no layers where a structure raised the ground. |
| `structure_replace_elevated` | `false` | Replace a block raised by exactly one with a layer instead. |
| `structure_replace_elevated_high` / `_high_max` | `false` / `3` | Also for higher raises, up to this many blocks. |
| `structure_skip_extra_cleanup` / `_distance` | `false` / `2` | Also clear layers within this radius of a raised spot. |

## Commands

| Command | Who | What it does |
|---------|-----|--------------|
| `/algconfig` | any player, client side | Opens the config screen. |
| `/aronalayers reload` | operators (level 2) | Rereads `config.json`, `generation_defaults.json` and the world's `generation.json`. Nothing changes if a file is invalid; settings that need a restart keep their running value. Mapping files are not reloaded. |
| `/algdebug` | operators | What the generator sees in your chunk: surface, heights, mappings, and on vanilla terrain the reconstructed surface and why a column got no layer. |
| `/algdebug wall3d`, `/algdebug walls3d` | operators | The 3D layer decision for the cell you look at, or tallied over your chunk. |
| `/algdebug plant` | operators | How plant grounding treats the block you look at. |
| `/algtellus [grid]` | operators | Tellus: elevation, predicted and built surface, and the layer, for your column or an area. |
| `/algdebug gameplay mine <pos> [silk_touch\|fortune\|sneak\|sneak_silk_touch]`, `/algdebug gameplay anchor <pos>\|clear` | operators | Test helpers: mine a block with a fake player, or make spreading run around a position with nobody near. |

## Upgrading from AronaLayers-gen and AronaLayers-extras

- **Settings carry over** on the first start. An old `config/aronalayersgen/` is copied to
  `config/aronalayers/`; its `layer_config.json` is split into `config.json` and
  `generation_defaults.json` and then deleted; `config/aronalayersextras.json` is read once into the
  Gameplay settings. The old folder and file are left where they were.
- **Existing worlds** get a `generation.json` from those defaults the first time they open, so they
  keep generating as before.
- **Block and item ids are not converted.** Blocks and items with an `aronalayersgen:` or
  `aronalayersextras:` id in existing worlds (powder snow and zinc layers, `deepslate_layer`,
  `moss_layer`, small ores) disappear; AronaLayers has them as `aronalayers:*`. Layers of
  AronaLayers: Blocks keep their ids.
- **Conquest Reforged support is gone** (rocks, extra foliage, NBT trees, CR layers, wet sand). For
  it, stay on the last releases of AronaLayers-gen and AronaLayers-extras.
- **Mod Menu + Cloth Config** are no longer needed for the gameplay settings.

## Distant Horizons and Voxy

Layers in LOD renderers are drawn by a separate mod, **LSL - Layer Supported LOD**. It reads
AronaLayers' layer knowledge (mappings, 3D layers, wall pieces) through
`io.arona74.aronalayers.api.LayersApi`, by reflection. That class is the mod's public API: its static
signatures stay stable and `LayersApi.VERSION` (2) goes up when it grows. Version 2 added
`placeLayers(ServerLevel, ChunkAccess)`, which places layers into a chunk built outside the world,
for LOD generators.

## Building from source

```
./gradlew build
```

Gradle needs JDK 25 to run, and JDK 21 for the 1.20.1 to 1.21.11 projects (Gradle finds installed
JDKs itself; elsewhere, pass them with `-Porg.gradle.java.installations.paths=<jdk21>,<jdk25>`). `runServer`,
`runClient` and `runDatagen` for 1.20.1 and 1.21.1 must run with `JAVA_HOME` set to JDK 21.

| Folder | Contents |
|---|---|
| `src/main/` | code and resources for every version and loader |
| `src/fabric/`, `src/neoforge/` | one loader's code for every version (entry points) |
| `vX/common/` | one version's code for every loader (`Compat` classes, version mixins) and the data written by datagen |
| `vX/fabric/`, `vX/neoforge/` | one version's loader code (`Platform`, datagen, client models) and its Gradle project |

Jars land in `vX/<loader>/build/libs/`, and after every build all of them are also copied to
`build/libs/` at the root, in one folder. Build one with `./gradlew :v1_21_1:fabric:build`.

`python scripts/verify_mixins.py` checks that every mixin target in the built jars exists in its
Minecraft version (run it after a build, from the repository root). A wrong target still builds and
only fails when the game loads.

## A note on how this mod is developed

I use Claude AI as a development aid, mainly for the repetitive parts of multi-version maintenance,
tracking down what a renamed Minecraft API became, debugging, reading other mods' sources to find
how to implement my layer mechanics, etc.

That is where the help stops. I decide what goes in, I read the changes, and I test in game before
releasing. Fixes get re-tested after the fact, not assumed.

I am saying this because "AI-assisted" often means code nobody checked. That is not what this is. If
you find a bug anyway, please open an issue — I would rather hear about it.

## License

This mod is under two licences, depending on where each part comes from:

- **[CC BY-NC 4.0](LICENSE)** for everything derived from Vanilla+ Layers: the layer blocks, their
  code, models, textures and data. This is everything not listed below.
- **[MIT](LICENSE-MIT)** for the code written for AronaLayers-gen and AronaLayers-extras:
  - the `gen` and `gameplay` packages, in every source folder
    (`src/*/java/io/arona74/aronalayers/{gen,gameplay}/`, `vX/*/src/main/java/io/arona74/aronalayers/{gen,gameplay}/`);
  - `api/LayersApi.java`;
  - the generator's blocks: `custom_blocks/ConvertingLayerBlock.java`, `PowderSnowLayerBlockBase.java`,
    each version's `PowderSnowLayerBlock.java`, `mixin/EntityTouchingPowderSnowAccessor.java`, and the
    blockstates and models of `powder_snow_layer`, `zinc_ore_layer` and `deepslate_zinc_ore_layer`;
  - `models/item/small_ore.json`;
  - `assets/aronalayers_config/` (the config screen's texts), `assets/aronalayers/textures/gui/config/`,
    and the four mapping files at the root of `src/main/resources/`;
  - `scripts/`.

The mod's metadata gives the licence as `CC-BY-NC-4.0 AND MIT`.

## Credits

AronaLayers is based on [Vanilla+ Layers](https://modrinth.com/mod/vanilla+-layers) by **Fellter**
([source](https://github.com/Fellteros/vanillalayerplus)). It has been modified: renamed, ported to
Mojang mappings and to NeoForge, and extended with 3D layers, world generation and layer mechanics.

Made by Arona74.
