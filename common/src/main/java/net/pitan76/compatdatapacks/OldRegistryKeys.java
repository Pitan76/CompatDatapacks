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
        add("structure", "structures");
        add("advancement", "advancements");
        add("recipe", "recipes");
        add("loot_table", "loot_tables");
        add("predicate", "predicates");
        add("item_modifier", "item_modifiers");
        add("function", "functions");
        add("tags/item", "tags/items");
        add("tags/block", "tags/blocks");
        add("tags/entity_type", "tags/entity_types");
        add("tags/fluid", "tags/fluids");
        add("tags/game_event", "tags/game_events");
    }
}
