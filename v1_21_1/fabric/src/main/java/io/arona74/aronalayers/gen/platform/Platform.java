package io.arona74.aronalayers.gen.platform;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;

import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Fabric, Minecraft 1.21.1: loader services used by the shared code. Every loader subproject has a
 * class with this name and these methods; vanilla differences between Minecraft versions are in
 * Compat instead. Client-only services are in ClientPlatform.
 */
public final class Platform {
    private Platform() {}

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static Path configDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    /** Server commands, added to every dispatcher the server builds. */
    public static void registerCommands(Consumer<CommandDispatcher<CommandSourceStack>> registrar) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> registrar.accept(dispatcher));
    }

    public static void onChunkLoad(BiConsumer<ServerLevel, LevelChunk> handler) {
        ServerChunkEvents.CHUNK_LOAD.register((level, chunk) -> handler.accept(level, chunk));
    }

    /** The end of every tick of every server level. */
    public static void onLevelTickEnd(Consumer<ServerLevel> handler) {
        ServerTickEvents.END_WORLD_TICK.register(handler::accept);
    }

    /** The loader's stand-in for a player, for code that acts as one (the gameplay test command). */
    public static ServerPlayer fakePlayer(ServerLevel level) {
        return FakePlayer.get(level);
    }

    /** A player broke a block; called once the block is gone. */
    public static void onPlayerBlockBreak(BlockBreakListener listener) {
        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
            if (level instanceof ServerLevel serverLevel) listener.afterBreak(serverLevel, player, pos, state);
        });
    }
}
