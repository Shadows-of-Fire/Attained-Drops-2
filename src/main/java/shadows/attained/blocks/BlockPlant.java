package shadows.attained.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SnowyDirtBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import shadows.attained.AttainedConfig;
import shadows.attained.AttainedDrops;
import shadows.attained.AttainedRegistry;
import shadows.attained.api.PlantingRegistry;
import shadows.attained.util.ParticleMessage;
import shadows.placebo.util.NetworkUtils;

public class BlockPlant extends BushBlock implements IGrowable {

	public static final IntegerProperty AGE = CropsBlock.AGE;
	public static final IntegerProperty BULBS = IntegerProperty.create("bulbs", 0, 4);
	public static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.box(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 4.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 12.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D) };
	public static final Properties PROPS = Properties.of(Material.PLANT).strength(0.2F, 0).sound(SoundType.GRASS).randomTicks();

	public BlockPlant() {
		super(PROPS);
		setRegistryName(AttainedDrops.MODID, "plant");
		registerDefaultState(stateDefinition.any().setValue(AGE, 0).setValue(BULBS, 0));

	}

	@Override
	protected boolean mayPlaceOn(BlockState state, IBlockReader world, BlockPos pos) {
		return state.getBlock() instanceof BlockSoil;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE_BY_AGE[state.getValue(AGE)];
	}

	@Override
	public boolean isBonemealSuccess(World world, Random rand, BlockPos pos, BlockState state) {
		return AttainedConfig.allowBonemeal;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder ctx) {
		List<ItemStack> drops = new ArrayList<>();
		if (ThreadLocalRandom.current().nextFloat() >= 0.3) {
			drops.add(new ItemStack(AttainedRegistry.SEED));
		}
		return drops;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return VoxelShapes.empty();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValidBonemealTarget(IBlockReader world, BlockPos pos, BlockState state, boolean bool) {
		int age = getAge(world.getBlockState(pos));
		return age < this.getMaxAge() || age == getMaxAge() && world.getBlockState(pos.above()).isAir(world, pos.above());
	}

	@Override
	public void performBonemeal(ServerWorld world, Random rand, BlockPos pos, BlockState state) {
		if (world.isClientSide) return;
		int age = getAge(state);
		if (age < getMaxAge()) {
			setAge(world, pos, getAge(state) + 1);
			return;
		}

		Block down = world.getBlockState(pos.below()).getBlock();
		if (down instanceof BlockSoil && isMaxAge(state) && world.isEmptyBlock(pos.above())) {
			BlockBulb place = PlantingRegistry.BULBS.get(((BlockSoil) down).type);
			if (place == null) return;
			NetworkUtils.sendToTracking(AttainedDrops.CHANNEL, new ParticleMessage(pos.above(), place.type.getColor(), 1), world, pos);
			world.setBlockAndUpdate(pos.above(), place.defaultBlockState());
			int bulbsGrown = state.getValue(BULBS);
			if (bulbsGrown > 0 && rand.nextInt(5 - bulbsGrown) == 0) {
				world.setBlockAndUpdate(pos.below(), (AttainedConfig.revertToDirt ? Blocks.DIRT : PlantingRegistry.SOILS.get(DefaultTypes.NONE)).defaultBlockState());
				world.setBlockAndUpdate(pos, state.setValue(BULBS, 0));
				NetworkUtils.sendToTracking(AttainedDrops.CHANNEL, new ParticleMessage(pos.above(), DyeColor.RED.textureDiffuseColor, 2), world, pos);
			} else world.setBlockAndUpdate(pos, state.setValue(BULBS, bulbsGrown + 1));
		}
	}

	@Override
	@Deprecated
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		Block down = world.getBlockState(pos.below()).getBlock();
		if (down instanceof BlockSoil && PlantingRegistry.BULBS.get(((BlockSoil) down).type) == null) {
			return down.use(down.defaultBlockState(), world, pos.below(), player, handIn, hit);
		}
		return ActionResultType.PASS;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.random.nextInt(10) == 0 && isValidBonemealTarget(world, pos, state, true)) performBonemeal(world, world.random, pos, state);
		else if (world.getBlockState(pos.below()).getBlock() instanceof SnowyDirtBlock && !(world.getBlockState(pos.above()).getBlock() instanceof BlockBulb) && state.getValue(AGE) > 0) {
			world.setBlockAndUpdate(pos, state.setValue(AGE, state.getValue(AGE) - 1));
		}
	}

	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
		return new ItemStack(AttainedRegistry.SEED);
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(AGE).add(BULBS);
	}

	public int getMaxAge() {
		return 7;
	}

	public int getAge(BlockState state) {
		return state.getValue(AGE);
	}

	private void setAge(World world, BlockPos pos, int age) {
		world.setBlock(pos, withAge(age), 2);
	}

	public BlockState withAge(int age) {
		return defaultBlockState().setValue(AGE, age);
	}

	public boolean isMaxAge(BlockState state) {
		return state.getValue(AGE) == getMaxAge();
	}

}
