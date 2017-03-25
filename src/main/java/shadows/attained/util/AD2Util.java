package shadows.attained.util;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.api.IBulb;
import shadows.attained.api.IVitalizedSoil;
import shadows.attained.blocks.BlockPlant;
import shadows.attained.init.ModRegistry;

/**
 * @author p455w0rd
 *
 */
public class AD2Util {

	public static Map<IBulb, IVitalizedSoil> getBulbs() {
		return ModRegistry.getBulbRegistry();
	}

	public static Map<IVitalizedSoil, IBulb> getSoils() {
		return ModRegistry.getSoilRegistry();
	}

	public static Map<IBulb, ItemStack> getDrops() {
		return ModRegistry.getDropsRegistry();
	}

	/**
	 * @param bulb
	 *            - Your implementation of {@link shadows.attained.api.IBulb
	 *            IBulb} - recommended to extend BlockBulb and pass in your
	 *            modid. You will then not need to register your blocks with
	 *            Minecraft as AD2 will take care of this for you.
	 * @param soil
	 *            - Your implementation of
	 *            {@link shadows.attained.api.IVitalizedSoil IVitalizedSoil}
	 * @param drop
	 *            - The item your plant will drop in the form of an
	 *            {@link net.minecraft.item.ItemStack ItemStack}
	 */
	public static void registerAD2(IBulb bulb, IVitalizedSoil soil, ItemStack drop) {
		registerBulb(bulb, soil);
		registerDrop(bulb, drop);
	}

	private static void registerBulb(IBulb bulb, IVitalizedSoil soil) {
		if (!getBulbs().containsKey(bulb)) {
			getBulbs().put(bulb, soil);
		}
		registerSoil(soil, bulb);
	}

	private static void registerSoil(IVitalizedSoil soil, IBulb bulb) {
		if (!getSoils().containsKey(soil)) {
			getSoils().put(soil, bulb);
		}
	}

	private static void registerDrop(IBulb bulb, ItemStack stack) {
		if (!getDrops().containsKey(bulb) && !getDrops().containsValue(stack)) {
			getDrops().put(bulb, stack);
		}
	}

	public static ItemStack getBulbDrop(IBulb bulb) {
		return getDrops().get(bulb);
	}

	public static List<ItemStack> getApplicableItems() {
		List<ItemStack> result = Lists.newArrayList();
		if (getBulbs().size() > 0) {
			for (IBulb bulb : getBulbs().keySet()) {
				result.add(getBulbDrop(bulb));
			}
		}
		return result;
	}

	public static void generateList(List<String> list) {
		List<String> nameList = AD2Util.getApplicableItemNames();
		list.add(I18n.format("tooltip.attaineddrops.enableditems",
				TextFormatting.ITALIC + "" + TextFormatting.UNDERLINE));
		for (String line : nameList) {
			list.add(line);
		}
	}

	public static boolean isItemApplicable(ItemStack stackIn) {
		for (ItemStack stack : getApplicableItems()) {
			if (ItemStack.areItemsEqual(stack, stackIn)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isPlant(Block block) {
		return block instanceof BlockPlant;
	}

	public static IBulb getBulbFromPlant(World world, BlockPos pos) {
		if (isPlant(world.getBlockState(pos).getBlock())) {
			Block downBlock = world.getBlockState(pos.down()).getBlock();
			Block upBlock = world.getBlockState(pos.up()).getBlock();
			if (isSoil(downBlock)) {
				IVitalizedSoil soil = getSoilFromBlock(downBlock);
				if (isSoilEnriched(soil)) {
					return getSoils().get(soil);
				}
			} else if (isBulb(upBlock)) {
				IBulb bulb = getBulbFromBlock(upBlock);
				return bulb;
			}
		}
		return null;
	}

	public static List<String> getApplicableItemNames() {
		List<String> nameList = Lists.newArrayList();
		for (ItemStack item : getApplicableItems()) {
			nameList.add(getBulbFromItem(item).getTextColor() + "" + item.getDisplayName());
		}
		return nameList;
	}

	public static IBulb getBulbFromItem(ItemStack stack) {
		if (isItemApplicable(stack)) {
			for (IBulb bulb : getBulbs().keySet()) {
				if (ItemStack.areItemsEqual(getBulbDrop(bulb), stack)) {
					return bulb;
				}
			}
		}
		return null;
	}

	public static IVitalizedSoil getSoilFromItem(ItemStack stack) {
		if (isItemApplicable(stack)) {
			for (IVitalizedSoil soil : getSoils().keySet()) {
				if (ItemStack.areItemsEqual(getBulbDrop(getBulbFromSoil(soil)), stack)) {
					return soil;
				}
			}
		}
		return null;
	}

	public static IBlockState getSoilState(IVitalizedSoil soil) {
		return getBlockFromSoil(soil).getDefaultState();
	}

	public static boolean isSoilEnriched(IVitalizedSoil soil) {
		return soil != getSoilFromBlock(ModRegistry.VITALIZED_BASE);
	}

	public static Block getBlockFromSoil(IVitalizedSoil soil) {
		return (Block) soil;
	}

	public static IBlockState getBulbState(IBulb bulb) {
		return getBlockFromBulb(bulb).getDefaultState();
	}

	public static Block getBlockFromBulb(IBulb bulb) {
		return (Block) bulb;
	}

	public static IBulb getBulbFromSoil(IVitalizedSoil soil) {
		return getSoils().get(soil);
	}

	public static boolean isSoil(Block block) {
		return block instanceof IVitalizedSoil;
	}

	public static boolean isBulb(Block block) {
		return block instanceof IBulb;
	}

	public static IBulb getBulbFromBlock(Block block) {
		return isBulb(block) ? (IBulb) block : null;
	}

	public static IVitalizedSoil getSoilFromBlock(Block block) {
		return isSoil(block) ? (IVitalizedSoil) block : null;
	}

	public static BlockPlant getPlantFromBlock(Block block) {
		return isPlant(block) ? (BlockPlant) block : null;
	}

	public static int getPlantGrowthPercent(IBlockState state) {
		float percent = 0.0f;
		if (isPlant(state.getBlock())) {
			BlockPlant plant = getPlantFromBlock(state.getBlock());
			int currentAge = plant.getAge(state);
			int maxAge = plant.getMaxAge();
			percent = Math.round((currentAge * 100) / maxAge);

		}
		return (int) percent;
	}

	@SideOnly(Side.CLIENT)
	public static String getSneakString() {
		KeyBinding sneak = getSneakKey();
		return I18n.format("tooltip.attaineddrops.holdshift", sneak.getDisplayName());
	}

	@SideOnly(Side.CLIENT)
	public static KeyBinding getSneakKey() {
		return Minecraft.getMinecraft().gameSettings.keyBindSneak;
	}

	public static boolean getMod(String modid) {
		return Loader.isModLoaded(modid);
	}

	public static ItemStack getSoilStack(IVitalizedSoil soil) {
		return new ItemStack(getBlockFromSoil(soil));
	}

	public static ItemStack getBulbStack(IBulb bulb) {
		return new ItemStack(getBlockFromBulb(bulb));
	}

}
