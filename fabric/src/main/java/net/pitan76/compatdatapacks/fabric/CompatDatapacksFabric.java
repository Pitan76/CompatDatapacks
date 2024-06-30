package net.pitan76.compatdatapacks.fabric;

import net.pitan76.compatdatapacks.CompatDatapacks;
import net.fabricmc.api.ModInitializer;

public class CompatDatapacksFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CompatDatapacks.init();
    }
}