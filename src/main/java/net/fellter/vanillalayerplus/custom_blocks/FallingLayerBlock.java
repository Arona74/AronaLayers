package net.fellter.vanillalayerplus.custom_blocks;

import net.fellter.vanillalayerplus.block.LayerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class FallingLayerBlock extends LayerBlock {
	public FallingLayerBlock(Properties settings) {
		super(settings);
	}

	protected void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		world.scheduleTick(pos, this, this.getFallDelay());
	}

	protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		world.scheduleTick(pos, this, this.getFallDelay());
		return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}

	protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (canFallThrough(world.getBlockState(pos.below())) && pos.getY() >= world.getMinBuildHeight()) {
			FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(world, pos, state);
			this.configureFallingBlockEntity(fallingBlockEntity);
			return;
		}

		// Nothing to fall into. If the layer has also lost its support, defer to the
		// base class so it breaks rather than floating; this happens when the block
		// below is solid enough to block a fall but is not a full square to rest on.
		if (!state.canSurvive(world, pos)) {
			super.tick(state, world, pos, random);
		}
	}

	protected void configureFallingBlockEntity(FallingBlockEntity entity) {
	}

	protected int getFallDelay() {
		return 2;
	}

	public static boolean canFallThrough(BlockState state) {
		return state.isAir() || state.is(BlockTags.FIRE) || state.liquid() || state.canBeReplaced();
	}

	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		if (random.nextInt(16) == 0) {
			BlockPos blockPos = pos.below();

			if (canFallThrough(world.getBlockState(blockPos))) {
				ParticleUtils.spawnParticleBelow(world, pos, random, new BlockParticleOption(ParticleTypes.FALLING_DUST, state));
			}
		}
	}
}
