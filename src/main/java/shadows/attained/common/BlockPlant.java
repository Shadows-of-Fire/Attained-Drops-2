package shadows.attained.common;

import shadows.attained.util.BulbHelper;

import java.util.Random;


import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops;
import shadows.attained.ModRegistry;
import shadows.attained.config.ADConfig;

public class BlockPlant extends BlockBush implements IGrowable{

	public BlockPlant(){
		super(Material.PLANTS);
		setRegistryName("plant");
		setUnlocalizedName(AttainedDrops.MODID + "blockplant");
		setHardness(0.2F);
		setCreativeTab(ModRegistry.Attained);
		setTickRandomly(true);
		setSoundType(SoundType.PLANT);
		setDefaultState(getStateFromMeta(0));
		GameRegistry.register(this);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return NULL_AABB;
	}	

	@Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock().isReplaceable(world, pos) && world.getBlockState(pos.down()).getBlock() == ModRegistry.dummysoil;
    }
	
	
	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean bool)
	{
		int i = getMetaFromState(world.getBlockState(pos));
		int dropNumber = (getMetaFromState(world.getBlockState(pos.down())) - 1);
		Block upperBlock = world.getBlockState(pos.up()).getBlock();
		if (i < 8)
		{
			return true;
		}
		else if (i == 8 && BulbHelper.canBonemealBulb(dropNumber) == true && upperBlock.isAir(state, world, pos))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}
 
	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state)
	{
		growBlock(world, pos);
	}
	
	@SuppressWarnings("deprecation")
	public void growBlock(World world, BlockPos pos)
	{
		int l = getMetaFromState(world.getBlockState(pos));
		int i = l + MathHelper.getRandomIntegerInRange(world.rand, 2, 5);
		int dropNumber = getMetaFromState(world.getBlockState(pos.down()));

		if (l == 8 && BulbHelper.canBonemealBulb(dropNumber - 1) == true)
		{
			if (world.isAirBlock(pos.up()) && dropNumber != 0
					&& world.rand.nextInt(BulbHelper.chanceForBoneMeal(dropNumber - 1)) == 0)
			{
				world.setBlockState(pos.up(), ModRegistry.blockbulb.getStateFromMeta(dropNumber - 1), 2);
				if (world.rand.nextInt(BulbHelper.getSoilResetChance(dropNumber - 1)) == 0
						&& BulbHelper.getCanSoilReset(dropNumber - 1) == true)
				{
					world.setBlockState(pos.down(), getStateFromMeta(0), 2);
				}
			}
		}

		if (i > 8)
		{
			i = 8;
		}

		world.setBlockState(pos, getStateFromMeta(i), 2);
	}	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(world, pos, state, rand);

		int meta = getMetaFromState(world.getBlockState(pos));
		int dropNumber = getMetaFromState(world.getBlockState(pos.down()));

		if (meta < 8)
		{
			if (rand.nextInt(ADConfig.BlockMobPlantUpdate) == 0)
			{
				++meta;
				world.setBlockState(pos, getStateFromMeta(meta), 2);
			}
		}
		if (meta == 8 && world.getBlockState(pos.down()).getBlock() == ModRegistry.dummysoil && dropNumber != 0)
		{
			if (rand.nextInt(BulbHelper.getBulbRate(dropNumber)) == 0 && world.isAirBlock(pos.up()))
			{
				world.setBlockState(pos.up(), ModRegistry.blockbulb.getStateFromMeta(dropNumber - 1), 2);
				if (rand.nextInt(BulbHelper.getSoilResetChance(dropNumber - 1)) == 0 && BulbHelper.getCanSoilReset(dropNumber - 1) == true)
				{
					world.setBlockState(pos.down(), getDefaultState(), 2);
				}
			}
		}
	}	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("plant", "inventory"));
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
