package shadows.attained.util;

import java.util.function.Supplier;

import net.minecraft.item.DyeColor;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import shadows.placebo.util.NetworkUtils;
import shadows.placebo.util.NetworkUtils.MessageProvider;

public class ParticleMessage extends MessageProvider<ParticleMessage> {

	BlockPos pos;
	int type;
	DyeColor color;

	public ParticleMessage(BlockPos pos, DyeColor color, int type) {
		this.pos = pos;
		this.color = color;
		this.type = type;
	}

	public ParticleMessage(BlockPos pos, DyeColor color) {
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
		BlockPos pos = BlockPos.fromLong(buf.readLong());
		DyeColor color = DyeColor.byId(buf.readByte());
		byte type = buf.readByte();
		return new ParticleMessage(pos, color, type);
	}

	@Override
	public void write(ParticleMessage msg, PacketBuffer buf) {
		buf.writeLong(msg.pos.toLong());
		buf.writeByte(msg.color.getId());
		buf.writeByte(msg.type);
	}

	@Override
	public void handle(ParticleMessage msg, Supplier<Context> ctx) {
		NetworkUtils.handlePacket(() -> () -> {
			ParticleHandler.handle(msg);
		}, ctx.get());
	}

}
