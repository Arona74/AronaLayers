package io.arona74.aronalayers.gen.client.config;

import io.arona74.aronalayers.gen.AronaLayersGen;
import io.arona74.aronalayers.gen.LayerConfig;
import io.arona74.aronalayers.gen.config.ConfigDraft;
import io.arona74.aronalayers.gen.config.ConfigOption;
import io.arona74.aronalayers.gen.config.ConfigTab;
import io.arona74.aronalayers.gen.config.NewWorldDraft;
import io.arona74.aronalayers.gen.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The in-game config screen: one tab per {@link ConfigTab} along the top, the options of the open
 * tab in the left half, and in the right half a preview of the option under the mouse (an image,
 * its name and its description: {@link ConfigPreview}). Done / Cancel sit in the footer. Edits go
 * into a {@link ConfigDraft} and reach the game only on Done.
 *
 * <p>The option list and the preview panel are drawn by each version's {@link OptionRows} and
 * {@link PreviewPanel}, as drawing differs between versions; the rest is shared from 1.21.1 on
 * (1.20.1 has its own copy of this class). Switching tab opens a new screen on the same draft.
 *
 * <p>Opened from the Create World screen's "Layers…" button ({@link #openForNewWorld}), it edits
 * the generation settings of the world about to be created: only the generation tabs, a draft
 * started from the defaults for new worlds (or the re-created world's settings), and Done keeps
 * them for that world ({@link NewWorldDraft}) instead of saving any file.
 */
public class AronaConfigScreen extends Screen {
    private static final int LIST_TOP = 50;
    private static final int FOOTER_HEIGHT = 32;

    /** Remembered for the session, so reopening the screen returns to where the user was. */
    private static ConfigTab lastTab = ConfigTab.VISUALS;
    private static boolean showAdvanced;

    private final Screen lastScreen;
    private final ConfigDraft draft;
    private final ConfigTab tab;
    private ConfigPreview preview;
    private boolean discard;

    public AronaConfigScreen(Screen parent) {
        this(parent, new ConfigDraft(), lastTab);
    }

    private AronaConfigScreen(Screen parent, ConfigDraft draft, ConfigTab tab) {
        super(Component.translatable("config.aronalayers.title"));
        this.lastScreen = parent;
        this.draft = draft;
        this.tab = tab;
        if (!draft.newWorld()) lastTab = tab;
    }

    /**
     * Opens the generation settings of the world being created on {@code createWorldScreen}: what
     * an earlier Done kept for it, else the defaults for new worlds, or the settings of the world it
     * re-creates. Done and Escape return there keeping the values; Cancel drops this visit's changes.
     */
    public static void openForNewWorld(Screen createWorldScreen) {
        Path source = NewWorldDraft.source(createWorldScreen);
        Map<ConfigOption<?>, Object> values = NewWorldDraft.values(createWorldScreen);
        if (values == null) values = LayerConfig.newWorldValues(source);
        ConfigDraft draft = ConfigDraft.forNewWorld(values, source != null);
        ScreenCompat.setScreen(new AronaConfigScreen(createWorldScreen, draft, ConfigTab.TERRAIN));
    }

    @Override
    protected void init() {
        int titleWidth = font.width(title);
        addRenderableWidget(new StringWidget((width - titleWidth) / 2, 8, titleWidth, 9, title, font));

        ConfigWidgets widgets = new ConfigWidgets(draft, font);
        List<ConfigTab> tabs = visibleTabs(draft.newWorld());
        int buttonWidth = Math.min(80, (width - 20) / tabs.size() - 4);
        int x = (width - tabs.size() * (buttonWidth + 4) + 4) / 2;
        for (ConfigTab t : tabs) {
            Button button = addRenderableWidget(Button.builder(widgets.tabLabel(t), b -> reopen(t))
                    .bounds(x, 22, buttonWidth, 20).build());
            button.active = t != tab;
            x += buttonWidth + 4;
        }

        int half = width / 2;
        int listBottom = height - FOOTER_HEIGHT;
        OptionRows list = new OptionRows(Minecraft.getInstance(), half, listBottom - LIST_TOP, LIST_TOP);
        fillRows(list, widgets, tab, font);
        addRenderableWidget(list);

        preview = new ConfigPreview(widgets, tab, font);
        preview.setBounds(half + 4, LIST_TOP + 2, width - 10, listBottom - 2);
        addRenderableOnly(new PreviewPanel(preview, font));

        int y = height - 26;
        int fx = width / 2 - 179;
        addRenderableWidget(ScreenCompat.onOff(showAdvanced)
                .create(fx, y, 100, 20, Component.translatable("config.aronalayers.advanced"), (b, value) -> {
                    showAdvanced = value;
                    reopen(tab);
                })).setTooltip(Tooltip.create(Component.translatable("config.aronalayers.advanced.tooltip")));
        Button reset = addRenderableWidget(Button.builder(Component.translatable("config.aronalayers.reset_tab"), b -> {
                    draft.resetToDefaults(tab);
                    reopen(tab);
                }).bounds(fx + 106, y, 70, 20)
                .tooltip(Tooltip.create(Component.translatable("config.aronalayers.reset_tab.tooltip"))).build());
        reset.active = widgets.showsOptions(tab);
        Button folder = addRenderableWidget(Button.builder(Component.translatable("config.aronalayers.open_folder"),
                        b -> ScreenCompat.openPath(widgets.folder(tab)))
                .bounds(fx + 182, y, 50, 20)
                .tooltip(Tooltip.create(widgets.folderTooltip(tab))).build());
        folder.active = widgets.hasFolder();
        addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, b -> {
                    discard = true;
                    onClose();
                }).bounds(fx + 238, y, 60, 20).build());
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> onClose()).bounds(fx + 304, y, 60, 20).build());
    }

    /**
     * The open tab's rows: on a world generation tab its heading first (the seam warning in a world,
     * "defaults for new worlds" outside one, "set by the server" on a remote server, where the
     * heading is all: that world's settings are in the server's file), then each group
     * under its heading, one option per row. A block-list box has no label of its own, so its name
     * gets a row above it.
     */
    static void fillRows(OptionRows list, ConfigWidgets widgets, ConfigTab tab, net.minecraft.client.gui.Font font) {
        if (tab.generation()) list.add(ScreenCompat.centeredText(ConfigWidgets.WIDTH, ConfigWidgets.HEIGHT, widgets.generationHeading(), font));
        if (!widgets.showsOptions(tab)) return;
        String group = null;
        for (ConfigOption<?> option : LayerConfig.options()) {
            if (option.tab != tab || (option.advanced && !showAdvanced)) continue;
            if (!Objects.equals(option.group, group)) {
                group = option.group;
                if (group != null) {
                    list.add(ScreenCompat.centeredText(ConfigWidgets.WIDTH, ConfigWidgets.HEIGHT,
                            Component.translatable("config.aronalayers.group." + group), font));
                }
            }
            if (option.kind == ConfigOption.Kind.STRING_LIST) list.add(widgets.label(option));
            list.add(widgets.create(option));
        }
    }

    /** The mouse wheel over the preview panel scrolls a long description. */
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (preview != null && preview.contains(mouseX, mouseY)) {
            preview.scroll(scrollY);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private void reopen(ConfigTab target) {
        ScreenCompat.setScreen(new AronaConfigScreen(lastScreen, draft, target));
    }

    /**
     * Done and Escape save, as in vanilla's options screens; Cancel discards. For a world not created
     * yet, "save" keeps the values for it until it is created.
     */
    @Override
    public void onClose() {
        if (!discard) {
            if (draft.newWorld()) NewWorldDraft.store(lastScreen, draft.worldValues());
            else commit(draft);
        }
        ScreenCompat.setScreen(lastScreen);
    }

    static void commit(ConfigDraft draft) {
        List<ConfigOption<?>> changed = draft.commit();
        List<String> restart = changed.stream()
                .filter(o -> o.apply == ConfigOption.Apply.RESTART)
                .map(o -> o.key)
                .toList();
        if (!restart.isEmpty()) {
            AronaLayersGen.LOGGER.info("[Config] Restart Minecraft for these changes to take effect: {}", restart);
        }
    }

    /** The tabs shown: for a world not created yet ({@code newWorld}), only the generation ones. */
    static List<ConfigTab> visibleTabs(boolean newWorld) {
        List<ConfigTab> tabs = new ArrayList<>();
        for (ConfigTab t : ConfigTab.values()) {
            if (newWorld && !t.generation()) continue;
            if (t.requiredMod == null || Platform.isModLoaded(t.requiredMod)) tabs.add(t);
        }
        return tabs;
    }
}
