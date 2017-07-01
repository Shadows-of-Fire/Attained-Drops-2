package shadows.attained.proxy;

import javax.security.auth.login.Configuration;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import shadows.attained.init.Config;
import shadows.attained.init.ModRegistry;

public class CommonProxy {
	public static Configuration config;

	public void preInit(FMLPreInitializationEvent e) {
		config = new Configuration(e.getSuggestedConfigurationFile());
		Config.syncConfig();
		MinecraftForge.EVENT_BUS.register(new ModRegistry());
	}

	public void init(FMLInitializationEvent e) {
	}

	public void postInit(FMLPostInitializationEvent e) {

	}

	public World getWorld() {
		return null;
	}

}
