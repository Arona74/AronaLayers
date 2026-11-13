package io.arona74.aronalayers.gen.fabric;

import io.arona74.aronalayers.gen.AronaLayersGen;
import net.fabricmc.api.ModInitializer;

/**
 * Fabric entry point of terrain generation, listed in fabric.mod.json after the mod's own
 * (fabric.AronaLayersFabric), which registers the blocks.
 */
public class AronaLayersGenFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AronaLayersGen.init();
    }
}
