package io.arona74.aronalayers.platform;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.compat.Compat;
import io.arona74.aronalayers.registry.RegistryArgs;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.CopyPropertiesProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.item.v1.BlockTransformerHelper;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Fabric, Minecraft 26.3: loader services used by the shared code. Every loader subproject
 * has a class with this name and these methods; vanilla differences between Minecraft
 * versions are in Compat instead.
 */
public final class Platform {
	private Platform() {
	}

	private static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(AronaLayers.MOD_ID, path);
	}

	public static CreativeModeTab.Builder tabBuilder() {
		return FabricCreativeModeTab.builder();
	}

	/** Registers a built-in data pack shipped in the mod jar, enabled by default. */
	public static void registerBuiltinPack(String name, Component title) {
		FabricLoader.getInstance().getModContainer(AronaLayers.MOD_ID).ifPresent(container ->
				ResourceLoader.registerBuiltinPack(id(name), container, title, PackActivationType.DEFAULT_ENABLED));
	}

	/**
	 * Registers a layer as fuel, burning 1/8 as long as its parent. Since 26.3 fuel is the
	 * COOKING_FUEL item component, whose burn time points at a context_int_provider entry: the
	 * layer points at aronalayers:cooking/layer/<parent entry>, which datagen writes as the
	 * parent entry divided by 8. Blast furnaces, smokers and data packs that retune vanilla
	 * fuels stay in ratio that way.
	 */
	public static void registerLayerFuel(Block block, RegistryArgs.Fuel parent) {
		DefaultItemComponentEvents.MODIFY.register(context -> context.modify(block.asItem(), builder -> builder.set(DataComponents.COOKING_FUEL,
				new CookingFuel(Compat.layerBurnTime(parent), ContextFloatProviders.COOKING_DEFAULT_SPEED_MULTIPLIER))));
	}

	// Since 26.3 tool interactions are data-driven block transformers. Fabric appends these to
	// the vanilla axe, shovel and hoe transformers when they load. CopyPropertiesProvider carries
	// the layer count and waterlogging over, as the tool mixins do on older versions.

	/** Makes the axe strip {@code input} into {@code stripped}. */
	public static void registerStripping(Block input, Block stripped) {
		BlockTransformerHelper.registerStripping(input, stripped);
	}

	/** Makes the shovel flatten {@code input} into {@code flattened}. */
	public static void registerFlattening(Block input, Block flattened) {
		BlockTransformerHelper.registerFlattening(BlockPredicate.matchesBlocks(input), new CopyPropertiesProvider(flattened));
	}

	/** Makes the hoe till {@code input} into {@code tilled}. */
	public static void registerTilling(Block input, Block tilled) {
		BlockTransformerHelper.registerTilling(BlockPredicate.matchesBlocks(input), new CopyPropertiesProvider(tilled));
	}

	public static void registerOxidizable(Block less, Block more) {
		OxidizableBlocksRegistry.registerNextStage(less, more);
	}

	public static void registerWaxable(Block unwaxed, Block waxed) {
		OxidizableBlocksRegistry.registerWaxable(unwaxed, waxed);
	}
}
