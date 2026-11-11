package io.arona74.aronalayers.custom_blocks;

import io.arona74.aronalayers.compat.Compat;
import org.jetbrains.annotations.Nullable;

import io.arona74.aronalayers.block.LayerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class IceLayerBlock extends LayerBlock {
	public IceLayerBlock(Properties settings) {
		super(settings);
	}

	public static BlockState getMeltedState() {
		return Blocks.WATER.defaultBlockState();
	}

	public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
		if (!EnchantmentHelper.hasTag(tool, EnchantmentTags.PREVENTS_ICE_MELTING)) {
			if (Compat.waterEvaporates(world, pos)) {
				world.removeBlock(pos, false);
				return;
			}

			BlockState blockState = world.getBlockState(pos.below());

			if ((blockState.blocksMotion() && state.getValue(LAYERS) == 8) || blockState.liquid()) {
				world.setBlockAndUpdate(pos, getMeltedState());
			}
		} else {
			super.playerDestroy(world, player, pos, state, blockEntity, tool);
		}
	}

	protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (world.getBrightness(LightLayer.BLOCK, pos) > 11 - Compat.lightBlock(state, world, pos)) {
			this.melt(world, pos);
		}
	}

	protected void melt(Level world, BlockPos pos) {
		if (Compat.waterEvaporates(world, pos) || world.getBlockState(pos).getValue(LAYERS) < 8) {
			world.removeBlock(pos, false);
		} else {
			world.setBlockAndUpdate(pos, getMeltedState());
			Compat.notifyNeighborChanged(world, pos, getMeltedState().getBlock());
		}
	}
}
