package shadows.attained.integration;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import shadows.attained.api.IBulb;
import shadows.attained.api.IVitalizedSoil;
import shadows.attained.blocks.BlockBulb;
import shadows.attained.blocks.BlockPlant;
import shadows.attained.blocks.BlockVitalized;
import shadows.attained.util.AD2Util;

/**
 * @author p455w0rd
 *
 */
public class WAILA {

	public static void callbackRegister(IWailaRegistrar registrar) {
		registrar.registerStackProvider(new Provider(), BlockPlant.class);
		registrar.registerBodyProvider(new Provider(), BlockPlant.class);
		registrar.registerBodyProvider(new Provider(), BlockVitalized.class);
		registrar.registerHeadProvider(new Provider(), BlockPlant.class);
		registrar.registerHeadProvider(new Provider(), BlockBulb.class);
		registrar.registerHeadProvider(new Provider(), BlockVitalized.class);
	}

	public static class Provider implements IWailaDataProvider {

		@Override
		public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
			if (accessor.getBlock() != null) {
				BlockPos pos = accessor.getPosition();
				World world = accessor.getWorld();
				IBlockState state = world.getBlockState(pos);
				Block block = state.getBlock();
				if (AD2Util.isPlant(block)) {
					if (AD2Util.getBulbFromPlant(world, pos) != null) {
						return new ItemStack(AD2Util.getBlockFromBulb(AD2Util.getBulbFromPlant(world, pos)));
					}
				}
			}
			return null;
		}

		@Override
		public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
				IWailaConfigHandler config) {
			currenttip = Lists.newArrayList();
			if (accessor.getBlock() != null) {
				BlockPos pos = accessor.getPosition();
				World world = accessor.getWorld();
				IBlockState state = world.getBlockState(pos);
				Block block = state.getBlock();
				if (AD2Util.isPlant(block)) {
					if (AD2Util.getBulbFromPlant(world, pos) != null) {
						IBulb bulb = AD2Util.getBulbFromPlant(world, pos);
						currenttip
								.add(bulb.getTextColor() + "" + AD2Util.getBulbDrop(bulb).getDisplayName() + " Plant");
					}
				} else if (AD2Util.isBulb(block)) {
					IBulb bulb = AD2Util.getBulbFromBlock(block);
					currenttip.add(bulb.getTextColor() + "" + AD2Util.getBulbDrop(bulb).getDisplayName() + " Bulb");
				} else if (AD2Util.isSoil(block)) {
					IVitalizedSoil soil = AD2Util.getSoilFromBlock(block);
					if (AD2Util.isSoilEnriched(soil)) {
						IBulb bulb = AD2Util.getBulbFromSoil(soil);
						currenttip.add(bulb.getTextColor() + "" + AD2Util.getBulbDrop(bulb).getDisplayName() + " "
								+ new ItemStack(block).getDisplayName());
					} else {
						currenttip.add(TextFormatting.WHITE + "" + new ItemStack(block).getDisplayName());
					}
				}
			}
			return currenttip;
		}

		@Override
		public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
				IWailaConfigHandler config) {
			if (accessor.getBlock() != null) {
				BlockPos pos = accessor.getPosition();
				World world = accessor.getWorld();
				IBlockState state = world.getBlockState(pos);
				Block block = state.getBlock();
				if (AD2Util.isPlant(block)) {
					if (AD2Util.getBulbFromPlant(world, pos) != null) {
						currenttip.add("Growth: " + AD2Util.getPlantGrowthPercent(state) + "%");
					}
				}
				if (AD2Util.isSoil(block)) {
					IVitalizedSoil soil = AD2Util.getSoilFromBlock(block);
					if (!AD2Util.isSoilEnriched(soil)) {
						if (accessor.getPlayer().isSneaking()
								|| Keyboard.isKeyDown(AD2Util.getSneakKey().getKeyCode())) {
							AD2Util.generateList(currenttip);
						} else {
							currenttip.add(AD2Util.getSneakString());
						}
					}
				}
			}
			return currenttip;
		}

		@Override
		public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
				IWailaConfigHandler config) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
				BlockPos pos) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
