package io.arona74.aronalayers;

import io.arona74.aronalayers.datagen.*;
import io.arona74.aronalayers.registry.Args;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

@Environment(EnvType.CLIENT)
public class AronaLayersDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		Args.registerDatagenArgs();

		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModLootTableProvider::new);
		pack.addProvider(ModLanguageProvider::new);

		FabricDataGenerator.Pack reverseRecipesPack = fabricDataGenerator.createBuiltinResourcePack(AronaLayers.REVERSE_RECIPES_PACK);
		reverseRecipesPack.addProvider(ModReverseRecipeProvider::new);
	}
}
