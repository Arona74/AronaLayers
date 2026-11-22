package io.arona74.aronalayers.gen.config;

import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The generation settings chosen on a Create World screen, held until that world's server starts
 * and {@link io.arona74.aronalayers.gen.LayerConfig#openWorld} writes them as its generation.json.
 *
 * <p>Everything here belongs to one Create World screen, its owner: a new screen replaces it, and
 * leaving the screen without creating (Cancel, Escape, a failed creation) drops it. Pressing Create
 * arms it for the folder the world is created in; the next server start takes it, whichever world
 * that is, so it reaches only a new world of that folder name and never outlives one start.
 *
 * <p>A re-created world's screen also knows the world it was made from: its settings are what the
 * new world starts with, with or without the screen's "Layers…" button.
 *
 * <p>Holds no Minecraft type (the owner is only compared by identity), as LayerConfig loads it on a
 * dedicated server too. Synchronized: the integrated server takes the draft on its own thread.
 */
public final class NewWorldDraft {
    private NewWorldDraft() {}

    /** What the next server start gets, for a world folder named {@code folder} that has no file yet. */
    public record Pending(String folder, Map<ConfigOption<?>, Object> values, Path source) {}

    private static WeakReference<Object> owner = new WeakReference<>(null);
    /** The WORLD values stored by the config screen's Done, or null while it has not been used. */
    private static Map<ConfigOption<?>, Object> values;
    /** The folder of the world the owner re-creates, or null for a fresh world. */
    private static Path source;
    /** The world folder name Create was pressed for, or null. */
    private static String armedFolder;

    /** Seen by a Re-create before its screen exists: the world folder it copies. */
    private static Path pendingSource;
    /** The next Create World screen comes from {@code createFromExisting} (a Re-create). */
    private static boolean nextFromExisting;

    /** A Re-create copies the data packs of the world in {@code worldDir}. */
    public static synchronized void recreating(Path worldDir) {
        pendingSource = worldDir == null ? null : worldDir.toAbsolutePath().normalize();
    }

    /** The next Create World screen is made by {@code createFromExisting}. */
    public static synchronized void fromExisting() {
        nextFromExisting = true;
    }

    /** A Create World screen was made: it owns a new, empty draft. */
    public static synchronized void screenCreated(Object screen) {
        Path from = nextFromExisting ? pendingSource : null;
        clear();
        owner = new WeakReference<>(screen);
        source = from;
    }

    /** {@code screen} was left without creating a world. */
    public static synchronized void screenClosed(Object screen) {
        if (owner.get() == screen) clear();
    }

    /** Create was pressed on {@code screen}, for a world folder named {@code folder}. */
    public static synchronized void creating(Object screen, String folder) {
        if (owner.get() == screen) armedFolder = folder;
        else clear();
    }

    /** The values stored for {@code screen} by an earlier Done, or null. */
    public static synchronized Map<ConfigOption<?>, Object> values(Object screen) {
        return owner.get() == screen && values != null ? new LinkedHashMap<>(values) : null;
    }

    /** The folder of the world {@code screen} re-creates, or null. */
    public static synchronized Path source(Object screen) {
        return owner.get() == screen ? source : null;
    }

    /** Done on the config screen opened from {@code screen}. */
    public static synchronized void store(Object screen, Map<ConfigOption<?>, Object> worldValues) {
        if (owner.get() != screen) return;
        values = new LinkedHashMap<>(worldValues);
    }

    /**
     * A server is starting the world in {@code worldDir}: what was chosen for it on its Create World
     * screen, or null. Whatever is held is dropped either way.
     */
    public static synchronized Pending take(Path worldDir) {
        Pending pending = null;
        Path name = worldDir.toAbsolutePath().normalize().getFileName();
        if (armedFolder != null && name != null && armedFolder.equals(name.toString()) && (values != null || source != null)) {
            pending = new Pending(armedFolder, values, source);
        }
        clear();
        return pending;
    }

    private static void clear() {
        owner = new WeakReference<>(null);
        values = null;
        source = null;
        armedFolder = null;
        pendingSource = null;
        nextFromExisting = false;
    }
}
