package io.arona74.aronalayers.api;

import io.arona74.aronalayers.custom_blocks.ConvertingLayerBlock;
import io.arona74.aronalayers.gen.LayerConfig;
import io.arona74.aronalayers.gen.injection.LayerBackend;
import io.arona74.aronalayers.gen.injection.LayerPlacementHelper;
import io.arona74.aronalayers.gen.injection.RTFCompat;
import io.arona74.aronalayers.gen.injection.RandomStateHolder;
import io.arona74.aronalayers.gen.injection.TellusCompat;
import io.arona74.aronalayers.gen.injection.VanillaLayerInjector;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * What gen knows about layer blocks, for other mods (LSL, Layer Supported LOD, reads it to draw
 * layers in Distant Horizons and Voxy LODs).
 *
 * <p>Gen's own detection covers the configured block mappings, gen's layer blocks and AronaLayers
 * 3D layers, which a mod looking only at shapes cannot fully tell apart. Callers reach this class by reflection, so gen and its callers never need each other's jar
 * to build: keep the class name and these static signatures stable, and bump {@link #VERSION} when a
 * method is added.
 *
 * <p>Safe to call from any thread once gen has initialised. Nothing here caches: callers do.
 */
public final class LayersApi {
    private LayersApi() {}

    /**
     * API revision. 1: isLayerBlock, hasLayerProperty, isFloorLayer, isWallPiece, layerCount, fullBlockOf.
     * 2: placeLayers.
     */
    public static final int VERSION = 2;

    /** A real layer block: one the mappings place, gen's own, a 3D layer, or a *_layer / *_slab with a full block. */
    public static boolean isLayerBlock(Block block) {
        return fullBlockOf(block) != null || LayerPlacementHelper.getMappingRegistry().isLayerBlock(block);
    }

    /** Carries a layer property (vanilla {@code layers}, or a 3D layer's). */
    public static boolean hasLayerProperty(BlockState state) {
        return LayerPlacementHelper.hasLayerProperty(state);
    }

    /** A layer lying on the ground (a layer property, not a wall piece). */
    public static boolean isFloorLayer(BlockState state) {
        return LayerPlacementHelper.isFloorLayer(state);
    }

    /** A layer standing against a wall: a 3D layer, or a layer with a horizontal facing. */
    public static boolean isWallPiece(BlockState state) {
        return LayerPlacementHelper.isWallPiece(state);
    }

    /** Layer count in eighths (1-8). 1 when the state has none. */
    public static int layerCount(BlockState state) {
        return LayerPlacementHelper.readLayerCount(state);
    }

    /** The whole block a layer stands for: its mapping source, gen's own full block, or by name. Null if none. */
    public static Block fullBlockOf(Block layer) {
        Block source = LayerPlacementHelper.getMappingRegistry().getSourceBlock(layer);
        if (source != null) return source;
        if (layer instanceof ConvertingLayerBlock gen && gen.getFullBlock() != Blocks.AIR) return gen.getFullBlock();
        return LayerPlacementHelper.getFullBlock(layer);
    }

    /**
     * Places gen's layers into {@code chunk} as generation does for {@code level}'s chunks (the
     * level's backend: Tellus, TerraForged or the vanilla density field, then the second-pass
     * cleanup), without structures or trees. For LOD generators that build chunks outside the
     * world (wgaccel's Voxy LODs): the chunk holds the terrain's top blocks and fluids, its
     * worldgen heightmaps primed. Returns false when the level gets no layers (layer injection
     * off, a ceiling dimension, an unsupported generator).
     */
    public static boolean placeLayers(ServerLevel level, ChunkAccess chunk) {
        if (!LayerConfig.LAYER_INJECTION) return false;
        ChunkGenerator generator = level.getChunkSource().getGenerator();
        RandomState randomState = RandomStateHolder.noiseConfigFor(level);
        LayerBackend backend = LayerBackend.of(level, generator, randomState);
        switch (backend) {
            case TELLUS -> TellusCompat.injectLayersWithTellus(chunk, generator);
            case TERRAFORGED -> RTFCompat.injectLayersWithRTF(chunk, randomState);
            case VANILLA -> VanillaLayerInjector.injectLayers(chunk, randomState, generator);
            case NONE -> {
                return false;
            }
        }

        LayerPlacementHelper.runSecondPassCleanup(chunk);
        return true;
    }
}
