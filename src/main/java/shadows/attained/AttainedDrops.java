package shadows.attained;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import shadows.attained.init.ModGlobals;
import shadows.attained.proxy.CommonProxy;

@Mod(modid = ModGlobals.MODID, version = ModGlobals.VERSION, name = ModGlobals.MODNAME, dependencies = ModGlobals.DEPENDENCIES)
public class AttainedDrops {

	@SidedProxy(clientSide = ModGlobals.CLIENT_PROXY, serverSide = ModGlobals.COMMON_PROXY)
	public static CommonProxy PROXY;

	@Mod.Instance
	public static AttainedDrops INSTANCE;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		PROXY.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		PROXY.init(e);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		PROXY.postInit(e);
	}

}
