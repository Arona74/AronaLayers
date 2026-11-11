package io.arona74.aronalayers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.arona74.aronalayers.compat.Compat;
import io.arona74.aronalayers.block.ModBlocks;
import io.arona74.aronalayers.item.ModItemGroups;
import io.arona74.aronalayers.item.ModItems;
import io.arona74.aronalayers.registry.ModRegistries;
import io.arona74.aronalayers.registry.ModRegistryArgs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.fabricmc.api.ModInitializer;

public class AronaLayers implements ModInitializer {
	public static final String MOD_ID = "aronalayers";
	public static final Logger LOGGER = LoggerFactory.getLogger("AronaLayers");

	public static final String REVERSE_RECIPES_PACK = "reverse_recipes";

	@Override
	public void onInitialize() {
		ModBlocks.registerModBlocks();
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();

		// Gameplay registrations run on both sides: tool interactions, oxidation and
		// fuel are applied by the server, including dedicated servers.
		ModRegistryArgs.registerRegistryArgs();
		ModRegistries.registerStrippableBlocks();
		ModRegistries.registerOxidizableBlocks();
		ModRegistries.registerFuel();
		ModRegistries.registerFlattenableBlocks();
		ModRegistries.registerTillableBlocks();

		Compat.registerBuiltinPack(REVERSE_RECIPES_PACK, Component.literal("AronaLayers: Reverse Recipes"));
	}

	public static boolean isNamespaced(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(MOD_ID);
	}
}
