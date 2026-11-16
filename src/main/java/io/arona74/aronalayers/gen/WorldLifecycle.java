package io.arona74.aronalayers.gen;

import io.arona74.aronalayers.gen.injection.LayerBackend;
import io.arona74.aronalayers.gen.injection.LayerPlacementHelper;
import io.arona74.aronalayers.gen.injection.PreStructureHeightmapStorage;
import io.arona74.aronalayers.gen.injection.RTFCompat;
import io.arona74.aronalayers.gen.injection.RandomStateHolder;
import io.arona74.aronalayers.gen.injection.TellusCompat;
import io.arona74.aronalayers.gen.platform.Platform;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

/**
 * What terrain generation does as a world opens and closes, on a dedicated server and on the
 * integrated server of singleplayer and LAN alike.
 *
 * <p>A server starting loads its world's generation settings ({@link LayerConfig#openWorld}) before
 * it creates its levels, so before any chunk, spawn chunks included, can generate. A server
 * stopping puts the defaults for new worlds back. Both forget the static worldgen state that belongs
 * to one world: one client session can open several worlds one after the other.
 *
 * <p>Its own class, not part of AronaLayersGen, so that nothing loaded early (the mixin plugin,
 * block bootstrap) names MinecraftServer.
 */
public final class WorldLifecycle {
    private WorldLifecycle() {}

    static void register() {
        Platform.onServerStarting(WorldLifecycle::starting);
        Platform.onServerStopped(server -> stopped());
    }

    private static void starting(MinecraftServer server) {
        // Also here, in case the last server of this game did not stop cleanly.
        resetWorldState();
        LayerConfig.openWorld(server.getWorldPath(LevelResource.ROOT));
    }

    private static void stopped() {
        LayerConfig.closeWorld();
        resetWorldState();
    }

    /**
     * Worldgen state kept in static fields that belongs to one world. ChunkMapMixin already clears
     * the first three when a dimension's chunk map closes; they are cleared here too so that a new
     * world never starts with them. The rest is keyed by chunk position or by the old server's
     * objects. The ThreadLocals of the worldgen code are cleared as each chunk or tree finishes,
     * and Layer3D's excluded-blocks cache by {@link LayerConfig#onValuesChanged()}.
     */
    private static void resetWorldState() {
        RandomStateHolder.clear();
        RTFCompat.resetWorldState();
        LayerBackend.clear();
        PreStructureHeightmapStorage.clear();
        TellusCompat.resetWorldState();
        LayerPlacementHelper.resetWorldState();
    }
}
