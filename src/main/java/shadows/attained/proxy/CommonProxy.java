package shadows.attained.proxy;

import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import shadows.attained.init.Config;
import shadows.attained.init.ModRecipes;
import shadows.attained.init.ModRegistry;

public class CommonProxy {
	public static Configuration config;

	public void preInit(FMLPreInitializationEvent e) {
		// Initialization of blocks and items typically goes here:
		config = new Configuration(e.getSuggestedConfigurationFile());
		Config.syncConfig();
		ModRegistry.init();
		ModRecipes.init();
	}

	public void init(FMLInitializationEvent e) {
	}

	public void postInit(FMLPostInitializationEvent e) {

	}

	public World getWorld() {
		return null;
	}

}
