package shadows.attained.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops2;
import shadows.attained.init.ModRegistry;

public class BlockVitalized extends Block {

	public static final PropertyInteger META = PropertyInteger.create("meta", 0, BulbTypes.values().length);
	public static final Map<ItemStack, Integer> lookup = new HashMap<ItemStack, Integer>();

	public BlockVitalized(String name) {
		super(Material.GROUND);
		setSoundType(SoundType.GROUND);
		setHardness(0.5F);
		setRegistryName(name);
		setUnlocalizedName(AttainedDrops2.MODID + "." + name + ".base");
		setDefaultState(this.blockState.getBaseState().withProperty(META, 0));
		setCreativeTab(ModRegistry.AD2_TAB);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this) {
			@Override
			public int getMetadata(int damage) {
				return damage;
			}

			@Override
			public String getUnlocalizedName(ItemStack stack) {
				if (stack.getMetadata() == 0)
					return block.getUnlocalizedName();
				else
					return "tile." + AttainedDrops2.MODID + "." + name + ".enriched";
			}
		}, getRegistryName());
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(META, meta);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(Blocks.DIRT));
		return ret;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if (!stack.isEmpty() && state.getValue(META) == 0 && !world.isRemote) {
			int meta = BulbTypes.getMetaFromStack(stack);
			if (meta == -1)
				return false;
			world.setBlockState(pos, getDefaultState().withProperty(META, meta + 1));
			world.playSound(player, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
			if (!player.capabilities.isCreativeMode)
				stack.shrink(1);
			return true;
		} else if (hand == EnumHand.MAIN_HAND && stack.isEmpty() && !world.isRemote && state.getValue(META) == 0) {
			player.sendMessage(new TextComponentString(I18n.format("phrase.attaineddrops2.dirtblank")));
			return true;
		} else if (hand == EnumHand.MAIN_HAND && stack.isEmpty() && !world.isRemote && state.getValue(META) > 0) {
			player.sendMessage(new TextComponentString(I18n.format("phrase.attaineddrops2.dirtstart") + " "
					+ BlockBulb.lookup.get(state.getValue(META) - 1).getDisplayName()));
			return true;
		}
		return false;
	}

	@Nullable
	public static IBlockState getBulbFromState(IBlockState state) {
		if (state.getValue(META) == 0)
			return null;
		return ModRegistry.BULB.getDefaultState().withProperty(BlockBulb.META, state.getValue(META) - 1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int i = 0; i <= BulbTypes.values().length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag useExtraInformation) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (stack.getMetadata() == 0) {
			if (player.isSneaking()
					|| Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())) {
				list.add(I18n.format("tooltip.attaineddrops2.enableditems"));
				String string = "";
				for (BulbTypes type : BulbTypes.values()) {
					string = string.concat(type.getDrop().getDisplayName() + ", ");
				}
				list.add(string.substring(0, string.length() - 2));
			} else {
				list.add(I18n.format("tooltip.attaineddrops2.holdshift",
						Minecraft.getMinecraft().gameSettings.keyBindSneak.getDisplayName()));
			}
		} else if (stack.getMetadata() > 0) {
			list.add(I18n.format("tooltip.attaineddrops2.enrichedwith",
					BlockBulb.lookup.get(stack.getMetadata() - 1).getDisplayName()));
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(META);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, META);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		for (int i = 0; i < BulbTypes.values().length + 1; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i,
					new ModelResourceLocation(getRegistryName(), "meta=" + i));
		}
	}

}
