package shadows.attained;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shadows.attained.init.Config;
import shadows.attained.init.ModRegistry;
import shadows.attained.proxy.CommonProxy;

@Mod(modid = AttainedDrops2.MODID, version = AttainedDrops2.VERSION, name = AttainedDrops2.MODNAME, dependencies = AttainedDrops2.DEPS)
public class AttainedDrops2 {

	public static final String MODID = "attaineddrops2";
	public static final String MODNAME = "Attained Drops 2";
	public static final String VERSION = "3.2.0";
	public static final String DEPS = "";

	@SidedProxy(clientSide = "shadows.attained.proxy.ClientProxy", serverSide = "shadows.attained.proxy.CommonProxy")
	public static CommonProxy PROXY;

	@Instance
	public static AttainedDrops2 INSTANCE;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		PROXY.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(this);
		PROXY.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		PROXY.postInit(e);
	}

	@SubscribeEvent
	public void onMobDrop(LivingDropsEvent event) {
		if (event.getEntity() instanceof IMob) {
			int rand = event.getEntity().world.rand.nextInt(Config.dropChance);
			if (rand == 0) {
				ItemStack dropItem = new ItemStack(ModRegistry.ESSENCE, 1);
				EntityItem drop = new EntityItem(event.getEntity().world, event.getEntity().posX,
						event.getEntity().posY, event.getEntity().posZ, dropItem);
				event.getDrops().add(drop);
			}
		}
	}

}
