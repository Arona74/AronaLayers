package io.arona74.aronalayers.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.arona74.aronalayers.block.Layer3DBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ModBlockStateModelGenerator {
	private static Variant v(ResourceLocation id) {
		return Variant.variant().with(VariantProperties.MODEL, id);
	}

	private static Variant v(ResourceLocation id, VariantProperties.Rotation x, VariantProperties.Rotation y) {
		Variant variant = Variant.variant().with(VariantProperties.MODEL, id);
		if (x != null) variant = variant.with(VariantProperties.X_ROT, x);
		if (y != null) variant = variant.with(VariantProperties.Y_ROT, y);
		return variant;
	}

	public static void registerLayerBlock(Block layerBlock, Block fullBlock, BlockModelGenerators bsmg, TextureMapping textureMap) {
		ResourceLocation id = ModModels.LAYER_2.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id2 = ModModels.LAYER_4.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id3 = ModModels.LAYER_6.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id4 = ModModels.LAYER_8.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id5 = ModModels.LAYER_10.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id6 = ModModels.LAYER_12.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id7 = ModModels.LAYER_14.create(layerBlock, textureMap, bsmg.modelOutput);
		bsmg.blockStateOutput.accept(createLayerBlockState(layerBlock, id, id2, id3, id4, id5, id6, id7, ModelLocationUtils.getModelLocation(fullBlock)));
		bsmg.delegateItemModel(layerBlock, id);
	}

	public static void registerLayerBlock(Block layerBlock, ResourceLocation fullBlock, BlockModelGenerators bsmg, TextureMapping textureMap) {
		ResourceLocation id = ModModels.LAYER_2.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id2 = ModModels.LAYER_4.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id3 = ModModels.LAYER_6.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id4 = ModModels.LAYER_8.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id5 = ModModels.LAYER_10.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id6 = ModModels.LAYER_12.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id7 = ModModels.LAYER_14.create(layerBlock, textureMap, bsmg.modelOutput);
		bsmg.blockStateOutput.accept(createLayerBlockState(layerBlock, id, id2, id3, id4, id5, id6, id7, fullBlock));
		bsmg.delegateItemModel(layerBlock, id);
	}

	public static void registerLayerBlockY15(Block layerBlock, Block fullBlock, BlockModelGenerators bsmg, TextureMapping textureMap) {
		ResourceLocation id = ModModels.LAYER_1.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id2 = ModModels.LAYER_3.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id3 = ModModels.LAYER_5.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id4 = ModModels.LAYER_7.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id5 = ModModels.LAYER_9.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id6 = ModModels.LAYER_11.create(layerBlock, textureMap, bsmg.modelOutput);
		ResourceLocation id7 = ModModels.LAYER_13.create(layerBlock, textureMap, bsmg.modelOutput);
		bsmg.blockStateOutput.accept(createLayerBlockState(layerBlock, id, id2, id3, id4, id5, id6, id7, ModelLocationUtils.getModelLocation(fullBlock)));
		bsmg.delegateItemModel(layerBlock, id);
	}

	/**
	 * Registers the models and blockstate of a 3D layer. Models are built for the north facing
	 * and rotated for the others. Full states (either value at 8) use {@code fullBlock}.
	 */
	public static void registerLayer3DBlock(Layer3DBlock block, ResourceLocation fullBlock, BlockModelGenerators bsmg, TextureMapping textureMap) {
		ResourceLocation[][] models = new ResourceLocation[8][8];

		for (int layers = 0; layers <= 7; layers++) {
			for (int width = 1; width <= 7; width++) {
				models[layers][width] = ModModels.LAYER_3D[layers][width].create(block, textureMap, bsmg.modelOutput);
			}
		}

		bsmg.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
				.with(PropertyDispatch.properties(Layer3DBlock.HORIZONTAL_FACING, Layer3DBlock.VERTICAL_LAYERS, Layer3DBlock.HORIZONTAL_LAYERS)
						.generate((facing, layers, width) -> {
							if (layers == 8 || width == 8) {
								return v(fullBlock);
							}

							Variant variant = v(models[layers][width]).with(VariantProperties.UV_LOCK, true);
							return switch (facing) {
								case EAST -> variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
								case SOUTH -> variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180);
								case WEST -> variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
								default -> variant;
							};
						})));
		bsmg.delegateItemModel(block, models[4][4]);
	}

	/**
	 * Writes the 3D layer template models: a floor layer {@code vertical layers} high merged with
	 * a wall layer {@code horizontal layers} thick against the north side.
	 */
	public static void registerLayer3DTemplates(BlockModelGenerators bsmg) {
		for (int layers = 0; layers <= 7; layers++) {
			for (int width = 1; width <= 7; width++) {
				int h = layers * 2;
				int d = width * 2;
				bsmg.modelOutput.accept(ModModels.layer3DTemplateId(layers, width), () -> layer3DTemplate(h, d));
			}
		}
	}

	// Two elements that don't overlap: the wall (full height, d deep) and the floor in front of it
	// (h high, from d to the south edge). The floor has no north face, it would be inside the wall.
	// A pure wall layer (h = 0) has no floor element. UVs match the ones Minecraft derives from the
	// element bounds, so textures line up with full blocks.
	private static JsonObject layer3DTemplate(int h, int d) {
		JsonArray elements = new JsonArray();

		JsonObject wall = new JsonObject();
		wall.add("down", face(0, 16 - d, 16, 16, "#bottom", "down"));
		wall.add("up", face(0, 0, 16, d, "#top", "up"));
		wall.add("north", face(0, 0, 16, 16, "#side", "north"));
		wall.add("south", face(0, 0, 16, 16, "#side", null));
		wall.add("west", face(0, 0, d, 16, "#side", "west"));
		wall.add("east", face(16 - d, 0, 16, 16, "#side", "east"));
		elements.add(element(vec(0, 0, 0), vec(16, 16, d), wall));

		if (h > 0) {
			JsonObject floor = new JsonObject();
			floor.add("down", face(0, 0, 16, 16 - d, "#bottom", "down"));
			floor.add("up", face(0, d, 16, 16, "#top", null));
			floor.add("south", face(0, 16 - h, 16, 16, "#side", "south"));
			floor.add("west", face(d, 16 - h, 16, 16, "#side", "west"));
			floor.add("east", face(0, 16 - h, 16 - d, 16, "#side", "east"));
			elements.add(element(vec(0, 0, d), vec(16, h, 16), floor));
		}

		JsonObject textures = new JsonObject();
		textures.addProperty("particle", "#side");

		JsonObject json = new JsonObject();
		json.addProperty("parent", "block/block");
		json.add("textures", textures);
		json.add("elements", elements);
		return json;
	}

	private static JsonObject element(JsonArray from, JsonArray to, JsonObject faces) {
		JsonObject element = new JsonObject();
		element.add("from", from);
		element.add("to", to);
		element.add("faces", faces);
		return element;
	}

	// Every face is tinted like in the regular layer templates, so tinted blocks color fully.
	private static JsonObject face(int u1, int v1, int u2, int v2, String texture, String cullface) {
		JsonObject face = new JsonObject();
		face.add("uv", vec(u1, v1, u2, v2));
		face.addProperty("texture", texture);

		if (cullface != null) {
			face.addProperty("cullface", cullface);
		}

		face.addProperty("tintindex", 0);
		return face;
	}

	private static JsonArray vec(int... values) {
		JsonArray array = new JsonArray();

		for (int value : values) {
			array.add(value);
		}

		return array;
	}

	public static BlockStateGenerator createLayerBlockState(Block layerBlock,
			ResourceLocation h2, ResourceLocation h4, ResourceLocation h6, ResourceLocation h8,
			ResourceLocation h10, ResourceLocation h12, ResourceLocation h14, ResourceLocation fullBlock) {
		return MultiVariantGenerator.multiVariant(layerBlock)
				.with(PropertyDispatch.properties(BlockStateProperties.LAYERS, BlockStateProperties.FACING)
						// down
						.select(1, Direction.DOWN, v(h2))
						.select(2, Direction.DOWN, v(h4))
						.select(3, Direction.DOWN, v(h6))
						.select(4, Direction.DOWN, v(h8))
						.select(5, Direction.DOWN, v(h10))
						.select(6, Direction.DOWN, v(h12))
						.select(7, Direction.DOWN, v(h14))
						.select(8, Direction.DOWN, v(fullBlock))
						// up (x180)
						.select(1, Direction.UP, v(h2, VariantProperties.Rotation.R180, null))
						.select(2, Direction.UP, v(h4, VariantProperties.Rotation.R180, null))
						.select(3, Direction.UP, v(h6, VariantProperties.Rotation.R180, null))
						.select(4, Direction.UP, v(h8, VariantProperties.Rotation.R180, null))
						.select(5, Direction.UP, v(h10, VariantProperties.Rotation.R180, null))
						.select(6, Direction.UP, v(h12, VariantProperties.Rotation.R180, null))
						.select(7, Direction.UP, v(h14, VariantProperties.Rotation.R180, null))
						.select(8, Direction.UP, v(fullBlock, VariantProperties.Rotation.R180, null))
						// north (x270)
						.select(1, Direction.NORTH, v(h2, VariantProperties.Rotation.R270, null))
						.select(2, Direction.NORTH, v(h4, VariantProperties.Rotation.R270, null))
						.select(3, Direction.NORTH, v(h6, VariantProperties.Rotation.R270, null))
						.select(4, Direction.NORTH, v(h8, VariantProperties.Rotation.R270, null))
						.select(5, Direction.NORTH, v(h10, VariantProperties.Rotation.R270, null))
						.select(6, Direction.NORTH, v(h12, VariantProperties.Rotation.R270, null))
						.select(7, Direction.NORTH, v(h14, VariantProperties.Rotation.R270, null))
						.select(8, Direction.NORTH, v(fullBlock, VariantProperties.Rotation.R270, null))
						// west (x270, y270)
						.select(1, Direction.WEST, v(h2, VariantProperties.Rotation.R270, VariantProperties.Rotation.R270))
						.select(2, Direction.WEST, v(h4, VariantProperties.Rotation.R270, VariantProperties.Rotation.R270))
						.select(3, Direction.WEST, v(h6, VariantProperties.Rotation.R270, VariantProperties.Rotation.R270))
						.select(4, Direction.WEST, v(h8, VariantProperties.Rotation.R270, VariantProperties.Rotation.R270))
						.select(5, Direction.WEST, v(h10, VariantProperties.Rotation.R270, VariantProperties.Rotation.R270))
						.select(6, Direction.WEST, v(h12, VariantProperties.Rotation.R270, VariantProperties.Rotation.R270))
						.select(7, Direction.WEST, v(h14, VariantProperties.Rotation.R270, VariantProperties.Rotation.R270))
						.select(8, Direction.WEST, v(fullBlock, VariantProperties.Rotation.R270, VariantProperties.Rotation.R270))
						// south (x90)
						.select(1, Direction.SOUTH, v(h2, VariantProperties.Rotation.R90, null))
						.select(2, Direction.SOUTH, v(h4, VariantProperties.Rotation.R90, null))
						.select(3, Direction.SOUTH, v(h6, VariantProperties.Rotation.R90, null))
						.select(4, Direction.SOUTH, v(h8, VariantProperties.Rotation.R90, null))
						.select(5, Direction.SOUTH, v(h10, VariantProperties.Rotation.R90, null))
						.select(6, Direction.SOUTH, v(h12, VariantProperties.Rotation.R90, null))
						.select(7, Direction.SOUTH, v(h14, VariantProperties.Rotation.R90, null))
						.select(8, Direction.SOUTH, v(fullBlock, VariantProperties.Rotation.R90, null))
						// east (x270, y90)
						.select(1, Direction.EAST, v(h2, VariantProperties.Rotation.R270, VariantProperties.Rotation.R90))
						.select(2, Direction.EAST, v(h4, VariantProperties.Rotation.R270, VariantProperties.Rotation.R90))
						.select(3, Direction.EAST, v(h6, VariantProperties.Rotation.R270, VariantProperties.Rotation.R90))
						.select(4, Direction.EAST, v(h8, VariantProperties.Rotation.R270, VariantProperties.Rotation.R90))
						.select(5, Direction.EAST, v(h10, VariantProperties.Rotation.R270, VariantProperties.Rotation.R90))
						.select(6, Direction.EAST, v(h12, VariantProperties.Rotation.R270, VariantProperties.Rotation.R90))
						.select(7, Direction.EAST, v(h14, VariantProperties.Rotation.R270, VariantProperties.Rotation.R90))
						.select(8, Direction.EAST, v(fullBlock, VariantProperties.Rotation.R270, VariantProperties.Rotation.R90))
		);
	}
}
