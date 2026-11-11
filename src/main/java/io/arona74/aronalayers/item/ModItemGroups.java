package io.arona74.aronalayers.item;

import io.arona74.aronalayers.compat.Compat;
import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.block.Layer3DBlock;
import io.arona74.aronalayers.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

public class ModItemGroups {
	public static final CreativeModeTab VLP_GROUP = Compat.registerTab("vlp",
			FabricItemGroup.builder().title(Component.translatable("itemgroup.aronalayers"))
					.icon(() -> new ItemStack(ModItems.VLP_TITLE)).displayItems((displayContext, entries) -> {
						entries.accept(ModBlocks.OAK_LAYER);
						entries.accept(ModBlocks.OAK_LOG_LAYER);
						entries.accept(ModBlocks.OAK_WOOD_LAYER);
						entries.accept(ModBlocks.STRIPPED_OAK_LOG_LAYER);
						entries.accept(ModBlocks.STRIPPED_OAK_WOOD_LAYER);

						entries.accept(ModBlocks.SPRUCE_LAYER);
						entries.accept(ModBlocks.SPRUCE_LOG_LAYER);
						entries.accept(ModBlocks.SPRUCE_WOOD_LAYER);
						entries.accept(ModBlocks.STRIPPED_SPRUCE_LOG_LAYER);
						entries.accept(ModBlocks.STRIPPED_SPRUCE_WOOD_LAYER);

						entries.accept(ModBlocks.BIRCH_LAYER);
						entries.accept(ModBlocks.BIRCH_LOG_LAYER);
						entries.accept(ModBlocks.BIRCH_WOOD_LAYER);
						entries.accept(ModBlocks.STRIPPED_BIRCH_LOG_LAYER);
						entries.accept(ModBlocks.STRIPPED_BIRCH_WOOD_LAYER);

						entries.accept(ModBlocks.JUNGLE_LAYER);
						entries.accept(ModBlocks.JUNGLE_LOG_LAYER);
						entries.accept(ModBlocks.JUNGLE_WOOD_LAYER);
						entries.accept(ModBlocks.STRIPPED_JUNGLE_LOG_LAYER);
						entries.accept(ModBlocks.STRIPPED_JUNGLE_WOOD_LAYER);

						entries.accept(ModBlocks.ACACIA_LAYER);
						entries.accept(ModBlocks.ACACIA_LOG_LAYER);
						entries.accept(ModBlocks.ACACIA_WOOD_LAYER);
						entries.accept(ModBlocks.STRIPPED_ACACIA_LOG_LAYER);
						entries.accept(ModBlocks.STRIPPED_ACACIA_WOOD_LAYER);

						entries.accept(ModBlocks.DARK_OAK_LAYER);
						entries.accept(ModBlocks.DARK_OAK_LOG_LAYER);
						entries.accept(ModBlocks.DARK_OAK_WOOD_LAYER);
						entries.accept(ModBlocks.STRIPPED_DARK_OAK_LOG_LAYER);
						entries.accept(ModBlocks.STRIPPED_DARK_OAK_WOOD_LAYER);

						entries.accept(ModBlocks.MANGROVE_LAYER);
						entries.accept(ModBlocks.MANGROVE_LOG_LAYER);
						entries.accept(ModBlocks.MANGROVE_WOOD_LAYER);
						entries.accept(ModBlocks.STRIPPED_MANGROVE_LOG_LAYER);
						entries.accept(ModBlocks.STRIPPED_MANGROVE_WOOD_LAYER);

						entries.accept(ModBlocks.CHERRY_LAYER);
						entries.accept(ModBlocks.CHERRY_LOG_LAYER);
						entries.accept(ModBlocks.CHERRY_WOOD_LAYER);
						entries.accept(ModBlocks.STRIPPED_CHERRY_LOG_LAYER);
						entries.accept(ModBlocks.STRIPPED_CHERRY_WOOD_LAYER);


						entries.accept(ModBlocks.BAMBOO_LAYER);
						entries.accept(ModBlocks.BAMBOO_MOSAIC_LAYER);
						entries.accept(ModBlocks.BAMBOO_BLOCK_LAYER);
						entries.accept(ModBlocks.STRIPPED_BAMBOO_BLOCK_LAYER);

						entries.accept(ModBlocks.CRIMSON_LAYER);
						entries.accept(ModBlocks.CRIMSON_STEM_LAYER);
						entries.accept(ModBlocks.CRIMSON_HYPHAE_LAYER);
						entries.accept(ModBlocks.STRIPPED_CRIMSON_STEM_LAYER);
						entries.accept(ModBlocks.STRIPPED_CRIMSON_HYPHAE_LAYER);

						entries.accept(ModBlocks.WARPED_LAYER);
						entries.accept(ModBlocks.WARPED_STEM_LAYER);
						entries.accept(ModBlocks.WARPED_HYPHAE_LAYER);
						entries.accept(ModBlocks.STRIPPED_WARPED_STEM_LAYER);
						entries.accept(ModBlocks.STRIPPED_WARPED_HYPHAE_LAYER);

						entries.accept(ModBlocks.STONE_LAYER);
						entries.accept(ModBlocks.COBBLESTONE_LAYER);
						entries.accept(ModBlocks.MOSSY_COBBLESTONE_LAYER);
						entries.accept(ModBlocks.SMOOTH_STONE_LAYER);
						entries.accept(ModBlocks.STONE_BRICKS_LAYER);
						entries.accept(ModBlocks.CRACKED_STONE_BRICKS_LAYER);
						entries.accept(ModBlocks.CHISELED_STONE_BRICKS_LAYER);
						entries.accept(ModBlocks.MOSSY_STONE_BRICKS_LAYER);

						entries.accept(ModBlocks.GRANITE_LAYER);
						entries.accept(ModBlocks.POLISHED_GRANITE_LAYER);
						entries.accept(ModBlocks.DIORITE_LAYER);
						entries.accept(ModBlocks.POLISHED_DIORITE_LAYER);
						entries.accept(ModBlocks.ANDESITE_LAYER);
						entries.accept(ModBlocks.POLISHED_ANDESITE_LAYER);

						entries.accept(ModBlocks.DEEPSLATE_LAYER);
						entries.accept(ModBlocks.REINFORCED_DEEPSLATE_LAYER);
						entries.accept(ModBlocks.COBBLED_DEEPSLATE_LAYER);
						entries.accept(ModBlocks.CHISELED_DEEPSLATE_LAYER);
						entries.accept(ModBlocks.POLISHED_DEEPSLATE_LAYER);
						entries.accept(ModBlocks.DEEPSLATE_BRICKS_LAYER);
						entries.accept(ModBlocks.CRACKED_DEEPSLATE_BRICKS_LAYER);
						entries.accept(ModBlocks.DEEPSLATE_TILES_LAYER);
						entries.accept(ModBlocks.CRACKED_DEEPSLATE_TILES_LAYER);

						entries.accept(ModBlocks.TUFF_LAYER);
						entries.accept(ModBlocks.CHISELED_TUFF_LAYER);
						entries.accept(ModBlocks.POLISHED_TUFF_LAYER);
						entries.accept(ModBlocks.TUFF_BRICKS_LAYER);
						entries.accept(ModBlocks.CHISELED_TUFF_BRICKS_LAYER);

						entries.accept(ModBlocks.BRICKS_LAYER);
						entries.accept(ModBlocks.PACKED_MUD_LAYER);
						entries.accept(ModBlocks.MUD_BRICKS_LAYER);

						entries.accept(ModBlocks.SANDSTONE_LAYER);
						entries.accept(ModBlocks.CHISELED_SANDSTONE_LAYER);
						entries.accept(ModBlocks.SMOOTH_SANDSTONE_LAYER);
						entries.accept(ModBlocks.CUT_SANDSTONE_LAYER);

						entries.accept(ModBlocks.RED_SANDSTONE_LAYER);
						entries.accept(ModBlocks.CHISELED_RED_SANDSTONE_LAYER);
						entries.accept(ModBlocks.SMOOTH_RED_SANDSTONE_LAYER);
						entries.accept(ModBlocks.CUT_RED_SANDSTONE_LAYER);

						entries.accept(ModBlocks.SEA_LANTERN_LAYER);
						entries.accept(ModBlocks.PRISMARINE_LAYER);
						entries.accept(ModBlocks.PRISMARINE_BRICKS_LAYER);
						entries.accept(ModBlocks.DARK_PRISMARINE_LAYER);

						entries.accept(ModBlocks.NETHERRACK_LAYER);
						entries.accept(ModBlocks.NETHER_BRICKS_LAYER);
						entries.accept(ModBlocks.CRACKED_NETHER_BRICKS_LAYER);
						entries.accept(ModBlocks.CHISELED_NETHER_BRICKS_LAYER);
						entries.accept(ModBlocks.RED_NETHER_BRICKS_LAYER);

						entries.accept(ModBlocks.BASALT_LAYER);
						entries.accept(ModBlocks.SMOOTH_BASALT_LAYER);
						entries.accept(ModBlocks.POLISHED_BASALT_LAYER);

						entries.accept(ModBlocks.BLACKSTONE_LAYER);
						entries.accept(ModBlocks.GILDED_BLACKSTONE_LAYER);
						entries.accept(ModBlocks.CHISELED_POLISHED_BLACKSTONE_LAYER);
						entries.accept(ModBlocks.POLISHED_BLACKSTONE_LAYER);
						entries.accept(ModBlocks.CRACKED_POLISHED_BLACKSTONE_BRICKS_LAYER);
						entries.accept(ModBlocks.POLISHED_BLACKSTONE_BRICKS_LAYER);

						entries.accept(ModBlocks.END_STONE_LAYER);
						entries.accept(ModBlocks.END_STONE_BRICKS_LAYER);
						entries.accept(ModBlocks.PURPUR_LAYER);
						entries.accept(ModBlocks.PURPUR_PILLAR_LAYER);

						entries.accept(ModBlocks.COAL_LAYER);
						entries.accept(ModBlocks.IRON_LAYER);
						entries.accept(ModBlocks.GOLD_LAYER);
						entries.accept(ModBlocks.REDSTONE_LAYER);
						entries.accept(ModBlocks.EMERALD_LAYER);
						entries.accept(ModBlocks.LAPIS_LAYER);
						entries.accept(ModBlocks.DIAMOND_LAYER);
						entries.accept(ModBlocks.NETHERITE_LAYER);

						entries.accept(ModBlocks.QUARTZ_LAYER);
						entries.accept(ModBlocks.CHISELED_QUARTZ_LAYER);
						entries.accept(ModBlocks.QUARTZ_BRICKS_LAYER);
						entries.accept(ModBlocks.QUARTZ_PILLAR_LAYER);
						entries.accept(ModBlocks.SMOOTH_QUARTZ_LAYER);

						entries.accept(ModBlocks.AMETHYST_LAYER);

						entries.accept(ModBlocks.COPPER_LAYER);
						entries.accept(ModBlocks.CHISELED_COPPER_LAYER);
						entries.accept(ModBlocks.COPPER_GRATE_LAYER);
						entries.accept(ModBlocks.CUT_COPPER_LAYER);

						entries.accept(ModBlocks.EXPOSED_COPPER_LAYER);
						entries.accept(ModBlocks.EXPOSED_CHISELED_COPPER_LAYER);
						entries.accept(ModBlocks.EXPOSED_COPPER_GRATE_LAYER);
						entries.accept(ModBlocks.EXPOSED_CUT_COPPER_LAYER);

						entries.accept(ModBlocks.WEATHERED_COPPER_LAYER);
						entries.accept(ModBlocks.WEATHERED_CHISELED_COPPER_LAYER);
						entries.accept(ModBlocks.WEATHERED_COPPER_GRATE_LAYER);
						entries.accept(ModBlocks.WEATHERED_CUT_COPPER_LAYER);

						entries.accept(ModBlocks.OXIDIZED_COPPER_LAYER);
						entries.accept(ModBlocks.OXIDIZED_CHISELED_COPPER_LAYER);
						entries.accept(ModBlocks.OXIDIZED_COPPER_GRATE_LAYER);
						entries.accept(ModBlocks.OXIDIZED_CUT_COPPER_LAYER);

						entries.accept(ModBlocks.WAXED_COPPER_LAYER);
						entries.accept(ModBlocks.WAXED_CHISELED_COPPER_LAYER);
						entries.accept(ModBlocks.WAXED_COPPER_GRATE_LAYER);
						entries.accept(ModBlocks.WAXED_CUT_COPPER_LAYER);

						entries.accept(ModBlocks.WAXED_EXPOSED_COPPER_LAYER);
						entries.accept(ModBlocks.WAXED_EXPOSED_CHISELED_COPPER_LAYER);
						entries.accept(ModBlocks.WAXED_EXPOSED_COPPER_GRATE_LAYER);
						entries.accept(ModBlocks.WAXED_EXPOSED_CUT_COPPER_LAYER);

						entries.accept(ModBlocks.WAXED_WEATHERED_COPPER_LAYER);
						entries.accept(ModBlocks.WAXED_WEATHERED_CHISELED_COPPER_LAYER);
						entries.accept(ModBlocks.WAXED_WEATHERED_COPPER_GRATE_LAYER);
						entries.accept(ModBlocks.WAXED_WEATHERED_CUT_COPPER_LAYER);

						entries.accept(ModBlocks.WAXED_OXIDIZED_COPPER_LAYER);
						entries.accept(ModBlocks.WAXED_OXIDIZED_CHISELED_COPPER_LAYER);
						entries.accept(ModBlocks.WAXED_OXIDIZED_COPPER_GRATE_LAYER);
						entries.accept(ModBlocks.WAXED_OXIDIZED_CUT_COPPER_LAYER);

						entries.accept(ModBlocks.WHITE_WOOL_LAYER);
						entries.accept(ModBlocks.LIGHT_GRAY_WOOL_LAYER);
						entries.accept(ModBlocks.GRAY_WOOL_LAYER);
						entries.accept(ModBlocks.BLACK_WOOL_LAYER);
						entries.accept(ModBlocks.BROWN_WOOL_LAYER);
						entries.accept(ModBlocks.RED_WOOL_LAYER);
						entries.accept(ModBlocks.ORANGE_WOOL_LAYER);
						entries.accept(ModBlocks.YELLOW_WOOL_LAYER);
						entries.accept(ModBlocks.LIME_WOOL_LAYER);
						entries.accept(ModBlocks.GREEN_WOOL_LAYER);
						entries.accept(ModBlocks.CYAN_WOOL_LAYER);
						entries.accept(ModBlocks.LIGHT_BLUE_WOOL_LAYER);
						entries.accept(ModBlocks.BLUE_WOOL_LAYER);
						entries.accept(ModBlocks.PURPLE_WOOL_LAYER);
						entries.accept(ModBlocks.MAGENTA_WOOL_LAYER);
						entries.accept(ModBlocks.PINK_WOOL_LAYER);

						entries.accept(ModBlocks.TERRACOTTA_LAYER);
						entries.accept(ModBlocks.WHITE_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.LIGHT_GRAY_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.GRAY_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.BLACK_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.BROWN_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.RED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.ORANGE_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.YELLOW_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.LIME_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.GREEN_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.CYAN_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.LIGHT_BLUE_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.BLUE_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.PURPLE_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.MAGENTA_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.PINK_TERRACOTTA_LAYER);

						entries.accept(ModBlocks.WHITE_CONCRETE_LAYER);
						entries.accept(ModBlocks.LIGHT_GRAY_CONCRETE_LAYER);
						entries.accept(ModBlocks.GRAY_CONCRETE_LAYER);
						entries.accept(ModBlocks.BLACK_CONCRETE_LAYER);
						entries.accept(ModBlocks.BROWN_CONCRETE_LAYER);
						entries.accept(ModBlocks.RED_CONCRETE_LAYER);
						entries.accept(ModBlocks.ORANGE_CONCRETE_LAYER);
						entries.accept(ModBlocks.YELLOW_CONCRETE_LAYER);
						entries.accept(ModBlocks.LIME_CONCRETE_LAYER);
						entries.accept(ModBlocks.GREEN_CONCRETE_LAYER);
						entries.accept(ModBlocks.CYAN_CONCRETE_LAYER);
						entries.accept(ModBlocks.LIGHT_BLUE_CONCRETE_LAYER);
						entries.accept(ModBlocks.BLUE_CONCRETE_LAYER);
						entries.accept(ModBlocks.PURPLE_CONCRETE_LAYER);
						entries.accept(ModBlocks.MAGENTA_CONCRETE_LAYER);
						entries.accept(ModBlocks.PINK_CONCRETE_LAYER);

						entries.accept(ModBlocks.WHITE_CONCRETE_POWDER_LAYER);
						entries.accept(ModBlocks.LIGHT_GRAY_CONCRETE_POWDER_LAYER);
						entries.accept(ModBlocks.GRAY_CONCRETE_POWDER_LAYER);
						entries.accept(ModBlocks.BLACK_CONCRETE_POWDER_LAYER);
						entries.accept(ModBlocks.BROWN_CONCRETE_POWDER_LAYER);
						entries.accept(ModBlocks.RED_CONCRETE_POWDER_LAYER);
						entries.accept(ModBlocks.ORANGE_CONCRETE_POWDER_LAYER);
						entries.accept(ModBlocks.YELLOW_CONCRETE_POWDER_LAYER);
						entries.accept(ModBlocks.LIME_CONCRETE_POWDER_LAYER);
						entries.accept(ModBlocks.GREEN_CONCRETE_POWDER_LAYER);
						entries.accept(ModBlocks.CYAN_CONCRETE_POWDER_LAYER);
						entries.accept(ModBlocks.LIGHT_BLUE_CONCRETE_POWDER_LAYER);
						entries.accept(ModBlocks.BLUE_CONCRETE_POWDER_LAYER);
						entries.accept(ModBlocks.PURPLE_CONCRETE_POWDER_LAYER);
						entries.accept(ModBlocks.MAGENTA_CONCRETE_POWDER_LAYER);
						entries.accept(ModBlocks.PINK_CONCRETE_POWDER_LAYER);

						entries.accept(ModBlocks.WHITE_GLAZED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.LIGHT_GRAY_GLAZED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.GRAY_GLAZED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.BLACK_GLAZED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.BROWN_GLAZED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.RED_GLAZED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.ORANGE_GLAZED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.YELLOW_GLAZED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.LIME_GLAZED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.GREEN_GLAZED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.CYAN_GLAZED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.LIGHT_BLUE_GLAZED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.BLUE_GLAZED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.PURPLE_GLAZED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.MAGENTA_GLAZED_TERRACOTTA_LAYER);
						entries.accept(ModBlocks.PINK_GLAZED_TERRACOTTA_LAYER);

						entries.accept(ModBlocks.GLASS_LAYER);
						entries.accept(ModBlocks.TINTED_GLASS_LAYER);
						entries.accept(ModBlocks.WHITE_STAINED_GLASS_LAYER);
						entries.accept(ModBlocks.LIGHT_GRAY_STAINED_GLASS_LAYER);
						entries.accept(ModBlocks.GRAY_STAINED_GLASS_LAYER);
						entries.accept(ModBlocks.BLACK_STAINED_GLASS_LAYER);
						entries.accept(ModBlocks.BROWN_STAINED_GLASS_LAYER);
						entries.accept(ModBlocks.RED_STAINED_GLASS_LAYER);
						entries.accept(ModBlocks.ORANGE_STAINED_GLASS_LAYER);
						entries.accept(ModBlocks.YELLOW_STAINED_GLASS_LAYER);
						entries.accept(ModBlocks.LIME_STAINED_GLASS_LAYER);
						entries.accept(ModBlocks.GREEN_STAINED_GLASS_LAYER);
						entries.accept(ModBlocks.CYAN_STAINED_GLASS_LAYER);
						entries.accept(ModBlocks.LIGHT_BLUE_STAINED_GLASS_LAYER);
						entries.accept(ModBlocks.BLUE_STAINED_GLASS_LAYER);
						entries.accept(ModBlocks.PURPLE_STAINED_GLASS_LAYER);
						entries.accept(ModBlocks.MAGENTA_STAINED_GLASS_LAYER);
						entries.accept(ModBlocks.PINK_STAINED_GLASS_LAYER);

						entries.accept(ModBlocks.GRASS_LAYER);
						entries.accept(ModBlocks.PODZOL_LAYER);
						entries.accept(ModBlocks.MYCELIUM_LAYER);
						entries.accept(ModBlocks.DIRT_PATH_LAYER);
						entries.accept(ModBlocks.DIRT_LAYER);
						entries.accept(ModBlocks.COARSE_DIRT_LAYER);
						entries.accept(ModBlocks.ROOTED_DIRT_LAYER);
						entries.accept(ModBlocks.FARMLAND_LAYER);

						entries.accept(ModBlocks.MUD_LAYER);
						entries.accept(ModBlocks.MUDDY_MANGROVE_ROOTS_LAYER);
						entries.accept(ModBlocks.CLAY_LAYER);
						entries.accept(ModBlocks.GRAVEL_LAYER);
						entries.accept(ModBlocks.SAND_LAYER);
						entries.accept(ModBlocks.RED_SAND_LAYER);

						entries.accept(ModBlocks.ICE_LAYER);
						entries.accept(ModBlocks.PACKED_ICE_LAYER);
						entries.accept(ModBlocks.BLUE_ICE_LAYER);
						entries.accept(ModBlocks.SNOW_LAYER);
						entries.accept(ModBlocks.MOSS_LAYER);

						entries.accept(ModBlocks.CALCITE_LAYER);
						entries.accept(ModBlocks.DRIPSTONE_LAYER);
						entries.accept(ModBlocks.MAGMA_LAYER);
						entries.accept(ModBlocks.OBSIDIAN_LAYER);
						entries.accept(ModBlocks.CRYING_OBSIDIAN_LAYER);

						entries.accept(ModBlocks.CRIMSON_NYLIUM_LAYER);
						entries.accept(ModBlocks.WARPED_NYLIUM_LAYER);
						entries.accept(ModBlocks.SOUL_SAND_LAYER);
						entries.accept(ModBlocks.SOUL_SOIL_LAYER);
						entries.accept(ModBlocks.BONE_LAYER);

						entries.accept(ModBlocks.COAL_ORE_LAYER);
						entries.accept(ModBlocks.DEEPSLATE_COAL_ORE_LAYER);
						entries.accept(ModBlocks.IRON_ORE_LAYER);
						entries.accept(ModBlocks.DEEPSLATE_IRON_ORE_LAYER);
						entries.accept(ModBlocks.COPPER_ORE_LAYER);
						entries.accept(ModBlocks.DEEPSLATE_COPPER_ORE_LAYER);
						entries.accept(ModBlocks.GOLD_ORE_LAYER);
						entries.accept(ModBlocks.DEEPSLATE_GOLD_ORE_LAYER);
						entries.accept(ModBlocks.REDSTONE_ORE_LAYER);
						entries.accept(ModBlocks.DEEPSLATE_REDSTONE_ORE_LAYER);
						entries.accept(ModBlocks.EMERALD_ORE_LAYER);
						entries.accept(ModBlocks.DEEPSLATE_EMERALD_ORE_LAYER);
						entries.accept(ModBlocks.LAPIS_ORE_LAYER);
						entries.accept(ModBlocks.DEEPSLATE_LAPIS_ORE_LAYER);
						entries.accept(ModBlocks.DIAMOND_ORE_LAYER);
						entries.accept(ModBlocks.DEEPSLATE_DIAMOND_ORE_LAYER);
						entries.accept(ModBlocks.NETHER_GOLD_ORE_LAYER);
						entries.accept(ModBlocks.NETHER_QUARTZ_ORE_LAYER);
						entries.accept(ModBlocks.ANCIENT_DEBRIS_LAYER);

						entries.accept(ModBlocks.RAW_IRON_LAYER);
						entries.accept(ModBlocks.RAW_COPPER_LAYER);
						entries.accept(ModBlocks.RAW_GOLD_LAYER);
						entries.accept(ModBlocks.GLOWSTONE_LAYER);

						entries.accept(ModBlocks.OAK_LEAVES_LAYER);
						entries.accept(ModBlocks.SPRUCE_LEAVES_LAYER);
						entries.accept(ModBlocks.BIRCH_LEAVES_LAYER);
						entries.accept(ModBlocks.JUNGLE_LEAVES_LAYER);
						entries.accept(ModBlocks.ACACIA_LEAVES_LAYER);
						entries.accept(ModBlocks.DARK_OAK_LEAVES_LAYER);
						entries.accept(ModBlocks.MANGROVE_LEAVES_LAYER);
						entries.accept(ModBlocks.CHERRY_LEAVES_LAYER);
						entries.accept(ModBlocks.AZALEA_LEAVES_LAYER);
						entries.accept(ModBlocks.FLOWERING_AZALEA_LEAVES_LAYER);

						entries.accept(ModBlocks.BROWN_MUSHROOM_LAYER);
						entries.accept(ModBlocks.RED_MUSHROOM_LAYER);
						entries.accept(ModBlocks.NETHER_WART_LAYER);
						entries.accept(ModBlocks.WARPED_WART_LAYER);
						entries.accept(ModBlocks.SHROOMLIGHT_LAYER);
						entries.accept(ModBlocks.DRIED_KELP_LAYER);

						entries.accept(ModBlocks.TUBE_CORAL_LAYER);
						entries.accept(ModBlocks.BRAIN_CORAL_LAYER);
						entries.accept(ModBlocks.BUBBLE_CORAL_LAYER);
						entries.accept(ModBlocks.FIRE_CORAL_LAYER);
						entries.accept(ModBlocks.HORN_CORAL_LAYER);
						entries.accept(ModBlocks.DEAD_TUBE_CORAL_LAYER);
						entries.accept(ModBlocks.DEAD_BRAIN_CORAL_LAYER);
						entries.accept(ModBlocks.DEAD_BUBBLE_CORAL_LAYER);
						entries.accept(ModBlocks.DEAD_FIRE_CORAL_LAYER);
						entries.accept(ModBlocks.DEAD_HORN_CORAL_LAYER);

						entries.accept(ModBlocks.SPONGE_LAYER);
						entries.accept(ModBlocks.WET_SPONGE_LAYER);
						entries.accept(ModBlocks.MELON_LAYER);
						entries.accept(ModBlocks.PUMPKIN_LAYER);
						entries.accept(ModBlocks.HAY_LAYER);
						entries.accept(ModBlocks.HONEYCOMB_LAYER);
						entries.accept(ModBlocks.SLIME_LAYER);
						entries.accept(ModBlocks.HONEY_LAYER);

						entries.accept(ModBlocks.OCHRE_FROGLIGHT_LAYER);
						entries.accept(ModBlocks.VERDANT_FROGLIGHT_LAYER);
						entries.accept(ModBlocks.PEARLESCENT_FROGLIGHT_LAYER);
						entries.accept(ModBlocks.SCULK_LAYER);
						entries.accept(ModBlocks.BEDROCK_LAYER);
						entries.accept(ModBlocks.TARGET_LAYER);
					}).build());

	public static final CreativeModeTab VLP_3D_GROUP = Compat.registerTab("vlp_3d",
			FabricItemGroup.builder().title(Component.translatable("itemgroup.aronalayers_3d"))
					.icon(() -> new ItemStack(ModBlocks.STONE_3D_LAYER)).displayItems((displayContext, entries) ->
							BuiltInRegistries.BLOCK.stream().filter(block -> block instanceof Layer3DBlock).forEach(entries::accept)).build());

	public static void registerItemGroups() {
	}
}
