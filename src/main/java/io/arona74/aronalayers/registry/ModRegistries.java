package io.arona74.aronalayers.registry;

import java.util.Map;
import java.util.Objects;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import io.arona74.aronalayers.compat.Compat;
import io.arona74.aronalayers.platform.Platform;
import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ModRegistries {
	public static void registerStrippableBlocks() {
		BuiltInRegistries.BLOCK.forEach(block -> {
			if (ModRegistryArgs.REGISTRY_ARGS.containsKey(block)) {
				RegistryArgs registryArgs = ModRegistryArgs.REGISTRY_ARGS.get(block);

				if (BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(AronaLayers.MOD_ID) && registryArgs.stripped != null) {
					Platform.registerStripping(block, registryArgs.stripped);
				}
			}
		});
	}

	public static void registerTillableBlocks() {
		BuiltInRegistries.BLOCK.forEach(block -> {
			if (ModRegistryArgs.REGISTRY_ARGS.containsKey(block)) {
				var identifier = BuiltInRegistries.BLOCK.getKey(block);
				RegistryArgs args = ModRegistryArgs.REGISTRY_ARGS.get(block);

				if (identifier.getNamespace().equals(AronaLayers.MOD_ID) && args.tilled != null) {
					Platform.registerTilling(block, args.tilled);
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
				var identifier = BuiltInRegistries.BLOCK.getKey(block);
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
		Platform.registerFlattening(input, flattened);
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
					Platform.registerOxidizable(block, registryArgs.exposed);
					Platform.registerOxidizable(registryArgs.exposed, registryArgs.weathered);
					Platform.registerOxidizable(registryArgs.weathered, registryArgs.oxidized);

					Platform.registerWaxable(block, registryArgs.waxed);
					Platform.registerWaxable(registryArgs.exposed, registryArgs.exposedWaxed);
					Platform.registerWaxable(registryArgs.weathered, registryArgs.weatheredWaxed);
					Platform.registerWaxable(registryArgs.oxidized, registryArgs.oxidizedWaxed);
				}
			}
		});
	}

	// Layers catch fire and burn away like their parent block.
	public static void registerFlammableBlocks() {
		ModRegistryArgs.REGISTRY_ARGS.forEach((block, args) -> {
			if (args.burn != null && args.spread != null) {
				Platform.registerFlammable(block, args.burn, args.spread);
			}
		});
	}

	// A layer burns 1/8 as long as its parent block, see Platform.registerLayerFuel.
	public static void registerFuel() {
		ModRegistryArgs.REGISTRY_ARGS.forEach((block, args) -> {
			if (args.fuel != null) {
				Platform.registerLayerFuel(block, args.fuel);
			}
		});
	}
}
