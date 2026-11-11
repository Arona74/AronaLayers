package net.fellter.vanillalayerplus.mixin;

import java.util.Map;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ShovelItem.class)
public abstract class MixinShovelItem {
	@Shadow
	@Final
	protected static Map<Block, BlockState> FLATTENABLES;

	@ModifyVariable(method = "useOn", at = @At(value = "STORE"), ordinal = 1)
	private BlockState path(BlockState value, UseOnContext context) {
		BlockState state = FLATTENABLES.get(context.getLevel().getBlockState(context.getClickedPos()).getBlock());
		BlockState stateFrom = context.getLevel().getBlockState(context.getClickedPos());

		if (state != null) {
			for (Property<?> property : state.getBlock().getStateDefinition().getProperties()) {
				state = withProperty(state, property, stateFrom);
			}

			return state;
		}

		return value;
	}

	@Unique
	private static <T extends Comparable<T>> BlockState withProperty(BlockState to, Property<T> property, BlockState from) {
		return to.setValue(property, from.getValue(property));
	}
}
