package shadows.attained.blocks;

import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.AttainedDrops2;
import shadows.attained.init.ModRegistry;
import shadows.placebo.block.BlockBasic;
import shadows.placebo.itemblock.ItemBlockBase;

public class BlockVitalized extends BlockBasic {

	public static final PropertyEnum<SoilType> SOIL = PropertyEnum.create("type", SoilType.class);

	public BlockVitalized() {
		super("soil", Material.GROUND, 0.5F, 12F, AttainedDrops2.INFO);
		setSoundType(SoundType.GROUND);
		setDefaultState(this.blockState.getBaseState().withProperty(SOIL, SoilType.NONE));
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
		return getDefaultState().withProperty(SOIL, SoilType.values()[meta]);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return state.getValue(SOIL).get();
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(Blocks.DIRT);
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		BulbType bulb = BulbType.byStack(stack);

		if (bulb != null && !stack.isEmpty() && state.getValue(SOIL) == SoilType.NONE) {
			if (!world.isRemote) {
				world.setBlockState(pos, getDefaultState().withProperty(SOIL, SoilType.values()[bulb.ordinal() + 1]));
				world.playSound(player, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
				if (!player.capabilities.isCreativeMode) stack.shrink(1);
			}
			return true;
		}

		if (hand == EnumHand.MAIN_HAND && stack.isEmpty() && world.isRemote) {
			if (state.getValue(SOIL) == SoilType.NONE) {
				player.sendMessage(new TextComponentTranslation("phrase.attaineddrops2.dirtblank"));
				return true;
			}
			player.sendMessage(new TextComponentTranslation("phrase.attaineddrops2.dirtstart", BulbType.values()[state.getValue(SOIL).ordinal() - 1].getDisplayName()));
			return true;
		}
		
		return false;
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int i = 0; i <= BulbType.values().length; i++) {
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
				for (BulbType type : BulbType.values()) {
					string = string.concat(type.getDrop().getDisplayName() + ", ");
				}
				list.add(string.substring(0, string.length() - 2));
			} else {
				list.add(I18n.format("tooltip.attaineddrops2.holdshift", Minecraft.getMinecraft().gameSettings.keyBindSneak.getDisplayName()));
			}
		} else if (stack.getMetadata() > 0) {
			list.add(I18n.format("tooltip.attaineddrops2.enrichedwith", BulbType.values()[stack.getMetadata() - 1].getDisplayName()));
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(SOIL).ordinal();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SOIL);
	}

	@Override
	public void initModels(ModelRegistryEvent e) {
		for (SoilType s : SoilType.values()) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), s.ordinal(), new ModelResourceLocation(getRegistryName(), "type=" + s.getName()));
		}
	}

	public static IBlockState getBulbFromState(IBlockState dirt) {
		SoilType s = dirt.getValue(SOIL);
		if (s == SoilType.NONE) return null;
		return ModRegistry.BULB.getDefaultState().withProperty(BlockBulb.BULB, BulbType.values()[s.ordinal() - 1]);
	}

}
