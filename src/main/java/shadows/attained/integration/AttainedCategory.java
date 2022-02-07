package shadows.attained.integration;

import java.util.Arrays;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import shadows.attained.AttainedDrops;
import shadows.attained.AttainedRegistry;
import shadows.attained.api.IAttainedType;
import shadows.attained.api.PlantingRegistry;

public class AttainedCategory implements IRecipeCategory<IAttainedType> {

	public static final ResourceLocation UID = new ResourceLocation(AttainedDrops.MODID, "fletching");
	public static final ResourceLocation TEXTURES = new ResourceLocation(AttainedDrops.MODID, "textures/gui/jei_screen.png");

	private final IDrawable background;
	private final IDrawable icon;
	private final Component localizedName;

	public AttainedCategory(IGuiHelper guiHelper) {
		ResourceLocation location = TEXTURES;
		background = guiHelper.createDrawable(location, 0, 0, 98, 54);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(AttainedRegistry.SEED));
		localizedName = new TranslatableComponent("attained_drops.growing");
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public Class<IAttainedType> getRecipeClass() {
		return IAttainedType.class;
	}

	@Override
	public Component getTitle() {
		return localizedName;
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public void setIngredients(IAttainedType recipe, IIngredients ing) {
		ing.setOutputs(VanillaTypes.ITEM, Arrays.asList(new ItemStack(PlantingRegistry.BULBS.get(recipe)), recipe.getDrop()));
		ing.setInput(VanillaTypes.ITEM, recipe.getDrop());
	}

	@Override
	public void setRecipe(IRecipeLayout layout, IAttainedType recipe, IIngredients ing) {
		IGuiItemStackGroup stacks = layout.getItemStacks();
		stacks.init(0, false, 76, 18);
		stacks.init(1, true, 0, 18);
		stacks.set(ing);
	}

}
