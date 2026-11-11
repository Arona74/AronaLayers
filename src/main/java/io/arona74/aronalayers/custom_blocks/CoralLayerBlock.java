package io.arona74.aronalayers.custom_blocks;

import org.jetbrains.annotations.Nullable;

import io.arona74.aronalayers.block.LayerBlock;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class CoralLayerBlock extends LayerBlock {
	private final Block dead;

	public CoralLayerBlock(Block dead, Properties settings) {
		super(settings);
		this.dead = dead;
	}

	protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		// See FlattenableLayerBlock: the base class schedules a tick when support is
		// lost, and that tick lands here, so it has to be handled before the coral
		// death check or the block ends up floating.
		if (!state.canSurvive(world, pos)) {
			super.tick(state, world, pos, random);
			return;
		}

		if (!this.isInWater(world, pos)) {
			world.setBlock(pos, this.dead.withPropertiesOf(state), 2);
		}
	}

	protected BlockState onShapeUpdate(BlockState state, Direction direction, BlockState neighborState, LevelReader world, BlockPos pos, BlockPos neighborPos, Ticks ticks, RandomSource random) {
		if (!this.isInWater(world, pos)) {
			ticks.block(pos, this, 60 + random.nextInt(40));
		}

		return super.onShapeUpdate(state, direction, neighborState, world, pos, neighborPos, ticks, random);
	}

	protected boolean isInWater(BlockGetter world, BlockPos pos) {
		for (Direction direction : Direction.values()) {
			FluidState fluidState = world.getFluidState(pos.relative(direction));

			if (fluidState.is(FluidTags.WATER)) {
				return true;
			}
		}

		return false;
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		if (!this.isInWater(ctx.getLevel(), ctx.getClickedPos())) {
			ctx.getLevel().scheduleTick(ctx.getClickedPos(), this, 60 + ctx.getLevel().getRandom().nextInt(40));
		}

		return super.getStateForPlacement(ctx);
	}
}
