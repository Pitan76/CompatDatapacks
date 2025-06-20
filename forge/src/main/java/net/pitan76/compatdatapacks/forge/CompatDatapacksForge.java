package net.pitan76.compatdatapacks.forge;

import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.pitan76.compatdatapacks.CompatDatapacks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CompatDatapacks.MOD_ID)
public class CompatDatapacksForge {
    public CompatDatapacksForge(FMLJavaModLoadingContext context) {
        BusGroup busGroup = context.getModBusGroup();
        FMLCommonSetupEvent.getBus(busGroup).addListener(this::onSetup);
    }

    public void onSetup(FMLCommonSetupEvent e) {
        CompatDatapacks.init();
    }
}