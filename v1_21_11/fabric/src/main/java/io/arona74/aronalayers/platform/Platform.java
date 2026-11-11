package io.arona74.aronalayers.platform;

import java.util.Map;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.registry.RegistryArgs;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.level.block.Block;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.fabric.api.registry.TillableBlockRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.fabric.impl.content.registry.util.ImmutableCollectionUtils;
import net.fabricmc.fabric.mixin.content.registry.AxeItemAccessor;
import net.fabricmc.fabric.mixin.content.registry.ShovelItemAccessor;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Fabric, Minecraft 1.21.11: loader services used by the shared code. Every loader subproject
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
		return FabricItemGroup.builder();
	}

	/** Registers a built-in data pack shipped in the mod jar, enabled by default. */
	public static void registerBuiltinPack(String name, Component title) {
		FabricLoader.getInstance().getModContainer(AronaLayers.MOD_ID).ifPresent(container ->
				ResourceLoader.registerBuiltinPack(id(name), container, title, PackActivationType.DEFAULT_ENABLED));
	}

	/**
	 * Registers a layer as fuel, burning 1/8 as long as its parent. Blast furnaces and smokers
	 * halve every burn time themselves, so the ratio holds there too.
	 */
	public static void registerLayerFuel(Block block, RegistryArgs.Fuel parent) {
		int burnTicks = parent.burnTicks() / 8;
		FuelRegistryEvents.BUILD.register((builder, context) -> builder.add(block, burnTicks * context.baseSmeltTime() / 200));
	}

	/** Makes the axe strip {@code input} into {@code stripped}. A mixin copies the layer properties over. */
	@SuppressWarnings("unstable")
	public static void registerStripping(Block input, Block stripped) {
		Map<Block, Block> strippables = ImmutableCollectionUtils.getAsMutableMap(AxeItemAccessor::getStrippedBlocks, AxeItemAccessor::setStrippedBlocks);
		strippables.put(input, stripped);
	}

	/** Makes the shovel flatten {@code input} into {@code flattened}. A mixin copies the layer properties over. */
	public static void registerFlattening(Block input, Block flattened) {
		ShovelItemAccessor.getPathStates().put(input, flattened.defaultBlockState());
	}

	/** Makes the hoe till {@code input} into {@code tilled} when there is air above. */
	public static void registerTilling(Block input, Block tilled) {
		TillableBlockRegistry.register(input, HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(tilled.defaultBlockState()));
	}

	public static void registerOxidizable(Block less, Block more) {
		OxidizableBlocksRegistry.registerOxidizableBlockPair(less, more);
	}

	public static void registerWaxable(Block unwaxed, Block waxed) {
		OxidizableBlocksRegistry.registerWaxableBlockPair(unwaxed, waxed);
	}

	/**
	 * Makes a block burn like a vanilla flammable block. The odds are in vanilla's
	 * FireBlock.setFlammable order: catching fire, then burning away.
	 */
	public static void registerFlammable(Block block, int igniteOdds, int burnOdds) {
		FlammableBlockRegistry.getDefaultInstance().add(block, igniteOdds, burnOdds);
	}
}
