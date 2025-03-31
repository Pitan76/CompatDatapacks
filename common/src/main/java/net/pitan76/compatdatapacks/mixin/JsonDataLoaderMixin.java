package net.pitan76.compatdatapacks.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.pitan76.compatdatapacks.CompatDatapacks;
import net.pitan76.compatdatapacks.JsonFixer;
import net.pitan76.compatdatapacks.OldRegistryKeys;
import net.pitan76.compatdatapacks.RewriteLogs;
import net.pitan76.compatdatapacks.config.Config;
import net.pitan76.compatdatapacks.config.IgnoreConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.resource.JsonDataLoader.load;

@Mixin(JsonDataLoader.class)
public class JsonDataLoaderMixin {

    @Unique
    private static boolean compatdatapacks76$loading = false;

    @Inject(method = "load", at = @At("TAIL"))
    private static void compatdatapacks76$load(ResourceManager resourceManager, String dataType, Gson gson, Map<Identifier, JsonElement> results, CallbackInfo ci) {
        if (!Config.isUseCompatDataType()) return;

        // 二重呼び出しを防ぐ
        if (compatdatapacks76$loading) return;

        if (!OldRegistryKeys.contains(dataType)) return;

        var oldKeys = OldRegistryKeys.get(dataType);
        if (oldKeys == null || oldKeys.isEmpty()) return;

        Map<Identifier, JsonElement> oldResults = new HashMap<>();

        compatdatapacks76$loading = true;
        for (var oldKey : oldKeys) {
            load(resourceManager, oldKey, gson, oldResults);
            ++RewriteLogs.loadingOldKeys;
        }
        compatdatapacks76$loading = false;

        for (var oldResult : oldResults.entrySet()) {
            if (IgnoreConfig.contains(oldResult.getKey().toString())) continue;
            if (results.containsKey(oldResult.getKey())) continue;

            results.put(oldResult.getKey(), oldResult.getValue());
        }

        CompatDatapacks.log("Loaded old registry keys " + String.join(", ", oldKeys) + " for " + dataType);
    }

    @ModifyVariable(method = "load", at = @At("STORE"), ordinal = 1)
    private static JsonElement compatdatapacks76$modifyJsonElement2(JsonElement jsonElement2) {
        if (compatdatapacks76$loading)
            return null;
        return jsonElement2;
    }

    @Unique
    private static boolean compatdatapacks76$isLootTable = false;

    @Inject(method = "load", at = @At("HEAD"))
    private static void compatdatapacks76$load_head(ResourceManager manager, String dataType, Gson gson, Map<Identifier, JsonElement> results, CallbackInfo ci) {
        if (Config.isUseCompatLootTable()) {
            compatdatapacks76$isLootTable = dataType.equals("loot_tables") || dataType.equals("loot_table");
        }
    }

    @ModifyVariable(method = "load", at = @At("STORE"), ordinal = 0)
    private static JsonElement compatdatapacks76$modifyParseReader(JsonElement jsonElement) {
        if (Config.isUseCompatLootTable() && compatdatapacks76$isLootTable) {
            JsonFixer.fixLootTable(jsonElement);
        }

        return jsonElement;
    }
}
