package net.pitan76.compatdatapacks.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.Identifier;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener.scanDirectory;

@Mixin(SimpleJsonResourceReloadListener.class)
public abstract class SimpleJsonResourceReloadListenerMixin {

    @Unique
    private static boolean compatdatapacks76$loading = false;

    @Inject(method = "scanDirectory(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/FileToIdConverter;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/util/Map;)V", at = @At("TAIL"))
    private static <T> void compatdatapacks76$load(ResourceManager manager, FileToIdConverter finder, DynamicOps<JsonElement> ops, Codec<T> codec, Map<Identifier, T> results, CallbackInfo ci) {

        if (!Config.isUseCompatDataType()) return;

        // 二重呼び出しを防ぐ
        if (compatdatapacks76$loading) return;

        var dataType = "";
        Iterator<Map.Entry<Identifier, Resource>> var5 = finder.listMatchingResources(manager).entrySet().iterator();
        // 1つだけ取得してprint
        if (var5.hasNext()) {
            Map.Entry<Identifier, Resource> entry = var5.next();
            Identifier key = entry.getKey();
            dataType = key.getPath().split("/")[0];
        }

        if (!OldRegistryKeys.contains(dataType)) return;

        var oldKeys = OldRegistryKeys.get(dataType);
        if (oldKeys == null || oldKeys.isEmpty()) return;

        Map<Identifier, T> oldResults = new HashMap<>();

        compatdatapacks76$loading = true;
        for (var oldKey : oldKeys) {
            scanDirectory(manager, FileToIdConverter.json(oldKey), ops, codec, oldResults);
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

    @Unique
    private static boolean compatdatapacks76$isRecipe = false;

    @Unique
    private static boolean compatdatapacks76$isLootTable = false;

    @Inject(method = "scanDirectory(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/FileToIdConverter;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/util/Map;)V", at = @At("HEAD"))
    private static <T> void compatdatapacks76$load_head(ResourceManager manager, FileToIdConverter finder, DynamicOps<JsonElement> ops, Codec<T> codec, Map<Identifier, T> results, CallbackInfo ci) {
        if (Config.isUseCompatRecipe() || Config.isUseCompatLootTable()) {
            var dataType = "";
            Iterator<Map.Entry<Identifier, Resource>> var5 = finder.listMatchingResources(manager).entrySet().iterator();
            // 1つだけ取得してprint
            if (var5.hasNext()) {
                Map.Entry<Identifier, Resource> entry = var5.next();
                Identifier key = entry.getKey();
                dataType = key.getPath().split("/")[0];
            }

            if (Config.isUseCompatRecipe())
                compatdatapacks76$isRecipe = dataType.equals("recipes") || dataType.equals("recipe");

            if (Config.isUseCompatLootTable())
                compatdatapacks76$isLootTable = dataType.equals("loot_tables") || dataType.equals("loot_table");
        }
    }

    @ModifyArg(method = "scanDirectory(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/FileToIdConverter;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/util/Map;)V", at = @At(value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;", remap = false),
            index = 1)
    private static <T> T compatdatapacks76$modifyParseReader(T obj) throws IOException {
        if (!(obj instanceof JsonElement)) return obj;
        JsonElement jsonElement = (JsonElement) obj;

        if (Config.isUseCompatRecipe() && compatdatapacks76$isRecipe) {
            JsonFixer.fixRecipe(jsonElement);
        }
        if (Config.isUseCompatLootTable() && compatdatapacks76$isLootTable) {
            JsonFixer.fixLootTable(jsonElement);
        }

        return obj;
    }
}
