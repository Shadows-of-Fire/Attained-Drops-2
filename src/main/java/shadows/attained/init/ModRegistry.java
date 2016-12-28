package shadows.attained.init;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.api.IBulb;
import shadows.attained.api.IVitalizedSoil;
import shadows.attained.blocks.BlockBulb;
import shadows.attained.blocks.BlockPlant;
import shadows.attained.blocks.BlockSoilCreator;
import shadows.attained.blocks.BlockVitalized;
import shadows.attained.items.ItemEssence;
import shadows.attained.items.ItemSeed;
import shadows.attained.util.AD2Util;

public class ModRegistry {

	private static Map<IBulb, IVitalizedSoil> BULB_REGISTRY = Maps.newHashMap();
	private static Map<IVitalizedSoil, IBulb> SOIL_REGISTRY = Maps.newHashMap();
	private static Map<IBulb, ItemStack> DROPS_REGISTRY = Maps.newHashMap();

	public static final ModCreativeTab AD2_TAB = new ModCreativeTab(ModGlobals.MODID);

	public static BlockSoilCreator BLOCK_SOILCREATOR;
	public static BlockVitalized VITALIZED_BASE;
	public static BlockPlant BLOCK_PLANT;
	public static ItemSeed ITEM_SEED;
	public static ItemEssence ITEM_ESSENCE;

	public static BlockVitalized VITALIZED_BLAZE;
	public static BlockVitalized VITALIZED_ENDERPEARL;
	public static BlockVitalized VITALIZED_GUNPOWDER;
	public static BlockVitalized VITALIZED_BONE;
	public static BlockVitalized VITALIZED_SPIDEREYE;
	public static BlockVitalized VITALIZED_STRING;
	public static BlockVitalized VITALIZED_GHASTTEAR;
	public static BlockVitalized VITALIZED_ROTTENFLESH;
	public static BlockVitalized VITALIZED_SLIMEBALL;
	public static BlockVitalized VITALIZED_PRISMARINE;
	public static BlockVitalized VITALIZED_WITHERSKULL;

	public static BlockBulb BULB_BLAZE;
	public static BlockBulb BULB_ENDERPEARL;
	public static BlockBulb BULB_GUNPOWDER;
	public static BlockBulb BULB_BONE;
	public static BlockBulb BULB_SPIDEREYE;
	public static BlockBulb BULB_STRING;
	public static BlockBulb BULB_GHASTTEAR;
	public static BlockBulb BULB_ROTTENFLESH;
	public static BlockBulb BULB_SLIMEBALL;
	public static BlockBulb BULB_PRISMARINE;
	public static BlockBulb BULB_WITHERSKULL;

	public static void init() {
		BLOCK_SOILCREATOR = new BlockSoilCreator();
		VITALIZED_BASE = new BlockVitalized("base");
		BLOCK_PLANT = new BlockPlant();
		ITEM_SEED = new ItemSeed();
		ITEM_ESSENCE = new ItemEssence();

		BULB_BLAZE = new BlockBulb("blaze", TextFormatting.GOLD);
		BULB_ENDERPEARL = new BlockBulb("enderpearl", TextFormatting.DARK_GREEN);
		BULB_GUNPOWDER = new BlockBulb("gunpowder", TextFormatting.GRAY);
		BULB_BONE = new BlockBulb("bone", TextFormatting.WHITE);
		BULB_SPIDEREYE = new BlockBulb("spidereye", TextFormatting.DARK_PURPLE);
		BULB_STRING = new BlockBulb("string", TextFormatting.WHITE);
		BULB_GHASTTEAR = new BlockBulb("ghasttear", TextFormatting.GRAY);
		BULB_ROTTENFLESH = new BlockBulb("rottenflesh", TextFormatting.GOLD);
		BULB_SLIMEBALL = new BlockBulb("slimeball", TextFormatting.GREEN);
		BULB_PRISMARINE = new BlockBulb("prismarine", TextFormatting.AQUA);
		BULB_WITHERSKULL = new BlockBulb("witherskull", TextFormatting.GRAY);

		VITALIZED_BLAZE = new BlockVitalized("blaze");
		VITALIZED_ENDERPEARL = new BlockVitalized("enderpearl");
		VITALIZED_GUNPOWDER = new BlockVitalized("gunpowder");
		VITALIZED_BONE = new BlockVitalized("bone");
		VITALIZED_SPIDEREYE = new BlockVitalized("spidereye");
		VITALIZED_STRING = new BlockVitalized("string");
		VITALIZED_GHASTTEAR = new BlockVitalized("ghasttear");
		VITALIZED_ROTTENFLESH = new BlockVitalized("rottenflesh");
		VITALIZED_SLIMEBALL = new BlockVitalized("slimeball");
		VITALIZED_PRISMARINE = new BlockVitalized("prismarine");
		VITALIZED_WITHERSKULL = new BlockVitalized("witherskull");

		AD2Util.registerAD2(BULB_BLAZE, VITALIZED_BLAZE, new ItemStack(Items.BLAZE_ROD));
		AD2Util.registerAD2(BULB_ENDERPEARL, VITALIZED_ENDERPEARL, new ItemStack(Items.ENDER_PEARL));
		AD2Util.registerAD2(BULB_GUNPOWDER, VITALIZED_GUNPOWDER, new ItemStack(Items.GUNPOWDER));
		AD2Util.registerAD2(BULB_BONE, VITALIZED_BONE, new ItemStack(Items.BONE));
		AD2Util.registerAD2(BULB_SPIDEREYE, VITALIZED_SPIDEREYE, new ItemStack(Items.SPIDER_EYE));
		AD2Util.registerAD2(BULB_STRING, VITALIZED_STRING, new ItemStack(Items.STRING));
		AD2Util.registerAD2(BULB_GHASTTEAR, VITALIZED_GHASTTEAR, new ItemStack(Items.GHAST_TEAR));
		AD2Util.registerAD2(BULB_ROTTENFLESH, VITALIZED_ROTTENFLESH, new ItemStack(Items.ROTTEN_FLESH));
		AD2Util.registerAD2(BULB_SLIMEBALL, VITALIZED_SLIMEBALL, new ItemStack(Items.SLIME_BALL));
		AD2Util.registerAD2(BULB_PRISMARINE, VITALIZED_PRISMARINE, new ItemStack(Items.PRISMARINE_SHARD));
		AD2Util.registerAD2(BULB_WITHERSKULL, VITALIZED_WITHERSKULL, new ItemStack(Items.SKULL, 1, 1));

	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		BLOCK_SOILCREATOR.initModel();
		VITALIZED_BASE.initModel();
		BLOCK_PLANT.initModel();
		ITEM_SEED.initModel();
		ITEM_ESSENCE.initModel();

		for (IVitalizedSoil soil : SOIL_REGISTRY.keySet()) {
			((BlockVitalized) soil).initModel();
			((BlockBulb) SOIL_REGISTRY.get(soil)).initModel();
		}
	}

	public static Map<IBulb, IVitalizedSoil> getBulbRegistry() {
		return BULB_REGISTRY;
	}

	public static Map<IVitalizedSoil, IBulb> getSoilRegistry() {
		return SOIL_REGISTRY;
	}

	public static Map<IBulb, ItemStack> getDropsRegistry() {
		return DROPS_REGISTRY;
	}

}
