package io.arona74.aronalayers.gameplay.mixin;

import io.arona74.aronalayers.block.LayerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The animals placed when a chunk generates stand on layers. Vanilla picks their spawn position
 * at the top of the column, then steps down one block if that block is pathfindable, which a
 * floor layer under 5 high is (as a snow layer is). The mob is then placed at the bottom of the
 * layer's block, inside its collision box, so the collision check rejects it. A spawn position
 * that is a layer with a collision box moves back up onto its top.
 */
@Mixin(NaturalSpawner.class)
public abstract class LayerSpawnHeightMixin {

    @Inject(method = "getTopNonCollidingPos", at = @At("RETURN"), cancellable = true)
    private static void aronalayers$spawnAboveLayer(LevelReader level, EntityType<?> type, int x, int z,
                                                    CallbackInfoReturnable<BlockPos> cir) {
        BlockPos pos = cir.getReturnValue();
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof LayerBlock && !state.getCollisionShape(level, pos).isEmpty()) {
            cir.setReturnValue(pos.above());
        }
    }
}
