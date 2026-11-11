package net.fellter.vanillalayerplus.custom_blocks;

import net.fellter.vanillalayerplus.block.LayerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SlimeLayerBlock extends LayerBlock {
	public SlimeLayerBlock(Properties settings) {
		super(settings);
	}

	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (!entity.isSuppressingBounce()) {
			entity.causeFallDamage(fallDistance, 0.0F, world.damageSources().fall());
		}
	}

	public void updateEntityAfterFallOn(BlockGetter world, Entity entity) {
		if (entity.isSuppressingBounce()) {
			super.updateEntityAfterFallOn(world, entity);
		} else {
			this.bounce(entity);
		}
	}

	private void bounce(Entity entity) {
		Vec3 vec3d = entity.getDeltaMovement();

		if (vec3d.y < 0.0) {
			double d = entity instanceof LivingEntity ? 1.0 : 0.8;
			entity.setDeltaMovement(vec3d.x, -vec3d.y * d, vec3d.z);
		}
	}

	public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
		double d = Math.abs(entity.getDeltaMovement().y);

		if (d < 0.1 && !entity.isSteppingCarefully()) {
			double e = 0.4 + d * 0.2;
			entity.setDeltaMovement(entity.getDeltaMovement().multiply(e, 1.0, e));
		}

		super.stepOn(world, pos, state, entity);
	}
}
