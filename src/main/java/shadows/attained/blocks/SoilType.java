package shadows.attained.blocks;

import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public enum SoilType {

	NONE(ItemStack.EMPTY, DyeColor.BROWN),
	BLAZE(new ItemStack(Items.BLAZE_ROD), DyeColor.ORANGE),
	PEARL(new ItemStack(Items.ENDER_PEARL), DyeColor.GRAY),
	BONE(new ItemStack(Items.BONE), DyeColor.WHITE),
	SLIME(new ItemStack(Items.SLIME_BALL), DyeColor.GREEN),
	FLESH(new ItemStack(Items.ROTTEN_FLESH), DyeColor.RED),
	TEAR(new ItemStack(Items.GHAST_TEAR), DyeColor.LIGHT_GRAY),
	GUNPOWDER(new ItemStack(Items.GUNPOWDER), DyeColor.GRAY),
	STRING(new ItemStack(Items.STRING), DyeColor.WHITE),
	EYE(new ItemStack(Items.SPIDER_EYE), DyeColor.BLACK),
	PRISMARINE(new ItemStack(Items.PRISMARINE_SHARD), DyeColor.CYAN),
	WITHER(new ItemStack(Blocks.WITHER_SKELETON_SKULL), DyeColor.BLACK),
	SHULKER(new ItemStack(Items.SHULKER_SHELL), DyeColor.PURPLE),
	LEATHER(new ItemStack(Items.LEATHER), DyeColor.BROWN),
	FEATHER(new ItemStack(Items.FEATHER), DyeColor.WHITE),
	PRISMARINE_CRYSTAL(new ItemStack(Items.PRISMARINE_CRYSTALS), DyeColor.CYAN);

	public static final SoilType[] VALUES = values();

	private final ItemStack drop;
	private final DyeColor color;

	SoilType(ItemStack drop, DyeColor color) {
		this.drop = drop;
		this.color = color;
	}

	public ItemStack getDrop() {
		return drop;
	}

	public DyeColor getColor() {
		return color;
	}

	public static SoilType byStack(ItemStack stack) {
		for (SoilType s : VALUES) {
			if (stack.isItemEqual(s.getDrop())) return s;
		}
		return null;
	}

}
