package io.arona74.aronalayers.gameplay.mixin;

import io.arona74.aronalayers.block.LayerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mobs spawn on top of floor layers. Ground mobs need the block under them to pass
 * {@code isValidSpawn}, whose default asks for a sturdy top face, so a layer under 8 high
 * failed it and no mob ever spawned on layered ground. A floor layer now answers as its full,
 * 8-high state does: the block's own rule still applies (none on glass layers, only polar bears
 * on ice layers, light-emitting layers excluded), just not the shape test.
 */
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class LayerSpawnSurfaceMixin {

    @Inject(method = "isValidSpawn", at = @At("HEAD"), cancellable = true)
    private void aronalayers$spawnOnLayer(BlockGetter level, BlockPos pos, EntityType<?> type,
                                          CallbackInfoReturnable<Boolean> cir) {
        if (!((Object) this instanceof BlockState state) || !(state.getBlock() instanceof LayerBlock)) return;
        if (state.getValue(LayerBlock.FACING) != Direction.DOWN || state.getValue(LayerBlock.LAYERS) == 8) return;

        cir.setReturnValue(state.setValue(LayerBlock.LAYERS, 8).isValidSpawn(level, pos, type));
    }
}
