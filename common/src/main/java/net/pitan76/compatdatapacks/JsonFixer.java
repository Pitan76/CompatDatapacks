package net.pitan76.compatdatapacks;

import com.google.gson.JsonElement;

import java.util.Map;
import java.util.Set;

public class JsonFixer {

    public static void fixDimensionType(JsonElement json) {
        boolean isFixed = false;

        if (!json.isJsonObject()) return;
        var obj = json.getAsJsonObject();

        if (obj.has("monster_spawn_light_level") && obj.get("monster_spawn_light_level").isJsonObject()) {
            var monster_spawn_light_level = obj.getAsJsonObject("monster_spawn_light_level");
            if (monster_spawn_light_level.has("value") && monster_spawn_light_level.get("value").isJsonObject()) {
                isFixed = true;

                var value = monster_spawn_light_level.getAsJsonObject("value");
                if (value.has("max_inclusive") && value.get("max_inclusive").isJsonPrimitive()) {
                    var max_inclusive = value.getAsJsonPrimitive("max_inclusive");
                    if (max_inclusive.isNumber()) {
                        monster_spawn_light_level.addProperty("max_inclusive", max_inclusive.getAsInt());
                        value.remove("max_inclusive");
                    }
                }
                if (value.has("min_inclusive") && value.get("min_inclusive").isJsonPrimitive()) {
                    var min_inclusive = value.getAsJsonPrimitive("min_inclusive");
                    if (min_inclusive.isNumber()) {
                        monster_spawn_light_level.addProperty("min_inclusive", min_inclusive.getAsInt());
                        value.remove("min_inclusive");
                    }
                }
            }
        }

        if (!isFixed) return;
        ++RewriteLogs.fixingDimensionType;
    }

    public static void fixRecipe(JsonElement json) {
        boolean isFixed = false;

        if (!json.isJsonObject()) return;
        var obj = json.getAsJsonObject();

        if (obj.has("result") && obj.get("result").isJsonObject()) {
            var result = obj.getAsJsonObject("result");
            if (result.has("item") && result.get("item").isJsonPrimitive()) {
                isFixed = true;
                var item = result.getAsJsonPrimitive("item");
                if (item.isString()) {
                    var itemId = item.getAsString();
                    result.addProperty("id", itemId);
                    result.remove("item");
                }
            }
        }

        if (obj.has("key")) {
            var key = obj.getAsJsonObject("key");
            var keySet = key.entrySet();
            Map.Entry<String, JsonElement>[] keyArray = keySet.toArray(Map.Entry[]::new);

            int size = keySet.size();

            for (int i = 0; i < size; ++i) {
                var entry = keyArray[i];
                if (!entry.getValue().isJsonObject()) continue;
                var value = entry.getValue().getAsJsonObject();
                if (value.has("item") && value.get("item").isJsonPrimitive()) {
                    isFixed = true;
                    var item = value.getAsJsonPrimitive("item");
                    if (item.isString()) {
                        key.remove(entry.getKey());
                        key.add(entry.getKey(), item);
                    }
                }
            }
        }

        if (obj.has("results") && obj.get("results").isJsonArray()) {
            var results = obj.getAsJsonArray("results");
            for (var result : results) {
                if (!result.isJsonObject()) continue;
                var resultObj = result.getAsJsonObject();
                if (resultObj.has("item") && resultObj.get("item").isJsonPrimitive()) {
                    isFixed = true;
                    var item = resultObj.getAsJsonPrimitive("item");
                    if (item.isString()) {
                        var itemId = item.getAsString();
                        resultObj.addProperty("id", itemId);
                        resultObj.remove("item");
                    }
                }
            }
        }

        if (!isFixed) return;
        ++RewriteLogs.fixingRecipe;
    }
}
