package shadows.attained.integration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;
import shadows.attained.blocks.BulbTypes;
import shadows.attained.init.ModRegistry;

@JEIPlugin
public class JEI implements IModPlugin {

	public JEI(){};
	
	@Override
	public void register(@Nonnull IModRegistry registry) {
		List<ItemStack> list = new ArrayList<ItemStack>();
		registry.addIngredientInfo(new ItemStack(ModRegistry.ESSENCE), ItemStack.class, "jei.attaineddrops2.desc.essence");
		registry.addIngredientInfo(new ItemStack(ModRegistry.SEED), ItemStack.class, "jei.attaineddrops2.desc.seed");
		for(int i = 1; i <= BulbTypes.values().length; i++){
			list.add(new ItemStack(ModRegistry.SOIL, 1, i));
		}
		registry.addIngredientInfo(list, ItemStack.class, "jei.attaineddrops2.desc.vitalized");
		registry.addIngredientInfo(new ItemStack(ModRegistry.SOIL), ItemStack.class, "jei.attaineddrops2.desc.vitalized2");
		registry.addIngredientInfo(new ItemStack(ModRegistry.CREATOR), ItemStack.class, "jei.attaineddrops2.desc.planter");
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