package shadows.attained.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEndRod;
import net.minecraft.client.particle.ParticleSimpleAnimated;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IShearable;
import shadows.attained.AttainedConfig;
import shadows.attained.AttainedDrops;

public class BlockBulb extends BlockBush implements IShearable {

	public static final VoxelShape SHAPE = makeCuboidShape(5, 0, 5, 11, 8, 11);
	public static final Properties PROPS = Properties.create(Material.PLANTS).sound(SoundType.PLANT).hardnessAndResistance(0.4F, 0);

	protected final BulbType type;

	public BlockBulb(BulbType type) {
		super(PROPS);
		setRegistryName(AttainedDrops.MODID, type.name().toLowerCase(Locale.ROOT) + "_bulb");
		this.type = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (rand.nextFloat() >= 0.6937F) {
			EnumDyeColor color = type.getColor();
			ParticleSimpleAnimated p = new ParticleEndRod(world, pos.getX() + 1.0D - rand.nextDouble(), pos.getY() + 0.4D, pos.getZ() + 1.0D - rand.nextDouble(), 0, 0.03D, 0);
			p.setColor(color.colorValue);
			p.setColorFade(color.colorValue);
			Minecraft.getInstance().particles.addEffect(p);
		}
	}

	@Override
	public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune) {
		ItemStack drop = type.createDrop();
		drops.add(drop);
		if (fortune > 0 && RANDOM.nextInt(Math.max(1, 4 - fortune)) == 0) drops.add(drop.copy());
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!AttainedConfig.INSTANCE.rightClickFarm.get()) return false;
		if (!world.isRemote) {
			ItemStack drop = type.createDrop();
			if (!player.addItemStackToInventory(drop)) spawnAsEntity(world, pos, drop);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
		return true;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return VoxelShapes.empty();
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isValidPosition(IBlockState state, IWorldReaderBase world, BlockPos pos) {
		return !world.isAirBlock(pos.down());
	}

	@Override
	public boolean isShearable(ItemStack item, IWorldReader world, BlockPos pos) {
		return true;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IWorld world, BlockPos pos, int fortune) {
		return Arrays.asList(new ItemStack(this));
	}
}
