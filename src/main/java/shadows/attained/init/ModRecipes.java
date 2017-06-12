package shadows.attained.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import shadows.attained.util.RecipeHelper;

public class ModRecipes {

	public static void init() {
		RecipeHelper.addShapeless(new ItemStack(ModRegistry.SEED, 2), ModRegistry.ESSENCE, Items.APPLE, Items.WHEAT_SEEDS);
		RecipeHelper.addShaped(new ItemStack(ModRegistry.CREATOR), 3, 3, null, ModRegistry.SEED, null,
				Items.DIAMOND, Blocks.DIRT, Items.DIAMOND, null, Items.WATER_BUCKET, null);
	}
}
