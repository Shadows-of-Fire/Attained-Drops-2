package shadows.attained.integration;

import javax.annotation.Nonnull;

import mezz.jei.api.IItemBlacklist;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.Method;
import shadows.attained.api.IVitalizedSoil;
import shadows.attained.init.ModGlobals;
import shadows.attained.init.ModRegistry;
import shadows.attained.util.AD2Util;

/**
 * @author p455w0rd
 *
 */
@Interface(iface = "mezz.jei.api.IModPlugin", modid = ModGlobals.MODID_JEI, striprefs = true)
@JEIPlugin
public class JEI implements IModPlugin {

	@Override
	@Method(modid = ModGlobals.MODID_JEI)
	public void register(@Nonnull IModRegistry registry) {
		IJeiHelpers helpers = registry.getJeiHelpers();
		IItemBlacklist blacklist = helpers.getItemBlacklist();
		for (IVitalizedSoil soil : ModRegistry.getSoilRegistry().keySet()) {
			blacklist.addItemToBlacklist(AD2Util.getSoilStack(soil));
			blacklist.addItemToBlacklist(AD2Util.getBulbStack(AD2Util.getBulbFromSoil(soil)));
		}
		blacklist.addItemToBlacklist(new ItemStack(ModRegistry.BLOCK_PLANT));
		registry.addDescription(new ItemStack(ModRegistry.ITEM_ESSENCE), "jei.attaineddrops.desc.essence");
		registry.addDescription(new ItemStack(ModRegistry.VITALIZED_BASE), "jei.attaineddrops.desc.vitalized");
		registry.addDescription(new ItemStack(ModRegistry.ITEM_SEED), "jei.attaineddrops.desc.seed");
		registry.addDescription(new ItemStack(ModRegistry.BLOCK_SOILCREATOR), "jei.attaineddrops.desc.planter");
	}

	@Override
	@Method(modid = ModGlobals.MODID_JEI)
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
	}

	@Override
	@Method(modid = ModGlobals.MODID_JEI)
	public void registerIngredients(IModIngredientRegistration registry) {
	}

	@Method(modid = ModGlobals.MODID_JEI)
	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
	}

}
