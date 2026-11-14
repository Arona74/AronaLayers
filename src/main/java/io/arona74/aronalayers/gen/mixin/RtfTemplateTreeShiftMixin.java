package io.arona74.aronalayers.gen.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.arona74.aronalayers.gen.injection.TemplateTreeHook;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Starts a ReTerraForged template tree one block higher (see TemplateTreeHook). The mod's
 * TemplateFeature.paste(level, random, origin, mirror, rotation, config, pasteType, boolean) checks
 * the origin with the placement's canPlaceAt, then pastes the template there: the origin is
 * replaced once that check accepted it. The other paste overload only delegates, before any check.
 *
 * <p>Selected by name, as the descriptors hold the mod's own classes; "paste*" because a bare name
 * selects only the first method of that name, the delegating one. Every read of the origin
 * argument is passed through, rather than "after canPlaceAt", because an @At target would need the
 * owner and descriptor, which differ between intermediary and Mojang names; TemplateTreeHook moves
 * only the first read after the check, the one handed to Paste.apply. Listed by GenMixinPlugin only
 * when ReTerraForged is installed; require = 0 so a version of it that changes paste costs the tree
 * shift, not the game.
 */
@Pseudo
@Mixin(targets = "raccoonman.reterraforged.world.worldgen.feature.template.TemplateFeature", remap = false)
public class RtfTemplateTreeShiftMixin {

    @ModifyVariable(method = "paste*", at = @At("LOAD"), argsOnly = true, ordinal = 0, remap = false, require = 0)
    private static BlockPos aronalayers$shiftOrigin(BlockPos origin, @Local(argsOnly = true) WorldGenLevel level) {
        return TemplateTreeHook.pasteOrigin(level, origin);
    }

    @Inject(method = "paste*", at = @At("RETURN"), remap = false, require = 0)
    private static void aronalayers$endPlacement(CallbackInfoReturnable<Boolean> cir) {
        TemplateTreeHook.end();
    }
}
