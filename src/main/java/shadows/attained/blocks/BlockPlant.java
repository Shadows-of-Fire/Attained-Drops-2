package shadows.attained.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import shadows.attained.AttainedConfig;
import shadows.attained.AttainedDrops;
import shadows.attained.AttainedRegistry;
import shadows.attained.api.PlantingRegistry;
import shadows.attained.util.ParticleMessage;
import shadows.placebo.network.PacketDistro;

public class BlockPlant extends BushBlock implements BonemealableBlock {

	public static final IntegerProperty AGE = CropBlock.AGE;
	public static final IntegerProperty BULBS = IntegerProperty.create("bulbs", 0, 4);
	public static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.box(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 4.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 12.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D) };
	public static final Properties PROPS = Properties.of(Material.PLANT).strength(0.2F, 0).sound(SoundType.GRASS).randomTicks();

	public BlockPlant() {
		super(PROPS);
		setRegistryName(AttainedDrops.MODID, "plant");
		registerDefaultState(stateDefinition.any().setValue(AGE, 0).setValue(BULBS, 0));

	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
		return state.getBlock() instanceof BlockSoil;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE_BY_AGE[state.getValue(AGE)];
	}

	@Override
	public boolean isBonemealSuccess(Level world, Random rand, BlockPos pos, BlockState state) {
		return AttainedConfig.allowBonemeal;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder ctx) {
		List<ItemStack> drops = new ArrayList<>();
		if (ThreadLocalRandom.current().nextFloat() >= 0.3) {
			drops.add(new ItemStack(AttainedRegistry.SEED));
		}
		return drops;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return Shapes.empty();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean bool) {
		int age = getAge(world.getBlockState(pos));
		return age < this.getMaxAge() || age == getMaxAge() && world.getBlockState(pos.above()).isAir();
	}

	@Override
	public void performBonemeal(ServerLevel world, Random rand, BlockPos pos, BlockState state) {
		if (world.isClientSide) return;
		int age = getAge(state);
		if (age < getMaxAge()) {
			setAge(world, pos, getAge(state) + 1);
			return;
		}

		Block down = world.getBlockState(pos.below()).getBlock();
		if (down instanceof BlockSoil && isMaxAge(state) && world.isEmptyBlock(pos.above())) {
			BlockBulb place = PlantingRegistry.BULBS.get(((BlockSoil) down).type);
			if (place == null) return;
			PacketDistro.sendToTracking(AttainedDrops.CHANNEL, new ParticleMessage(pos.above(), place.type.getColor(), 1), world, pos);
			world.setBlockAndUpdate(pos.above(), place.defaultBlockState());
			int bulbsGrown = state.getValue(BULBS);
			if (bulbsGrown > 0 && rand.nextInt(5 - bulbsGrown) == 0) {
				world.setBlockAndUpdate(pos.below(), (AttainedConfig.revertToDirt ? Blocks.DIRT : PlantingRegistry.SOILS.get(DefaultTypes.NONE)).defaultBlockState());
				world.setBlockAndUpdate(pos, state.setValue(BULBS, 0));
				PacketDistro.sendToTracking(AttainedDrops.CHANNEL, new ParticleMessage(pos.above(), DyeColor.RED.getFireworkColor(), 2), world, pos);
			} else world.setBlockAndUpdate(pos, state.setValue(BULBS, bulbsGrown + 1));
		}
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		Block down = world.getBlockState(pos.below()).getBlock();
		if (down instanceof BlockSoil && PlantingRegistry.BULBS.get(((BlockSoil) down).type) == null) {
			return down.use(down.defaultBlockState(), world, pos.below(), player, handIn, hit);
		}
		return InteractionResult.PASS;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (world.random.nextInt(10) == 0 && isValidBonemealTarget(world, pos, state, true)) performBonemeal(world, world.random, pos, state);
		else if (world.getBlockState(pos.below()).getBlock() instanceof SnowyDirtBlock && !(world.getBlockState(pos.above()).getBlock() instanceof BlockBulb) && state.getValue(AGE) > 0) {
			world.setBlockAndUpdate(pos, state.setValue(AGE, state.getValue(AGE) - 1));
		}
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
		return new ItemStack(AttainedRegistry.SEED);
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(AGE).add(BULBS);
	}

	public int getMaxAge() {
		return 7;
	}

	public int getAge(BlockState state) {
		return state.getValue(AGE);
	}

	private void setAge(Level world, BlockPos pos, int age) {
		world.setBlock(pos, withAge(age), 2);
	}

	public BlockState withAge(int age) {
		return defaultBlockState().setValue(AGE, age);
	}

	public boolean isMaxAge(BlockState state) {
		return state.getValue(AGE) == getMaxAge();
	}

}
