package net.pitan76.compatdatapacks.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.pitan76.compatdatapacks.CompatDatapacks;

@Mod(CompatDatapacks.MOD_ID)
public class CompatDatapacksNeoForge {
    public CompatDatapacksNeoForge(IEventBus bus) {
        bus.addListener(this::onSetup);
    }

    public void onSetup(FMLCommonSetupEvent e) {
        CompatDatapacks.init();
    }
}