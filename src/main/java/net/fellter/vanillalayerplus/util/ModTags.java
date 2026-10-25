package net.fellter.vanillalayerplus.util;

import net.fellter.vanillalayerplus.VanillaLayerPlus;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
	public static final TagKey<Block> LAYERS = createTag("layers");
	public static final TagKey<Block> LAYERS_3D = createTag("3d_layers");

	private static TagKey<Block> createTag(String name) {
		return TagKey.of(RegistryKeys.BLOCK, Identifier.of(VanillaLayerPlus.MOD_ID, name));
	}
}
