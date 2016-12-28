package shadows.attained.init;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import shadows.attained.network.PacketSpawnParticle;

/**
 * @author p455w0rd
 *
 */
public class ModNetworkHandler {

	private static int packetId = 0;
	private static SimpleNetworkWrapper INSTANCE = null;

	public static int nextID() {
		return packetId++;
	}

	public static SimpleNetworkWrapper getInstance() {
		return INSTANCE;
	}

	public static void init() {
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModGlobals.MODID);
		INSTANCE.registerMessage(PacketSpawnParticle.Handler.class, PacketSpawnParticle.class, nextID(), Side.CLIENT);
	}

}
