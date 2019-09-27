package shadows.attained.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import shadows.attained.AttainedDrops;
import shadows.attained.blocks.BlockBulb;
import shadows.attained.blocks.BlockSoil;
import shadows.attained.blocks.CustomBulbType;
import shadows.attained.blocks.DefaultTypes;

public class PlantingRegistry {

	public static final Map<IAttainedType, BlockSoil> SOILS = new HashMap<>();
	public static final Map<IAttainedType, BlockBulb> BULBS = new HashMap<>();

	public static IAttainedType byStack(ItemStack stack) {
		for (IAttainedType s : SOILS.keySet()) {
			if (stack.isItemEqual(s.getDrop())) return s;
		}
		return null;
	}

	/**
	 * Loads custom blocks from attained_drops_custom.properties.
	 * Spec is as follows.  A block declation uses up a single line, this line declares both the bulb, and the soil.
	 * registry_name=drop|color
	 * drop is an item registry name.
	 * color is an integer, can be hex (0xFFFFFF etc).
	 */
	private static void loadCustomTypes() {
		File f = new File(FMLPaths.CONFIGDIR.get().toFile(), "attained_drops_custom.properties");
		if (!f.exists()) try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Properties props = new Properties();
		try (FileInputStream stream = new FileInputStream(f);) {
			props.load(stream);
		} catch (IOException e) {
			AttainedDrops.LOGGER.error("Failed to load custom block properties file!");
			e.printStackTrace();
		}
		for (Object o : props.keySet()) {
			String name = (String) o;
			String unparsed = props.getProperty(name);
			try {
				String[] split = unparsed.split("\\|");
				int color = Integer.parseInt(split[1].contains("0x") ? split[1].substring(2) : split[1], split[1].contains("0x") ? 16 : 10);
				CustomBulbType type = new CustomBulbType(name, color, () -> new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(split[0]))));
				PlantingRegistry.SOILS.put(type, new BlockSoil(type));
				PlantingRegistry.BULBS.put(type, new BlockBulb(type));
			} catch (Exception e) {
				AttainedDrops.LOGGER.error("Error loading custom block definition for: " + name);
				e.printStackTrace();
			}
		}
	}

	public static void load() {
		for (DefaultTypes t : DefaultTypes.values()) {
			if (t != DefaultTypes.NONE) {
				BlockBulb b = new BlockBulb(t);
				PlantingRegistry.BULBS.put(t, b);
			}
		}
		for (DefaultTypes t : DefaultTypes.VALUES) {
			BlockSoil b = new BlockSoil(t);
			PlantingRegistry.SOILS.put(t, b);
		}
		loadCustomTypes();
	}

}
