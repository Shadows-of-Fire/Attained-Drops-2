package shadows.attained.common;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops;
import shadows.attained.ModRegistry;

import org.lwjgl.input.Keyboard;


public class ItemSeed extends Item
{
	public ItemSeed()
	{
		super();
		setCreativeTab(ModRegistry.Attained);
		setUnlocalizedName(AttainedDrops.MODID + ".itemseed");
		setRegistryName("itemseed");
		GameRegistry.register(this);
	}

	@Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)

	{
		if (world.getBlockState(pos).getBlock() == ModRegistry.dummysoil && world.isAirBlock(pos.up()))
		{
			if (player.canPlayerEdit(pos, facing, stack) && facing == EnumFacing.UP)
				world.setBlockState(pos.up(), ModRegistry.blockplant.getDefaultState());
			--stack.stackSize;
			world.playSound(player, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, (float) 0.6, (float) 1.0);
			return EnumActionResult.SUCCESS;
		}

		else
		{
			return EnumActionResult.FAIL;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean useExtraInformation)
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			list.add("Plant on vitalized soil");
		}
		else
		{
			list.add(TextFormatting.GRAY + "Hold Shift");
		}
	}
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
