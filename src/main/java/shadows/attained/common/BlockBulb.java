package shadows.attained.common;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.*;
import shadows.attained.*;

import javax.annotation.Nonnull;

public class BlockBulb extends Block {

	private static final AxisAlignedBB BulbBox = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.5D, 0.6875D);
	public static Item[] MobDrops = {
			Items.BLAZE_ROD, Items.ENDER_PEARL, Items.GUNPOWDER, Items.BONE, Items.SPIDER_EYE, Items.STRING, Items.GHAST_TEAR, Items.ROTTEN_FLESH, Items.SLIME_BALL, Items.PRISMARINE_SHARD
	};

	public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, (MobDrops.length - 1));

	public BlockBulb() {
		super(Material.PLANTS);
		setTickRandomly(true);
		setRegistryName("bulb");
		setHardness(0.3F);
		setSoundType(SoundType.PLANT);
		setCreativeTab(ModRegistry.Attained);
		setUnlocalizedName(AttainedDrops.MODID + ".bulb");
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
		setDefaultState(blockState.getBaseState().withProperty(getTypeProperty(), 0));
	}

	public int getDamageValue(World world, int x, int y, int z) {
		return getMetaFromState(world.getBlockState(new BlockPos(x, y, z)));
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BulbBox;
	}

	@Override
	public void getSubBlocks(@Nonnull Item block, CreativeTabs creativeTabs, List<ItemStack> list) {
		for (int i = 0; i < MobDrops.length; ++i) {
			list.add(new ItemStack(ModRegistry.itembulb, 1, i));
			list.remove(new ItemStack(block, 1, 0));
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull World worldIn, @Nonnull BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		int meta = getMetaFromState(state);
		return MobDrops[meta];
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public int quantityDropped(Random rand) {
		return 1;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, @Nonnull Random random) {
		int DropNumber;

		DropNumber = random.nextInt(2);

			return (quantityDroppedWithBonus(fortune, random) + DropNumber);
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, @Nonnull Random rand) {
		if (fortune > 0 && Item.getItemFromBlock(this) != getItemDropped(getDefaultState(), rand, fortune)) {
			int j = rand.nextInt(fortune + 2) - 1;

			if (j < 0) {
				j = 0;
			}

			return this.quantityDropped(rand) * (j + 1);
		}
		else {
			return this.quantityDropped(rand);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {
		return world.getBlockState(pos).getBlock().isReplaceable(world, pos) && !world.isAirBlock(pos.down(1));
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
		if (world.getBlockState(pos.down()) != null && world.getBlockState(pos.down()) == ModRegistry.blockplant) {
			world.setBlockState(pos.down(), getStateFromMeta(7), 2);

		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		super.neighborChanged(state, worldIn, pos, blockIn);
		if (worldIn.getBlockState(pos.down()).getBlock() != ModRegistry.blockplant) {
			dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	@Override
	public int tickRate(World world) {
		return 90;
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, Random rand) {
		int dropNumber = getMetaFromState(world.getBlockState(pos));

			particles(world, pos, dropNumber);
		
	}

	private void particles(World world, BlockPos pos, int dropNumber) {
		if (world.rand.nextInt(8) == 0) {
			double d0 = pos.getX() + world.rand.nextFloat();
			double d1 = pos.getY() + world.rand.nextFloat();
			double d2 = pos.getZ() + world.rand.nextFloat();
			double d3 = 0.0D;
			double d4 = 0.0D;
			double d5 = 0.0D;

			int i1 = world.rand.nextInt(2) * 2 - 1;
			d3 = (world.rand.nextFloat() - 0.5D) * 0.5D;
			d5 = (world.rand.nextFloat() - 0.5D) * 0.5D;

			d3 = world.rand.nextFloat() * 30.0F * i1;
			d4 = world.rand.nextFloat() * 30.0F * i1;
			d5 = world.rand.nextFloat() * 30.0F * i1;

			world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, d1, d2, d0, d4, d5, d3);
			world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, d0, d1, d2, d3, d4, d5);
			world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, d2, d3, d1, d5, d3, d4);
		}
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return withType(meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return getType(state);
	}

	protected int getType(IBlockState state) {
		return state.getValue(getTypeProperty());
	}

	public IBlockState withType(int type) {
		return getDefaultState().withProperty(getTypeProperty(), type);
	}

	protected PropertyInteger getTypeProperty() {
		return TYPE;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
