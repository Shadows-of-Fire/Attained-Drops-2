package shadows.attained.proxy;

import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import shadows.attained.*;
import shadows.attained.config.ConfigHandler;
public class CommonProxy {
	
	
	
	public static String configPath;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {

		configPath = e.getModConfigurationDirectory() + "/attaineddrops/";
		// Initialization of blocks and items typically goes here:
		ConfigHandler.init(configPath);
		ModRegistry.init();
		RecipeRegistry.init();
	}

	public void init(FMLInitializationEvent e) {

	}

	public void postInit(FMLPostInitializationEvent e) {

	}

}
