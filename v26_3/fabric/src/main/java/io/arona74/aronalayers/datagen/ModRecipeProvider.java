package io.arona74.aronalayers.datagen;

import java.util.concurrent.CompletableFuture;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.block.LayerBlock;
import io.arona74.aronalayers.item.ModItems;
import io.arona74.aronalayers.registry.Args;
import io.arona74.aronalayers.registry.DatagenArgs;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

public class ModRecipeProvider extends FabricRecipeProvider {
	public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
		return new RecipeProvider(recipes, advancements) {
			public RecipeBuilder layerBlockRecipe(ItemLike output, ItemLike input) {
				return shaped(RecipeCategory.DECORATIONS, output, 16)
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

				smallOreRecipe(ModItems.SMALL_COAL, Items.COAL);
				smallOreRecipe(ModItems.SMALL_RAW_IRON, Items.RAW_IRON);
				smallOreRecipe(ModItems.SMALL_RAW_COPPER, Items.RAW_COPPER);
				smallOreRecipe(ModItems.SMALL_RAW_GOLD, Items.RAW_GOLD);
				smallOreRecipe(ModItems.SMALL_DIAMOND, Items.DIAMOND);
				smallOreRecipe(ModItems.SMALL_EMERALD, Items.EMERALD);
			}

			// Eight small ores, one per layer of an ore layer, make the ore's normal drop.
			private void smallOreRecipe(ItemLike smallOre, ItemLike whole) {
				shaped(RecipeCategory.MISC, whole)
						.define('#', smallOre)
						.pattern("###")
						.pattern("# #")
						.pattern("###")
						.unlockedBy(getHasName(smallOre), has(smallOre))
						.save(output, AronaLayers.MOD_ID + ":" + getItemName(whole) + "_from_" + getItemName(smallOre));
			}
		};
	}

	@Override
	public String getName() {
		return "Recipes";
	}
}
