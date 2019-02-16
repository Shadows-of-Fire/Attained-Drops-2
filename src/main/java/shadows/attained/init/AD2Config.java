package shadows.attained.init;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class AD2Config {

	public static IntValue dropChance;
	public static BooleanValue allowBonemeal;
	public static IntValue spreaderRadius;
	public static BooleanValue revertToDirt;
	public static BooleanValue rightClickFarm;

	public static void load() {
		ForgeConfigSpec.Builder build = new ForgeConfigSpec.Builder();
		dropChance = build.defineInRange("Drop Chance", 18, 1, Integer.MAX_VALUE);
		build.comment("The 1/n chance for life essence to drop from a monster.");
		allowBonemeal = build.define("Allow Bonemeal", false);
		build.comment("If bonemeal works on vitalized plants.");
		spreaderRadius = build.defineInRange("Vitality Spreader Radius", 2, 0, 5);
		build.comment("The radius of the Vitality Spreader.");
		revertToDirt = build.define("Dirt Reversion", true);
		build.comment("If vitalized soil will revert to dirt (instead of vitalized soil) after growing 1-4 bulbs.");
		rightClickFarm = build.define("Simple Harvest", true);
		build.comment("If bulbs can be harvested on right click.");
		ModLoadingContext.get().registerConfig(Type.COMMON, build.build());
	}

}
