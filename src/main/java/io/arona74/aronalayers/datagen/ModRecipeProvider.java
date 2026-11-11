package io.arona74.aronalayers.datagen;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.block.LayerBlock;
import io.arona74.aronalayers.registry.Args;
import io.arona74.aronalayers.registry.DatagenArgs;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

public class ModRecipeProvider extends FabricRecipeProvider {
	public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	private RecipeBuilder layerBlockRecipe(ItemLike output, ItemLike input) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, output, 16)
				.define('W', input)
				.pattern(" W")
				.pattern("W ")
				.unlockedBy(getHasName(input), has(input))
				.showNotification(true);
	}

	@Override
	public void buildRecipes(RecipeOutput exporter) {
		BuiltInRegistries.BLOCK.stream().filter(AronaLayers::isNamespaced).forEach(block -> {
			if (Args.DATAGEN_ARGS.containsKey(block)) {
				DatagenArgs args = Args.DATAGEN_ARGS.get(block);

				if (args.parentBlock != null) {
					if (block instanceof LayerBlock) {
						layerBlockRecipe(block, args.parentBlock).save(exporter);
					}

					if (block instanceof LayerBlock && args.stonecuttingInput != null) {
						for (ItemLike itemConvertible : args.stonecuttingInput) {
							// offerStonecuttingRecipe names the recipe with a bare string, which
							// resolves to the minecraft namespace, so the generated unlock
							// advancement pointed at a recipe that does not exist and the recipe
							// never showed up in the recipe book. Build it with our own id instead.
							SingleItemRecipeBuilder.stonecutting(Ingredient.of(itemConvertible), RecipeCategory.BUILDING_BLOCKS, block, 8)
									.unlockedBy(getHasName(itemConvertible), has(itemConvertible))
									.save(exporter, ResourceLocation.fromNamespaceAndPath(AronaLayers.MOD_ID, getConversionRecipeName(block, itemConvertible) + "_stonecutting"));
						}
					}
				}
			}
		});
	}

	@Override
	public String getName() {
		return "Recipes";
	}
}
