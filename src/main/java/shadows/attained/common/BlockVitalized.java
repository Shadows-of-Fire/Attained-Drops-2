package shadows.attained.common;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
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
import org.lwjgl.input.Keyboard;
import shadows.attained.AttainedDrops;
import shadows.attained.ModRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class BlockVitalized extends Block {

  public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, BlockBulb.MobDrops.length);

  public BlockVitalized() {
    super(Material.GROUND);
    setRegistryName("vitalized");
    setHardness(0.8F);
    setCreativeTab(ModRegistry.Attained);
    setSoundType(SoundType.GROUND);
    setUnlocalizedName(AttainedDrops.MODID + ".vitalized");
    GameRegistry.register(this);
    GameRegistry.register(new ItemBlock(this), getRegistryName());
    setDefaultState(blockState.getBaseState().withProperty(getTypeProperty(), 0));
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (hand != EnumHand.MAIN_HAND) {
      return false;
    }
    IBlockState k = world.getBlockState(pos);
    if (!world.isRemote) {
      if (player.inventory.getCurrentItem() != null) {
        for (int i = 0; i < BlockBulb.MobDrops.length; i++) {
          if (canPlayerEnrich(player, k, i)) {
            world.setBlockState(pos, getStateFromMeta(i + 1), 2);
            world.playSound(player, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, (float) 0.6, (float) 0.8);
            if (!player.capabilities.isCreativeMode) {
              player.inventory.decrStackSize(player.inventory.currentItem, 1);
            }
            return true;
          }
        }
      }
    } else {
      if (player.inventory.getCurrentItem() == null) {
        int mx = getMetaFromState(k);
        if (mx != 0) {
          player.addChatComponentMessage(new TextComponentString(I18n.format("phrase.AttainedDrops.DirtStart") + TextFormatting.GREEN + " " + I18n.format(BlockBulb.MobDrops[mx - 1].getUnlocalizedName(new ItemStack(BlockBulb.MobDrops[mx - 1])) + ".name")));
        } else {
          player.addChatComponentMessage(new TextComponentString(I18n.format("phrase.AttainedDrops.DirtBlank")));
        }
      }
    }
    return false;
  }

  private boolean canPlayerEnrich(EntityPlayer player, IBlockState state, int dropNumber) {
    ItemStack currentItem = player.inventory.getCurrentItem();
    if (currentItem != null && currentItem.getItem() == BlockBulb.MobDrops[dropNumber]) {
      if (getMetaFromState(state) != (dropNumber + 1)) {
        if (player.capabilities.isCreativeMode) {
          return true;
        }
      }
    }

    return false;
  }


  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, TYPE);
  }


  @Override
  public IBlockState getStateFromMeta(int meta) {
    return withType(meta);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return getType(state);
  }

  public static int getSoilMeta(IBlockState state) {
    return getType(state);
  }

  protected static int getType(IBlockState state) {
    return state.getValue(getTypeProperty());
  }

  public IBlockState withType(int type) {
    return getDefaultState().withProperty(getTypeProperty(), type);
  }

  protected static PropertyInteger getTypeProperty() {
    return TYPE;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean useExtraInformation) {
    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
      list.add("Right click with enabled drop to enrich");
      list.add("Enabled Items:");
      printItems(list);
    } else {
      list.add(TextFormatting.GRAY + "Hold Shift");
    }
  }

  public static void printItems(List<String> list) {
    String string = "";
    for (int i = 0; i < 4; i++) {

      string = string + BlockBulb.MobDrops[i].getItemStackDisplayName(new ItemStack(BlockBulb.MobDrops[i])) + ", ";

    }
    list.add(TextFormatting.GREEN + string);
    string = "";
    for (int i = 4; i < BlockBulb.MobDrops.length; i++) {

      string = string + BlockBulb.MobDrops[i].getItemStackDisplayName(new ItemStack(BlockBulb.MobDrops[i])) + ", ";

    }
    list.add(TextFormatting.GREEN + string);
  }


  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos,  IBlockState state, int fortune) {
    List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
    ret.add(new ItemStack(Blocks.DIRT));
    return ret;
  }

  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
  }

}
