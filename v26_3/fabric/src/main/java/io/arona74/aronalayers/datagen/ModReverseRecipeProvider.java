package io.arona74.aronalayers.datagen;

import java.util.concurrent.CompletableFuture;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.block.LayerBlock;
import io.arona74.aronalayers.registry.Args;
import io.arona74.aronalayers.registry.DatagenArgs;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

/**
 * Generates the "8 layers -> 1 full block" recipes into the built-in
 * {@code reverse_recipes} datapack, so players can toggle them per world.
 */
public class ModReverseRecipeProvider extends FabricRecipeProvider {
	public ModReverseRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
		return new RecipeProvider(recipes, advancements) {
			public void offerReverseRecipe(Block layer, Block fullBlock) {
				String layerName = BuiltInRegistries.BLOCK.getKey(layer).getPath();
				String fullBlockName = BuiltInRegistries.BLOCK.getKey(fullBlock).getPath();

				shaped(RecipeCategory.BUILDING_BLOCKS, fullBlock, 1)
						.define('L', layer)
						.pattern("LLL")
						.pattern("L L")
						.pattern("LLL")
						.unlockedBy(getHasName(layer), has(layer))
						.save(output, AronaLayers.MOD_ID + ":" + fullBlockName + "_from_" + layerName);
			}

			@Override
			public void buildRecipes() {
				BuiltInRegistries.BLOCK.stream().filter(AronaLayers::isNamespaced).forEach(block -> {
					DatagenArgs args = Args.DATAGEN_ARGS.get(block);

					if (block instanceof LayerBlock && args != null && args.parentBlock != null) {
						offerReverseRecipe(block, args.parentBlock);
					}
				});
			}
		};
	}

	@Override
	public String getName() {
		return "Reverse Layer Recipes";
	}
}
