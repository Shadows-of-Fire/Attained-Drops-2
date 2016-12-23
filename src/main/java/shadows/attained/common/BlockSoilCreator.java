package shadows.attained.common;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.*;
import shadows.attained.*;

import javax.annotation.Nonnull;

public class BlockSoilCreator extends Block {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);

	public BlockSoilCreator() {
		super(Material.GROUND);
		setRegistryName("soilcreator");
		setHardness(0.8F);
		setTickRandomly(true);
		setCreativeTab(ModRegistry.Attained);
		setSoundType(SoundType.GROUND);
		setUnlocalizedName(AttainedDrops.MODID + ".soilcreator");
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
		setDefaultState(blockState.getBaseState().withProperty(getAgeProperty(), 0));
	}

	@Override
    public void randomTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random random)
    {
        updateTick(worldIn, pos, state, random);
        updateTick(worldIn, pos, state, random);
        updateTick(worldIn, pos, state, random);
    }
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(world, pos, state, rand);
		int l = getMetaFromState(state);

		if (l < 15) {
			l++;
			int i = pos.getX() + (rand.nextInt(3) - 1);
			int k = pos.getZ() + (rand.nextInt(3) - 1);
			if (world.getBlockState(new BlockPos(i, pos.getY(), k)).getBlock() == Blocks.DIRT || world.getBlockState(new BlockPos(i, pos.getY(), k)).getBlock() == Blocks.GRASS) {
				world.setBlockState(new BlockPos(i, pos.getY(), k), ModRegistry.vitalized.getDefaultState(), 2);
				world.setBlockState(pos, getStateFromMeta(l), 2);
			}
		}
		if (l == 15) {
			world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
		}
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, AGE);
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


	protected int getAge(IBlockState state) {
		return state.getValue(getAgeProperty());
	}

	public IBlockState withAge(int age) {
		return getDefaultState().withProperty(getAgeProperty(), age);
	}

	protected PropertyInteger getAgeProperty() {
		return AGE;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
