package io.arona74.aronalayers.gameplay.ore;

import io.arona74.aronalayers.block.ModBlocks;
import io.arona74.aronalayers.gameplay.Compat;
import io.arona74.aronalayers.gen.LayerConfig;
import io.arona74.aronalayers.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

/**
 * Drops of the ore layers ({@code aronalayers:*_ore_layer}) mined with the right tool and without
 * Silk Touch (option small_ore_drops): a share of the ore's normal drop and experience,
 * proportional to the layer count, so that eight layers are worth one ore block.
 *
 * <ul>
 *   <li>Single-item ores (coal, iron, copper, gold, diamond, emerald) drop their small ore item
 *       (ModItems.SMALL_*): per layer, as many as the ore drops items. Eight small ores craft
 *       into one item, so copper's 2-5 raw copper per ore becomes 2-5 small ores per layer.</li>
 *   <li>Ores that already drop several small items (redstone, lapis, nether gold, nether quartz)
 *       drop those directly: one eighth of the ore's roll per layer, randomly rounded.</li>
 *   <li>Fortune applies as on the ore itself: the ore-drops multiplier for most, redstone's
 *       uniform bonus for redstone.</li>
 *   <li>Experience is one eighth of the ore's roll per layer, randomly rounded.</li>
 * </ul>
 *
 * Silk Touch keeps the loot table's drop (the layers themselves) so they can still be collected
 * for building. Ancient debris, the 3D ore layers and the zinc ore layers are not covered: they
 * keep their loot table too.
 */
public final class OreLayerDrops {
    private OreLayerDrops() {}

    private enum Bonus { ORE_DROPS, UNIFORM }

    /**
     * @param perLayer true: {@code min..max} items per layer (small ores). false: the ore's
     *                 {@code min..max} per block, shared out over its eight layers.
     */
    private record Ore(Item item, int min, int max, boolean perLayer, Bonus bonus, int xpMin, int xpMax) {}

    private static final int LAYERS_PER_BLOCK = 8;

    /** Built at the first drop: by then the blocks and items are registered on every loader. */
    private static final class Ores {
        static final Map<Block, Ore> BY_BLOCK = new HashMap<>();

        // Vanilla 1.20-26.x ore drop and experience ranges.
        static {
            put(new Ore(ModItems.SMALL_COAL, 1, 1, true, Bonus.ORE_DROPS, 0, 2), ModBlocks.COAL_ORE_LAYER, ModBlocks.DEEPSLATE_COAL_ORE_LAYER);
            put(new Ore(ModItems.SMALL_RAW_IRON, 1, 1, true, Bonus.ORE_DROPS, 0, 0), ModBlocks.IRON_ORE_LAYER, ModBlocks.DEEPSLATE_IRON_ORE_LAYER);
            put(new Ore(ModItems.SMALL_RAW_COPPER, 2, 5, true, Bonus.ORE_DROPS, 0, 0), ModBlocks.COPPER_ORE_LAYER, ModBlocks.DEEPSLATE_COPPER_ORE_LAYER);
            put(new Ore(ModItems.SMALL_RAW_GOLD, 1, 1, true, Bonus.ORE_DROPS, 0, 0), ModBlocks.GOLD_ORE_LAYER, ModBlocks.DEEPSLATE_GOLD_ORE_LAYER);
            put(new Ore(ModItems.SMALL_DIAMOND, 1, 1, true, Bonus.ORE_DROPS, 3, 7), ModBlocks.DIAMOND_ORE_LAYER, ModBlocks.DEEPSLATE_DIAMOND_ORE_LAYER);
            put(new Ore(ModItems.SMALL_EMERALD, 1, 1, true, Bonus.ORE_DROPS, 3, 7), ModBlocks.EMERALD_ORE_LAYER, ModBlocks.DEEPSLATE_EMERALD_ORE_LAYER);
            put(new Ore(Items.REDSTONE, 4, 5, false, Bonus.UNIFORM, 1, 5), ModBlocks.REDSTONE_ORE_LAYER, ModBlocks.DEEPSLATE_REDSTONE_ORE_LAYER);
            put(new Ore(Items.LAPIS_LAZULI, 4, 9, false, Bonus.ORE_DROPS, 2, 5), ModBlocks.LAPIS_ORE_LAYER, ModBlocks.DEEPSLATE_LAPIS_ORE_LAYER);
            put(new Ore(Items.GOLD_NUGGET, 2, 6, false, Bonus.ORE_DROPS, 0, 1), ModBlocks.NETHER_GOLD_ORE_LAYER);
            put(new Ore(Items.QUARTZ, 1, 1, false, Bonus.ORE_DROPS, 2, 5), ModBlocks.NETHER_QUARTZ_ORE_LAYER);
        }

        private static void put(Ore ore, Block... layers) {
            for (Block layer : layers) BY_BLOCK.put(layer, ore);
        }
    }

    /**
     * Drops this ore layer's small ores and experience in place of its normal drop.
     *
     * @return true if handled (the caller must skip the normal drop), false to leave it as it is
     */
    public static boolean drop(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool) {
        if (!LayerConfig.SMALL_ORE_DROPS) return false;
        Ore ore = Ores.BY_BLOCK.get(state.getBlock());
        if (ore == null || !state.hasProperty(BlockStateProperties.LAYERS)) return false;
        if (Compat.silkTouchLevel(level, tool) > 0) return false;

        int layers = state.getValue(BlockStateProperties.LAYERS);
        RandomSource random = level.getRandom();

        int rolled = 0;
        for (int i = 0; i < layers; i++) rolled += roll(random, ore.min(), ore.max());
        int count = ore.perLayer() ? rolled : roundRandomly((double) rolled / LAYERS_PER_BLOCK, random);

        int fortune = Compat.fortuneLevel(level, tool);
        if (fortune > 0) {
            if (ore.bonus() == Bonus.ORE_DROPS) {
                count *= Math.max(0, random.nextInt(fortune + 2) - 1) + 1;
            } else {
                int bonus = 0;
                for (int i = 0; i < layers; i++) bonus += random.nextInt(fortune + 1);
                count += roundRandomly((double) bonus / LAYERS_PER_BLOCK, random);
            }
        }

        Item item = ore.item();
        int stackSize = new ItemStack(item).getMaxStackSize();
        while (count > 0) {
            int n = Math.min(count, stackSize);
            Block.popResource(level, pos, new ItemStack(item, n));
            count -= n;
        }

        int xpRolled = 0;
        for (int i = 0; i < layers; i++) xpRolled += roll(random, ore.xpMin(), ore.xpMax());
        int xp = roundRandomly((double) xpRolled / LAYERS_PER_BLOCK, random);
        if (xp > 0 && Compat.doBlockDrops(level)) {
            ExperienceOrb.award(level, Vec3.atCenterOf(pos), xp);
        }
        return true;
    }

    /**
     * The drop of one layer mined off a taller ore layer while sneaking (LayerBlock.oneLayerDrop):
     * without this, that way of mining would hand out the layer item whatever the tool.
     */
    public static boolean dropOneLayer(BlockState oneLayer, Level level, BlockPos pos, ItemStack tool) {
        return level instanceof ServerLevel server && drop(oneLayer, server, pos, tool);
    }

    private static int roll(RandomSource random, int min, int max) {
        return max <= min ? min : min + random.nextInt(max - min + 1);
    }

    /** Integer part, plus one with probability equal to the fraction: right on average. */
    private static int roundRandomly(double value, RandomSource random) {
        int whole = (int) Math.floor(value);
        return whole + (random.nextDouble() < value - whole ? 1 : 0);
    }
}
