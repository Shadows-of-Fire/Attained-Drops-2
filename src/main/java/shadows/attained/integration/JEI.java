package shadows.attained.integration;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;
import shadows.attained.api.IVitalizedSoil;
import shadows.attained.init.ModRegistry;
import shadows.attained.util.AD2Util;

/**
 * @author p455w0rd
 *
 */
@JEIPlugin
public class JEI implements IModPlugin {

	@Override
	public void register(@Nonnull IModRegistry registry) {
		IJeiHelpers helpers = registry.getJeiHelpers();
		IIngredientBlacklist blacklist = helpers.getIngredientBlacklist();
		for (IVitalizedSoil soil : ModRegistry.getSoilRegistry().keySet()) {
			blacklist.addIngredientToBlacklist(AD2Util.getSoilStack(soil));
			blacklist.addIngredientToBlacklist(AD2Util.getBulbStack(AD2Util.getBulbFromSoil(soil)));
		}
		blacklist.addIngredientToBlacklist(new ItemStack(ModRegistry.BLOCK_PLANT));
		registry.addDescription(new ItemStack(ModRegistry.ITEM_ESSENCE), "jei.attaineddrops.desc.essence");
		registry.addDescription(new ItemStack(ModRegistry.VITALIZED_BASE), "jei.attaineddrops.desc.vitalized");
		registry.addDescription(new ItemStack(ModRegistry.ITEM_SEED), "jei.attaineddrops.desc.seed");
		registry.addDescription(new ItemStack(ModRegistry.BLOCK_SOILCREATOR), "jei.attaineddrops.desc.planter");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
	}

}