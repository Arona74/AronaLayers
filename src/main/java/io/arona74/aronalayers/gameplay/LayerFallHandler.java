package io.arona74.aronalayers.gameplay;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.block.Layer3DBlock;
import io.arona74.aronalayers.block.LayerBlock;
import io.arona74.aronalayers.gen.LayerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Layers resting on sand, red sand or gravel fall with it (option layers_fall_with_sand).
 *
 * <p>A layer already falls by itself when nothing holds it (LayerBlock.tick), but any solid block
 * beside it holds it, so on a beach most layers would stay in the air when the sand under them
 * goes. Here the whole run of layers above the sand is turned into falling blocks at once. The
 * layer's own scheduled tick then finds air and does nothing: no layer falls twice.
 *
 * <p>Two entry points: sand that starts falling (mixin.FallingBlockMixin) and sand a player
 * breaks (the block break event).
 */
public final class LayerFallHandler {
    private LayerFallHandler() {}

    /** Layers stacked above the sand that are looked at. */
    private static final int MAX_STACK = 32;

    static void onBlockBroken(ServerLevel level, Player player, BlockPos pos, BlockState state) {
        if (!LayerConfig.LAYERS_FALL_WITH_SAND) return;
        if (!isSandOrGravel(state)) return;

        dropStackAbove(level, pos);
    }

    public static boolean isSandOrGravel(BlockState state) {
        return state.is(Blocks.SAND) || state.is(Blocks.RED_SAND) || state.is(Blocks.GRAVEL);
    }

    /** Turns the run of layers sitting directly on top of {@code pos} into falling blocks. */
    public static void dropStackAbove(Level level, BlockPos pos) {
        BlockPos checkPos = pos.above();
        for (int i = 0; i < MAX_STACK; i++) {
            BlockState above = level.getBlockState(checkPos);
            if (!fallsWithSand(above)) break;
            // Removed without neighbour updates first, so the layer above it is still whole when
            // the loop reaches it. FallingBlockEntity.fall then has nothing left to remove.
            level.setBlock(checkPos, above.getFluidState().createLegacyBlock(), Block.UPDATE_CLIENTS);
            FallingBlockEntity.fall(level, checkPos, above);
            checkPos = checkPos.above();
        }
    }

    /**
     * Whether a block goes down with the sand under it: this mod's layers that rest on the block
     * below (not those set against a wall or a ceiling), and the blocks of additional_falling_blocks.
     */
    public static boolean fallsWithSand(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof LayerBlock) return state.getValue(LayerBlock.FACING) == Direction.DOWN;
        if (block instanceof Layer3DBlock) return state.getValue(Layer3DBlock.VERTICAL_LAYERS) > 0;
        if (state.isAir()) return false;
        // The snow-style layers (powder snow, zinc ore): every other block of this mod is a layer too.
        if (AronaLayers.isNamespaced(block)) return true;

        List<String> additional = LayerConfig.ADDITIONAL_FALLING_BLOCKS;
        return !additional.isEmpty() && additional.contains(io.arona74.aronalayers.gen.Compat.blockId(block));
    }
}
