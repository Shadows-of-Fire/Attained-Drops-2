package shadows.attained.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops2;
import shadows.attained.init.ModRegistry;
import shadows.placebo.block.base.BlockBasic;
import shadows.placebo.itemblock.ItemBlockBase;

public class BlockVitalized extends BlockBasic {

	public static final PropertyInteger META = PropertyInteger.create("meta", 0, BulbTypes.values().length);
	public static final Map<ItemStack, Integer> lookup = new HashMap<ItemStack, Integer>();

	public BlockVitalized() {
		super("soil", Material.GROUND, 0.5F, 12F, AttainedDrops2.INFO);
		setSoundType(SoundType.GROUND);
		setDefaultState(this.blockState.getBaseState().withProperty(META, 0));
	}

	@Override
	public ItemBlock createItemBlock() {
		return (ItemBlock) new ItemBlockBase(this) {
			@Override
			public int getMetadata(int damage) {
				return damage;
			}

			@Override
			public String getUnlocalizedName(ItemStack stack) {
				if (stack.getMetadata() == 0) return block.getUnlocalizedName();
				else return "tile." + AttainedDrops2.MODID + ".soil.enriched";
			}
		}.setHasSubtypes(true);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(META, meta);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, state.getValue(META));
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		boolean flag = false;
		if (!stack.isEmpty() && state.getValue(META) == 0) {
			int meta = BulbTypes.getMetaFromStack(stack);
			if (meta == -1) return flag;
			if (!world.isRemote) {
				world.setBlockState(pos, getDefaultState().withProperty(META, meta + 1));
				world.playSound(player, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
				if (!player.capabilities.isCreativeMode) stack.shrink(1);
			}
			flag = true;
		} else if (hand == EnumHand.MAIN_HAND && stack.isEmpty() && state.getValue(META) == 0) {
			if (world.isRemote) player.sendMessage(new TextComponentString(I18n.format("phrase.attaineddrops2.dirtblank")));
			flag = true;
		} else if (hand == EnumHand.MAIN_HAND && stack.isEmpty() && state.getValue(META) > 0) {
			if (world.isRemote) player.sendMessage(new TextComponentString(I18n.format("phrase.attaineddrops2.dirtstart") + " " + BlockBulb.lookup.get(state.getValue(META) - 1).getDisplayName()));
			flag = true;
		}
		return flag;
	}

	public static IBlockState getBulbFromState(IBlockState state) {
		if (state.getBlock() != ModRegistry.SOIL || state.getValue(META) == 0) return null;
		return ModRegistry.BULB.getDefaultState().withProperty(BlockBulb.META, state.getValue(META) - 1);
	}

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
			if (player != null && player.isSneaking() || Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())) {
				list.add(I18n.format("tooltip.attaineddrops2.enableditems"));
				String string = "";
				for (BulbTypes type : BulbTypes.values()) {
					string = string.concat(type.getDrop().getDisplayName() + ", ");
				}
				list.add(string.substring(0, string.length() - 2));
			} else {
				list.add(I18n.format("tooltip.attaineddrops2.holdshift", Minecraft.getMinecraft().gameSettings.keyBindSneak.getDisplayName()));
			}
		} else if (stack.getMetadata() > 0) {
			list.add(I18n.format("tooltip.attaineddrops2.enrichedwith", BlockBulb.lookup.get(stack.getMetadata() - 1).getDisplayName()));
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

	@Override
	public void initModels(ModelRegistryEvent e) {
		for (int i = 0; i < BulbTypes.values().length + 1; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation(getRegistryName(), "meta=" + i));
		}
	}

}
