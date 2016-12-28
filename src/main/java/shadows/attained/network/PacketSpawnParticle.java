package shadows.attained.network;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import shadows.attained.AttainedDrops;

/**
 * @author p455w0rd
 *
 */
public class PacketSpawnParticle implements IMessage {

	public EnumParticleTypes particle;
	public int x, y, z;

	public PacketSpawnParticle() {
	}

	public PacketSpawnParticle(int particlIndex, int posX, int posY, int posZ) {
		particle = EnumParticleTypes.getParticleFromId(particlIndex);
		x = posX;
		y = posY;
		z = posZ;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		particle = EnumParticleTypes.getParticleFromId(buf.readInt());
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(particle.getParticleID());
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	public static class Handler implements IMessageHandler<PacketSpawnParticle, IMessage> {

		@Override
		public IMessage onMessage(final PacketSpawnParticle message, final MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(new Runnable() {
				@Override
				public void run() {
					handle(message, ctx);
				}
			});
			return null;
		}

		private void handle(PacketSpawnParticle message, MessageContext ctx) {
			if (ctx.getClientHandler() != null) {
				World world = AttainedDrops.PROXY.getWorld();
				Random rand = world.rand;
				double posX = message.x;
				double posY = message.y;
				double posZ = message.z;
				int r1 = rand.nextInt(2);
				int r2 = rand.nextInt(2);
				double x = posX + (r1 == 0 ? rand.nextDouble() / 10 : -(rand.nextDouble() / 10));
				double y = posY + rand.nextDouble() + 0.5;
				double z = posZ + (r2 == 0 ? rand.nextDouble() / 10 : -(rand.nextDouble() / 10));
				world.spawnParticle(message.particle, x + 0.6, y, z + 0.6, 0.0, 0.0, 0.0, new int[0]);
			}
		}
	}

}
