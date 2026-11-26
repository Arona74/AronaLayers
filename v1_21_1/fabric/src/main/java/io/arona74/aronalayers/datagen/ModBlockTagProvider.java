package io.arona74.aronalayers.datagen;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.block.Layer3DBlock;
import io.arona74.aronalayers.block.LayerBlock;
import io.arona74.aronalayers.block.ModBlocks;
import io.arona74.aronalayers.registry.Args;
import io.arona74.aronalayers.registry.DatagenArgs;
import io.arona74.aronalayers.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	// Mining tier tags a layer inherits from its parent block, so e.g. an iron ore layer
	// only drops when mined with a stone pickaxe or better, like iron ore itself.
	private static final List<TagKey<Block>> TOOL_TIER_TAGS = List.of(BlockTags.NEEDS_STONE_TOOL, BlockTags.NEEDS_IRON_TOOL, BlockTags.NEEDS_DIAMOND_TOOL);
	// Tags a mob's spawn rule checks on the block under it (animals on grass, rabbits on sand,
	// goats on stone...): a layer of a block in one is in it too, so mobs spawn on layered ground
	// as on the full block. Names the running Minecraft version doesn't have are skipped.
	private static final List<String> SPAWN_TAGS = List.of("animals_spawnable_on", "armadillo_spawnable_on", "axolotls_spawnable_on",
			"bats_spawnable_on", "camels_spawnable_on", "foxes_spawnable_on", "frogs_spawnable_on", "goats_spawnable_on",
			"mooshrooms_spawnable_on", "parrots_spawnable_on", "polar_bears_spawnable_on_alternate", "rabbits_spawnable_on",
			"wolves_spawnable_on");

	public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		List<TagKey<Block>> inheritedTags = Stream.concat(TOOL_TIER_TAGS.stream(), SPAWN_TAGS.stream()
				.map(name -> TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace(name)))
				.filter(tag -> vanillaBlockTagExists(tag.location()))).toList();
		List<Set<ResourceLocation>> tierMembers = inheritedTags.stream().map(tag -> readVanillaBlockTag(tag.location())).toList();

		BuiltInRegistries.BLOCK.stream().filter(AronaLayers::isNamespaced).forEach(block -> {
			ResourceLocation identifier = BuiltInRegistries.BLOCK.getKey(block);
			if (block instanceof LayerBlock && Args.DATAGEN_ARGS.containsKey(block)) {
				getOrCreateRawBuilder(ModTags.LAYERS).addElement(identifier);
				addInheritedTags(identifier, Args.DATAGEN_ARGS.get(block), inheritedTags, tierMembers);
			} else if (block instanceof Layer3DBlock layer3D && Args.DATAGEN_ARGS.containsKey(layer3D.getBaseLayer())) {
				// A 3D layer is mined like its base layer.
				getOrCreateRawBuilder(ModTags.LAYERS_3D).addElement(identifier);
				addInheritedTags(identifier, Args.DATAGEN_ARGS.get(layer3D.getBaseLayer()), inheritedTags, tierMembers);
			}
		});

		// The zinc ore layers have no vanilla parent to inherit from: they are mined like Create's zinc ores.
		for (Block block : List.of(ModBlocks.ZINC_ORE_LAYER, ModBlocks.DEEPSLATE_ZINC_ORE_LAYER)) {
			getOrCreateRawBuilder(BlockTags.MINEABLE_WITH_PICKAXE).addElement(BuiltInRegistries.BLOCK.getKey(block));
			getOrCreateRawBuilder(BlockTags.NEEDS_IRON_TOOL).addElement(BuiltInRegistries.BLOCK.getKey(block));
		}
	}

	private void addInheritedTags(ResourceLocation identifier, DatagenArgs args, List<TagKey<Block>> inheritedTags, List<Set<ResourceLocation>> tierMembers) {
		for (TagKey<Block> blockTagKey : args.blockTags) {
			getOrCreateRawBuilder(blockTagKey).addElement(identifier);
		}

		if (args.parentBlock != null) {
			ResourceLocation parent = BuiltInRegistries.BLOCK.getKey(args.parentBlock);

			for (int i = 0; i < inheritedTags.size(); i++) {
				if (tierMembers.get(i).contains(parent)) {
					getOrCreateRawBuilder(inheritedTags.get(i)).addElement(identifier);
				}
			}
		}
	}

	private static boolean vanillaBlockTagExists(ResourceLocation tag) {
		return Block.class.getClassLoader().getResource("data/" + tag.getNamespace() + "/tags/block/" + tag.getPath() + ".json") != null;
	}

	// Vanilla tags are not bound during datagen, so read the tag file shipped in the
	// Minecraft jar instead, following "#tag" references. Every copy on the classpath is
	// merged: this mod's own previously generated file has the same path and could
	// otherwise hide the vanilla one.
	private static Set<ResourceLocation> readVanillaBlockTag(ResourceLocation tag) {
		Set<ResourceLocation> blocks = new HashSet<>();
		String path = "data/" + tag.getNamespace() + "/tags/block/" + tag.getPath() + ".json";

		try {
			List<URL> files = Collections.list(Block.class.getClassLoader().getResources(path));

			if (files.isEmpty()) {
				throw new IllegalStateException("Vanilla block tag not found: " + path);
			}

			for (URL file : files) {
				try (InputStream stream = file.openStream()) {
					for (JsonElement value : JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject().getAsJsonArray("values")) {
						String entry = value.isJsonObject() ? value.getAsJsonObject().get("id").getAsString() : value.getAsString();

						if (entry.startsWith("#")) {
							blocks.addAll(readVanillaBlockTag(ResourceLocation.parse(entry.substring(1))));
						} else {
							blocks.add(ResourceLocation.parse(entry));
						}
					}
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException("Could not read vanilla block tag " + path, e);
		}

		return blocks;
	}
}
