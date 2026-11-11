package io.arona74.aronalayers.registry;

import io.arona74.aronalayers.AronaLayers;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

/**
 * Client-only registrations. Kept out of {@link ModRegistries} so that class can run
 * from the common initializer on a dedicated server, where client classes are absent.
 */
public class ModClientRegistries {
	public static void registerTransparentBlocks() {
		BuiltInRegistries.BLOCK.forEach(block -> {
			if (ModRegistryArgs.REGISTRY_ARGS.containsKey(block)) {
				RegistryArgs registryArgs = ModRegistryArgs.REGISTRY_ARGS.get(block);

				if (BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(AronaLayers.MOD_ID) && registryArgs.transparent) {
					BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
				}
			}
		});
	}

	public static void registerTranslucentBlocks() {
		BuiltInRegistries.BLOCK.forEach(block -> {
			if (ModRegistryArgs.REGISTRY_ARGS.containsKey(block)) {
				RegistryArgs registryArgs = ModRegistryArgs.REGISTRY_ARGS.get(block);

				if (BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(AronaLayers.MOD_ID) && registryArgs.translucent) {
					BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.translucent());
				}
			}
		});
	}

	public static void registerFoliage() {
		BuiltInRegistries.BLOCK.forEach(block -> {
			if (ModRegistryArgs.REGISTRY_ARGS.containsKey(block)) {
				ResourceLocation identifier = BuiltInRegistries.BLOCK.getKey(block);
				RegistryArgs args = ModRegistryArgs.REGISTRY_ARGS.get(block);

				if (identifier.getNamespace().equals(AronaLayers.MOD_ID)) {
					if (args.grassTinted) {
						ColorProviderRegistry.BLOCK.register(((state, world, pos, tintIndex) -> {
							if (world == null || pos == null) {
								return GrassColor.getDefaultColor();
							}

							return BiomeColors.getAverageGrassColor(world, pos);
						}), block);
					}

					if (args.foliageTinted) {
						ColorProviderRegistry.BLOCK.register(((state, world, pos, tintIndex) -> {
							if (world == null || pos == null) {
								return FoliageColor.getDefaultColor();
							}

							return BiomeColors.getAverageFoliageColor(world, pos);
						}), block);
					}
				}
			}
		});
	}
}
