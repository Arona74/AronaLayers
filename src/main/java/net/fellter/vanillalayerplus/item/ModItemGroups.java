package net.fellter.vanillalayerplus.item;

import net.fellter.vanillalayerplus.VanillaLayerPlus;
import net.fellter.vanillalayerplus.block.Layer3DBlock;
import net.fellter.vanillalayerplus.block.ModBlocks;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

public class ModItemGroups {
	public static final ItemGroup VLP_GROUP = Registry.register(Registries.ITEM_GROUP,
			Identifier.of(VanillaLayerPlus.MOD_ID, "vlp"),
			FabricItemGroup.builder().displayName(Text.translatable("itemgroup.vanillalayerplus"))
					.icon(() -> new ItemStack(ModItems.VLP_TITLE)).entries((displayContext, entries) -> {
						entries.add(ModBlocks.OAK_LAYER);
						entries.add(ModBlocks.OAK_LOG_LAYER);
						entries.add(ModBlocks.OAK_WOOD_LAYER);
						entries.add(ModBlocks.STRIPPED_OAK_LOG_LAYER);
						entries.add(ModBlocks.STRIPPED_OAK_WOOD_LAYER);

						entries.add(ModBlocks.SPRUCE_LAYER);
						entries.add(ModBlocks.SPRUCE_LOG_LAYER);
						entries.add(ModBlocks.SPRUCE_WOOD_LAYER);
						entries.add(ModBlocks.STRIPPED_SPRUCE_LOG_LAYER);
						entries.add(ModBlocks.STRIPPED_SPRUCE_WOOD_LAYER);

						entries.add(ModBlocks.BIRCH_LAYER);
						entries.add(ModBlocks.BIRCH_LOG_LAYER);
						entries.add(ModBlocks.BIRCH_WOOD_LAYER);
						entries.add(ModBlocks.STRIPPED_BIRCH_LOG_LAYER);
						entries.add(ModBlocks.STRIPPED_BIRCH_WOOD_LAYER);

						entries.add(ModBlocks.JUNGLE_LAYER);
						entries.add(ModBlocks.JUNGLE_LOG_LAYER);
						entries.add(ModBlocks.JUNGLE_WOOD_LAYER);
						entries.add(ModBlocks.STRIPPED_JUNGLE_LOG_LAYER);
						entries.add(ModBlocks.STRIPPED_JUNGLE_WOOD_LAYER);

						entries.add(ModBlocks.ACACIA_LAYER);
						entries.add(ModBlocks.ACACIA_LOG_LAYER);
						entries.add(ModBlocks.ACACIA_WOOD_LAYER);
						entries.add(ModBlocks.STRIPPED_ACACIA_LOG_LAYER);
						entries.add(ModBlocks.STRIPPED_ACACIA_WOOD_LAYER);

						entries.add(ModBlocks.DARK_OAK_LAYER);
						entries.add(ModBlocks.DARK_OAK_LOG_LAYER);
						entries.add(ModBlocks.DARK_OAK_WOOD_LAYER);
						entries.add(ModBlocks.STRIPPED_DARK_OAK_LOG_LAYER);
						entries.add(ModBlocks.STRIPPED_DARK_OAK_WOOD_LAYER);

						entries.add(ModBlocks.MANGROVE_LAYER);
						entries.add(ModBlocks.MANGROVE_LOG_LAYER);
						entries.add(ModBlocks.MANGROVE_WOOD_LAYER);
						entries.add(ModBlocks.STRIPPED_MANGROVE_LOG_LAYER);
						entries.add(ModBlocks.STRIPPED_MANGROVE_WOOD_LAYER);

						entries.add(ModBlocks.CHERRY_LAYER);
						entries.add(ModBlocks.CHERRY_LOG_LAYER);
						entries.add(ModBlocks.CHERRY_WOOD_LAYER);
						entries.add(ModBlocks.STRIPPED_CHERRY_LOG_LAYER);
						entries.add(ModBlocks.STRIPPED_CHERRY_WOOD_LAYER);


						entries.add(ModBlocks.BAMBOO_LAYER);
						entries.add(ModBlocks.BAMBOO_MOSAIC_LAYER);
						entries.add(ModBlocks.BAMBOO_BLOCK_LAYER);
						entries.add(ModBlocks.STRIPPED_BAMBOO_BLOCK_LAYER);

						entries.add(ModBlocks.CRIMSON_LAYER);
						entries.add(ModBlocks.CRIMSON_STEM_LAYER);
						entries.add(ModBlocks.CRIMSON_HYPHAE_LAYER);
						entries.add(ModBlocks.STRIPPED_CRIMSON_STEM_LAYER);
						entries.add(ModBlocks.STRIPPED_CRIMSON_HYPHAE_LAYER);

						entries.add(ModBlocks.WARPED_LAYER);
						entries.add(ModBlocks.WARPED_STEM_LAYER);
						entries.add(ModBlocks.WARPED_HYPHAE_LAYER);
						entries.add(ModBlocks.STRIPPED_WARPED_STEM_LAYER);
						entries.add(ModBlocks.STRIPPED_WARPED_HYPHAE_LAYER);

						entries.add(ModBlocks.STONE_LAYER);
						entries.add(ModBlocks.COBBLESTONE_LAYER);
						entries.add(ModBlocks.MOSSY_COBBLESTONE_LAYER);
						entries.add(ModBlocks.SMOOTH_STONE_LAYER);
						entries.add(ModBlocks.STONE_BRICKS_LAYER);
						entries.add(ModBlocks.CRACKED_STONE_BRICKS_LAYER);
						entries.add(ModBlocks.CHISELED_STONE_BRICKS_LAYER);
						entries.add(ModBlocks.MOSSY_STONE_BRICKS_LAYER);

						entries.add(ModBlocks.GRANITE_LAYER);
						entries.add(ModBlocks.POLISHED_GRANITE_LAYER);
						entries.add(ModBlocks.DIORITE_LAYER);
						entries.add(ModBlocks.POLISHED_DIORITE_LAYER);
						entries.add(ModBlocks.ANDESITE_LAYER);
						entries.add(ModBlocks.POLISHED_ANDESITE_LAYER);

						entries.add(ModBlocks.DEEPSLATE_LAYER);
						entries.add(ModBlocks.REINFORCED_DEEPSLATE_LAYER);
						entries.add(ModBlocks.COBBLED_DEEPSLATE_LAYER);
						entries.add(ModBlocks.CHISELED_DEEPSLATE_LAYER);
						entries.add(ModBlocks.POLISHED_DEEPSLATE_LAYER);
						entries.add(ModBlocks.DEEPSLATE_BRICKS_LAYER);
						entries.add(ModBlocks.CRACKED_DEEPSLATE_BRICKS_LAYER);
						entries.add(ModBlocks.DEEPSLATE_TILES_LAYER);
						entries.add(ModBlocks.CRACKED_DEEPSLATE_TILES_LAYER);

						entries.add(ModBlocks.TUFF_LAYER);
						entries.add(ModBlocks.CHISELED_TUFF_LAYER);
						entries.add(ModBlocks.POLISHED_TUFF_LAYER);
						entries.add(ModBlocks.TUFF_BRICKS_LAYER);
						entries.add(ModBlocks.CHISELED_TUFF_BRICKS_LAYER);

						entries.add(ModBlocks.BRICKS_LAYER);
						entries.add(ModBlocks.PACKED_MUD_LAYER);
						entries.add(ModBlocks.MUD_BRICKS_LAYER);

						entries.add(ModBlocks.SANDSTONE_LAYER);
						entries.add(ModBlocks.CHISELED_SANDSTONE_LAYER);
						entries.add(ModBlocks.SMOOTH_SANDSTONE_LAYER);
						entries.add(ModBlocks.CUT_SANDSTONE_LAYER);

						entries.add(ModBlocks.RED_SANDSTONE_LAYER);
						entries.add(ModBlocks.CHISELED_RED_SANDSTONE_LAYER);
						entries.add(ModBlocks.SMOOTH_RED_SANDSTONE_LAYER);
						entries.add(ModBlocks.CUT_RED_SANDSTONE_LAYER);

						entries.add(ModBlocks.SEA_LANTERN_LAYER);
						entries.add(ModBlocks.PRISMARINE_LAYER);
						entries.add(ModBlocks.PRISMARINE_BRICKS_LAYER);
						entries.add(ModBlocks.DARK_PRISMARINE_LAYER);

						entries.add(ModBlocks.NETHERRACK_LAYER);
						entries.add(ModBlocks.NETHER_BRICKS_LAYER);
						entries.add(ModBlocks.CRACKED_NETHER_BRICKS_LAYER);
						entries.add(ModBlocks.CHISELED_NETHER_BRICKS_LAYER);
						entries.add(ModBlocks.RED_NETHER_BRICKS_LAYER);

						entries.add(ModBlocks.BASALT_LAYER);
						entries.add(ModBlocks.SMOOTH_BASALT_LAYER);
						entries.add(ModBlocks.POLISHED_BASALT_LAYER);

						entries.add(ModBlocks.BLACKSTONE_LAYER);
						entries.add(ModBlocks.GILDED_BLACKSTONE_LAYER);
						entries.add(ModBlocks.CHISELED_POLISHED_BLACKSTONE_LAYER);
						entries.add(ModBlocks.POLISHED_BLACKSTONE_LAYER);
						entries.add(ModBlocks.CRACKED_POLISHED_BLACKSTONE_BRICKS_LAYER);
						entries.add(ModBlocks.POLISHED_BLACKSTONE_BRICKS_LAYER);

						entries.add(ModBlocks.END_STONE_LAYER);
						entries.add(ModBlocks.END_STONE_BRICKS_LAYER);
						entries.add(ModBlocks.PURPUR_LAYER);
						entries.add(ModBlocks.PURPUR_PILLAR_LAYER);

						entries.add(ModBlocks.COAL_LAYER);
						entries.add(ModBlocks.IRON_LAYER);
						entries.add(ModBlocks.GOLD_LAYER);
						entries.add(ModBlocks.REDSTONE_LAYER);
						entries.add(ModBlocks.EMERALD_LAYER);
						entries.add(ModBlocks.LAPIS_LAYER);
						entries.add(ModBlocks.DIAMOND_LAYER);
						entries.add(ModBlocks.NETHERITE_LAYER);

						entries.add(ModBlocks.QUARTZ_LAYER);
						entries.add(ModBlocks.CHISELED_QUARTZ_LAYER);
						entries.add(ModBlocks.QUARTZ_BRICKS_LAYER);
						entries.add(ModBlocks.QUARTZ_PILLAR_LAYER);
						entries.add(ModBlocks.SMOOTH_QUARTZ_LAYER);

						entries.add(ModBlocks.AMETHYST_LAYER);

						entries.add(ModBlocks.COPPER_LAYER);
						entries.add(ModBlocks.CHISELED_COPPER_LAYER);
						entries.add(ModBlocks.COPPER_GRATE_LAYER);
						entries.add(ModBlocks.CUT_COPPER_LAYER);

						entries.add(ModBlocks.EXPOSED_COPPER_LAYER);
						entries.add(ModBlocks.EXPOSED_CHISELED_COPPER_LAYER);
						entries.add(ModBlocks.EXPOSED_COPPER_GRATE_LAYER);
						entries.add(ModBlocks.EXPOSED_CUT_COPPER_LAYER);

						entries.add(ModBlocks.WEATHERED_COPPER_LAYER);
						entries.add(ModBlocks.WEATHERED_CHISELED_COPPER_LAYER);
						entries.add(ModBlocks.WEATHERED_COPPER_GRATE_LAYER);
						entries.add(ModBlocks.WEATHERED_CUT_COPPER_LAYER);

						entries.add(ModBlocks.OXIDIZED_COPPER_LAYER);
						entries.add(ModBlocks.OXIDIZED_CHISELED_COPPER_LAYER);
						entries.add(ModBlocks.OXIDIZED_COPPER_GRATE_LAYER);
						entries.add(ModBlocks.OXIDIZED_CUT_COPPER_LAYER);

						entries.add(ModBlocks.WAXED_COPPER_LAYER);
						entries.add(ModBlocks.WAXED_CHISELED_COPPER_LAYER);
						entries.add(ModBlocks.WAXED_COPPER_GRATE_LAYER);
						entries.add(ModBlocks.WAXED_CUT_COPPER_LAYER);

						entries.add(ModBlocks.WAXED_EXPOSED_COPPER_LAYER);
						entries.add(ModBlocks.WAXED_EXPOSED_CHISELED_COPPER_LAYER);
						entries.add(ModBlocks.WAXED_EXPOSED_COPPER_GRATE_LAYER);
						entries.add(ModBlocks.WAXED_EXPOSED_CUT_COPPER_LAYER);

						entries.add(ModBlocks.WAXED_WEATHERED_COPPER_LAYER);
						entries.add(ModBlocks.WAXED_WEATHERED_CHISELED_COPPER_LAYER);
						entries.add(ModBlocks.WAXED_WEATHERED_COPPER_GRATE_LAYER);
						entries.add(ModBlocks.WAXED_WEATHERED_CUT_COPPER_LAYER);

						entries.add(ModBlocks.WAXED_OXIDIZED_COPPER_LAYER);
						entries.add(ModBlocks.WAXED_OXIDIZED_CHISELED_COPPER_LAYER);
						entries.add(ModBlocks.WAXED_OXIDIZED_COPPER_GRATE_LAYER);
						entries.add(ModBlocks.WAXED_OXIDIZED_CUT_COPPER_LAYER);

						entries.add(ModBlocks.WHITE_WOOL_LAYER);
						entries.add(ModBlocks.LIGHT_GRAY_WOOL_LAYER);
						entries.add(ModBlocks.GRAY_WOOL_LAYER);
						entries.add(ModBlocks.BLACK_WOOL_LAYER);
						entries.add(ModBlocks.BROWN_WOOL_LAYER);
						entries.add(ModBlocks.RED_WOOL_LAYER);
						entries.add(ModBlocks.ORANGE_WOOL_LAYER);
						entries.add(ModBlocks.YELLOW_WOOL_LAYER);
						entries.add(ModBlocks.LIME_WOOL_LAYER);
						entries.add(ModBlocks.GREEN_WOOL_LAYER);
						entries.add(ModBlocks.CYAN_WOOL_LAYER);
						entries.add(ModBlocks.LIGHT_BLUE_WOOL_LAYER);
						entries.add(ModBlocks.BLUE_WOOL_LAYER);
						entries.add(ModBlocks.PURPLE_WOOL_LAYER);
						entries.add(ModBlocks.MAGENTA_WOOL_LAYER);
						entries.add(ModBlocks.PINK_WOOL_LAYER);

						entries.add(ModBlocks.TERRACOTTA_LAYER);
						entries.add(ModBlocks.WHITE_TERRACOTTA_LAYER);
						entries.add(ModBlocks.LIGHT_GRAY_TERRACOTTA_LAYER);
						entries.add(ModBlocks.GRAY_TERRACOTTA_LAYER);
						entries.add(ModBlocks.BLACK_TERRACOTTA_LAYER);
						entries.add(ModBlocks.BROWN_TERRACOTTA_LAYER);
						entries.add(ModBlocks.RED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.ORANGE_TERRACOTTA_LAYER);
						entries.add(ModBlocks.YELLOW_TERRACOTTA_LAYER);
						entries.add(ModBlocks.LIME_TERRACOTTA_LAYER);
						entries.add(ModBlocks.GREEN_TERRACOTTA_LAYER);
						entries.add(ModBlocks.CYAN_TERRACOTTA_LAYER);
						entries.add(ModBlocks.LIGHT_BLUE_TERRACOTTA_LAYER);
						entries.add(ModBlocks.BLUE_TERRACOTTA_LAYER);
						entries.add(ModBlocks.PURPLE_TERRACOTTA_LAYER);
						entries.add(ModBlocks.MAGENTA_TERRACOTTA_LAYER);
						entries.add(ModBlocks.PINK_TERRACOTTA_LAYER);

						entries.add(ModBlocks.WHITE_CONCRETE_LAYER);
						entries.add(ModBlocks.LIGHT_GRAY_CONCRETE_LAYER);
						entries.add(ModBlocks.GRAY_CONCRETE_LAYER);
						entries.add(ModBlocks.BLACK_CONCRETE_LAYER);
						entries.add(ModBlocks.BROWN_CONCRETE_LAYER);
						entries.add(ModBlocks.RED_CONCRETE_LAYER);
						entries.add(ModBlocks.ORANGE_CONCRETE_LAYER);
						entries.add(ModBlocks.YELLOW_CONCRETE_LAYER);
						entries.add(ModBlocks.LIME_CONCRETE_LAYER);
						entries.add(ModBlocks.GREEN_CONCRETE_LAYER);
						entries.add(ModBlocks.CYAN_CONCRETE_LAYER);
						entries.add(ModBlocks.LIGHT_BLUE_CONCRETE_LAYER);
						entries.add(ModBlocks.BLUE_CONCRETE_LAYER);
						entries.add(ModBlocks.PURPLE_CONCRETE_LAYER);
						entries.add(ModBlocks.MAGENTA_CONCRETE_LAYER);
						entries.add(ModBlocks.PINK_CONCRETE_LAYER);

						entries.add(ModBlocks.WHITE_CONCRETE_POWDER_LAYER);
						entries.add(ModBlocks.LIGHT_GRAY_CONCRETE_POWDER_LAYER);
						entries.add(ModBlocks.GRAY_CONCRETE_POWDER_LAYER);
						entries.add(ModBlocks.BLACK_CONCRETE_POWDER_LAYER);
						entries.add(ModBlocks.BROWN_CONCRETE_POWDER_LAYER);
						entries.add(ModBlocks.RED_CONCRETE_POWDER_LAYER);
						entries.add(ModBlocks.ORANGE_CONCRETE_POWDER_LAYER);
						entries.add(ModBlocks.YELLOW_CONCRETE_POWDER_LAYER);
						entries.add(ModBlocks.LIME_CONCRETE_POWDER_LAYER);
						entries.add(ModBlocks.GREEN_CONCRETE_POWDER_LAYER);
						entries.add(ModBlocks.CYAN_CONCRETE_POWDER_LAYER);
						entries.add(ModBlocks.LIGHT_BLUE_CONCRETE_POWDER_LAYER);
						entries.add(ModBlocks.BLUE_CONCRETE_POWDER_LAYER);
						entries.add(ModBlocks.PURPLE_CONCRETE_POWDER_LAYER);
						entries.add(ModBlocks.MAGENTA_CONCRETE_POWDER_LAYER);
						entries.add(ModBlocks.PINK_CONCRETE_POWDER_LAYER);

						entries.add(ModBlocks.WHITE_GLAZED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.LIGHT_GRAY_GLAZED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.GRAY_GLAZED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.BLACK_GLAZED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.BROWN_GLAZED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.RED_GLAZED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.ORANGE_GLAZED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.YELLOW_GLAZED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.LIME_GLAZED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.GREEN_GLAZED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.CYAN_GLAZED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.LIGHT_BLUE_GLAZED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.BLUE_GLAZED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.PURPLE_GLAZED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.MAGENTA_GLAZED_TERRACOTTA_LAYER);
						entries.add(ModBlocks.PINK_GLAZED_TERRACOTTA_LAYER);

						entries.add(ModBlocks.GLASS_LAYER);
						entries.add(ModBlocks.TINTED_GLASS_LAYER);
						entries.add(ModBlocks.WHITE_STAINED_GLASS_LAYER);
						entries.add(ModBlocks.LIGHT_GRAY_STAINED_GLASS_LAYER);
						entries.add(ModBlocks.GRAY_STAINED_GLASS_LAYER);
						entries.add(ModBlocks.BLACK_STAINED_GLASS_LAYER);
						entries.add(ModBlocks.BROWN_STAINED_GLASS_LAYER);
						entries.add(ModBlocks.RED_STAINED_GLASS_LAYER);
						entries.add(ModBlocks.ORANGE_STAINED_GLASS_LAYER);
						entries.add(ModBlocks.YELLOW_STAINED_GLASS_LAYER);
						entries.add(ModBlocks.LIME_STAINED_GLASS_LAYER);
						entries.add(ModBlocks.GREEN_STAINED_GLASS_LAYER);
						entries.add(ModBlocks.CYAN_STAINED_GLASS_LAYER);
						entries.add(ModBlocks.LIGHT_BLUE_STAINED_GLASS_LAYER);
						entries.add(ModBlocks.BLUE_STAINED_GLASS_LAYER);
						entries.add(ModBlocks.PURPLE_STAINED_GLASS_LAYER);
						entries.add(ModBlocks.MAGENTA_STAINED_GLASS_LAYER);
						entries.add(ModBlocks.PINK_STAINED_GLASS_LAYER);

						entries.add(ModBlocks.GRASS_LAYER);
						entries.add(ModBlocks.PODZOL_LAYER);
						entries.add(ModBlocks.MYCELIUM_LAYER);
						entries.add(ModBlocks.DIRT_PATH_LAYER);
						entries.add(ModBlocks.DIRT_LAYER);
						entries.add(ModBlocks.COARSE_DIRT_LAYER);
						entries.add(ModBlocks.ROOTED_DIRT_LAYER);
						entries.add(ModBlocks.FARMLAND_LAYER);

						entries.add(ModBlocks.MUD_LAYER);
						entries.add(ModBlocks.MUDDY_MANGROVE_ROOTS_LAYER);
						entries.add(ModBlocks.CLAY_LAYER);
						entries.add(ModBlocks.GRAVEL_LAYER);
						entries.add(ModBlocks.SAND_LAYER);
						entries.add(ModBlocks.RED_SAND_LAYER);

						entries.add(ModBlocks.ICE_LAYER);
						entries.add(ModBlocks.PACKED_ICE_LAYER);
						entries.add(ModBlocks.BLUE_ICE_LAYER);
						entries.add(ModBlocks.SNOW_LAYER);
						entries.add(ModBlocks.MOSS_LAYER);

						entries.add(ModBlocks.CALCITE_LAYER);
						entries.add(ModBlocks.DRIPSTONE_LAYER);
						entries.add(ModBlocks.MAGMA_LAYER);
						entries.add(ModBlocks.OBSIDIAN_LAYER);
						entries.add(ModBlocks.CRYING_OBSIDIAN_LAYER);

						entries.add(ModBlocks.CRIMSON_NYLIUM_LAYER);
						entries.add(ModBlocks.WARPED_NYLIUM_LAYER);
						entries.add(ModBlocks.SOUL_SAND_LAYER);
						entries.add(ModBlocks.SOUL_SOIL_LAYER);
						entries.add(ModBlocks.BONE_LAYER);

						entries.add(ModBlocks.COAL_ORE_LAYER);
						entries.add(ModBlocks.DEEPSLATE_COAL_ORE_LAYER);
						entries.add(ModBlocks.IRON_ORE_LAYER);
						entries.add(ModBlocks.DEEPSLATE_IRON_ORE_LAYER);
						entries.add(ModBlocks.COPPER_ORE_LAYER);
						entries.add(ModBlocks.DEEPSLATE_COPPER_ORE_LAYER);
						entries.add(ModBlocks.GOLD_ORE_LAYER);
						entries.add(ModBlocks.DEEPSLATE_GOLD_ORE_LAYER);
						entries.add(ModBlocks.REDSTONE_ORE_LAYER);
						entries.add(ModBlocks.DEEPSLATE_REDSTONE_ORE_LAYER);
						entries.add(ModBlocks.EMERALD_ORE_LAYER);
						entries.add(ModBlocks.DEEPSLATE_EMERALD_ORE_LAYER);
						entries.add(ModBlocks.LAPIS_ORE_LAYER);
						entries.add(ModBlocks.DEEPSLATE_LAPIS_ORE_LAYER);
						entries.add(ModBlocks.DIAMOND_ORE_LAYER);
						entries.add(ModBlocks.DEEPSLATE_DIAMOND_ORE_LAYER);
						entries.add(ModBlocks.NETHER_GOLD_ORE_LAYER);
						entries.add(ModBlocks.NETHER_QUARTZ_ORE_LAYER);
						entries.add(ModBlocks.ANCIENT_DEBRIS_LAYER);

						entries.add(ModBlocks.RAW_IRON_LAYER);
						entries.add(ModBlocks.RAW_COPPER_LAYER);
						entries.add(ModBlocks.RAW_GOLD_LAYER);
						entries.add(ModBlocks.GLOWSTONE_LAYER);

						entries.add(ModBlocks.OAK_LEAVES_LAYER);
						entries.add(ModBlocks.SPRUCE_LEAVES_LAYER);
						entries.add(ModBlocks.BIRCH_LEAVES_LAYER);
						entries.add(ModBlocks.JUNGLE_LEAVES_LAYER);
						entries.add(ModBlocks.ACACIA_LEAVES_LAYER);
						entries.add(ModBlocks.DARK_OAK_LEAVES_LAYER);
						entries.add(ModBlocks.MANGROVE_LEAVES_LAYER);
						entries.add(ModBlocks.CHERRY_LEAVES_LAYER);
						entries.add(ModBlocks.AZALEA_LEAVES_LAYER);
						entries.add(ModBlocks.FLOWERING_AZALEA_LEAVES_LAYER);

						entries.add(ModBlocks.BROWN_MUSHROOM_LAYER);
						entries.add(ModBlocks.RED_MUSHROOM_LAYER);
						entries.add(ModBlocks.NETHER_WART_LAYER);
						entries.add(ModBlocks.WARPED_WART_LAYER);
						entries.add(ModBlocks.SHROOMLIGHT_LAYER);
						entries.add(ModBlocks.DRIED_KELP_LAYER);

						entries.add(ModBlocks.TUBE_CORAL_LAYER);
						entries.add(ModBlocks.BRAIN_CORAL_LAYER);
						entries.add(ModBlocks.BUBBLE_CORAL_LAYER);
						entries.add(ModBlocks.FIRE_CORAL_LAYER);
						entries.add(ModBlocks.HORN_CORAL_LAYER);
						entries.add(ModBlocks.DEAD_TUBE_CORAL_LAYER);
						entries.add(ModBlocks.DEAD_BRAIN_CORAL_LAYER);
						entries.add(ModBlocks.DEAD_BUBBLE_CORAL_LAYER);
						entries.add(ModBlocks.DEAD_FIRE_CORAL_LAYER);
						entries.add(ModBlocks.DEAD_HORN_CORAL_LAYER);

						entries.add(ModBlocks.SPONGE_LAYER);
						entries.add(ModBlocks.WET_SPONGE_LAYER);
						entries.add(ModBlocks.MELON_LAYER);
						entries.add(ModBlocks.PUMPKIN_LAYER);
						entries.add(ModBlocks.HAY_LAYER);
						entries.add(ModBlocks.HONEYCOMB_LAYER);
						entries.add(ModBlocks.SLIME_LAYER);
						entries.add(ModBlocks.HONEY_LAYER);

						entries.add(ModBlocks.OCHRE_FROGLIGHT_LAYER);
						entries.add(ModBlocks.VERDANT_FROGLIGHT_LAYER);
						entries.add(ModBlocks.PEARLESCENT_FROGLIGHT_LAYER);
						entries.add(ModBlocks.SCULK_LAYER);
						entries.add(ModBlocks.BEDROCK_LAYER);
						entries.add(ModBlocks.TARGET_LAYER);
					}).build());

	public static final ItemGroup VLP_3D_GROUP = Registry.register(Registries.ITEM_GROUP,
			Identifier.of(VanillaLayerPlus.MOD_ID, "vlp_3d"),
			FabricItemGroup.builder().displayName(Text.translatable("itemgroup.vanillalayerplus_3d"))
					.icon(() -> new ItemStack(ModBlocks.STONE_3D_LAYER)).entries((displayContext, entries) ->
							Registries.BLOCK.stream().filter(block -> block instanceof Layer3DBlock).forEach(entries::add)).build());

	public static void registerItemGroups() {
	}
}
