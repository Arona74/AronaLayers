package io.arona74.aronalayers.datagen;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.block.Layer3DBlock;
import io.arona74.aronalayers.block.LayerBlock;
import io.arona74.aronalayers.block.ModBlocks;
import io.arona74.aronalayers.item.ModItems;
import io.arona74.aronalayers.registry.Args;
import io.arona74.aronalayers.registry.DatagenArgs;
import io.arona74.aronalayers.util.Textures;

import net.minecraft.client.color.item.Constant;
import net.minecraft.client.color.item.GrassColorSource;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import static io.arona74.aronalayers.util.ModBlockStateModelGenerator.registerLayer3DBlock;
import static io.arona74.aronalayers.util.ModBlockStateModelGenerator.registerLayer3DTemplates;
import static io.arona74.aronalayers.util.ModBlockStateModelGenerator.registerLayerBlock;
import static io.arona74.aronalayers.util.ModBlockStateModelGenerator.registerLayerBlockY15;

public class ModModelProvider extends FabricModelProvider {
	public ModModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators bsmg) {
		registerLayerBlock(ModBlocks.BROWN_MUSHROOM_LAYER, Identifier.fromNamespaceAndPath(AronaLayers.MOD_ID, "block/brown_mushroom_block"), bsmg, TextureMapping.cube(Blocks.BROWN_MUSHROOM_BLOCK));
		registerLayerBlock(ModBlocks.RED_MUSHROOM_LAYER, Identifier.fromNamespaceAndPath(AronaLayers.MOD_ID, "block/red_mushroom_block"), bsmg, TextureMapping.cube(Blocks.RED_MUSHROOM_BLOCK));

		BuiltInRegistries.BLOCK.stream().filter(AronaLayers::isNamespaced).forEach(block -> {
			if (Args.DATAGEN_ARGS.containsKey(block)) {
				DatagenArgs args = Args.DATAGEN_ARGS.get(block);
				Block fullBlock = args.fullTextureBlock != null ? args.fullTextureBlock : args.parentBlock;

				if (block instanceof LayerBlock && args.textureMap != null && fullBlock != null) {
					ItemTintSource tint = tint(args);

					if (args.y15 && tint != null) {
						registerLayerBlockY15(block, fullBlock, bsmg, textures(args.textureMap), tint);
					} else if (args.y15) {
						registerLayerBlockY15(block, fullBlock, bsmg, textures(args.textureMap));
					} else if (tint != null) {
						registerLayerBlock(block, fullBlock, bsmg, textures(args.textureMap), tint);
					} else {
						registerLayerBlock(block, fullBlock, bsmg, textures(args.textureMap));
					}
				}
			}
		});

		registerLayer3DTemplates(bsmg);
		BuiltInRegistries.BLOCK.stream().filter(block -> block instanceof Layer3DBlock).forEach(block -> {
			Layer3DBlock layer3D = (Layer3DBlock) block;
			DatagenArgs args = Args.DATAGEN_ARGS.get(layer3D.getBaseLayer());
			Block fullBlock = args.fullTextureBlock != null ? args.fullTextureBlock : args.parentBlock;
			registerLayer3DBlock(layer3D, ModelLocationUtils.getModelLocation(fullBlock), bsmg, textures(args.textureMap));
		});
	}

	// Item models carry their tint since 1.21.4.
	private static ItemTintSource tint(DatagenArgs args) {
		if (args.grassItemTint) {
			return new GrassColorSource();
		}

		return args.itemTint != null ? new Constant(args.itemTint) : null;
	}

	// Shared datagen args describe textures version-neutrally; this version's models take a TextureMapping.
	private static TextureMapping textures(Textures textures) {
		TextureMapping mapping = new TextureMapping();
		textures.slots().forEach((slot, id) -> mapping.put(switch (slot) {
			case "all" -> TextureSlot.ALL;
			case "top" -> TextureSlot.TOP;
			case "side" -> TextureSlot.SIDE;
			case "bottom" -> TextureSlot.BOTTOM;
			default -> throw new IllegalArgumentException("Unknown texture slot " + slot);
		}, Identifier.parse(id)));
		return mapping;
	}

	@Override
	public void generateItemModels(ItemModelGenerators img) {
		img.generateFlatItem(ModItems.TAB_ICON, ModelTemplates.FLAT_ITEM);
	}
}
