package shadows.attained.common;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops;
import shadows.attained.ModRegistry;

public class ItemBulb extends ItemBlock
{

	public ItemBulb(Block block)
	{
		super(block);
		this.hasSubtypes = true;
		setRegistryName("itembulb");
		setUnlocalizedName(AttainedDrops.MODID + ".itembulb");
		//setCreativeTab(ModRegistry.Attained);
		GameRegistry.register(this);
	}
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list)
    {
		for (int i = 0; i < BlockBulb.MobDrops.length; ++i)
		{
			list.add(new ItemStack(ModRegistry.itembulb, 1, i));
		}
    }

	@Override
	public int getMetadata(int Meta)
	{
		return Meta;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		int meta = itemstack.getItemDamage();
		if (meta < 0 || meta >= BlockBulb.MobDrops.length)
		{
			meta = 0;
		}
		return super.getUnlocalizedName() + "." + BlockBulb.MobDrops[meta].getUnlocalizedName();
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
