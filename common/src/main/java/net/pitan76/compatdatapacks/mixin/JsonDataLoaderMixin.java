package net.pitan76.compatdatapacks.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.pitan76.compatdatapacks.CompatDatapacks;
import net.pitan76.compatdatapacks.OldRegistryKeys;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

import static net.minecraft.resource.JsonDataLoader.load;

@Mixin(JsonDataLoader.class)
public class JsonDataLoaderMixin {

    /*
    @Shadow @Final private Gson gson;

    @Shadow @Final private String dataType;

    @Inject(method = "prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Ljava/util/Map;", at = @At("TAIL"))
    private void compatdatapacks$prepare(ResourceManager resourceManager, Profiler profiler, CallbackInfoReturnable<Map<Identifier, JsonElement>> cir, @Local Map<Identifier, JsonElement> map) {
        if (!OldRegistryKeys.contains(dataType)) return;

        var oldKeys = OldRegistryKeys.get(dataType);
        if (oldKeys == null || oldKeys.isEmpty()) return;

        for (var oldKey : oldKeys) {
            load(resourceManager, oldKey, gson, map);
        }

        CompatDatapacks.log("Loaded old registry keys " + String.join(", ", oldKeys) + " for " + dataType);
    }

     */

    @Unique
    private static boolean compatdatapacks76$loading = false;

    @Inject(method = "load", at = @At("TAIL"))
    private static void compatdatapacks76$load(ResourceManager resourceManager, String dataType, Gson gson, Map<Identifier, JsonElement> results, CallbackInfo ci) {
        // 二重呼び出しを防ぐ
        if (compatdatapacks76$loading) return;

        if (!OldRegistryKeys.contains(dataType)) return;

        var oldKeys = OldRegistryKeys.get(dataType);
        if (oldKeys == null || oldKeys.isEmpty()) return;

        compatdatapacks76$loading = true;
        for (var oldKey : oldKeys) {
            load(resourceManager, oldKey, gson, results);
        }
        compatdatapacks76$loading = false;

        CompatDatapacks.log("Loaded old registry keys " + String.join(", ", oldKeys) + " for " + dataType);
    }
}
