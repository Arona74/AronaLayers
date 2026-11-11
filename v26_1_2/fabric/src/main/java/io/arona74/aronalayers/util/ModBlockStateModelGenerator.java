package io.arona74.aronalayers.util;

import com.mojang.math.Quadrant;

import io.arona74.aronalayers.block.Layer3DBlock;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.*;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ModBlockStateModelGenerator {
	public static MultiVariant varOf(Identifier id) {
		return BlockModelGenerators.plainVariant(id);
	}

	public static void registerLayerBlock(Block layerBlock, Block fullBlock, BlockModelGenerators bsmg, TextureMapping textureMap, ItemTintSource tintSource) {
		Identifier id = ModModels.LAYER_2.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id2 = ModModels.LAYER_4.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id3 = ModModels.LAYER_6.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id4 = ModModels.LAYER_8.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id5 = ModModels.LAYER_10.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id6 = ModModels.LAYER_12.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id7 = ModModels.LAYER_14.create(layerBlock, textureMap, bsmg.modelOutput);
		bsmg.blockStateOutput.accept(createLayerBlockState(layerBlock, varOf(id), varOf(id2), varOf(id3), varOf(id4), varOf(id5), varOf(id6), varOf(id7), varOf(ModelLocationUtils.getModelLocation(fullBlock))));
		bsmg.registerSimpleTintedItemModel(layerBlock, id, tintSource);
	}

	public static void registerLayerBlockY15(Block layerBlock, Block fullBlock, BlockModelGenerators bsmg, TextureMapping textureMap, ItemTintSource tintSource) {
		Identifier id = ModModels.LAYER_1.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id2 = ModModels.LAYER_3.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id3 = ModModels.LAYER_5.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id4 = ModModels.LAYER_7.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id5 = ModModels.LAYER_9.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id6 = ModModels.LAYER_11.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id7 = ModModels.LAYER_13.create(layerBlock, textureMap, bsmg.modelOutput);
		bsmg.blockStateOutput.accept(createLayerBlockState(layerBlock, varOf(id), varOf(id2), varOf(id3), varOf(id4), varOf(id5), varOf(id6), varOf(id7), varOf(ModelLocationUtils.getModelLocation(fullBlock))));
		bsmg.registerSimpleTintedItemModel(layerBlock, id, tintSource);
	}

	public static void registerLayerBlock(Block layerBlock, Block fullBlock, BlockModelGenerators bsmg, TextureMapping textureMap) {
		Identifier id = ModModels.LAYER_2.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id2 = ModModels.LAYER_4.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id3 = ModModels.LAYER_6.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id4 = ModModels.LAYER_8.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id5 = ModModels.LAYER_10.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id6 = ModModels.LAYER_12.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id7 = ModModels.LAYER_14.create(layerBlock, textureMap, bsmg.modelOutput);
		bsmg.blockStateOutput.accept(createLayerBlockState(layerBlock, varOf(id), varOf(id2), varOf(id3), varOf(id4), varOf(id5), varOf(id6), varOf(id7), varOf(ModelLocationUtils.getModelLocation(fullBlock))));
		bsmg.registerSimpleItemModel(layerBlock, id);
	}

	public static void registerLayerBlock(Block layerBlock, Identifier fullBlock, BlockModelGenerators bsmg, TextureMapping textureMap) {
		Identifier id = ModModels.LAYER_2.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id2 = ModModels.LAYER_4.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id3 = ModModels.LAYER_6.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id4 = ModModels.LAYER_8.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id5 = ModModels.LAYER_10.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id6 = ModModels.LAYER_12.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id7 = ModModels.LAYER_14.create(layerBlock, textureMap, bsmg.modelOutput);
		bsmg.blockStateOutput.accept(createLayerBlockState(layerBlock, varOf(id), varOf(id2), varOf(id3), varOf(id4), varOf(id5), varOf(id6), varOf(id7), varOf(fullBlock)));
		bsmg.registerSimpleItemModel(layerBlock, id);
	}

	public static void registerLayerBlockY15(Block layerBlock, Block fullBlock, BlockModelGenerators bsmg, TextureMapping textureMap) {
		Identifier id = ModModels.LAYER_1.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id2 = ModModels.LAYER_3.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id3 = ModModels.LAYER_5.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id4 = ModModels.LAYER_7.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id5 = ModModels.LAYER_9.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id6 = ModModels.LAYER_11.create(layerBlock, textureMap, bsmg.modelOutput);
		Identifier id7 = ModModels.LAYER_13.create(layerBlock, textureMap, bsmg.modelOutput);
		bsmg.blockStateOutput.accept(createLayerBlockState(layerBlock, varOf(id), varOf(id2), varOf(id3), varOf(id4), varOf(id5), varOf(id6), varOf(id7), varOf(ModelLocationUtils.getModelLocation(fullBlock))));
		bsmg.registerSimpleItemModel(layerBlock, id);
	}

	public static BlockModelDefinitionGenerator createLayerBlockState(Block layerBlock, MultiVariant h2, MultiVariant h4, MultiVariant h6, MultiVariant h8, MultiVariant h10, MultiVariant h12, MultiVariant h14, MultiVariant fullBlock) {
		VariantMutator x90 = VariantMutator.X_ROT.withValue(Quadrant.R90);
		VariantMutator x180 = VariantMutator.X_ROT.withValue(Quadrant.R180);
		VariantMutator x270 = VariantMutator.X_ROT.withValue(Quadrant.R270);
		VariantMutator y90 = VariantMutator.Y_ROT.withValue(Quadrant.R90);
		VariantMutator y270 = VariantMutator.Y_ROT.withValue(Quadrant.R270);
		return MultiVariantGenerator.dispatch(layerBlock).with(PropertyDispatch.initial(BlockStateProperties.LAYERS, BlockStateProperties.FACING)
				//down
				.select(1, Direction.DOWN, h2)
				.select(2, Direction.DOWN, h4)
				.select(3, Direction.DOWN, h6)
				.select(4, Direction.DOWN, h8)
				.select(5, Direction.DOWN, h10)
				.select(6, Direction.DOWN, h12)
				.select(7, Direction.DOWN, h14)
				.select(8, Direction.DOWN, fullBlock)
				//up
				.select(1, Direction.UP, h2.with(x180))
				.select(2, Direction.UP, h4.with(x180))
				.select(3, Direction.UP, h6.with(x180))
				.select(4, Direction.UP, h8.with(x180))
				.select(5, Direction.UP, h10.with(x180))
				.select(6, Direction.UP, h12.with(x180))
				.select(7, Direction.UP, h14.with(x180))
				.select(8, Direction.UP, fullBlock.with(x180))
				//north
				.select(1, Direction.NORTH, h2.with(x270))
				.select(2, Direction.NORTH, h4.with(x270))
				.select(3, Direction.NORTH, h6.with(x270))
				.select(4, Direction.NORTH, h8.with(x270))
				.select(5, Direction.NORTH, h10.with(x270))
				.select(6, Direction.NORTH, h12.with(x270))
				.select(7, Direction.NORTH, h14.with(x270))
				.select(8, Direction.NORTH, fullBlock.with(x270))
				//west
				.select(1, Direction.WEST, h2.with(x270).with(y270))
				.select(2, Direction.WEST, h4.with(x270).with(y270))
				.select(3, Direction.WEST, h6.with(x270).with(y270))
				.select(4, Direction.WEST, h8.with(x270).with(y270))
				.select(5, Direction.WEST, h10.with(x270).with(y270))
				.select(6, Direction.WEST, h12.with(x270).with(y270))
				.select(7, Direction.WEST, h14.with(x270).with(y270))
				.select(8, Direction.WEST, fullBlock.with(x270).with(y270))
				//south
				.select(1, Direction.SOUTH, h2.with(x90))
				.select(2, Direction.SOUTH, h4.with(x90))
				.select(3, Direction.SOUTH, h6.with(x90))
				.select(4, Direction.SOUTH, h8.with(x90))
				.select(5, Direction.SOUTH, h10.with(x90))
				.select(6, Direction.SOUTH, h12.with(x90))
				.select(7, Direction.SOUTH, h14.with(x90))
				.select(8, Direction.SOUTH, fullBlock.with(x90))
				//east
				.select(1, Direction.EAST, h2.with(x270).with(y90))
				.select(2, Direction.EAST, h4.with(x270).with(y90))
				.select(3, Direction.EAST, h6.with(x270).with(y90))
				.select(4, Direction.EAST, h8.with(x270).with(y90))
				.select(5, Direction.EAST, h10.with(x270).with(y90))
				.select(6, Direction.EAST, h12.with(x270).with(y90))
				.select(7, Direction.EAST, h14.with(x270).with(y90))
				.select(8, Direction.EAST, fullBlock.with(x270).with(y90)));
	}

	/**
	 * Registers the models and blockstate of a 3D layer. Models are built for the north facing
	 * and rotated for the others. Full states (either value at 8) use {@code fullBlock}.
	 */
	public static void registerLayer3DBlock(Layer3DBlock block, Identifier fullBlock, BlockModelGenerators bsmg, TextureMapping textureMap) {
		Identifier[][] models = new Identifier[8][8];

		for (int layers = 0; layers <= Layer3DTemplates.MAX_LAYERS; layers++) {
			for (int width = 1; width <= Layer3DTemplates.MAX_LAYERS; width++) {
				models[layers][width] = ModModels.LAYER_3D[layers][width].create(block, textureMap, bsmg.modelOutput);
			}
		}

		VariantMutator uvLock = VariantMutator.UV_LOCK.withValue(true);
		bsmg.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(Layer3DBlock.HORIZONTAL_FACING, Layer3DBlock.VERTICAL_LAYERS, Layer3DBlock.HORIZONTAL_LAYERS)
						.generate((facing, layers, width) -> {
							if (layers == 8 || width == 8) {
								return varOf(fullBlock);
							}

							MultiVariant variant = varOf(models[layers][width]).with(uvLock);
							return switch (facing) {
								case EAST -> variant.with(VariantMutator.Y_ROT.withValue(Quadrant.R90));
								case SOUTH -> variant.with(VariantMutator.Y_ROT.withValue(Quadrant.R180));
								case WEST -> variant.with(VariantMutator.Y_ROT.withValue(Quadrant.R270));
								default -> variant;
							};
						})));
		bsmg.registerSimpleItemModel(block, models[4][4]);
	}

	/** Writes the 3D layer template models, see {@link Layer3DTemplates}. */
	public static void registerLayer3DTemplates(BlockModelGenerators bsmg) {
		for (int layers = 0; layers <= Layer3DTemplates.MAX_LAYERS; layers++) {
			for (int width = 1; width <= Layer3DTemplates.MAX_LAYERS; width++) {
				int vertical = layers;
				int horizontal = width;
				bsmg.modelOutput.accept(ModModels.layer3DTemplateId(layers, width), () -> Layer3DTemplates.template(vertical, horizontal));
			}
		}
	}
}
