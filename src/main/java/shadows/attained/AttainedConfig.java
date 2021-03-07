package shadows.attained;

import java.util.HashSet;
import java.util.Set;

import shadows.attained.blocks.DefaultTypes;
import shadows.placebo.config.Configuration;

public class AttainedConfig {

	public static int dropChance = 18;
	public static boolean allowBonemeal = false;
	public static int spreaderRadius = 2;
	public static boolean revertToDirt = true;
	public static boolean rightClickFarm = true;
	public static Set<DefaultTypes> disabledBulbs = new HashSet<>();

	public static void load() {
		Configuration cfg = new Configuration(AttainedDrops.MODID);
		dropChance = cfg.getInt("Essence Drop Chance", "general", dropChance, 1, Integer.MAX_VALUE, "The 1/n chance for life essence to drop from a monster.");
		allowBonemeal = cfg.getBoolean("Allow Bonemeal", "general", allowBonemeal, "If bonemeal works on vitalized plants");
		spreaderRadius = cfg.getInt("Vitality Spreader Radius", "general", 2, 0, 5, "The radius of the Vitality Spreader.");
		revertToDirt = cfg.getBoolean("Dirt Reversion", "general", revertToDirt, "If vitalized soil has a chance to revert to dirt on bulb growth.");
		rightClickFarm = cfg.getBoolean("Right Click Harvest", "general", rightClickFarm, "If bulbs are harvested by a right click.");
		String[] disabled = cfg.getStringList("Disabled Bulbs", "general", new String[0], "A list of disabled default bulb types.  Names must match the enum types listed at https://github.com/Shadows-of-Fire/Attained-Drops-2/blob/master/src/main/java/shadows/attained/blocks/DefaultTypes.java");
		for (String s : disabled) {
			try {
				DefaultTypes t = DefaultTypes.valueOf(s);
				if (t != DefaultTypes.NONE) disabledBulbs.add(t);
			} catch (Exception e) {
				AttainedDrops.LOGGER.error("Error disabling bulb type " + s + " as it does not exist!");
				e.printStackTrace();
			}
		}
		if (cfg.hasChanged()) cfg.save();
	}

}
