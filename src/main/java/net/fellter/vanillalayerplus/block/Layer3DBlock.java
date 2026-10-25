package net.fellter.vanillalayerplus.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

/**
 * Two full layers merged together: a floor layer {@code vertical_layers} high, and a wall
 * layer {@code horizontal_layers} thick against the {@code horizontal_facing} side. At 4 and 4
 * it is shaped like a stair. At {@code vertical_layers=0} it is a pure wall layer, which unlike
 * a regular layer on a wall keeps its textures upright. If either value reaches 8 the shape is
 * a full cube, so the block turns into its base layer at 8 layers.
 */
public class Layer3DBlock extends Block implements Waterloggable {
	public static final EnumProperty<Direction> HORIZONTAL_FACING = EnumProperty.of("horizontal_facing", Direction.class, Direction.Type.HORIZONTAL);
	public static final IntProperty VERTICAL_LAYERS = IntProperty.of("vertical_layers", 0, 8);
	public static final IntProperty HORIZONTAL_LAYERS = IntProperty.of("horizontal_layers", 1, 8);
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	// [horizontal id][vertical layers][horizontal layers]
	private static final VoxelShape[][][] SHAPES = new VoxelShape[4][9][9];
	// Clicks this close to the block's edge hit an outer face, not the inner step.
	private static final double EDGE = 1.0E-5;

	private final Block baseLayer;

