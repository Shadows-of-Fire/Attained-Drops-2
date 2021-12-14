package shadows.attained.blocks;

import java.util.List;
import java.util.Locale;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import shadows.attained.AttainedDrops;
import shadows.attained.api.IAttainedType;
import shadows.attained.api.ITypedBlock;
import shadows.attained.api.PlantingRegistry;

public class BlockSoil extends Block implements ITypedBlock {

	public static final Properties PROPS = Properties.of(Material.DIRT).sound(SoundType.GRAVEL).strength(0.7F, 4);

	public final IAttainedType type;

	public BlockSoil(IAttainedType type) {
		super(PROPS);
		setRegistryName(AttainedDrops.MODID, type.name().toLowerCase(Locale.ROOT) + "_soil");
		this.type = type;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult res) {
		ItemStack stack = player.getItemInHand(hand);
		IAttainedType type = PlantingRegistry.byStack(stack);

		if (type != null && this.type == DefaultTypes.NONE) {
			if (!world.isClientSide) {
				world.setBlockAndUpdate(pos, PlantingRegistry.SOILS.get(type).defaultBlockState());
				if (!player.isCreative()) stack.shrink(1);
			}
			world.playSound(player, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 0.5F, 1.0F);
			return InteractionResult.SUCCESS;
		}

		if (hand == InteractionHand.MAIN_HAND && stack.isEmpty() && world.isClientSide) {
			if (this.type == DefaultTypes.NONE) {
				player.sendMessage(new TranslatableComponent("phrase.attained_drops.blank"), Util.NIL_UUID);
				return InteractionResult.SUCCESS;
			}
			player.sendMessage(new TranslatableComponent("phrase.attained_drops.vitalized", this.type.getDrop().getHoverName()), Util.NIL_UUID);
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, BlockGetter world, List<Component> list, TooltipFlag flag) {
		if (this.type == DefaultTypes.NONE) {
			list.add(new TranslatableComponent("tooltip.attained_drops.unenriched"));
		} else {
			list.add(new TranslatableComponent("tooltip.attained_drops.enrichedwith", type.getDrop().getHoverName()));
		}
	}

	@Override
	public String getDescriptionId() {
		if (type == DefaultTypes.NONE) return "block.attained_drops.soil";
		else return "block.attained_drops.enriched.soil";
	}

	@Override
	public int getColor() {
		return type.getColor();
	}

	public boolean isCustom() {
		return type instanceof CustomBulbType;
	}

}
