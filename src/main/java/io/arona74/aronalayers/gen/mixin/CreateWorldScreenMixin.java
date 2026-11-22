package io.arona74.aronalayers.gen.mixin;

import io.arona74.aronalayers.gen.config.NewWorldDraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;

/**
 * Ties the generation settings chosen with the Create World screen's "Layers…" button
 * ({@link NewWorldDraft}) to that screen: a new screen starts a new draft, leaving it without
 * creating drops the draft, and Create arms it for the folder the world is created in. A Re-create
 * also records the world it copies, whose settings the new world starts from.
 *
 * <p>Every target has the same name, and the hooks need no arguments that differ, on every
 * supported version (1.20.1 to 26.3): popScreen is what Cancel, Escape and a failed creation call;
 * the world folder is {@code getUiState().getTargetFolder()}, the name createNewWorld uses.
 */
@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void aronalayers$created(CallbackInfo ci) {
        NewWorldDraft.screenCreated(this);
    }

    @Inject(method = "popScreen", at = @At("HEAD"))
    private void aronalayers$closed(CallbackInfo ci) {
        NewWorldDraft.screenClosed(this);
    }

    @Inject(method = "onCreate", at = @At("HEAD"))
    private void aronalayers$creating(CallbackInfo ci) {
        NewWorldDraft.creating(this, ((CreateWorldScreen) (Object) this).getUiState().getTargetFolder());
    }

    /** A Re-create's screen: the world it copies was recorded just before, below. */
    @Inject(method = "createFromExisting", at = @At("HEAD"))
    private static void aronalayers$fromExisting(CallbackInfoReturnable<CreateWorldScreen> cir) {
        NewWorldDraft.fromExisting();
    }

    /** Called by Re-create only, with {@code <world>/datapacks} of the world being re-created. */
    @Inject(method = "createTempDataPackDirFromExistingWorld", at = @At("HEAD"))
    private static void aronalayers$recreating(Path dataPackDir, Minecraft minecraft, CallbackInfoReturnable<Path> cir) {
        NewWorldDraft.recreating(dataPackDir == null ? null : dataPackDir.toAbsolutePath().normalize().getParent());
    }
}
