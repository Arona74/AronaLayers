package io.arona74.aronalayers.fabric;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.gameplay.AronaLayersGameplay;
import io.arona74.aronalayers.gen.AronaLayersGen;

import net.fabricmc.api.ModInitializer;

/**
 * Fabric entry point. Terrain generation and the vanilla mechanics for layers are set up after
 * the blocks and items they use are registered.
 */
public class AronaLayersFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		AronaLayers.init();
		AronaLayersGen.init();
		AronaLayersGameplay.init();
	}
}
