package net.pitan76.compatdatapacks;

import com.google.gson.JsonElement;

public class JsonFixer {
    public static void fixLootTable(JsonElement json) {
        boolean isFixed = false;

        if (!json.isJsonObject()) return;
        var obj = json.getAsJsonObject();

        // pools.[*].entries.[*].functions.[*].function: "minecraft:set_nbt" -> "minecraft:set_custom_data"
        // pools.[*].entries.[*].functions.[*].function: "minecraft:copy_nbt" -> "minecraft:copy_custom_data"
        if (obj.has("pools") && obj.get("pools").isJsonArray()) {
            var pools = obj.getAsJsonArray("pools");
            for (var pool : pools) {
                if (!pool.isJsonObject()) continue;
                var poolObj = pool.getAsJsonObject();
                if (poolObj.has("entries") && poolObj.get("entries").isJsonArray()) {
                    var entries = poolObj.getAsJsonArray("entries");
                    for (var entry : entries) {
                        if (!entry.isJsonObject()) continue;
                        var entryObj = entry.getAsJsonObject();
                        if (entryObj.has("functions") && entryObj.get("functions").isJsonArray()) {
                            var functions = entryObj.getAsJsonArray("functions");
                            for (var function : functions) {
                                if (!function.isJsonObject()) continue;
                                var functionObj = function.getAsJsonObject();
                                if (functionObj.has("function") && functionObj.get("function").isJsonPrimitive()) {
                                    var functionStr = functionObj.getAsJsonPrimitive("function").getAsString();
                                    if (functionStr.equals("minecraft:set_nbt") || functionStr.equals("set_nbt")) {
                                        isFixed = true;
                                        functionObj.remove("function");
                                        functionObj.addProperty("function", "minecraft:set_custom_data");
                                        continue;
                                    }
                                    if (functionStr.equals("minecraft:copy_nbt") || functionStr.equals("copy_nbt")) {
                                        isFixed = true;
                                        functionObj.remove("function");
                                        functionObj.addProperty("function", "minecraft:copy_custom_data");
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!isFixed) return;
        ++RewriteLogs.fixingLootTable;
    }
}
