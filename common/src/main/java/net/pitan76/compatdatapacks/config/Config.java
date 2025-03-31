package net.pitan76.compatdatapacks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

import static net.pitan76.compatdatapacks.CompatDatapacks.configDir;

public class Config {
    private static final File file = new File(configDir, "compatdatapacks76.json");

    private static Map<String, Object> map = new HashMap<>();

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void init() {
        if (!file.exists()) {
            // Default values
            map.put("enabled", true);
            map.put("useCompatRecipe", true);
            map.put("useCompatLootTable", true);

            save();
        }
        load();
    }

    public static boolean isEnabled() {
        return getBoolean("enabled");
    }

    public static boolean isUseCompatRecipe() {
        if (!isEnabled()) return false;
        return getBoolean("useCompatRecipe");
    }

    public static boolean isUseCompatLootTable() {
        if (!isEnabled()) return false;
        return getBoolean("useCompatLootTable");
    }

    public static void load() {
        // Load compatdatapacks76.json
        if (!file.exists()) return;

        try (var reader = new FileReader(file)) {
            map = gson.fromJson(reader, map.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        // Save compatdatapacks76.json
        try {
            String json = gson.toJson(map);
            if (configDir.exists() || configDir.mkdirs())
                file.createNewFile();

            try (var writer = new FileWriter(file)) {
                writer.write(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void put(String key, Object value) {
        map.put(key, value);
    }

    public static Object get(String key) {
        return map.get(key);
    }

    public static boolean contains(String key) {
        return map.containsKey(key);
    }

    public static void remove(String key) {
        map.remove(key);
    }

    public static boolean getBoolean(String key) {
        if (!map.containsKey(key)) return true;
        return (boolean) map.get(key);
    }

    public static int getInt(String key) {
        return (int) map.get(key);
    }

    public static double getDouble(String key) {
        return (double) map.get(key);
    }

    public static String getString(String key) {
        return (String) map.get(key);
    }

    public static List<String> getStringList(String key) {
        return (List<String>) map.get(key);
    }
}
