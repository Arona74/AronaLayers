package net.fellter.vanillalayerplus.mixin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(StainedGlassBlock.class)
public class MixinStainedGlassBlock extends Block {
	@Unique
	private static final Map<BlockState, VoxelShape> CACHED = new ConcurrentHashMap<>();
	@Shadow
	@Final
	private DyeColor color;

	public MixinStainedGlassBlock(Properties settings) {
		super(settings);
	}

	@Unique
	protected VoxelShape getCached(BlockState state) {
		return CACHED.computeIfAbsent(state, s -> s.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
	}

	@Override
	protected boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
		if (stateFrom.getBlock() instanceof BeaconBeamBlock stainable && !stateFrom.getBlock().equals(Blocks.BEACON)) {
			VoxelShape stateCullingShape = getCached(state);
			VoxelShape stateFromCullingShape = getCached(stateFrom);

			return Shapes.blockOccudes(stateCullingShape, stateFromCullingShape, direction) && stainable.getColor() == color;
		}

		return false;
	}
}
