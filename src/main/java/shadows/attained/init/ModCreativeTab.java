package shadows.attained.init;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.api.IBulb;
import shadows.attained.api.IVitalizedSoil;
import shadows.attained.blocks.BlockBulb;
import shadows.attained.blocks.BlockVitalized;
import shadows.attained.util.AD2Util;

public class ModCreativeTab extends CreativeTabs {

	public ModCreativeTab(String label) {
		super(label);

	}

	@SideOnly(Side.CLIENT)
	@Override
	public Item getTabIconItem() {
		return ModRegistry.ITEM_SEED;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(List<ItemStack> list) {
		list.add(new ItemStack(ModRegistry.BLOCK_SOILCREATOR));
		list.add(new ItemStack(ModRegistry.VITALIZED_BASE));
		list.add(new ItemStack(ModRegistry.BLOCK_PLANT));
		list.add(new ItemStack(ModRegistry.ITEM_SEED));
		list.add(new ItemStack(ModRegistry.ITEM_ESSENCE));

		for (IBulb bulb : AD2Util.getBulbs().keySet()) {
			list.add(new ItemStack((BlockBulb) bulb));
		}

		for (IVitalizedSoil soil : AD2Util.getSoils().keySet()) {
			list.add(new ItemStack((BlockVitalized) soil));
		}

	}

}
