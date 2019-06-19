package shadows.attained.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;
import shadows.attained.AttainedDrops;

@SuppressWarnings("deprecation")
public class RecipeHelper implements IResourceManagerReloadListener {

	private int j = 0;
	private final String modid;
	private final String modname;
	private final List<IRecipe<?>> recipes = new LinkedList<>();

	public RecipeHelper(String modid, String modname) {
		this.modid = modid;
		this.modname = modname;
		MinecraftForge.EVENT_BUS.addListener(this::serverStart);
	}

	public void addRecipe(IRecipe<?> rec) {
		recipes.add(rec);
	}

	public void addShapeless(Object output, Object... inputs) {
		addRecipe(new ShapelessRecipe(new ResourceLocation(modid + ":" + j++), modid, makeStack(output), createInput(false, inputs)));
	}

	public void addShaped(Object output, int width, int height, Object... input) {
		addRecipe(genShaped(makeStack(output), width, height, input));
	}

	public ShapedRecipe genShaped(ItemStack output, int l, int w, Object... input) {
		if (l * w != input.length) throw new UnsupportedOperationException("Attempted to add invalid shaped recipe.  Complain to the author of " + modname);
		return new ShapedRecipe(new ResourceLocation(modid + ":" + j++), modid, l, w, createInput(true, input), output);
	}

	public NonNullList<Ingredient> createInput(boolean allowEmpty, Object... input) {
		NonNullList<Ingredient> inputL = NonNullList.create();
		for (int i = 0; i < input.length; i++) {
			Object k = input[i];
			//if (k instanceof String) inputL.add(i, new TagIngredient(ItemTags.getCollection().get(new ResourceLocation((String) k))));
			if (k instanceof ItemStack && !((ItemStack) k).isEmpty()) inputL.add(i, CachedIngredient.create((ItemStack) k));
			else if (k instanceof IForgeRegistryEntry) inputL.add(i, CachedIngredient.create(makeStack(k)));
			else if (k instanceof Ingredient) inputL.add(i, (Ingredient) k);
			else if (allowEmpty) inputL.add(i, Ingredient.EMPTY);
			else throw new UnsupportedOperationException("Attempted to add invalid shapeless recipe.  Complain to the author of " + modname);
		}
		return inputL;
	}

	public void addSimpleShapeless(Object output, Object input, int numInputs) {
		addShapeless(output, NonNullList.withSize(numInputs, makeStack(input)));
	}

	public List<IRecipe<?>> getRecipes() {
		return recipes;
	}

	public static ItemStack makeStack(Object thing, int size) {
		if (thing instanceof ItemStack) return (ItemStack) thing;
		if (thing instanceof Item) return new ItemStack((Item) thing, size);
		if (thing instanceof Block) return new ItemStack((Block) thing, size);
		throw new IllegalArgumentException("Attempted to create an ItemStack from something that cannot be converted: " + thing);
	}

	public static ItemStack makeStack(Object thing) {
		return makeStack(thing, 1);
	}

	public static class CachedIngredient extends Ingredient {

		public static Int2ObjectMap<CachedIngredient> ing = new Int2ObjectOpenHashMap<>();

		protected CachedIngredient(ItemStack... matches) {
			super(Arrays.stream(matches).map(s -> new SingleItemList(s)));
			if (matches.length == 1) ing.put(RecipeItemHelper.pack(matches[0]), this);
		}

		public static CachedIngredient create(ItemStack... matches) {
			if (matches.length == 1) {
				CachedIngredient coi = ing.get(RecipeItemHelper.pack(matches[0]));
				return coi != null ? coi : new CachedIngredient(matches);
			} else return new CachedIngredient(matches);
		}

		@Override
		public IIngredientSerializer<? extends Ingredient> getSerializer() {
			return CraftingHelper.INGREDIENT_VANILLA;
		}

	}

	boolean loaded = false;

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		if (!loaded || !ServerLifecycleHooks.getCurrentServer().getWorld(DimensionType.OVERWORLD).getWorldInfo().getDisabledDataPacks().contains("mod:" + modid)) {
			recipes.forEach(ServerLifecycleHooks.getCurrentServer().getRecipeManager()::addRecipe);
			loaded = true;
			AttainedDrops.LOGGER.info("Loaded {} recipes", recipes.size());
		}
	}

	@SubscribeEvent
	public void serverStart(FMLServerAboutToStartEvent e) {
		e.getServer().getResourceManager().func_219534_a(this);
		this.onResourceManagerReload(null);
	}

}