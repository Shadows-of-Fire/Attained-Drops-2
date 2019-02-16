package shadows.attained.init;

import java.util.EnumMap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;
import shadows.attained.AttainedDrops;
import shadows.attained.blocks.BlockBulb;
import shadows.attained.blocks.BlockPlant;
import shadows.attained.blocks.BlockSoil;
import shadows.attained.blocks.BlockVitalitySpreader;
import shadows.attained.blocks.BulbType;
import shadows.attained.blocks.SoilType;
import shadows.attained.items.ItemSeed;

@ObjectHolder(AttainedDrops.MODID)
@EventBusSubscriber(modid = AttainedDrops.MODID, bus = Bus.MOD)
public class ModRegistry {

	//Definalized to avoid ObjectHolder errors.
	public static EnumMap<SoilType, BlockSoil> SOILS = new EnumMap<>(SoilType.class);
	public static EnumMap<SoilType, BlockBulb> SOIL_TO_BULB = new EnumMap<>(SoilType.class);

	public static final ItemSeed SEED = null;
	public static final Item LIFE_ESSENCE = null;
	public static final BlockVitalitySpreader VITALITY_SPREADER = null;
	public static final BlockPlant PLANT = null;

	public static void initRecipes() {
		AttainedDrops.RECIPES.addShapeless(new ItemStack(ModRegistry.SEED, 2), ModRegistry.LIFE_ESSENCE, Items.APPLE, Items.WHEAT_SEEDS);
		AttainedDrops.RECIPES.addShaped(ModRegistry.VITALITY_SPREADER, 3, 3, null, ModRegistry.SEED, null, Items.DIAMOND, Blocks.DIRT, Items.DIAMOND, null, Items.WATER_BUCKET, null);
	}

	@SubscribeEvent
	public void onBlockRegistry(Register<Block> e) {
		for (BulbType t : BulbType.values()) {
			BlockBulb b;
			e.getRegistry().register(b = new BlockBulb(t));
			SOIL_TO_BULB.put(SoilType.VALUES[t.ordinal() + 1], b);
		}
		for (SoilType t : SoilType.VALUES) {
			BlockSoil b;
			e.getRegistry().register(b = new BlockSoil(t));
			SOILS.put(t, b);
		}
		e.getRegistry().registerAll(new BlockVitalitySpreader(), new BlockPlant());
	}

	@SubscribeEvent
	public void onItemRegistry(Register<Item> e) {
		e.getRegistry().registerAll(new ItemSeed(), new Item(new Item.Properties()).setRegistryName(AttainedDrops.MODID, "life_essence"));
		for (Block b : SOILS.values()) {
			e.getRegistry().register(new ItemBlock(b, new Item.Properties()).setRegistryName(b.getRegistryName()));
		}
		for (Block b : SOIL_TO_BULB.values()) {
			e.getRegistry().register(new ItemBlock(b, new Item.Properties()).setRegistryName(b.getRegistryName()));
		}
		e.getRegistry().register(new ItemBlock(VITALITY_SPREADER, new Item.Properties().defaultMaxDamage(15)).setRegistryName(VITALITY_SPREADER.getRegistryName()));
	}

}
