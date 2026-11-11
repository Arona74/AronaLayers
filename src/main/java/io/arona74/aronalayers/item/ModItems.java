package io.arona74.aronalayers.item;

import java.util.function.Function;

import io.arona74.aronalayers.AronaLayers;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItems {
	public static final Item VLP_TITLE = registerItem("vlp_title", Item::new);

	private static Item registerItem(String name, Function<Item.Properties, Item> function) {
		return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(AronaLayers.MOD_ID, name), function.apply(new Item.Properties()));
	}


	public static void registerModItems() {
	}
}
