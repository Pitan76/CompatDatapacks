package net.pitan76.compatdatapacks;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class PlatformUtil {
    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
