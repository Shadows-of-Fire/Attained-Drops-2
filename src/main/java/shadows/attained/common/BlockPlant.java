package shadows.attained.common;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.*;
import shadows.attained.*;

import javax.annotation.Nonnull;

public class BlockPlant extends BlockBush implements IGrowable {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
	private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {
			new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.125D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.25D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.375D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.5D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.625D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.75D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.875, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 1.0D, 0.84375D)
	};

	public BlockPlant() {
		setRegistryName("plant");
		setUnlocalizedName(AttainedDrops.MODID + ".blockplant");
		setHardness(0.2F);
		setCreativeTab(ModRegistry.Attained);
		setTickRandomly(true);
		setSoundType(SoundType.PLANT);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
		setDefaultState(blockState.getBaseState().withProperty(getAgeProperty(), 0));

	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return CROPS_AABB[state.getValue(getAgeProperty())];
	}

	public Item getSeed() {
		return ModRegistry.itemseed;
	}

	public Item getCrop() {
		return null;
	}

	@Override
	public boolean canUseBonemeal(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public java.util.List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
		java.util.List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
		ret.clear();
		if (Math.random() >= 0.3) {
			ret.add(new ItemStack(getSeed()));
		}
		return ret;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
    public void randomTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random random)
    {
        updateTick(worldIn, pos, state, random);
        updateTick(worldIn, pos, state, random);
        updateTick(worldIn, pos, state, random);
    }
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return world.getBlockState(pos.down()).getBlock() == ModRegistry.vitalized;
	}

	@Override
	public boolean canGrow(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean bool) {
		if (world.getBlockState(pos.down()).getBlock() == ModRegistry.vitalized) {
			int i = getAge(world.getBlockState(pos));
			int dropNumber = (BlockVitalized.getSoilMeta(world.getBlockState(pos.down())) - 1);
			if (i < 7) {
				return true;
			}
			else if (i == 7 && world.isAirBlock(pos.up())) {
				return true;
			}
		}
		return false;
	}

	public void grow(World world, BlockPos pos, IBlockState state) {
		if (world.getBlockState(pos.down()).getBlock() == ModRegistry.vitalized) {
			growBlock(world, pos);
		}

	}

	@Override
	public void grow(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		if (world.getBlockState(pos.down()).getBlock() == ModRegistry.vitalized) {
			growBlock(world, pos);
		}

	}

	public void growBlock(World world, BlockPos pos) {
		int l = getAge(world.getBlockState(pos));
		//int i = l + MathHelper.getRandomIntegerInRange(world.rand, 2, 5);
		int bulbID = BlockVitalized.getSoilMeta(world.getBlockState(pos.down()));

		if (l == 7) {
			if (world.isAirBlock(pos.up()) && bulbID != 0 && world.rand.nextInt(8) == 5)
			/*&& world.rand.nextInt(BulbHelper.chanceForBoneMeal(dropNumber - 1)) == 0*/
			{
				world.setBlockState(pos.up(), ModRegistry.blockbulb.getStateFromMeta(bulbID - 1), 2);
				//if (world.rand.nextInt(BulbHelper.getSoilResetChance(bulbID - 1)) == 0
				//		&& BulbHelper.getCanSoilReset(bulbID - 1) == true)
				//{
				//	world.setBlockState(pos.down(), getDefaultState(), 2);
				//}
			}
		}

		//if (i > 8)
		//{
		//	i = 8;
		//}

		//world.setBlockState(pos, getDefaultState(), 2);
	}

	@Override
	public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, Random rand) {
		super.updateTick(world, pos, state, rand);
		int meta = getAge(world.getBlockState(pos));
		if (world.getBlockState(pos.down()).getBlock() == ModRegistry.vitalized) {
			int soilID = BlockVitalized.getSoilMeta(world.getBlockState(pos.down()));
			if ((world.getBlockState(pos.down()).getBlock() == ModRegistry.vitalized)) {

				if (meta < 7) {
					if ((rand.nextInt(5) == 0) && (world.getBlockState(pos.down()).getBlock() == ModRegistry.vitalized)) {
						++meta;
						world.setBlockState(pos, withAge(meta), 2);

					}
				}
				if ((meta == 7) && (soilID != 0)) {
					if ((rand.nextInt(/*BulbHelper.getBulbRate(soilID)*/5) == 0) && world.isAirBlock(pos.up())) {
						world.setBlockState(pos.up(), ModRegistry.blockbulb.getStateFromMeta(soilID - 1), 2);
						if (rand.nextInt(/*BulbHelper.getSoilResetChance(soilID - 1)*/15) == 0/* && BulbHelper.getCanSoilReset(soilID - 1) == true*/) {
							world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState(), 2);
						}
					}
				}
			}
		}

		if (world.getBlockState(pos.down()) == Blocks.DIRT.getDefaultState()) {
			/*	world.setBlockToAir(pos);
				spawnAsEntity(world, pos, new ItemStack(ModRegistry.itemseed));	Deprecated Methods that originally would kill plant if it was on dirt*/
			if (meta > 0) {
				if (rand.nextInt(5) == 0) {
					--meta;
					world.setBlockState(pos, withAge(meta), 2);
				}

			}
		}

	}

	protected int getBonemealAgeIncrease(World worldIn) {
		return 1;
	}

	@Override
	protected boolean canSustainBush(IBlockState state) {
		return state.getBlock() == ModRegistry.vitalized;
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return withAge(meta);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		return getAge(state);
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, AGE);
	}

	protected PropertyInteger getAgeProperty() {
		return AGE;
	}

	public int getMaxAge() {
		return 7;
	}

	protected int getAge(IBlockState state) {
		return state.getValue(getAgeProperty());
	}

	public IBlockState withAge(int age) {
		return getDefaultState().withProperty(getAgeProperty(), age);
	}

	public boolean isMaxAge(IBlockState state) {
		return state.getValue(getAgeProperty()) >= getMaxAge();
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
