package io.arona74.aronalayers.gameplay;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;

/**
 * Minecraft 1.20.1: the vanilla calls of the gameplay package that differ between Minecraft
 * versions (game rules, enchantment levels, build height). Every version has a class with this
 * name and these methods.
 */
public final class Compat {
    private Compat() {}

    /** Lowest buildable Y. */
    public static int bottomY(LevelHeightAccessor level) {
        return level.getMinBuildHeight();
    }

    public static int randomTickSpeed(ServerLevel level) {
        return level.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
    }

    public static boolean doMobGriefing(Level level) {
        return level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
    }

    /** The block drops game rule (doTileDrops). */
    public static boolean doBlockDrops(ServerLevel level) {
        return level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS);
    }

    /** Fortune level of a tool. Enchantments are still plain objects on 1.20.1. */
    public static int fortuneLevel(ServerLevel level, ItemStack tool) {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
    }

    public static int silkTouchLevel(ServerLevel level, ItemStack tool) {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool);
    }

    /** For the test command: puts Silk Touch, or Fortune at {@code fortune} if above 0, on a tool. */
    public static void enchant(ServerLevel level, ItemStack tool, boolean silkTouch, int fortune) {
        if (silkTouch) tool.enchant(Enchantments.SILK_TOUCH, 1);
        if (fortune > 0) tool.enchant(Enchantments.BLOCK_FORTUNE, fortune);
    }
}
