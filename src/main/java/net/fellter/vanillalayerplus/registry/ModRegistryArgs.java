package net.fellter.vanillalayerplus.registry;

import java.util.LinkedHashMap;
import java.util.Map;

import net.fellter.vanillalayerplus.block.Layer3DBlock;
import net.fellter.vanillalayerplus.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

/**
 * Gameplay registry args (stripping, tilling, flattening, oxidation, fuel...).
 * Kept apart from {@link Args}, which references client-only datagen classes, so it
 * can be loaded from the common initializer on a dedicated server.
 */
public final class ModRegistryArgs {
	public static Map<Block, RegistryArgs> REGISTRY_ARGS = new LinkedHashMap<>();

	private static void putInRegistryArgs(Block block, RegistryArgs registryArgs) {
		RegistryArgs oldArgs = REGISTRY_ARGS.put(block, registryArgs);

		if (oldArgs == null) {
			REGISTRY_ARGS.put(block, registryArgs);
		} else {
			throw new IllegalStateException("Block " + block.getName() + " has already been put in RegistryArgs source map.");
		}
	}

	public static void registerRegistryArgs() {
		putInRegistryArgs(ModBlocks.OAK_LAYER, new RegistryArgs().flammable(5, 20).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.OAK_LOG_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_OAK_LOG_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.OAK_WOOD_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_OAK_WOOD_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_OAK_LOG_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_OAK_WOOD_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.SPRUCE_LAYER, new RegistryArgs().flammable(5, 20).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.SPRUCE_LOG_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_SPRUCE_LOG_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.SPRUCE_WOOD_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_SPRUCE_WOOD_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_SPRUCE_LOG_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_SPRUCE_WOOD_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.BIRCH_LAYER, new RegistryArgs().flammable(5, 20).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.BIRCH_LOG_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_BIRCH_LOG_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.BIRCH_WOOD_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_BIRCH_WOOD_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_BIRCH_LOG_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_BIRCH_WOOD_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.JUNGLE_LAYER, new RegistryArgs().flammable(5, 20).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.JUNGLE_LOG_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_JUNGLE_LOG_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.JUNGLE_WOOD_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_JUNGLE_WOOD_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_JUNGLE_LOG_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_JUNGLE_WOOD_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.ACACIA_LAYER, new RegistryArgs().flammable(5, 20).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.ACACIA_LOG_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_ACACIA_LOG_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.ACACIA_WOOD_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_ACACIA_WOOD_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_ACACIA_LOG_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_ACACIA_WOOD_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.DARK_OAK_LAYER, new RegistryArgs().flammable(5, 20).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.DARK_OAK_LOG_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_DARK_OAK_LOG_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.DARK_OAK_WOOD_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_DARK_OAK_WOOD_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_DARK_OAK_LOG_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_DARK_OAK_WOOD_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.MANGROVE_LAYER, new RegistryArgs().flammable(5, 20).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.MANGROVE_LOG_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_MANGROVE_LOG_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.MANGROVE_WOOD_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_MANGROVE_WOOD_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_MANGROVE_LOG_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_MANGROVE_WOOD_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.CHERRY_LAYER, new RegistryArgs().flammable(5, 20).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.CHERRY_LOG_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_CHERRY_LOG_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.CHERRY_WOOD_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_CHERRY_WOOD_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_CHERRY_LOG_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_CHERRY_WOOD_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.BAMBOO_LAYER, new RegistryArgs().flammable(5, 20).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.BAMBOO_MOSAIC_LAYER, new RegistryArgs().flammable(5, 20).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.BAMBOO_BLOCK_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_BAMBOO_BLOCK_LAYER).flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.STRIPPED_BAMBOO_BLOCK_LAYER, new RegistryArgs().flammable(5, 5).fuel(RegistryArgs.FUEL_WOOD_BLOCKS));
		putInRegistryArgs(ModBlocks.CRIMSON_STEM_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_CRIMSON_STEM_LAYER));
		putInRegistryArgs(ModBlocks.CRIMSON_HYPHAE_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_CRIMSON_HYPHAE_LAYER));
		putInRegistryArgs(ModBlocks.WARPED_STEM_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_WARPED_STEM_LAYER));
		putInRegistryArgs(ModBlocks.WARPED_HYPHAE_LAYER, new RegistryArgs().stripped(ModBlocks.STRIPPED_WARPED_HYPHAE_LAYER));
		putInRegistryArgs(ModBlocks.WAXED_COPPER_GRATE_LAYER, new RegistryArgs().transparent());
		putInRegistryArgs(ModBlocks.WAXED_EXPOSED_COPPER_GRATE_LAYER, new RegistryArgs().transparent());
		putInRegistryArgs(ModBlocks.WAXED_WEATHERED_COPPER_GRATE_LAYER, new RegistryArgs().transparent());
		putInRegistryArgs(ModBlocks.WAXED_OXIDIZED_COPPER_GRATE_LAYER, new RegistryArgs().transparent());
		putInRegistryArgs(ModBlocks.EXPOSED_COPPER_GRATE_LAYER, new RegistryArgs().transparent());
		putInRegistryArgs(ModBlocks.WEATHERED_COPPER_GRATE_LAYER, new RegistryArgs().transparent());
		putInRegistryArgs(ModBlocks.OXIDIZED_COPPER_GRATE_LAYER, new RegistryArgs().transparent());
		putInRegistryArgs(ModBlocks.COPPER_LAYER, new RegistryArgs().oxidizable(ModBlocks.EXPOSED_COPPER_LAYER, ModBlocks.WEATHERED_COPPER_LAYER, ModBlocks.OXIDIZED_COPPER_LAYER, ModBlocks.WAXED_COPPER_LAYER, ModBlocks.WAXED_EXPOSED_COPPER_LAYER, ModBlocks.WAXED_WEATHERED_COPPER_LAYER, ModBlocks.WAXED_OXIDIZED_COPPER_LAYER));
		putInRegistryArgs(ModBlocks.CHISELED_COPPER_LAYER, new RegistryArgs().oxidizable(ModBlocks.EXPOSED_CHISELED_COPPER_LAYER, ModBlocks.WEATHERED_CHISELED_COPPER_LAYER, ModBlocks.OXIDIZED_CHISELED_COPPER_LAYER, ModBlocks.WAXED_CHISELED_COPPER_LAYER, ModBlocks.WAXED_EXPOSED_CHISELED_COPPER_LAYER, ModBlocks.WAXED_WEATHERED_CHISELED_COPPER_LAYER, ModBlocks.WAXED_OXIDIZED_CHISELED_COPPER_LAYER));
		putInRegistryArgs(ModBlocks.COPPER_GRATE_LAYER, new RegistryArgs().transparent().oxidizable(ModBlocks.EXPOSED_COPPER_GRATE_LAYER, ModBlocks.WEATHERED_COPPER_GRATE_LAYER, ModBlocks.OXIDIZED_COPPER_GRATE_LAYER, ModBlocks.WAXED_COPPER_GRATE_LAYER, ModBlocks.WEATHERED_COPPER_GRATE_LAYER, ModBlocks.WAXED_WEATHERED_COPPER_GRATE_LAYER, ModBlocks.WAXED_OXIDIZED_COPPER_GRATE_LAYER));
		putInRegistryArgs(ModBlocks.CUT_COPPER_LAYER, new RegistryArgs().oxidizable(ModBlocks.EXPOSED_CUT_COPPER_LAYER, ModBlocks.WEATHERED_CUT_COPPER_LAYER, ModBlocks.OXIDIZED_CUT_COPPER_LAYER, ModBlocks.WAXED_CUT_COPPER_LAYER, ModBlocks.WAXED_EXPOSED_CUT_COPPER_LAYER, ModBlocks.WAXED_WEATHERED_CUT_COPPER_LAYER, ModBlocks.WAXED_OXIDIZED_CUT_COPPER_LAYER));
		putInRegistryArgs(ModBlocks.GLASS_LAYER, new RegistryArgs().transparent());
		putInRegistryArgs(ModBlocks.TINTED_GLASS_LAYER, new RegistryArgs().transparent());
		putInRegistryArgs(ModBlocks.WHITE_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.LIGHT_GRAY_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.GRAY_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.BLACK_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.BROWN_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.RED_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.ORANGE_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.YELLOW_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.LIME_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.GREEN_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.CYAN_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.LIGHT_BLUE_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.BLUE_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.PURPLE_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.MAGENTA_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.PINK_STAINED_GLASS_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.DIRT_LAYER, new RegistryArgs().tillable(ModBlocks.FARMLAND_LAYER).flattenable(ModBlocks.DIRT_PATH_LAYER));
		putInRegistryArgs(ModBlocks.GRASS_LAYER, new RegistryArgs().transparent().grassTinted().flattenable(ModBlocks.DIRT_PATH_LAYER).tillable(ModBlocks.FARMLAND_LAYER));
		putInRegistryArgs(ModBlocks.PODZOL_LAYER, new RegistryArgs().flattenable(ModBlocks.DIRT_PATH_LAYER));
		putInRegistryArgs(ModBlocks.MYCELIUM_LAYER, new RegistryArgs().flattenable(ModBlocks.DIRT_PATH_LAYER));
		putInRegistryArgs(ModBlocks.COARSE_DIRT_LAYER, new RegistryArgs().flattenable(ModBlocks.DIRT_PATH_LAYER));
		putInRegistryArgs(ModBlocks.ROOTED_DIRT_LAYER, new RegistryArgs().flattenable(ModBlocks.DIRT_PATH_LAYER));
		putInRegistryArgs(ModBlocks.ICE_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.PACKED_ICE_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.BLUE_ICE_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.OAK_LEAVES_LAYER, new RegistryArgs().transparent().foliageTinted());
		putInRegistryArgs(ModBlocks.SPRUCE_LEAVES_LAYER, new RegistryArgs().transparent().foliageTinted());
		putInRegistryArgs(ModBlocks.BIRCH_LEAVES_LAYER, new RegistryArgs().transparent().foliageTinted());
		putInRegistryArgs(ModBlocks.JUNGLE_LEAVES_LAYER, new RegistryArgs().transparent().foliageTinted());
		putInRegistryArgs(ModBlocks.ACACIA_LEAVES_LAYER, new RegistryArgs().transparent().foliageTinted());
		putInRegistryArgs(ModBlocks.DARK_OAK_LEAVES_LAYER, new RegistryArgs().transparent().foliageTinted());
		putInRegistryArgs(ModBlocks.MANGROVE_LEAVES_LAYER, new RegistryArgs().transparent().foliageTinted());
		putInRegistryArgs(ModBlocks.CHERRY_LEAVES_LAYER, new RegistryArgs().transparent());
		putInRegistryArgs(ModBlocks.AZALEA_LEAVES_LAYER, new RegistryArgs().transparent());
		putInRegistryArgs(ModBlocks.FLOWERING_AZALEA_LEAVES_LAYER, new RegistryArgs().transparent());
		putInRegistryArgs(ModBlocks.SLIME_LAYER, new RegistryArgs().translucent());
		putInRegistryArgs(ModBlocks.HONEY_LAYER, new RegistryArgs().translucent());

		putInRegistryArgs(ModBlocks.WHITE_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));
		putInRegistryArgs(ModBlocks.LIGHT_GRAY_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));
		putInRegistryArgs(ModBlocks.GRAY_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));
		putInRegistryArgs(ModBlocks.BLACK_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));
		putInRegistryArgs(ModBlocks.BROWN_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));
		putInRegistryArgs(ModBlocks.RED_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));
		putInRegistryArgs(ModBlocks.ORANGE_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));
		putInRegistryArgs(ModBlocks.YELLOW_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));
		putInRegistryArgs(ModBlocks.LIME_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));
		putInRegistryArgs(ModBlocks.GREEN_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));
		putInRegistryArgs(ModBlocks.CYAN_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));
		putInRegistryArgs(ModBlocks.LIGHT_BLUE_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));
		putInRegistryArgs(ModBlocks.BLUE_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));
		putInRegistryArgs(ModBlocks.PURPLE_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));
		putInRegistryArgs(ModBlocks.MAGENTA_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));
		putInRegistryArgs(ModBlocks.PINK_WOOL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_WOOL));

		putInRegistryArgs(ModBlocks.COAL_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_COAL_BLOCK));
		putInRegistryArgs(ModBlocks.DRIED_KELP_LAYER, new RegistryArgs().fuel(RegistryArgs.FUEL_DRIED_KELP_BLOCK));

		register3DLayerArgs();
	}

	// 3D layers render like their base layer, but don't inherit tool interactions or fuel.
	private static void register3DLayerArgs() {
		BuiltInRegistries.BLOCK.forEach(block -> {
			if (block instanceof Layer3DBlock layer3D && REGISTRY_ARGS.containsKey(layer3D.getBaseLayer())) {
				RegistryArgs base = REGISTRY_ARGS.get(layer3D.getBaseLayer());
				RegistryArgs args = new RegistryArgs();
				args.transparent = base.transparent;
				args.translucent = base.translucent;
				args.grassTinted = base.grassTinted;
				args.foliageTinted = base.foliageTinted;
				putInRegistryArgs(block, args);
			}
		});
	}
}
