package io.arona74.aronalayers.compat;

import java.util.Map;
import java.util.function.Function;

import io.arona74.aronalayers.AronaLayers;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.impl.content.registry.util.ImmutableCollectionUtils;
import net.fabricmc.fabric.mixin.content.registry.AxeItemAccessor;
import net.fabricmc.fabric.mixin.content.registry.ShovelItemAccessor;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Minecraft 1.21.1 implementation of the version helpers used by the shared code.
 * Every version subproject has a class with this name and these methods, so shared code
 * never names version-specific types such as ResourceLocation.
 */
public final class Compat {
	private Compat() {
	}

	private static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(AronaLayers.MOD_ID, path);
	}

	/** Whether this Minecraft version has the vanilla block with this id. */
	public static boolean hasVanillaBlock(String path) {
		return BuiltInRegistries.BLOCK.containsKey(ResourceLocation.withDefaultNamespace(path));
	}

	/** The vanilla block with this id. Fails if this Minecraft version doesn't have it. */
	public static Block vanillaBlock(String path) {
		if (!hasVanillaBlock(path)) {
			throw new IllegalArgumentException("No vanilla block minecraft:" + path);
		}

		return BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(path));
	}

	/** A full copy of a vanilla block's properties. */
	public static BlockBehaviour.Properties copyOf(String vanillaPath) {
		return BlockBehaviour.Properties.ofFullCopy(vanillaBlock(vanillaPath));
	}

	/** Registers a mod block and its block item. */
	public static <T extends Block> T registerBlock(String name, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties properties) {
		T block = factory.apply(properties);
		Registry.register(BuiltInRegistries.ITEM, id(name), new BlockItem(block, new Item.Properties()));
		return Registry.register(BuiltInRegistries.BLOCK, id(name), block);
	}

	public static Item registerItem(String name, Function<Item.Properties, Item> factory) {
		return Registry.register(BuiltInRegistries.ITEM, id(name), factory.apply(new Item.Properties()));
	}

	public static CreativeModeTab.Builder tabBuilder() {
		return FabricItemGroup.builder();
	}

	public static CreativeModeTab registerTab(String name, CreativeModeTab tab) {
		return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id(name), tab);
	}

	public static TagKey<Block> blockTag(String name) {
		return TagKey.create(Registries.BLOCK, id(name));
	}

	/** Registers a built-in data pack shipped in the mod jar, enabled by default. */
	public static void registerBuiltinPack(String name, Component title) {
		FabricLoader.getInstance().getModContainer(AronaLayers.MOD_ID).ifPresent(container ->
				ResourceManagerHelper.registerBuiltinResourcePack(id(name), container, title, ResourcePackActivationType.DEFAULT_ENABLED));
	}

	/** Registers a fuel, with its burn time in ticks at the default smelt time of 200 ticks. */
	public static void registerFuel(Block block, int burnTicks) {
		FuelRegistry.INSTANCE.add(block, burnTicks);
	}

	public static boolean causeFallDamage(Level level, Entity entity, double fallDistance, float multiplier) {
		return entity.causeFallDamage((float) fallDistance, multiplier, level.damageSources().fall());
	}

	public static int minY(Level level) {
		return level.getMinBuildHeight();
	}

	public static boolean isBoat(Entity entity) {
		return entity instanceof Boat;
	}

	public static boolean isMinecart(Entity entity) {
		return entity instanceof AbstractMinecart;
	}

	/** Whether shape {@code a} fully hides shape {@code b} on side {@code direction}. */
	public static boolean occludes(VoxelShape a, VoxelShape b, Direction direction) {
		return Shapes.blockOccudes(a, b, direction);
	}

	/** Whether water evaporates here, like in the Nether. */
	public static boolean waterEvaporates(Level level, BlockPos pos) {
		return level.dimensionType().ultraWarm();
	}

	public static int lightBlock(BlockState state, BlockGetter level, BlockPos pos) {
		return state.getLightBlock(level, pos);
	}

	public static boolean isSolidRender(BlockState state, BlockGetter level, BlockPos pos) {
		return state.isSolidRender(level, pos);
	}

	/** Tells the block at {@code pos} that a neighbor changed, with {@code block} as the source. */
	public static void notifyNeighborChanged(Level level, BlockPos pos, Block block) {
		level.neighborChanged(pos, block, pos);
	}

	/** Fabric's mutable copy of the axe stripping map. */
	@SuppressWarnings("unstable")
	public static Map<Block, Block> strippables() {
		return ImmutableCollectionUtils.getAsMutableMap(AxeItemAccessor::getStrippedBlocks, AxeItemAccessor::setStrippedBlocks);
	}

	/** The shovel flattening map, made mutable by Fabric. */
	public static Map<Block, BlockState> flattenables() {
		return ShovelItemAccessor.getPathStates();
	}

	public static void registerOxidizable(Block less, Block more) {
		OxidizableBlocksRegistry.registerOxidizableBlockPair(less, more);
	}

	public static void registerWaxable(Block unwaxed, Block waxed) {
		OxidizableBlocksRegistry.registerWaxableBlockPair(unwaxed, waxed);
	}

	public static void triggerHoneySlide(ServerPlayer player, BlockState state) {
		CriteriaTriggers.HONEY_BLOCK_SLIDE.trigger(player, state);
	}
}
