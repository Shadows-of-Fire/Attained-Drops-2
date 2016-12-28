package shadows.attained.integration;

import javax.annotation.Nullable;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import shadows.attained.api.ITOPInfoProvider;
import shadows.attained.init.ModGlobals;

/**
 * @author p455w0rd
 *
 */
public class TOP {
	private static boolean registered;

	public static void init() {
		if (registered) {
			return;
		}
		registered = true;
		FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "shadows.attained.integration.TOP$GetTheOneProbe");
	}

	public static class GetTheOneProbe implements com.google.common.base.Function<ITheOneProbe, Void> {

		public static ITheOneProbe probe;

		@Nullable
		@Override
		public Void apply(ITheOneProbe theOneProbe) {
			probe = theOneProbe;
			probe.registerProvider(new IProbeInfoProvider() {
				@Override
				public String getID() {
					return ModGlobals.MODID + ":default";
				}

				@Override
				public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
					Block block = blockState.getBlock();
					if (block instanceof ITOPInfoProvider) {
						ITOPInfoProvider provider = (ITOPInfoProvider) block;
						provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
					}

				}
			});
			return null;
		}
	}
}
