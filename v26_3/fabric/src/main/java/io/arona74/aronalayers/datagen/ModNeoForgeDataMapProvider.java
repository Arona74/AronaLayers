package io.arona74.aronalayers.datagen;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.registry.ModRegistryArgs;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

/**
 * Writes NeoForge's data maps for what Fabric registers in code (see Platform): oxidation and
 * waxing, from which NeoForge also derives scraping and wax removal, and, since 26.3 made tool
 * interactions data-driven, the transformables that let the axe strip, the shovel flatten and the
 * hoe till layers. Entries follow the vanilla block transformers, and copy_properties keeps the
 * layer count and waterlogging. The Fabric jar leaves data/neoforge out.
 */
public class ModNeoForgeDataMapProvider implements DataProvider {
	private final PackOutput.PathProvider dataMaps;

	public ModNeoForgeDataMapProvider(FabricPackOutput output) {
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

		JsonObject transformables = new JsonObject();
		byId().forEach((name, block) -> {
			var args = ModRegistryArgs.REGISTRY_ARGS.get(block);
			JsonObject transformers = new JsonObject();

			if (args.stripped != null) {
				transformers.add("minecraft:axe", transform(block, args.stripped, false, "minecraft:item.axe.strip"));
			}

			if (args.flattened != null) {
				transformers.add("minecraft:shovel", transform(block, args.flattened, true, "minecraft:item.shovel.flatten"));
			}

			if (args.tilled != null) {
				transformers.add("minecraft:hoe", transform(block, args.tilled, true, "minecraft:item.hoe.till"));
			}

			if (!transformers.isEmpty()) {
				JsonObject value = new JsonObject();
				value.add("transformers", transformers);
				transformables.add(name, value);
			}
		});

		JsonObject transformablesJson = new JsonObject();
		transformablesJson.add("values", transformables);

		return CompletableFuture.allOf(
				save(output, "oxidizables", oxidizables, "next_oxidation_stage"),
				save(output, "waxables", waxables, "waxed"),
				DataProvider.saveStable(output, transformablesJson, dataMaps.json(Identifier.fromNamespaceAndPath("neoforge", "transformables"))));
	}

	private static Map<String, Block> byId() {
		Map<String, Block> blocks = new TreeMap<>();
		ModRegistryArgs.REGISTRY_ARGS.keySet().stream().filter(AronaLayers::isNamespaced).forEach(block -> blocks.put(id(block), block));
		return blocks;
	}

	// One block transformer entry, like the vanilla ones: turns input into output, keeping the
	// properties both share. Shovel and hoe entries need air above and can't be used from below.
	private static JsonObject transform(Block input, Block output, boolean needsAirAbove, String sound) {
		JsonObject matches = new JsonObject();
		matches.addProperty("type", "minecraft:matching_blocks");
		matches.addProperty("blocks", id(input));

		JsonObject condition = matches;

		if (needsAirAbove) {
			JsonObject air = new JsonObject();
			air.addProperty("type", "minecraft:matching_block_tag");
			JsonArray offset = new JsonArray();
			offset.add(0);
			offset.add(1);
			offset.add(0);
			air.add("offset", offset);
			air.addProperty("tag", "minecraft:air");

			JsonArray predicates = new JsonArray();
			predicates.add(matches);
			predicates.add(air);
			condition = new JsonObject();
			condition.addProperty("type", "minecraft:all_of");
			condition.add("predicates", predicates);
		}

		JsonObject source = new JsonObject();
		source.addProperty("id", id(output));
		JsonObject then = new JsonObject();
		then.addProperty("type", "minecraft:copy_properties");
		then.add("source", source);

		JsonObject rule = new JsonObject();
		rule.add("if_true", condition);
		rule.add("then", then);
		JsonArray rules = new JsonArray();
		rules.add(rule);

		JsonObject provider = new JsonObject();
		provider.addProperty("type", "minecraft:rule_based");
		provider.add("rules", rules);

		JsonObject data = new JsonObject();
		data.add("block_state_provider", provider);

		if (needsAirAbove) {
			JsonArray faces = new JsonArray();
			faces.add("down");
			data.add("disallowed_faces", faces);
		}

		data.addProperty("item_damage_per_use", 1);
		data.addProperty("sound", sound);
		return data;
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
