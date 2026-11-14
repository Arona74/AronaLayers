package io.arona74.aronalayers.gameplay.mixin;

import io.arona74.aronalayers.gameplay.ore.OreLayerDrops;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Replaces the drops of ore layers broken by a player (see OreLayerDrops).
 *
 * <p>This overload is the one Block.playerDestroy calls, after the game has already checked the
 * tool may harvest the block, and it is the only one that knows the tool, which Fortune and Silk
 * Touch need. Explosions and other tool-less breaks use the other overloads and keep the loot
 * table's drop. Done in code rather than in the loot tables so that the option can switch it off
 * and the drop can depend on the layer count without 8 entries per ore.
 */
@Mixin(Block.class)
public abstract class OreLayerDropMixin {

    @Inject(
        method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void aronalayers$smallOreDrops(BlockState state, Level level, BlockPos pos, BlockEntity blockEntity,
                                                  Entity entity, ItemStack tool, CallbackInfo ci) {
        if (level instanceof ServerLevel server && OreLayerDrops.drop(state, server, pos, tool)) {
            ci.cancel();
        }
    }
}
