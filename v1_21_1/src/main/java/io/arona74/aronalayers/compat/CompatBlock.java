package io.arona74.aronalayers.compat;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Minecraft 1.21.1 base class for the mod's blocks. Vanilla methods whose signatures change
 * between Minecraft versions are overridden here, final, and forwarded to stable hooks that the
 * shared block classes override instead. Every version subproject has a class with this name
 * and these hooks.
 */
public abstract class CompatBlock extends Block {
	protected CompatBlock(Properties properties) {
		super(properties);
	}

	/** Schedules block and fluid ticks, whatever object the Minecraft version provides for it. */
	public interface Ticks {
		void block(BlockPos pos, Block block, int delay);

		void fluid(BlockPos pos, Fluid fluid, int delay);
	}

	// Hooks. Defaults behave like vanilla.

	protected BlockState onShapeUpdate(BlockState state, Direction direction, BlockState neighborState, LevelReader level, BlockPos pos, BlockPos neighborPos, Ticks ticks, RandomSource random) {
		return state;
	}

	public void onFallenOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
		super.fallOn(level, state, pos, entity, (float) fallDistance);
	}

	protected void onEntityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		super.entityInside(state, level, pos, entity);
	}

	protected void onNeighborChanged(BlockState state, Level level, BlockPos pos, Block sourceBlock, boolean movedByPiston) {
	}

	protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		return super.useWithoutItem(state, level, pos, player, hit);
	}

	/** For waterloggable blocks: whether a fluid can be placed in this block. */
	protected boolean canHoldFluid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	/** For waterloggable blocks: takes the fluid out with a bucket. */
	protected ItemStack pickUpFluid(LevelAccessor level, BlockPos pos, BlockState state) {
		return ItemStack.EMPTY;
	}

	// Bridges from this version's vanilla signatures to the hooks.

	@Override
	protected final BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		Ticks ticks = new Ticks() {
			@Override
			public void block(BlockPos tickPos, Block block, int delay) {
				level.scheduleTick(tickPos, block, delay);
			}

			@Override
			public void fluid(BlockPos tickPos, Fluid fluid, int delay) {
				level.scheduleTick(tickPos, fluid, delay);
			}
		};
		return onShapeUpdate(state, direction, neighborState, level, pos, neighborPos, ticks, level.getRandom());
	}

	@Override
	public final void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		onFallenOn(level, state, pos, entity, fallDistance);
	}

	@Override
	protected final void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		onEntityInside(state, level, pos, entity);
	}

	@Override
	protected final void neighborChanged(BlockState state, Level level, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean movedByPiston) {
		onNeighborChanged(state, level, pos, sourceBlock, movedByPiston);
	}

	@Override
	protected final InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		return onUse(state, level, pos, player, hit);
	}

	public final boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
		return canHoldFluid(level, pos, state, fluid);
	}

	public final ItemStack pickupBlock(@Nullable Player player, LevelAccessor level, BlockPos pos, BlockState state) {
		return pickUpFluid(level, pos, state);
	}
}
