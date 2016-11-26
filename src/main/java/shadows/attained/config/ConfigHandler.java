package shadows.attained.config;

import java.io.File;

public class ConfigHandler
{

	public static File adConfig;
	public static File bulbConfig;

	public static void init(String configPath)
	{
		bulbConfig = new File(configPath + "bulbsettings.cfg");
		adConfig = new File(configPath + "attaineddrops.cfg");

		ADBulbConfig.init(bulbConfig);
		ADConfig.init(adConfig);

	}

}
