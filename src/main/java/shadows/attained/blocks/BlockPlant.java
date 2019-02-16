package shadows.attained.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDirtSnowy;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import shadows.attained.AttainedDrops;
import shadows.attained.init.AD2Config;
import shadows.attained.init.ModRegistry;
import shadows.attained.util.ParticleMessage;

public class BlockPlant extends BlockBush implements IGrowable {

	public static final IntegerProperty AGE = BlockCrops.AGE;
	public static final IntegerProperty BULBS = IntegerProperty.create("bulbs", 0, 4);
	public static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D) };
	public static final Properties PROPS = Properties.create(Material.PLANTS).hardnessAndResistance(0.2F, 0).sound(SoundType.PLANT);

	public BlockPlant() {
		super(PROPS);
		setRegistryName(AttainedDrops.MODID, "plant");
		setDefaultState(stateContainer.getBaseState().with(AGE, 0).with(BULBS, 0));

	}

	@Override
	protected boolean isValidGround(IBlockState state, IBlockReader world, BlockPos pos) {
		return state.getBlock() instanceof BlockSoil;
	}

	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return SHAPE_BY_AGE[state.get(AGE)];
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
		return AD2Config.allowBonemeal.get();
	}

	@Override
	public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune) {
		if (world.rand.nextFloat() >= 0.3) {
			drops.add(new ItemStack(ModRegistry.SEED));
		}
	}

	@Override
	public VoxelShape getCollisionShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return VoxelShapes.empty();
	}

	@Override
	public boolean canGrow(IBlockReader world, BlockPos pos, IBlockState state, boolean bool) {
		int age = getAge(world.getBlockState(pos));
		return age < this.getMaxAge() || (age == getMaxAge() && world.getBlockState(pos).isAir(world, pos));
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		if (world.isRemote) return;
		int age = getAge(state);
		if (age < getMaxAge()) {
			setAge(world, pos, getAge(state) + 1);
			return;
		}

		Block down = world.getBlockState(pos.down()).getBlock();
		if (down instanceof BlockSoil && isMaxAge(state) && world.isAirBlock(pos.up())) {
			BlockBulb place = ModRegistry.SOIL_TO_BULB.get(((BlockSoil) down).type);
			AttainedDrops.sendToTracking(new ParticleMessage(pos.up(), place.type.getColor(), 1), (WorldServer) world, pos);
			world.setBlockState(pos.up(), place.getDefaultState());
			int bulbsGrown = state.get(BULBS);
			if (bulbsGrown > 0 && rand.nextInt(5 - bulbsGrown) == 0) {
				world.setBlockState(pos.down(), (AD2Config.revertToDirt.get() ? Blocks.DIRT : ModRegistry.SOILS.get(SoilType.NONE)).getDefaultState());
				world.setBlockState(pos, state.with(BULBS, 0));
				AttainedDrops.sendToTracking(new ParticleMessage(pos.up(), EnumDyeColor.RED, 2), (WorldServer) world, pos);
			} else world.setBlockState(pos, state.with(BULBS, bulbsGrown + 1));
		}
	}

	@Override
	public void tick(IBlockState state, World world, BlockPos pos, Random random) {
		if (world.rand.nextInt(10) == 0 && canGrow(world, pos, state, true)) grow(world, world.rand, pos, state);
		else if (world.getBlockState(pos.down()).getBlock() instanceof BlockDirtSnowy && state.get(AGE) > 0) {
			world.setBlockState(pos, state.with(AGE, state.get(AGE) - 1));
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(ModRegistry.SEED);
	}

	@Override
	protected void fillStateContainer(Builder<Block, IBlockState> builder) {
		builder.add(AGE).add(BULBS);
	}

	public int getMaxAge() {
		return 7;
	}

	public int getAge(IBlockState state) {
		return state.get(AGE);
	}

	private void setAge(World world, BlockPos pos, int age) {
		world.setBlockState(pos, withAge(age), 2);
	}

	public IBlockState withAge(int age) {
		return getDefaultState().with(AGE, age);
	}

	public boolean isMaxAge(IBlockState state) {
		return state.get(AGE) == getMaxAge();
	}

}
