package net.pitan76.compatdatapacks;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Map;

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

        // old tag -> new tag
        if (obj.has("key")) {
            var key = obj.getAsJsonObject("key");
            var keySet = key.entrySet();
            Map.Entry<String, JsonElement>[] keyArray = keySet.toArray(Map.Entry[]::new);

            int size = keySet.size();
            for (int i = 0; i < size; ++i) {
                var entry = keyArray[i];
                if (!entry.getValue().isJsonObject()) continue;
                var value = entry.getValue().getAsJsonObject();

                if (value.has("tag") && value.get("tag").isJsonPrimitive()) {
                    isFixed = true;
                    var tag = value.getAsJsonPrimitive("tag");
                    if (tag.isString()) {
                        String tagStr = CommonTagConvert.convert(tag.getAsString());

                        if (!tagStr.equals(tag.getAsString())) {
                            value.remove("tag");
                            value.add("tag", new JsonPrimitive(tagStr));
                        }
                    }
                }
            }
        }

        // old tag -> new tag (ingredients)
        if (obj.has("ingredients") && obj.get("ingredients").isJsonArray()) {
            var ingredients = obj.getAsJsonArray("ingredients");
            JsonElement[] ingredientsArr = ingredients.asList().toArray(new JsonElement[0]);

            for (JsonElement ingredient : ingredientsArr) {
                if (!ingredient.isJsonObject()) continue;
                var ingredientObj = ingredient.getAsJsonObject();

                if (ingredientObj.has("tag") && ingredientObj.get("tag").isJsonPrimitive()) {
                    isFixed = true;
                    var tag = ingredientObj.getAsJsonPrimitive("tag");
                    if (tag.isString()) {
                        String tagStr = CommonTagConvert.convert(tag.getAsString());

                        if (!tagStr.equals(tag.getAsString())) {
                            ingredientObj.remove("tag");
                            ingredientObj.add("tag", new JsonPrimitive(tagStr));
                        }
                    }
                }
            }
        }

        if (!isFixed) return;
        ++RewriteLogs.fixingRecipe;
    }
}
