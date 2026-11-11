package io.arona74.aronalayers.custom_blocks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.arona74.aronalayers.compat.Compat;
import io.arona74.aronalayers.block.LayerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GlassLayerBlock extends LayerBlock {
	protected static final Map<BlockState, VoxelShape> CACHED = new ConcurrentHashMap<>();
	public GlassLayerBlock(Properties settings) {
		super(settings);
	}

	protected VoxelShape getCached(BlockState state) {
		return CACHED.computeIfAbsent(state, s -> s.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
	}

	protected VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	protected float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
		return 1.0F;
	}

	protected boolean isTransparent(BlockState state) {
		return true;
	}

	protected boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
		VoxelShape stateCullingShape = getCached(state);
		VoxelShape stateFromCullingShape = getCached(stateFrom);
		return Compat.occludes(stateCullingShape, stateFromCullingShape, direction);
	}
}
