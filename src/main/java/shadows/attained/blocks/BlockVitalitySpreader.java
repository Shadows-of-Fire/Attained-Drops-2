package shadows.attained.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IPlantable;
import shadows.attained.AttainedConfig;
import shadows.attained.AttainedDrops;
import shadows.attained.AttainedRegistry;
import shadows.attained.util.ParticleMessage;

public class BlockVitalitySpreader extends Block {

	public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 15);
	public static final Properties PROPS = Properties.create(Material.GROUND).hardnessAndResistance(0.5F, 12).sound(SoundType.GROUND).needsRandomTick();

	public BlockVitalitySpreader() {
		super(PROPS);
		setRegistryName(AttainedDrops.MODID, "vitality_spreader");
		setDefaultState(stateContainer.getBaseState().with(CHARGE, 15));
	}

	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext ctx) {
		return getDefaultState().with(CHARGE, 15 - ctx.getItem().getDamage());
	}

	@Override
	public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune) {
		ItemStack s = new ItemStack(this);
		s.setDamage(15 - state.get(CHARGE));
		drops.add(s);
	}

	@Override
	public void animateTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (rand.nextInt(20 - state.get(CHARGE)) == 0) {
			for (int i = 0; i < rand.nextInt(9); i++)
				world.addParticle(Particles.END_ROD, pos.getX() + 0.5D, pos.getY() + 1.02D, pos.getZ() + 0.5D, MathHelper.nextDouble(rand, -0.05, 0.05), 0.06D, MathHelper.nextDouble(rand, -0.05, 0.05));
		}
	}

	@Override
	protected void fillStateContainer(Builder<Block, IBlockState> builder) {
		builder.add(CHARGE);
	}

	@Override
	public void tick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (!world.isRemote) {
			genNewSoil(world, pos, state, rand);
			((WorldServer) world).getPendingBlockTicks().scheduleTick(pos, this, 40);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (!world.isRemote) ((WorldServer) world).getPendingBlockTicks().scheduleTick(pos, this, 40);
	}

	private void genNewSoil(World world, BlockPos pos, IBlockState state, Random rand) {
		int radius = AttainedConfig.INSTANCE.spreaderRadius.get();
		BlockPos pos2 = pos.add(MathHelper.nextInt(rand, radius * -1, radius), 0, MathHelper.nextInt(rand, radius * -1, radius));
		if (world.getBlockState(pos2).getBlock().canSustainPlant(world.getBlockState(pos2), world, pos2, EnumFacing.UP, (IPlantable) Blocks.DANDELION)) {
			world.setBlockState(pos2, AttainedRegistry.SOILS.get(SoilType.NONE).getDefaultState());
			if (rand.nextBoolean()) {
				if (state.get(CHARGE) == 0) {
					world.setBlockState(pos, Blocks.DIRT.getDefaultState());
					AttainedDrops.sendToTracking(new ParticleMessage(pos.up(), EnumDyeColor.RED, 2), (WorldServer) world, pos);
				} else world.setBlockState(pos, state.with(CHARGE, state.get(CHARGE) - 1));
			}
			AttainedDrops.sendToTracking(new ParticleMessage(pos2, EnumDyeColor.GREEN), (WorldServer) world, pos);
		}
	}
}
