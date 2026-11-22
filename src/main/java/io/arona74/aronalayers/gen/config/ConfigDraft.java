package io.arona74.aronalayers.gen.config;

import io.arona74.aronalayers.gen.LayerConfig;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Edits made in the config screen, held apart from the live LayerConfig fields until they are
 * committed: worldgen threads read those fields mid-chunk, so a toggle must not reach them one click
 * at a time. One draft is shared by every tab of a screen session.
 *
 * <p>A new-world draft ({@link #forNewWorld}) holds the generation settings of a world not created
 * yet: it is never committed, its WORLD values go to {@link NewWorldDraft} instead.
 */
public final class ConfigDraft {
    /** What a draft edits. */
    public enum Target {
        /** The running values and their files. */
        LIVE,
        /** A world being created on the Create World screen. */
        NEW_WORLD,
        /** A world being re-created from an existing one. */
        RECREATED_WORLD
    }

    private final Map<ConfigOption<?>, Object> values = new IdentityHashMap<>();
    private final Target target;

    public ConfigDraft() {
        this(Target.LIVE);
    }

    private ConfigDraft(Target target) {
        this.target = target;
        for (ConfigOption<?> option : LayerConfig.options()) {
            values.put(option, copyOf(option, option.get()));
        }
    }

    /** A draft of a new world's generation settings, starting from {@code worldValues}. */
    public static ConfigDraft forNewWorld(Map<ConfigOption<?>, Object> worldValues, boolean recreated) {
        ConfigDraft draft = new ConfigDraft(recreated ? Target.RECREATED_WORLD : Target.NEW_WORLD);
        for (Map.Entry<ConfigOption<?>, Object> entry : worldValues.entrySet()) {
            if (entry.getKey().tab.scope == ConfigTab.Scope.WORLD) draft.values.put(entry.getKey(), copyOf(entry.getKey(), entry.getValue()));
        }
        return draft;
    }

    public Target target() {
        return target;
    }

    /** Whether this draft is a world not created yet (only its generation tabs are shown). */
    public boolean newWorld() {
        return target != Target.LIVE;
    }

    /** A copy of the WORLD values, in option order. */
    public Map<ConfigOption<?>, Object> worldValues() {
        Map<ConfigOption<?>, Object> world = new LinkedHashMap<>();
        for (ConfigOption<?> option : LayerConfig.options()) {
            if (option.tab.scope == ConfigTab.Scope.WORLD) world.put(option, copyOf(option, values.get(option)));
        }
        return world;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ConfigOption<T> option) {
        return (T) values.get(option);
    }

    public <T> void set(ConfigOption<T> option, T value) {
        values.put(option, value);
    }

    /** Whether every option this one depends on currently has the value it needs, transitively. */
    public boolean requirementsMet(ConfigOption<?> option) {
        for (ConfigOption.Requirement requirement : option.requirements) {
            if (!Objects.equals(values.get(requirement.option()), requirement.value())) return false;
            if (!requirementsMet(requirement.option())) return false;
        }
        return true;
    }

    /** The requirements of {@code option} that are not met, for the tooltip. */
    public List<ConfigOption.Requirement> unmetRequirements(ConfigOption<?> option) {
        List<ConfigOption.Requirement> unmet = new ArrayList<>();
        for (ConfigOption.Requirement requirement : option.requirements) {
            if (!Objects.equals(values.get(requirement.option()), requirement.value())) unmet.add(requirement);
        }
        return unmet;
    }

    public void resetToDefaults(ConfigTab tab) {
        for (ConfigOption<?> option : LayerConfig.options()) {
            if (option.tab == tab) values.put(option, copyOf(option, option.defaultValue));
        }
    }

    /**
     * Writes every changed value into LayerConfig and saves the files of the scopes that changed:
     * config.json for GLOBAL options; for WORLD options the open world's generation.json, or
     * generation_defaults.json outside a world (see {@link LayerConfig#save(Set)}).
     * @return the options that changed
     */
    public List<ConfigOption<?>> commit() {
        if (newWorld()) throw new IllegalStateException("a new world's draft is stored, not committed");
        List<ConfigOption<?>> changed = new ArrayList<>();
        Set<ConfigTab.Scope> scopes = EnumSet.noneOf(ConfigTab.Scope.class);
        for (ConfigOption<?> option : LayerConfig.options()) {
            if (!Objects.equals(option.get(), values.get(option))) {
                changed.add(option);
                scopes.add(option.tab.scope);
                apply(option);
            }
        }
        if (!changed.isEmpty()) {
            LayerConfig.save(scopes);
            LayerConfig.onValuesChanged();
        }
        return changed;
    }

    @SuppressWarnings("unchecked")
    private <T> void apply(ConfigOption<T> option) {
        option.set(option.copy((T) values.get(option)));
    }

    @SuppressWarnings("unchecked")
    private static <T> Object copyOf(ConfigOption<T> option, Object value) {
        return option.copy((T) value);
    }
}
