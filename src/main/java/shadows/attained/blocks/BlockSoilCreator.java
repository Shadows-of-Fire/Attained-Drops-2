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
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.init.ModConfig;
import shadows.attained.init.ModGlobals;
import shadows.attained.init.ModNetworkHandler;
import shadows.attained.init.ModRegistry;
import shadows.attained.network.PacketSpawnParticle;

public class BlockSoilCreator extends Block {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);

	public BlockSoilCreator() {
		super(Material.GROUND);
		setRegistryName("soilcreator");
		setHardness(0.8F);
		setTickRandomly(true);
		setCreativeTab(ModRegistry.AD2_TAB);
		setSoundType(SoundType.GROUND);
		setUnlocalizedName(ModGlobals.MODID + ".soilcreator");
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
		setDefaultState(blockState.getBaseState().withProperty(getAgeProperty(), 0));
	}

	@Override
	public void randomTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random random) {
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
			int diameter = ModConfig.getDiameter();
			int minX = (int) (pos.getX() - Math.ceil(diameter / 2));
			int minZ = (int) (pos.getZ() - Math.ceil(diameter / 2));
			int maxX = minX + diameter;
			int maxZ = minZ + diameter;
			int i = rand.nextInt(maxX - minX) + minX;
			int k = rand.nextInt(maxZ - minZ) + minZ;
			if (world.getBlockState(new BlockPos(i, pos.getY(), k)).getBlock() == Blocks.DIRT || world.getBlockState(new BlockPos(i, pos.getY(), k)).getBlock() == Blocks.GRASS) {
				world.setBlockState(new BlockPos(i, pos.getY(), k), ModRegistry.VITALIZED_BASE.getDefaultState(), 2);
				world.setBlockState(pos, getStateFromMeta(l), 2);
				if (!world.isRemote) {
					ModNetworkHandler.getInstance().sendToAllAround(new PacketSpawnParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), i, pos.getY(), k, 3, 0.5), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 30));
				}
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
