package shadows.attained.config;

import java.io.File;


import net.minecraftforge.common.config.Configuration;



public class ADConfig
{

	public static Configuration config;

	private static final String CATEGORY_BLOCKUPDATES = "Block Updates";

	public static int BlockMobPlantUpdate;
	public static int BlockConcentrateDropUpdate;

	/*
	 * prints config options get posted to attaineddrops.cfg in here players can
	 * set options for gameplay, tailoring it to their style
	 */
	public static void init(File adConfig)
	{
		config = new Configuration(adConfig);

		//AttainedDrops.log.log(Level.INFO, "Loading Main Config");

		config.load();

		// Block Update times
		int BlockMobPlantUpdate = config.getInt("BlockMobPlantUpdate", CATEGORY_BLOCKUPDATES,
				20, 1, 1000,
				"Random number in 0 - x that a plant update will occur, the higher x, the rarer the occasion");

		int BlockConcentrateDropUpdate = config.getInt("VitalizedSoilUpdate", CATEGORY_BLOCKUPDATES,
				5, 1, 1000,
				"Random number in 0 - x that the vitalized soil will spread, the higher x, the rarer the occasion");

		config.save();

	}

}
