package io.arona74.aronalayers.gameplay;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.gamerules.GameRules;

/**
 * Minecraft 26.3: the vanilla calls of the gameplay package that differ between Minecraft
 * versions (game rules, enchantment levels, build height). Every version has a class with this
 * name and these methods.
 */
public final class Compat {
    private Compat() {}

    /** Lowest buildable Y. */
    public static int bottomY(LevelHeightAccessor level) {
        return level.getMinY();
    }

    // Game rules live in net.minecraft.world.level.gamerules since 1.21.11, read with one
    // generic get (RANDOM_TICK_SPEED, MOB_GRIEFING, BLOCK_DROPS).
    public static int randomTickSpeed(ServerLevel level) {
        return level.getGameRules().get(GameRules.RANDOM_TICK_SPEED);
    }

    /**
     * Level has no getGameRules, only ServerLevel. The only caller is the sheep eating goal,
     * which runs on the server.
     */
    public static boolean doMobGriefing(Level level) {
        return level instanceof ServerLevel server && server.getGameRules().get(GameRules.MOB_GRIEFING);
    }

    /** The block drops game rule (doTileDrops before 1.21.11). */
    public static boolean doBlockDrops(ServerLevel level) {
        return level.getGameRules().get(GameRules.BLOCK_DROPS);
    }

    /** Fortune level of a tool. Enchantments are data-driven from 1.21: looked up through the registry. */
    public static int fortuneLevel(ServerLevel level, ItemStack tool) {
        return enchantmentLevel(level, Enchantments.FORTUNE, tool);
    }

    public static int silkTouchLevel(ServerLevel level, ItemStack tool) {
        return enchantmentLevel(level, Enchantments.SILK_TOUCH, tool);
    }

    /** For the test command: puts Silk Touch, or Fortune at {@code fortune} if above 0, on a tool. */
    public static void enchant(ServerLevel level, ItemStack tool, boolean silkTouch, int fortune) {
        var enchantments = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        if (silkTouch) tool.enchant(enchantments.getOrThrow(Enchantments.SILK_TOUCH), 1);
        if (fortune > 0) tool.enchant(enchantments.getOrThrow(Enchantments.FORTUNE), fortune);
    }

    private static int enchantmentLevel(ServerLevel level, ResourceKey<Enchantment> key, ItemStack tool) {
        return EnchantmentHelper.getItemEnchantmentLevel(
                level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key), tool);
    }
}
