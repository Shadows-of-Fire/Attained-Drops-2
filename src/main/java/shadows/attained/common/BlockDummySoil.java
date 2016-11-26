package shadows.attained.common;

import javax.annotation.Nullable;

import shadows.attained.util.BulbHelper;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops;
import shadows.attained.ModRegistry;

public class BlockDummySoil extends Block {
	public BlockDummySoil(){
		super(Material.GROUND);
		setRegistryName("dummysoil");
		setHardness(0.8F);
		setCreativeTab(ModRegistry.Attained);
		setSoundType(SoundType.GROUND);
		setUnlocalizedName(AttainedDrops.MODID + "dummysoil");
		setDefaultState(getStateFromMeta(0));
		GameRegistry.register(this);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		IBlockState k = world.getBlockState(pos);
		int xp = player.experienceLevel;
		if (player.inventory.getCurrentItem() != null)
		{
			for (int i = 0; i < BlockBulb.MobDrops.length; i++)
			{
				if (canPlayerEnrich(world, pos, player, k, i, xp) == true)
				{
					world.setBlockState(pos, getStateFromMeta(i + 1), 2);
					world.playSound(player, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, (float) 0.6, (float) 0.8);
					if (player.capabilities.isCreativeMode == false)
					{
						player.inventory.decrStackSize(player.inventory.currentItem, 1);
						player.experienceLevel = (xp - BulbHelper.getXPUse(i));
					}
					return true;
				}
			}
		}
		else if (world.isRemote)
		{
			int mx = getMetaFromState(k);
			if (mx != 0)
			{
				player.addChatComponentMessage(new TextComponentString(I18n.translateToLocal("phrase.AttainedDrops.DirtStart")
						+ TextFormatting.GREEN
						+ I18n.translateToLocal(BlockBulb.MobDrops[mx - 1].getUnlocalizedNameInefficiently(new ItemStack(
								BlockBulb.MobDrops[mx - 1])) + ".name")));
			}
			else
			{
				player.addChatMessage(new TextComponentString(I18n.translateToLocal("phrase.AttainedDrops.DirtBlank")));
			}
		}
		return false;
	}
	
	public boolean canPlayerEnrich(World world, BlockPos pos, EntityPlayer player, IBlockState state, int dropNumber, int xp)
	{
		if (player.inventory.getCurrentItem().getItem() == BlockBulb.MobDrops[dropNumber])
		{
			if (getMetaFromState(state) != (dropNumber + 1) && BulbHelper.isDropEnabled(dropNumber))
			{
				if (xp >= BulbHelper.getXPUse(dropNumber) || player.capabilities.isCreativeMode)
				{
					return true;
				}
			}
			chatHelper(world, pos, player, dropNumber, xp);
		}

		return false;
	}	
	public static void chatHelper(World world, BlockPos pos, EntityPlayer player, int dropNumber, int xp)
	{
		if (world.isRemote)
		{
			if (BulbHelper.isDropEnabled(dropNumber) == false)
			{
				player.addChatMessage(new TextComponentString(I18n.translateToLocal("phrase.AttainedDrops.DisabledBulbChat")));
			}
			if (xp < BulbHelper.getXPUse(dropNumber))
			{
				player.addChatMessage(new TextComponentString(I18n.translateToLocal("phrase.AttainedDrops.ShortXPChat")));
			}
		}
	}
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("dummysoil", "inventory"));
	}
	

}