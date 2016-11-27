package shadows.attained.common;

import shadows.attained.util.BulbHelper;

import java.util.Random;


import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
	
	
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
    private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

	public BlockPlant(){
		setRegistryName("plant");
		setUnlocalizedName(AttainedDrops.MODID + ".blockplant");
		setHardness(0.2F);
	//	setCreativeTab(ModRegistry.Attained);
		setTickRandomly(true);
		setSoundType(SoundType.PLANT);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
		setDefaultState(this.blockState.getBaseState().withProperty(this.getAgeProperty(), Integer.valueOf(0)));
		
	}
	
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return CROPS_AABB[((Integer)state.getValue(this.getAgeProperty())).intValue()];
    }
	
	public Item getSeed(){
		return ModRegistry.itemseed;
	}
	

	public Item getCrop(){
		return null;
	}
	@Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }
	
    @Override
    public java.util.List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        java.util.List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
        ret.clear();
        if (Math.random() >= 0.3){
        ret.add(new ItemStack(getSeed()));
        }
        return ret;
    }
	
	@Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }	

	@Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
        return world.getBlockState(pos.down()).getBlock() == ModRegistry.vitalized;
    }
	
	
	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean bool)
	{
		if (world.getBlockState(pos.down()).getBlock() == ModRegistry.vitalized){
		int i = getAge(world.getBlockState(pos));
		int dropNumber = (BlockVitalized.getSoilMeta(world.getBlockState(pos.down())) - 1);
		if (i < 7)
		{
			return true;
		}
		else if (i == 7 && BulbHelper.canBonemealBulb(dropNumber) && world.isAirBlock(pos.up()))
		{
			return true;
		}
		}
		return false;
	}
 

	public void grow(World world, BlockPos pos, IBlockState state)
	{
		if (world.getBlockState(pos.down()).getBlock() == ModRegistry.vitalized){
		growBlock(world, pos);
	}
		
	}
	
	@Override
    public void grow(World world, Random rand, BlockPos pos, IBlockState state)
    {
		if (world.getBlockState(pos.down()).getBlock() == ModRegistry.vitalized){
		growBlock(world, pos);
    }
		
	}
	
	public void growBlock(World world, BlockPos pos)
	{
		int l = getAge(world.getBlockState(pos));
		//int i = l + MathHelper.getRandomIntegerInRange(world.rand, 2, 5);
		int bulbID = BlockVitalized.getSoilMeta(world.getBlockState(pos.down()));

		if (l == 7)
		{
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
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(world, pos, state, rand);
		int meta = getAge(world.getBlockState(pos));
		if (world.getBlockState(pos.down()).getBlock() == ModRegistry.vitalized)
		{
		int soilID = BlockVitalized.getSoilMeta(world.getBlockState(pos.down()));
		if ((world.getBlockState(pos.down()).getBlock() == ModRegistry.vitalized)){

		if (meta < 7)
		{
			if ((rand.nextInt(5) == 0) && (world.getBlockState(pos.down()).getBlock() == ModRegistry.vitalized))
			{
				++meta;
				world.setBlockState(pos, this.withAge(meta), 2);

			
		}
		}
		if ((meta == 7) && (soilID != 0))
		{
			if ((rand.nextInt(/*BulbHelper.getBulbRate(soilID)*/5) == 0) && world.isAirBlock(pos.up()))
			{
				world.setBlockState(pos.up(), ModRegistry.blockbulb.getStateFromMeta(soilID - 1), 2);
				if (rand.nextInt(/*BulbHelper.getSoilResetChance(soilID - 1)*/15) == 0/* && BulbHelper.getCanSoilReset(soilID - 1) == true*/)
				{
					world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState(), 2);
				}
			}
		}
		}
		}
		
		if (world.getBlockState(pos.down()) == Blocks.DIRT.getDefaultState()){
		/*	world.setBlockToAir(pos);
			spawnAsEntity(world, pos, new ItemStack(ModRegistry.itemseed));	Deprecated Methods that originally would kill plant if it was on dirt*/
			if (meta > 0)
			{
				if (rand.nextInt(5) == 0)
				{
					--meta;
					world.setBlockState(pos, this.withAge(meta), 2);
				}
		
		}
		}
		
	}
	
    protected int getBonemealAgeIncrease(World worldIn)
    {
        return 1;
    }
	
	@Override
    protected boolean canSustainBush(IBlockState state)
    {
        return state.getBlock() == ModRegistry.vitalized;
    }
	
    public IBlockState getStateFromMeta(int meta)
    {
        return this.withAge(meta);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return this.getAge(state);
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {AGE});
    }
    
    protected PropertyInteger getAgeProperty()
    {
        return AGE;
    }

    public int getMaxAge()
    {
        return 7;
    }

    protected int getAge(IBlockState state)
    {
        return ((Integer)state.getValue(this.getAgeProperty())).intValue();
    }

    public IBlockState withAge(int age)
    {
        return this.getDefaultState().withProperty(this.getAgeProperty(), Integer.valueOf(age));
    }

    public boolean isMaxAge(IBlockState state)
    {
        return ((Integer)state.getValue(this.getAgeProperty())).intValue() >= this.getMaxAge();
    }
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("plant", "inventory"));
	}

}
