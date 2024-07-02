package net.pitan76.compatdatapacks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OldRegistryKeys {

    public static Map<String, List<String>> keys = new HashMap<>();

    public static void init() {

    }

    public static void add(String key, List<String> oldKeys) {
        keys.put(key, oldKeys);
    }

    public static void add(String key, String... oldKeys) {
        keys.put(key, List.of(oldKeys));
    }

    public static boolean contains(String key) {
        return keys.containsKey(key);
    }

    public static List<String> get(String key) {
        return keys.get(key);
    }

    static {
        //
    }
}
