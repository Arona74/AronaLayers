package net.fellter.vanillalayerplus.block;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import net.fellter.vanillalayerplus.VanillaLayerPlus;
import net.fellter.vanillalayerplus.custom_blocks.*;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.Shapes;

public class ModBlocks {
	private static BlockBehaviour.Properties logSettings() {
		return BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava();
	}
	private static BlockBehaviour.Properties bambooSettings() {
		return BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.BAMBOO_WOOD).ignitedByLava();
	}
	private static BlockBehaviour.Properties netherSettings() {
		return BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.STEM);
	}

	public static final Block OAK_LAYER = registerBlock("oak_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));
	public static final Block STRIPPED_OAK_LOG_LAYER = registerBlock("stripped_oak_log_layer", LayerBlock::new, logSettings());
	public static final Block STRIPPED_OAK_WOOD_LAYER = registerBlock("stripped_oak_wood_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD));
	public static final Block OAK_LOG_LAYER = registerBlock("oak_log_layer", LayerBlock::new, logSettings());
	public static final Block OAK_WOOD_LAYER = registerBlock("oak_wood_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD));

	public static final Block SPRUCE_LAYER = registerBlock("spruce_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS));
	public static final Block STRIPPED_SPRUCE_LOG_LAYER = registerBlock("stripped_spruce_log_layer", LayerBlock::new, logSettings());
	public static final Block STRIPPED_SPRUCE_WOOD_LAYER = registerBlock("stripped_spruce_wood_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_SPRUCE_WOOD));
	public static final Block SPRUCE_LOG_LAYER = registerBlock("spruce_log_layer", LayerBlock::new, logSettings());
	public static final Block SPRUCE_WOOD_LAYER = registerBlock("spruce_wood_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_WOOD));

	public static final Block BIRCH_LAYER = registerBlock("birch_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_PLANKS));
	public static final Block STRIPPED_BIRCH_LOG_LAYER = registerBlock("stripped_birch_log_layer", LayerBlock::new, logSettings());
	public static final Block STRIPPED_BIRCH_WOOD_LAYER = registerBlock("stripped_birch_wood_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_BIRCH_WOOD));
	public static final Block BIRCH_LOG_LAYER = registerBlock("birch_log_layer", LayerBlock::new, logSettings());
	public static final Block BIRCH_WOOD_LAYER = registerBlock("birch_wood_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_WOOD));

	public static final Block JUNGLE_LAYER = registerBlock("jungle_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_PLANKS));
	public static final Block STRIPPED_JUNGLE_LOG_LAYER = registerBlock("stripped_jungle_log_layer", LayerBlock::new, logSettings());
	public static final Block STRIPPED_JUNGLE_WOOD_LAYER = registerBlock("stripped_jungle_wood_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_JUNGLE_WOOD));
	public static final Block JUNGLE_LOG_LAYER = registerBlock("jungle_log_layer", LayerBlock::new, logSettings());
	public static final Block JUNGLE_WOOD_LAYER = registerBlock("jungle_wood_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_WOOD));

	public static final Block ACACIA_LAYER = registerBlock("acacia_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS));
	public static final Block STRIPPED_ACACIA_LOG_LAYER = registerBlock("stripped_acacia_log_layer", LayerBlock::new, logSettings());
	public static final Block STRIPPED_ACACIA_WOOD_LAYER = registerBlock("stripped_acacia_wood_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_ACACIA_WOOD));
	public static final Block ACACIA_LOG_LAYER = registerBlock("acacia_log_layer", LayerBlock::new, logSettings());
	public static final Block ACACIA_WOOD_LAYER = registerBlock("acacia_wood_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_WOOD));

	public static final Block DARK_OAK_LAYER = registerBlock("dark_oak_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS));
	public static final Block STRIPPED_DARK_OAK_LOG_LAYER = registerBlock("stripped_dark_oak_log_layer", LayerBlock::new, logSettings());
	public static final Block STRIPPED_DARK_OAK_WOOD_LAYER = registerBlock("stripped_dark_oak_wood_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_DARK_OAK_WOOD));
	public static final Block DARK_OAK_LOG_LAYER = registerBlock("dark_oak_log_layer", LayerBlock::new, logSettings());
	public static final Block DARK_OAK_WOOD_LAYER = registerBlock("dark_oak_wood_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_WOOD));

	public static final Block MANGROVE_LAYER = registerBlock("mangrove_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_PLANKS));
	public static final Block STRIPPED_MANGROVE_LOG_LAYER = registerBlock("stripped_mangrove_log_layer", LayerBlock::new, logSettings());
	public static final Block STRIPPED_MANGROVE_WOOD_LAYER = registerBlock("stripped_mangrove_wood_layer", LayerBlock::new, logSettings());
	public static final Block MANGROVE_LOG_LAYER = registerBlock("mangrove_log_layer", LayerBlock::new, logSettings());
	public static final Block MANGROVE_WOOD_LAYER = registerBlock("mangrove_wood_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_WOOD));

	public static final Block CHERRY_LAYER = registerBlock("cherry_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS));
	public static final Block STRIPPED_CHERRY_LOG_LAYER = registerBlock("stripped_cherry_log_layer", LayerBlock::new, logSettings());
	public static final Block STRIPPED_CHERRY_WOOD_LAYER = registerBlock("stripped_cherry_wood_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CHERRY_WOOD));
	public static final Block CHERRY_LOG_LAYER = registerBlock("cherry_log_layer", LayerBlock::new, logSettings());
	public static final Block CHERRY_WOOD_LAYER = registerBlock("cherry_wood_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_WOOD));

	public static final Block BAMBOO_LAYER = registerBlock("bamboo_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS));
	public static final Block BAMBOO_MOSAIC_LAYER = registerBlock("bamboo_mosaic_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_MOSAIC));
	public static final Block STRIPPED_BAMBOO_BLOCK_LAYER = registerBlock("stripped_bamboo_block_layer", LayerBlock::new, bambooSettings().sound(SoundType.BAMBOO_WOOD));
	public static final Block BAMBOO_BLOCK_LAYER = registerBlock("bamboo_block_layer", LayerBlock::new, bambooSettings().sound(SoundType.BAMBOO_WOOD));

	public static final Block CRIMSON_LAYER = registerBlock("crimson_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_PLANKS));
	public static final Block STRIPPED_CRIMSON_STEM_LAYER = registerBlock("stripped_crimson_stem_layer", LayerBlock::new, netherSettings().sound(SoundType.STEM));
	public static final Block STRIPPED_CRIMSON_HYPHAE_LAYER = registerBlock("stripped_crimson_hyphae_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CRIMSON_HYPHAE));
	public static final Block CRIMSON_STEM_LAYER = registerBlock("crimson_stem_layer", LayerBlock::new, netherSettings().sound(SoundType.STEM));
	public static final Block CRIMSON_HYPHAE_LAYER = registerBlock("crimson_hyphae_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_HYPHAE));

	public static final Block WARPED_LAYER = registerBlock("warped_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_PLANKS));
	public static final Block STRIPPED_WARPED_STEM_LAYER = registerBlock("stripped_warped_stem_layer", LayerBlock::new, netherSettings().sound(SoundType.STEM));
	public static final Block STRIPPED_WARPED_HYPHAE_LAYER = registerBlock("stripped_warped_hyphae_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_HYPHAE));
	public static final Block WARPED_STEM_LAYER = registerBlock("warped_stem_layer", LayerBlock::new, netherSettings().sound(SoundType.STEM));
	public static final Block WARPED_HYPHAE_LAYER = registerBlock("warped_hyphae_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_HYPHAE));

	public static final Block STONE_LAYER = registerBlock("stone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
	public static final Block COBBLESTONE_LAYER = registerBlock("cobblestone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE));
	public static final Block MOSSY_COBBLESTONE_LAYER = registerBlock("mossy_cobblestone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MOSSY_COBBLESTONE));
	public static final Block SMOOTH_STONE_LAYER = registerBlock("smooth_stone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_STONE));
	public static final Block STONE_BRICKS_LAYER = registerBlock("stone_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS));
	public static final Block CRACKED_STONE_BRICKS_LAYER = registerBlock("cracked_stone_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_STONE_BRICKS));
	public static final Block CHISELED_STONE_BRICKS_LAYER = registerBlock("chiseled_stone_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_STONE_BRICKS));
	public static final Block MOSSY_STONE_BRICKS_LAYER = registerBlock("mossy_stone_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MOSSY_STONE_BRICKS));

	public static final Block GRANITE_LAYER = registerBlock("granite_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GRANITE));
	public static final Block POLISHED_GRANITE_LAYER = registerBlock("polished_granite_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_GRANITE));
	public static final Block DIORITE_LAYER = registerBlock("diorite_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DIORITE));
	public static final Block POLISHED_DIORITE_LAYER = registerBlock("polished_diorite_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DIORITE));
	public static final Block ANDESITE_LAYER = registerBlock("andesite_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.ANDESITE));
	public static final Block POLISHED_ANDESITE_LAYER = registerBlock("polished_andesite_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_ANDESITE));

	public static final Block DEEPSLATE_LAYER = registerBlock("deepslate_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE));
	public static final Block REINFORCED_DEEPSLATE_LAYER = registerBlock("reinforced_deepslate_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.REINFORCED_DEEPSLATE));
	public static final Block COBBLED_DEEPSLATE_LAYER = registerBlock("cobbled_deepslate_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE));
	public static final Block CHISELED_DEEPSLATE_LAYER = registerBlock("chiseled_deepslate_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_DEEPSLATE));
	public static final Block POLISHED_DEEPSLATE_LAYER = registerBlock("polished_deepslate_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DEEPSLATE));
	public static final Block DEEPSLATE_BRICKS_LAYER = registerBlock("deepslate_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS));
	public static final Block CRACKED_DEEPSLATE_BRICKS_LAYER = registerBlock("cracked_deepslate_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_DEEPSLATE_BRICKS));
	public static final Block DEEPSLATE_TILES_LAYER = registerBlock("deepslate_tiles_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_TILES));
	public static final Block CRACKED_DEEPSLATE_TILES_LAYER = registerBlock("cracked_deepslate_tiles_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_DEEPSLATE_TILES));

	public static final Block TUFF_LAYER = registerBlock("tuff_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF));
	public static final Block CHISELED_TUFF_LAYER = registerBlock("chiseled_tuff_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_TUFF));
	public static final Block POLISHED_TUFF_LAYER = registerBlock("polished_tuff_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_TUFF));
	public static final Block TUFF_BRICKS_LAYER = registerBlock("tuff_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF_BRICKS));
	public static final Block CHISELED_TUFF_BRICKS_LAYER = registerBlock("chiseled_tuff_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_TUFF_BRICKS));

	public static final Block BRICKS_LAYER = registerBlock("bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS));
	public static final Block PACKED_MUD_LAYER = registerBlock("packed_mud_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PACKED_MUD));
	public static final Block MUD_BRICKS_LAYER = registerBlock("mud_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MUD_BRICKS));
	public static final Block SANDSTONE_LAYER = registerBlock("sandstone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
	public static final Block CHISELED_SANDSTONE_LAYER = registerBlock("chiseled_sandstone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_SANDSTONE));
	public static final Block SMOOTH_SANDSTONE_LAYER = registerBlock("smooth_sandstone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_SANDSTONE));
	public static final Block CUT_SANDSTONE_LAYER = registerBlock("cut_sandstone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CUT_SANDSTONE));

	public static final Block RED_SANDSTONE_LAYER = registerBlock("red_sandstone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SANDSTONE));
	public static final Block CHISELED_RED_SANDSTONE_LAYER = registerBlock("chiseled_red_sandstone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_RED_SANDSTONE));
	public static final Block SMOOTH_RED_SANDSTONE_LAYER = registerBlock("smooth_red_sandstone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_RED_SANDSTONE));
	public static final Block CUT_RED_SANDSTONE_LAYER = registerBlock("cut_red_sandstone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CUT_RED_SANDSTONE));

	public static final Block SEA_LANTERN_LAYER = registerBlock("sea_lantern_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SEA_LANTERN));
	public static final Block PRISMARINE_LAYER = registerBlock("prismarine_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE));
	public static final Block PRISMARINE_BRICKS_LAYER = registerBlock("prismarine_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE_BRICKS));
	public static final Block DARK_PRISMARINE_LAYER = registerBlock("dark_prismarine_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_PRISMARINE));

	public static final Block NETHERRACK_LAYER = registerBlock("netherrack_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERRACK));
	public static final Block NETHER_BRICKS_LAYER = registerBlock("nether_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICKS));
	public static final Block CRACKED_NETHER_BRICKS_LAYER = registerBlock("cracked_nether_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_NETHER_BRICKS));
	public static final Block CHISELED_NETHER_BRICKS_LAYER = registerBlock("chiseled_nether_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_NETHER_BRICKS));
	public static final Block RED_NETHER_BRICKS_LAYER = registerBlock("red_nether_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.RED_NETHER_BRICKS));

	public static final Block BASALT_LAYER = registerBlock("basalt_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BASALT));
	public static final Block SMOOTH_BASALT_LAYER = registerBlock("smooth_basalt_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_BASALT));
	public static final Block POLISHED_BASALT_LAYER = registerBlock("polished_basalt_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_BASALT));

	public static final Block BLACKSTONE_LAYER = registerBlock("blackstone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE));
	public static final Block GILDED_BLACKSTONE_LAYER = registerBlock("gilded_blackstone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GILDED_BLACKSTONE));
	public static final Block CHISELED_POLISHED_BLACKSTONE_LAYER = registerBlock("chiseled_polished_blackstone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_POLISHED_BLACKSTONE));
	public static final Block POLISHED_BLACKSTONE_LAYER = registerBlock("polished_blackstone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_BLACKSTONE));
	public static final Block CRACKED_POLISHED_BLACKSTONE_BRICKS_LAYER = registerBlock("cracked_polished_blackstone_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS));
	public static final Block POLISHED_BLACKSTONE_BRICKS_LAYER = registerBlock("polished_blackstone_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_BLACKSTONE_BRICKS));

	public static final Block END_STONE_LAYER = registerBlock("end_stone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE));
	public static final Block END_STONE_BRICKS_LAYER = registerBlock("end_stone_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE_BRICKS));
	public static final Block PURPUR_LAYER = registerBlock("purpur_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PURPUR_BLOCK));
	public static final Block PURPUR_PILLAR_LAYER = registerBlock("purpur_pillar_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PURPUR_PILLAR));

	public static final Block COAL_LAYER = registerBlock("coal_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_BLOCK));
	public static final Block IRON_LAYER = registerBlock("iron_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
	public static final Block GOLD_LAYER = registerBlock("gold_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK));
	public static final Block REDSTONE_LAYER = registerBlock("redstone_layer", RedstoneLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_BLOCK));
	public static final Block EMERALD_LAYER = registerBlock("emerald_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_BLOCK));
	public static final Block LAPIS_LAYER = registerBlock("lapis_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_BLOCK));
	public static final Block DIAMOND_LAYER = registerBlock("diamond_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK));
	public static final Block NETHERITE_LAYER = registerBlock("netherite_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK));

	public static final Block QUARTZ_LAYER = registerBlock("quartz_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK));
	public static final Block CHISELED_QUARTZ_LAYER = registerBlock("chiseled_quartz_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_QUARTZ_BLOCK));
	public static final Block QUARTZ_BRICKS_LAYER = registerBlock("quartz_bricks_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BRICKS));
	public static final Block QUARTZ_PILLAR_LAYER = registerBlock("quartz_pillar_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_PILLAR));
	public static final Block SMOOTH_QUARTZ_LAYER = registerBlock("smooth_quartz_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ));

	public static final Block AMETHYST_LAYER = registerBlock("amethyst_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK));

	public static final Block WAXED_COPPER_LAYER = registerBlock("waxed_copper_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_COPPER_BLOCK));
	public static final Block WAXED_CHISELED_COPPER_LAYER = registerBlock("waxed_chiseled_copper_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_CHISELED_COPPER));
	public static final Block WAXED_COPPER_GRATE_LAYER = registerBlock("waxed_copper_grate_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_COPPER_GRATE));
	public static final Block WAXED_CUT_COPPER_LAYER = registerBlock("waxed_cut_copper_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_CUT_COPPER));

	public static final Block WAXED_EXPOSED_COPPER_LAYER = registerBlock("waxed_exposed_copper_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_EXPOSED_COPPER));
	public static final Block WAXED_EXPOSED_CHISELED_COPPER_LAYER = registerBlock("waxed_exposed_chiseled_copper_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_EXPOSED_CHISELED_COPPER));
	public static final Block WAXED_EXPOSED_COPPER_GRATE_LAYER = registerBlock("waxed_exposed_copper_grate_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_EXPOSED_COPPER_GRATE));
	public static final Block WAXED_EXPOSED_CUT_COPPER_LAYER = registerBlock("waxed_exposed_cut_copper_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_EXPOSED_CUT_COPPER));

	public static final Block WAXED_WEATHERED_COPPER_LAYER = registerBlock("waxed_weathered_copper_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_WEATHERED_COPPER));
	public static final Block WAXED_WEATHERED_CHISELED_COPPER_LAYER = registerBlock("waxed_weathered_chiseled_copper_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_WEATHERED_CHISELED_COPPER));
	public static final Block WAXED_WEATHERED_COPPER_GRATE_LAYER = registerBlock("waxed_weathered_copper_grate_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_WEATHERED_COPPER_GRATE));
	public static final Block WAXED_WEATHERED_CUT_COPPER_LAYER = registerBlock("waxed_weathered_cut_copper_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_WEATHERED_CUT_COPPER));

	public static final Block WAXED_OXIDIZED_COPPER_LAYER = registerBlock("waxed_oxidized_copper_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_OXIDIZED_COPPER));
	public static final Block WAXED_OXIDIZED_CHISELED_COPPER_LAYER = registerBlock("waxed_oxidized_chiseled_copper_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_OXIDIZED_CHISELED_COPPER));
	public static final Block WAXED_OXIDIZED_COPPER_GRATE_LAYER = registerBlock("waxed_oxidized_copper_grate_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_OXIDIZED_COPPER_GRATE));
	public static final Block WAXED_OXIDIZED_CUT_COPPER_LAYER = registerBlock("waxed_oxidized_cut_copper_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_OXIDIZED_CUT_COPPER));

	public static final Block EXPOSED_COPPER_LAYER = registerBlock("exposed_copper_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.EXPOSED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.EXPOSED_COPPER));
	public static final Block EXPOSED_CHISELED_COPPER_LAYER = registerBlock("exposed_chiseled_copper_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.EXPOSED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.EXPOSED_CHISELED_COPPER));
	public static final Block EXPOSED_COPPER_GRATE_LAYER = registerBlock("exposed_copper_grate_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.EXPOSED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.EXPOSED_COPPER_GRATE));
	public static final Block EXPOSED_CUT_COPPER_LAYER = registerBlock("exposed_cut_copper_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.EXPOSED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.EXPOSED_CUT_COPPER));

	public static final Block WEATHERED_COPPER_LAYER = registerBlock("weathered_copper_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.WEATHERED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.WEATHERED_COPPER));
	public static final Block WEATHERED_CHISELED_COPPER_LAYER = registerBlock("weathered_chiseled_copper_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.WEATHERED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.WEATHERED_CHISELED_COPPER));
	public static final Block WEATHERED_COPPER_GRATE_LAYER = registerBlock("weathered_copper_grate_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.WEATHERED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.WEATHERED_COPPER_GRATE));
	public static final Block WEATHERED_CUT_COPPER_LAYER = registerBlock("weathered_cut_copper_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.WEATHERED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.WEATHERED_CUT_COPPER));

	public static final Block OXIDIZED_COPPER_LAYER = registerBlock("oxidized_copper_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.OXIDIZED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.OXIDIZED_COPPER));
	public static final Block OXIDIZED_CHISELED_COPPER_LAYER = registerBlock("oxidized_chiseled_copper_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.OXIDIZED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.OXIDIZED_CHISELED_COPPER));
	public static final Block OXIDIZED_COPPER_GRATE_LAYER = registerBlock("oxidized_copper_grate_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.OXIDIZED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.OXIDIZED_COPPER_GRATE));
	public static final Block OXIDIZED_CUT_COPPER_LAYER = registerBlock("oxidized_cut_copper_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.OXIDIZED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.OXIDIZED_CUT_COPPER));

	public static final Block COPPER_LAYER = registerBlock("copper_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.UNAFFECTED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK));
	public static final Block CHISELED_COPPER_LAYER = registerBlock("chiseled_copper_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.UNAFFECTED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_COPPER));
	public static final Block COPPER_GRATE_LAYER = registerBlock("copper_grate_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.UNAFFECTED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_GRATE));
	public static final Block CUT_COPPER_LAYER = registerBlock("cut_copper_layer", settings -> new OxidizableLayerBlock(WeatheringCopper.WeatherState.UNAFFECTED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.CUT_COPPER));

	public static final Block WHITE_WOOL_LAYER = registerBlock("white_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL));
	public static final Block LIGHT_GRAY_WOOL_LAYER = registerBlock("light_gray_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_WOOL));
	public static final Block GRAY_WOOL_LAYER = registerBlock("gray_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_WOOL));
	public static final Block BLACK_WOOL_LAYER = registerBlock("black_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_WOOL));
	public static final Block BROWN_WOOL_LAYER = registerBlock("brown_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_WOOL));
	public static final Block RED_WOOL_LAYER = registerBlock("red_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.RED_WOOL));
	public static final Block ORANGE_WOOL_LAYER = registerBlock("orange_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_WOOL));
	public static final Block YELLOW_WOOL_LAYER = registerBlock("yellow_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_WOOL));
	public static final Block LIME_WOOL_LAYER = registerBlock("lime_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LIME_WOOL));
	public static final Block GREEN_WOOL_LAYER = registerBlock("green_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_WOOL));
	public static final Block CYAN_WOOL_LAYER = registerBlock("cyan_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_WOOL));
	public static final Block LIGHT_BLUE_WOOL_LAYER = registerBlock("light_blue_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_WOOL));
	public static final Block BLUE_WOOL_LAYER = registerBlock("blue_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_WOOL));
	public static final Block PURPLE_WOOL_LAYER = registerBlock("purple_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PURPLE_WOOL));
	public static final Block MAGENTA_WOOL_LAYER = registerBlock("magenta_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_WOOL));
	public static final Block PINK_WOOL_LAYER = registerBlock("pink_wool_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_WOOL));

	public static final Block TERRACOTTA_LAYER = registerBlock("terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA));
	public static final Block WHITE_TERRACOTTA_LAYER = registerBlock("white_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA));
	public static final Block LIGHT_GRAY_TERRACOTTA_LAYER = registerBlock("light_gray_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_TERRACOTTA));
	public static final Block GRAY_TERRACOTTA_LAYER = registerBlock("gray_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_TERRACOTTA));
	public static final Block BLACK_TERRACOTTA_LAYER = registerBlock("black_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_TERRACOTTA));
	public static final Block BROWN_TERRACOTTA_LAYER = registerBlock("brown_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_TERRACOTTA));
	public static final Block RED_TERRACOTTA_LAYER = registerBlock("red_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.RED_TERRACOTTA));
	public static final Block ORANGE_TERRACOTTA_LAYER = registerBlock("orange_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_TERRACOTTA));
	public static final Block YELLOW_TERRACOTTA_LAYER = registerBlock("yellow_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_TERRACOTTA));
	public static final Block LIME_TERRACOTTA_LAYER = registerBlock("lime_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LIME_TERRACOTTA));
	public static final Block GREEN_TERRACOTTA_LAYER = registerBlock("green_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_TERRACOTTA));
	public static final Block CYAN_TERRACOTTA_LAYER = registerBlock("cyan_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_TERRACOTTA));
	public static final Block LIGHT_BLUE_TERRACOTTA_LAYER = registerBlock("light_blue_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_TERRACOTTA));
	public static final Block BLUE_TERRACOTTA_LAYER = registerBlock("blue_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_TERRACOTTA));
	public static final Block PURPLE_TERRACOTTA_LAYER = registerBlock("purple_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PURPLE_TERRACOTTA));
	public static final Block MAGENTA_TERRACOTTA_LAYER = registerBlock("magenta_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_TERRACOTTA));
	public static final Block PINK_TERRACOTTA_LAYER = registerBlock("pink_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_TERRACOTTA));

	public static final Block WHITE_CONCRETE_LAYER = registerBlock("white_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE));
	public static final Block LIGHT_GRAY_CONCRETE_LAYER = registerBlock("light_gray_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_CONCRETE));
	public static final Block GRAY_CONCRETE_LAYER = registerBlock("gray_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE));
	public static final Block BLACK_CONCRETE_LAYER = registerBlock("black_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_CONCRETE));
	public static final Block BROWN_CONCRETE_LAYER = registerBlock("brown_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_CONCRETE));
	public static final Block RED_CONCRETE_LAYER = registerBlock("red_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.RED_CONCRETE));
	public static final Block ORANGE_CONCRETE_LAYER = registerBlock("orange_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_CONCRETE));
	public static final Block YELLOW_CONCRETE_LAYER = registerBlock("yellow_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_CONCRETE));
	public static final Block LIME_CONCRETE_LAYER = registerBlock("lime_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LIME_CONCRETE));
	public static final Block GREEN_CONCRETE_LAYER = registerBlock("green_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_CONCRETE));
	public static final Block CYAN_CONCRETE_LAYER = registerBlock("cyan_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_CONCRETE));
	public static final Block LIGHT_BLUE_CONCRETE_LAYER = registerBlock("light_blue_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_CONCRETE));
	public static final Block BLUE_CONCRETE_LAYER = registerBlock("blue_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_CONCRETE));
	public static final Block PURPLE_CONCRETE_LAYER = registerBlock("purple_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PURPLE_CONCRETE));
	public static final Block MAGENTA_CONCRETE_LAYER = registerBlock("magenta_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_CONCRETE));
	public static final Block PINK_CONCRETE_LAYER = registerBlock("pink_concrete_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_CONCRETE));

	public static final Block WHITE_CONCRETE_POWDER_LAYER = registerBlock("white_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(WHITE_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE_POWDER));
	public static final Block LIGHT_GRAY_CONCRETE_POWDER_LAYER = registerBlock("light_gray_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(LIGHT_GRAY_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_CONCRETE_POWDER));
	public static final Block GRAY_CONCRETE_POWDER_LAYER = registerBlock("gray_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(GRAY_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE_POWDER));
	public static final Block BLACK_CONCRETE_POWDER_LAYER = registerBlock("black_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(BLACK_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_CONCRETE_POWDER));
	public static final Block BROWN_CONCRETE_POWDER_LAYER = registerBlock("brown_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(BROWN_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_CONCRETE_POWDER));
	public static final Block RED_CONCRETE_POWDER_LAYER = registerBlock("red_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(RED_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.RED_CONCRETE_POWDER));
	public static final Block ORANGE_CONCRETE_POWDER_LAYER = registerBlock("orange_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(ORANGE_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_CONCRETE_POWDER));
	public static final Block YELLOW_CONCRETE_POWDER_LAYER = registerBlock("yellow_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(YELLOW_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_CONCRETE_POWDER));
	public static final Block LIME_CONCRETE_POWDER_LAYER = registerBlock("lime_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(LIME_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.LIME_CONCRETE_POWDER));
	public static final Block GREEN_CONCRETE_POWDER_LAYER = registerBlock("green_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(GREEN_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_CONCRETE_POWDER));
	public static final Block CYAN_CONCRETE_POWDER_LAYER = registerBlock("cyan_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(CYAN_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_CONCRETE_POWDER));
	public static final Block LIGHT_BLUE_CONCRETE_POWDER_LAYER = registerBlock("light_blue_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(LIGHT_BLUE_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_CONCRETE_POWDER));
	public static final Block BLUE_CONCRETE_POWDER_LAYER = registerBlock("blue_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(BLUE_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_CONCRETE_POWDER));
	public static final Block PURPLE_CONCRETE_POWDER_LAYER = registerBlock("purple_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(PURPLE_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.PURPLE_CONCRETE_POWDER));
	public static final Block MAGENTA_CONCRETE_POWDER_LAYER = registerBlock("magenta_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(MAGENTA_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_CONCRETE_POWDER));
	public static final Block PINK_CONCRETE_POWDER_LAYER = registerBlock("pink_concrete_powder_layer", settings -> new ConcretePowderLayerBlock(PINK_CONCRETE_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_CONCRETE_POWDER));

	public static final Block WHITE_GLAZED_TERRACOTTA_LAYER = registerBlock("white_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_GLAZED_TERRACOTTA));
	public static final Block LIGHT_GRAY_GLAZED_TERRACOTTA_LAYER = registerBlock("light_gray_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA));
	public static final Block GRAY_GLAZED_TERRACOTTA_LAYER = registerBlock("gray_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_GLAZED_TERRACOTTA));
	public static final Block BLACK_GLAZED_TERRACOTTA_LAYER = registerBlock("black_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_GLAZED_TERRACOTTA));
	public static final Block BROWN_GLAZED_TERRACOTTA_LAYER = registerBlock("brown_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_GLAZED_TERRACOTTA));
	public static final Block RED_GLAZED_TERRACOTTA_LAYER = registerBlock("red_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.RED_GLAZED_TERRACOTTA));
	public static final Block ORANGE_GLAZED_TERRACOTTA_LAYER = registerBlock("orange_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_GLAZED_TERRACOTTA));
	public static final Block YELLOW_GLAZED_TERRACOTTA_LAYER = registerBlock("yellow_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_GLAZED_TERRACOTTA));
	public static final Block LIME_GLAZED_TERRACOTTA_LAYER = registerBlock("lime_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LIME_GLAZED_TERRACOTTA));
	public static final Block GREEN_GLAZED_TERRACOTTA_LAYER = registerBlock("green_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_GLAZED_TERRACOTTA));
	public static final Block CYAN_GLAZED_TERRACOTTA_LAYER = registerBlock("cyan_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_GLAZED_TERRACOTTA));
	public static final Block LIGHT_BLUE_GLAZED_TERRACOTTA_LAYER = registerBlock("light_blue_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA));
	public static final Block BLUE_GLAZED_TERRACOTTA_LAYER = registerBlock("blue_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_GLAZED_TERRACOTTA));
	public static final Block PURPLE_GLAZED_TERRACOTTA_LAYER = registerBlock("purple_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PURPLE_GLAZED_TERRACOTTA));
	public static final Block MAGENTA_GLAZED_TERRACOTTA_LAYER = registerBlock("magenta_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_GLAZED_TERRACOTTA));
	public static final Block PINK_GLAZED_TERRACOTTA_LAYER = registerBlock("pink_glazed_terracotta_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_GLAZED_TERRACOTTA));

	public static final Block GLASS_LAYER = registerBlock("glass_layer", GlassLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS));
	public static final Block TINTED_GLASS_LAYER = registerBlock("tinted_glass_layer", GlassLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TINTED_GLASS));
	public static final Block WHITE_STAINED_GLASS_LAYER = registerBlock("white_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.WHITE, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_STAINED_GLASS));
	public static final Block LIGHT_GRAY_STAINED_GLASS_LAYER = registerBlock("light_gray_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.LIGHT_GRAY, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_STAINED_GLASS));
	public static final Block GRAY_STAINED_GLASS_LAYER = registerBlock("gray_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.GRAY, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_STAINED_GLASS));
	public static final Block BLACK_STAINED_GLASS_LAYER = registerBlock("black_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.BLACK, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_STAINED_GLASS));
	public static final Block BROWN_STAINED_GLASS_LAYER = registerBlock("brown_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.BROWN, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_STAINED_GLASS));
	public static final Block RED_STAINED_GLASS_LAYER = registerBlock("red_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.RED, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.RED_STAINED_GLASS));
	public static final Block ORANGE_STAINED_GLASS_LAYER = registerBlock("orange_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.ORANGE, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_STAINED_GLASS));
	public static final Block YELLOW_STAINED_GLASS_LAYER = registerBlock("yellow_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.YELLOW, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_STAINED_GLASS));
	public static final Block LIME_STAINED_GLASS_LAYER = registerBlock("lime_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.LIME, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.LIME_STAINED_GLASS));
	public static final Block GREEN_STAINED_GLASS_LAYER = registerBlock("green_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.GREEN, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_STAINED_GLASS));
	public static final Block CYAN_STAINED_GLASS_LAYER = registerBlock("cyan_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.CYAN, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_STAINED_GLASS));
	public static final Block LIGHT_BLUE_STAINED_GLASS_LAYER = registerBlock("light_blue_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.LIGHT_BLUE, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_STAINED_GLASS));
	public static final Block BLUE_STAINED_GLASS_LAYER = registerBlock("blue_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.BLUE, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_STAINED_GLASS));
	public static final Block PURPLE_STAINED_GLASS_LAYER = registerBlock("purple_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.PURPLE, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.PURPLE_STAINED_GLASS));
	public static final Block MAGENTA_STAINED_GLASS_LAYER = registerBlock("magenta_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.MAGENTA, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_STAINED_GLASS));
	public static final Block PINK_STAINED_GLASS_LAYER = registerBlock("pink_stained_glass_layer", settings -> new StainedGlassLayerBlock(DyeColor.PINK, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_STAINED_GLASS));

	public static final Block DIRT_PATH_LAYER = registerBlock("dirt_path_layer", FlattenableLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT_PATH));
	public static final Block FARMLAND_LAYER = registerBlock("farmland_layer", FlattenableLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND));
	public static final Block DIRT_LAYER = registerBlock("dirt_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT));
	public static final Block GRASS_LAYER = registerBlock("grass_layer", settings -> new LayerBlock(settings) {
		private static boolean canBeGrass(BlockState state, LevelReader world, BlockPos pos) {
			BlockPos blockPos = pos.above();
			BlockState blockState = world.getBlockState(blockPos);

			if (blockState.getFluidState().getAmount() == 8) {
				return false;
			} else if (state.getValue(LAYERS) == 8) {
				return !Shapes.blockOccudes(state.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO), blockState.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO), Direction.UP);
			}

			return true;
		}

		protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
			if (!canBeGrass(state, world, pos)) {
				world.setBlockAndUpdate(pos, ModBlocks.DIRT_LAYER.withPropertiesOf(state));
			}
		}
	}, BlockBehaviour.Properties.of().strength(0.6f).sound(SoundType.GRASS).mapColor(MapColor.GRASS).randomTicks());
	public static final Block PODZOL_LAYER = registerBlock("podzol_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PODZOL));
	public static final Block MYCELIUM_LAYER = registerBlock("mycelium_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MYCELIUM));
	public static final Block COARSE_DIRT_LAYER = registerBlock("coarse_dirt_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.COARSE_DIRT));
	public static final Block ROOTED_DIRT_LAYER = registerBlock("rooted_dirt_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.ROOTED_DIRT));

	public static final Block MUD_LAYER = registerBlock("mud_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MUD));
	public static final Block MUDDY_MANGROVE_ROOTS_LAYER = registerBlock("muddy_mangrove_roots_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MUDDY_MANGROVE_ROOTS));
	public static final Block CLAY_LAYER = registerBlock("clay_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY));
	public static final Block GRAVEL_LAYER = registerBlock("gravel_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL));
	public static final Block SAND_LAYER = registerBlock("sand_layer", FallingLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SAND));
	public static final Block RED_SAND_LAYER = registerBlock("red_sand_layer", FallingLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SAND));

	public static final Block ICE_LAYER = registerBlock("ice_layer", IceLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.ICE));
	public static final Block PACKED_ICE_LAYER = registerBlock("packed_ice_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PACKED_ICE));
	public static final Block BLUE_ICE_LAYER = registerBlock("blue_ice_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_ICE));
	public static final Block SNOW_LAYER = registerBlock("snow_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW_BLOCK));

	public static final Block MOSS_LAYER = registerBlock("moss_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_BLOCK));

	public static final Block CALCITE_LAYER = registerBlock("calcite_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE));
	public static final Block DRIPSTONE_LAYER = registerBlock("dripstone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK));
	public static final Block MAGMA_LAYER = registerBlock("magma_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MAGMA_BLOCK));
	public static final Block OBSIDIAN_LAYER = registerBlock("obsidian_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN));
	public static final Block CRYING_OBSIDIAN_LAYER = registerBlock("crying_obsidian_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CRYING_OBSIDIAN));

	public static final Block CRIMSON_NYLIUM_LAYER = registerBlock("crimson_nylium_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_NYLIUM));
	public static final Block WARPED_NYLIUM_LAYER = registerBlock("warped_nylium_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_NYLIUM));
	public static final Block SOUL_SAND_LAYER = registerBlock("soul_sand_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SAND));
	public static final Block SOUL_SOIL_LAYER = registerBlock("soul_soil_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SOIL));
	public static final Block BONE_LAYER = registerBlock("bone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BONE_BLOCK));

	public static final Block COAL_ORE_LAYER = registerBlock("coal_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_ORE));
	public static final Block DEEPSLATE_COAL_ORE_LAYER = registerBlock("deepslate_coal_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COAL_ORE));
	public static final Block IRON_ORE_LAYER = registerBlock("iron_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
	public static final Block DEEPSLATE_IRON_ORE_LAYER = registerBlock("deepslate_iron_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
	public static final Block COPPER_ORE_LAYER = registerBlock("copper_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE));
	public static final Block DEEPSLATE_COPPER_ORE_LAYER = registerBlock("deepslate_copper_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE));
	public static final Block GOLD_ORE_LAYER = registerBlock("gold_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE));
	public static final Block DEEPSLATE_GOLD_ORE_LAYER = registerBlock("deepslate_gold_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_GOLD_ORE));
	public static final Block REDSTONE_ORE_LAYER = registerBlock("redstone_ore_layer", RedstoneOreLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_ORE));
	public static final Block DEEPSLATE_REDSTONE_ORE_LAYER = registerBlock("deepslate_redstone_ore_layer", RedstoneOreLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_REDSTONE_ORE));
	public static final Block EMERALD_ORE_LAYER = registerBlock("emerald_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_ORE));
	public static final Block DEEPSLATE_EMERALD_ORE_LAYER = registerBlock("deepslate_emerald_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_EMERALD_ORE));
	public static final Block LAPIS_ORE_LAYER = registerBlock("lapis_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_ORE));
	public static final Block DEEPSLATE_LAPIS_ORE_LAYER = registerBlock("deepslate_lapis_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_LAPIS_ORE));
	public static final Block DIAMOND_ORE_LAYER = registerBlock("diamond_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_ORE));
	public static final Block DEEPSLATE_DIAMOND_ORE_LAYER = registerBlock("deepslate_diamond_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_DIAMOND_ORE));
	public static final Block NETHER_GOLD_ORE_LAYER = registerBlock("nether_gold_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_GOLD_ORE));
	public static final Block NETHER_QUARTZ_ORE_LAYER = registerBlock("nether_quartz_ore_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_QUARTZ_ORE));
	public static final Block ANCIENT_DEBRIS_LAYER = registerBlock("ancient_debris_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.ANCIENT_DEBRIS));

	public static final Block RAW_IRON_LAYER = registerBlock("raw_iron_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK));
	public static final Block RAW_COPPER_LAYER = registerBlock("raw_copper_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_COPPER_BLOCK));
	public static final Block RAW_GOLD_LAYER = registerBlock("raw_gold_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_GOLD_BLOCK));
	public static final Block GLOWSTONE_LAYER = registerBlock("glowstone_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GLOWSTONE));

	public static final Block OAK_LEAVES_LAYER = registerBlock("oak_leaves_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES));
	public static final Block SPRUCE_LEAVES_LAYER = registerBlock("spruce_leaves_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES));
	public static final Block BIRCH_LEAVES_LAYER = registerBlock("birch_leaves_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_LEAVES));
	public static final Block JUNGLE_LEAVES_LAYER = registerBlock("jungle_leaves_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_LEAVES));
	public static final Block ACACIA_LEAVES_LAYER = registerBlock("acacia_leaves_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LEAVES));
	public static final Block DARK_OAK_LEAVES_LAYER = registerBlock("dark_oak_leaves_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_LEAVES));
	public static final Block MANGROVE_LEAVES_LAYER = registerBlock("mangrove_leaves_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_LEAVES));
	public static final Block CHERRY_LEAVES_LAYER = registerBlock("cherry_leaves_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LEAVES));
	public static final Block AZALEA_LEAVES_LAYER = registerBlock("azalea_leaves_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.AZALEA_LEAVES));
	public static final Block FLOWERING_AZALEA_LEAVES_LAYER = registerBlock("flowering_azalea_leaves_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWERING_AZALEA_LEAVES));

	public static final Block BROWN_MUSHROOM_LAYER = registerBlock("brown_mushroom_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM_BLOCK));
	public static final Block RED_MUSHROOM_LAYER = registerBlock("red_mushroom_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.RED_MUSHROOM_BLOCK));
	public static final Block NETHER_WART_LAYER = registerBlock("nether_wart_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_WART_BLOCK));
	public static final Block WARPED_WART_LAYER = registerBlock("warped_wart_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_WART_BLOCK));
	public static final Block SHROOMLIGHT_LAYER = registerBlock("shroomlight_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SHROOMLIGHT));
	public static final Block DRIED_KELP_LAYER = registerBlock("dried_kelp_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DRIED_KELP_BLOCK));

	public static final Block DEAD_TUBE_CORAL_LAYER = registerBlock("dead_tube_coral_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEAD_TUBE_CORAL_BLOCK));
	public static final Block DEAD_BRAIN_CORAL_LAYER = registerBlock("dead_brain_coral_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEAD_BRAIN_CORAL_BLOCK));
	public static final Block DEAD_BUBBLE_CORAL_LAYER = registerBlock("dead_bubble_coral_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEAD_BUBBLE_CORAL_BLOCK));
	public static final Block DEAD_FIRE_CORAL_LAYER = registerBlock("dead_fire_coral_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEAD_FIRE_CORAL_BLOCK));
	public static final Block DEAD_HORN_CORAL_LAYER = registerBlock("dead_horn_coral_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEAD_HORN_CORAL_BLOCK));
	public static final Block TUBE_CORAL_LAYER = registerBlock("tube_coral_layer", settings -> new CoralLayerBlock(DEAD_TUBE_CORAL_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.TUBE_CORAL_BLOCK));
	public static final Block BRAIN_CORAL_LAYER = registerBlock("brain_coral_layer", settings -> new CoralLayerBlock(DEAD_BRAIN_CORAL_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.BRAIN_CORAL_BLOCK));
	public static final Block BUBBLE_CORAL_LAYER = registerBlock("bubble_coral_layer", settings -> new CoralLayerBlock(DEAD_BUBBLE_CORAL_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.BUBBLE_CORAL_BLOCK));
	public static final Block FIRE_CORAL_LAYER = registerBlock("fire_coral_layer", settings -> new CoralLayerBlock(DEAD_FIRE_CORAL_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.FIRE_CORAL_BLOCK));
	public static final Block HORN_CORAL_LAYER = registerBlock("horn_coral_layer", settings -> new CoralLayerBlock(DEAD_HORN_CORAL_LAYER, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.HORN_CORAL_BLOCK));

	public static final Block SPONGE_LAYER = registerBlock("sponge_layer", SpongeLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SPONGE));
	public static final Block WET_SPONGE_LAYER = registerBlock("wet_sponge_layer", WetSpongeLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WET_SPONGE));
	public static final Block MELON_LAYER = registerBlock("melon_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MELON));
	public static final Block PUMPKIN_LAYER = registerBlock("pumpkin_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PUMPKIN));
	public static final Block HAY_LAYER = registerBlock("hay_layer", settings -> new LayerBlock(settings) {
		public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
			entity.causeFallDamage(fallDistance, 0.2F, world.damageSources().fall());
		}
	}, BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK));
	public static final Block HONEYCOMB_LAYER = registerBlock("honeycomb_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.HONEYCOMB_BLOCK));
	public static final Block SLIME_LAYER = registerBlock("slime_layer", SlimeLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK));
	public static final Block HONEY_LAYER = registerBlock("honey_layer", HoneyLayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.HONEY_BLOCK));

	public static final Block OCHRE_FROGLIGHT_LAYER = registerBlock("ochre_froglight_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OCHRE_FROGLIGHT));
	public static final Block VERDANT_FROGLIGHT_LAYER = registerBlock("verdant_froglight_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.VERDANT_FROGLIGHT));
	public static final Block PEARLESCENT_FROGLIGHT_LAYER = registerBlock("pearlescent_froglight_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PEARLESCENT_FROGLIGHT));

	public static final Block SCULK_LAYER = registerBlock("sculk_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK));
	public static final Block BEDROCK_LAYER = registerBlock("bedrock_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK));
	public static final Block TARGET_LAYER = registerBlock("target_layer", LayerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TARGET));

	// 3D layers: terrain layers that are also partial horizontally (see Layer3DBlock)
	public static final Block PACKED_ICE_3D_LAYER = register3DLayer("packed_ice_3d_layer", PACKED_ICE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.PACKED_ICE));
	public static final Block STONE_3D_LAYER = register3DLayer("stone_3d_layer", STONE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
	public static final Block GRANITE_3D_LAYER = register3DLayer("granite_3d_layer", GRANITE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.GRANITE));
	public static final Block DIORITE_3D_LAYER = register3DLayer("diorite_3d_layer", DIORITE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.DIORITE));
	public static final Block ANDESITE_3D_LAYER = register3DLayer("andesite_3d_layer", ANDESITE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.ANDESITE));
	public static final Block DEEPSLATE_3D_LAYER = register3DLayer("deepslate_3d_layer", DEEPSLATE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE));
	public static final Block TUFF_3D_LAYER = register3DLayer("tuff_3d_layer", TUFF_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF));
	public static final Block CALCITE_3D_LAYER = register3DLayer("calcite_3d_layer", CALCITE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE));
	public static final Block SANDSTONE_3D_LAYER = register3DLayer("sandstone_3d_layer", SANDSTONE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
	public static final Block RED_SANDSTONE_3D_LAYER = register3DLayer("red_sandstone_3d_layer", RED_SANDSTONE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SANDSTONE));
	public static final Block TERRACOTTA_3D_LAYER = register3DLayer("terracotta_3d_layer", TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA));
	public static final Block WHITE_TERRACOTTA_3D_LAYER = register3DLayer("white_terracotta_3d_layer", WHITE_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA));
	public static final Block LIGHT_GRAY_TERRACOTTA_3D_LAYER = register3DLayer("light_gray_terracotta_3d_layer", LIGHT_GRAY_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_TERRACOTTA));
	public static final Block GRAY_TERRACOTTA_3D_LAYER = register3DLayer("gray_terracotta_3d_layer", GRAY_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_TERRACOTTA));
	public static final Block BLACK_TERRACOTTA_3D_LAYER = register3DLayer("black_terracotta_3d_layer", BLACK_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_TERRACOTTA));
	public static final Block BROWN_TERRACOTTA_3D_LAYER = register3DLayer("brown_terracotta_3d_layer", BROWN_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_TERRACOTTA));
	public static final Block RED_TERRACOTTA_3D_LAYER = register3DLayer("red_terracotta_3d_layer", RED_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.RED_TERRACOTTA));
	public static final Block ORANGE_TERRACOTTA_3D_LAYER = register3DLayer("orange_terracotta_3d_layer", ORANGE_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_TERRACOTTA));
	public static final Block YELLOW_TERRACOTTA_3D_LAYER = register3DLayer("yellow_terracotta_3d_layer", YELLOW_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_TERRACOTTA));
	public static final Block LIME_TERRACOTTA_3D_LAYER = register3DLayer("lime_terracotta_3d_layer", LIME_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.LIME_TERRACOTTA));
	public static final Block GREEN_TERRACOTTA_3D_LAYER = register3DLayer("green_terracotta_3d_layer", GREEN_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_TERRACOTTA));
	public static final Block CYAN_TERRACOTTA_3D_LAYER = register3DLayer("cyan_terracotta_3d_layer", CYAN_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_TERRACOTTA));
	public static final Block LIGHT_BLUE_TERRACOTTA_3D_LAYER = register3DLayer("light_blue_terracotta_3d_layer", LIGHT_BLUE_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_TERRACOTTA));
	public static final Block BLUE_TERRACOTTA_3D_LAYER = register3DLayer("blue_terracotta_3d_layer", BLUE_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_TERRACOTTA));
	public static final Block PURPLE_TERRACOTTA_3D_LAYER = register3DLayer("purple_terracotta_3d_layer", PURPLE_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.PURPLE_TERRACOTTA));
	public static final Block MAGENTA_TERRACOTTA_3D_LAYER = register3DLayer("magenta_terracotta_3d_layer", MAGENTA_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_TERRACOTTA));
	public static final Block PINK_TERRACOTTA_3D_LAYER = register3DLayer("pink_terracotta_3d_layer", PINK_TERRACOTTA_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_TERRACOTTA));
	public static final Block NETHERRACK_3D_LAYER = register3DLayer("netherrack_3d_layer", NETHERRACK_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERRACK));
	public static final Block BASALT_3D_LAYER = register3DLayer("basalt_3d_layer", BASALT_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.BASALT));
	public static final Block BLACKSTONE_3D_LAYER = register3DLayer("blackstone_3d_layer", BLACKSTONE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE));
	public static final Block END_STONE_3D_LAYER = register3DLayer("end_stone_3d_layer", END_STONE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE));
	public static final Block SNOW_3D_LAYER = register3DLayer("snow_3d_layer", SNOW_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW_BLOCK));
	public static final Block MOSS_3D_LAYER = register3DLayer("moss_3d_layer", MOSS_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_BLOCK));
	public static final Block COARSE_DIRT_3D_LAYER = register3DLayer("coarse_dirt_3d_layer", COARSE_DIRT_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.COARSE_DIRT));

	public static final Block COAL_ORE_3D_LAYER = register3DLayer("coal_ore_3d_layer", COAL_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_ORE));
	public static final Block DEEPSLATE_COAL_ORE_3D_LAYER = register3DLayer("deepslate_coal_ore_3d_layer", DEEPSLATE_COAL_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COAL_ORE));
	public static final Block IRON_ORE_3D_LAYER = register3DLayer("iron_ore_3d_layer", IRON_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
	public static final Block DEEPSLATE_IRON_ORE_3D_LAYER = register3DLayer("deepslate_iron_ore_3d_layer", DEEPSLATE_IRON_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
	public static final Block COPPER_ORE_3D_LAYER = register3DLayer("copper_ore_3d_layer", COPPER_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE));
	public static final Block DEEPSLATE_COPPER_ORE_3D_LAYER = register3DLayer("deepslate_copper_ore_3d_layer", DEEPSLATE_COPPER_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE));
	public static final Block GOLD_ORE_3D_LAYER = register3DLayer("gold_ore_3d_layer", GOLD_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE));
	public static final Block DEEPSLATE_GOLD_ORE_3D_LAYER = register3DLayer("deepslate_gold_ore_3d_layer", DEEPSLATE_GOLD_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_GOLD_ORE));
	// Redstone ore's light level reads its "lit" property, which 3D layers don't have.
	public static final Block REDSTONE_ORE_3D_LAYER = register3DLayer("redstone_ore_3d_layer", REDSTONE_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_ORE).lightLevel(state -> 0));
	public static final Block DEEPSLATE_REDSTONE_ORE_3D_LAYER = register3DLayer("deepslate_redstone_ore_3d_layer", DEEPSLATE_REDSTONE_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_REDSTONE_ORE).lightLevel(state -> 0));
	public static final Block EMERALD_ORE_3D_LAYER = register3DLayer("emerald_ore_3d_layer", EMERALD_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_ORE));
	public static final Block DEEPSLATE_EMERALD_ORE_3D_LAYER = register3DLayer("deepslate_emerald_ore_3d_layer", DEEPSLATE_EMERALD_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_EMERALD_ORE));
	public static final Block LAPIS_ORE_3D_LAYER = register3DLayer("lapis_ore_3d_layer", LAPIS_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_ORE));
	public static final Block DEEPSLATE_LAPIS_ORE_3D_LAYER = register3DLayer("deepslate_lapis_ore_3d_layer", DEEPSLATE_LAPIS_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_LAPIS_ORE));
	public static final Block DIAMOND_ORE_3D_LAYER = register3DLayer("diamond_ore_3d_layer", DIAMOND_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_ORE));
	public static final Block DEEPSLATE_DIAMOND_ORE_3D_LAYER = register3DLayer("deepslate_diamond_ore_3d_layer", DEEPSLATE_DIAMOND_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_DIAMOND_ORE));
	public static final Block NETHER_GOLD_ORE_3D_LAYER = register3DLayer("nether_gold_ore_3d_layer", NETHER_GOLD_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_GOLD_ORE));
	public static final Block NETHER_QUARTZ_ORE_3D_LAYER = register3DLayer("nether_quartz_ore_3d_layer", NETHER_QUARTZ_ORE_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_QUARTZ_ORE));
	public static final Block ANCIENT_DEBRIS_3D_LAYER = register3DLayer("ancient_debris_3d_layer", ANCIENT_DEBRIS_LAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.ANCIENT_DEBRIS));

	private static Block register3DLayer(String name, Block baseLayer, BlockBehaviour.@NotNull Properties settings) {
		return registerBlock(name, s -> new Layer3DBlock(baseLayer, s), settings);
	}

	private static Block registerBlock(String name, @NotNull Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.@NotNull Properties settings) {
		Block block = function.apply(settings);
		Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(VanillaLayerPlus.MOD_ID, name), new BlockItem(block, new Item.Properties()));
		return Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(VanillaLayerPlus.MOD_ID, name), block);
	}



	public static void registerModBlocks() {
	}
}
