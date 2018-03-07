package shadows.attained.init;

import net.minecraftforge.common.config.Configuration;

public class Config {

	public static int dropChance = 18;
	public static boolean allowBonemeal = true;
	public static int creatorRadius = 2;
	public static boolean revertToDirt = true;
	public static boolean rightClickFarm = false;

	public static void syncConfig(Configuration config) {
		config.load();

		String g = Configuration.CATEGORY_GENERAL;

		dropChance = config.getInt("Drop Chance", g, 18, 1, Integer.MAX_VALUE, "The 1/n chance for Essence to drop from mobs.");
		allowBonemeal = config.getBoolean("Allow Bonemeal", g, false, "If bonemealing Attained Plants is allowed.  This is strictly for plants, you can never grow bulbs with bonemeal.");
		creatorRadius = config.getInt("Creator Radius", g, 2, 1, 5, "The radius of the soil creator.");
		revertToDirt = config.getBoolean("Revert to Dirt", g, true, "If Vitalized Soil has a chance to revert to dirt upon bulb growth.");
		rightClickFarm = config.getBoolean("Right-Click Harvest", g, false, "If bulbs will be harvested by right click.");

		if (config.hasChanged()) config.save();
	}

}
