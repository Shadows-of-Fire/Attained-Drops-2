package shadows.attained.items;

import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.init.ModGlobals;
import shadows.attained.init.ModRegistry;
import shadows.attained.util.AD2Util;

public class ItemSeed extends Item {
	public ItemSeed() {
		super();
		setCreativeTab(ModRegistry.AD2_TAB);
		setUnlocalizedName(ModGlobals.MODID + ".itemseed");
		setRegistryName("itemseed");
		GameRegistry.register(this);
	}

	@Nonnull
	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		Block block = world.getBlockState(pos).getBlock();
		if (AD2Util.isSoil(block) && AD2Util.isSoilEnriched(AD2Util.getSoilFromBlock(block)) && world.isAirBlock(pos.up())) {
			ItemStack stack = player.getHeldItem(hand);
			if (player.canPlayerEdit(pos, facing, stack) && facing == EnumFacing.UP) {
				world.setBlockState(pos.up(), ModRegistry.BLOCK_PLANT.getDefaultState());
				stack.shrink(1);
				world.playSound(player, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, (float) 0.6, (float) 1.0);
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean useExtraInformation) {
		KeyBinding sneak = Minecraft.getMinecraft().gameSettings.keyBindSneak;
		if (player.isSneaking() || Keyboard.isKeyDown(sneak.getKeyCode())) {
			list.add(I18n.format("tooltip.attaineddrops.plantvitalized"));
		}
		else {
			list.add(I18n.format("tooltip.attaineddrops.holdshift", sneak.getDisplayName()));
		}
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
