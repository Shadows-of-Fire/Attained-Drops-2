package shadows.attained;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import shadows.attained.api.ITypedBlock;
import shadows.attained.api.PlantingRegistry;
import shadows.placebo.statemap.ModelMapRegistry;

@EventBusSubscriber(modid = AttainedDrops.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class AttainedClient {

	static ModelResourceLocation bulb = new ModelResourceLocation(AttainedDrops.MODID + ":custom_bulb", "");
	static ModelResourceLocation soil = new ModelResourceLocation(AttainedDrops.MODID + ":custom_soil", "");
	static ModelResourceLocation bulbI = new ModelResourceLocation(AttainedDrops.MODID + ":custom_bulb", "inventory");
	static ModelResourceLocation soilI = new ModelResourceLocation(AttainedDrops.MODID + ":custom_soil", "inventory");

	@SubscribeEvent
	public static void models(ModelRegistryEvent e) {
		ModelLoader.addSpecialModel(bulb);
		ModelLoader.addSpecialModel(soil);
		ModelLoader.addSpecialModel(bulbI);
		ModelLoader.addSpecialModel(soilI);
		PlantingRegistry.BULBS.values().stream().filter(b -> b.isCustom()).forEach(b -> {
			ModelMapRegistry.registerBlockMap(b, bl -> bulb);
			ModelMapRegistry.registerItemMap(b.asItem(), i -> bulbI);
		});
		PlantingRegistry.SOILS.values().stream().filter(b -> b.isCustom()).forEach(s -> {
			ModelMapRegistry.registerBlockMap(s, sl -> soil);
			ModelMapRegistry.registerItemMap(s.asItem(), i -> soilI);
		});
		RenderTypeLookup.setRenderLayer(AttainedRegistry.PLANT, RenderType.cutout());
		PlantingRegistry.SOILS.values().forEach(b -> RenderTypeLookup.setRenderLayer(b, RenderType.cutout()));
	}

	@SubscribeEvent
	public static void colors(ColorHandlerEvent.Item e) {
		List<Item> items = new ArrayList<>();
		PlantingRegistry.BULBS.values().stream().filter(b -> b.isCustom()).map(b -> b.asItem()).forEach(items::add);
		PlantingRegistry.SOILS.values().stream().filter(b -> b.isCustom()).map(b -> b.asItem()).forEach(items::add);
		e.getItemColors().register((i, tint) -> tint == 1 ? ((ITypedBlock) ((BlockItem) i.getItem()).getBlock()).getColor() : -1, items.toArray(new Item[0]));
	}

	@SubscribeEvent
	public static void colors(ColorHandlerEvent.Block e) {
		List<Block> blocks = new ArrayList<>();
		PlantingRegistry.BULBS.values().stream().filter(b -> b.isCustom()).forEach(blocks::add);
		PlantingRegistry.SOILS.values().stream().filter(b -> b.isCustom()).forEach(blocks::add);
		e.getBlockColors().register((state, env, p, tint) -> tint == 1 ? ((ITypedBlock) state.getBlock()).getColor() : -1, blocks.toArray(new Block[0]));
	}

}
