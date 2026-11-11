package net.fellter.vanillalayerplus.registry;

import java.util.List;
import java.util.Objects;
import net.minecraft.world.level.block.Block;

public class RegistryArgs {
	public Boolean transparent = false;
	public Boolean translucent = false;
	public Boolean grassTinted = false;
	public Boolean foliageTinted = false;
	public Block flattened;
	public Block stripped;
	public Block tilled;
	public Integer burn;
	public Integer spread;
	public Block exposed;
	public Block weathered;
	public Block oxidized;
	public Block waxed;
	public Block exposedWaxed;
	public Block weatheredWaxed;
	public Block oxidizedWaxed;
	public List<Block> oxidizables;
	// Vanilla burn times of the fuel parents, in ticks at the default base smelt time (200).
	public static final int FUEL_WOOD_BLOCKS = 300;
	public static final int FUEL_WOOL = 100;
	public static final int FUEL_COAL_BLOCK = 16000;
	public static final int FUEL_DRIED_KELP_BLOCK = 4001;

	// Burn time of the parent block; the layer burns 1/8 of it.
	public Integer fuel;

	public RegistryArgs() {
	}

	public RegistryArgs transparent() {
		this.transparent = true;
		return this;
	}

	public RegistryArgs translucent() {
		this.translucent = true;
		return this;
	}

	public RegistryArgs tillable(Block tilled) {
		this.tilled = tilled;
		return this;
	}

	public RegistryArgs stripped(Block stripped) {
		this.stripped = stripped;
		return this;
	}

	public RegistryArgs grassTinted() {
		this.grassTinted = true;
		return this;
	}

	public RegistryArgs foliageTinted() {
		this.foliageTinted = true;
		return this;
	}

	public RegistryArgs flattenable(Block flattened) {
		this.flattened = flattened;
		return this;
	}

	public RegistryArgs flammable(Integer burn, Integer spread) {
		this.burn = burn;
		this.spread = spread;
		return this;
	}

	public RegistryArgs oxidizable(Block exposed, Block weathered, Block oxidized, Block waxed, Block exposedWaxed, Block weatheredWaxed, Block oxidizedWaxed) {
		this.exposed = Objects.requireNonNull(exposed);
		this.weathered = Objects.requireNonNull(weathered);
		this.oxidized = Objects.requireNonNull(oxidized);
		this.waxed = Objects.requireNonNull(waxed);
		this.exposedWaxed = Objects.requireNonNull(exposedWaxed);
		this.weatheredWaxed = Objects.requireNonNull(weatheredWaxed);
		this.oxidizedWaxed = Objects.requireNonNull(oxidizedWaxed);
		this.oxidizables = List.of(exposed, weathered, oxidized, waxed, exposedWaxed, weatheredWaxed, oxidizedWaxed);
		return this;
	}

	public RegistryArgs fuel(int parentBurnTime) {
		this.fuel = parentBurnTime;
		return this;
	}
}