	public Layer3DBlock(Block baseLayer, Settings settings) {
		super(settings);
		this.baseLayer = baseLayer;
		this.setDefaultState(this.getStateManager().getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH).with(VERTICAL_LAYERS, 1).with(HORIZONTAL_LAYERS, 1).with(WATERLOGGED, false));
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
				return Blocks.AIR.getDefaultState();
			}

			return baseLayer.getDefaultState().with(LayerBlock.FACING, Direction.DOWN).with(LayerBlock.LAYERS, vertical);
		}

		return toBaseLayerIfFull(this.getDefaultState()
				.with(VERTICAL_LAYERS, vertical)
				.with(HORIZONTAL_LAYERS, horizontal)
				.with(HORIZONTAL_FACING, facing.getAxis().isHorizontal() ? facing : Direction.NORTH));
	}

	public static boolean isFull(BlockState state) {
		return state.get(VERTICAL_LAYERS) == 8 || state.get(HORIZONTAL_LAYERS) == 8;
	}

	private BlockState toBaseLayerIfFull(BlockState state) {
		if (!isFull(state) || !(baseLayer instanceof LayerBlock)) {
			return state;
		}

		return baseLayer.getDefaultState().with(LayerBlock.FACING, Direction.DOWN).with(LayerBlock.LAYERS, 8);
	}

	@Override
	protected boolean canPathfindThrough(BlockState state, NavigationType type) {
		return false;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return getSidesShape(state, world, pos);
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return getSidesShape(state, world, pos);
	}

	@Override
	protected VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
		return SHAPES[state.get(HORIZONTAL_FACING).getHorizontal()][state.get(VERTICAL_LAYERS)][state.get(HORIZONTAL_LAYERS)];
	}

	@Override
	protected VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return getOutlineShape(state, world, pos, context);
	}

	@Override
	protected boolean hasSidedTransparency(BlockState state) {
		return true;
	}

	@Override
	protected float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return isFull(state) ? 0.35f : 1.0f;
	}

	// Held from below, or from the side by the wall layer.
	@Override
	protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos below = pos.down();
		if (world.getBlockState(below).isSideSolidFullSquare(world, below, Direction.UP)) return true;
		Direction f = state.get(HORIZONTAL_FACING);
		BlockPos wall = pos.offset(f);
		return world.getBlockState(wall).isSideSolidFullSquare(world, wall, f.getOpposite());
	}

	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!state.canPlaceAt(world, pos)) {
			// Schedule a tick instead of immediately breaking, so the block can fall as an entity
			world.scheduleBlockTick(pos, this, 2);
		}

		if (state.get(WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.canPlaceAt(world, pos)) {
			if (FallingBlock.canFallThrough(world.getBlockState(pos.down()))) {
				FallingBlockEntity.spawnFromBlock(world, pos, state);
			} else {
				world.breakBlock(pos, true);
			}
		}
	}

	@Override
	public boolean canFillWithFluid(@Nullable PlayerEntity player, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return !isFull(state);
	}

	@Override
	public ItemStack tryDrainFluid(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos, BlockState state) {
		if (state.get(WATERLOGGED)) {
			world.setBlockState(pos, state.with(WATERLOGGED, false), 3);
			return new ItemStack(Items.WATER_BUCKET);
		}

		return ItemStack.EMPTY;
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		if (isFull(state)) {
			return Fluids.EMPTY.getDefaultState();
		}

		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	// Using the item on the floor's top adds a vertical layer, on the wall's inner face adds a
	// horizontal layer. On a pure wall layer, using it on the ground in front of the wall adds the
	// first vertical layer. The top of the wall and the outer faces place a new block next to this one.
	@Override
	protected boolean canReplace(BlockState state, ItemPlacementContext context) {
		if (!context.getStack().isOf(this.asItem())) {
			return false;
		}

		Vec3d hit = context.getHitPos().subtract(Vec3d.of(context.getBlockPos()));
		Direction facing = state.get(HORIZONTAL_FACING);

		if (context.getSide() == Direction.UP) {
			return hit.y < 1.0 - EDGE && state.get(VERTICAL_LAYERS) < 8;
		}

		return context.getSide() == facing.getOpposite() && depthFromWall(hit, facing) < 1.0 - EDGE && state.get(HORIZONTAL_LAYERS) < 8;
	}

	// Distance of a point inside the block from the side the wall layer is against.
	private static double depthFromWall(Vec3d local, Direction facing) {
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
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState existing = ctx.getWorld().getBlockState(ctx.getBlockPos());

		if (existing.isOf(this)) {
			BlockState grown = ctx.getSide() == Direction.UP
					? existing.with(VERTICAL_LAYERS, Math.min(8, existing.get(VERTICAL_LAYERS) + 1))
					: existing.with(HORIZONTAL_LAYERS, Math.min(8, existing.get(HORIZONTAL_LAYERS) + 1));
			return toBaseLayerIfFull(grown);
		}

		boolean water = ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER;
		BlockState state = this.getDefaultState().with(WATERLOGGED, water);

		if (ctx.getSide().getAxis().isHorizontal()) {
			return state.with(HORIZONTAL_FACING, ctx.getSide().getOpposite()).with(VERTICAL_LAYERS, 0);
		}

		return state.with(HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing());
	}

	@Override
	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(HORIZONTAL_FACING, rotation.rotate(state.get(HORIZONTAL_FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(HORIZONTAL_FACING)));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, VERTICAL_LAYERS, HORIZONTAL_LAYERS, WATERLOGGED);
	}

	static {
		for (Direction facing : Direction.Type.HORIZONTAL) {
			for (int vertical = 0; vertical <= 8; vertical++) {
				for (int horizontal = 1; horizontal <= 8; horizontal++) {
					double h = vertical * 2.0;
					double w = horizontal * 2.0;
					VoxelShape floor = vertical == 0 ? VoxelShapes.empty() : Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, h, 16.0);
					VoxelShape wall = switch (facing) {
						case NORTH -> Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, w);
						case SOUTH -> Block.createCuboidShape(0.0, 0.0, 16.0 - w, 16.0, 16.0, 16.0);
						case WEST -> Block.createCuboidShape(0.0, 0.0, 0.0, w, 16.0, 16.0);
						case EAST -> Block.createCuboidShape(16.0 - w, 0.0, 0.0, 16.0, 16.0, 16.0);
						default -> throw new IllegalStateException("Unexpected facing " + facing);
					};
					SHAPES[facing.getHorizontal()][vertical][horizontal] = VoxelShapes.union(floor, wall);
				}
			}
		}
	}
}
