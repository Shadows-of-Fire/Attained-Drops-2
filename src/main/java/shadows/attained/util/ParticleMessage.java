package shadows.attained.util;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;
import shadows.placebo.network.MessageHelper;
import shadows.placebo.network.MessageProvider;

public class ParticleMessage implements MessageProvider<ParticleMessage> {

	BlockPos pos;
	int type;
	int color;

	public ParticleMessage(BlockPos pos, int color, int type) {
		this.pos = pos;
		this.color = color;
		this.type = type;
	}

	public ParticleMessage(BlockPos pos, int color) {
		this(pos, color, 0);
	}

	public ParticleMessage() {
	}

	@Override
	public Class<ParticleMessage> getMsgClass() {
		return ParticleMessage.class;
	}

	@Override
	public ParticleMessage read(FriendlyByteBuf buf) {
		BlockPos pos = BlockPos.of(buf.readLong());
		int color = buf.readInt();
		byte type = buf.readByte();
		return new ParticleMessage(pos, color, type);
	}

	@Override
	public void write(ParticleMessage msg, FriendlyByteBuf buf) {
		buf.writeLong(msg.pos.asLong());
		buf.writeInt(msg.color);
		buf.writeByte(msg.type);
	}

	@Override
	public void handle(ParticleMessage msg, Supplier<Context> ctx) {
		MessageHelper.handlePacket(() -> () -> {
			ParticleHandler.handle(msg);
		}, ctx);
	}

}
