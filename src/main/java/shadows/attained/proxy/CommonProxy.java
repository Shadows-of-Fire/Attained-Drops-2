package shadows.attained.proxy;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import shadows.attained.init.ModConfig;
import shadows.attained.init.ModEvents;
import shadows.attained.init.ModGlobals;
import shadows.attained.init.ModNetworkHandler;
import shadows.attained.init.ModRecipes;
import shadows.attained.init.ModRegistry;
import shadows.attained.integration.TOP;
import shadows.attained.util.AD2Util;

public class CommonProxy {

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		ModRegistry.init();
		ModRecipes.init();
		ModConfig.init();
		ModEvents.init();
		ModNetworkHandler.init();
		if (AD2Util.getMod(ModGlobals.MODID_TOP)) {
			TOP.init();
		}
	}

	public void init(FMLInitializationEvent e) {
	}

	public void postInit(FMLPostInitializationEvent e) {

	}

	public World getWorld() {
		return null;
	}

}
