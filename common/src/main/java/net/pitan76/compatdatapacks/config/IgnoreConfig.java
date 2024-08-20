package net.pitan76.compatdatapacks.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.pitan76.compatdatapacks.PlatformUtil.getConfigDir;

public class IgnoreConfig {
    private static final List<String> ignores = new ArrayList<>();

    public static File configDir = new File(getConfigDir().toFile(), "compatdatapacks76");
    public static File file = new File(configDir, "ignore.txt");

    static {
        load();
    }

    public static void init() {
        // Load ignore.txt
    }

    public static boolean contains(String key) {
        if (ignores.isEmpty()) return false;
        if (ignores.contains(key)) return true;
        for (var ignore : ignores) {
            var namespace = "minecraft";
            var path = "";

            // example:example
            if (ignore.contains(":")) {
                var split = ignore.split(":");
                namespace = split[0];
                path = split[1];
            } else {
                path = ignore;
            }

            // example:*
            if (path.equals("*")) {
                if (key.startsWith(namespace + ":")) return true;
            }

            // example:example/*
            if (path.endsWith("*")) {
                if (key.startsWith(namespace + ":" + path.substring(0, path.length() - 1))) return true;
            }
        }

        return false;
    }

    public static void add(String key) {
        if (ignores.contains(key)) return;
        ignores.add(key);
    }

    public static void remove(String key) {
        ignores.remove(key);
    }

    // ----

    public static void load() {
        if (!configDir.exists()) {
            // Create config dir
            configDir.mkdirs();
        }

        if (!file.exists()) {
            // Create ignore.txt
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        // Load ignore.txt
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("//")) continue;
                add(line);
            }

            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
