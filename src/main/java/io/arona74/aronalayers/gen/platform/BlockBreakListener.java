package io.arona74.aronalayers.gen.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

/** What Platform.onPlayerBlockBreak calls: a player broke a block, and it is gone from the level. */
@FunctionalInterface
public interface BlockBreakListener {
    /** @param state the block that was at {@code pos} */
    void afterBreak(ServerLevel level, Player player, BlockPos pos, BlockState state);
}
