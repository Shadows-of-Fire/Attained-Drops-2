package shadows.attained;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

public class AttainedConfig {

	static final ForgeConfigSpec spec;
	public static final AttainedConfig INSTANCE;
	static {
		final Pair<AttainedConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(AttainedConfig::new);
		spec = specPair.getRight();
		INSTANCE = specPair.getLeft();
	}

	public final IntValue dropChance;
	public final BooleanValue allowBonemeal;
	public final IntValue spreaderRadius;
	public final BooleanValue revertToDirt;
	public final BooleanValue rightClickFarm;

	AttainedConfig(ForgeConfigSpec.Builder build) {
		build.comment("Server Configuration");
		build.push("server");
		dropChance = build.comment("The 1/n chance for life essence to drop from a monster.").defineInRange("Drop Chance", 18, 1, Integer.MAX_VALUE);
		allowBonemeal = build.comment("If bonemeal works on vitalized plants.").define("Allow Bonemeal", false);
		spreaderRadius = build.comment("The radius of the Vitality Spreader.").defineInRange("Vitality Spreader Radius", 2, 0, 5);
		revertToDirt = build.comment("If vitalized soil will revert to dirt (instead of vitalized soil) after growing 1-4 bulbs.").define("Dirt Reversion", true);
		rightClickFarm = build.comment("If bulbs can be harvested on right click.").define("Simple Harvest", true);
		build.pop();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading e) {
		if (e.getConfig().getModId().equals(AttainedDrops.MODID)) AttainedDrops.LOGGER.info("Loaded config file!");
	}

}
