package io.arona74.aronalayers;

import io.arona74.aronalayers.registry.ModClientRegistries;

import net.fabricmc.api.ClientModInitializer;

public class AronaLayersClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModClientRegistries.registerFoliage();
		ModClientRegistries.registerTranslucentBlocks();
		ModClientRegistries.registerTransparentBlocks();
	}
}
