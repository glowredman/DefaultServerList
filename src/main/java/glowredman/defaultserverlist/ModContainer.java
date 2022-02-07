package glowredman.defaultserverlist;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("defaultserverlist")
public class ModContainer {
	
	public ModContainer() {
		FMLJavaModLoadingContext.get().getModEventBus().register(this.getClass());
	}
	
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		Config.init();
	}

}
