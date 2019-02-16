package shadows.attained;

import javax.security.auth.login.Configuration;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import shadows.attained.init.AD2Config;
import shadows.attained.init.ModRegistry;
import shadows.attained.util.ParticleHandler;
import shadows.attained.util.ParticleMessage;
import shadows.attained.util.RecipeHelper;

@Mod(AttainedDrops.MODID)
public class AttainedDrops {

	public static final String MODID = "attained_drops";

	public static final ItemGroup GROUP = new ItemGroup(MODID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Blocks.ACACIA_BUTTON);
		}
	};

	//Formatter::off
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(MODID, "channel"))
            .clientAcceptedVersions(s->true)
            .serverAcceptedVersions(s->true)
            .networkProtocolVersion(() -> "1.0.0")
            .simpleChannel();
    //Formatter::on

	public static final RecipeHelper RECIPES = new RecipeHelper(MODID, "Attained Drops");

	public static Configuration config;

	@SubscribeEvent
	public void setup(FMLCommonSetupEvent e) {
		AD2Config.load();
		MinecraftForge.EVENT_BUS.register(new ModRegistry());
		CHANNEL.registerMessage(0, ParticleMessage.class, ParticleMessage::write, ParticleMessage::read, ParticleHandler::handle);
		MinecraftForge.EVENT_BUS.addListener(this::onMobDrop);
		ModRegistry.initRecipes();
	}

	@SubscribeEvent
	public void serverStart(FMLServerStartingEvent e) {
		e.getServer().getResourceManager().addReloadListener(RECIPES);
	}

	@SubscribeEvent
	public void onMobDrop(LivingDropsEvent event) {
		if (event.getEntity() instanceof IMob && event.getSource().getTrueSource() instanceof EntityPlayer && event.getEntity().world.rand.nextInt(Math.max(AD2Config.dropChance.get() - event.getLootingLevel(), 1)) == 0) {
			event.getDrops().add(new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, new ItemStack(ModRegistry.LIFE_ESSENCE)));
		}
	}

	public static void sendToTracking(Object packet, WorldServer world, BlockPos pos) {
		world.getPlayerChunkMap().getEntry(pos.getX() >> 4, pos.getZ() >> 4).getWatchingPlayers().forEach(p -> {
			CHANNEL.sendTo(packet, p.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
		});
	}

}
