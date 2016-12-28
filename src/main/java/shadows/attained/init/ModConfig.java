package shadows.attained.init;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author p455w0rd
 *
 */
public class ModConfig {

	private static Configuration CONFIG;
	private static File CONFIG_FILE = new File("config/" + ModGlobals.MODNAME.replaceAll("\\s+", "").trim() + ".cfg");
	private static String DEF_CAT = "Options";

	public static void init() {
		if (CONFIG == null) {
			CONFIG = new Configuration(CONFIG_FILE);
			MinecraftForge.EVENT_BUS.register(new ModConfig());
		}

		ConfigOptions.VAPOUR_MODE = CONFIG.getBoolean("VapourMode", DEF_CAT, false, "Mimic original AD recipes and items/blocks");
		ConfigOptions.SOIL_CREATOR_DIAMETER = CONFIG.getInt("SoilCreatorDiameter", DEF_CAT, 3, 3, 9, "Dimater of effective area (must be odd number)");
		ConfigOptions.CAN_BONEMEAL = CONFIG.getBoolean("AllowBonemeal", DEF_CAT, true, "Allow plants to be bonemealed");

		if (CONFIG.hasChanged()) {
			CONFIG.save();
		}
	}

	public static int getDiameter() {
		if (ConfigOptions.SOIL_CREATOR_DIAMETER < 3) {
			return 3;
		}
		if (ConfigOptions.SOIL_CREATOR_DIAMETER > 9) {
			return 9;
		}
		if ((ConfigOptions.SOIL_CREATOR_DIAMETER % 2) == 0) {
			ConfigOptions.SOIL_CREATOR_DIAMETER--;
		}
		return ConfigOptions.SOIL_CREATOR_DIAMETER;
	}

	@SubscribeEvent
	public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent e) {
		if (e.getModID().equals(ModGlobals.MODID)) {
			init();
		}
	}

	public static boolean vapourMode() {
		return ConfigOptions.VAPOUR_MODE;
	}

	public static class ConfigOptions {

		public static boolean VAPOUR_MODE = false;
		public static int SOIL_CREATOR_DIAMETER = 3;
		public static boolean CAN_BONEMEAL = true;

	}

}
