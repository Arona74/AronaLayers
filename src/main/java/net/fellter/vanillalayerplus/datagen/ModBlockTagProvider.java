package net.fellter.vanillalayerplus.datagen;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.fellter.vanillalayerplus.VanillaLayerPlus;
import net.fellter.vanillalayerplus.block.Layer3DBlock;
import net.fellter.vanillalayerplus.block.LayerBlock;
import net.fellter.vanillalayerplus.registry.Args;
import net.fellter.vanillalayerplus.registry.DatagenArgs;
import net.fellter.vanillalayerplus.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
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

	public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		List<Set<ResourceLocation>> tierMembers = TOOL_TIER_TAGS.stream().map(tag -> readVanillaBlockTag(tag.location())).toList();

		BuiltInRegistries.BLOCK.stream().filter(VanillaLayerPlus::isNamespaced).forEach(block -> {
			ResourceLocation identifier = BuiltInRegistries.BLOCK.getKey(block);
			if (block instanceof LayerBlock && Args.DATAGEN_ARGS.containsKey(block)) {
				getOrCreateRawBuilder(ModTags.LAYERS).addElement(identifier);
				addInheritedTags(identifier, Args.DATAGEN_ARGS.get(block), tierMembers);
			} else if (block instanceof Layer3DBlock layer3D && Args.DATAGEN_ARGS.containsKey(layer3D.getBaseLayer())) {
				// A 3D layer is mined like its base layer.
				getOrCreateRawBuilder(ModTags.LAYERS_3D).addElement(identifier);
				addInheritedTags(identifier, Args.DATAGEN_ARGS.get(layer3D.getBaseLayer()), tierMembers);
			}
		});
	}

	private void addInheritedTags(ResourceLocation identifier, DatagenArgs args, List<Set<ResourceLocation>> tierMembers) {
		for (TagKey<Block> blockTagKey : args.blockTags) {
			getOrCreateRawBuilder(blockTagKey).addElement(identifier);
		}

		if (args.parentBlock != null) {
			ResourceLocation parent = BuiltInRegistries.BLOCK.getKey(args.parentBlock);

			for (int i = 0; i < TOOL_TIER_TAGS.size(); i++) {
				if (tierMembers.get(i).contains(parent)) {
					getOrCreateRawBuilder(TOOL_TIER_TAGS.get(i)).addElement(identifier);
				}
			}
		}
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
