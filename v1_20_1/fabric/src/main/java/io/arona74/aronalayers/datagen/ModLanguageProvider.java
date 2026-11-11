package io.arona74.aronalayers.datagen;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.block.Layer3DBlock;
import io.arona74.aronalayers.registry.Args;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.loader.impl.util.StringUtil;

public class ModLanguageProvider extends FabricLanguageProvider {
	public ModLanguageProvider(FabricDataOutput dataOutput) {
		super(dataOutput, "en_us");
	}

	@Override
	public void generateTranslations(TranslationBuilder translationBuilder) {
		BuiltInRegistries.BLOCK.stream().filter(AronaLayers::isNamespaced).forEach(block -> {
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

		translationBuilder.add(ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(AronaLayers.MOD_ID, "layers")), "AronaLayers");
		translationBuilder.add(ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(AronaLayers.MOD_ID, "3d_layers")), "AronaLayers 3D");
	}
}
