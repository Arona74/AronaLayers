package io.arona74.aronalayers.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.arona74.aronalayers.AronaLayers;

/**
 * The 3D layer template models, as JSON, the same for every Minecraft version: a floor layer
 * {@code vertical layers} high merged with a wall layer {@code horizontal layers} thick against
 * the north side. Each version's datagen writes them and builds the per-block models on them.
 * Both values only go up to 7: if either is 8 a 3D layer is a full cube and uses the full
 * block's model.
 */
public final class Layer3DTemplates {
	public static final int MAX_LAYERS = 7;

	private Layer3DTemplates() {
	}

	public static String suffix(int verticalLayers, int horizontalLayers) {
		return "_height_" + verticalLayers * 2 + "_width_" + horizontalLayers * 2;
	}

	/** Path of a template model in this mod's namespace, under models/. */
	public static String path(int verticalLayers, int horizontalLayers) {
		return "blockmodels/layer_3d/layer" + suffix(verticalLayers, horizontalLayers);
	}

	public static String id(int verticalLayers, int horizontalLayers) {
		return AronaLayers.MOD_ID + ":" + path(verticalLayers, horizontalLayers);
	}

	// Two elements that don't overlap: the wall (full height, d deep) and the floor in front of it
	// (h high, from d to the south edge). The floor has no north face, it would be inside the wall.
	// A pure wall layer (h = 0) has no floor element. UVs match the ones Minecraft derives from the
	// element bounds, so textures line up with full blocks.
	public static JsonObject template(int verticalLayers, int horizontalLayers) {
		int h = verticalLayers * 2;
		int d = horizontalLayers * 2;
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
}
