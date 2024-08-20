package net.pitan76.compatdatapacks;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OldTags {

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
        add("tags/item", "tags/items");
        add("tags/block", "tags/blocks");
        add("tags/entity_type", "tags/entity_types");
        add("tags/fluid", "tags/fluids");
        add("tags/game_event", "tags/game_events");
    }

    /**
     * Replace old keys with new keys
     * @param oldPath old path
     * @return new path
     */
    public static String replace(String oldPath) {
        for (var entry : keys.entrySet()) {
            for (var oldKey : entry.getValue()) {
                if (oldPath.contains(":" + oldKey)) {
                    oldPath = oldPath.replaceFirst(oldKey, entry.getKey());
                    break;
                }
            }
        }
        return oldPath;
    }

    public static Identifier replace(Identifier oldId) {
        return Identifier.of(replace(oldId.toString()));
    }
}
