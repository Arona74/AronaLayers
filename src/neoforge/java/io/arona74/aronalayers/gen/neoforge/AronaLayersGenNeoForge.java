package io.arona74.aronalayers.gen.neoforge;

import io.arona74.aronalayers.gen.AronaLayersGen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

/**
 * NeoForge entry point of terrain generation: a second @Mod class of the same mod, next to
 * neoforge.AronaLayersNeoForge, which registers the blocks. The setup only adds event listeners
 * and reads the config, so it runs here. ClientSetup is each Minecraft version's own.
 */
@Mod(AronaLayersGen.MOD_ID)
public class AronaLayersGenNeoForge {
    public AronaLayersGenNeoForge(IEventBus modBus, ModContainer container, Dist dist) {
        AronaLayersGen.init();

        if (dist.isClient()) {
            ClientSetup.init(modBus, container);
        }
    }
}
