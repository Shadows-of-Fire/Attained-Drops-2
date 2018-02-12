package shadows.attained.blocks;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import shadows.attained.AttainedDrops2;
import shadows.attained.init.Config;
import shadows.attained.init.ModRegistry;
import shadows.attained.util.ParticleMessage;
import shadows.placebo.client.IHasModel;

public class BlockPlant extends BlockBush implements IGrowable, IHasModel {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
	public static final PropertyInteger CHARGE = PropertyInteger.create("charge", 0, 4);
	public static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.125D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.25D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.375D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.5D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.625D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.75D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.875, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 1.0D, 0.84375D) };

	public BlockPlant() {
		setRegistryName(AttainedDrops2.MODID, "plant");
		setUnlocalizedName(AttainedDrops2.MODID + ".plant");
		setHardness(0.2F);
		setSoundType(SoundType.PLANT);
		AttainedDrops2.INFO.getBlockList().add(this);
		AttainedDrops2.INFO.getItemList().add(new ItemBlock(this).setRegistryName(getRegistryName()));
		setDefaultState(blockState.getBaseState().withProperty(AGE, 0).withProperty(CHARGE, 0));

	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		return canSustainBush(world.getBlockState(pos.down()));
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return CROPS_AABB[state.getValue(AGE)];
	}

	@Override
	public boolean canUseBonemeal(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		return Config.allowBonemeal && !isMaxAge(state);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> ret, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
		if (ThreadLocalRandom.current().nextFloat() >= 0.3) {
			ret.add(new ItemStack(ModRegistry.SEED));
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return world.getBlockState(pos.down()).getBlock() == ModRegistry.SOIL;
	}

	@Override
	public boolean canGrow(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean bool) {
		if (world.getBlockState(pos.down()).getBlock() == ModRegistry.SOIL) {
			int age = getAge(world.getBlockState(pos));
			return age < this.getMaxAge() || (world.rand.nextInt(1 + state.getValue(CHARGE)) == 0 && isMaxAge(state) && world.isAirBlock(pos.up()));
		}
		return false;
	}

	@Override
	public void grow(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		int age = getAge(state);
		if (age < getMaxAge()) {
			setAge(world, pos, getAge(state) + 1);
		}
		if (isMaxAge(state) && world.isAirBlock(pos.up())) {
			IBlockState place = BlockVitalized.getBulbFromState(world.getBlockState(pos.down()));
			if (place != null) {
				AttainedDrops2.NETWORK.sendToAllAround(new ParticleMessage(place.getValue(BlockBulb.BULB).getColor(), pos.up(), 1), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 30));
				world.setBlockState(pos.up(), place);
				int charge = state.getValue(CHARGE);
				world.setBlockState(pos, state.withProperty(CHARGE, charge + 1 > 4 ? 0 : charge + 1));
				if (charge > 0 && rand.nextInt(5 - charge) == 0) {
					world.setBlockState(pos.down(), world.getBlockState(pos.down()).getBlock().getDefaultState());
					world.setBlockState(pos, state.withProperty(CHARGE, 0));
					if (Config.revertToDirt && rand.nextInt(8 - charge) == 0) {
						if (world.getBlockState(pos.up()).getBlock() == ModRegistry.BULB) world.destroyBlock(pos.up(), true);
						world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState());
						AttainedDrops2.NETWORK.sendToAllAround(new ParticleMessage(EnumDyeColor.RED, pos.up(), 2), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 30));
					}
				}
			}
		}
	}

	@Override
	public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, Random rand) {
		if (canGrow(world, pos, state, true)) grow(world, world.rand, pos, state);
		else if (!canGrow(world, pos, state, true) && world.getBlockState(pos.down()).getBlock() instanceof BlockDirt && state.getValue(AGE) > 0) {
			world.setBlockState(pos, state.withProperty(AGE, state.getValue(AGE) - 1));
		}
	}

	@Override
	protected boolean canSustainBush(IBlockState state) {
		return state.getMaterial() == Material.GROUND || state.getMaterial() == Material.GRASS;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(ModRegistry.SEED);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = getDefaultState();
		if (meta > 7) {
			state = state.withProperty(AGE, 7).withProperty(CHARGE, meta - 7);
		} else state = withAge(meta);
		return state;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int k = state.getValue(AGE);
		if (k == 7) k += state.getValue(CHARGE);
		return k;

	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, AGE, CHARGE);
	}

	public int getMaxAge() {
		return 7;
	}

	public int getAge(IBlockState state) {
		return state.getValue(AGE);
	}

	private void setAge(World world, BlockPos pos, int age) {
		world.setBlockState(pos, withAge(age), 2);
	}

	public IBlockState withAge(int age) {
		return getDefaultState().withProperty(AGE, age);
	}

	public boolean isMaxAge(IBlockState state) {
		return state.getValue(AGE) >= getMaxAge();
	}

}
