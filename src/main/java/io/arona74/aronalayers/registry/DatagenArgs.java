package io.arona74.aronalayers.registry;

import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class DatagenArgs {
	public ArrayList<TagKey<Block>> blockTags = new ArrayList<>();
	public Block parentBlock;
	public TextureMapping textureMap;
	public Block fullTextureBlock;
	public Boolean y15 = false;
	public Boolean needsSilkTouch = false;
	public ArrayList<ItemLike> stonecuttingInput = new ArrayList<>();

	public DatagenArgs() {
	}

	@SafeVarargs
	public final DatagenArgs blockTags(TagKey<Block>... blockTags) {
		this.blockTags.addAll(Arrays.asList(blockTags));
		return this;
	}

	public DatagenArgs needsSilkTouch() {
		this.needsSilkTouch = true;
		return this;
	}

	@SafeVarargs
	public final DatagenArgs removeBlockTag(TagKey<Block>... blockTags) {
		for (TagKey<Block> tagKey : blockTags) {
			this.blockTags.remove(tagKey);
		}

		return this;
	}

	public DatagenArgs stonecutting(ItemLike... stonecuttingInput) {
		this.stonecuttingInput.add(this.parentBlock);
		this.stonecuttingInput.addAll(Arrays.asList(stonecuttingInput));
		return this;
	}

	public DatagenArgs stonecutting() {
		this.stonecuttingInput.add(this.parentBlock);
		return this;
	}

	public DatagenArgs parentBlock(Block fullBlock) {
		this.parentBlock = fullBlock;
		this.fullTextureBlock = fullBlock;
		return this;
	}

	public DatagenArgs parentBlock(Block parentBlock, Block fullTextureBlock) {
		this.parentBlock = parentBlock;
		this.fullTextureBlock = fullTextureBlock;
		return this;
	}

	public DatagenArgs textureMap(TextureMapping textureMap) {
		this.textureMap = textureMap;
		return this;
	}

	public DatagenArgs y15() {
		this.y15 = true;
		return this;
	}
}
