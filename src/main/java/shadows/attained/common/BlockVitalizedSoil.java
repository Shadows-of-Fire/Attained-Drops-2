package shadows.attained.common;

import java.util.Random;


import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops;
import shadows.attained.ModRegistry;

public class BlockVitalizedSoil extends Block{
	
	
	public BlockVitalizedSoil() {
		super(Material.GROUND);
		setRegistryName("vitalizedsoil");
		setHardness(0.8F);
		setTickRandomly(true);
		setCreativeTab(ModRegistry.Attained);
		setSoundType(SoundType.GROUND);
		setUnlocalizedName(AttainedDrops.MODID + "vitalizedsoil");
		setDefaultState(getStateFromMeta(0));
		GameRegistry.register(this);
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(world, pos, state, rand);

		int l = getMetaFromState(state);

		if (l < 15 && rand.nextInt(3) == 0)
		{
			l++;
			int i = pos.getX() + (rand.nextInt(3) - 1);
			int k = pos.getZ() + (rand.nextInt(3) - 1);
			if (world.getBlockState(new BlockPos(i, pos.getY(), k)).getBlock() == Blocks.DIRT || world.getBlockState(new BlockPos(i, pos.getY(), k)).getBlock() == Blocks.GRASS)
			{
				world.setBlockState(new BlockPos(i, pos.getY(), k), ModRegistry.dummysoil.getDefaultState(), 2);
				world.setBlockState(pos, getStateFromMeta(l), 2);
			}
		}
		if (l == 15)
		{
			world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
		}
	}	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("vitalizedsoil", "inventory"));
	}
	
	
	
	
	
	
	
	
}