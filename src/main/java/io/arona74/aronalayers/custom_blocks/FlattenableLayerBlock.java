package io.arona74.aronalayers.custom_blocks;

import org.jetbrains.annotations.Nullable;

import io.arona74.aronalayers.block.LayerBlock;
import io.arona74.aronalayers.registry.ModRegistries;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FlattenableLayerBlock extends LayerBlock {
	protected static VoxelShape[] FLOOR_LAYERS_TO_SHAPE_15;
	protected static VoxelShape[] NORTH_LAYERS_TO_SHAPE_15;
	protected static VoxelShape[] SOUTH_LAYERS_TO_SHAPE_15;
	protected static VoxelShape[] EAST_LAYERS_TO_SHAPE_15;
	protected static VoxelShape[] WEST_LAYERS_TO_SHAPE_15;
	protected static VoxelShape[] CEILING_LAYERS_TO_SHAPE_15;

	public FlattenableLayerBlock(Properties settings) {
		super(settings);
	}

	@Override
	public BlockState onShapeUpdate(BlockState state, Direction direction, BlockState neighborState, LevelReader world, BlockPos pos, BlockPos neighborPos, Ticks ticks, RandomSource random) {
		if (direction == Direction.UP) {
			ticks.block(pos, this, 1);
		}

		return super.onShapeUpdate(state, direction, neighborState, world, pos, neighborPos, ticks, random);
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		// LayerBlock schedules a tick when support is lost so the layer can fall. That
		// tick arrives here, so without this the block would be left floating instead.
		// Handle it first: setToDirt would throw on the air left behind.
		if (!state.canSurvive(world, pos)) {
			super.tick(state, world, pos, random);
			return;
		}

		setToDirt(null, state, world, pos);
	}

	public void setToDirt(@Nullable Entity entity, BlockState from, Level world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		BlockState state = ModRegistries.FLATTENED_TO_BLOCK_MAP.get(blockState.getBlock()).getBlock().withPropertiesOf(blockState);
		world.setBlockAndUpdate(pos, state);
		world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, state));
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return super.getStateForPlacement(ctx);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.getBlockState(pos.above()).isRedstoneConductor(world, pos.above()) && state.getValue(LAYERS) == 8) {
			world.scheduleTick(pos, this, 1);
		}
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
		Direction direction = state.getValue(FACING);
		VoxelShape voxel;
		switch (direction) {
			case WEST -> voxel = WEST_LAYERS_TO_SHAPE_15[state.getValue(LAYERS)];
			case EAST -> voxel = EAST_LAYERS_TO_SHAPE_15[state.getValue(LAYERS)];
			case SOUTH -> voxel = SOUTH_LAYERS_TO_SHAPE_15[state.getValue(LAYERS)];
			case NORTH -> voxel = NORTH_LAYERS_TO_SHAPE_15[state.getValue(LAYERS)];
			case UP -> voxel = CEILING_LAYERS_TO_SHAPE_15[state.getValue(LAYERS)];
			case DOWN -> voxel = FLOOR_LAYERS_TO_SHAPE_15[state.getValue(LAYERS)];
			default -> throw new IllegalStateException("unreachable");
		}

		return voxel;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return getBlockSupportShape(state, world, pos);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return getBlockSupportShape(state, world, pos);
	}

	static {
		FLOOR_LAYERS_TO_SHAPE_15 = new VoxelShape[]{Shapes.empty(),
				Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 11.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 13.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0)};
		NORTH_LAYERS_TO_SHAPE_15 = new VoxelShape[]{Shapes.empty(),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 3.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 5.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 7.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 9.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 11.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 13.0),
				Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 15.0)};
		SOUTH_LAYERS_TO_SHAPE_15 = new VoxelShape[]{Shapes.empty(),
				Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 13.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 11.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 9.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 7.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 5.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 3.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 1.0, 16.0, 16.0, 16.0)};
		WEST_LAYERS_TO_SHAPE_15 = new VoxelShape[]{Shapes.empty(),
				Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 3.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 5.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 7.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 9.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 11.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 13.0, 16.0, 16.0),
				Block.box(0.0, 0.0, 0.0, 15.0, 16.0, 16.0)};
		EAST_LAYERS_TO_SHAPE_15 = new VoxelShape[]{Shapes.empty(),
				Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(13.0, 0.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(11.0, 0.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(9.0, 0.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(7.0, 0.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(5.0, 0.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(3.0, 0.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(1.0, 0.0, 0.0, 16.0, 16.0, 16.0)};
		CEILING_LAYERS_TO_SHAPE_15 = new VoxelShape[]{Shapes.empty(),
				Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 13.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 11.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 9.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 7.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 5.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 3.0, 0.0, 16.0, 16.0, 16.0),
				Block.box(0.0, 1.0, 0.0, 16.0, 16.0, 16.0)};
	}
}
