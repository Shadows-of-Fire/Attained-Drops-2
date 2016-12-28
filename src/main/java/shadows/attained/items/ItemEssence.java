package shadows.attained.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.init.ModGlobals;
import shadows.attained.init.ModRegistry;

public class ItemEssence extends Item {

	public ItemEssence() {
		setCreativeTab(ModRegistry.AD2_TAB);
		setUnlocalizedName(ModGlobals.MODID + ".itemessence");
		setRegistryName("itemessence");
		GameRegistry.register(this);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
