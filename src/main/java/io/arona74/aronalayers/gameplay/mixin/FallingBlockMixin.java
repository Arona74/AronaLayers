package io.arona74.aronalayers.gameplay.mixin;

import io.arona74.aronalayers.gameplay.LayerFallHandler;
import io.arona74.aronalayers.gen.LayerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Layers fall with the sand under them (option layers_fall_with_sand), see LayerFallHandler. */
@Mixin(FallingBlockEntity.class)
public class FallingBlockMixin {

    @Shadow private BlockState blockState;
    @Shadow public boolean dropItem;

    /** Sand or gravel starts falling: the layers on it go with it. */
    @Inject(method = "fall", at = @At("TAIL"))
    private static void aronalayers$dropLayersAbove(Level level, BlockPos pos, BlockState state, CallbackInfoReturnable<FallingBlockEntity> cir) {
        if (!LayerConfig.LAYERS_FALL_WITH_SAND) return;
        if (!LayerFallHandler.isSandOrGravel(state)) return;

        LayerFallHandler.dropStackAbove(level, pos);
    }

    /**
     * Lands a layer on top of a partial-height block instead of breaking it.
     *
     * A falling block comes to rest wherever its collision box stops, and blockPosition() floors
     * that. Landing on a full block leaves it in the empty space above, which is what vanilla
     * expects. Landing on a lower layer leaves it INSIDE that layer's own position, so vanilla's
     * replaceability test fails and the entity is discarded and popped as an item: the same
     * reason vanilla sand breaks when it lands on a slab.
     *
     * That happens to layers that fell together, the upper one landing a tick after the lower one
     * placed itself. Placing it one block higher puts it back where it was: on the lower layer.
     *
     * Vanilla sand and gravel are deliberately left alone: only the blocks that fall with sand
     * take the redirect.
     */
    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;canBeReplaced(Lnet/minecraft/world/item/context/BlockPlaceContext;)Z"
            )
    )
    private boolean aronalayers$landOnPartialBlocks(BlockState landingState, BlockPlaceContext context) {
        if (landingState.canBeReplaced(context)) return true;
        if (!LayerConfig.LAYERS_FALL_WITH_SAND) return false;
        if (this.blockState == null || !LayerFallHandler.fallsWithSand(this.blockState)) return false;

        FallingBlockEntity self = (FallingBlockEntity) (Object) this;
        Level level = self.level();
        BlockPos above = self.blockPosition().above();
        if (!level.getBlockState(above).canBeReplaced()) return false;
        if (!this.blockState.canSurvive(level, above)) return false;

        // FallingBlockEntity.fall clears WATERLOGGED on the way down, and the vanilla landing
        // path puts it back when the block settles in water. This path has to do the same, or a
        // layer that lands in a river leaves a dry pocket behind. FluidTags.WATER rather than
        // Fluids.WATER: it covers flowing water as well as source blocks.
        BlockState landed = this.blockState;
        if (landed.hasProperty(BlockStateProperties.WATERLOGGED)
                && !landed.getValue(BlockStateProperties.WATERLOGGED)
                && level.getFluidState(above).is(FluidTags.WATER)) {
            landed = landed.setValue(BlockStateProperties.WATERLOGGED, true);
        }

        if (level.setBlock(above, landed, Block.UPDATE_ALL)) {
            // The caller is about to discard the entity because we answered false. The block is
            // already placed, so it must not also drop as an item.
            this.dropItem = false;
        }
        return false;
    }
}
