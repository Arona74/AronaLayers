package io.arona74.aronalayers.custom_blocks;

import io.arona74.aronalayers.block.LayerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class RedstoneOreLayerBlock extends LayerBlock {
	public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

	public RedstoneOreLayerBlock(Properties settings) {
		super(settings);
	}

	protected void attack(BlockState state, Level world, BlockPos pos, Player player) {
		light(state, world, pos);
		super.attack(state, world, pos, player);
	}

	public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
		if (!entity.isSteppingCarefully()) {
			light(state, world, pos);
		}

		super.stepOn(world, pos, state, entity);
	}

	protected InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (world.isClientSide()) {
			spawnParticles(world, pos);
		} else {
			light(state, world, pos);
		}

		return InteractionResult.PASS;
	}

	private static void light(BlockState state, Level world, BlockPos pos) {
		spawnParticles(world, pos);

		if (!(Boolean) state.getValue(LIT)) {
			world.setBlock(pos, state.setValue(LIT, true), 3);
		}
	}

	protected boolean isRandomlyTicking(BlockState state) {
		return state.getValue(LIT);
	}

	protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (state.getValue(LIT)) {
			world.setBlock(pos, state.setValue(LIT, false), 3);
		}
	}

	protected void spawnAfterBreak(BlockState state, ServerLevel world, BlockPos pos, ItemStack tool, boolean dropExperience) {
		super.spawnAfterBreak(state, world, pos, tool, dropExperience);

		if (dropExperience) {
			this.tryDropExperience(world, pos, tool, UniformInt.of(1, 5));
		}
	}

	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		if (state.getValue(LIT)) {
			spawnParticles(world, pos);
		}
	}

	private static void spawnParticles(Level world, BlockPos pos) {
		RandomSource random = world.random;

		for (Direction direction : Direction.values()) {
			BlockPos blockPos = pos.relative(direction);

			if (!world.getBlockState(blockPos).isSolidRender(world, blockPos)) {
				Direction.Axis axis = direction.getAxis();
				double e = axis == Direction.Axis.X ? (double) 0.5F + (double) 0.5625F * (double) direction.getStepX() : (double) random.nextFloat();
				double f = axis == Direction.Axis.Y ? (double) 0.5F + (double) 0.5625F * (double) direction.getStepY() : (double) random.nextFloat();
				double g = axis == Direction.Axis.Z ? (double) 0.5F + (double) 0.5625F * (double) direction.getStepZ() : (double) random.nextFloat();
				world.addParticle(DustParticleOptions.REDSTONE, (double) pos.getX() + e, (double) pos.getY() + f, (double) pos.getZ() + g, 0.0F, 0.0F, 0.0F);
			}
		}
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIT);
	}
}
