package shadows.attained.proxy;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import shadows.attained.ModRegistry;


public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		ModRegistry.initModels();

	}
}

