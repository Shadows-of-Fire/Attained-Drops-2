package shadows.attained.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import shadows.attained.AttainedDrops;
import shadows.attained.AttainedRegistry;
import shadows.attained.api.PlantingRegistry;
import shadows.attained.blocks.DefaultTypes;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

	static ResourceLocation plugin = new ResourceLocation(AttainedDrops.MODID, "plugin");

	@Override
	public ResourceLocation getPluginUid() {
		return plugin;
	}

	@Override
	public void registerRecipes(IRecipeRegistration registry) {
		registry.addIngredientInfo(new ItemStack(AttainedRegistry.LIFE_ESSENCE), VanillaTypes.ITEM, new TranslationTextComponent("jei.attained_drops.desc.essence"));
		registry.addIngredientInfo(new ItemStack(PlantingRegistry.SOILS.get(DefaultTypes.NONE)), VanillaTypes.ITEM, new TranslationTextComponent("jei.attained_drops.desc.vitalized"));
		registry.addIngredientInfo(new ItemStack(AttainedRegistry.VITALITY_SPREADER), VanillaTypes.ITEM, new TranslationTextComponent("jei.attained_drops.desc.planter"));
		registry.addRecipes(PlantingRegistry.BULBS.keySet(), AttainedCategory.UID);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
		reg.addRecipeCatalyst(new ItemStack(AttainedRegistry.SEED), AttainedCategory.UID);
		reg.addRecipeCatalyst(new ItemStack(PlantingRegistry.SOILS.get(DefaultTypes.NONE)), AttainedCategory.UID);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration reg) {
		reg.addRecipeCategories(new AttainedCategory(reg.getJeiHelpers().getGuiHelper()));
	}

}
