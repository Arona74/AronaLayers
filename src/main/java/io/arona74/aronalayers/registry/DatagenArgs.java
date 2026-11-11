package io.arona74.aronalayers.registry;

import java.util.ArrayList;
import java.util.Arrays;

import io.arona74.aronalayers.compat.Compat;
import io.arona74.aronalayers.util.Textures;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/** Datagen description of one layer. Vanilla blocks are given by id, so the table is the same for every Minecraft version. */
public class DatagenArgs {
	public ArrayList<TagKey<Block>> blockTags = new ArrayList<>();
	public Block parentBlock;
	public Textures textureMap;
	public Block fullTextureBlock;
	public Boolean y15 = false;
	public Boolean needsSilkTouch = false;
	// Item model tint, for versions whose item models carry one (1.21.4+): a fixed ARGB color, or the grass color.
	public Integer itemTint;
	public Boolean grassItemTint = false;
	public ArrayList<Block> stonecuttingInput = new ArrayList<>();

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

	public DatagenArgs stonecutting(String... stonecuttingInput) {
		this.stonecuttingInput.add(this.parentBlock);

		for (String input : stonecuttingInput) {
			this.stonecuttingInput.add(Compat.vanillaBlock(input));
		}

		return this;
	}

	public DatagenArgs stonecutting() {
		this.stonecuttingInput.add(this.parentBlock);
		return this;
	}

	public DatagenArgs parentBlock(String fullBlock) {
		this.parentBlock = Compat.vanillaBlock(fullBlock);
		this.fullTextureBlock = this.parentBlock;
		return this;
	}

	public DatagenArgs parentBlock(String parentBlock, String fullTextureBlock) {
		this.parentBlock = Compat.vanillaBlock(parentBlock);
		this.fullTextureBlock = Compat.vanillaBlock(fullTextureBlock);
		return this;
	}

	public DatagenArgs textureMap(Textures textureMap) {
		this.textureMap = textureMap;
		return this;
	}

	public DatagenArgs y15() {
		this.y15 = true;
		return this;
	}

	public DatagenArgs itemTint(int argb) {
		this.itemTint = argb;
		return this;
	}

	public DatagenArgs grassItemTint() {
		this.grassItemTint = true;
		return this;
	}
}
