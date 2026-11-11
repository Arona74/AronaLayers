package io.arona74.aronalayers.item;

import java.util.function.Function;

import io.arona74.aronalayers.compat.Compat;
import io.arona74.aronalayers.AronaLayers;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class ModItems {
	public static final Item TAB_ICON = registerItem("tab_icon", Item::new);

	private static Item registerItem(String name, Function<Item.Properties, Item> function) {
		return Compat.registerItem(name, function);
	}


	public static void registerModItems() {
	}
}
