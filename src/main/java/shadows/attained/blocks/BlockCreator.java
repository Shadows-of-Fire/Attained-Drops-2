package shadows.attained.blocks;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops2;
import shadows.attained.init.ModRegistry;

public class BlockCreator extends Block {

	final String regname = "creator";
	public static final PropertyInteger CHARGE = PropertyInteger.create("charge", 0, 15);

	public BlockCreator() {
		super(Material.GROUND);
		setRegistryName(regname);
		setCreativeTab(ModRegistry.AD2_TAB);
		setUnlocalizedName(AttainedDrops2.MODID + "." + regname);
		setSoundType(SoundType.GROUND);
		setHardness(0.5F);
		setTickRandomly(true);
		setDefaultState(this.blockState.getBaseState().withProperty(CHARGE, 15));
		GameRegistry.register(this);
		ItemBlock k = new ItemBlock(this) {
			@Override
			public int getMetadata(int damage) {
				return damage;
			};
		};
		GameRegistry.register(k.setHasSubtypes(true), getRegistryName());
	}
	
	@Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return this.blockState.getBaseState().withProperty(CHARGE, 15 - meta);
    }
	
	@Override
    public int damageDropped(IBlockState state)
    {
        return 15 - state.getValue(CHARGE);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (rand.nextInt(16 - state.getValue(CHARGE)) == 0){
			for(int i = 0; i < 3; i++)
			world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, pos.getX() + 1.0D - rand.nextDouble(), pos.getY() + 1.1D,
					pos.getZ() + 1.0D - rand.nextDouble(), 0, 0.8D, 0);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(CHARGE, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(CHARGE);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CHARGE);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		for (int i = 0; i <= 15; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i,
					new ModelResourceLocation(getRegistryName(), "charge=" + i));
		}
	}

	@Override
	public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, Random rand) {
		for(int i = 0; i < 5; i++){
			genNewSoil(world,pos,state,rand);
		}
	}
	
	
	private void genNewSoil(World world, BlockPos pos, IBlockState state, Random rand){
		BlockPos pos2 = pos.add(MathHelper.getInt(rand, -3, 3), 0, MathHelper.getInt(rand, -3, 3));
		if (world.getBlockState(pos2).getBlock() == Blocks.DIRT
				|| world.getBlockState(pos2).getBlock() == Blocks.GRASS) {
			world.setBlockState(pos2, ModRegistry.SOIL.getDefaultState());
			if (rand.nextInt(4) == 0) {
				if (state.getValue(CHARGE) - 1 <= 0)
					world.setBlockState(pos, Blocks.DIRT.getDefaultState());
				else
					world.setBlockState(pos, state.withProperty(CHARGE, state.getValue(CHARGE) - 1));
			}
		}
	}
}
