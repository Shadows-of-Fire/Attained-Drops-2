package shadows.attained.proxy;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shadows.attained.*;
import shadows.attained.config.ConfigHandler;
public class CommonProxy {
	
	
	
	public static String configPath;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {

		configPath = e.getModConfigurationDirectory() + "/attaineddrops/";
		// Initialization of blocks and items typically goes here:
		ConfigHandler.init(configPath);
		ModRegistry.init();
		RecipeRegistry.init();
	}
	
    @SubscribeEvent
    public void onMobDrop(LivingDropsEvent event) {
        if (event.getEntity() instanceof IMob) {
            int rand = event.getEntity().worldObj.rand.nextInt(10);
            if (rand == 0) {
                ItemStack dropItem = new ItemStack(ModRegistry.itemessence, 1);
                EntityItem drop = new EntityItem(event.getEntity().worldObj, event.getEntity().posX + 5.0D, event.getEntity().posY + 2.0D, event.getEntity().posZ + 5.0D, dropItem);
                event.getDrops().add(drop);
            }
        }
    }

	public void init(FMLInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(this);

	}

	public void postInit(FMLPostInitializationEvent e) {

	}

}
