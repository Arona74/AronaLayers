package io.arona74.aronalayers.neoforge;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.registry.ModRegistryArgs;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

/**
 * NeoForge, Minecraft 1.21.11: client-only registrations, as Fabric's ModClientRegistries does
 * them. Only loaded on the client.
 */
public final class ClientSetup {
	private ClientSetup() {
	}

	public static void addListeners(IEventBus modBus) {
		modBus.addListener(FMLClientSetupEvent.class, event -> event.enqueueWork(() -> ModRegistryArgs.REGISTRY_ARGS.forEach((block, args) -> {
			if (AronaLayers.isNamespaced(block)) {
				if (args.transparent) {
					ItemBlockRenderTypes.setRenderLayer(block, ChunkSectionLayer.CUTOUT);
				}

				if (args.translucent) {
					ItemBlockRenderTypes.setRenderLayer(block, ChunkSectionLayer.TRANSLUCENT);
				}
			}
		})));

		modBus.addListener(RegisterColorHandlersEvent.Block.class, event -> ModRegistryArgs.REGISTRY_ARGS.forEach((block, args) -> {
			if (!AronaLayers.isNamespaced(block)) {
				return;
			}

			if (args.grassTinted) {
				event.register((state, level, pos, tintIndex) -> level == null || pos == null
						? GrassColor.getDefaultColor() : BiomeColors.getAverageGrassColor(level, pos), block);
			}

			if (args.foliageTinted) {
				event.register((state, level, pos, tintIndex) -> level == null || pos == null
						? FoliageColor.FOLIAGE_DEFAULT : BiomeColors.getAverageFoliageColor(level, pos), block);
			}
		}));
	}
}
