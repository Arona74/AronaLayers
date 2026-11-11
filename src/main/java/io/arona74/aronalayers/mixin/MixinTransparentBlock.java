package io.arona74.aronalayers.mixin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.arona74.aronalayers.compat.Compat;
import io.arona74.aronalayers.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HalfTransparentBlock.class)
public class MixinTransparentBlock extends Block {
	@Unique
	private static final Map<BlockState, VoxelShape> CACHED = new ConcurrentHashMap<>();
	public MixinTransparentBlock(Properties settings) {
		super(settings);
	}

	@Unique
	protected VoxelShape getCached(BlockState state) {
		return CACHED.computeIfAbsent(state, s -> s.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
	}

	@Inject(method = "skipRendering", at = @At("HEAD"), cancellable = true)
	public void skipRendering(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
		if (stateFrom.is(ModBlocks.GLASS_LAYER)) {
			VoxelShape stateCullingShape = getCached(state);
			VoxelShape stateFromCullingShape = getCached(stateFrom);
			cir.setReturnValue(Compat.occludes(stateCullingShape, stateFromCullingShape, direction));
		}
	}
}
