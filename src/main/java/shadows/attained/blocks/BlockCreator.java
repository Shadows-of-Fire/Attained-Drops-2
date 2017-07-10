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
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops2;
import shadows.attained.init.Config;
import shadows.attained.init.DataLists;
import shadows.attained.init.ModRegistry;
import shadows.attained.proxy.CommonProxy;
import shadows.attained.util.IHasModel;
import shadows.attained.util.ParticleMessage;

public class BlockCreator extends Block implements IHasModel {

	private static final String regname = "creator";
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
		DataLists.BLOCKS.add(this);
		ItemBlock k = new ItemBlock(this) {
			@Override
			public int getMetadata(int damage) {
				return damage;
			};
		};
		DataLists.ITEMS.add(k.setHasSubtypes(true).setRegistryName(getRegistryName()));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.blockState.getBaseState().withProperty(CHARGE, 15 - meta);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 15 - state.getValue(CHARGE);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (rand.nextInt(20 - state.getValue(CHARGE)) == 0) {
			for (int i = 0; i < rand.nextInt(9); i++)
				world.spawnParticle(EnumParticleTypes.END_ROD, pos.getX() + 0.5D, pos.getY() + 1.02D, pos.getZ() + 0.5D, MathHelper.nextDouble(rand, -0.05, 0.05), 0.06D, MathHelper.nextDouble(rand, -0.05, 0.05));
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
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation(getRegistryName(), "charge=" + (15 - i)));
		}
	}

	@Override
	public int tickRate(World world) {
		return 1;
	}

	@Override
	public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, Random rand) {
		genNewSoil(world, pos, state, rand);
	}

	private void genNewSoil(World world, BlockPos pos, IBlockState state, Random rand) {
		int radius = Config.creatorRadius;
		BlockPos pos2 = pos.add(MathHelper.getInt(rand, radius * -1, radius), 0, MathHelper.getInt(rand, radius * -1, radius));
		if (Blocks.YELLOW_FLOWER.canPlaceBlockAt(world, pos2.up())) {
			world.setBlockState(pos2, ModRegistry.SOIL.getDefaultState());
			if (rand.nextBoolean()) {
				if (state.getValue(CHARGE) - 1 <= 0)
					world.setBlockState(pos, Blocks.DIRT.getDefaultState());
				else
					world.setBlockState(pos, state.withProperty(CHARGE, state.getValue(CHARGE) - 1));
			}
			CommonProxy.INSTANCE.sendToAllAround(new ParticleMessage(pos2), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 30));

		}
	}
}
