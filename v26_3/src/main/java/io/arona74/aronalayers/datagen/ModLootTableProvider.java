package io.arona74.aronalayers.datagen;

import java.util.concurrent.CompletableFuture;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.block.Layer3DBlock;
import io.arona74.aronalayers.block.LayerBlock;
import io.arona74.aronalayers.registry.Args;
import io.arona74.aronalayers.registry.DatagenArgs;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.UniformContainerBase;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;

public class ModLootTableProvider extends FabricBlockLootSubProvider {
	public ModLootTableProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
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
	}

	// A 3D layer drops its base layer item, as many as its volume in layers, rounded up. In
	// 1/64ths of a block, the floor is 8 * vertical and the wall above it horizontal * (8 - vertical).
	private LootTable.Builder layer3DDrops(Layer3DBlock block, boolean needsSilkTouch) {
		UniformContainerBase.Builder<?> entry = LootItem.lootTableItem(block.getBaseLayer());

		for (int layers = 0; layers <= 8; layers++) {
			for (int horizontalLayers = 1; horizontalLayers <= 8; horizontalLayers++) {
				int volume = 8 * layers + horizontalLayers * (8 - layers);
				int count = (volume + 7) / 8;
				entry.apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(count))
						.when(MatchBlock.blockMatches(this.blocks, block, StatePropertiesPredicate.Builder.properties()
								.hasProperty(Layer3DBlock.VERTICAL_LAYERS, layers)
								.hasProperty(Layer3DBlock.HORIZONTAL_LAYERS, horizontalLayers))));
			}
		}

		LootPool.Builder pool = LootPool.lootPool().setRolls(ContextIntProviders.exactly(1));

		if (needsSilkTouch) {
			return LootTable.lootTable().withPool(pool.when(this.hasSilkTouch()).add(entry));
		}

		return LootTable.lootTable().withPool(pool.add((LootPoolEntryContainer.Builder<?>) this.applyExplosionDecay(block, entry)));
	}

	private LootTable.Builder silkTouchDrops(Block drop) {
		return LootTable.lootTable().withPool(LootPool.lootPool()
				.when(this.hasSilkTouch()).setRolls(ContextIntProviders.exactly(1)).add(layerDropsEntry(drop)));
	}

	private UniformContainerBase.Builder<?> layerDropsEntry(Block drop) {
		return LootItem.lootTableItem(drop)
				.apply(LayerBlock.LAYERS.getPossibleValues(), layers -> SetItemCountFunction.setCount(ContextIntProviders.exactly(layers))
						.when(MatchBlock.blockMatches(this.blocks, drop, StatePropertiesPredicate.Builder.properties().hasProperty(LayerBlock.LAYERS, layers.intValue()))));
	}

	public LootTable.Builder layerDrops(Block drop) {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
				.add((LootPoolEntryContainer.Builder<?>) this.applyExplosionDecay(drop, layerDropsEntry(drop))));
	}
}
