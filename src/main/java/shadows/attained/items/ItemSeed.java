package shadows.attained.items;

import java.util.List;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
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
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		Player player = ctx.getPlayer();
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof BlockSoil && world.isEmptyBlock(pos.above())) {
			ItemStack stack = ctx.getItemInHand();
			if (player.mayUseItemAt(pos, ctx.getClickedFace(), stack) && ctx.getClickedFace() == Direction.UP) {
				world.setBlockAndUpdate(pos.above(), AttainedRegistry.PLANT.defaultBlockState());
				stack.shrink(1);
				world.playSound(player, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, (float) 0.6, (float) 1.0);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag advanced) {
		list.add(new TranslatableComponent("tooltip.attained_drops.plantvitalized"));
	}

}
