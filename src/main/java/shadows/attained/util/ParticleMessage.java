package shadows.attained.util;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ParticleMessage implements IMessage {

	public ParticleMessage() {
	}

	private long pos2;
	private byte type = 0;

	public ParticleMessage(BlockPos pos) {
		this.pos2 = pos.toLong();
	}

	public ParticleMessage(BlockPos pos, byte i) {
		this(pos);
		type = i;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos2 = buf.readLong();
		type = buf.readByte();

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos2);
		buf.writeByte(type);

	}

	public static class ParticleMessageHandler implements IMessageHandler<ParticleMessage, IMessage> {

		@Override
		public IMessage onMessage(ParticleMessage message, MessageContext ctx) {
			BlockPos pos2 = BlockPos.fromLong(message.pos2);
			byte type = message.type;
			if (type == 0) Minecraft.getMinecraft().addScheduledTask(() -> {
				int k = 0;
				while (k < 30) {
					double j = 0.8D - MathHelper.clamp((0.5D / (double) ++k), 0D, 0.7D);
					Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.END_ROD, true, pos2.getX() + 0.5D, pos2.getY() + 5.0D, pos2.getZ() + 0.5D, 0, -j, 0);
				}

			});
			else if (type == 1) Minecraft.getMinecraft().addScheduledTask(() -> {
				int k = 0;
				while (k < 10) {
					k++;
					double j = 0.05D;
					Random rand = Minecraft.getMinecraft().world.rand;
					Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.END_ROD, true, pos2.getX() + 0.5D, pos2.getY(), pos2.getZ() + 0.5D, MathHelper.nextDouble(rand, -0.1, 0.1), j, MathHelper.nextDouble(rand, -0.1, 0.1));
				}

			});
			else if (type == 2) Minecraft.getMinecraft().addScheduledTask(() -> {
				int k = 0;
				while (k < 30) {
					double j = 0.5D - (0.01D * k++);
					Random rand = Minecraft.getMinecraft().world.rand;
					Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.END_ROD, true, pos2.getX() + rand.nextDouble(), pos2.getY(), pos2.getZ() + rand.nextDouble(), 0, j, 0);
				}

			});

			return null;
		}

	}

}
