package io.arona74.aronalayers.gen.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.arona74.aronalayers.gen.injection.TemplateTreeHook;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Records the origin a FreeTerraForged tree placement accepts (see TemplateTreeHook). canPlaceAt is
 * the mod's own method, the same name on every loader; selected by name alone, as its descriptor
 * holds the mod's own Dimensions class. Listed by GenMixinPlugin only when FreeTerraForged is installed.
 */
@Pseudo
@Mixin(targets = "etcodehome.freeterraforged.world.worldgen.feature.template.placement.TreePlacement", remap = false)
public class FtfTemplateTreePlacementMixin {

    @Inject(method = "canPlaceAt", at = @At("RETURN"), remap = false, require = 0)
    private void aronalayers$accepted(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) BlockPos origin) {
        if (cir.getReturnValueZ()) TemplateTreeHook.accepted(origin);
    }
}
