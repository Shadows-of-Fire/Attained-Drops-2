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
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import shadows.attained.AttainedConfig;
import shadows.attained.AttainedDrops;
import shadows.attained.AttainedRegistry;
import shadows.attained.util.ParticleMessage;

public class BlockPlant extends BushBlock implements IGrowable {

	public static final IntegerProperty AGE = CropsBlock.AGE;
	public static final IntegerProperty BULBS = IntegerProperty.create("bulbs", 0, 4);
	public static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 4.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 12.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D) };
	public static final Properties PROPS = Properties.create(Material.PLANTS).hardnessAndResistance(0.2F, 0).sound(SoundType.PLANT).tickRandomly();

	public BlockPlant() {
		super(PROPS);
		setRegistryName(AttainedDrops.MODID, "plant");
		setDefaultState(stateContainer.getBaseState().with(AGE, 0).with(BULBS, 0));

	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos pos) {
		return state.getBlock() instanceof BlockSoil;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE_BY_AGE[state.get(AGE)];
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, BlockState state) {
		return AttainedConfig.INSTANCE.allowBonemeal.get();
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

	@Override
	public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean bool) {
		int age = getAge(world.getBlockState(pos));
		return age < this.getMaxAge() || (age == getMaxAge() && world.getBlockState(pos.up()).isAir(world, pos.up()));
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, BlockState state) {
		if (world.isRemote) return;
		int age = getAge(state);
		if (age < getMaxAge()) {
			setAge(world, pos, getAge(state) + 1);
			return;
		}

		Block down = world.getBlockState(pos.down()).getBlock();
		if (down instanceof BlockSoil && isMaxAge(state) && world.isAirBlock(pos.up())) {
			BlockBulb place = AttainedRegistry.SOIL_TO_BULB.get(((BlockSoil) down).type);
			if (place == null) return;
			AttainedDrops.sendToTracking(new ParticleMessage(pos.up(), place.type.getColor(), 1), (ServerWorld) world, pos);
			world.setBlockState(pos.up(), place.getDefaultState());
			int bulbsGrown = state.get(BULBS);
			if (bulbsGrown > 0 && rand.nextInt(5 - bulbsGrown) == 0) {
				world.setBlockState(pos.down(), (AttainedConfig.INSTANCE.revertToDirt.get() ? Blocks.DIRT : AttainedRegistry.SOILS.get(SoilType.NONE)).getDefaultState());
				world.setBlockState(pos, state.with(BULBS, 0));
				AttainedDrops.sendToTracking(new ParticleMessage(pos.up(), DyeColor.RED, 2), (ServerWorld) world, pos);
			} else world.setBlockState(pos, state.with(BULBS, bulbsGrown + 1));
		}
	}

	@Override
	public void tick(BlockState state, World world, BlockPos pos, Random random) {
		if (world.rand.nextInt(10) == 0 && canGrow(world, pos, state, true)) grow(world, world.rand, pos, state);
		else if (world.getBlockState(pos.down()).getBlock() instanceof SnowyDirtBlock && state.get(AGE) > 0) {
			world.setBlockState(pos, state.with(AGE, state.get(AGE) - 1));
		}
	}

	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
		return new ItemStack(AttainedRegistry.SEED);
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(AGE).add(BULBS);
	}

	public int getMaxAge() {
		return 7;
	}

	public int getAge(BlockState state) {
		return state.get(AGE);
	}

	private void setAge(World world, BlockPos pos, int age) {
		world.setBlockState(pos, withAge(age), 2);
	}

	public BlockState withAge(int age) {
		return getDefaultState().with(AGE, age);
	}

	public boolean isMaxAge(BlockState state) {
		return state.get(AGE) == getMaxAge();
	}

}
