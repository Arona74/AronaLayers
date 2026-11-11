package io.arona74.aronalayers.custom_blocks;

import io.arona74.aronalayers.compat.Compat;
import io.arona74.aronalayers.block.LayerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public class OxidizableLayerBlock extends LayerBlock implements WeatheringCopper {
	private final WeatheringCopper.WeatherState oxidationLevel;

	public OxidizableLayerBlock(WeatherState oxidationLevel, Properties settings) {
		super(settings);
		this.oxidationLevel = oxidationLevel;
	}

	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		Compat.changeOverTime(this, state, world, pos, random);
	}

	public boolean isRandomlyTicking(BlockState state) {
		return WeatheringCopper.getNext(state.getBlock()).isPresent();
	}

	@Override
	public WeatherState getAge() {
		return oxidationLevel;
	}
}
