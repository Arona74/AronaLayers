# AronaLayers: Blocks

A Fabric mod adding layer blocks (stackable two pixel tall blocks, like snow layers) for most vanilla blocks, plus **3D layers**: a floor layer and a wall layer merged into one block, for stair-like steps, cliff edges and upright wall layers.

It is the block provider for [AronaLayers-gen](https://github.com/Arona74/AronaLayers-gen), which places layers over terrain during world generation, and [AronaLayers-extras](https://github.com/Arona74/AronaLayers-extras).

## 3D layers

A 3D layer has these block state properties:

| Property | Values | Meaning |
|---|---|---|
| `horizontal_facing` | `north`, `south`, `east`, `west` | the side the wall layer is against |
| `vertical_layers` | `0`–`8` | height of the floor layer; `0` is a pure wall layer |
| `horizontal_layers` | `1`–`8` | thickness of the wall layer |
| `waterlogged` | `true`, `false` | |

If either value reaches 8 the block is a full cube and turns into the regular layer at 8 layers. For world generation, use `Layer3DBlock#getState(verticalLayers, horizontalLayers, facing)`: it also accepts `horizontalLayers = 0`, which gives the regular floor layer.

## Credits

AronaLayers: Blocks is based on [Vanilla+ Layers](https://modrinth.com/mod/vanilla+-layers) by **Fellter** ([source](https://github.com/Fellteros/vanillalayerplus)). It has been modified: renamed, ported to Mojang mappings, and extended with 3D layers and other changes.

## License

[CC BY-NC 4.0](LICENSE), like the original mod.
