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

public class BlockVitalizedSoil extends Block {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);

	public BlockVitalizedSoil() {
		super(Material.GROUND);
		setRegistryName("vitalizedsoil");
		setHardness(0.8F);
		setTickRandomly(true);
		setCreativeTab(ModRegistry.Attained);
		setSoundType(SoundType.GROUND);
		setUnlocalizedName(AttainedDrops.MODID + "vitalizedsoil");
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
		setDefaultState(blockState.getBaseState().withProperty(getAgeProperty(), Integer.valueOf(0)));
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(world, pos, state, rand);
		int l = getMetaFromState(state);

		if (l < 15 && rand.nextInt(3) == 0) {
			l++;
			int i = pos.getX() + (rand.nextInt(3) - 1);
			int k = pos.getZ() + (rand.nextInt(3) - 1);
			if (world.getBlockState(new BlockPos(i, pos.getY(), k)).getBlock() == Blocks.DIRT || world.getBlockState(new BlockPos(i, pos.getY(), k)).getBlock() == Blocks.GRASS) {
				world.setBlockState(new BlockPos(i, pos.getY(), k), ModRegistry.dummysoil.getDefaultState(), 2);
				world.setBlockState(pos, getStateFromMeta(l), 2);
			}
		}
		if (l == 15) {
			world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {
				AGE
		});
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return withAge(meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return getAge(state);
	}


	protected int getAge(IBlockState state) {
		return state.getValue(getAgeProperty()).intValue();
	}

	public IBlockState withAge(int age) {
		return getDefaultState().withProperty(getAgeProperty(), Integer.valueOf(age));
	}

	protected PropertyInteger getAgeProperty() {
		return AGE;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("vitalizedsoil", "inventory"));
	}

}
