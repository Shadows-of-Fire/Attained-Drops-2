package shadows.attained.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import shadows.attained.AttainedDrops2;
import shadows.attained.init.ModRegistry;

public class AD2Tab extends CreativeTabs {

	public AD2Tab() {
		super(AttainedDrops2.MODID);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(ModRegistry.BULB);
	}

	@Override
	public void displayAllRelevantItems(NonNullList<ItemStack> list) {
		ModRegistry.BULB.getSubBlocks(this, list);
		ModRegistry.SOIL.getSubBlocks(this, list);
		list.add(new ItemStack(ModRegistry.ESSENCE));
		list.add(new ItemStack(ModRegistry.SEED));
		list.add(new ItemStack(ModRegistry.CREATOR));
	}

}
