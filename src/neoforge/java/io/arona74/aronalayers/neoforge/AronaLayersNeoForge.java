package io.arona74.aronalayers.neoforge;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.gen.AronaLayersGen;
import io.arona74.aronalayers.platform.Platform;

import net.minecraft.core.registries.Registries;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * NeoForge entry point. The shared code registers straight into the vanilla registries, which
 * NeoForge only allows while it fires RegisterEvent, so it runs from the event for blocks.
 * Terrain generation's setup only adds event listeners and reads its config, so it runs here.
 * Platform and the two ClientSetup classes are each Minecraft version's own.
 */
@Mod(AronaLayers.MOD_ID)
public class AronaLayersNeoForge {
	public AronaLayersNeoForge(IEventBus modBus, ModContainer container, Dist dist) {
		modBus.addListener(RegisterEvent.class, event -> {
			if (event.getRegistryKey().equals(Registries.BLOCK)) {
				AronaLayers.init();
			}
		});
		Platform.addListeners(modBus);
		AronaLayersGen.init();

		if (dist.isClient()) {
			ClientSetup.addListeners(modBus);
			io.arona74.aronalayers.gen.neoforge.ClientSetup.init(modBus, container);
		}
	}
}
