package io.arona74.aronalayers.gen.platform;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * NeoForge, Minecraft 26.2: loader services used by the shared code. Every loader subproject
 * has a class with this name and these methods; vanilla differences between Minecraft versions
 * are in Compat instead. Client-only services are in ClientPlatform.
 */
public final class Platform {
    private Platform() {}

    /**
     * ModList exists once mods start loading; the mixin plugin asks earlier, while only the list
     * of mod files found is known.
     */
    public static boolean isModLoaded(String modId) {
        ModList mods = ModList.get();
        if (mods != null) return mods.isLoaded(modId);
        return LoadingModList.get().getModFileById(modId) != null;
    }

    public static Path configDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    /** Server commands, added to every dispatcher the server builds. */
    public static void registerCommands(Consumer<CommandDispatcher<CommandSourceStack>> registrar) {
        NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, event -> registrar.accept(event.getDispatcher()));
    }

    /** A chunk loaded into a server level, as Fabric's ServerChunkEvents.CHUNK_LOAD. */
    public static void onChunkLoad(BiConsumer<ServerLevel, LevelChunk> handler) {
        NeoForge.EVENT_BUS.addListener(ChunkEvent.Load.class, event -> {
            if (event.getLevel() instanceof ServerLevel level && event.getChunk() instanceof LevelChunk chunk) {
                handler.accept(level, chunk);
            }
        });
    }

    /** The end of every tick of every server level. */
    public static void onLevelTickEnd(Consumer<ServerLevel> handler) {
        NeoForge.EVENT_BUS.addListener(LevelTickEvent.Post.class, event -> {
            if (event.getLevel() instanceof ServerLevel level) handler.accept(level);
        });
    }

    /** The loader's stand-in for a player, for code that acts as one (the gameplay test command). */
    public static ServerPlayer fakePlayer(ServerLevel level) {
        return FakePlayers.get(level);
    }

    /**
     * Its own class so that loading Platform loads no Minecraft class: the mixin plugin calls
     * isModLoaded before the mixins are applied, and verifying the FakePlayer returned below as
     * a ServerPlayer would load ServerPlayer and with it Entity, which would then miss every
     * mixin (on Fabric 26.x no entity could be added to a level any more).
     */
    private static final class FakePlayers {
        static ServerPlayer get(ServerLevel level) {
            return FakePlayerFactory.getMinecraft(level);
        }
    }

    /**
     * A player broke a block; called once the block is gone. NeoForge's BreakBlockEvent fires
     * before the block is removed and can still be cancelled, so the listener runs from the
     * server's task queue, later in the same tick, and only if the block really went.
     */
    public static void onPlayerBlockBreak(BlockBreakListener listener) {
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, BreakBlockEvent.class, event -> {
            if (!(event.getLevel() instanceof ServerLevel level)) return;
            BlockPos pos = event.getPos().immutable();
            BlockState state = event.getState();
            Player player = event.getPlayer();
            MinecraftServer server = level.getServer();
            server.schedule(new TickTask(server.getTickCount(), () -> {
                if (!level.getBlockState(pos).is(state.getBlock())) listener.afterBreak(level, player, pos, state);
            }));
        });
    }
}
