package shadows.attained.init;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shadows.attained.AttainedDrops2;
import shadows.attained.blocks.BlockBulb;
import shadows.attained.blocks.BlockCreator;
import shadows.attained.blocks.BlockPlant;
import shadows.attained.blocks.BlockVitalized;
import shadows.attained.items.ItemEssence;
import shadows.attained.items.ItemSeed;

public class ModRegistry {

	public static final Block BULB = new BlockBulb("bulb");
	public static final Block SOIL = new BlockVitalized();
	public static final Block PLANT = new BlockPlant();
	public static final Item SEED = new ItemSeed();
	public static final Item ESSENCE = new ItemEssence();
	public static final Block CREATOR = new BlockCreator();

	public void initRecipes() {
		AttainedDrops2.HELPER.addShapeless(new ItemStack(ModRegistry.SEED, 2), ModRegistry.ESSENCE, Items.APPLE, Items.WHEAT_SEEDS);
		AttainedDrops2.HELPER.addShaped(ModRegistry.CREATOR, 3, 3, null, ModRegistry.SEED, null, Items.DIAMOND, Blocks.DIRT, Items.DIAMOND, null, Items.WATER_BUCKET, null);
	}

	@SubscribeEvent
	public void onBlockRegistry(Register<Block> e) {
		e.getRegistry().registerAll(AttainedDrops2.INFO.getBlockList().toArray(new Block[0]));
	}

	@SubscribeEvent
	public void onItemRegistry(Register<Item> e) {
		e.getRegistry().registerAll(AttainedDrops2.INFO.getItemList().toArray(new Item[0]));
	}

	@SubscribeEvent
	public void onRecipeRegistry(Register<IRecipe> e) {
		initRecipes();
		e.getRegistry().registerAll(AttainedDrops2.INFO.getRecipeList().toArray(new IRecipe[0]));
	}

}
