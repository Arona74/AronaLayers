package io.arona74.aronalayers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.arona74.aronalayers.block.ModBlocks;
import io.arona74.aronalayers.item.ModItemGroups;
import io.arona74.aronalayers.item.ModItems;
import io.arona74.aronalayers.registry.ModRegistries;
import io.arona74.aronalayers.registry.ModRegistryArgs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;

public class AronaLayers implements ModInitializer {
	public static final String MOD_ID = "aronalayers";
	public static final Logger LOGGER = LoggerFactory.getLogger("AronaLayers");

	public static final ResourceLocation REVERSE_RECIPES_PACK = ResourceLocation.fromNamespaceAndPath(MOD_ID, "reverse_recipes");

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

		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container ->
				ResourceManagerHelper.registerBuiltinResourcePack(REVERSE_RECIPES_PACK, container,
						Component.literal("AronaLayers: Reverse Recipes"), ResourcePackActivationType.DEFAULT_ENABLED));
	}

	public static boolean isNamespaced(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(MOD_ID);
	}
}
