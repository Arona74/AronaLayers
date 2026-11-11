package io.arona74.aronalayers.fabric;

import io.arona74.aronalayers.registry.ModClientRegistries;

import net.fabricmc.api.ClientModInitializer;

/** Fabric client entry point. ModClientRegistries is each Minecraft version's own. */
public class AronaLayersFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModClientRegistries.registerFoliage();
		ModClientRegistries.registerTranslucentBlocks();
		ModClientRegistries.registerTransparentBlocks();
	}
}
