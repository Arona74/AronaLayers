package io.arona74.aronalayers.compat;

import java.util.Map;
import java.util.function.Function;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.registry.RegistryArgs;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.fabric.api.registry.TillableBlockRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.impl.content.registry.util.ImmutableCollectionUtils;
import net.fabricmc.fabric.mixin.content.registry.AxeItemAccessor;
import net.fabricmc.fabric.mixin.content.registry.ShovelItemAccessor;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Minecraft 1.20.1 implementation of the version helpers used by the shared code.
 * Every version subproject has a class with this name and these methods, so shared code
 * never names version-specific types such as ResourceLocation.
 */
public final class Compat {
	private Compat() {
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(AronaLayers.MOD_ID, path);
	}

	/** Whether this Minecraft version has the vanilla block with this id. */
	public static boolean hasVanillaBlock(String path) {
		return BuiltInRegistries.BLOCK.containsKey(new ResourceLocation(path));
	}

	/** The vanilla block with this id. Fails if this Minecraft version doesn't have it. */
	public static Block vanillaBlock(String path) {
		if (!hasVanillaBlock(path)) {
			throw new IllegalArgumentException("No vanilla block minecraft:" + path);
		}

		return BuiltInRegistries.BLOCK.get(new ResourceLocation(path));
	}

	/** A full copy of a vanilla block's properties. */
	public static BlockBehaviour.Properties copyOf(String vanillaPath) {
		return BlockBehaviour.Properties.copy(vanillaBlock(vanillaPath));
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

	/**
	 * Registers a layer as fuel, burning 1/8 as long as its parent. Blast furnaces and smokers
	 * halve every burn time themselves, so the ratio holds there too.
	 */
	public static void registerLayerFuel(Block block, RegistryArgs.Fuel parent) {
		int burnTicks = parent.burnTicks() / 8;
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

	/** Makes the axe strip {@code input} into {@code stripped}. A mixin copies the layer properties over. */
	@SuppressWarnings("unstable")
	public static void registerStripping(Block input, Block stripped) {
		Map<Block, Block> strippables = ImmutableCollectionUtils.getAsMutableMap(AxeItemAccessor::getStrippedBlocks, AxeItemAccessor::setStrippedBlocks);
		strippables.put(input, stripped);
	}

	/** Makes the shovel flatten {@code input} into {@code flattened}. A mixin copies the layer properties over. */
	public static void registerFlattening(Block input, Block flattened) {
		ShovelItemAccessor.getPathStates().put(input, flattened.defaultBlockState());
	}

	/** Makes the hoe till {@code input} into {@code tilled} when there is air above. */
	public static void registerTilling(Block input, Block tilled) {
		TillableBlockRegistry.register(input, HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(tilled.defaultBlockState()));
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

	/** Whether breaking ice on top of this block leaves water, like vanilla ice. */
	public static boolean iceMeltsAbove(BlockState below) {
		return below.blocksMotion();
	}

	public static void spawnParticleBelow(Level level, BlockPos pos, RandomSource random, ParticleOptions particle) {
		ParticleUtils.spawnParticleBelow(level, pos, random, particle);
	}

	/** Whether mining ice with this tool keeps it from melting (silk touch). */
	public static boolean preventsIceMelting(ItemStack tool) {
		return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0;
	}

	/** Random tick of an oxidizing block: may move it to its next oxidation stage. */
	public static void changeOverTime(WeatheringCopper block, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		block.onRandomTick(state, level, pos, random);
	}

	public static void playSpongeAbsorb(Level level, BlockPos pos) {
		// The sponge absorb sound doesn't exist yet: absorbing is silent, like vanilla's sponge.
	}

	/** The wet sponge dries sound doesn't exist yet; vanilla's wet sponge uses fire extinguish. */
	public static void playWetSpongeDries(Level level, BlockPos pos) {
		level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, (1.0F + level.getRandom().nextFloat() * 0.2F) * 0.7F);
	}

	/** Takes the fluid out of a fluid block or waterlogged block, as a sponge does. */
	public static ItemStack pickupFluid(BucketPickup block, LevelAccessor level, BlockPos pos, BlockState state) {
		return block.pickupBlock(level, pos, state);
	}
}
