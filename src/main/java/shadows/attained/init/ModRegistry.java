package shadows.attained.init;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import shadows.attained.AttainedDrops2;
import shadows.attained.blocks.BlockBulb;
import shadows.attained.blocks.BlockCreator;
import shadows.attained.blocks.BlockPlant;
import shadows.attained.blocks.BlockVitalized;
import shadows.attained.items.ItemEssence;
import shadows.attained.items.ItemSeed;
import shadows.attained.util.RecipeHelper;

public class ModRegistry {

	public static final Block BULB = new BlockBulb("bulb");
	public static final Block SOIL = new BlockVitalized("soil");
	public static final Block PLANT = new BlockPlant();
	public static final Item SEED = new ItemSeed();
	public static final Item ESSENCE = new ItemEssence();
	public static final Block CREATOR = new BlockCreator();

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

	public void initRecipes() {
		RecipeHelper.addShapeless(new ItemStack(ModRegistry.SEED, 2), ModRegistry.ESSENCE, Items.APPLE,
				Items.WHEAT_SEEDS);
		RecipeHelper.addShaped(ModRegistry.CREATOR, 3, 3, null, ModRegistry.SEED, null, Items.DIAMOND,
				Blocks.DIRT, Items.DIAMOND, null, Items.WATER_BUCKET, null);
	}

	@SubscribeEvent
	public void onBlockRegistry(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> reg = event.getRegistry();
		for (Block block : DataLists.BLOCKS) {
			reg.register(block);
		}
	}

	@SubscribeEvent
	public void onItemRegistry(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> reg = event.getRegistry();
		for (Item item : DataLists.ITEMS) {
			reg.register(item);
		}
	}

	@SubscribeEvent
	public void onRecipeRegistry(RegistryEvent.Register<IRecipe> event) {
		initRecipes();
		IForgeRegistry<IRecipe> reg = event.getRegistry();
		for (IRecipe rec : DataLists.RECIPES) {
			reg.register(rec);
		}
	}

}
