package shadows.attained.items;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops2;
import shadows.attained.blocks.BlockVitalized;
import shadows.attained.init.ModRegistry;
import shadows.placebo.item.ItemBase;

public class ItemSeed extends ItemBase {

	public ItemSeed() {
		super("seed", AttainedDrops2.INFO);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof BlockVitalized && world.isAirBlock(pos.up())) {
			ItemStack stack = player.getHeldItem(hand);
			if (player.canPlayerEdit(pos, facing, stack) && facing == EnumFacing.UP) {
				world.setBlockState(pos.up(), ModRegistry.PLANT.getDefaultState());
				stack.shrink(1);
				world.playSound(player, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, (float) 0.6, (float) 1.0);
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag advanced) {
		KeyBinding sneak = Minecraft.getMinecraft().gameSettings.keyBindSneak;
		if (Keyboard.isKeyDown(sneak.getKeyCode())) {
			list.add(I18n.format("tooltip.attaineddrops2.plantvitalized"));
		} else {
			list.add(I18n.format("tooltip.attaineddrops2.holdshift", sneak.getDisplayName()));
		}
	}

}
