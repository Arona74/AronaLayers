package io.arona74.aronalayers.gen.injection;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;

/**
 * Raises the trees FreeTerraForged and ReTerraForged paste from templates, as TreeShiftMixin does
 * for vanilla trees. On a TerraForged world most trees are not TreeFeatures: TemplateFeature.paste
 * checks the spot with the template's placement (TreePlacement.canPlaceAt: soil under the origin,
 * room above) and pastes the template at the origin.
 *
 * <p>{Ftf,Rtf}TemplateTreePlacementMixin records the origin a tree placement accepted;
 * {Ftf,Rtf}TemplateTreeShiftMixin then moves paste's origin up, after the check, so the check still
 * sees the soil. Other templates (bushes, rocks: other placements) are never recorded, so they keep
 * their origin. The trunk the mod extends down into the freed cell is cleared by
 * LayerPlacementHelper.clearTrunkGaps. GenMixinPlugin adds the mixins when the mod is installed.
 */
public final class TemplateTreeHook {
    private TemplateTreeHook() {}

    private static final ThreadLocal<BlockPos> accepted = new ThreadLocal<>();

    /** A tree placement accepted {@code origin}. */
    public static void accepted(BlockPos origin) {
        accepted.set(origin.immutable());
    }

    /**
     * Called on every read of paste's origin: one block up for the first read after a tree
     * placement accepted {@code origin}, unchanged otherwise (including that check's own read).
     */
    public static BlockPos pasteOrigin(WorldGenLevel level, BlockPos origin) {
        BlockPos tree = accepted.get();
        if (tree == null || !tree.equals(origin)) return origin;
        accepted.remove();
        return LayerPlacementHelper.shiftTreeOrigin(level, origin, false);
    }

    /** Called when paste returns. */
    public static void end() {
        accepted.remove();
        LayerPlacementHelper.endTreePlacement();
    }
}
