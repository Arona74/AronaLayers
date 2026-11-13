package io.arona74.aronalayers.datagen;

import java.util.concurrent.CompletableFuture;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.block.Layer3DBlock;
import io.arona74.aronalayers.block.LayerBlock;
import io.arona74.aronalayers.block.ModBlocks;
import io.arona74.aronalayers.registry.Args;
import io.arona74.aronalayers.registry.DatagenArgs;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
	public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {
		BuiltInRegistries.BLOCK.stream().filter(AronaLayers::isNamespaced).forEach(block -> {
			if (Args.DATAGEN_ARGS.containsKey(block)) {
				DatagenArgs datagenArgs = Args.DATAGEN_ARGS.get(block);

				if (block instanceof LayerBlock && block.getLootTable().isPresent() && datagenArgs.needsSilkTouch) {
					add(block, silkTouchDrops(block));
				} else if (block instanceof LayerBlock && block.getLootTable().isPresent()) {
					add(block, layerDrops(block));
				}
			} else if (block instanceof Layer3DBlock layer3D && Args.DATAGEN_ARGS.containsKey(layer3D.getBaseLayer())) {
				add(block, layer3DDrops(layer3D, Args.DATAGEN_ARGS.get(layer3D.getBaseLayer()).needsSilkTouch));
			}
		});

		// The snow-style layers: a zinc ore layer drops itself once whatever its height, a powder
		// snow layer drops nothing, like powder snow.
		for (Block block : new Block[] {ModBlocks.ZINC_ORE_LAYER, ModBlocks.DEEPSLATE_ZINC_ORE_LAYER}) {
			add(block, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block))));
		}

		add(ModBlocks.POWDER_SNOW_LAYER, LootTable.lootTable());
	}

	// A 3D layer drops its base layer item, as many as its volume in layers, rounded up. In
	// 1/64ths of a block, the floor is 8 * vertical and the wall above it horizontal * (8 - vertical).
	private LootTable.Builder layer3DDrops(Layer3DBlock block, boolean needsSilkTouch) {
		LootPoolSingletonContainer.Builder<?> entry = LootItem.lootTableItem(block.getBaseLayer());

		for (int layers = 0; layers <= 8; layers++) {
			for (int horizontalLayers = 1; horizontalLayers <= 8; horizontalLayers++) {
				int volume = 8 * layers + horizontalLayers * (8 - layers);
				int count = (volume + 7) / 8;
				entry.apply(SetItemCountFunction.setCount(ConstantValue.exactly(count))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties()
										.hasProperty(Layer3DBlock.VERTICAL_LAYERS, layers)
										.hasProperty(Layer3DBlock.HORIZONTAL_LAYERS, horizontalLayers))));
			}
		}

		LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F));

		if (needsSilkTouch) {
			return LootTable.lootTable().withPool(pool.when(this.hasSilkTouch()).add(entry));
		}

		return LootTable.lootTable().withPool(pool.add(this.applyExplosionDecay(block, entry)));
	}

	private LootTable.Builder silkTouchDrops(Block drop) {
		return LootTable.lootTable().withPool(LootPool.lootPool()
				.when(this.hasSilkTouch()).setRolls(ConstantValue.exactly(1.0F)).add(layerDropsEntry(drop)));
	}

	private LootPoolSingletonContainer.Builder<?> layerDropsEntry(Block drop) {
		return LootItem.lootTableItem(drop)
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(drop)
								.setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.LAYERS, 1))))
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(drop)
								.setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.LAYERS, 2))))
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(drop)
								.setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.LAYERS, 3))))
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(drop)
								.setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.LAYERS, 4))))
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(5.0F))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(drop)
								.setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.LAYERS, 5))))
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(6.0F))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(drop)
								.setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.LAYERS, 6))))
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(7.0F))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(drop)
								.setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.LAYERS, 7))))
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(8.0F))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(drop)
								.setProperties(net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.LAYERS, 8))));
	}

	public LootTable.Builder layerDrops(Block drop) {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(this.applyExplosionDecay(drop, layerDropsEntry(drop))));
	}
}
