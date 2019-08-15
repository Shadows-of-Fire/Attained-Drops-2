package shadows.attained.util;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class ParticleHandler {

	public static void handle(ParticleMessage msg) {
		BlockPos pos = msg.pos;
		int type = msg.type;
		if (type == 0) {
			int k = 0;
			while (k < 30) {
				double j = 0.8D - MathHelper.clamp((0.5D / ++k), 0D, 0.7D);
				EndRodParticle p = (EndRodParticle) Minecraft.getInstance().particles.addParticle(ParticleTypes.END_ROD, pos.getX() + 0.5D, pos.getY() + 5.0D, pos.getZ() + 0.5D, 0, -j, 0);
				p.setColor(msg.color.colorValue);
				p.setColorFade(msg.color.colorValue);
				Minecraft.getInstance().particles.addEffect(p);
			}

		} else if (type == 1) {
			int k = 0;
			while (k < 10) {
				k++;
				double j = 0.05D;
				Random rand = Minecraft.getInstance().world.rand;
				EndRodParticle p = (EndRodParticle) Minecraft.getInstance().particles.addParticle(ParticleTypes.END_ROD, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.nextDouble(rand, -0.1, 0.1), j, MathHelper.nextDouble(rand, -0.1, 0.1));
				p.setColor(msg.color.colorValue);
				p.setColorFade(msg.color.colorValue);
				Minecraft.getInstance().particles.addEffect(p);
			}

		} else if (type == 2) {
			int k = 0;
			while (k < 30) {
				double j = 0.5D - (0.01D * k++);
				Random rand = Minecraft.getInstance().world.rand;
				EndRodParticle p = (EndRodParticle) Minecraft.getInstance().particles.addParticle(ParticleTypes.END_ROD, pos.getX() + rand.nextDouble(), pos.getY(), pos.getZ() + rand.nextDouble(), 0, j, 0);
				p.setColor(msg.color.colorValue);
				p.setColorFade(msg.color.colorValue);
				Minecraft.getInstance().particles.addEffect(p);
			}
		}
	}
}
