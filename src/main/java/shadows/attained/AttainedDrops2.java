package shadows.attained;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import shadows.attained.init.Config;
import shadows.attained.init.ModRegistry;
import shadows.attained.proxy.CommonProxy;
import shadows.attained.util.AD2Tab;
import shadows.attained.util.ParticleMessage;
import shadows.attained.util.ParticleMessage.ParticleMessageHandler;
import shadows.placebo.registry.RegistryInformation;
import shadows.placebo.util.RecipeHelper;

@Mod(modid = AttainedDrops2.MODID, version = AttainedDrops2.VERSION, name = AttainedDrops2.MODNAME, dependencies = AttainedDrops2.DEPS)
public class AttainedDrops2 {

	public static final String MODID = "attaineddrops2";
	public static final String MODNAME = "Attained Drops 2";
	public static final String VERSION = "3.7.3";
	public static final String DEPS = "required-after:placebo@[1.2.0,)";

	@SidedProxy(clientSide = "shadows.attained.proxy.ClientProxy", serverSide = "shadows.attained.proxy.CommonProxy")
	public static CommonProxy PROXY;

	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(AttainedDrops2.MODID);

	public static final CreativeTabs TAB = new AD2Tab();

	public static final RegistryInformation INFO = new RegistryInformation(AttainedDrops2.MODID, TAB);
	public static final RecipeHelper HELPER = new RecipeHelper(MODID, MODNAME, INFO.getRecipeList());

	public static Configuration config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		config = new Configuration(e.getSuggestedConfigurationFile());
		Config.syncConfig(config);
		MinecraftForge.EVENT_BUS.register(new ModRegistry());
		PROXY.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(this);
		NETWORK.registerMessage(ParticleMessageHandler.class, ParticleMessage.class, 0, Side.CLIENT);
		PROXY.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		PROXY.postInit(e);
	}

	@SubscribeEvent
	public void onMobDrop(LivingDropsEvent event) {
		if (event.getEntity() instanceof IMob && event.getSource().getTrueSource() instanceof EntityPlayer && event.getEntity().world.rand.nextInt(MathHelper.clamp(Config.dropChance - event.getLootingLevel(), 1, Integer.MAX_VALUE)) == 0) {
			event.getDrops().add(new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, new ItemStack(ModRegistry.ESSENCE)));
		}
	}

}
