package shadows.attained.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops2;
import shadows.attained.init.DataLists;
import shadows.attained.init.ModRegistry;
import shadows.attained.util.IHasModel;

public class ItemEssence extends Item implements IHasModel {

	private final String regname = "essence";

	public ItemEssence() {
		setCreativeTab(ModRegistry.AD2_TAB);
		setUnlocalizedName(AttainedDrops2.MODID + "." + regname);
		setRegistryName(regname);
		DataLists.ITEMS.add(this);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return EnumActionResult.PASS;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
