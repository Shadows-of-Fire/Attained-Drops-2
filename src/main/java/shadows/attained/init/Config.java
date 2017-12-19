package shadows.attained.init;

import net.minecraftforge.common.config.Configuration;

public class Config {

	public static int dropChance = 18;
	public static boolean allowBonemeal = true;
	public static int creatorRadius = 2;
	public static boolean revertToDirt = true;

	public static void syncConfig(Configuration config) {
		config.load();

		dropChance = config.getInt(Configuration.CATEGORY_GENERAL, "Drop Chance", 18, 1, Integer.MAX_VALUE, "The 1/n chance for Essence to drop from mobs.");
		allowBonemeal = config.getBoolean(Configuration.CATEGORY_GENERAL, "Allow Bonemeal", false, "If bonemealing Attained Plants is allowed.  This is strictly for plants, you can never grow bulbs with bonemeal.");
		creatorRadius = config.getInt(Configuration.CATEGORY_GENERAL, "Creator Radius", 2, 1, 5, "The radius of the soil creator.");
		revertToDirt = config.getBoolean(Configuration.CATEGORY_GENERAL, "Revert to dirt", true, "If Vitalized Soil has a chance to revert to dirt upon bulb growth.");

		if (config.hasChanged()) config.save();
	}

}
