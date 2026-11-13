package io.arona74.aronalayers.fabric;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.gen.AronaLayersGen;

import net.fabricmc.api.ModInitializer;

/** Fabric entry point. Terrain generation is set up after the blocks it places are registered. */
public class AronaLayersFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		AronaLayers.init();
		AronaLayersGen.init();
	}
}
