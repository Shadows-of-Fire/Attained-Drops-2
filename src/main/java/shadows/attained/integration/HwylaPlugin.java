package shadows.attained.integration;

import mcp.mobius.waila.Waila;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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
		public void appendTooltip(ITooltip currenttip, BlockAccessor accessor, IPluginConfig config) {
			if (accessor.getBlock() != null) {
				BlockPos pos = accessor.getPosition();
				Level world = accessor.getLevel();
				BlockState state = world.getBlockState(pos);
				Block block = state.getBlock();
				if (block instanceof BlockPlant) {
					//currenttip.add(new TranslatableComponent("tooltip.attained_drops.growth", (int) (100 * ((float) state.getValue(BlockPlant.AGE) / AttainedRegistry.PLANT.getMaxAge())) + "%"));
					BlockState down = world.getBlockState(pos.below());
					if (down.getBlock() instanceof BlockSoil) {
						BlockBulb bulb = PlantingRegistry.BULBS.get(((BlockSoil) down.getBlock()).type);
						if (bulb == null) return;
						Component name = bulb.getName();
						currenttip.add(new TranslatableComponent("tooltip.attained_drops.growing", name));
					}
				}
			}
		}

	}

	public static class SoilProvider implements IComponentProvider {

		@Override
		public void appendTooltip(ITooltip currenttip, BlockAccessor accessor, IPluginConfig config) {
			if (accessor.getBlock() != null) {
				BlockPos pos = accessor.getPosition();
				Level world = accessor.getLevel();
				BlockState state = world.getBlockState(pos);
				Block block = state.getBlock();
				if (block instanceof BlockSoil) {
					if (((BlockSoil) block).type != DefaultTypes.NONE) {
						currenttip.add(new TranslatableComponent("tooltip.attained_drops.enrichedwith", ((BlockSoil) block).type.getDrop().getHoverName()));
					}
				}
			}
		}

	}

	public static class SpreaderProvider implements IComponentProvider {

		@Override
		public void appendTooltip(ITooltip currenttip, BlockAccessor accessor, IPluginConfig config) {
			if (accessor.getBlock() != null) {
				BlockPos pos = accessor.getPosition();
				Level world = accessor.getLevel();
				BlockState state = world.getBlockState(pos);
				Block block = state.getBlock();
				if (block instanceof BlockVitalitySpreader) {
					currenttip.add(new TranslatableComponent("tooltip.attained_drops.creatorcharge", state.getValue(BlockVitalitySpreader.CHARGE)));
				}
			}
		}

	}

	public static class BulbProvider implements IComponentProvider {

		@Override
		public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
			tooltip.clear();
			tooltip.add(new TextComponent(String.format(Waila.CONFIG.get().getFormatting().getBlockName(), accessor.getPickedResult().getHoverName().getString())));
		}

	}

}
