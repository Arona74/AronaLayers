package io.arona74.aronalayers.datagen;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import io.arona74.aronalayers.compat.Compat;
import io.arona74.aronalayers.registry.ModRegistryArgs;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;

/**
 * Generates one burn-time entry per parent fuel type used by a layer: the parent's
 * vanilla entry divided by 8. See {@link Compat#registerLayerFuel}.
 */
public class ModFuelProvider extends FabricDynamicRegistryProvider {
	public ModFuelProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(HolderLookup.Provider registries, Entries entries) {
		HolderLookup.RegistryLookup<ContextIntProvider> providers = registries.lookupOrThrow(Registries.CONTEXT_INT_PROVIDER);

		ModRegistryArgs.REGISTRY_ARGS.values().stream().map(args -> args.fuel).filter(Objects::nonNull).distinct().forEach(parentBurnTime ->
				entries.add(Compat.layerBurnTime(parentBurnTime),
						ContextIntProviders.div(providers.getOrThrow(Compat.burnTime(parentBurnTime)), ContextIntProviders.exactly(8)).value()));
	}

	@Override
	public String getName() {
		return "Layer Fuel Values";
	}
}
