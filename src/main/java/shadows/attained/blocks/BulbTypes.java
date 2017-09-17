package shadows.attained.blocks;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public enum BulbTypes {
	BLAZE(new ItemStack(Items.BLAZE_ROD), 0),
	PEARL(new ItemStack(Items.ENDER_PEARL), 1),
	BONE(new ItemStack(Items.BONE), 2),
	SLIME(new ItemStack(Items.SLIME_BALL), 3),
	FLESH(new ItemStack(Items.ROTTEN_FLESH), 4),
	TEAR(new ItemStack(Items.GHAST_TEAR), 5),
	GUNPOWDER(new ItemStack(Items.GUNPOWDER), 6),
	STRING(new ItemStack(Items.STRING), 7),
	EYE(new ItemStack(Items.SPIDER_EYE), 8),
	PRISMARINE(new ItemStack(Items.PRISMARINE_SHARD), 9),
	WITHER(new ItemStack(Items.SKULL, 1, 1), 10),
	SHULKER(new ItemStack(Items.SHULKER_SHELL), 11),
	LEATHER(new ItemStack(Items.LEATHER), 12),
	FEATHER(new ItemStack(Items.FEATHER), 13),
	PRISMARINE_C(new ItemStack(Items.PRISMARINE_CRYSTALS), 14),;
	private final ItemStack drop;
	private final int meta;

	BulbTypes(ItemStack drop, int meta) {
		this.drop = drop;
		this.meta = meta;
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
		return meta;
	}

}
