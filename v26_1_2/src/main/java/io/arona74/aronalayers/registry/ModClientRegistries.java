package io.arona74.aronalayers.registry;

import java.util.List;

import io.arona74.aronalayers.AronaLayers;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;

/**
 * Client-only registrations. Kept out of {@link ModRegistries} so that class can run
 * from the common initializer on a dedicated server, where client classes are absent.
 */
public class ModClientRegistries {
	// Render layers are no longer registered at runtime in 26.1; both Fabric's
	// BlockRenderLayerMap and vanilla's ItemBlockRenderTypes are gone. The layer is
	// derived from the texture instead: alpha content is detected automatically, so
	// blocks flagged transparent need nothing, and blocks flagged translucent get
	// "force_translucent" on their sprites from ModModelProvider. These two methods
	// are kept as no-ops so the call sites in AronaLayersClient stay stable.
	public static void registerTransparentBlocks() {
	}

	public static void registerTranslucentBlocks() {
	}

	public static void registerFoliage() {
		BuiltInRegistries.BLOCK.forEach(block -> {
			if (ModRegistryArgs.REGISTRY_ARGS.containsKey(block)) {
				Identifier identifier = BuiltInRegistries.BLOCK.getKey(block);
				RegistryArgs args = ModRegistryArgs.REGISTRY_ARGS.get(block);

				if (identifier.getNamespace().equals(AronaLayers.MOD_ID)) {
					if (args.grassTinted) {
						BlockColorRegistry.register(List.of(BlockTintSources.grass()), block);
					}

					if (args.foliageTinted) {
						BlockColorRegistry.register(List.of(BlockTintSources.foliage()), block);
					}
				}
			}
		});
	}
}
