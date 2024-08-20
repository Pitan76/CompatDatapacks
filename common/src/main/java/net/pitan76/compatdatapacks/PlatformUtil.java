package net.pitan76.compatdatapacks;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public class PlatformUtil {
    @ExpectPlatform
    public static Path getConfigDir() {
        return null;
    }
}
