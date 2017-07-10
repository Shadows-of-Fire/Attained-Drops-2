package shadows.attained.blocks;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops2;
import shadows.attained.init.Config;
import shadows.attained.init.DataLists;
import shadows.attained.init.ModRegistry;
import shadows.attained.util.IHasModel;

public class BlockPlant extends BlockBush implements IGrowable, IHasModel {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
	private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.125D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.25D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.375D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.5D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.625D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.75D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.875, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 1.0D, 0.84375D) };
	public static final String regname = "plant";

	public BlockPlant() {
		setRegistryName(regname);
		setUnlocalizedName(AttainedDrops2.MODID + "." + regname);
		setHardness(0.2F);
		setSoundType(SoundType.PLANT);
		DataLists.BLOCKS.add(this);
		DataLists.ITEMS.add(new ItemBlock(this).setRegistryName(getRegistryName()));
		setDefaultState(blockState.getBaseState().withProperty(AGE, 0));

	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		if (state.getBlock() == this) {
			return canPlaceBlockAt(world, pos);
		}
		return canSustainBush(world.getBlockState(pos.down()));
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return CROPS_AABB[state.getValue(AGE)];
	}

	@Override
	public boolean canUseBonemeal(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		return Config.allowBonemeal;
	}

	@Override
	public void getDrops(NonNullList<ItemStack> ret, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
		ret.clear();
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
		Block block = world.getBlockState(pos.down()).getBlock();
		return block instanceof BlockVitalized || block instanceof BlockDirt;
	}

	@Override
	public boolean canGrow(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean bool) {
		if (world.getBlockState(pos.down()).getBlock() instanceof BlockVitalized) {
			int age = getAge(world.getBlockState(pos));
			return age < this.getMaxAge() || (isMaxAge(state) && world.isAirBlock(pos.up()));
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
				world.setBlockState(pos.up(), place);
				if (Config.revertChance > 0 && rand.nextInt(Config.revertChance) == 0) {
					world.setBlockState(pos.down(), world.getBlockState(pos.down()).getBlock().getDefaultState());
					if (Config.revertToDirt && rand.nextInt(Config.revertChance) == 0)
						world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState());
				}
			}
		}
	}

	@Override
	public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, Random rand) {
		if (canGrow(world, pos, state, true))
			if(rand.nextInt(6) == 0) grow(world, world.rand, pos, state);
		else if (!canGrow(world, pos, state, true) && world.getBlockState(pos.down()).getBlock() instanceof BlockDirt && state.getValue(AGE) > 0) {
			world.setBlockState(pos, state.withProperty(AGE, state.getValue(AGE) - 1));
		}
	}

	@Override
	protected boolean canSustainBush(IBlockState state) {
		return state.getBlock() instanceof BlockVitalized || state.getBlock() instanceof BlockDirt;
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return withAge(meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return getAge(state);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, AGE);
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

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
