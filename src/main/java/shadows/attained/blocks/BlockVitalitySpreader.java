package shadows.attained.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.IPlantable;
import shadows.attained.AttainedConfig;
import shadows.attained.AttainedDrops;
import shadows.attained.api.PlantingRegistry;
import shadows.attained.util.ParticleMessage;
import shadows.placebo.util.NetworkUtils;

public class BlockVitalitySpreader extends Block {

	public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 15);
	public static final Properties PROPS = Properties.create(Material.EARTH).hardnessAndResistance(0.5F, 12).sound(SoundType.GROUND).tickRandomly();

	public BlockVitalitySpreader() {
		super(PROPS);
		setRegistryName(AttainedDrops.MODID, "vitality_spreader");
		setDefaultState(stateContainer.getBaseState().with(CHARGE, 15));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return getDefaultState().with(CHARGE, 15 - ctx.getItem().getDamage());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder ctx) {
		List<ItemStack> drops = new ArrayList<>();
		ItemStack s = new ItemStack(this);
		s.setDamage(15 - state.get(CHARGE));
		drops.add(s);
		return drops;
	}

	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		if (rand.nextInt(20 - state.get(CHARGE)) == 0) {
			for (int i = 0; i < rand.nextInt(9); i++)
				world.addParticle(ParticleTypes.END_ROD, pos.getX() + 0.5D, pos.getY() + 1.02D, pos.getZ() + 0.5D, MathHelper.nextDouble(rand, -0.05, 0.05), 0.06D, MathHelper.nextDouble(rand, -0.05, 0.05));
		}
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(CHARGE);
	}

	@Override
	public void tick(BlockState state, World world, BlockPos pos, Random rand) {
		if (!world.isRemote) {
			genNewSoil(world, pos, state, rand);
			((ServerWorld) world).getPendingBlockTicks().scheduleTick(pos, this, 40);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (!world.isRemote) ((ServerWorld) world).getPendingBlockTicks().scheduleTick(pos, this, 40);
	}

	private void genNewSoil(World world, BlockPos pos, BlockState state, Random rand) {
		int radius = AttainedConfig.INSTANCE.spreaderRadius.get();
		BlockPos pos2 = pos.add(MathHelper.nextInt(rand, radius * -1, radius), 0, MathHelper.nextInt(rand, radius * -1, radius));
		if (world.getBlockState(pos2).getBlock().canSustainPlant(world.getBlockState(pos2), world, pos2, Direction.UP, (IPlantable) Blocks.DANDELION)) {
			world.setBlockState(pos2, PlantingRegistry.SOILS.get(DefaultTypes.NONE).getDefaultState());
			if (rand.nextBoolean()) {
				if (state.get(CHARGE) == 0) {
					world.setBlockState(pos, Blocks.DIRT.getDefaultState());
					NetworkUtils.sendToTracking(AttainedDrops.CHANNEL, new ParticleMessage(pos.up(), DyeColor.RED.colorValue, 2), (ServerWorld) world, pos);
				} else world.setBlockState(pos, state.with(CHARGE, state.get(CHARGE) - 1));
			}
			NetworkUtils.sendToTracking(AttainedDrops.CHANNEL, new ParticleMessage(pos2, DyeColor.GREEN.colorValue), (ServerWorld) world, pos);
		}
	}
}
