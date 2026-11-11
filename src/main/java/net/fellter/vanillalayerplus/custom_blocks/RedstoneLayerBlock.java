package net.fellter.vanillalayerplus.custom_blocks;

import net.fellter.vanillalayerplus.block.LayerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class RedstoneLayerBlock extends LayerBlock {
	public RedstoneLayerBlock(Properties settings) {
		super(settings);
	}

	protected boolean isSignalSource(BlockState state) {
		return true;
	}

	protected int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return (state.getValue(LAYERS) * 2) - 1;
	}
}
