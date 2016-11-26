package shadows.attained;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.common.BlockBulb;

public class ModRegistry {

    public static final CreativeTabs Attained = new CreativeTabs("ad")
    {
        @SideOnly(Side.CLIENT) @Override
        public Item getTabIconItem()
        {
            return Items.WHEAT_SEEDS;
        }
    };
	public static Block blockbulb;
	public static Block vitalizedsoil;
	public static Block dummysoil;
	
	public static void init(){
		
		
		
		
		
	}
}
