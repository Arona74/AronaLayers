package net.fellter.vanillalayerplus.custom_blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.state.BlockState;

public class StainedGlassLayerBlock extends GlassLayerBlock implements BeaconBeamBlock {
	private final DyeColor color;

	public StainedGlassLayerBlock(DyeColor color, Properties settings) {
		super(settings);
		this.color = color;
	}

	@Override
	public DyeColor getColor() {
		return color;
	}

	protected boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
		if (stateFrom.getBlock() instanceof BeaconBeamBlock stainable) {
			return stainable.getColor() == color && super.skipRendering(state, stateFrom, direction);
		}

		return false;
	}
}
