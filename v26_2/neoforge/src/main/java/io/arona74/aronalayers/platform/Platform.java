package io.arona74.aronalayers.platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.registry.RegistryArgs;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 * NeoForge, Minecraft 26.2: loader services used by the shared code. Every loader subproject
 * has a class with this name and these methods; vanilla differences between Minecraft
 * versions are in Compat instead. The shared code calls the register methods from
 * AronaLayers.init(); the NeoForge events that apply them are added by {@link #addListeners}.
 */
public final class Platform {
	private static final Map<String, Component> BUILTIN_PACKS = new HashMap<>();
	private static final Map<Block, Integer> FUELS = new HashMap<>();
	private static final Map<Block, Block> STRIPPING = new HashMap<>();
	private static final Map<Block, Block> FLATTENING = new HashMap<>();
	private static final Map<Block, Block> TILLING = new HashMap<>();
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
		NeoForge.EVENT_BUS.addListener(BlockEvent.BlockToolModificationEvent.class, Platform::modifyWithTool);
		NeoForge.EVENT_BUS.addListener(FurnaceFuelBurnTimeEvent.class, event -> {
			if (event.getItemStack().getItem() instanceof BlockItem blockItem && FUELS.containsKey(blockItem.getBlock())) {
				event.setBurnTime(FUELS.get(blockItem.getBlock()));
			}
		});
	}

	public static CreativeModeTab.Builder tabBuilder() {
		return CreativeModeTab.builder();
	}

	/** Registers a built-in data pack shipped in the mod jar under resourcepacks/, enabled by default. */
	public static void registerBuiltinPack(String name, Component title) {
		BUILTIN_PACKS.put(name, title);
	}

	/** Registers a layer as fuel, burning 1/8 as long as its parent, in ticks at vanilla's smelt time of 200. */
	public static void registerLayerFuel(Block block, RegistryArgs.Fuel parent) {
		FUELS.put(block, parent.burnTicks() / 8);
	}

	/** Makes the axe strip {@code input} into {@code stripped}, keeping the layer properties. */
	public static void registerStripping(Block input, Block stripped) {
		STRIPPING.put(input, stripped);
	}

	/** Makes the shovel flatten {@code input} into {@code flattened}, keeping the layer properties. */
	public static void registerFlattening(Block input, Block flattened) {
		FLATTENING.put(input, flattened);
	}

	/** Makes the hoe till {@code input} into {@code tilled} when there is air above. */
	public static void registerTilling(Block input, Block tilled) {
		TILLING.put(input, tilled);
	}

	// NeoForge reads oxidation and waxing from its neoforge:oxidizables and neoforge:waxables
	// data maps only. The Fabric subproject's datagen writes them from the same registry args.
	public static void registerOxidizable(Block less, Block more) {
	}

	public static void registerWaxable(Block unwaxed, Block waxed) {
	}

	/**
	 * Makes a block burn like a vanilla flammable block. FireBlock.setFlammable is private since
	 * 26.1; this subproject's access transformer makes it public. The odds are in vanilla's
	 * order: catching fire, then burning away.
	 */
	public static void registerFlammable(Block block, int igniteOdds, int burnOdds) {
		FLAMMABLES.add(() -> ((FireBlock) Blocks.FIRE).setFlammable(block, igniteOdds, burnOdds));
	}

	private static void modifyWithTool(BlockEvent.BlockToolModificationEvent event) {
		BlockState state = event.getState();
		ItemAbility ability = event.getItemAbility();
		Block result = null;

		if (ability == ItemAbilities.AXE_STRIP) {
			result = STRIPPING.get(state.getBlock());
		} else if (ability == ItemAbilities.SHOVEL_FLATTEN) {
			result = FLATTENING.get(state.getBlock());
		} else if (ability == ItemAbilities.HOE_TILL && event.getContext() != null
				&& event.getContext().getLevel().getBlockState(event.getContext().getClickedPos().above()).isAir()) {
			result = TILLING.get(state.getBlock());
		}

		if (result != null) {
			event.setFinalState(result.withPropertiesOf(state));
		}
	}
}
