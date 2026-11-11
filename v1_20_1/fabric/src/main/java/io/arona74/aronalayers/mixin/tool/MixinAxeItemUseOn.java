package io.arona74.aronalayers.mixin.tool;

import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
/**
 * 1.20.1 version of the shared MixinAxeItem: AxeItem has no evaluateNewBlockState yet, so the
 * stripping is done here, at the start of useOn, the way vanilla does it.
 */
public class MixinAxeItemUseOn {
	@Shadow
	@Final
	protected static Map<Block, Block> STRIPPABLES;

	@Inject(method = "useOn", at = @At(value = "HEAD"), cancellable = true)
	private void aronalayers$useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		BlockState state = world.getBlockState(pos);

		Optional<BlockState> optional = this.aronalayers$getStrippedState(state);

		if (optional.isPresent()) {
			world.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0f, 1.0f);
			if (!world.isClientSide()) {
				world.setBlock(pos, optional.get(), Block.UPDATE_ALL | Block.UPDATE_IMMEDIATE);
				if (player != null) {
					context.getItemInHand().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(context.getHand()));
				}
			}
			cir.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide()));
		}
	}

	@Unique
	private Optional<BlockState> aronalayers$getStrippedState(BlockState blockState) {
		return Optional.ofNullable(STRIPPABLES.get(blockState.getBlock())).map(block ->
				block.withPropertiesOf(blockState));
	}
}
