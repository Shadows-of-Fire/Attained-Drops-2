package shadows.attained;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import shadows.attained.api.PlantingRegistry;
import shadows.attained.integration.TOPPlugin;
import shadows.attained.util.ParticleMessage;
import shadows.placebo.network.MessageHelper;
import shadows.placebo.recipe.RecipeHelper;

@Mod(AttainedDrops.MODID)
public class AttainedDrops {

	public static final String MODID = "attained_drops";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static final CreativeModeTab GROUP = new CreativeModeTab(MODID) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(AttainedRegistry.SEED);
		}

		@Override
		public void fillItemList(NonNullList<ItemStack> items) {
			items.add(new ItemStack(AttainedRegistry.SEED));
			items.add(new ItemStack(AttainedRegistry.LIFE_ESSENCE));
			items.add(new ItemStack(AttainedRegistry.VITALITY_SPREADER));
			PlantingRegistry.SOILS.values().forEach(b -> items.add(new ItemStack(b)));
			PlantingRegistry.BULBS.values().forEach(b -> items.add(new ItemStack(b)));
		};
	};

	//Formatter::off
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(MODID, "channel"))
            .clientAcceptedVersions(s->true)
            .serverAcceptedVersions(s->true)
            .networkProtocolVersion(() -> "1.0.0")
            .simpleChannel();
    //Formatter::on

	public static final RecipeHelper RECIPES = new RecipeHelper(MODID);

	public AttainedDrops() throws Exception {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);
		AttainedConfig.load();
	}

	@SubscribeEvent
	public void setup(FMLCommonSetupEvent e) {
		MinecraftForge.EVENT_BUS.register(new AttainedRegistry());
		MessageHelper.registerMessage(CHANNEL, 0, new ParticleMessage());
		AttainedRegistry.initRecipes();
		if (ModList.get().isLoaded("theoneprobe")) TOPPlugin.register();
	}

	@SubscribeEvent
	public void onMobDrop(LivingDropsEvent event) {
		if (event.getEntity() instanceof Enemy && event.getSource().getEntity() instanceof Player && event.getEntity().level.random.nextInt(Math.max(AttainedConfig.dropChance - event.getLootingLevel(), 1)) == 0) {
			event.getDrops().add(new ItemEntity(event.getEntity().level, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(AttainedRegistry.LIFE_ESSENCE)));
		}
	}

}
