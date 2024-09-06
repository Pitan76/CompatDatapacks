package net.pitan76.compatdatapacks.neoforge;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.pitan76.compatdatapacks.CompatDatapacks;

@Mod(CompatDatapacks.MOD_ID)
public class CompatDatapacksNeoForge {
    public CompatDatapacksNeoForge(ModContainer modContainer) {
        CompatDatapacks.init();
    }
}