package io.arona74.aronalayers.gameplay.mixin;

import io.arona74.aronalayers.block.ModBlocks;
import io.arona74.aronalayers.gameplay.Compat;
import io.arona74.aronalayers.gen.LayerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Sheep eat grass layers (option sheep_eat_grass_layers): the layer becomes a dirt layer of the
 * same height, as a grass block becomes dirt.
 *
 * <p>Where a grass layer is to be eaten, both methods answer in vanilla's place and cancel it.
 * Vanilla would otherwise go on to the block under the sheep's feet, and a grass layer lying on
 * a grass block would cost both, and feed the sheep twice.
 */
@Mixin(EatBlockGoal.class)
public abstract class EatGrassGoalMixin extends Goal {

    @Shadow
    @Final
    private Mob mob;

    @Shadow
    @Final
    private Level level;

    @Shadow
    private int eatAnimationTick;

    /**
     * The grass layer the sheep would eat, or null. A sheep stands inside the block space of a
     * layer up to 7 high; on a layer 8 high it stands in the space above, as on a full block,
     * and that layer is only eaten when nothing grows there (vanilla eats the short grass first).
     */
    @Unique
    private BlockPos aronalayers$grassLayerPos() {
        BlockPos pos = this.mob.blockPosition();
        BlockState state = this.level.getBlockState(pos);
        if (state.is(ModBlocks.GRASS_LAYER)) return pos;
        if (state.isAir() && this.level.getBlockState(pos.below()).is(ModBlocks.GRASS_LAYER)) return pos.below();
        return null;
    }

    /** Same odds as vanilla, rolled here so they are not rolled twice. */
    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void aronalayers$canEatGrassLayer(CallbackInfoReturnable<Boolean> cir) {
        if (!LayerConfig.SHEEP_EAT_GRASS_LAYERS || aronalayers$grassLayerPos() == null) return;

        cir.setReturnValue(this.mob.getRandom().nextInt(this.mob.isBaby() ? 50 : 1000) == 0);
    }

    /** The tick on which vanilla eats: the animation counter, once decreased, is at 4 (in goal ticks). */
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void aronalayers$eatGrassLayer(CallbackInfo ci) {
        if (!LayerConfig.SHEEP_EAT_GRASS_LAYERS) return;
        int next = Math.max(0, this.eatAnimationTick - 1);
        if (next != this.adjustedTickDelay(4)) return;
        BlockPos pos = aronalayers$grassLayerPos();
        if (pos == null) return;

        this.eatAnimationTick = next;
        if (Compat.doMobGriefing(this.level)) {
            BlockState state = this.level.getBlockState(pos);
            // The break particles and sound, as when a grass block is eaten.
            this.level.levelEvent(2001, pos, Block.getId(state));
            this.level.setBlock(pos, ModBlocks.DIRT_LAYER.withPropertiesOf(state), Block.UPDATE_CLIENTS);
        }
        this.mob.ate();
        ci.cancel();
    }
}
