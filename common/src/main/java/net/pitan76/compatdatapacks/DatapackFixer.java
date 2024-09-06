package net.pitan76.compatdatapacks;

import com.google.gson.JsonElement;

public class DatapackFixer {

    public static void fixDimensionType(JsonElement json) {
        if (json.isJsonObject()) {
            var obj = json.getAsJsonObject();
            if (obj.has("monster_spawn_light_level") && obj.get("monster_spawn_light_level").isJsonObject()) {
                var monster_spawn_light_level = obj.getAsJsonObject("monster_spawn_light_level");
                if (monster_spawn_light_level.has("value") && monster_spawn_light_level.get("value").isJsonObject()) {
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
        }

        ++RewriteLogs.fixingDimensionType;
    }
}
