package shadows.attained.util;

import net.minecraft.item.DyeColor;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class ParticleMessage {

	final BlockPos pos;
	final int type;
	final DyeColor color;

	public ParticleMessage(BlockPos pos, DyeColor color, int type) {
		this.pos = pos;
		this.color = color;
		this.type = type;
	}

	public ParticleMessage(BlockPos pos, DyeColor color) {
		this(pos, color, 0);
	}

	public static ParticleMessage read(PacketBuffer buf) {
		BlockPos pos = BlockPos.fromLong(buf.readLong());
		DyeColor color = DyeColor.byId(buf.readByte());
		byte type = buf.readByte();
		return new ParticleMessage(pos, color, type);
	}

	public static void write(ParticleMessage msg, PacketBuffer buf) {
		buf.writeLong(msg.pos.toLong());
		buf.writeByte(msg.color.getId());
		buf.writeByte(msg.type);
	}

}
