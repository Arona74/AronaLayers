package net.fellter.vanillalayerplus.datagen;

import java.util.concurrent.CompletableFuture;

import net.fellter.vanillalayerplus.VanillaLayerPlus;
import net.fellter.vanillalayerplus.block.Layer3DBlock;
import net.fellter.vanillalayerplus.registry.Args;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.loader.impl.util.StringUtil;

public class ModLanguageProvider extends FabricLanguageProvider {
	public ModLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder translationBuilder) {
		BuiltInRegistries.BLOCK.stream().filter(VanillaLayerPlus::isNamespaced).forEach(block -> {
			if (Args.DATAGEN_ARGS.containsKey(block) || block instanceof Layer3DBlock) {
				ResourceLocation identifier = BuiltInRegistries.BLOCK.getKey(block);
				String[] var10000 = identifier.getPath().split("_");
				StringBuilder stringBuilder = new StringBuilder();
				String var10001;

				for (String string : var10000) {
					stringBuilder.append(string.equals("3d") ? "3D" : StringUtil.capitalize(string)).append(" ");
				}

				var10001 = stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length()).toString();
				translationBuilder.add(block, var10001);
			}
		});

		translationBuilder.add(ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(VanillaLayerPlus.MOD_ID, "vlp")), "Vanilla+ Layers");
		translationBuilder.add(ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(VanillaLayerPlus.MOD_ID, "vlp_3d")), "Vanilla+ 3D Layers");
	}
}
