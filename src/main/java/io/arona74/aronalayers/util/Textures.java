package io.arona74.aronalayers.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The textures of a layer's models, by texture slot ("all", "top", "side", "bottom"...), as
 * texture ids like "minecraft:block/oak_planks". A version-neutral stand-in for Minecraft's
 * TextureMapping, which each version's datagen builds from it.
 */
public final class Textures {
	private final Map<String, String> slots = new LinkedHashMap<>();

	/** The texture of a vanilla block, as Minecraft names it for its model: "minecraft:block/&lt;path&gt;&lt;suffix&gt;". */
	public static String vanilla(String blockPath, String suffix) {
		return "minecraft:block/" + blockPath + suffix;
	}

	/** One texture on every side, the vanilla block's own. */
	public static Textures all(String blockPath) {
		return allTexture(vanilla(blockPath, ""));
	}

	/** One texture on every side, given as a texture id. */
	public static Textures allTexture(String textureId) {
		return new Textures().put("all", textureId);
	}

	public Textures put(String slot, String textureId) {
		slots.put(slot, textureId);
		return this;
	}

	public Map<String, String> slots() {
		return Collections.unmodifiableMap(slots);
	}
}
