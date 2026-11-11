package net.fellter.vanillalayerplus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fellter.vanillalayerplus.block.ModBlocks;
import net.fellter.vanillalayerplus.item.ModItemGroups;
import net.fellter.vanillalayerplus.item.ModItems;
import net.fellter.vanillalayerplus.registry.ModRegistries;
import net.fellter.vanillalayerplus.registry.ModRegistryArgs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;

public class VanillaLayerPlus implements ModInitializer {
	public static final String MOD_ID = "vanillalayerplus";
	public static final Logger LOGGER = LoggerFactory.getLogger("Vanilla+ Layers");

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
						Component.literal("Vanilla+ Layers: Reverse Recipes"), ResourcePackActivationType.DEFAULT_ENABLED));
	}

	public static boolean isNamespaced(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(MOD_ID);
	}
}
