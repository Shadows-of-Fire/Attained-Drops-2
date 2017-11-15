package shadows.attained.util;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEndRod;
import net.minecraft.client.particle.ParticleSimpleAnimated;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleMessage implements IMessage {

	public ParticleMessage() {
	}

	private BlockPos pos;
	private byte type = 0;
	private EnumDyeColor color;

	public ParticleMessage(EnumDyeColor color, BlockPos pos) {
		this.pos = pos;
		this.color = color;
	}

	public ParticleMessage(EnumDyeColor color, BlockPos pos, byte i) {
		this(color, pos);
		type = i;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		type = buf.readByte();
		color = EnumDyeColor.byMetadata(buf.readInt());

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeByte(type);
		buf.writeInt(color.getMetadata());
	}

	public static class ParticleMessageHandler implements IMessageHandler<ParticleMessage, IMessage> {

		@Override
		public IMessage onMessage(ParticleMessage message, MessageContext ctx) {
			if(FMLCommonHandler.instance().getEffectiveSide().isClient()) doMessage(message, ctx);
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		public void doMessage(ParticleMessage message, MessageContext ctx) {
			BlockPos pos = message.pos;
			byte type = message.type;
			if (type == 0) Minecraft.getMinecraft().addScheduledTask(() -> {
				int k = 0;
				while (k < 30) {
					double j = 0.8D - MathHelper.clamp((0.5D / (double) ++k), 0D, 0.7D);
					ParticleSimpleAnimated p = new ParticleEndRod(Minecraft.getMinecraft().world, pos.getX() + 0.5D, pos.getY() + 5.0D, pos.getZ() + 0.5D, 0, -j, 0);
					p.setColor(message.color.getColorValue());
					p.setColorFade(message.color.getColorValue());
					Minecraft.getMinecraft().effectRenderer.addEffect(p);
				}

			});
			else if (type == 1) Minecraft.getMinecraft().addScheduledTask(() -> {
				int k = 0;
				while (k < 10) {
					k++;
					double j = 0.05D;
					Random rand = Minecraft.getMinecraft().world.rand;
					ParticleSimpleAnimated p = new ParticleEndRod(Minecraft.getMinecraft().world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.nextDouble(rand, -0.1, 0.1), j, MathHelper.nextDouble(rand, -0.1, 0.1));
					p.setColor(message.color.getColorValue());
					p.setColorFade(message.color.getColorValue());
					Minecraft.getMinecraft().effectRenderer.addEffect(p);
				}

			});
			else if (type == 2) Minecraft.getMinecraft().addScheduledTask(() -> {
				int k = 0;
				while (k < 30) {
					double j = 0.5D - (0.01D * k++);
					Random rand = Minecraft.getMinecraft().world.rand;
					ParticleSimpleAnimated p = new ParticleEndRod(Minecraft.getMinecraft().world, pos.getX() + rand.nextDouble(), pos.getY(), pos.getZ() + rand.nextDouble(), 0, j, 0);
					p.setColor(message.color.getColorValue());
					p.setColorFade(message.color.getColorValue());
					Minecraft.getMinecraft().effectRenderer.addEffect(p);
				}

			});
		}

	}

}
