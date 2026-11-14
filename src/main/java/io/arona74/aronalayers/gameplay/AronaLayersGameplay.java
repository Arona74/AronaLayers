package io.arona74.aronalayers.gameplay;

import io.arona74.aronalayers.block.LayerBlock;
import io.arona74.aronalayers.gameplay.command.GameplayDebugCommand;
import io.arona74.aronalayers.gameplay.ore.OreLayerDrops;
import io.arona74.aronalayers.gen.platform.Platform;
import io.arona74.aronalayers.item.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Loader-independent setup of the vanilla mechanics for layers (the former Arona Layers Extras
 * mod): grass and mycelium spread onto dirt layers, sheep eat grass layers, layers fall with the
 * sand under them, ore layers drop small ores. Each has its option in the Gameplay tab
 * (gen.LayerConfig). The sheep, the falling and landing, the grass decay and the ore drops are
 * mixins (gameplay.mixin); the rest hangs on the two events registered here.
 *
 * <p>The blocks and the small ore items are registered by AronaLayers.init(), not here.
 */
public final class AronaLayersGameplay {
    private AronaLayersGameplay() {}

    public static final Logger LOGGER = LoggerFactory.getLogger("AronaLayers/Gameplay");

    public static void init() {
        Platform.onLevelTickEnd(LayerSpreadHandler::onLevelTick);
        Platform.onPlayerBlockBreak(LayerFallHandler::onBlockBroken);
        LayerBlock.oneLayerDrop = OreLayerDrops::dropOneLayer;
        Platform.registerCommands(GameplayDebugCommand::register);

        // A supplier: on NeoForge this runs before the items are registered.
        io.arona74.aronalayers.platform.Platform.addToCreativeTab(CreativeModeTabs.INGREDIENTS, () -> List.of(
                ModItems.SMALL_COAL, ModItems.SMALL_RAW_IRON, ModItems.SMALL_RAW_COPPER,
                ModItems.SMALL_RAW_GOLD, ModItems.SMALL_DIAMOND, ModItems.SMALL_EMERALD));

        LOGGER.info("Vanilla mechanics for layers are set up");
    }
}
