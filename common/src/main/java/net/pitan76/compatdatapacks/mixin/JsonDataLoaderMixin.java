package net.pitan76.compatdatapacks.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.pitan76.compatdatapacks.CompatDatapacks;
import net.pitan76.compatdatapacks.OldRegistryKeys;
import net.pitan76.compatdatapacks.RewriteLogs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static net.minecraft.resource.JsonDataLoader.load;

@Mixin(JsonDataLoader.class)
public class JsonDataLoaderMixin {

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
            ++RewriteLogs.loadingOldKeys;
        }
        compatdatapacks76$loading = false;

        CompatDatapacks.log("Loaded old registry keys " + String.join(", ", oldKeys) + " for " + dataType);
    }
}
