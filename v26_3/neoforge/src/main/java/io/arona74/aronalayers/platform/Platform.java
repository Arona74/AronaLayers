package io.arona74.aronalayers.platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.compat.Compat;
import io.arona74.aronalayers.registry.RegistryArgs;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

/**
 * NeoForge, Minecraft 26.3: loader services used by the shared code. Every loader subproject
 * has a class with this name and these methods; vanilla differences between Minecraft
 * versions are in Compat instead. The shared code calls the register methods from
 * AronaLayers.init(); the NeoForge events that apply them are added by {@link #addListeners}.
 */
public final class Platform {
	private static final Map<String, Component> BUILTIN_PACKS = new HashMap<>();
	private static final Map<Block, RegistryArgs.Fuel> FUELS = new HashMap<>();
	private static final List<Runnable> FLAMMABLES = new ArrayList<>();

	private Platform() {
	}

	private static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(AronaLayers.MOD_ID, path);
	}

	/** Called by the NeoForge entry point, before the shared registrations run. */
	public static void addListeners(IEventBus modBus) {
		modBus.addListener(AddPackFindersEvent.class, event -> {
			if (event.getPackType() == PackType.SERVER_DATA) {
				// BUILT_IN packs are enabled in new worlds, like Fabric's DEFAULT_ENABLED.
				BUILTIN_PACKS.forEach((name, title) -> event.addPackFinders(id("resourcepacks/" + name), PackType.SERVER_DATA, title, PackSource.BUILT_IN, false, Pack.Position.TOP));
			}
		});
		modBus.addListener(FMLCommonSetupEvent.class, event -> event.enqueueWork(() -> FLAMMABLES.forEach(Runnable::run)));
		modBus.addListener(ModifyDefaultComponentsEvent.class, event -> FUELS.forEach((block, parent) ->
				event.modify(block, (components, registries, item) -> components.set(DataComponents.COOKING_FUEL,
						new CookingFuel(Compat.layerBurnTime(parent), ContextFloatProviders.COOKING_DEFAULT_SPEED_MULTIPLIER)))));
	}

	public static CreativeModeTab.Builder tabBuilder() {
		return CreativeModeTab.builder();
	}

	/** Registers a built-in data pack shipped in the mod jar under resourcepacks/, enabled by default. */
	public static void registerBuiltinPack(String name, Component title) {
		BUILTIN_PACKS.put(name, title);
	}

	/**
	 * Registers a layer as fuel, burning 1/8 as long as its parent. Since 26.3 fuel is the
	 * COOKING_FUEL item component, whose burn time points at a context_int_provider entry: the
	 * layer points at aronalayers:cooking/layer/<parent entry>, which datagen writes as the
	 * parent entry divided by 8, as on Fabric.
	 */
	public static void registerLayerFuel(Block block, RegistryArgs.Fuel parent) {
		FUELS.put(block, parent);
	}

	// Since 26.3 the axe, shovel and hoe use data-driven block transformers. NeoForge adds this
	// mod's entries from its neoforge:transformables data map, which the Fabric subproject's
	// datagen writes from the same registry args (with copy_properties, so layer counts are kept),
	// and derives scraping and wax removal from the oxidizables and waxables data maps.
	public static void registerStripping(Block input, Block stripped) {
	}

	public static void registerFlattening(Block input, Block flattened) {
	}

	public static void registerTilling(Block input, Block tilled) {
	}

	public static void registerOxidizable(Block less, Block more) {
	}

	public static void registerWaxable(Block unwaxed, Block waxed) {
	}

	/**
	 * Makes a block burn like a vanilla flammable block. FireBlock.setFlammable is private since
	 * 26.1; this subproject's access transformer makes it public. NeoForge's neoforge:flammable
	 * data map comes first, and blocks without an entry fall back to these odds. The odds are in
	 * vanilla's order: catching fire, then burning away.
	 */
	public static void registerFlammable(Block block, int igniteOdds, int burnOdds) {
		FLAMMABLES.add(() -> ((FireBlock) Blocks.FIRE).setFlammable(block, igniteOdds, burnOdds));
	}
}
