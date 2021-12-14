package shadows.attained.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
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
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
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
		ItemStack harvester = ctx.getOptionalParameter(LootContextParams.TOOL);
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
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
		if (!AttainedConfig.rightClickFarm) return InteractionResult.PASS;
		if (!world.isClientSide) {
			ItemStack drop = type.getDrop().copy();
			if (!player.addItem(drop)) popResource(world, pos, drop);
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_, CollisionContext p_220071_4_) {
		return Shapes.empty();
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return !world.isEmptyBlock(pos.below());
	}

	@Override
	public int getColor() {
		return type.getColor();
	}

	@Override
	public MutableComponent getName() {
		if (isCustom()) return new TranslatableComponent("block.attained_drops.custom_bulb", type.getDrop().getHoverName());
		return new TranslatableComponent(this.getDescriptionId());
	}

	public boolean isCustom() {
		return type instanceof CustomBulbType;
	}

}
