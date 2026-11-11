package net.fellter.vanillalayerplus.mixin;

import java.util.function.Consumer;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HoeItem.class)
public abstract class MixinHoeItem {
	@ModifyReturnValue(method = "changeIntoState", at = @At("RETURN"))
	private static Consumer<UseOnContext> fellter$createTillAction(Consumer<UseOnContext> original, BlockState stateTo) {
		return context -> {
			context.getLevel().setBlock(context.getClickedPos(), stateTo.getBlock().withPropertiesOf(context.getLevel().getBlockState(context.getClickedPos())), 11);
			context.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, context.getClickedPos(), GameEvent.Context.of(context.getPlayer(), stateTo));
		};
	}

	@ModifyReturnValue(method = "changeIntoStateAndDropItem", at = @At("RETURN"))
	private static Consumer<UseOnContext> fellter$createTillAndDropAction(Consumer<UseOnContext> original, BlockState result, ItemLike droppedItem) {
		return context -> {
			context.getLevel().setBlock(context.getClickedPos(), result.getBlock().withPropertiesOf(context.getLevel().getBlockState(context.getClickedPos())), 11);
			context.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, context.getClickedPos(), GameEvent.Context.of(context.getPlayer(), result));
			Block.popResourceFromFace(context.getLevel(), context.getClickedPos(), context.getClickedFace(), new ItemStack(droppedItem));
		};
	}
}
