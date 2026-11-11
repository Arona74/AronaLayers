package io.arona74.aronalayers.datagen;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonObject;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.registry.ModRegistryArgs;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

/**
 * Writes NeoForge's oxidation and waxing data maps, which is how NeoForge learns that copper
 * layers oxidize and can be waxed (Fabric registers the same pairs in code, see Platform). The
 * Fabric jar leaves data/neoforge out.
 */
public class ModNeoForgeDataMapProvider implements DataProvider {
	private final PackOutput.PathProvider dataMaps;

	public ModNeoForgeDataMapProvider(FabricDataOutput output) {
		this.dataMaps = output.createPathProvider(PackOutput.Target.DATA_PACK, "data_maps/block");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		Map<String, Block> oxidizables = new TreeMap<>();
		Map<String, Block> waxables = new TreeMap<>();

		ModRegistryArgs.REGISTRY_ARGS.forEach((block, args) -> {
			if (AronaLayers.isNamespaced(block) && args.oxidizables != null) {
				oxidizables.put(id(block), args.exposed);
				oxidizables.put(id(args.exposed), args.weathered);
				oxidizables.put(id(args.weathered), args.oxidized);

				waxables.put(id(block), args.waxed);
				waxables.put(id(args.exposed), args.exposedWaxed);
				waxables.put(id(args.weathered), args.weatheredWaxed);
				waxables.put(id(args.oxidized), args.oxidizedWaxed);
			}
		});

		return CompletableFuture.allOf(
				save(output, "oxidizables", oxidizables, "next_oxidation_stage"),
				save(output, "waxables", waxables, "waxed"));
	}

	private CompletableFuture<?> save(CachedOutput output, String name, Map<String, Block> entries, String field) {
		JsonObject values = new JsonObject();
		entries.forEach((block, target) -> {
			JsonObject value = new JsonObject();
			value.addProperty(field, id(target));
			values.add(block, value);
		});

		JsonObject json = new JsonObject();
		json.add("values", values);
		return DataProvider.saveStable(output, json, dataMaps.json(Identifier.fromNamespaceAndPath("neoforge", name)));
	}

	private static String id(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block).toString();
	}

	@Override
	public String getName() {
		return "NeoForge Data Maps";
	}
}
