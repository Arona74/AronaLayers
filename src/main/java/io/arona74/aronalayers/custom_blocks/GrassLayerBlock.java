package io.arona74.aronalayers.custom_blocks;

import io.arona74.aronalayers.block.LayerBlock;
import io.arona74.aronalayers.block.ModBlocks;
import io.arona74.aronalayers.compat.Compat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

/** The grass layer: turns into a dirt layer when it is covered, like a grass block. */
public class GrassLayerBlock extends LayerBlock {
	public GrassLayerBlock(Properties settings) {
		super(settings);
	}

	/**
	 * Whether a grass-like layer in this state can stay at {@code pos}: not under a full block of
	 * fluid, and, at 8 layers, not under a block that covers its top. Anything that turns a dirt
	 * layer into grass or mycelium must ask first, or the next random tick turns it back.
	 */
	public static boolean canBeGrass(BlockState state, LevelReader world, BlockPos pos) {
		BlockPos blockPos = pos.above();
		BlockState blockState = world.getBlockState(blockPos);

		if (blockState.getFluidState().getAmount() == 8) {
			return false;
		} else if (state.getValue(LAYERS) == 8) {
			return !Compat.occludes(state.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO), blockState.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO), Direction.UP);
		}

		return true;
	}

	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (!canBeGrass(state, world, pos)) {
			world.setBlockAndUpdate(pos, ModBlocks.DIRT_LAYER.withPropertiesOf(state));
		}
	}
}
