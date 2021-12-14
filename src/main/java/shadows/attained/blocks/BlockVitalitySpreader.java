package shadows.attained.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import shadows.attained.AttainedConfig;
import shadows.attained.AttainedDrops;
import shadows.attained.api.PlantingRegistry;
import shadows.attained.util.ParticleMessage;
import shadows.placebo.util.NetworkUtils;

public class BlockVitalitySpreader extends Block {

	public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 15);
	public static final Properties PROPS = Properties.of(Material.DIRT).strength(0.5F, 12).sound(SoundType.GRAVEL).randomTicks();

	public BlockVitalitySpreader() {
		super(PROPS);
		setRegistryName(AttainedDrops.MODID, "vitality_spreader");
		registerDefaultState(stateDefinition.any().setValue(CHARGE, 15));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return defaultBlockState().setValue(CHARGE, 15 - ctx.getItemInHand().getDamageValue());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder ctx) {
		List<ItemStack> drops = new ArrayList<>();
		ItemStack s = new ItemStack(this);
		s.setDamageValue(15 - state.getValue(CHARGE));
		drops.add(s);
		return drops;
	}

	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		if (rand.nextInt(20 - state.getValue(CHARGE)) == 0) {
			for (int i = 0; i < rand.nextInt(9); i++)
				world.addParticle(ParticleTypes.END_ROD, pos.getX() + 0.5D, pos.getY() + 1.02D, pos.getZ() + 0.5D, MathHelper.nextDouble(rand, -0.05, 0.05), 0.06D, MathHelper.nextDouble(rand, -0.05, 0.05));
		}
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(CHARGE);
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		genNewSoil(world, pos, state, rand);
		world.getBlockTicks().scheduleTick(pos, this, 40);
	}

	@Override
	public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (!world.isClientSide) ((ServerWorld) world).getBlockTicks().scheduleTick(pos, this, 40);
	}

	private void genNewSoil(World world, BlockPos pos, BlockState state, Random rand) {
		int radius = AttainedConfig.spreaderRadius;
		BlockPos pos2 = pos.offset(MathHelper.nextInt(rand, radius * -1, radius), 0, MathHelper.nextInt(rand, radius * -1, radius));
		if (world.getBlockState(pos2).getBlock().canSustainPlant(world.getBlockState(pos2), world, pos2, Direction.UP, (IPlantable) Blocks.DANDELION)) {
			world.setBlockAndUpdate(pos2, PlantingRegistry.SOILS.get(DefaultTypes.NONE).defaultBlockState());
			if (rand.nextBoolean()) {
				if (state.getValue(CHARGE) == 0) {
					world.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
					NetworkUtils.sendToTracking(AttainedDrops.CHANNEL, new ParticleMessage(pos.above(), DyeColor.RED.textureDiffuseColor, 2), (ServerWorld) world, pos);
				} else world.setBlockAndUpdate(pos, state.setValue(CHARGE, state.getValue(CHARGE) - 1));
			}
			NetworkUtils.sendToTracking(AttainedDrops.CHANNEL, new ParticleMessage(pos2, DyeColor.GREEN.textureDiffuseColor), (ServerWorld) world, pos);
		}
	}
}
