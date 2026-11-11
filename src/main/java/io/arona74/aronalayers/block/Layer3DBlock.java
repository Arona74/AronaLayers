package io.arona74.aronalayers.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
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
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Two full layers merged together: a floor layer {@code vertical_layers} high, and a wall
 * layer {@code horizontal_layers} thick against the {@code horizontal_facing} side. At 4 and 4
 * it is shaped like a stair. At {@code vertical_layers=0} it is a pure wall layer, which unlike
 * a regular layer on a wall keeps its textures upright. If either value reaches 8 the shape is
 * a full cube, so the block turns into its base layer at 8 layers.
 */
public class Layer3DBlock extends Block implements SimpleWaterloggedBlock {
	public static final EnumProperty<Direction> HORIZONTAL_FACING = EnumProperty.create("horizontal_facing", Direction.class, Direction.Plane.HORIZONTAL);
	public static final IntegerProperty VERTICAL_LAYERS = IntegerProperty.create("vertical_layers", 0, 8);
	public static final IntegerProperty HORIZONTAL_LAYERS = IntegerProperty.create("horizontal_layers", 1, 8);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	// [horizontal id][vertical layers][horizontal layers]
	private static final VoxelShape[][][] SHAPES = new VoxelShape[4][9][9];
	// Clicks this close to the block's edge hit an outer face, not the inner step.
	private static final double EDGE = 1.0E-5;

	private final Block baseLayer;

	public Layer3DBlock(Block baseLayer, Properties settings) {
		super(settings);
		this.baseLayer = baseLayer;
		this.registerDefaultState(this.getStateDefinition().any().setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(VERTICAL_LAYERS, 1).setValue(HORIZONTAL_LAYERS, 1).setValue(WATERLOGGED, false));
	}

	/** The regular layer this block is the 3D variant of. Used for drops, tags and textures. */
	public Block getBaseLayer() {
		return baseLayer;
	}

	/**
	 * State with the given size, for world generation. Values are clamped to 0-8. Shapes this
	 * block doesn't cover give the base layer instead: a floor layer when
	 * {@code horizontalLayers} is 0, a full block when either value is 8, and air when both are 0.
	 */
	public BlockState getState(int verticalLayers, int horizontalLayers, Direction facing) {
		int vertical = Math.clamp(verticalLayers, 0, 8);
		int horizontal = Math.clamp(horizontalLayers, 0, 8);

		if (horizontal == 0) {
			if (vertical == 0) {
				return Blocks.AIR.defaultBlockState();
			}

			return baseLayer.defaultBlockState().setValue(LayerBlock.FACING, Direction.DOWN).setValue(LayerBlock.LAYERS, vertical);
		}

		return toBaseLayerIfFull(this.defaultBlockState()
				.setValue(VERTICAL_LAYERS, vertical)
				.setValue(HORIZONTAL_LAYERS, horizontal)
				.setValue(HORIZONTAL_FACING, facing.getAxis().isHorizontal() ? facing : Direction.NORTH));
	}

	public static boolean isFull(BlockState state) {
		return state.getValue(VERTICAL_LAYERS) == 8 || state.getValue(HORIZONTAL_LAYERS) == 8;
	}

	private BlockState toBaseLayerIfFull(BlockState state) {
		if (!isFull(state) || !(baseLayer instanceof LayerBlock)) {
			return state;
		}

		return baseLayer.defaultBlockState().setValue(LayerBlock.FACING, Direction.DOWN).setValue(LayerBlock.LAYERS, 8);
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
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
		return SHAPES[state.getValue(HORIZONTAL_FACING).get2DDataValue()][state.getValue(VERTICAL_LAYERS)][state.getValue(HORIZONTAL_LAYERS)];
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
		return isFull(state) ? 0.35f : 1.0f;
	}

