package io.arona74.aronalayers.gameplay.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.arona74.aronalayers.gameplay.Compat;
import io.arona74.aronalayers.gameplay.LayerSpreadHandler;
import io.arona74.aronalayers.gen.platform.Platform;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Test commands for the gameplay features, for operators. They make a dedicated server with
 * nobody on it do what otherwise takes a player, which is how the features are tested from a
 * script (docs/MERGE_PLAN.md, phase 3):
 *
 * <ul>
 *   <li>{@code /algdebug gameplay mine <pos> [silk_touch|fortune|sneak|sneak_silk_touch]}: the
 *       loader's fake player breaks the block with a netherite pickaxe, through the same code as
 *       a player's break (block break events, tool, drops).</li>
 *   <li>{@code /algdebug gameplay anchor <pos>}: grass and mycelium spread around this position
 *       as around a player. {@code anchor clear} removes every anchor. Anchors are not saved.</li>
 * </ul>
 */
public final class GameplayDebugCommand {
    private GameplayDebugCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("algdebug")
            .requires(source -> io.arona74.aronalayers.gen.Compat.hasPermission(source, 2))
            .then(Commands.literal("gameplay")
                .then(Commands.literal("mine")
                    .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(ctx -> mine(ctx, false, 0, false))
                        .then(Commands.literal("silk_touch").executes(ctx -> mine(ctx, true, 0, false)))
                        .then(Commands.literal("fortune").executes(ctx -> mine(ctx, false, 3, false)))
                        .then(Commands.literal("sneak").executes(ctx -> mine(ctx, false, 0, true)))
                        .then(Commands.literal("sneak_silk_touch").executes(ctx -> mine(ctx, true, 0, true)))))
                .then(Commands.literal("anchor")
                    .then(Commands.literal("clear").executes(ctx -> {
                        LayerSpreadHandler.clearAnchors();
                        send(ctx.getSource(), "[ALG gameplay] spread anchors cleared");
                        return 1;
                    }))
                    .then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(ctx -> {
                        BlockPos pos = BlockPosArgument.getLoadedBlockPos(ctx, "pos");
                        LayerSpreadHandler.addAnchor(ctx.getSource().getLevel(), pos);
                        send(ctx.getSource(), "[ALG gameplay] grass and mycelium now spread around chunk "
                            + (pos.getX() >> 4) + "," + (pos.getZ() >> 4) + " as around a player");
                        return 1;
                    })))));
    }

    private static int mine(CommandContext<CommandSourceStack> ctx, boolean silkTouch, int fortune, boolean sneak) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerLevel level = source.getLevel();
        BlockPos pos = BlockPosArgument.getLoadedBlockPos(ctx, "pos");
        BlockState state = level.getBlockState(pos);

        ServerPlayer player = Platform.fakePlayer(level);
        ItemStack tool = new ItemStack(Items.NETHERITE_PICKAXE);
        Compat.enchant(level, tool, silkTouch, fortune);
        player.setItemInHand(InteractionHand.MAIN_HAND, tool);
        player.setShiftKeyDown(sneak);
        boolean broken;
        try {
            broken = player.gameMode.destroyBlock(pos);
        } finally {
            player.setShiftKeyDown(false);
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }

        send(source, "[ALG gameplay] " + (broken ? "mined " : "could not mine ") + state
            + (silkTouch ? " with Silk Touch" : fortune > 0 ? " with Fortune " + fortune : "") + (sneak ? ", sneaking" : ""));
        return broken ? 1 : 0;
    }

    private static void send(CommandSourceStack source, String msg) {
        source.sendSuccess(() -> Component.literal(msg), false);
    }
}
