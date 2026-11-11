package io.arona74.aronalayers.compat;

import java.util.function.Function;

import io.arona74.aronalayers.AronaLayers;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
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

/**
 * Minecraft 1.21.11 implementation of the version helpers used by the shared code.
 * Every version subproject has a class with this name and these methods, so shared code
 * never names version-specific types such as Identifier.
 */
public final class Compat {
	private Compat() {
	}

	private static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(AronaLayers.MOD_ID, path);
	}

	/** Whether this Minecraft version has the vanilla block with this id. */
	public static boolean hasVanillaBlock(String path) {
		return BuiltInRegistries.BLOCK.containsKey(Identifier.withDefaultNamespace(path));
	}

	/** The vanilla block with this id. Fails if this Minecraft version doesn't have it. */
	public static Block vanillaBlock(String path) {
		if (!hasVanillaBlock(path)) {
			throw new IllegalArgumentException("No vanilla block minecraft:" + path);
		}

		return BuiltInRegistries.BLOCK.getValue(Identifier.withDefaultNamespace(path));
	}

	/** A full copy of a vanilla block's properties. */
	public static BlockBehaviour.Properties copyOf(String vanillaPath) {
		return BlockBehaviour.Properties.ofFullCopy(vanillaBlock(vanillaPath));
	}

	/** Registers a mod block and its block item. Since 1.21.2 the properties must carry the registry key. */
	public static <T extends Block> T registerBlock(String name, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties properties) {
		T block = factory.apply(properties.setId(ResourceKey.create(Registries.BLOCK, id(name))));
		Registry.register(BuiltInRegistries.ITEM, id(name), new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, id(name)))));
		return Registry.register(BuiltInRegistries.BLOCK, id(name), block);
	}

	public static Item registerItem(String name, Function<Item.Properties, Item> factory) {
		return Registry.register(BuiltInRegistries.ITEM, id(name), factory.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id(name)))));
	}

	public static CreativeModeTab registerTab(String name, CreativeModeTab tab) {
		return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id(name), tab);
	}

	public static TagKey<Block> blockTag(String name) {
		return TagKey.create(Registries.BLOCK, id(name));
	}

	public static boolean causeFallDamage(Level level, Entity entity, double fallDistance, float multiplier) {
		return entity.causeFallDamage(fallDistance, multiplier, level.damageSources().fall());
	}

	public static int minY(Level level) {
		return level.getMinY();
	}

	public static boolean isBoat(Entity entity) {
		return entity instanceof AbstractBoat;
	}

	public static boolean isMinecart(Entity entity) {
		return entity instanceof AbstractMinecart;
	}

	/** Whether shape {@code a} fully hides shape {@code b} on side {@code direction}. */
	public static boolean occludes(VoxelShape a, VoxelShape b, Direction direction) {
		return Shapes.blockOccludes(a, b, direction);
	}

	/** Whether water evaporates here, like in the Nether. */
	public static boolean waterEvaporates(Level level, BlockPos pos) {
		return level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, pos);
	}

	public static int lightBlock(BlockState state, BlockGetter level, BlockPos pos) {
		return state.getLightBlock();
	}

	public static boolean isSolidRender(BlockState state, BlockGetter level, BlockPos pos) {
		return state.isSolidRender();
	}

	/** Tells the block at {@code pos} that a neighbor changed, with {@code block} as the source. */
	public static void notifyNeighborChanged(Level level, BlockPos pos, Block block) {
		level.neighborChanged(pos, block, null);
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
		return EnchantmentHelper.hasTag(tool, EnchantmentTags.PREVENTS_ICE_MELTING);
	}

	/** Random tick of an oxidizing block: may move it to its next oxidation stage. */
	public static void changeOverTime(WeatheringCopper block, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		block.changeOverTime(state, level, pos, random);
	}

	public static void playSpongeAbsorb(Level level, BlockPos pos) {
		level.playSound(null, pos, SoundEvents.SPONGE_ABSORB, SoundSource.BLOCKS, 1.0F, 1.0F);
	}

	public static void playWetSpongeDries(Level level, BlockPos pos) {
		level.playSound(null, pos, SoundEvents.WET_SPONGE_DRIES, SoundSource.BLOCKS, 1.0F, (1.0F + level.getRandom().nextFloat() * 0.2F) * 0.7F);
	}

	/** Takes the fluid out of a fluid block or waterlogged block, as a sponge does. */
	public static ItemStack pickupFluid(BucketPickup block, LevelAccessor level, BlockPos pos, BlockState state) {
		return block.pickupBlock(null, level, pos, state);
	}
}
