package io.arona74.aronalayers.custom_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * A snow-style layer (1-8 layers, the LAYERS property only, height-based collision) with another
 * block's texture, for a full block that belongs to another mod (Create's zinc ores). Terrain
 * generation places it where that block is the surface.
 *
 * <p>At layers=8 it turns into the full block, as powder_snow_layer turns into powder_snow.
 * Worldgen places the full block directly (see LayerPlacementHelper), so this conversion only
 * handles runtime placement (a player stacking layers to 8).
 */
public class ConvertingLayerBlock extends SnowLayerBlock {
    private final String fullBlockId;
    private volatile Block fullBlock;

    /**
     * @param fullBlockId id of the full block, looked up when first needed: its mod may register
     *                    its blocks after this one, or not be installed
     */
    public ConvertingLayerBlock(Properties settings, String fullBlockId) {
        super(settings);
        this.fullBlockId = fullBlockId;
    }

    /** The full block this layer becomes at layers=8, or AIR when its mod is not installed. */
    public Block getFullBlock() {
        Block full = fullBlock;
        if (full == null) {
            full = Blocks.AIR;
            for (Block block : BuiltInRegistries.BLOCK) {
                if (BuiltInRegistries.BLOCK.getKey(block).toString().equals(fullBlockId)) {
                    full = block;
                    break;
                }
            }
            fullBlock = full;
        }
        return full;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onPlace(state, world, pos, oldState, notify);
        Block full = getFullBlock();
        if (!world.isClientSide() && state.getValue(BlockStateProperties.LAYERS) == 8 && full != Blocks.AIR) {
            world.setBlock(pos, full.defaultBlockState(), Block.UPDATE_ALL);
        }
    }
}
