package io.arona74.aronalayers.util;

import io.arona74.aronalayers.AronaLayers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {
	public static final TagKey<Block> LAYERS = createTag("layers");
	public static final TagKey<Block> LAYERS_3D = createTag("3d_layers");

	private static TagKey<Block> createTag(String name) {
		return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(AronaLayers.MOD_ID, name));
	}
}
