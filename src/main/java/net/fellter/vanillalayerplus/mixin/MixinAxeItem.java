package net.fellter.vanillalayerplus.mixin;

import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
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
public class MixinAxeItem {
	@Shadow
	@Final
	protected static Map<Block, Block> STRIPPABLES;

	@Inject(method = "evaluateNewBlockState", at = @At(value = "HEAD"), cancellable = true)
	private void fellter$tryStrip(Level world, BlockPos pos, Player player, BlockState state, CallbackInfoReturnable<Optional<BlockState>> cir) {
		Optional<BlockState> optional = this.fellter$getStrippedState(state);

		if (optional.isPresent()) {
			world.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0f, 1.0f);
			cir.setReturnValue(optional);
		}
	}

	@Unique
	private Optional<BlockState> fellter$getStrippedState(BlockState blockState) {
		return Optional.ofNullable(STRIPPABLES.get(blockState.getBlock())).map(block ->
				block.withPropertiesOf(blockState));
	}
}
