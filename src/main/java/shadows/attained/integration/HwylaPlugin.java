package shadows.attained.integration;

import java.util.List;

import mcp.mobius.waila.Waila;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import shadows.attained.AttainedRegistry;
import shadows.attained.api.PlantingRegistry;
import shadows.attained.blocks.BlockBulb;
import shadows.attained.blocks.BlockPlant;
import shadows.attained.blocks.BlockSoil;
import shadows.attained.blocks.BlockVitalitySpreader;
import shadows.attained.blocks.DefaultTypes;

@WailaPlugin
public class HwylaPlugin implements IWailaPlugin {

	@Override
	public void register(IRegistrar registrar) {
		registrar.registerComponentProvider(new PlantProvider(), TooltipPosition.BODY, BlockPlant.class);
		registrar.registerComponentProvider(new SoilProvider(), TooltipPosition.BODY, BlockSoil.class);
		registrar.registerComponentProvider(new SpreaderProvider(), TooltipPosition.BODY, BlockVitalitySpreader.class);
		registrar.registerComponentProvider(new BulbProvider(), TooltipPosition.HEAD, BlockBulb.class);
	}

	public static class PlantProvider implements IComponentProvider {

		@Override
		public void appendBody(List<ITextComponent> currenttip, IDataAccessor accessor, IPluginConfig config) {
			if (accessor.getBlock() != null) {
				BlockPos pos = accessor.getPosition();
				World world = accessor.getWorld();
				BlockState state = world.getBlockState(pos);
				Block block = state.getBlock();
				if (block instanceof BlockPlant) {
					currenttip.add(new TranslationTextComponent("tooltip.attained_drops.growth", (int) (100 * ((float) state.get(BlockPlant.AGE) / AttainedRegistry.PLANT.getMaxAge())) + "%"));
					BlockState down = world.getBlockState(pos.down());
					if (down.getBlock() instanceof BlockSoil) {
						BlockBulb bulb = PlantingRegistry.BULBS.get(((BlockSoil) down.getBlock()).type);
						if (bulb == null) return;
						ITextComponent name = bulb.getTranslatedName();
						currenttip.add(new TranslationTextComponent("tooltip.attained_drops.growing", name));
					}
				}
			}
		}

	}

	public static class SoilProvider implements IComponentProvider {

		@Override
		public void appendBody(List<ITextComponent> currenttip, IDataAccessor accessor, IPluginConfig config) {
			if (accessor.getBlock() != null) {
				BlockPos pos = accessor.getPosition();
				World world = accessor.getWorld();
				BlockState state = world.getBlockState(pos);
				Block block = state.getBlock();
				if (block instanceof BlockSoil) {
					if (((BlockSoil) block).type != DefaultTypes.NONE) {
						currenttip.add(new TranslationTextComponent("tooltip.attained_drops.enrichedwith", ((BlockSoil) block).type.getDrop().getDisplayName()));
					}
				}
			}
		}

	}

	public static class SpreaderProvider implements IComponentProvider {

		@Override
		public void appendBody(List<ITextComponent> currenttip, IDataAccessor accessor, IPluginConfig config) {
			if (accessor.getBlock() != null) {
				BlockPos pos = accessor.getPosition();
				World world = accessor.getWorld();
				BlockState state = world.getBlockState(pos);
				Block block = state.getBlock();
				if (block instanceof BlockVitalitySpreader) {
					currenttip.add(new TranslationTextComponent("tooltip.attained_drops.creatorcharge", state.get(BlockVitalitySpreader.CHARGE)));
				}
			}
		}

	}

	public static class BulbProvider implements IComponentProvider {

		@Override
		public void appendHead(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
			tooltip.clear();
			tooltip.add(new StringTextComponent(String.format(Waila.CONFIG.get().getFormatting().getBlockName(), accessor.getStack().getDisplayName().getString())));
		}

	}

}
