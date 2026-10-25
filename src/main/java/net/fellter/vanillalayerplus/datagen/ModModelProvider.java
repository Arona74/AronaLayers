package net.fellter.vanillalayerplus.datagen;

import net.fellter.vanillalayerplus.VanillaLayerPlus;
import net.fellter.vanillalayerplus.block.Layer3DBlock;
import net.fellter.vanillalayerplus.block.LayerBlock;
import net.fellter.vanillalayerplus.block.ModBlocks;
import net.fellter.vanillalayerplus.item.ModItems;
import net.fellter.vanillalayerplus.registry.Args;
import net.fellter.vanillalayerplus.registry.DatagenArgs;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureMap;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;

import static net.fellter.vanillalayerplus.util.ModBlockStateModelGenerator.registerLayer3DBlock;
import static net.fellter.vanillalayerplus.util.ModBlockStateModelGenerator.registerLayer3DTemplates;
import static net.fellter.vanillalayerplus.util.ModBlockStateModelGenerator.registerLayerBlock;
import static net.fellter.vanillalayerplus.util.ModBlockStateModelGenerator.registerLayerBlockY15;

public class ModModelProvider extends FabricModelProvider {
	public ModModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator bsmg) {
		registerLayerBlock(ModBlocks.BROWN_MUSHROOM_LAYER, Identifier.of(VanillaLayerPlus.MOD_ID, "block/brown_mushroom_block"), bsmg, TextureMap.all(Blocks.BROWN_MUSHROOM_BLOCK));
		registerLayerBlock(ModBlocks.RED_MUSHROOM_LAYER, Identifier.of(VanillaLayerPlus.MOD_ID, "block/red_mushroom_block"), bsmg, TextureMap.all(Blocks.RED_MUSHROOM_BLOCK));

		Registries.BLOCK.stream().filter(VanillaLayerPlus::isNamespaced).forEach(block -> {
			if (Args.DATAGEN_ARGS.containsKey(block)) {
				DatagenArgs args = Args.DATAGEN_ARGS.get(block);

				if (args.textureMap != null) {
					if (block instanceof LayerBlock && args.y15 && args.fullTextureBlock != null) {
						registerLayerBlockY15(block, args.fullTextureBlock, bsmg, args.textureMap);
					} else if (block instanceof LayerBlock && args.y15 && args.parentBlock != null) {
						registerLayerBlockY15(block, args.parentBlock, bsmg, args.textureMap);
					} else if (block instanceof LayerBlock && args.fullTextureBlock != null) {
						registerLayerBlock(block, args.fullTextureBlock, bsmg, args.textureMap);
					} else if (block instanceof LayerBlock && args.parentBlock != null) {
						registerLayerBlock(block, args.parentBlock, bsmg, args.textureMap);
					}
				}
			}
		});

		registerLayer3DTemplates(bsmg);
		Registries.BLOCK.stream().filter(block -> block instanceof Layer3DBlock).forEach(block -> {
			Layer3DBlock layer3D = (Layer3DBlock) block;
			DatagenArgs args = Args.DATAGEN_ARGS.get(layer3D.getBaseLayer());
			Block fullBlock = args.fullTextureBlock != null ? args.fullTextureBlock : args.parentBlock;
			registerLayer3DBlock(layer3D, ModelIds.getBlockModelId(fullBlock), bsmg, args.textureMap);
		});
	}

	@Override
	public void generateItemModels(ItemModelGenerator img) {
		img.register(ModItems.VLP_TITLE, Models.GENERATED);
	}
}
