package shadows.attained.util;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import shadows.placebo.util.NetworkUtils;
import shadows.placebo.util.NetworkUtils.MessageProvider;

public class ParticleMessage extends MessageProvider<ParticleMessage> {

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
	public ParticleMessage read(PacketBuffer buf) {
		BlockPos pos = BlockPos.of(buf.readLong());
		int color = buf.readInt();
		byte type = buf.readByte();
		return new ParticleMessage(pos, color, type);
	}

	@Override
	public void write(ParticleMessage msg, PacketBuffer buf) {
		buf.writeLong(msg.pos.asLong());
		buf.writeInt(msg.color);
		buf.writeByte(msg.type);
	}

	@Override
	public void handle(ParticleMessage msg, Supplier<Context> ctx) {
		NetworkUtils.handlePacket(() -> () -> {
			ParticleHandler.handle(msg);
		}, ctx.get());
	}

}
