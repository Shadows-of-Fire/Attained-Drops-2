package shadows.attained.common;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops;
import shadows.attained.ModRegistry;
import shadows.attained.util.BulbHelper;

import org.lwjgl.input.Keyboard;


public class ItemDummySoil extends ItemBlock
{

	public ItemDummySoil(Block block)
	{
		super(block);
		setCreativeTab(ModRegistry.Attained);
		setUnlocalizedName(AttainedDrops.MODID + ".itemsoil");
		setRegistryName("itemsoil");
		GameRegistry.register(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean useExtraInformation)
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			list.add("Right click with enabled drop to enrich");
			list.add("Enabled Items:");
			printItems(list);
		}
		else
		{
			list.add(TextFormatting.GRAY + "Hold Shift");
		}
	}

	public static void printItems(List<String> list)
	{
		String string = "";
		for (int i = 0; i < 4; i++)
		{
			if (BulbHelper.isDropEnabled(i))
			{
				string = string + BlockBulb.MobDrops[i].getItemStackDisplayName(new ItemStack(BlockBulb.MobDrops[i])) + ", ";
			}
		}
		list.add(TextFormatting.GREEN + string);
		string = "";
		for (int i = 4; i < BlockBulb.MobDrops.length; i++)
		{
			if (BulbHelper.isDropEnabled(i))
			{
				string = string + BlockBulb.MobDrops[i].getItemStackDisplayName(new ItemStack(BlockBulb.MobDrops[i])) + ", ";
			}
		}
		list.add(TextFormatting.GREEN + string);
		return;
	}
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
