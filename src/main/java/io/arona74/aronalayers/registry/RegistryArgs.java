package io.arona74.aronalayers.registry;

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
	/**
	 * A fuel parent: its vanilla burn time in ticks at the default smelt time of 200 ticks, and
	 * the name of its burn time entry, which 26.3+ keeps as minecraft:cooking/{@code cookingTime}.
	 */
	public record Fuel(String cookingTime, int burnTicks) {
	}

	public static final Fuel FUEL_WOOD_BLOCKS = new Fuel("time_wood_blocks", 300);
	public static final Fuel FUEL_WOOL = new Fuel("time_wool", 100);
	public static final Fuel FUEL_COAL_BLOCK = new Fuel("time_coal_block", 16000);
	public static final Fuel FUEL_DRIED_KELP_BLOCK = new Fuel("time_dried_kelp_block", 4001);

	// Fuel of the parent block; the layer burns 1/8 as long.
	public Fuel fuel;

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

	/** Fire odds, as vanilla gives them to the parent block: catching fire ({@code burn}) and burning away ({@code spread}). */
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

	public RegistryArgs fuel(Fuel parent) {
		this.fuel = Objects.requireNonNull(parent);
		return this;
	}
}

