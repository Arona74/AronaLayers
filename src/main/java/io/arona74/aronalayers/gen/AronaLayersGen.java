package io.arona74.aronalayers.gen;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.gen.command.ChunkDebugCommand;
import io.arona74.aronalayers.gen.command.ConfigCommand;
import io.arona74.aronalayers.gen.command.TellusDebugCommand;
import io.arona74.aronalayers.gen.grounding.GroundingFlags;
import io.arona74.aronalayers.gen.injection.RTFLayerInjector;
import io.arona74.aronalayers.gen.platform.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loader-independent setup of terrain generation with layers (the former Arona Layers Generator
 * mod). The mod's entry points (fabric.AronaLayersFabric, neoforge.AronaLayersNeoForge) call
 * {@link #init()}; the layer blocks themselves are registered by {@link AronaLayers#init()}.
 */
public final class AronaLayersGen {
    private AronaLayersGen() {}

    public static final String MOD_ID = AronaLayers.MOD_ID;
    public static final Logger LOGGER = LoggerFactory.getLogger("AronaLayers/Gen");

    public static void init() {
        LOGGER.info("Initializing terrain generation with layers");

        Platform.registerCommands(dispatcher -> {
            ChunkDebugCommand.register(dispatcher);
            TellusDebugCommand.register(dispatcher);
            ConfigCommand.register(dispatcher);
        });

        // Each world's generation settings are loaded as its server starts (and logged then).
        WorldLifecycle.register();

        // Re-map layers whose RTF surface was swapped to beach/river sand after POST_FEATURES
        // injection; runs at chunk-load so it catches spawn/cross-border chunks the in-init pass misses.
        Platform.onChunkLoad(RTFLayerInjector::onChunkLoadRemap);

        // Layers in the Distant Horizons LODs Tellus builds without chunks (Tellus builds with the hook).
        io.arona74.aronalayers.gen.injection.TellusCompat.registerLodSurfaceHook();

        // Plant grounding answers "not grounded" until now: shapes are queried during vanilla's
        // block bootstrap, before the config can safely load (see GroundingFlags).
        GroundingFlags.ready = true;
    }
}
