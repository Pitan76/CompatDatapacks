package net.pitan76.compatdatapacks.neoforge;

import net.neoforged.fml.loading.FMLPaths;
import java.nio.file.Path;

public class PlatformUtilImpl {
    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
