package io.arona74.aronalayers.registry;

import java.util.Map;
import java.util.Objects;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.fabric.api.registry.TillableBlockRegistry;
import net.fabricmc.fabric.impl.content.registry.util.ImmutableCollectionUtils;
import net.fabricmc.fabric.mixin.content.registry.AxeItemAccessor;
import net.fabricmc.fabric.mixin.content.registry.ShovelItemAccessor;

public class ModRegistries {
	public static void register(Block input, Block stripped) {
		Block old = getRegistry().put(input, stripped);

		if (old != null) {
			AronaLayers.LOGGER.debug("Replaced old stripping mapping from {} to {} with {}", input, old, stripped);
		}
	}

	@SuppressWarnings("unstable")
	private static Map<Block, Block> getRegistry() {
		return ImmutableCollectionUtils.getAsMutableMap(AxeItemAccessor::getStrippedBlocks, AxeItemAccessor::setStrippedBlocks);
	}

	public static void registerStrippableBlocks() {
		BuiltInRegistries.BLOCK.forEach(block -> {
			if (ModRegistryArgs.REGISTRY_ARGS.containsKey(block)) {
				RegistryArgs registryArgs = ModRegistryArgs.REGISTRY_ARGS.get(block);

				if (BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(AronaLayers.MOD_ID) && registryArgs.stripped != null) {
					register(block, registryArgs.stripped);
				}
			}
		});
	}

	public static void registerTillableBlocks() {
		BuiltInRegistries.BLOCK.forEach(block -> {
			if (ModRegistryArgs.REGISTRY_ARGS.containsKey(block)) {
				ResourceLocation identifier = BuiltInRegistries.BLOCK.getKey(block);
				RegistryArgs args = ModRegistryArgs.REGISTRY_ARGS.get(block);

				if (identifier.getNamespace().equals(AronaLayers.MOD_ID) && args.tilled != null) {
					TillableBlockRegistry.register(block, HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(args.tilled.defaultBlockState()));
				}
			}
		});
	}

	public static final Map<Block, BlockState> FLATTENED_TO_BLOCK_MAP = Maps.newHashMap(new ImmutableMap.Builder<Block, BlockState>().build());

	public static void registerFlattenableBlocks() {
		flattenedToBlock(ModBlocks.DIRT_PATH_LAYER, ModBlocks.DIRT_LAYER);
		flattenedToBlock(ModBlocks.FARMLAND_LAYER, ModBlocks.DIRT_LAYER);

		BuiltInRegistries.BLOCK.forEach(block -> {
			if (ModRegistryArgs.REGISTRY_ARGS.containsKey(block)) {
				ResourceLocation identifier = BuiltInRegistries.BLOCK.getKey(block);
				RegistryArgs args = ModRegistryArgs.REGISTRY_ARGS.get(block);

				if (identifier.getNamespace().equals(AronaLayers.MOD_ID) && args.flattened != null) {
					blockToFlattened(block, args.flattened);
				}
			}
		});
	}

	private static void blockToFlattened(Block input, Block flattened) {
		Objects.requireNonNull(input, "input block cannot be null");
		Objects.requireNonNull(flattened, "flattened block state cannot be null");
		BlockState old = ShovelItemAccessor.getPathStates().put(input, flattened.defaultBlockState());

		if (old != null) {
			AronaLayers.LOGGER.debug("Replaced old flattening mapping from {} to {} with {}", input, old, flattened);
		}
	}

	private static void flattenedToBlock(Block flattened, Block output) {
		Objects.requireNonNull(flattened, "flattened block cannot be null");
		Objects.requireNonNull(output, "output block cannot be null");
		BlockState old = FLATTENED_TO_BLOCK_MAP.put(flattened, output.defaultBlockState());

		if (old != null) {
			AronaLayers.LOGGER.debug("Replaced old block mapping from {} to {} with {}", flattened, old, output);
		}
	}

	public static void registerOxidizableBlocks() {
		BuiltInRegistries.BLOCK.forEach(block -> {
			if (ModRegistryArgs.REGISTRY_ARGS.containsKey(block)) {
				RegistryArgs registryArgs = ModRegistryArgs.REGISTRY_ARGS.get(block);

				if (BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(AronaLayers.MOD_ID) && registryArgs.oxidizables != null) {
					OxidizableBlocksRegistry.registerOxidizableBlockPair(block, registryArgs.exposed);
					OxidizableBlocksRegistry.registerOxidizableBlockPair(registryArgs.exposed, registryArgs.weathered);
					OxidizableBlocksRegistry.registerOxidizableBlockPair(registryArgs.weathered, registryArgs.oxidized);

					OxidizableBlocksRegistry.registerWaxableBlockPair(block, registryArgs.waxed);
					OxidizableBlocksRegistry.registerWaxableBlockPair(registryArgs.exposed, registryArgs.exposedWaxed);
					OxidizableBlocksRegistry.registerWaxableBlockPair(registryArgs.weathered, registryArgs.weatheredWaxed);
					OxidizableBlocksRegistry.registerWaxableBlockPair(registryArgs.oxidized, registryArgs.oxidizedWaxed);
				}
			}
		});
	}

	// A layer burns 1/8 as long as its parent block. Blast furnaces and smokers
	// halve every burn time themselves, so the ratio holds there too.
	public static void registerFuel() {
		ModRegistryArgs.REGISTRY_ARGS.forEach((block, args) -> {
			if (args.fuel != null) {
				FuelRegistry.INSTANCE.add(block, args.fuel / 8);
			}
		});
	}
}
