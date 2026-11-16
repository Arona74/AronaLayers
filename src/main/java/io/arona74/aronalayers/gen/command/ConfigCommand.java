package io.arona74.aronalayers.gen.command;

import com.mojang.brigadier.CommandDispatcher;
import io.arona74.aronalayers.gen.Compat;
import io.arona74.aronalayers.gen.LayerConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

/**
 * {@code /aronalayers reload} (permission level 2, like vanilla's /reload): reads
 * config/aronalayers/config.json and the open world's generation settings again (its
 * generation.json over generation_defaults.json), after they were edited by hand. Mostly for
 * dedicated servers, whose settings have no screen. See {@link LayerConfig#reloadFiles()}.
 *
 * <p>Not under /algconfig, which is a client command on both loaders. The mapping files
 * (al_block_mappings.json and the others) are not reloaded.
 */
public final class ConfigCommand {
    private ConfigCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("aronalayers")
                .requires(source -> Compat.hasPermission(source, 2))
                .then(Commands.literal("reload").executes(context -> reload(context.getSource()))));
    }

    private static int reload(CommandSourceStack source) {
        LayerConfig.Reloaded result = LayerConfig.reloadFiles();
        if (!result.errors().isEmpty()) {
            source.sendFailure(Component.literal("AronaLayers settings not reloaded, nothing changed: "
                    + String.join("; ", result.errors())));
            return 0;
        }
        StringBuilder text = new StringBuilder(result.changed().isEmpty()
                ? "AronaLayers settings reloaded: nothing changed"
                : "AronaLayers settings reloaded, changed: " + String.join(", ", result.changed())
                + ". Generation settings reach new chunks only");
        if (!result.needRestart().isEmpty()) {
            text.append(". Not applied, they need a restart: ").append(String.join(", ", result.needRestart()));
        }
        String message = text.toString();
        source.sendSuccess(() -> Component.literal(message), true);
        return 1;
    }
}
