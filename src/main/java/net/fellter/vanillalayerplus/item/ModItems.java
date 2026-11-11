package net.fellter.vanillalayerplus.item;

import java.util.function.Function;

import net.fellter.vanillalayerplus.VanillaLayerPlus;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItems {
	public static final Item VLP_TITLE = registerItem("vlp_title", Item::new);

	private static Item registerItem(String name, Function<Item.Properties, Item> function) {
		return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(VanillaLayerPlus.MOD_ID, name), function.apply(new Item.Properties()));
	}


	public static void registerModItems() {
	}
}
