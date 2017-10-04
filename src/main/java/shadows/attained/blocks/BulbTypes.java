package shadows.attained.blocks;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

public enum BulbTypes {
	BLAZE(new ItemStack(Items.BLAZE_ROD), EnumDyeColor.ORANGE),
	PEARL(new ItemStack(Items.ENDER_PEARL), EnumDyeColor.GRAY),
	BONE(new ItemStack(Items.BONE), EnumDyeColor.WHITE),
	SLIME(new ItemStack(Items.SLIME_BALL), EnumDyeColor.GREEN),
	FLESH(new ItemStack(Items.ROTTEN_FLESH), EnumDyeColor.RED),
	TEAR(new ItemStack(Items.GHAST_TEAR), EnumDyeColor.SILVER),
	GUNPOWDER(new ItemStack(Items.GUNPOWDER), EnumDyeColor.GRAY),
	STRING(new ItemStack(Items.STRING), EnumDyeColor.WHITE),
	EYE(new ItemStack(Items.SPIDER_EYE), EnumDyeColor.BLACK),
	PRISMARINE(new ItemStack(Items.PRISMARINE_SHARD), EnumDyeColor.CYAN),
	WITHER(new ItemStack(Items.SKULL, 1, 1), EnumDyeColor.BLACK),
	SHULKER(new ItemStack(Items.SHULKER_SHELL), EnumDyeColor.PURPLE),
	LEATHER(new ItemStack(Items.LEATHER), EnumDyeColor.BROWN),
	FEATHER(new ItemStack(Items.FEATHER), EnumDyeColor.WHITE),
	PRISMARINE_C(new ItemStack(Items.PRISMARINE_CRYSTALS), EnumDyeColor.CYAN),;

	private final ItemStack drop;
	private final EnumDyeColor color;

	BulbTypes(ItemStack drop, EnumDyeColor color) {
		this.drop = drop;
		this.color = color;
	}

	public static int getMetaFromStack(ItemStack stack) {
		for (int i = 0; i < BulbTypes.values().length; i++) {
			ItemStack k = BlockBulb.lookup.get(i);
			if (ItemStack.areItemsEqual(stack, k)) return i;
		}
		return -1;
	}

	public ItemStack getDrop() {
		return drop;
	}

	public int getMeta() {
		return ordinal();
	}

	public EnumDyeColor getColor() {
		return color;
	}

}
