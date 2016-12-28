package shadows.attained.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import shadows.attained.init.ModConfig.ConfigOptions;

public class ModRecipes {

	public static void init() {
		if (ConfigOptions.VAPOUR_MODE) {
			GameRegistry.addShapelessRecipe(new ItemStack(ModRegistry.ITEM_SEED), new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()), Items.APPLE, Items.WHEAT_SEEDS);
			GameRegistry.addShapelessRecipe(new ItemStack(ModRegistry.BLOCK_SOILCREATOR), Items.WHEAT_SEEDS, new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()), Items.WATER_BUCKET, new ItemStack(Blocks.DIRT));
			return;
		}
		GameRegistry.addShapelessRecipe(new ItemStack(ModRegistry.ITEM_SEED, 2), ModRegistry.ITEM_ESSENCE, Items.APPLE, Items.WHEAT_SEEDS);
		GameRegistry.addShapedRecipe(new ItemStack(ModRegistry.BLOCK_SOILCREATOR), " L ", "FSF", " L ", 'F', ModRegistry.ITEM_SEED, 'L', Items.EGG, 'S', Blocks.DIRT);
	}
}
