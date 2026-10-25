package net.fellter.vanillalayerplus.util;

import java.util.Optional;

import net.fellter.vanillalayerplus.VanillaLayerPlus;

import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.util.Identifier;

public class ModModels {
	public static final Model LAYER_2 = ModModels.block("layer_height_2", "_height_2", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);
	public static final Model LAYER_4 = ModModels.block("layer_height_4", "_height_4", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);
	public static final Model LAYER_6 = ModModels.block("layer_height_6", "_height_6", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);
	public static final Model LAYER_8 = ModModels.block("layer_height_8", "_height_8", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);
	public static final Model LAYER_10 = ModModels.block("layer_height_10", "_height_10", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);
	public static final Model LAYER_12 = ModModels.block("layer_height_12", "_height_12", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);
	public static final Model LAYER_14 = ModModels.block("layer_height_14", "_height_14", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);

	public static final Model LAYER_1 = ModModels.block15("layer_height_1", "_height_1", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);
	public static final Model LAYER_3 = ModModels.block15("layer_height_3", "_height_3", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);
	public static final Model LAYER_5 = ModModels.block15("layer_height_5", "_height_5", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);
	public static final Model LAYER_7 = ModModels.block15("layer_height_7", "_height_7", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);
	public static final Model LAYER_9 = ModModels.block15("layer_height_9", "_height_9", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);
	public static final Model LAYER_11 = ModModels.block15("layer_height_11", "_height_11", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);
	public static final Model LAYER_13 = ModModels.block15("layer_height_13", "_height_13", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);

	// 3D layer models, indexed [vertical layers][horizontal layers]. Both only go up to 7: if
	// either is 8 a 3D layer is a full cube and uses the full block's model.
	public static final Model[][] LAYER_3D = new Model[8][8];

	static {
		for (int layers = 0; layers <= 7; layers++) {
			for (int width = 1; width <= 7; width++) {
				LAYER_3D[layers][width] = new Model(Optional.of(layer3DTemplateId(layers, width)), Optional.of(layer3DSuffix(layers, width)), TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);
			}
		}
	}

	public static String layer3DSuffix(int layers, int horizontalLayers) {
		return "_height_" + layers * 2 + "_width_" + horizontalLayers * 2;
	}

	public static Identifier layer3DTemplateId(int layers, int horizontalLayers) {
		return Identifier.of(VanillaLayerPlus.MOD_ID, "blockmodels/layer_3d/layer" + layer3DSuffix(layers, horizontalLayers));
	}

	private static Model block(String parent, String variant, TextureKey... requiredTextureKeys) {
		return new Model(Optional.of(Identifier.of(VanillaLayerPlus.MOD_ID, "blockmodels/" + parent)), Optional.of(variant), requiredTextureKeys);
	}

	private static Model block15(String parent, String variant, TextureKey... requiredTextureKeys) {
		return new Model(Optional.of(Identifier.of(VanillaLayerPlus.MOD_ID, "blockmodels/y15/" + parent)), Optional.of(variant), requiredTextureKeys);
	}
}
