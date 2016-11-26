package shadows.attained.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import shadows.attained.*;
public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		// Initialization of blocks and items typically goes here:
		ModRegistry.init();
		RecipeRegistry.init();
	}

	public void init(FMLInitializationEvent e) {

	}

	public void postInit(FMLPostInitializationEvent e) {

	}

}
