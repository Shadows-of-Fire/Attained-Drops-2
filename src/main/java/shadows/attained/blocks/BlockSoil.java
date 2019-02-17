package shadows.attained.blocks;

import java.util.List;
import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import shadows.attained.AttainedDrops;
import shadows.attained.AttainedRegistry;

public class BlockSoil extends Block {

	public static final Properties PROPS = Properties.create(Material.GROUND).sound(SoundType.GROUND).hardnessAndResistance(0.7F, 4);

	protected final SoilType type;

	public BlockSoil(SoilType type) {
		super(PROPS);
		setRegistryName(AttainedDrops.MODID, type.name().toLowerCase(Locale.ROOT) + "_soil");
		this.type = type;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		SoilType type = SoilType.byStack(stack);

		if (type != null && this.type == SoilType.NONE) {
			if (!world.isRemote) {
				world.setBlockState(pos, AttainedRegistry.SOILS.get(type).getDefaultState());
				world.playSound(player, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
				if (!player.isCreative()) stack.shrink(1);
			}
			return true;
		}

		if (hand == EnumHand.MAIN_HAND && stack.isEmpty() && world.isRemote) {
			if (this.type == SoilType.NONE) {
				player.sendMessage(new TextComponentTranslation("phrase.attained_drops.blank"));
				return true;
			}
			player.sendMessage(new TextComponentTranslation("phrase.attained_drops.vitalized", this.type));
			return true;
		}

		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, IBlockReader world, List<ITextComponent> list, ITooltipFlag flag) {
		EntityPlayer player = Minecraft.getInstance().player;
		if (this.type == SoilType.NONE) {
			if (player != null && player.isSneaking() || Minecraft.getInstance().gameSettings.keyBindSneak.isKeyDown()) {
				list.add(new TextComponentTranslation("tooltip.attained_drops.enableditems"));
				String string = "";
				for (BulbType type : BulbType.values()) {
					string = string.concat(type.getDrop().getDisplayName() + ", ");
				}
				list.add(new TextComponentString(string.substring(0, string.length() - 2)));
			} else {
				list.add(new TextComponentTranslation("tooltip.attained_drops.holdshift", new TextComponentTranslation(Minecraft.getInstance().gameSettings.keyBindSneak.getTranslationKey())));
			}
		} else {
			list.add(new TextComponentTranslation("tooltip.attained_drops.enrichedwith", type.getDrop().getDisplayName()));
		}
	}

	@Override
	public String getTranslationKey() {
		if (type == SoilType.NONE) return "block.attained_drops.soil";
		else return "block.attained_drops.enriched.soil";
	}

}
