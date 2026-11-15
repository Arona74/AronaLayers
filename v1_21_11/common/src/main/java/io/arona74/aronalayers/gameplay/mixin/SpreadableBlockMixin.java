package io.arona74.aronalayers.gameplay.mixin;

import io.arona74.aronalayers.gen.LayerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * A covered grass block stays grass (option prevent_grass_decay).
 *
 * <p>Only the random tick that would turn the block into dirt is cancelled. A grass block that
 * can stay grass ticks as usual and spreads onto dirt by itself, so LayerSpreadHandler leaves
 * dirt blocks to it and nothing spreads twice.
 */
@Mixin(SpreadingSnowyDirtBlock.class)
public class SpreadableBlockMixin {

    @Shadow
    private static boolean canBeGrass(BlockState state, LevelReader level, BlockPos pos) {
        throw new AssertionError();
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void aronalayers$preventGrassDecay(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (LayerConfig.PREVENT_GRASS_DECAY && state.is(Blocks.GRASS_BLOCK) && !canBeGrass(state, level, pos)) {
            ci.cancel();
        }
    }
}
