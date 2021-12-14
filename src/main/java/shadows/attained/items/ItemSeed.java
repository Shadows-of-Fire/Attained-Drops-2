package shadows.attained.items;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import shadows.attained.AttainedDrops;
import shadows.attained.AttainedRegistry;
import shadows.attained.blocks.BlockSoil;

public class ItemSeed extends Item {

	public ItemSeed(Item.Properties props) {
		super(props);
		setRegistryName(AttainedDrops.MODID, "seed");
	}

	@Override
	public ActionResultType useOn(ItemUseContext ctx) {
		World world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		PlayerEntity player = ctx.getPlayer();
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof BlockSoil && world.isEmptyBlock(pos.above())) {
			ItemStack stack = ctx.getItemInHand();
			if (player.mayUseItemAt(pos, ctx.getClickedFace(), stack) && ctx.getClickedFace() == Direction.UP) {
				world.setBlockAndUpdate(pos.above(), AttainedRegistry.PLANT.defaultBlockState());
				stack.shrink(1);
				world.playSound(player, pos, SoundEvents.GRASS_BREAK, SoundCategory.BLOCKS, (float) 0.6, (float) 1.0);
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag advanced) {
		list.add(new TranslationTextComponent("tooltip.attained_drops.plantvitalized"));
	}

}
