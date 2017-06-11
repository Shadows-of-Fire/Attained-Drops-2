package shadows.attained.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import shadows.attained.init.ModRegistry;
//import shadows.attained.integration.Waila;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		ModRegistry.initModels();
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		//if (Loader.isModLoaded("waila")) FMLInterModComms.sendMessage("waila", "register", Waila.class.getName() + ".callbackRegister");
	}

	@Override
	public World getWorld() {
		return Minecraft.getMinecraft().world;
	}
}
