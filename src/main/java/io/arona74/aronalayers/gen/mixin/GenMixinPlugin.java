package io.arona74.aronalayers.gen.mixin;

import io.arona74.aronalayers.gen.platform.Platform;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Adds the mixins aimed at another mod's classes only when that mod is installed. Listed in
 * aronalayers.gen.mixins.json, they would make Mixin look the target classes up at every start
 * and log a "could not load class" warning for each one that is missing.
 */
public class GenMixinPlugin implements IMixinConfigPlugin {

    @Override
    public List<String> getMixins() {
        List<String> mixins = new ArrayList<>();
        // FreeTerraForged and ReTerraForged declare each other incompatible: at most one is present.
        if (Platform.isModLoaded("freeterraforged")) {
            mixins.add("FtfTemplateTreeShiftMixin");
            mixins.add("FtfTemplateTreePlacementMixin");
        }
        if (Platform.isModLoaded("reterraforged")) {
            mixins.add("RtfTemplateTreeShiftMixin");
            mixins.add("RtfTemplateTreePlacementMixin");
        }
        return mixins;
    }

    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public boolean shouldApplyMixin(String targetClassName, String mixinClassName) { return true; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
