package shadows.attained.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import shadows.attained.AttainedConfig;
import shadows.attained.AttainedDrops;
import shadows.attained.api.IAttainedType;
import shadows.attained.api.ITypedBlock;

public class BlockBulb extends BushBlock implements ITypedBlock {

	public static final VoxelShape SHAPE = box(5, 0, 5, 11, 8, 11);
	public static final Properties PROPS = Properties.of(Material.PLANT).sound(SoundType.GRASS).strength(0.4F, 0);

	protected final IAttainedType type;

	public BlockBulb(IAttainedType type) {
		super(PROPS);
		setRegistryName(AttainedDrops.MODID, type.name().toLowerCase(Locale.ROOT) + "_bulb");
		this.type = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		if (rand.nextFloat() >= 0.6937F) {
			int color = type.getColor();
			EndRodParticle p = (EndRodParticle) Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.END_ROD, pos.getX() + 1.0D - rand.nextDouble(), pos.getY() + 0.4D, pos.getZ() + 1.0D - rand.nextDouble(), 0, 0.03D, 0);
			p.setColor(color);
			p.setFadeColor(color);
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder ctx) {
		List<ItemStack> drops = new ArrayList<>();
		ItemStack harvester = ctx.getOptionalParameter(LootParameters.TOOL);
		if (harvester.getItem() == Items.SHEARS || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, harvester) > 0) {
			drops.add(new ItemStack(this));
			return drops;
		}
		ItemStack drop = type.getDrop().copy();
		drops.add(drop);
		int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, harvester);
		if (fortune > 0 && RANDOM.nextInt(Math.max(1, 4 - fortune)) == 0) drops.add(drop.copy());
		return drops;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
		if (!AttainedConfig.rightClickFarm) return ActionResultType.PASS;
		if (!world.isClientSide) {
			ItemStack drop = type.getDrop().copy();
			if (!player.addItem(drop)) popResource(world, pos, drop);
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
		return VoxelShapes.empty();
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
		return !world.isEmptyBlock(pos.below());
	}

	@Override
	public int getColor() {
		return type.getColor();
	}

	@Override
	public IFormattableTextComponent getName() {
		if (isCustom()) return new TranslationTextComponent("block.attained_drops.custom_bulb", type.getDrop().getHoverName());
		return new TranslationTextComponent(this.getDescriptionId());
	}

	public boolean isCustom() {
		return type instanceof CustomBulbType;
	}

}
