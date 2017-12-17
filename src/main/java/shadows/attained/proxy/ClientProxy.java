package shadows.attained.proxy;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import shadows.attained.AttainedDrops2;
import shadows.attained.integration.Waila;
import shadows.placebo.client.IHasModel;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void init(FMLInitializationEvent e) {
		if (Loader.isModLoaded("waila")) FMLInterModComms.sendMessage("waila", "register", Waila.class.getName() + ".callbackRegister");
	}

	@SubscribeEvent
	public void onModelRegister(ModelRegistryEvent e) {
		for (Block b : AttainedDrops2.INFO.getBlockList())
			if (b instanceof IHasModel) ((IHasModel) b).initModels(e);
		for (Item i : AttainedDrops2.INFO.getItemList())
			if (i instanceof IHasModel) ((IHasModel) i).initModels(e);
	}

	@SubscribeEvent
	public void onRenderShadows(PlayerTickEvent e) {
		if (e.player.world.isRemote && e.player.getName().equals("Shadows_of_Fire")) {
			Random rand = Minecraft.getMinecraft().world.rand;
			if (rand.nextInt(80) == 0) {
				BlockPos pos = e.player.getPosition();
				Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.END_ROD, true, pos.getX(), pos.getY() + 1.0D, pos.getZ(), MathHelper.nextDouble(rand, -0.05D, 0.05D), MathHelper.nextDouble(rand, 0.03D, 0.15D), MathHelper.nextDouble(rand, -0.05D, 0.05D));
			}
		}
	}

}
