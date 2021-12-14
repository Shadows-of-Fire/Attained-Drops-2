package shadows.attained.blocks;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import shadows.attained.api.IAttainedType;

public enum DefaultTypes implements IAttainedType {

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

	public static final DefaultTypes[] VALUES = values();

	private final ItemStack drop;
	private final DyeColor color;

	DefaultTypes(ItemStack drop, DyeColor color) {
		this.drop = drop;
		this.color = color;
	}

	@Override
	public ItemStack getDrop() {
		return drop;
	}

	@Override
	public int getColor() {
		return color.getFireworkColor();
	}

}
