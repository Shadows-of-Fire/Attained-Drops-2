package shadows.attained.integration;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import shadows.attained.api.PlantingRegistry;
import shadows.attained.blocks.BlockBulb;
import shadows.attained.blocks.BlockPlant;
import shadows.attained.blocks.BlockSoil;
import shadows.attained.blocks.BlockVitalitySpreader;
import shadows.attained.blocks.DefaultTypes;
import shadows.placebo.compat.TOPCompat;

public class TOPPlugin implements TOPCompat.Provider {

	public static void register() {
		TOPCompat.registerProvider(new TOPPlugin());
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData hitData) {
		if (state.getBlock() instanceof BlockPlant) {
			BlockState down = level.getBlockState(hitData.getPos().below());
			if (down.getBlock() instanceof BlockSoil) {
				BlockBulb bulb = PlantingRegistry.BULBS.get(((BlockSoil) down.getBlock()).type);
				if (bulb == null) return;
				info.mcText(new TranslatableComponent("tooltip.attained_drops.growing", bulb.getName()));
			}
		} else if (state.getBlock() instanceof BlockSoil soil) {
			if (soil.type != DefaultTypes.NONE) {
				info.mcText(new TranslatableComponent("tooltip.attained_drops.enrichedwith", soil.type.getDrop().getHoverName()));
			}
		} else if (state.getBlock() instanceof BlockVitalitySpreader) {
			info.mcText(new TranslatableComponent("tooltip.attained_drops.creatorcharge", state.getValue(BlockVitalitySpreader.CHARGE)));
		}
	}

}
