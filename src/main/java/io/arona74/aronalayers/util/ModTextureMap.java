package io.arona74.aronalayers.util;

import static io.arona74.aronalayers.util.Textures.vanilla;

public class ModTextureMap {
	public static Textures custom(String sideBlock, String topBlock, String bottomBlock, String sideSuffix, String topSuffix, String bottomSuffix) {
		return new Textures()
				.put("side", vanilla(sideBlock, sideSuffix))
				.put("top", vanilla(topBlock, topSuffix))
				.put("bottom", vanilla(bottomBlock, bottomSuffix));
	}

	public static Textures blockAndTopForEnds(String block) {
		return new Textures().put("side", vanilla(block, ""))
				.put("top", vanilla(block, "_top"))
				.put("bottom", vanilla(block, "_top"));
	}

	public static Textures sideAndTopForEnds(String block) {
		return new Textures().put("side", vanilla(block, "_side"))
				.put("top", vanilla(block, "_top"))
				.put("bottom", vanilla(block, "_top"));
	}

	public static Textures blockTB(String block) {
		return new Textures()
				.put("top", vanilla(block, "_top"))
				.put("side", vanilla(block, ""))
				.put("bottom", vanilla(block, "_bottom"));
	}

	public static Textures allWithSuffix(String block, String suffix) {
		return new Textures()
				.put("all", vanilla(block, suffix));
	}

	public static Textures blockSTB(String block) {
		return new Textures()
				.put("top", vanilla(block, "_top"))
				.put("side", vanilla(block, "_side"))
				.put("bottom", vanilla(block, "_bottom"));
	}

	/** Like {@link #blockSTB(String)}, for a texture id that isn't named after a block. */
	public static Textures textureSTB(String textureId) {
		return new Textures()
				.put("top", textureId + "_top")
				.put("side", textureId + "_side")
				.put("bottom", textureId + "_bottom");
	}
}
