package io.arona74.aronalayers.gameplay.neoforge;

import io.arona74.aronalayers.AronaLayers;
import io.arona74.aronalayers.gameplay.AronaLayersGameplay;

import net.neoforged.fml.common.Mod;

/**
 * TEMPORARY (docs/MERGE_PLAN.md, phase 3): NeoForge entry point of the gameplay package while it
 * is only ported to Minecraft 1.21.1. The mod's entry point (neoforge.AronaLayersNeoForge) is
 * compiled for every version and cannot name the package yet. Once every version has it, that
 * class calls AronaLayersGameplay.init() itself and this one goes.
 */
@Mod(AronaLayers.MOD_ID)
public class GameplayNeoForge {
    public GameplayNeoForge() {
        AronaLayersGameplay.init();
    }
}
