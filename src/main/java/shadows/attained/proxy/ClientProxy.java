package shadows.attained.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import shadows.attained.init.ModGlobals;
import shadows.attained.init.ModRegistry;
import shadows.attained.integration.WAILA;
import shadows.attained.util.AD2Util;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		ModRegistry.initModels();
	}

	@Override
	public void init(FMLInitializationEvent e) {
		if (AD2Util.getMod(ModGlobals.MODID_WAILA)) {
			FMLInterModComms.sendMessage(ModGlobals.MODID_WAILA, "register", WAILA.class.getName() + ".callbackRegister");
		}
	}

	@Override
	public World getWorld() {
		return Minecraft.getMinecraft().world;
	}
}
