package shadows.attained.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.api.IBulb;
import shadows.attained.init.ModGlobals;
import shadows.attained.init.ModRegistry;
import shadows.attained.util.AD2Util;

public class BlockBulbBase extends Block implements IBulb {

	private TextFormatting textColor;

	public BlockBulbBase(String name) {
		this("", name);
	}

	public BlockBulbBase(String modid, String name) {
		super(Material.PLANTS);
		setTickRandomly(true);
		setRegistryName(name + "_bulb");
		setHardness(0.3F);
		setSoundType(SoundType.PLANT);
		setUnlocalizedName((modid.isEmpty() ? ModGlobals.MODID : modid) + ".bulb." + name);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
	}

	public int getDamageValue(World world, int x, int y, int z) {
		return getMetaFromState(world.getBlockState(new BlockPos(x, y, z)));
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.5D, 0.6875D);
	}

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
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
	public int quantityDropped(IBlockState state, int fortune, @Nonnull Random random) {
		return quantityDroppedWithBonus(fortune, random);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, @Nonnull Random rand) {
		int j = rand.nextInt(fortune + 1);
		return j + 1;
	}

	@Override
	public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {
		return world.getBlockState(pos).getBlock().isReplaceable(world, pos) && !world.isAirBlock(pos.down(1));
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack) {
		double d0 = world.rand.nextFloat() * 0.5F + 0.25D;
		double d1 = world.rand.nextFloat() * 0.5F + 0.25D;
		double d2 = world.rand.nextFloat() * 0.5F + 0.25D;
		Item drop = AD2Util.getBulbDrop(this).getItem();
		int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
		ItemStack drops = new ItemStack(drop, quantityDropped(state, fortuneLevel, world.rand));
		EntityItem entityitem = new EntityItem(world, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, drops);
		entityitem.setDefaultPickupDelay();
		world.spawnEntity(entityitem);
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
		if (world.getBlockState(pos.down()) != null && world.getBlockState(pos.down()).getBlock() == ModRegistry.BLOCK_PLANT) {
			Block block2Below = world.getBlockState(pos.offset(EnumFacing.DOWN, 2)).getBlock();
			if (AD2Util.isSoil(block2Below) && AD2Util.isSoilEnriched(AD2Util.getSoilFromBlock(block2Below))) {
				world.setBlockState(pos.down(), ((BlockPlant) world.getBlockState(pos.down()).getBlock()).getStateFromMeta(7), 2);
				return;
			}
		}
		world.setBlockState(pos.down(), Blocks.AIR.getDefaultState(), 3);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		if (worldIn.getBlockState(pos.down()).getBlock() != ModRegistry.BLOCK_PLANT) {
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	@Override
	public int tickRate(World world) {
		return 90;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	public void setTextColor(TextFormatting color) {
		if (color.isColor()) {
			textColor = color;
			return;
		}
		textColor = TextFormatting.GRAY;
	}

	@Override
	public TextFormatting getTextColor() {
		return textColor;
	}

}
