package net.pitan76.compatdatapacks;

import java.util.HashMap;
import java.util.Map;

public class OldMineablePaths {

    public static Map<String, String> keys = new HashMap<>();

    public static void init() {

    }

    public static void add(String key, String newKey) {
        keys.put(key, newKey);
    }

    public static boolean contains(String key) {
        return keys.containsKey(key);
    }

    public static String get(String key) {
        return keys.get(key);
    }

    static {
        add("minecraft:tags/blocks/mineable/axe.json", "minecraft:tags/block/mineable/axe.json");
        add("minecraft:tags/blocks/mineable/pickaxe.json", "minecraft:tags/block/mineable/pickaxe.json");
        add("minecraft:tags/blocks/mineable/shovel.json", "minecraft:tags/block/mineable/shovel.json");
        add("minecraft:tags/blocks/mineable/hoe.json", "minecraft:tags/block/mineable/hoe.json");
        add("fabric:tags/blocks/mineable/sword.json", "fabric:tags/block/mineable/sword.json");
        add("fabric:tags/blocks/mineable/shears.json", "fabric:tags/block/mineable/shears.json");
    }
}
