package shadows.attained.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.base.Preconditions;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEndRod;
import net.minecraft.client.particle.ParticleSimpleAnimated;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops2;
import shadows.attained.init.ModRegistry;
import shadows.placebo.client.IHasModel;

public class BlockBulb extends BlockBush implements IHasModel, IShearable {

	public static final PropertyInteger META = PropertyInteger.create("meta", 0, BulbTypes.values().length - 1);
	public static final AxisAlignedBB BULB_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.5D, 0.6875D);
	public static Map<Integer, ItemStack> lookup = new HashMap<Integer, ItemStack>();

	public BlockBulb(String name) {
		setRegistryName(name);
		setUnlocalizedName(AttainedDrops2.MODID + "." + name);
		setCreativeTab(AttainedDrops2.TAB);
		setSoundType(SoundType.PLANT);
		setHardness(0.4F);
		setTickRandomly(false);
		setDefaultState(this.blockState.getBaseState().withProperty(META, 0));
		AttainedDrops2.INFO.getBlockList().add(this);
		AttainedDrops2.INFO.getItemList().add(new ItemBlock(this) {
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

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int i = 0; i < BulbTypes.values().length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (rand.nextFloat() > 0.6937F) {
			EnumDyeColor color = getColorFromState(state);
			ParticleSimpleAnimated p = new ParticleEndRod(Minecraft.getMinecraft().world, pos.getX() + 1.0D - rand.nextDouble(), pos.getY() + 0.4D, pos.getZ() + 1.0D - rand.nextDouble(), 0, 0.03D, 0);
			p.setColor(color.getColorValue());
			p.setColorFade(color.getColorValue());
			Minecraft.getMinecraft().effectRenderer.addEffect(p);
		}
	}

	public static EnumDyeColor getColorFromState(IBlockState state) {
		Preconditions.checkArgument(state.getBlock() == ModRegistry.BULB);
		return BulbTypes.values()[state.getValue(META)].getColor();
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, state.getValue(META));
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

	@Override
	public void initModels(ModelRegistryEvent e) {
		for (int i = 0; i < BulbTypes.values().length; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation(getRegistryName(), "meta=" + i));
		}
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		return Arrays.asList(new ItemStack(this, 1, world.getBlockState(pos).getValue(META)));
	}
}
