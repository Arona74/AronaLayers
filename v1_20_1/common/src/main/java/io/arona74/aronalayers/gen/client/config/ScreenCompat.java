package io.arona74.aronalayers.gen.client.config;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

/**
 * Version seam for the config screen (Minecraft 1.20.1). The shared screen code calls only
 * these; everything else it uses is identical across the supported versions.
 */
public final class ScreenCompat {
    private ScreenCompat() {}

    /** The values go in before the initial one, which is looked up among them when it is set. */
    public static <T> CycleButton.Builder<T> cycle(Function<T, Component> format, List<T> values, T initial) {
        return CycleButton.builder(format).withValues(values).withInitialValue(initial);
    }

    public static CycleButton.Builder<Boolean> onOff(boolean initial) {
        return CycleButton.onOffBuilder(initial);
    }

    /**
     * A line of text centred in its row: group headings and list labels of the option list. A
     * StringWidget centres its text on this version.
     */
    public static AbstractWidget centeredText(int width, int height, Component text, Font font) {
        return new StringWidget(width, height, text, font);
    }

    public static void setScreen(Screen screen) {
        Minecraft.getInstance().setScreen(screen);
    }

    public static void openPath(Path path) {
        net.minecraft.Util.getPlatform().openFile(path.toFile());
    }

    /** Runs on the client thread after the current tick, e.g. once the chat screen has closed. */
    public static void schedule(Runnable task) {
        Minecraft.getInstance().tell(task);
    }
}
