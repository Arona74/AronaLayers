package io.arona74.aronalayers.custom_blocks;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import io.arona74.aronalayers.block.LayerBlock;
import io.arona74.aronalayers.block.ModBlocks;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class SpongeLayerBlock extends LayerBlock {
	public SpongeLayerBlock(Properties settings) {
		super(settings);
	}

	protected void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!oldState.is(state.getBlock())) {
			this.update(world, pos);
		}
	}

	protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		this.update(world, pos);
		super.neighborChanged(state, world, pos, sourceBlock, sourcePos, notify);
	}

	protected void update(Level world, BlockPos pos) {
		if (this.absorbWater(world, pos)) {
			world.setBlock(pos, ModBlocks.WET_SPONGE_LAYER.withPropertiesOf(world.getBlockState(pos)).setValue(WATERLOGGED, false), 2);
			world.playSound(null, pos, SoundEvents.SPONGE_ABSORB, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}

	private boolean absorbWater(Level world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		int maxRange = (int) Math.ceil(state.getValue(LAYERS) * 0.75);

		Deque<BlockPos> posQueue = new ArrayDeque<>();
		Deque<Integer> depthQueue = new ArrayDeque<>();
		Set<Long> visited = new HashSet<>();

		posQueue.add(pos);
		depthQueue.add(0);
		visited.add(pos.asLong());

		int absorbed = 0;

		while (!posQueue.isEmpty()) {
			BlockPos currentPos = posQueue.poll();
			int depth = depthQueue.poll();

			for (Direction direction : UPDATE_SHAPE_ORDER) {
				BlockPos neighborPos = currentPos.relative(direction);
				if (!visited.add(neighborPos.asLong())) continue;

				BlockState blockState = world.getBlockState(neighborPos);
				FluidState fluidState = world.getFluidState(neighborPos);

				if (!fluidState.is(FluidTags.WATER)) continue;

				Block block = blockState.getBlock();
				boolean drained = false;

				if (block instanceof BucketPickup fluidDrainable) {
					if (!fluidDrainable.pickupBlock(null, world, neighborPos, blockState).isEmpty()) {
						drained = true;
					}
				}

				if (!drained) {
					if (blockState.getBlock() instanceof LiquidBlock) {
						world.setBlock(neighborPos, Blocks.AIR.defaultBlockState(), 3);
						drained = true;
					} else if (blockState.is(Blocks.KELP) || blockState.is(Blocks.KELP_PLANT)
							|| blockState.is(Blocks.SEAGRASS) || blockState.is(Blocks.TALL_SEAGRASS)) {
						BlockEntity blockEntity = blockState.hasBlockEntity() ? world.getBlockEntity(neighborPos) : null;
						dropResources(blockState, world, neighborPos, blockEntity);
						world.setBlock(neighborPos, Blocks.AIR.defaultBlockState(), 3);
						drained = true;
					}
				}

				if (drained) {
					absorbed++;
					if (depth < maxRange) {
						posQueue.add(neighborPos);
						depthQueue.add(depth + 1);
					}
				}
			}
		}

		return absorbed > 0;
	}
}
