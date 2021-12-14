package shadows.attained.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.IPlantable;
import shadows.attained.AttainedConfig;
import shadows.attained.AttainedDrops;
import shadows.attained.api.PlantingRegistry;
import shadows.attained.util.ParticleMessage;
import shadows.placebo.network.PacketDistro;

public class BlockVitalitySpreader extends Block {

	public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 15);
	public static final Properties PROPS = Properties.of(Material.DIRT).strength(0.5F, 12).sound(SoundType.GRAVEL).randomTicks();

	public BlockVitalitySpreader() {
		super(PROPS);
		setRegistryName(AttainedDrops.MODID, "vitality_spreader");
		registerDefaultState(stateDefinition.any().setValue(CHARGE, 15));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
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
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		if (rand.nextInt(20 - state.getValue(CHARGE)) == 0) {
			for (int i = 0; i < rand.nextInt(9); i++)
				world.addParticle(ParticleTypes.END_ROD, pos.getX() + 0.5D, pos.getY() + 1.02D, pos.getZ() + 0.5D, Mth.nextDouble(rand, -0.05, 0.05), 0.06D, Mth.nextDouble(rand, -0.05, 0.05));
		}
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(CHARGE);
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
		genNewSoil(world, pos, state, rand);
		world.getBlockTicks().scheduleTick(pos, this, 40);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (!world.isClientSide) ((ServerLevel) world).getBlockTicks().scheduleTick(pos, this, 40);
	}

	private void genNewSoil(Level world, BlockPos pos, BlockState state, Random rand) {
		int radius = AttainedConfig.spreaderRadius;
		BlockPos pos2 = pos.offset(Mth.nextInt(rand, radius * -1, radius), 0, Mth.nextInt(rand, radius * -1, radius));
		if (world.getBlockState(pos2).getBlock().canSustainPlant(world.getBlockState(pos2), world, pos2, Direction.UP, (IPlantable) Blocks.DANDELION)) {
			world.setBlockAndUpdate(pos2, PlantingRegistry.SOILS.get(DefaultTypes.NONE).defaultBlockState());
			if (rand.nextBoolean()) {
				if (state.getValue(CHARGE) == 0) {
					world.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
					PacketDistro.sendToTracking(AttainedDrops.CHANNEL, new ParticleMessage(pos.above(), DyeColor.RED.getFireworkColor(), 2), (ServerLevel) world, pos);
				} else world.setBlockAndUpdate(pos, state.setValue(CHARGE, state.getValue(CHARGE) - 1));
			}
			PacketDistro.sendToTracking(AttainedDrops.CHANNEL, new ParticleMessage(pos2, DyeColor.GREEN.getFireworkColor()), (ServerLevel) world, pos);
		}
	}
}
