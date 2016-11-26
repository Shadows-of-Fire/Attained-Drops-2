package shadows.attained.common;

import java.util.List;
import java.util.Random;


import shadows.attained.util.BulbHelper;


import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
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

public class BlockBulb extends Block{
	
	public static Item[] MobDrops =
	{ Items.BLAZE_ROD, Items.ENDER_PEARL, Items.GUNPOWDER, Items.BONE, Items.SPIDER_EYE, Items.STRING, Items.GHAST_TEAR,
			Items.ROTTEN_FLESH, Items.SLIME_BALL };
	
	
	public BlockBulb()
	{
		super(Material.PLANTS);
		setTickRandomly(true);
		setRegistryName("bulb");
		setHardness(0.3F);
		setSoundType(SoundType.PLANT);
		setCreativeTab(ModRegistry.Attained);
		setUnlocalizedName(AttainedDrops.MODID + "bulb");
		setDefaultState(getStateFromMeta(0));
		GameRegistry.register(this);
	}
	
	public int getDamageValue(World world, int x, int y, int z)
	{
		return getMetaFromState(world.getBlockState(new BlockPos(x, y, z)));
	}
	
	@Override
	public void getSubBlocks(Item block, CreativeTabs creativeTabs, List<ItemStack> list)
	{
		for (int i = 0; i < MobDrops.length; ++i)
		{
			list.add(new ItemStack(block, 1, i));
		}
	}
	
	public void onNeighborBlockChange(World world, BlockPos pos, Block block, int fortune)
	{
		if (!this.canBlockStay(world, pos))
		{
			this.dropBlockAsItem(world, pos, world.getBlockState(pos), fortune);
			world.setBlockToAir(pos);
		}
	}
	
	public boolean canBlockStay(World world, BlockPos pos)
	{
		if (world.isAirBlock(pos.down()))
		{
			return false;
		}
		return true;
	}
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return NULL_AABB;
	}	

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		int meta = getMetaFromState(state);
		return MobDrops[meta];
	}

	@Override
	public int quantityDropped(Random rand)
	{
		return 1;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		int DropNumber;
		
		DropNumber = BulbHelper.staticDropNumber(getMetaFromState(state)) + random.nextInt(BulbHelper.dynamicDropNumber(getMetaFromState(state)));

		if (BulbHelper.canFortuneBulb(getMetaFromState(state)) == true)
		{
			return (quantityDroppedWithBonus(fortune, random) + DropNumber);
		}
		else
			return DropNumber;
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random rand)
	{
		if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(getDefaultState(), rand, fortune))
		{
			int j = rand.nextInt(fortune + 2) - 1;

			if (j < 0)
			{
				j = 0;
			}

			return this.quantityDropped(rand) * (j + 1);
		}
		else
		{
			return this.quantityDropped(rand);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlock().isReplaceable(world, pos) && !world.isAirBlock(pos.down(1));
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state)
	{
		if (world.getBlockState(pos.down()) != null && world.getBlockState(pos.down()) == ModRegistry.blockplant)
		{
			world.setBlockState(pos.down(), this.getStateFromMeta(7), 2);
			
		}
	}

	@Override
	public int tickRate(World world)
	{
		return 90;
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, Random rand)
	{
		int dropNumber = getMetaFromState(world.getBlockState(pos));
		if (BulbHelper.canSpawnParticles(dropNumber) == true)
		{
			particles(world, pos, dropNumber);
		}
	}

	private void particles(World world, BlockPos pos, int dropNumber)
	{
		if (world.rand.nextInt(BulbHelper.particleSpawnRate(dropNumber)) == 0)
		{
			double d0 = (double) ((float) pos.getX() + world.rand.nextFloat());
			double d1 = (double) ((float) pos.getY() + world.rand.nextFloat());
			double d2 = (double) ((float) pos.getZ() + world.rand.nextFloat());
			double d3 = 0.0D;
			double d4 = 0.0D;
			double d5 = 0.0D;

			int i1 = world.rand.nextInt(2) * 2 - 1;
			d3 = ((double) world.rand.nextFloat() - 0.5D) * 0.5D;
			d5 = ((double) world.rand.nextFloat() - 0.5D) * 0.5D;

			d3 = (double) (world.rand.nextFloat() * 30.0F * (float) i1);
			d4 = (double) (world.rand.nextFloat() * 30.0F * (float) i1);
			d5 = (double) (world.rand.nextFloat() * 30.0F * (float) i1);

			world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, d1, d2, d0, d4, d5, d3);
			world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, d0, d1, d2, d3, d4, d5);
			world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, d2, d3, d1, d5, d3, d4);
		}
	}
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("bulb", "inventory"));
	}
	
}


