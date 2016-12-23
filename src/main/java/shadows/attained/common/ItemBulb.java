package shadows.attained.common;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.*;
import shadows.attained.*;

import javax.annotation.Nonnull;

public class ItemBulb extends ItemBlock {

	public ItemBulb(Block block) {
		super(block);
		hasSubtypes = true;
		setRegistryName("itembulb");
		setUnlocalizedName(AttainedDrops.MODID + ".itembulb");
		//setCreativeTab(ModRegistry.Attained);
		GameRegistry.register(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(@Nonnull Item item, @Nonnull CreativeTabs tab, @Nonnull List<ItemStack> list) {
		for (int i = 0; i < BlockBulb.MobDrops.length; ++i) {
			list.add(new ItemStack(ModRegistry.itembulb, 1, i));
		}
	}

	@Override
	public int getMetadata(int Meta) {
		return Meta;
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		int meta = itemstack.getItemDamage();
		if (meta < 0 || meta >= BlockBulb.MobDrops.length) {
			meta = 0;
		}
		return super.getUnlocalizedName() + "." + BlockBulb.MobDrops[meta].getUnlocalizedName();
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		for (int i = 0; i < BlockBulb.MobDrops.length; ++i) {
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(getRegistryName(), "type=" + i));
		}
	}

}
