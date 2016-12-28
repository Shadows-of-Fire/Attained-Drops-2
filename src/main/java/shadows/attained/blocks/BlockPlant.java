package shadows.attained.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.api.IBulb;
import shadows.attained.api.ITOPInfoProvider;
import shadows.attained.api.IVitalizedSoil;
import shadows.attained.init.ModConfig.ConfigOptions;
import shadows.attained.init.ModGlobals;
import shadows.attained.init.ModNetworkHandler;
import shadows.attained.init.ModRegistry;
import shadows.attained.network.PacketSpawnParticle;
import shadows.attained.util.AD2Util;

public class BlockPlant extends BlockBush implements IGrowable, ITOPInfoProvider {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
	private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {
			new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.125D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.25D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.375D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.5D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.625D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.75D, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.875, 0.84375D), new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 1.0D, 0.84375D)
	};

	public BlockPlant() {
		setRegistryName("plant");
		setUnlocalizedName(ModGlobals.MODID + ".blockplant");
		setHardness(0.2F);
		setCreativeTab(ModRegistry.AD2_TAB);
		setSoundType(SoundType.PLANT);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
		setDefaultState(blockState.getBaseState().withProperty(AGE, 0));

	}

	@Override
	public int tickRate(World world) {
		return 1;
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return CROPS_AABB[state.getValue(AGE)];
	}

	@Override
	public boolean canUseBonemeal(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		return ConfigOptions.CAN_BONEMEAL;
	}

	@Nonnull
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
		List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
		ret.clear();
		if (Math.random() >= 0.3) {
			ret.add(new ItemStack(ModRegistry.ITEM_SEED));
		}
		return ret;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public void randomTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random random) {
		updateTick(worldIn, pos, state, random);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return AD2Util.isSoil(world.getBlockState(pos.down()).getBlock());
	}

	@Override
	public boolean canGrow(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean bool) {
		if (AD2Util.isSoil(world.getBlockState(pos.down()).getBlock())) {
			int age = getAge(world.getBlockState(pos));
			if (age < getMaxAge()) {
				return true;
			}
			else if (age == getMaxAge() && world.isAirBlock(pos.up())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void grow(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		if (AD2Util.isSoil(world.getBlockState(pos.down()).getBlock())) {
			if (getAge(state) < getMaxAge()) {
				if (rand.nextInt(3) == 0) {
					setAge(world, pos, getAge(state) + 1);
				}
			}
			if (getAge(state) == getMaxAge()) {
				if (world.getBlockState(pos.down()).getBlock() instanceof IVitalizedSoil) {
					IVitalizedSoil soil = (IVitalizedSoil) world.getBlockState(pos.down()).getBlock();
					IBulb bulb = AD2Util.getBulbFromSoil(soil);
					if (world.isAirBlock(pos.up()) && AD2Util.isSoilEnriched(soil) && world.rand.nextInt(8) == 5) {
						world.setBlockState(pos.up(), AD2Util.getBulbState(bulb), 2);
					}
				}
			}
		}
	}

	@Override
	public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, Random rand) {
		super.updateTick(world, pos, state, rand);
		int meta = getAge(world.getBlockState(pos));
		Block downBlock = world.getBlockState(pos.down()).getBlock();
		if (AD2Util.isSoil(downBlock) && AD2Util.isSoilEnriched(AD2Util.getSoilFromBlock(downBlock))) {
			if (meta < 7) {
				if (rand.nextInt(5) == 0) {
					++meta;
					setAge(world, pos, meta);
					ModNetworkHandler.getInstance().sendToAllAround(new PacketSpawnParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), pos.getX(), pos.getY(), pos.getZ()), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 30));
				}
			}
			if (meta == 7) {
				if ((rand.nextInt(5) == 0) && world.isAirBlock(pos.up())) {
					ModNetworkHandler.getInstance().sendToAllAround(new PacketSpawnParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), pos.getX(), pos.getY(), pos.getZ()), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 30));
					world.setBlockState(pos.up(), AD2Util.getBulbState(AD2Util.getBulbFromSoil(AD2Util.getSoilFromBlock(downBlock))), 2);
					if (rand.nextInt(15) == 0) {
						world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState(), 2);
					}
				}
			}
			return;
		}
	}

	@Override
	protected boolean canSustainBush(IBlockState state) {
		return AD2Util.isSoil(state.getBlock());
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

	@Nonnull
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

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		IBulb bulb = AD2Util.getBulbFromPlant(world, data.getPos());
		probeInfo.horizontal().text("     Type: " + bulb.getTextColor() + "" + AD2Util.getBulbDrop(bulb).getDisplayName());
		probeInfo.horizontal().text("     Growth: " + AD2Util.getPlantGrowthPercent(blockState) + "%");
	}

}
