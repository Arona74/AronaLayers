package io.arona74.aronalayers.gameplay;

import io.arona74.aronalayers.block.ModBlocks;
import io.arona74.aronalayers.custom_blocks.GrassLayerBlock;
import io.arona74.aronalayers.gen.LayerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Grass and mycelium spreading onto layers (options grass_spreading and mycelium_spreading).
 *
 * <p>Every level tick, in the 3x3 chunks around each player, a few random columns are looked at
 * from the top down. If the first solid block is grass or mycelium, it tries to spread:
 *
 * <ul>
 *   <li>a grass layer, onto dirt layers and dirt blocks;</li>
 *   <li>a grass block, onto dirt layers only: onto dirt blocks it spreads by its own random tick;</li>
 *   <li>a mycelium layer, onto dirt layers and dirt blocks. A mycelium block does not spread onto
 *       layers.</li>
 * </ul>
 *
 * A target is only converted where it would stay converted: the grass layer and the vanilla grass
 * and mycelium blocks turn back into dirt on their own random tick when covered, so a dirt layer
 * must pass {@link GrassLayerBlock#canBeGrass} and a dirt block must have air above.
 */
public final class LayerSpreadHandler {
    private LayerSpreadHandler() {}

    /** Chunks around each player that are looked at: 1 is a 3x3 area. */
    private static final int CHUNK_RADIUS = 1;

    static void onLevelTick(ServerLevel level) {
        boolean grass = LayerConfig.GRASS_SPREADING;
        boolean mycelium = LayerConfig.MYCELIUM_SPREADING;
        if (!grass && !mycelium) return;

        int randomTickSpeed = Compat.randomTickSpeed(level);
        if (randomTickSpeed <= 0) return;

        // One column in five, a third of the random tick speed times per chunk: close to the pace
        // of vanilla spreading, since every column looked at goes straight to its surface block.
        int attempts = Math.max(1, randomTickSpeed / 3);

        for (ServerPlayer player : level.players()) {
            spreadAround(level, player.blockPosition().getX() >> 4, player.blockPosition().getZ() >> 4, attempts, grass, mycelium);
        }
        for (Anchor anchor : ANCHORS) {
            if (anchor.level() == level) spreadAround(level, anchor.chunkX(), anchor.chunkZ(), attempts, grass, mycelium);
        }
    }

    private static void spreadAround(ServerLevel level, int chunkX, int chunkZ, int attempts, boolean grass, boolean mycelium) {
        RandomSource random = level.getRandom();
        int bottom = Compat.bottomY(level);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int dx = -CHUNK_RADIUS; dx <= CHUNK_RADIUS; dx++) {
            for (int dz = -CHUNK_RADIUS; dz <= CHUNK_RADIUS; dz++) {
                LevelChunk chunk = level.getChunkSource().getChunkNow(chunkX + dx, chunkZ + dz);
                if (chunk == null) continue;

                for (int i = 0; i < attempts; i++) {
                    if (random.nextInt(5) != 0) continue;

                    int x = ((chunkX + dx) << 4) + random.nextInt(16);
                    int z = ((chunkZ + dz) << 4) + random.nextInt(16);
                    // Nothing above the highest motion-blocking block is solid or grass.
                    int top = chunk.getHeight(Heightmap.Types.MOTION_BLOCKING, x & 15, z & 15);

                    for (int y = top; y > bottom; y--) {
                        cursor.set(x, y, z);
                        BlockState state = chunk.getBlockState(cursor);

                        if (grass && (state.is(ModBlocks.GRASS_LAYER) || state.is(Blocks.GRASS_BLOCK))) {
                            spreadGrass(level, cursor.immutable(), state, random);
                            break;
                        } else if (mycelium && state.is(ModBlocks.MYCELIUM_LAYER)) {
                            spreadMycelium(level, cursor.immutable(), random);
                            break;
                        } else if (!state.isAir() && state.canOcclude()) {
                            break;
                        }
                    }
                }
            }
        }
    }

    // ---- test anchors (command.GameplayDebugCommand): places treated as if a player stood there

    private record Anchor(ServerLevel level, int chunkX, int chunkZ) {}

    private static final List<Anchor> ANCHORS = new CopyOnWriteArrayList<>();

    public static void addAnchor(ServerLevel level, BlockPos pos) {
        ANCHORS.add(new Anchor(level, pos.getX() >> 4, pos.getZ() >> 4));
    }

    public static void clearAnchors() {
        ANCHORS.clear();
    }

    private static void spreadGrass(ServerLevel level, BlockPos pos, BlockState source, RandomSource random) {
        if (level.getMaxLocalRawBrightness(pos.above()) < 9) return;
        boolean fromLayer = source.is(ModBlocks.GRASS_LAYER);

        for (int i = 0; i < 4; i++) {
            BlockPos target = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
            if (!level.isLoaded(target) || level.getMaxLocalRawBrightness(target.above()) < 9) continue;
            BlockState targetState = level.getBlockState(target);

            if (targetState.is(ModBlocks.DIRT_LAYER)) {
                convertLayer(level, target, targetState, ModBlocks.GRASS_LAYER);
            } else if (fromLayer && targetState.is(Blocks.DIRT) && level.getBlockState(target.above()).isAir()) {
                level.setBlock(target, Blocks.GRASS_BLOCK.defaultBlockState(), Block.UPDATE_ALL);
            }
        }
    }

    /** Mycelium spreads at any light level. */
    private static void spreadMycelium(ServerLevel level, BlockPos pos, RandomSource random) {
        for (int i = 0; i < 4; i++) {
            BlockPos target = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
            if (!level.isLoaded(target)) continue;
            BlockState targetState = level.getBlockState(target);

            if (targetState.is(ModBlocks.DIRT_LAYER)) {
                convertLayer(level, target, targetState, ModBlocks.MYCELIUM_LAYER);
            } else if (targetState.is(Blocks.DIRT) && level.getBlockState(target.above()).isAir()) {
                level.setBlock(target, Blocks.MYCELIUM.defaultBlockState(), Block.UPDATE_ALL);
            }
        }
    }

    /** Turns a dirt layer into {@code grown}, same height and facing, unless it is covered or under water. */
    private static void convertLayer(ServerLevel level, BlockPos pos, BlockState dirtLayer, Block grown) {
        BlockState state = grown.withPropertiesOf(dirtLayer);
        if (!GrassLayerBlock.canBeGrass(state, level, pos)) return;
        if (level.getFluidState(pos.above()).is(FluidTags.WATER)) return;
        level.setBlock(pos, state, Block.UPDATE_ALL);
    }
}
