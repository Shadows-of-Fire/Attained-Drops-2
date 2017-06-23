package shadows.attained.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops2;
import shadows.attained.init.DataLists;
import shadows.attained.init.ModRegistry;
import shadows.attained.util.IHasModel;

public class BlockBulb extends BlockBush implements IHasModel {

	public static final PropertyInteger META = PropertyInteger.create("meta", 0, BulbTypes.values().length - 1);
	public static final AxisAlignedBB BULB_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.5D, 0.6875D);
	public static Map<Integer, ItemStack> lookup = new HashMap<Integer, ItemStack>();

	public BlockBulb(String name) {
		setRegistryName(name);
		setUnlocalizedName(AttainedDrops2.MODID + "." + name);
		setCreativeTab(ModRegistry.AD2_TAB);
		setSoundType(SoundType.PLANT);
		setHardness(0.4F);
		setTickRandomly(false);
		setDefaultState(this.blockState.getBaseState().withProperty(META, 0));
		DataLists.BLOCKS.add(this);
		DataLists.ITEMS.add(new ItemBlock(this) {
			@Override
			public int getMetadata(int damage) {
				return damage;
			}

			@Override
			public String getUnlocalizedName(ItemStack stack) {
				return block.getUnlocalizedName() + "." + stack.getMetadata();
			}
		}.setHasSubtypes(true).setRegistryName(getRegistryName()));
		for (int i = 0; i < BulbTypes.values().length; i++) {
			lookup.put(i, BulbTypes.values()[i].getDrop());
		}

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int i = 0; i < BulbTypes.values().length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (rand.nextFloat() > 0.6937F)
			world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, pos.getX() + 1.0D - rand.nextDouble(),
					pos.getY() + 0.4D, pos.getZ() + 1.0D - rand.nextDouble(), 0, 0.4D, 0);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> ret = new ArrayList<ItemStack>();
		ItemStack drops = lookup.get(state.getValue(META));
		ItemStack newDropStack = new ItemStack(drops.getItem(), 1, drops.getMetadata());
		ret.add(newDropStack);
		if (fortune > 0 && ThreadLocalRandom.current().nextInt(MathHelper.clamp(4 - fortune, 1, 4)) == 0) {
			ret.add(newDropStack);
		}
		return ret;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(META, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(META);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, META);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BULB_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
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
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		return !world.isAirBlock(pos.down());
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		for (int i = 0; i < BulbTypes.values().length; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i,
					new ModelResourceLocation(getRegistryName(), "meta=" + i));
		}
	}
}
