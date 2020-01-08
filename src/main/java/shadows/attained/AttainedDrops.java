package shadows.attained;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import shadows.attained.api.PlantingRegistry;
import shadows.attained.util.ParticleMessage;
import shadows.placebo.recipe.RecipeHelper;
import shadows.placebo.util.NetworkUtils;

@Mod(AttainedDrops.MODID)
public class AttainedDrops {

	public static final String MODID = "attained_drops";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static final ItemGroup GROUP = new ItemGroup(MODID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(AttainedRegistry.SEED);
		}

		@Override
		public void fill(NonNullList<ItemStack> items) {
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
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AttainedConfig.spec);
		FMLJavaModLoadingContext.get().getModEventBus().register(AttainedConfig.class);
	}

	@SubscribeEvent
	public void setup(FMLCommonSetupEvent e) {
		MinecraftForge.EVENT_BUS.register(new AttainedRegistry());
		NetworkUtils.registerMessage(CHANNEL, 0, new ParticleMessage());
		AttainedRegistry.initRecipes();
	}

	@SubscribeEvent
	public void onMobDrop(LivingDropsEvent event) {
		if (event.getEntity() instanceof IMob && event.getSource().getTrueSource() instanceof PlayerEntity && event.getEntity().world.rand.nextInt(Math.max(AttainedConfig.INSTANCE.dropChance.get() - event.getLootingLevel(), 1)) == 0) {
			event.getDrops().add(new ItemEntity(event.getEntity().world, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(AttainedRegistry.LIFE_ESSENCE)));
		}
	}

}
