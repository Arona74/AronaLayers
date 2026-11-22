package io.arona74.aronalayers.gen.mixin;

import io.arona74.aronalayers.gen.client.config.AronaConfigScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * A "Layers…" button on the Create World screen's More tab, under Game Rules, Experiments and Data
 * Packs, opening the generation settings of the world being created
 * ({@link AronaConfigScreen#openForNewWorld}).
 *
 * <p>On every supported version (1.20.1 to 26.3) the More tab is a GridLayoutTab whose constructor
 * takes only the outer screen and fills rows 0 to 2 of a one-column grid; the screen lays the grid
 * out, on resize too. MoreTab is a private inner class, hence the string target.
 */
@Mixin(targets = "net.minecraft.client.gui.screens.worldselection.CreateWorldScreen$MoreTab")
public abstract class CreateWorldMoreTabMixin extends GridLayoutTab {
    private static final int ROW = 3;
    private static final int WIDTH = 210;

    private CreateWorldMoreTabMixin(Component title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void aronalayers$addLayersButton(CreateWorldScreen screen, CallbackInfo ci) {
        layout.addChild(Button.builder(Component.translatable("config.aronalayers.create_world.button"),
                        b -> AronaConfigScreen.openForNewWorld(screen))
                .width(WIDTH)
                .tooltip(Tooltip.create(Component.translatable("config.aronalayers.create_world.button.tooltip")))
                .build(), ROW, 0);
    }
}
