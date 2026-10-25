package net.fellter.vanillalayerplus.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.fellter.vanillalayerplus.block.Layer3DBlock;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class ModBlockStateModelGenerator {
	private static BlockStateVariant v(Identifier id) {
		return BlockStateVariant.create().put(VariantSettings.MODEL, id);
	}

	private static BlockStateVariant v(Identifier id, VariantSettings.Rotation x, VariantSettings.Rotation y) {
		BlockStateVariant variant = BlockStateVariant.create().put(VariantSettings.MODEL, id);
		if (x != null) variant = variant.put(VariantSettings.X, x);
		if (y != null) variant = variant.put(VariantSettings.Y, y);
		return variant;
	}

	public static void registerLayerBlock(Block layerBlock, Block fullBlock, BlockStateModelGenerator bsmg, TextureMap textureMap) {
		Identifier id = ModModels.LAYER_2.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id2 = ModModels.LAYER_4.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id3 = ModModels.LAYER_6.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id4 = ModModels.LAYER_8.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id5 = ModModels.LAYER_10.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id6 = ModModels.LAYER_12.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id7 = ModModels.LAYER_14.upload(layerBlock, textureMap, bsmg.modelCollector);
		bsmg.blockStateCollector.accept(createLayerBlockState(layerBlock, id, id2, id3, id4, id5, id6, id7, ModelIds.getBlockModelId(fullBlock)));
		bsmg.registerParentedItemModel(layerBlock, id);
	}

	public static void registerLayerBlock(Block layerBlock, Identifier fullBlock, BlockStateModelGenerator bsmg, TextureMap textureMap) {
		Identifier id = ModModels.LAYER_2.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id2 = ModModels.LAYER_4.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id3 = ModModels.LAYER_6.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id4 = ModModels.LAYER_8.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id5 = ModModels.LAYER_10.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id6 = ModModels.LAYER_12.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id7 = ModModels.LAYER_14.upload(layerBlock, textureMap, bsmg.modelCollector);
		bsmg.blockStateCollector.accept(createLayerBlockState(layerBlock, id, id2, id3, id4, id5, id6, id7, fullBlock));
		bsmg.registerParentedItemModel(layerBlock, id);
	}

	public static void registerLayerBlockY15(Block layerBlock, Block fullBlock, BlockStateModelGenerator bsmg, TextureMap textureMap) {
		Identifier id = ModModels.LAYER_1.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id2 = ModModels.LAYER_3.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id3 = ModModels.LAYER_5.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id4 = ModModels.LAYER_7.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id5 = ModModels.LAYER_9.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id6 = ModModels.LAYER_11.upload(layerBlock, textureMap, bsmg.modelCollector);
		Identifier id7 = ModModels.LAYER_13.upload(layerBlock, textureMap, bsmg.modelCollector);
		bsmg.blockStateCollector.accept(createLayerBlockState(layerBlock, id, id2, id3, id4, id5, id6, id7, ModelIds.getBlockModelId(fullBlock)));
		bsmg.registerParentedItemModel(layerBlock, id);
	}

	/**
	 * Registers the models and blockstate of a 3D layer. Models are built for the north facing
	 * and rotated for the others. Full states (either value at 8) use {@code fullBlock}.
	 */
	public static void registerLayer3DBlock(Layer3DBlock block, Identifier fullBlock, BlockStateModelGenerator bsmg, TextureMap textureMap) {
		Identifier[][] models = new Identifier[8][8];

		for (int layers = 0; layers <= 7; layers++) {
			for (int width = 1; width <= 7; width++) {
				models[layers][width] = ModModels.LAYER_3D[layers][width].upload(block, textureMap, bsmg.modelCollector);
			}
		}

		bsmg.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
				.coordinate(BlockStateVariantMap.create(Layer3DBlock.HORIZONTAL_FACING, Layer3DBlock.VERTICAL_LAYERS, Layer3DBlock.HORIZONTAL_LAYERS)
						.register((facing, layers, width) -> {
							if (layers == 8 || width == 8) {
								return v(fullBlock);
							}

							BlockStateVariant variant = v(models[layers][width]).put(VariantSettings.UVLOCK, true);
							return switch (facing) {
								case EAST -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R90);
								case SOUTH -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R180);
								case WEST -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R270);
								default -> variant;
							};
						})));
		bsmg.registerParentedItemModel(block, models[4][4]);
	}

	/**
	 * Writes the 3D layer template models: a floor layer {@code vertical layers} high merged with
	 * a wall layer {@code horizontal layers} thick against the north side.
	 */
	public static void registerLayer3DTemplates(BlockStateModelGenerator bsmg) {
		for (int layers = 0; layers <= 7; layers++) {
			for (int width = 1; width <= 7; width++) {
				int h = layers * 2;
				int d = width * 2;
				bsmg.modelCollector.accept(ModModels.layer3DTemplateId(layers, width), () -> layer3DTemplate(h, d));
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

	public static BlockStateSupplier createLayerBlockState(Block layerBlock,
			Identifier h2, Identifier h4, Identifier h6, Identifier h8,
			Identifier h10, Identifier h12, Identifier h14, Identifier fullBlock) {
		return VariantsBlockStateSupplier.create(layerBlock)
				.coordinate(BlockStateVariantMap.create(Properties.LAYERS, Properties.FACING)
						// down
						.register(1, Direction.DOWN, v(h2))
						.register(2, Direction.DOWN, v(h4))
						.register(3, Direction.DOWN, v(h6))
						.register(4, Direction.DOWN, v(h8))
						.register(5, Direction.DOWN, v(h10))
						.register(6, Direction.DOWN, v(h12))
						.register(7, Direction.DOWN, v(h14))
						.register(8, Direction.DOWN, v(fullBlock))
						// up (x180)
						.register(1, Direction.UP, v(h2, VariantSettings.Rotation.R180, null))
						.register(2, Direction.UP, v(h4, VariantSettings.Rotation.R180, null))
						.register(3, Direction.UP, v(h6, VariantSettings.Rotation.R180, null))
						.register(4, Direction.UP, v(h8, VariantSettings.Rotation.R180, null))
						.register(5, Direction.UP, v(h10, VariantSettings.Rotation.R180, null))
						.register(6, Direction.UP, v(h12, VariantSettings.Rotation.R180, null))
						.register(7, Direction.UP, v(h14, VariantSettings.Rotation.R180, null))
						.register(8, Direction.UP, v(fullBlock, VariantSettings.Rotation.R180, null))
						// north (x270)
						.register(1, Direction.NORTH, v(h2, VariantSettings.Rotation.R270, null))
						.register(2, Direction.NORTH, v(h4, VariantSettings.Rotation.R270, null))
						.register(3, Direction.NORTH, v(h6, VariantSettings.Rotation.R270, null))
						.register(4, Direction.NORTH, v(h8, VariantSettings.Rotation.R270, null))
						.register(5, Direction.NORTH, v(h10, VariantSettings.Rotation.R270, null))
						.register(6, Direction.NORTH, v(h12, VariantSettings.Rotation.R270, null))
						.register(7, Direction.NORTH, v(h14, VariantSettings.Rotation.R270, null))
						.register(8, Direction.NORTH, v(fullBlock, VariantSettings.Rotation.R270, null))
						// west (x270, y270)
						.register(1, Direction.WEST, v(h2, VariantSettings.Rotation.R270, VariantSettings.Rotation.R270))
						.register(2, Direction.WEST, v(h4, VariantSettings.Rotation.R270, VariantSettings.Rotation.R270))
						.register(3, Direction.WEST, v(h6, VariantSettings.Rotation.R270, VariantSettings.Rotation.R270))
						.register(4, Direction.WEST, v(h8, VariantSettings.Rotation.R270, VariantSettings.Rotation.R270))
						.register(5, Direction.WEST, v(h10, VariantSettings.Rotation.R270, VariantSettings.Rotation.R270))
						.register(6, Direction.WEST, v(h12, VariantSettings.Rotation.R270, VariantSettings.Rotation.R270))
						.register(7, Direction.WEST, v(h14, VariantSettings.Rotation.R270, VariantSettings.Rotation.R270))
						.register(8, Direction.WEST, v(fullBlock, VariantSettings.Rotation.R270, VariantSettings.Rotation.R270))
						// south (x90)
						.register(1, Direction.SOUTH, v(h2, VariantSettings.Rotation.R90, null))
						.register(2, Direction.SOUTH, v(h4, VariantSettings.Rotation.R90, null))
						.register(3, Direction.SOUTH, v(h6, VariantSettings.Rotation.R90, null))
						.register(4, Direction.SOUTH, v(h8, VariantSettings.Rotation.R90, null))
						.register(5, Direction.SOUTH, v(h10, VariantSettings.Rotation.R90, null))
						.register(6, Direction.SOUTH, v(h12, VariantSettings.Rotation.R90, null))
						.register(7, Direction.SOUTH, v(h14, VariantSettings.Rotation.R90, null))
						.register(8, Direction.SOUTH, v(fullBlock, VariantSettings.Rotation.R90, null))
						// east (x270, y90)
						.register(1, Direction.EAST, v(h2, VariantSettings.Rotation.R270, VariantSettings.Rotation.R90))
						.register(2, Direction.EAST, v(h4, VariantSettings.Rotation.R270, VariantSettings.Rotation.R90))
						.register(3, Direction.EAST, v(h6, VariantSettings.Rotation.R270, VariantSettings.Rotation.R90))
						.register(4, Direction.EAST, v(h8, VariantSettings.Rotation.R270, VariantSettings.Rotation.R90))
						.register(5, Direction.EAST, v(h10, VariantSettings.Rotation.R270, VariantSettings.Rotation.R90))
						.register(6, Direction.EAST, v(h12, VariantSettings.Rotation.R270, VariantSettings.Rotation.R90))
						.register(7, Direction.EAST, v(h14, VariantSettings.Rotation.R270, VariantSettings.Rotation.R90))
						.register(8, Direction.EAST, v(fullBlock, VariantSettings.Rotation.R270, VariantSettings.Rotation.R90))
		);
	}
}
