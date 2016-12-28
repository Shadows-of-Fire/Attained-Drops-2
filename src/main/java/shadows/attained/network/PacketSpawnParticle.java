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
	public int x, y, z, numParticles;
	public double yLevel;

	public PacketSpawnParticle() {
	}

	public PacketSpawnParticle(int particlIndex, int posX, int posY, int posZ) {
		this(particlIndex, posX, posY, posZ, 1, 0);
	}

	public PacketSpawnParticle(int particlIndex, int posX, int posY, int posZ, int numParticlesIn, double yLevelIn) {
		particle = EnumParticleTypes.getParticleFromId(particlIndex);
		x = posX;
		y = posY;
		z = posZ;
		numParticles = numParticlesIn;
		yLevel = yLevelIn;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		particle = EnumParticleTypes.getParticleFromId(buf.readInt());
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		numParticles = buf.readInt();
		yLevel = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(particle.getParticleID());
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(numParticles);
		buf.writeDouble(yLevel);
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
				for (int i = 0; i < message.numParticles; i++) {
					int r1 = rand.nextInt(2);
					int r2 = rand.nextInt(2);
					double x = posX + (r1 == 0 ? rand.nextDouble() / 10 : -(rand.nextDouble() / 10));
					double y = posY + rand.nextDouble() + 0.5;
					double z = posZ + (r2 == 0 ? rand.nextDouble() / 10 : -(rand.nextDouble() / 10));
					world.spawnParticle(message.particle, x + 0.6, y + message.yLevel, z + 0.6, 0.0, 0.0, 0.0, new int[0]);
				}
			}
		}
	}

}
