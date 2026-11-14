package io.arona74.aronalayers.item;

import java.util.function.Function;

import io.arona74.aronalayers.compat.Compat;
import io.arona74.aronalayers.AronaLayers;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class ModItems {
	public static final Item TAB_ICON = registerItem("tab_icon", Item::new);

	// Ore fragments dropped by ore layers mined without Silk Touch, one layer's worth each (see
	// gameplay.ore.OreLayerDrops). Eight craft into the ore's normal drop. Only ores whose normal
	// drop is a single item need one: redstone, lapis, nether gold and quartz layers drop a share
	// of the ore's small vanilla items. Registered whatever the config says: item registries are
	// synced to clients.
	public static final Item SMALL_COAL = registerItem("small_coal", Item::new);
	public static final Item SMALL_RAW_IRON = registerItem("small_raw_iron", Item::new);
	public static final Item SMALL_RAW_COPPER = registerItem("small_raw_copper", Item::new);
	public static final Item SMALL_RAW_GOLD = registerItem("small_raw_gold", Item::new);
	public static final Item SMALL_DIAMOND = registerItem("small_diamond", Item::new);
	public static final Item SMALL_EMERALD = registerItem("small_emerald", Item::new);

	private static Item registerItem(String name, Function<Item.Properties, Item> function) {
		return Compat.registerItem(name, function);
	}


	public static void registerModItems() {
	}
}
