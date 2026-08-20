package net.pitan76.compatdatapacks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.server.packs.resources.Resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 旧形式のタグから拾ってきたエントリを optional ("required": false) に書き換える
 */
public class OptionalTagRewriter {

    public static List<Resource> makeOptional(List<Resource> resources) {
        List<Resource> result = new ArrayList<>(resources.size());
        for (Resource resource : resources) {
            Resource rewritten = makeOptional(resource);
            result.add(rewritten == null ? resource : rewritten);
        }

        return result;
    }

    public static Resource makeOptional(Resource resource) {
        try (InputStream input = resource.open()) {
            JsonElement parsed = JsonParser.parseReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            if (!parsed.isJsonObject()) return null;

            JsonObject json = parsed.getAsJsonObject();
            if (!json.has("values") || !json.get("values").isJsonArray()) return null;

            JsonArray values = json.getAsJsonArray("values");
            JsonArray newValues = new JsonArray();
            for (JsonElement value : values) {
                newValues.add(toOptional(value));
            }

            json.add("values", newValues);

            byte[] bytes = json.toString().getBytes(StandardCharsets.UTF_8);
            return new Resource(resource.source(), () -> new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            return null;
        }
    }

    private static JsonElement toOptional(JsonElement value) {
        if (value.isJsonPrimitive()) {
            JsonObject object = new JsonObject();
            object.addProperty("id", value.getAsString());
            object.addProperty("required", false);
            return object;
        }

        if (value.isJsonObject()) {
            JsonObject object = value.getAsJsonObject().deepCopy();
            object.addProperty("required", false);
            return object;
        }

        return value;
    }
}
