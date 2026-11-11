package io.arona74.aronalayers.datagen;

import java.util.concurrent.CompletableFuture;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.block.LayerBlock;
import io.arona74.aronalayers.registry.Args;
import io.arona74.aronalayers.registry.DatagenArgs;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.ItemLike;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

public class ModRecipeProvider extends FabricRecipeProvider {
	public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
		return new RecipeProvider(registryLookup, exporter) {
			public RecipeBuilder layerBlockRecipe(ItemLike output, ItemLike input) {
				return ShapedRecipeBuilder.shaped(registries.lookupOrThrow(Registries.ITEM), RecipeCategory.DECORATIONS, output, 16)
						.define('W', input)
						.pattern(" W")
						.pattern("W ")
						.unlockedBy(getHasName(input), has(input))
						.showNotification(true);
			}

			public void offerStonecuttingRecipe(ItemLike output, ItemLike input) {
				this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, output, input, 8);
			}

			@Override
			public void buildRecipes() {
				BuiltInRegistries.BLOCK.stream().filter(AronaLayers::isNamespaced).forEach(block -> {
					if (Args.DATAGEN_ARGS.containsKey(block)) {
						DatagenArgs args = Args.DATAGEN_ARGS.get(block);

						if (args.parentBlock != null) {
							if (block instanceof LayerBlock) {
								layerBlockRecipe(block, args.parentBlock).save(output);
							}

							if (block instanceof LayerBlock && args.stonecuttingInput != null) {
								for (ItemLike itemConvertible : args.stonecuttingInput) {
									offerStonecuttingRecipe(block, itemConvertible);
								}
							}
						}
					}
				});
			}
		};
	}

	@Override
	public String getName() {
		return "Recipes";
	}
}
