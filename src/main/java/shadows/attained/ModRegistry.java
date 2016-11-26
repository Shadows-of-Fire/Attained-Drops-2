package shadows.attained;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.common.BlockBulb;
import shadows.attained.common.BlockDummySoil;
import shadows.attained.common.BlockPlant;
import shadows.attained.common.BlockVitalizedSoil;

public class ModRegistry {

    public static final CreativeTabs Attained = new CreativeTabs("ad")
    {
        @SideOnly(Side.CLIENT) @Override
        public Item getTabIconItem()
        {
            return Items.WHEAT_SEEDS;
        }
    };
	public static BlockBulb blockbulb;
	public static BlockVitalizedSoil vitalizedsoil;
	public static BlockDummySoil dummysoil;
	public static BlockPlant blockplant;
	
	public static void init(){
	blockbulb = new BlockBulb();	
	vitalizedsoil = new BlockVitalizedSoil();
	dummysoil = new BlockDummySoil();	
	blockplant = new BlockPlant();	
		
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels(){
	blockbulb.initModel();
	vitalizedsoil.initModel();
	dummysoil.initModel();
	blockplant.initModel();
		
	}
}
