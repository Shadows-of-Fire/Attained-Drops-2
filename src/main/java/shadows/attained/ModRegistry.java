package shadows.attained;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.common.BlockBulb;
import shadows.attained.common.BlockVitalized;
import shadows.attained.common.BlockPlant;
import shadows.attained.common.BlockSoilCreator;
import shadows.attained.common.ItemBulb;
import shadows.attained.common.ItemEssence;
import shadows.attained.common.ItemSeed;
import shadows.attained.util.AttainedTab;

public class ModRegistry {
	
    public static final AttainedTab Attained = new AttainedTab("ad")
    {

		 @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return itemseed;
        }
    };
	public static BlockBulb blockbulb;
	public static BlockSoilCreator soilcreator;
	public static BlockVitalized vitalized;
	public static BlockPlant blockplant;
	public static ItemBulb itembulb;
	public static ItemSeed itemseed;
	public static ItemEssence itemessence;
	
	public static void init(){
	blockbulb = new BlockBulb();	
	soilcreator = new BlockSoilCreator();
	vitalized = new BlockVitalized();	
	blockplant = new BlockPlant();	
	itembulb = new ItemBulb(blockbulb);
	itemseed = new ItemSeed();
	itemessence = new ItemEssence();
	
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels(){
	blockbulb.initModel();
	soilcreator.initModel();
	vitalized.initModel();
	blockplant.initModel();
	itemseed.initModel();
	itembulb.initModel();
	itemessence.initModel();
		
	}
	
	
}
