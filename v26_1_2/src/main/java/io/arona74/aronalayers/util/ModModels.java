package io.arona74.aronalayers.util;

import java.util.Optional;

import io.arona74.aronalayers.AronaLayers;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;

public class ModModels {
	public static final ModelTemplate LAYER_2 = ModModels.block("layer_height_2", "_height_2", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate LAYER_4 = ModModels.block("layer_height_4", "_height_4", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate LAYER_6 = ModModels.block("layer_height_6", "_height_6", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate LAYER_8 = ModModels.block("layer_height_8", "_height_8", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate LAYER_10 = ModModels.block("layer_height_10", "_height_10", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate LAYER_12 = ModModels.block("layer_height_12", "_height_12", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate LAYER_14 = ModModels.block("layer_height_14", "_height_14", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);

	public static final ModelTemplate LAYER_1 = ModModels.block15("layer_height_1", "_height_1", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate LAYER_3 = ModModels.block15("layer_height_3", "_height_3", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate LAYER_5 = ModModels.block15("layer_height_5", "_height_5", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate LAYER_7 = ModModels.block15("layer_height_7", "_height_7", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate LAYER_9 = ModModels.block15("layer_height_9", "_height_9", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate LAYER_11 = ModModels.block15("layer_height_11", "_height_11", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
	public static final ModelTemplate LAYER_13 = ModModels.block15("layer_height_13", "_height_13", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);

	private static ModelTemplate block(String parent, String variant, TextureSlot... requiredTextureKeys) {
		return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(AronaLayers.MOD_ID, "blockmodels/" + parent)), Optional.of(variant), requiredTextureKeys);
	}

	private static ModelTemplate block15(String parent, String variant, TextureSlot... requiredTextureKeys) {
		return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(AronaLayers.MOD_ID, "blockmodels/y15/" + parent)), Optional.of(variant), requiredTextureKeys);
	}

	// 3D layer models, indexed [vertical layers][horizontal layers], see Layer3DTemplates.
	public static final ModelTemplate[][] LAYER_3D = new ModelTemplate[8][8];

	static {
		for (int layers = 0; layers <= Layer3DTemplates.MAX_LAYERS; layers++) {
			for (int width = 1; width <= Layer3DTemplates.MAX_LAYERS; width++) {
				LAYER_3D[layers][width] = new ModelTemplate(Optional.of(layer3DTemplateId(layers, width)), Optional.of(Layer3DTemplates.suffix(layers, width)), TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
			}
		}
	}

	public static Identifier layer3DTemplateId(int layers, int horizontalLayers) {
		return Identifier.fromNamespaceAndPath(AronaLayers.MOD_ID, Layer3DTemplates.path(layers, horizontalLayers));
	}
}
