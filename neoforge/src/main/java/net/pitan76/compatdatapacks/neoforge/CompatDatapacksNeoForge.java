package net.pitan76.compatdatapacks.neoforge;

import dev.architectury.platform.forge.EventBuses;
import net.pitan76.compatdatapacks.CompatDatapacks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CompatDatapacks.MOD_ID)
public class CompatDatapacksNeoForge {
    public CompatDatapacksNeoForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(CompatDatapacks.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        CompatDatapacks.init();
    }
}