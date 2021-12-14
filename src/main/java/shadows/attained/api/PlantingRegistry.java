package shadows.attained.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import shadows.attained.AttainedConfig;
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
			if (stack.sameItem(s.getDrop())) return s;
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
			Properties props = new Properties();
			String comment = "In this file, each new line entry is a new bulb type.  A line must contain the following information: name=item|color\r\n" + "Name: This is the internal name of this bulb type.  It may only use lowercase letters and numbers, and must be unique.\r\n" + "Item: This is the relevant item, used to vitalize soil and the item dropped by the bulb.  Must be an item registry name, such as minecraft:diamond\r\n" + "Color: This is a hex integer that represents the color of this soil/bulb combo.  It must be six digits long, and requires the 0x prefix.  Ex: 0xFFFFFF\r\n" + "An example definition for redstone is redstone=minecraft:redstone|0xAA0F01";
			props.store(new FileOutputStream(f), comment);
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
		for (DefaultTypes t : DefaultTypes.VALUES) {
			if (AttainedConfig.disabledBulbs.contains(t)) continue;
			PlantingRegistry.SOILS.put(t, new BlockSoil(t));
			if (t != DefaultTypes.NONE) {
				PlantingRegistry.BULBS.put(t, new BlockBulb(t));
			}
		}
		loadCustomTypes();
	}

}
