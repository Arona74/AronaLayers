package io.arona74.aronalayers.fabric;

import io.arona74.aronalayers.AronaLayers;

import net.fabricmc.api.ModInitializer;

/** Fabric entry point. */
public class AronaLayersFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		AronaLayers.init();
	}
}
