package net.pitan76.compatdatapacks.mixin;

import com.google.gson.JsonElement;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import net.pitan76.compatdatapacks.RewriteLogs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    // result.item -> result.id
    @ModifyVariable(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
            at = @At("STORE"), ordinal = 0)
    private Map.Entry<Identifier, JsonElement> compatdatapacks76$modifyEntry(Map.Entry<Identifier, JsonElement> entry) {
        boolean isFixed = false;

        var json = entry.getValue();
        if (json.isJsonObject()) {
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
        }

        if (isFixed)
            ++RewriteLogs.fixingRecipe;

        return entry;
    }

}
