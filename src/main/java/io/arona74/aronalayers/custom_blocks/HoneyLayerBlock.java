package io.arona74.aronalayers.custom_blocks;

import io.arona74.aronalayers.block.LayerBlock;
import io.arona74.aronalayers.compat.Compat;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class HoneyLayerBlock extends LayerBlock {
	public HoneyLayerBlock(Properties settings) {
		super(settings);
	}

	private static boolean hasHoneyBlockEffects(Entity entity) {
		return entity instanceof LivingEntity || Compat.isMinecart(entity) || entity instanceof PrimedTnt || Compat.isBoat(entity);
	}

	public void onFallenOn(Level world, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
		entity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);

		if (!world.isClientSide()) {
			world.broadcastEntityEvent(entity, (byte) 54);
		}

		if (Compat.causeFallDamage(world, entity, fallDistance, 0.2F)) {
			entity.playSound(this.soundType.getFallSound(), this.soundType.getVolume() * 0.5F, this.soundType.getPitch() * 0.75F);
		}
	}

	protected void onEntityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (this.isSliding(pos, entity)) {
			this.triggerAdvancement(entity, pos);
			this.updateSlidingVelocity(entity);
			this.addCollisionEffects(world, entity);
		}

		super.onEntityInside(state, world, pos, entity);
	}

	private static double method_65067(double d) {
		return d / 0.9800000190734863 + 0.08;
	}

	private static double method_65068() {
		return (-0.05 - 0.08) * 0.9800000190734863;
	}

	private boolean isSliding(BlockPos pos, Entity entity) {
		if (entity.onGround()) {
			return false;
		} else if (entity.getY() > (double) pos.getY() + 0.9375 - 1.0E-7) {
			return false;
		} else if (method_65067(entity.getDeltaMovement().y) >= -0.08) {
			return false;
		} else {
			double d = Math.abs((double) pos.getX() + 0.5 - entity.getX());
			double e = Math.abs((double) pos.getZ() + 0.5 - entity.getZ());
			double f = 0.4375 + (double) (entity.getBbWidth() / 2.0F);
			return d + 1.0E-7 > f || e + 1.0E-7 > f;
		}
	}

	private void triggerAdvancement(Entity entity, BlockPos pos) {
		if (entity instanceof ServerPlayer && entity.level().getGameTime() % 20L == 0L) {
			CriteriaTriggers.HONEY_BLOCK_SLIDE.trigger((ServerPlayer) entity, entity.level().getBlockState(pos));
		}
	}

	private void updateSlidingVelocity(Entity entity) {
		Vec3 vec3d = entity.getDeltaMovement();

		if (method_65067(entity.getDeltaMovement().y) < -0.13) {
			double d = -0.05 / method_65067(entity.getDeltaMovement().y);
			entity.setDeltaMovement(new Vec3(vec3d.x * d, method_65068(), vec3d.z * d));
		} else {
			entity.setDeltaMovement(new Vec3(vec3d.x, method_65068(), vec3d.z));
		}

		entity.resetFallDistance();
	}

	private void addCollisionEffects(Level world, Entity entity) {
		if (hasHoneyBlockEffects(entity)) {
			if (world.getRandom().nextInt(5) == 0) {
				entity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
			}

			if (!world.isClientSide() && world.getRandom().nextInt(5) == 0) {
				world.broadcastEntityEvent(entity, (byte) 53);
			}
		}
	}
}
