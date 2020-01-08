package shadows.attained.blocks;

import java.util.List;
import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import shadows.attained.AttainedDrops;
import shadows.attained.api.IAttainedType;
import shadows.attained.api.ITypedBlock;
import shadows.attained.api.PlantingRegistry;

public class BlockSoil extends Block implements ITypedBlock {

	public static final Properties PROPS = Properties.create(Material.EARTH).sound(SoundType.GROUND).hardnessAndResistance(0.7F, 4);

	public final IAttainedType type;

	public BlockSoil(IAttainedType type) {
		super(PROPS);
		setRegistryName(AttainedDrops.MODID, type.name().toLowerCase(Locale.ROOT) + "_soil");
		this.type = type;
	}

	@Override
	public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult res) {
		ItemStack stack = player.getHeldItem(hand);
		IAttainedType type = PlantingRegistry.byStack(stack);

		if (type != null && this.type == DefaultTypes.NONE) {
			if (!world.isRemote) {
				world.setBlockState(pos, PlantingRegistry.SOILS.get(type).getDefaultState());
				world.playSound(player, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
				if (!player.isCreative()) stack.shrink(1);
			}
			return ActionResultType.CONSUME;
		}

		if (hand == Hand.MAIN_HAND && stack.isEmpty() && world.isRemote) {
			if (this.type == DefaultTypes.NONE) {
				player.sendMessage(new TranslationTextComponent("phrase.attained_drops.blank"));
				return ActionResultType.SUCCESS;
			}
			player.sendMessage(new TranslationTextComponent("phrase.attained_drops.vitalized", this.type.getDrop().getDisplayName()));
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, IBlockReader world, List<ITextComponent> list, ITooltipFlag flag) {
		if (this.type == DefaultTypes.NONE) {
			list.add(new TranslationTextComponent("tooltip.attained_drops.unenriched"));
		} else {
			list.add(new TranslationTextComponent("tooltip.attained_drops.enrichedwith", type.getDrop().getDisplayName()));
		}
	}

	@Override
	public String getTranslationKey() {
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
