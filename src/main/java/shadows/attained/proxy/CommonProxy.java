package shadows.attained.proxy;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import shadows.attained.AttainedDrops2;
import shadows.attained.init.Config;
import shadows.attained.init.ModRegistry;
import shadows.attained.util.ParticleMessage;
import shadows.attained.util.ParticleMessage.ParticleMessageHandler;

public class CommonProxy {
	public static Configuration config;

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(AttainedDrops2.MODID);

	public void preInit(FMLPreInitializationEvent e) {
		config = new Configuration(e.getSuggestedConfigurationFile());
		Config.syncConfig();
		MinecraftForge.EVENT_BUS.register(new ModRegistry());
	}

	public void init(FMLInitializationEvent e) {
		int i = 0;
		INSTANCE.registerMessage(ParticleMessageHandler.class, ParticleMessage.class, i++, Side.CLIENT);
	}

	public void postInit(FMLPostInitializationEvent e) {

	}

	public World getWorld() {
		return null;
	}

}