	// Held from below, or from the side by the wall layer.
	@Override
	protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		BlockPos below = pos.below();
		if (world.getBlockState(below).isFaceSturdy(world, below, Direction.UP)) return true;
		Direction f = state.getValue(HORIZONTAL_FACING);
		BlockPos wall = pos.relative(f);
		return world.getBlockState(wall).isFaceSturdy(world, wall, f.getOpposite());
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
		return !isFull(state);
	}

	@Override
	public ItemStack pickupBlock(@Nullable Player player, LevelAccessor world, BlockPos pos, BlockState state) {
		if (state.getValue(WATERLOGGED)) {
			world.setBlock(pos, state.setValue(WATERLOGGED, false), 3);
			return new ItemStack(Items.WATER_BUCKET);
		}

		return ItemStack.EMPTY;
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		if (isFull(state)) {
			return Fluids.EMPTY.defaultFluidState();
		}

		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	// Using the item on the floor's top adds a vertical layer, on the wall's inner face adds a
	// horizontal layer. On a pure wall layer, using it on the ground in front of the wall adds the
	// first vertical layer. The top of the wall and the outer faces place a new block next to this one.
	@Override
	protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		if (!context.getItemInHand().is(this.asItem())) {
			return false;
		}

		Vec3 hit = context.getClickLocation().subtract(Vec3.atLowerCornerOf(context.getClickedPos()));
		Direction facing = state.getValue(HORIZONTAL_FACING);

		if (context.getClickedFace() == Direction.UP) {
			return hit.y < 1.0 - EDGE && state.getValue(VERTICAL_LAYERS) < 8;
		}

		return context.getClickedFace() == facing.getOpposite() && depthFromWall(hit, facing) < 1.0 - EDGE && state.getValue(HORIZONTAL_LAYERS) < 8;
	}

	// Distance of a point inside the block from the side the wall layer is against.
	private static double depthFromWall(Vec3 local, Direction facing) {
		return switch (facing) {
			case NORTH -> local.z;
			case SOUTH -> 1.0 - local.z;
			case WEST -> local.x;
			case EAST -> 1.0 - local.x;
			default -> throw new IllegalStateException("Unexpected facing " + facing);
		};
	}

	// A new block placed against a wall starts as a pure wall layer on that wall; placed on a
	// floor it starts with one layer of each, the wall on the side the player looks toward.
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState existing = ctx.getLevel().getBlockState(ctx.getClickedPos());

		if (existing.is(this)) {
			BlockState grown = ctx.getClickedFace() == Direction.UP
					? existing.setValue(VERTICAL_LAYERS, Math.min(8, existing.getValue(VERTICAL_LAYERS) + 1))
					: existing.setValue(HORIZONTAL_LAYERS, Math.min(8, existing.getValue(HORIZONTAL_LAYERS) + 1));
			return toBaseLayerIfFull(grown);
		}

		boolean water = ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER;
		BlockState state = this.defaultBlockState().setValue(WATERLOGGED, water);

		if (ctx.getClickedFace().getAxis().isHorizontal()) {
			return state.setValue(HORIZONTAL_FACING, ctx.getClickedFace().getOpposite()).setValue(VERTICAL_LAYERS, 0);
		}

		return state.setValue(HORIZONTAL_FACING, ctx.getHorizontalDirection());
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(HORIZONTAL_FACING, rotation.rotate(state.getValue(HORIZONTAL_FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(HORIZONTAL_FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, VERTICAL_LAYERS, HORIZONTAL_LAYERS, WATERLOGGED);
	}

	static {
		for (Direction facing : Direction.Plane.HORIZONTAL) {
			for (int vertical = 0; vertical <= 8; vertical++) {
				for (int horizontal = 1; horizontal <= 8; horizontal++) {
					double h = vertical * 2.0;
					double w = horizontal * 2.0;
					VoxelShape floor = vertical == 0 ? Shapes.empty() : Block.box(0.0, 0.0, 0.0, 16.0, h, 16.0);
					VoxelShape wall = switch (facing) {
						case NORTH -> Block.box(0.0, 0.0, 0.0, 16.0, 16.0, w);
						case SOUTH -> Block.box(0.0, 0.0, 16.0 - w, 16.0, 16.0, 16.0);
						case WEST -> Block.box(0.0, 0.0, 0.0, w, 16.0, 16.0);
						case EAST -> Block.box(16.0 - w, 0.0, 0.0, 16.0, 16.0, 16.0);
						default -> throw new IllegalStateException("Unexpected facing " + facing);
					};
					SHAPES[facing.get2DDataValue()][vertical][horizontal] = Shapes.or(floor, wall);
				}
			}
		}
	}
}
