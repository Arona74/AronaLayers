package io.arona74.aronalayers.custom_blocks;

import io.arona74.aronalayers.block.LayerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class RedstoneLayerBlock extends LayerBlock {
	public RedstoneLayerBlock(Properties settings) {
		super(settings);
	}

	public boolean isSignalSource(BlockState state) {
		return true;
	}

	public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return (state.getValue(LAYERS) * 2) - 1;
	}
}
