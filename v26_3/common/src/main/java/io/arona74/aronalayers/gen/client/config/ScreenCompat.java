package io.arona74.aronalayers.gen.client.config;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

/**
 * Version seam for the config screen (Minecraft 26.3: screens are set on Gui, folders opened by Blaze3D). The shared screen code calls only
 * these; everything else it uses is identical across the supported versions.
 */
public final class ScreenCompat {
    private ScreenCompat() {}

    public static <T> CycleButton.Builder<T> cycle(Function<T, Component> format, List<T> values, T initial) {
        return CycleButton.builder(format, initial).withValues(values);
    }

    public static CycleButton.Builder<Boolean> onOff(boolean initial) {
        return CycleButton.onOffBuilder(initial);
    }

    /**
     * A line of text centred in its row: group headings and list labels of the option list. From
     * 1.21.11 a StringWidget always draws its text at the left (its alignCenter() is gone), so this
     * one draws it centred itself.
     */
    public static AbstractWidget centeredText(int width, int height, Component text, Font font) {
        return new StringWidget(width, height, text, font) {
            @Override
            public void visitLines(ActiveTextCollector output) {
                output.accept(TextAlignment.CENTER, getX() + getWidth() / 2, getY() + (getHeight() - font.lineHeight) / 2, getMessage());
            }
        };
    }

    public static void setScreen(Screen screen) {
        Minecraft.getInstance().gui.setScreen(screen);
    }

    public static void openPath(Path path) {
        com.mojang.blaze3d.Blaze3D.openPath(path);
    }

    /** Runs on the client thread after the current tick, e.g. once the chat screen has closed. */
    public static void schedule(Runnable task) {
        Minecraft.getInstance().schedule(task);
    }
}
