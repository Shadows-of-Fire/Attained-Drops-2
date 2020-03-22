package shadows.attained;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;
import shadows.attained.api.PlantingRegistry;
import shadows.attained.blocks.BlockPlant;
import shadows.attained.blocks.BlockVitalitySpreader;
import shadows.attained.items.ItemSeed;

@EventBusSubscriber(modid = AttainedDrops.MODID, bus = Bus.MOD)
public class AttainedRegistry {

	@ObjectHolder("attained_drops:seed")
	public static final ItemSeed SEED = null;
	@ObjectHolder("attained_drops:life_essence")
	public static final Item LIFE_ESSENCE = null;
	@ObjectHolder("attained_drops:vitality_spreader")
	public static final BlockVitalitySpreader VITALITY_SPREADER = null;
	@ObjectHolder("attained_drops:plant")
	public static final BlockPlant PLANT = null;

	public static void initRecipes() {
		AttainedDrops.RECIPES.addShapeless(new ItemStack(AttainedRegistry.SEED, 2), AttainedRegistry.LIFE_ESSENCE, Items.APPLE, Items.WHEAT_SEEDS);
		AttainedDrops.RECIPES.addShaped(AttainedRegistry.VITALITY_SPREADER, 3, 3, null, AttainedRegistry.SEED, null, Items.DIAMOND, Blocks.DIRT, Items.DIAMOND, null, Items.WATER_BUCKET, null);
	}

	@SubscribeEvent
	public static void onBlockRegister(Register<Block> e) {
		PlantingRegistry.load();
		e.getRegistry().registerAll(PlantingRegistry.SOILS.values().toArray(new Block[0]));
		e.getRegistry().registerAll(PlantingRegistry.BULBS.values().toArray(new Block[0]));
		e.getRegistry().registerAll(new BlockVitalitySpreader(), new BlockPlant());
	}

	@SubscribeEvent
	public static void onItemRegister(Register<Item> e) {
		Item.Properties props = new Item.Properties().group(AttainedDrops.GROUP);
		e.getRegistry().registerAll(new ItemSeed(props), new Item(props).setRegistryName(AttainedDrops.MODID, "life_essence"));
		for (Block b : PlantingRegistry.SOILS.values()) {
			e.getRegistry().register(new BlockItem(b, props).setRegistryName(b.getRegistryName()));
		}
		for (Block b : PlantingRegistry.BULBS.values()) {
			e.getRegistry().register(new BlockItem(b, props) {
				@Override
				public ITextComponent getDisplayName(ItemStack s) {
					return new TranslationTextComponent(b.getTranslationKey());
				};
			}.setRegistryName(b.getRegistryName()));
		}
		e.getRegistry().register(new BlockItem(VITALITY_SPREADER, new Item.Properties().group(AttainedDrops.GROUP).defaultMaxDamage(15)).setRegistryName(VITALITY_SPREADER.getRegistryName()));
	}

}
