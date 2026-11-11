package net.fellter.vanillalayerplus.custom_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ConcretePowderLayerBlock extends FallingLayerBlock {
	private final BlockState hardened;

	public ConcretePowderLayerBlock(Block hardened, Properties settings) {
		super(settings);
		this.hardened = hardened.defaultBlockState();
	}

	public void onLanding(Level world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, FallingBlockEntity fallingBlockEntity) {
		if (shouldHarden(world, pos, currentStateInPos) && pos != null) {
			world.setBlock(pos, this.hardened
					.setValue(WATERLOGGED, world.getBlockState(pos).getValue(WATERLOGGED))
					.setValue(FACING, world.getBlockState(pos).getValue(FACING))
					.setValue(LAYERS, world.getBlockState(pos).getValue(LAYERS)), Block.UPDATE_ALL);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockPos blockPos = ctx.getClickedPos();
		Level blockView = ctx.getLevel();
		BlockState placementState = super.getStateForPlacement(ctx);

		if (shouldHarden(blockView, blockPos, blockView.getBlockState(blockPos))) {
			if (placementState != null) {
				return this.hardened
						.setValue(WATERLOGGED, placementState.getValue(WATERLOGGED))
						.setValue(FACING, placementState.getValue(FACING))
						.setValue(LAYERS, placementState.getValue(LAYERS));
			}
		}

		return super.getStateForPlacement(ctx);
	}

	private static boolean shouldHarden(BlockGetter world, BlockPos pos, BlockState state) {
		return hardensIn(state) || hardensOnAnySide(world, pos);
	}

	private static boolean hardensOnAnySide(BlockGetter world, BlockPos pos) {
		boolean bl = false;
		BlockPos.MutableBlockPos mutable = pos.mutable();

		for (Direction direction : Direction.values()) {
			BlockState blockState = world.getBlockState(mutable);
			if (direction == Direction.DOWN && !hardensIn(blockState)) continue;
			mutable.setWithOffset(pos, direction);
			blockState = world.getBlockState(mutable);
			if (!hardensIn(blockState) || blockState.isFaceSturdy(world, pos, direction.getOpposite())) continue;
			bl = true;
			break;
		}

		return bl;
	}

	private static boolean hardensIn(BlockState state) {
		return state.getFluidState().is(FluidTags.WATER);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (hardensOnAnySide(world, pos)) {
			return this.hardened
					.setValue(WATERLOGGED, world.getBlockState(pos).getValue(WATERLOGGED))
					.setValue(FACING, world.getBlockState(pos).getValue(FACING))
					.setValue(LAYERS, world.getBlockState(pos).getValue(LAYERS));
		}

		world.scheduleTick(pos, this, this.getFallDelay());
		return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}
}
