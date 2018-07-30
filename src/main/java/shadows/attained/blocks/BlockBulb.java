package shadows.attained.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
import shadows.attained.init.Config;
import shadows.placebo.client.IHasModel;
import shadows.placebo.itemblock.ItemBlockBase;

public class BlockBulb extends BlockBush implements IHasModel, IShearable {

	public static final PropertyEnum<BulbType> BULB = PropertyEnum.create("type", BulbType.class);
	public static final AxisAlignedBB BULB_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.5D, 0.6875D);

	public BlockBulb(String name) {
		setRegistryName(name);
		setTranslationKey(AttainedDrops2.MODID + "." + name);
		setCreativeTab(AttainedDrops2.TAB);
		setSoundType(SoundType.PLANT);
		setHardness(0.4F);
		setTickRandomly(false);
		setDefaultState(this.blockState.getBaseState().withProperty(BULB, BulbType.BLAZE));
		AttainedDrops2.INFO.getBlockList().add(this);
		AttainedDrops2.INFO.getItemList().add(new ItemBlockBase(this) {
			@Override
			public int getMetadata(int damage) {
				return damage;
			}

			@Override
			public String getTranslationKey(ItemStack stack) {
				return block.getTranslationKey() + "." + BulbType.values()[MathHelper.clamp(stack.getMetadata(), 0, 14)].getName();
			}
		}.setHasSubtypes(true));
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (BulbType b : BulbType.values()) {
			list.add(b.get());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (rand.nextFloat() >= 0.6937F) {
			EnumDyeColor color = state.getValue(BULB).getColor();
			ParticleSimpleAnimated p = new ParticleEndRod(Minecraft.getMinecraft().world, pos.getX() + 1.0D - rand.nextDouble(), pos.getY() + 0.4D, pos.getZ() + 1.0D - rand.nextDouble(), 0, 0.03D, 0);
			p.setColor(color.getColorValue());
			p.setColorFade(color.getColorValue());
			Minecraft.getMinecraft().effectRenderer.addEffect(p);
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return state.getValue(BULB).get();
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ItemStack drop = state.getValue(BULB).getDrop();
		drops.add(drop);
		if (fortune > 0 && RANDOM.nextInt(MathHelper.clamp(4 - fortune, 1, 4)) == 0) drops.add(drop.copy());
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!Config.rightClickFarm) return false;
		if (!world.isRemote) {
			ItemStack drop = state.getValue(BULB).getDrop();
			if (!player.addItemStackToInventory(drop)) spawnAsEntity(world, pos, drop);
			world.setBlockToAir(pos);
		}
		return true;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BULB, BulbType.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BULB).ordinal();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BULB);
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
		for (BulbType b : BulbType.values()) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), b.ordinal(), new ModelResourceLocation(getRegistryName(), "type=" + b.getName()));
		}
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		return Arrays.asList(world.getBlockState(pos).getValue(BULB).get());
	}
}
