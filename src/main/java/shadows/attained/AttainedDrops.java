package shadows.attained;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import shadows.attained.proxy.CommonProxy;

@Mod(modid = AttainedDrops.MODID, version = AttainedDrops.VERSION, name = AttainedDrops.MODNAME)


public class AttainedDrops {
    public static final String MODID = "attaineddrops";
    public static final String MODNAME = "Attained Drops";
    public static final String VERSION = "2.0.0";

    @SidedProxy(clientSide = "shadows.attained.proxy.ClientProxy", serverSide = "shadows.attained.proxy.ServerProxy")
    public static CommonProxy proxy;
	
    @Mod.Instance
    public static AttainedDrops instance;
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	proxy.preInit(event);
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
    	proxy.init(e);
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {
    	proxy.postInit(e);
    }


}
