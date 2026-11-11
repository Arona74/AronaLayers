package io.arona74.aronalayers.block;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LayerBlock extends Block implements SimpleWaterloggedBlock {
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static VoxelShape[] FLOOR_LAYERS_TO_SHAPE;
	protected static VoxelShape[] NORTH_LAYERS_TO_SHAPE;
	protected static VoxelShape[] SOUTH_LAYERS_TO_SHAPE;
	protected static VoxelShape[] EAST_LAYERS_TO_SHAPE;
	protected static VoxelShape[] WEST_LAYERS_TO_SHAPE;
	protected static VoxelShape[] CEILING_LAYERS_TO_SHAPE;

	public LayerBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.getStateDefinition().any().setValue(LAYERS, 1).setValue(WATERLOGGED, false).setValue(FACING, Direction.DOWN));
	}

	protected boolean canPathfindThrough(BlockState state, BlockGetter world, PathComputationType type) {
		if (Objects.requireNonNull(type) == PathComputationType.LAND && state.getValue(FACING) == Direction.DOWN) {
			return state.getValue(LAYERS) < 5;
		}

		return false;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return getBlockSupportShape(state, world, pos);
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return getBlockSupportShape(state, world, pos);
	}

	@Override
	protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
		Direction direction = state.getValue(FACING);
		VoxelShape voxel;
		switch (direction) {
			case WEST -> voxel = WEST_LAYERS_TO_SHAPE[state.getValue(LAYERS)];
			case EAST -> voxel = EAST_LAYERS_TO_SHAPE[state.getValue(LAYERS)];
			case SOUTH -> voxel = SOUTH_LAYERS_TO_SHAPE[state.getValue(LAYERS)];
			case NORTH -> voxel = NORTH_LAYERS_TO_SHAPE[state.getValue(LAYERS)];
			case UP -> voxel = CEILING_LAYERS_TO_SHAPE[state.getValue(LAYERS)];
			case DOWN -> voxel = FLOOR_LAYERS_TO_SHAPE[state.getValue(LAYERS)];
			default -> throw new MatchException(null, null);
		}

		return voxel;
	}

	@Override
	protected VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return getShape(state, world, pos, context);
	}

	@Override
	protected boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

	@Override
	protected float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
		return state.getValue(LAYERS) == 8 ? 0.35f : 1.0f;
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		for (Direction direction : UPDATE_SHAPE_ORDER) {
			boolean canPlace = world.getBlockState(pos.relative(direction)).isFaceSturdy(world, pos, direction);
			if (canPlace) return true;
		}

		return false;
	}

	@Override
	public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
		if (player.isShiftKeyDown() && state.getValue(LAYERS) != 1) {
			world.setBlockAndUpdate(pos, state.setValue(LAYERS, state.getValue(LAYERS) - 1));
			player.awardStat(Stats.BLOCK_MINED.get(this));
			player.causeFoodExhaustion(0.005F);
			popResource(world, pos, new ItemStack(this));
		} else {
			super.playerDestroy(world, player, pos, state, blockEntity, tool);
		}
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!state.canSurvive(world, pos)) {
			// Schedule a tick instead of immediately breaking, so the block can fall as an entity
			world.scheduleTick(pos, this, 2);
		}

		if (state.getValue(WATERLOGGED)) {
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}

		return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (!state.canSurvive(world, pos)) {
			if (FallingBlock.isFree(world.getBlockState(pos.below()))) {
				FallingBlockEntity.fall(world, pos, state);
			} else {
				world.destroyBlock(pos, true);
			}
		}
	}

	@Override
	public boolean canPlaceLiquid(@Nullable Player player, BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
		return state.getValue(BlockStateProperties.LAYERS) < 8;
	}

	@Override
	public ItemStack pickupBlock(@Nullable Player player, LevelAccessor world, BlockPos pos, BlockState state) {
		if (state.getValue(WATERLOGGED)) {
			world.setBlock(pos, state.setValue(WATERLOGGED, false), 3);

			if (!state.canSurvive(world, pos)) {
				world.destroyBlock(pos, true);
			}

			return new ItemStack(Items.WATER_BUCKET);
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		int i = state.getValue(LAYERS);

		if (context.getItemInHand().is(this.asItem()) && i < 8) {
			if (context.replacingClickedOnBlock()) {
				return context.getClickedFace().getOpposite() == state.getValue(FACING);
			}

			return context.getClickedFace().getOpposite() == state.getValue(FACING);
		}

		return false;
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Direction direction = ctx.getClickedFace().getOpposite();
		BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
		FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());

		if (blockState.is(this)) {
			int i = blockState.getValue(LAYERS);
			return this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER).setValue(LAYERS, Math.min(8, i + 1)).setValue(FACING, direction);
		}

		return Objects.requireNonNull(super.getStateForPlacement(ctx)).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER).setValue(FACING, direction);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LAYERS, WATERLOGGED, FACING);
	}

	protected FluidState getFluidState(BlockState state) {
		if (state.getValue(BlockStateProperties.LAYERS) >= 8) {
			return Fluids.EMPTY.defaultFluidState();
		} else {
			return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
		}
	}

	static {
		FLOOR_LAYERS_TO_SHAPE = new VoxelShape[]{Shapes.empty(),
				Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)};
		NORTH_LAYERS_TO_SHAPE = new VoxelShape[]{Shapes.empty(),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 2.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 4.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 6.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 8.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 10.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 12.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 14.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)};
		SOUTH_LAYERS_TO_SHAPE = new VoxelShape[]{Shapes.empty(),
				Block.box(0.0, 0.0, 14.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 12.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 10.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 8.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 4.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 2.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)};
		WEST_LAYERS_TO_SHAPE = new VoxelShape[]{Shapes.empty(),
				Block.box(0.0, 0.0, 0.0, 2.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 4.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 6.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 8.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 10.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 12.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 14.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)};
		EAST_LAYERS_TO_SHAPE = new VoxelShape[]{Shapes.empty(),
				Block.box(14.0, 0.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(12.0, 0.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(10.0, 0.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(8.0, 0.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(6.0, 0.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(4.0, 0.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(2.0, 0.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)};
		CEILING_LAYERS_TO_SHAPE = new VoxelShape[]{Shapes.empty(),
				Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 12.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 10.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 8.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 6.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 4.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 2.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)};
	}
}
