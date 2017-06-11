package shadows.attained.init;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops2;
import shadows.attained.blocks.BlockBulb;
import shadows.attained.blocks.BlockCreator;
import shadows.attained.blocks.BlockPlant;
import shadows.attained.blocks.BlockVitalized;
import shadows.attained.items.ItemEssence;
import shadows.attained.items.ItemSeed;

public class ModRegistry {

	public static final Block BULB = new BlockBulb("bulb");
	public static final Block SOIL = new BlockVitalized("soil");
	public static final Block PLANT = new BlockPlant();
	public static final Item SEED = new ItemSeed();
	public static final Item ESSENCE = new ItemEssence();
	public static final Block CREATOR = new BlockCreator();

	public static void init() {

	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		((BlockBulb) BULB).initModel();
		((BlockVitalized) SOIL).initModel();
		((BlockPlant) PLANT).initModel();
		((ItemSeed) SEED).initModel();
		((ItemEssence) ESSENCE).initModel();
		((BlockCreator) CREATOR).initModel();
	}

	public static CreativeTabs AD2_TAB = new CreativeTabs(AttainedDrops2.MODID) {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(BULB);
		}

		@Override
		public void displayAllRelevantItems(NonNullList<ItemStack> list) {
			list.add(new ItemStack(ESSENCE));
			list.add(new ItemStack(SEED));
			BULB.getSubBlocks(this, list);
			SOIL.getSubBlocks(this, list);
			list.add(new ItemStack(CREATOR));
		}

	};

}
