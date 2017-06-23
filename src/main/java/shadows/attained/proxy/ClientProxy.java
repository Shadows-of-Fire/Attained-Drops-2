package shadows.attained.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shadows.attained.init.DataLists;
import shadows.attained.integration.Waila;
import shadows.attained.util.IHasModel;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		if (Loader.isModLoaded("waila"))
			FMLInterModComms.sendMessage("waila", "register", Waila.class.getName() + ".callbackRegister");
	}

	@SubscribeEvent
	public void onModelRegistry(ModelRegistryEvent event) {
		for (Item item : DataLists.ITEMS) {
			if (item instanceof IHasModel)
				((IHasModel) item).initModel();
		}
		for (Block block : DataLists.BLOCKS) {
			if (block instanceof IHasModel)
				((IHasModel) block).initModel();
		}
	}

	@Override
	public World getWorld() {
		return Minecraft.getMinecraft().world;
	}
}
