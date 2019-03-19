package shadows.attained.util;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEndRod;
import net.minecraft.client.particle.ParticleSimpleAnimated;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class ParticleHandler {

	public static void handle(ParticleMessage message, Supplier<NetworkEvent.Context> context) {
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> handleMessage(message));
		context.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.CLIENT)
	private static void handleMessage(ParticleMessage message) {
		BlockPos pos = message.pos;
		int type = message.type;
		if (type == 0) Minecraft.getInstance().addScheduledTask(() -> {
			int k = 0;
			while (k < 30) {
				double j = 0.8D - MathHelper.clamp((0.5D / ++k), 0D, 0.7D);
				ParticleSimpleAnimated p = new ParticleEndRod(Minecraft.getInstance().world, pos.getX() + 0.5D, pos.getY() + 5.0D, pos.getZ() + 0.5D, 0, -j, 0);
				p.setColor(message.color.colorValue);
				p.setColorFade(message.color.colorValue);
				Minecraft.getInstance().particles.addEffect(p);
			}

		});
		else if (type == 1) Minecraft.getInstance().addScheduledTask(() -> {
			int k = 0;
			while (k < 10) {
				k++;
				double j = 0.05D;
				Random rand = Minecraft.getInstance().world.rand;
				ParticleSimpleAnimated p = new ParticleEndRod(Minecraft.getInstance().world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.nextDouble(rand, -0.1, 0.1), j, MathHelper.nextDouble(rand, -0.1, 0.1));
				p.setColor(message.color.colorValue);
				p.setColorFade(message.color.colorValue);
				Minecraft.getInstance().particles.addEffect(p);
			}

		});
		else if (type == 2) Minecraft.getInstance().addScheduledTask(() -> {
			int k = 0;
			while (k < 30) {
				double j = 0.5D - (0.01D * k++);
				Random rand = Minecraft.getInstance().world.rand;
				ParticleSimpleAnimated p = new ParticleEndRod(Minecraft.getInstance().world, pos.getX() + rand.nextDouble(), pos.getY(), pos.getZ() + rand.nextDouble(), 0, j, 0);
				p.setColor(message.color.colorValue);
				p.setColorFade(message.color.colorValue);
				Minecraft.getInstance().particles.addEffect(p);
			}

		});
	}

}
