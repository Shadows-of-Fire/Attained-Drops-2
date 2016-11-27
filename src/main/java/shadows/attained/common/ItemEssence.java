package shadows.attained.common;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops;
import shadows.attained.ModRegistry;

public class ItemEssence extends Item{

	public ItemEssence()
	{
		setCreativeTab(ModRegistry.Attained);
		setUnlocalizedName(AttainedDrops.MODID + ".itemessence");
		setRegistryName("itemessence");
		GameRegistry.register(this);
	}
	
	
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
