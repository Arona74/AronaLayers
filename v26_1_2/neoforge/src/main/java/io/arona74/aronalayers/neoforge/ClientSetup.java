package io.arona74.aronalayers.neoforge;

import java.util.List;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.registry.ModRegistryArgs;

import net.minecraft.client.color.block.BlockTintSources;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

/**
 * NeoForge, Minecraft 26.1.2: client-only registrations, as Fabric's ModClientRegistries does
 * them. Only loaded on the client. Render layers aren't registered since 26.1: they come from
 * the textures, which the generated models mark as translucent where needed.
 */
public final class ClientSetup {
	private ClientSetup() {
	}

	public static void addListeners(IEventBus modBus) {
		modBus.addListener(RegisterColorHandlersEvent.BlockTintSources.class, event -> ModRegistryArgs.REGISTRY_ARGS.forEach((block, args) -> {
			if (!AronaLayers.isNamespaced(block)) {
				return;
			}

			if (args.grassTinted) {
				event.register(List.of(BlockTintSources.grass()), block);
			}

			if (args.foliageTinted) {
				event.register(List.of(BlockTintSources.foliage()), block);
			}
		}));
	}
}
