package shadows.attained.blocks;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.attained.api.IBulb;
import shadows.attained.api.ITOPInfoProvider;
import shadows.attained.api.IVitalizedSoil;
import shadows.attained.init.ModGlobals;
import shadows.attained.init.ModRegistry;
import shadows.attained.util.AD2Util;

public class BlockVitalizedBase extends Block implements IVitalizedSoil, ITOPInfoProvider {

	private final String modid;

	public BlockVitalizedBase(String name) {
		this("", name);
	}

	public BlockVitalizedBase(String modid, String name) {
		super(Material.GROUND);
		setRegistryName(name + "_soil");
		setHardness(0.8F);
		setCreativeTab(ModRegistry.AD2_TAB);
		setSoundType(SoundType.GROUND);
		this.modid = modid.isEmpty() ? ModGlobals.MODID : modid;
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND) {
			return false;
		}
		if (!world.isRemote) {
			if (heldItem != null) {
				if (canPlayerEnrich(player, state)) {
					world.setBlockState(pos, AD2Util.getBlockFromSoil(AD2Util.getSoilFromItem(heldItem)).getDefaultState(), 2);
					world.playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, (float) 0.6, (float) 0.8);
					if (!player.capabilities.isCreativeMode) {
						player.inventory.decrStackSize(player.inventory.currentItem, 1);
					}
					return true;
				}
			}
		}
		else {
			if (player.inventory.getCurrentItem() == null) {
				if (getBulb() != null) {
					player.addChatComponentMessage(new TextComponentString(I18n.format("phrase.attaineddrops.dirtstart") + getBulb().getTextColor() + " " + AD2Util.getBulbDrop(getBulb()).getDisplayName()));
				}
				else {
					player.addChatComponentMessage(new TextComponentString(I18n.format("phrase.attaineddrops.dirtblank")));
				}
			}
		}
		return false;
	}

	private IBulb getBulb() {
		return AD2Util.getBulbFromSoil(this);
	}

	private boolean canPlayerEnrich(EntityPlayer player, IBlockState state) {
		ItemStack currentItem = player.inventory.getCurrentItem();
		return currentItem != null && AD2Util.isItemApplicable(currentItem) && !AD2Util.isSoilEnriched(AD2Util.getSoilFromBlock(state.getBlock()));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean useExtraInformation) {
		if (!AD2Util.isSoilEnriched(this)) {
			if (player.isSneaking() || Keyboard.isKeyDown(AD2Util.getSneakKey().getKeyCode())) {
				AD2Util.generateList(list);
			}
			else {
				list.add(AD2Util.getSneakString());
			}
		}
		else {
			list.add(I18n.format("tooltip.attaineddrops.enrichedwith", getBulb().getTextColor() + AD2Util.getBulbDrop(getBulb()).getDisplayName() + TextFormatting.GRAY));
		}
	}

	@Nonnull
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
		List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
		ret.add(new ItemStack(Blocks.DIRT));
		return ret;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public String getUnlocalizedName() {
		return "tile." + modid + ".vitalized." + (!AD2Util.isSoilEnriched(this) ? "base" : "enriched");
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		IBulb bulb = AD2Util.getBulbFromSoil(this);
		if (!AD2Util.isSoilEnriched(this)) {
			if (player.isSneaking() || Keyboard.isKeyDown(AD2Util.getSneakKey().getKeyCode())) {
				List<String> nameList = AD2Util.getApplicableItemNames();
				probeInfo.horizontal(new LayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(I18n.format("tooltip.attaineddrops.enableditems", TextFormatting.ITALIC + "" + TextFormatting.UNDERLINE));
				for (String line : nameList) {
					probeInfo.horizontal().text(line);
				}
			}
			else {
				probeInfo.horizontal().text(AD2Util.getSneakString());
			}
		}
		else {
			probeInfo.horizontal().text("     Type: " + bulb.getTextColor() + "" + AD2Util.getBulbDrop(bulb).getDisplayName());
		}
	}

}
