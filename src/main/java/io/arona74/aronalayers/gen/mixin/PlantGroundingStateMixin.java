package io.arona74.aronalayers.gen.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.arona74.aronalayers.gen.grounding.GroundedState;
import io.arona74.aronalayers.gen.grounding.GroundingFlags;
import io.arona74.aronalayers.gen.grounding.PlantGrounding;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Moves the outline and collision shapes of grounded plants down with them (see PlantGrounding),
 * so a lowered plant is selected, outlined and collided with where it is drawn.
 *
 * <p>These methods run for every block, all the time — entity movement alone queries collision
 * shapes by the hundred per tick — so the hooks are MixinExtras value modifiers (no callback
 * allocation) and the grounded-or-not answer is cached on the state, leaving non-plants with one
 * field read and a compare.
 *
 * <p>The same hooks reach a standing trunk (an upright log on a layer) or post (a fence, wall,
 * pane or bars) down to the layer's top and make the layer under it solid; see
 * PlantGrounding.standingOutline and trunkFooting.
 *
 * <p>Hooked points:
 * <ul>
 *   <li>{@code getShape(level, pos, context)}: the two-argument overload delegates here.</li>
 *   <li>{@code getCollisionShape(level, pos, context)}.</li>
 *   <li>The cached collision shape read inside {@code getCollisionShape(level, pos)}. That overload
 *       returns the state's cached shape when it has one and only falls through to the context
 *       overload when it doesn't, so hooking its return as well would move the uncached path twice.</li>
 * </ul>
 */
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class PlantGroundingStateMixin implements GroundedState {

    @Unique
    private byte aronalayers$grounded;

    @Unique
    private int aronalayers$groundedEpoch;

    /** Whether this state is a layer block, which a standing trunk can rest on; UNKNOWN until asked. */
    @Unique
    private byte aronalayers$layerSoil;

    @Unique
    private boolean aronalayers$isLayerSoil() {
        byte layerSoil = this.aronalayers$layerSoil;
        if (layerSoil == PlantGrounding.UNKNOWN) {
            if (!GroundingFlags.ready) return false;
            layerSoil = PlantGrounding.isLayerSoil((BlockState) (Object) this) ? PlantGrounding.YES : PlantGrounding.NO;
            this.aronalayers$layerSoil = layerSoil;
        }
        return layerSoil == PlantGrounding.YES;
    }

    @Override
    public boolean aronalayers$isGrounded() {
        byte grounded = this.aronalayers$grounded;
        int epoch = GroundingFlags.epoch;
        if (grounded != PlantGrounding.UNKNOWN && this.aronalayers$groundedEpoch == epoch) {
            return grounded == PlantGrounding.YES;
        }
        // Not before onInitialize (see GroundingFlags), and not from inside a classification.
        if (!GroundingFlags.ready || PlantGrounding.isClassifying()) return false;
        grounded = PlantGrounding.classify((BlockState) (Object) this);
        if (grounded == PlantGrounding.UNKNOWN) return false;
        this.aronalayers$grounded = grounded;
        this.aronalayers$groundedEpoch = epoch;
        return grounded == PlantGrounding.YES;
    }

    @ModifyReturnValue(
        method = "getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
        at = @At("RETURN")
    )
    private VoxelShape aronalayers$groundOutline(VoxelShape shape, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (!aronalayers$isGrounded()) return PlantGrounding.standingOutline((BlockState) (Object) this, level, pos, shape);
        return PlantGrounding.lowerShape((BlockState) (Object) this, level, pos, shape);
    }

    @ModifyReturnValue(
        method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
        at = @At("RETURN")
    )
    private VoxelShape aronalayers$groundCollision(VoxelShape shape, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (aronalayers$isLayerSoil()) return PlantGrounding.trunkFooting(level, pos, shape);
        if (!aronalayers$isGrounded()) return PlantGrounding.bridgedCollision((BlockState) (Object) this, level, pos, shape);
        return PlantGrounding.lowerShape((BlockState) (Object) this, level, pos, shape);
    }

    @ModifyExpressionValue(
        method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$BlockStateBase$Cache;collisionShape:Lnet/minecraft/world/phys/shapes/VoxelShape;"
        )
    )
    private VoxelShape aronalayers$groundCachedCollision(VoxelShape shape, BlockGetter level, BlockPos pos) {
        if (aronalayers$isLayerSoil()) return PlantGrounding.trunkFooting(level, pos, shape);
        if (!aronalayers$isGrounded()) return PlantGrounding.bridgedCollision((BlockState) (Object) this, level, pos, shape);
        return PlantGrounding.lowerShape((BlockState) (Object) this, level, pos, shape);
    }
}
