package net.pitan76.compatdatapacks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resource.Resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 旧形式のタグから拾ってきたエントリを optional ("required": false) に書き換える。
 * <p>
 * 旧ディレクトリのタグは本来そのバージョンでは読まれないものなので、
 * 中に存在しないブロック/アイテムが書かれていることがある。
 * そのまま合流させると参照解決に失敗してタグ全体がロードされず、
 * 例えば minecraft:mineable/pickaxe が丸ごと消えてツルハシが機能しなくなる。
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
        try (InputStream input = resource.getInputStream()) {
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
            return new Resource(resource.getPack(), () -> new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            // 書き換えに失敗した場合は元のリソースをそのまま使わせる
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
