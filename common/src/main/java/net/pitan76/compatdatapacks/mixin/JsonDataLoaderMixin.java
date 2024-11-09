package net.pitan76.compatdatapacks.mixin;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
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
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.resource.JsonDataLoader.load;

@Mixin(JsonDataLoader.class)
public class JsonDataLoaderMixin {

    @Unique
    private static boolean compatdatapacks76$loading = false;

    @Inject(method = "load", at = @At("TAIL"))
    private static <T> void compatdatapacks76$load(ResourceManager resourceManager, String dataType, DynamicOps<JsonElement> gson, Codec<T> codec, Map<Identifier, T> results, CallbackInfo ci) {

        if (!Config.isUseCompatDataType()) return;

        // 二重呼び出しを防ぐ
        if (compatdatapacks76$loading) return;

        if (!OldRegistryKeys.contains(dataType)) return;

        var oldKeys = OldRegistryKeys.get(dataType);
        if (oldKeys == null || oldKeys.isEmpty()) return;

        Map<Identifier, T> oldResults = new HashMap<>();

        compatdatapacks76$loading = true;
        for (var oldKey : oldKeys) {
            load(resourceManager, oldKey, gson, codec, oldResults);
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

    @ModifyArg(method = "load", at = @At(value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"),
    index = 1)
    private static <T> T compatdatapacks76$modifyParseReader(T obj) throws IOException {
        if (!(obj instanceof JsonElement)) return obj;
        JsonElement jsonElement = (JsonElement) obj;

        if (compatdatapacks76$loading)
            return null;

        if (Config.isUseCompatRecipe()) {
            JsonFixer.fixRecipe(jsonElement);
        }
        return obj;
    }
}
