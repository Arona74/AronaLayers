package net.fellter.vanillalayerplus.datagen;

import java.util.concurrent.CompletableFuture;

import net.fellter.vanillalayerplus.VanillaLayerPlus;
import net.fellter.vanillalayerplus.block.Layer3DBlock;
import net.fellter.vanillalayerplus.block.LayerBlock;
import net.fellter.vanillalayerplus.registry.Args;
import net.fellter.vanillalayerplus.registry.DatagenArgs;

import net.minecraft.block.Block;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.property.Properties;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
	public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {
		Registries.BLOCK.stream().filter(VanillaLayerPlus::isNamespaced).forEach(block -> {
			if (Args.DATAGEN_ARGS.containsKey(block)) {
				DatagenArgs datagenArgs = Args.DATAGEN_ARGS.get(block);

				if (block instanceof LayerBlock && datagenArgs.needsSilkTouch) {
					addDrop(block, silkTouchDrops(block));
				} else if (block instanceof LayerBlock) {
					addDrop(block, layerDrops(block));
				}
			} else if (block instanceof Layer3DBlock layer3D && Args.DATAGEN_ARGS.containsKey(layer3D.getBaseLayer())) {
				addDrop(block, layer3DDrops(layer3D, Args.DATAGEN_ARGS.get(layer3D.getBaseLayer()).needsSilkTouch));
			}
		});
	}

	// A 3D layer drops its base layer item, as many as its volume in layers, rounded up. In
	// 1/64ths of a block, the floor is 8 * vertical and the wall above it horizontal * (8 - vertical).
	private LootTable.Builder layer3DDrops(Layer3DBlock block, boolean needsSilkTouch) {
		LeafEntry.Builder<?> entry = ItemEntry.builder(block.getBaseLayer());

		for (int layers = 0; layers <= 8; layers++) {
			for (int horizontalLayers = 1; horizontalLayers <= 8; horizontalLayers++) {
				int volume = 8 * layers + horizontalLayers * (8 - layers);
				int count = (volume + 7) / 8;
				entry.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(count))
						.conditionally(BlockStatePropertyLootCondition.builder(block)
								.properties(net.minecraft.predicate.StatePredicate.Builder.create()
										.exactMatch(Layer3DBlock.VERTICAL_LAYERS, layers)
										.exactMatch(Layer3DBlock.HORIZONTAL_LAYERS, horizontalLayers))));
			}
		}

		LootPool.Builder pool = LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F));

		if (needsSilkTouch) {
			return LootTable.builder().pool(pool.conditionally(this.createSilkTouchCondition()).with(entry));
		}

		return LootTable.builder().pool(pool.with(this.applyExplosionDecay(block, entry)));
	}

	private LootTable.Builder silkTouchDrops(Block drop) {
		return LootTable.builder().pool(LootPool.builder()
				.conditionally(this.createSilkTouchCondition()).rolls(ConstantLootNumberProvider.create(1.0F)).with(layerDropsEntry(drop)));
	}

	private LeafEntry.Builder<?> layerDropsEntry(Block drop) {
		return ItemEntry.builder(drop)
				.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))
						.conditionally(BlockStatePropertyLootCondition.builder(drop)
								.properties(net.minecraft.predicate.StatePredicate.Builder.create().exactMatch(Properties.LAYERS, 1))))
				.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F))
						.conditionally(BlockStatePropertyLootCondition.builder(drop)
								.properties(net.minecraft.predicate.StatePredicate.Builder.create().exactMatch(Properties.LAYERS, 2))))
				.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(3.0F))
						.conditionally(BlockStatePropertyLootCondition.builder(drop)
								.properties(net.minecraft.predicate.StatePredicate.Builder.create().exactMatch(Properties.LAYERS, 3))))
				.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(4.0F))
						.conditionally(BlockStatePropertyLootCondition.builder(drop)
								.properties(net.minecraft.predicate.StatePredicate.Builder.create().exactMatch(Properties.LAYERS, 4))))
				.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(5.0F))
						.conditionally(BlockStatePropertyLootCondition.builder(drop)
								.properties(net.minecraft.predicate.StatePredicate.Builder.create().exactMatch(Properties.LAYERS, 5))))
				.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(6.0F))
						.conditionally(BlockStatePropertyLootCondition.builder(drop)
								.properties(net.minecraft.predicate.StatePredicate.Builder.create().exactMatch(Properties.LAYERS, 6))))
				.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(7.0F))
						.conditionally(BlockStatePropertyLootCondition.builder(drop)
								.properties(net.minecraft.predicate.StatePredicate.Builder.create().exactMatch(Properties.LAYERS, 7))))
				.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(8.0F))
						.conditionally(BlockStatePropertyLootCondition.builder(drop)
								.properties(net.minecraft.predicate.StatePredicate.Builder.create().exactMatch(Properties.LAYERS, 8))));
	}

	public LootTable.Builder layerDrops(Block drop) {
		return LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
				.with(this.applyExplosionDecay(drop, layerDropsEntry(drop))));
	}
}
