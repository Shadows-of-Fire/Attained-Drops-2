package shadows.attained;

import java.util.List;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.common.BlockBulb;
import shadows.attained.common.BlockDummySoil;
import shadows.attained.common.BlockPlant;
import shadows.attained.common.BlockVitalizedSoil;
import shadows.attained.common.ItemBulb;
import shadows.attained.common.ItemDummySoil;
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
	public static BlockVitalizedSoil vitalizedsoil;
	public static BlockDummySoil dummysoil;
	public static BlockPlant blockplant;
	public static ItemBulb itembulb;
	public static ItemDummySoil itemsoil;
	public static ItemSeed itemseed;
	
	public static void init(){
	blockbulb = new BlockBulb();	
	vitalizedsoil = new BlockVitalizedSoil();
	dummysoil = new BlockDummySoil();	
	blockplant = new BlockPlant();	
	itembulb = new ItemBulb(blockbulb);
	itemsoil = new ItemDummySoil(dummysoil);	
	itemseed = new ItemSeed();
	
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels(){
	blockbulb.initModel();
	vitalizedsoil.initModel();
	dummysoil.initModel();
	blockplant.initModel();
	itemsoil.initModel();
	itemseed.initModel();
	itembulb.initModel();
		
	}
	
	
}
