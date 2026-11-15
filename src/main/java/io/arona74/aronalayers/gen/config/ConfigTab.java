package io.arona74.aronalayers.gen.config;

import java.util.Locale;

/**
 * The groups options are sorted into: a tab of the config screen, and a section of the config file
 * its scope is saved in. Declaration order is the order of both.
 *
 * <p>Tabs are split by what a change touches. {@link Scope#WORLD} tabs shape the terrain of chunks
 * as they generate, so a change there never reaches chunks already generated and leaves seams in
 * an existing world; they are the settings a world is created with. {@link Scope#GLOBAL} tabs act
 * on every world as it is drawn or played.
 */
public enum ConfigTab {
    VISUALS(Scope.GLOBAL),
    GAMEPLAY(Scope.GLOBAL),
    TERRAIN(Scope.WORLD),
    PLANTS_TREES(Scope.WORLD),
    STRUCTURES(Scope.WORLD),
    DEBUG(Scope.GLOBAL);

    /**
     * Which file a tab's options live in: GLOBAL in config/aronalayers/config.json, WORLD (world
     * generation) in config/aronalayers/generation_defaults.json, the settings new worlds get.
     */
    public enum Scope { GLOBAL, WORLD }

    /** Shown only when this mod is loaded, or always when null. */
    public final String requiredMod;

    public final Scope scope;

    ConfigTab(Scope scope) {
        this(scope, null);
    }

    ConfigTab(Scope scope, String requiredMod) {
        this.scope = scope;
        this.requiredMod = requiredMod;
    }

    /** Options here shape newly generated terrain (see the class comment). */
    public boolean generation() {
        return scope == Scope.WORLD;
    }

    public String id() {
        return name().toLowerCase(Locale.ROOT);
    }

    public String langKey() {
        return "config.aronalayers.tab." + id();
    }
}
