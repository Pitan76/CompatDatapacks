package net.pitan76.compatdatapacks.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.pitan76.compatdatapacks.JsonFixer;
import net.pitan76.compatdatapacks.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.Map;

@Mixin(JsonDataLoader.class)
public class JsonDataLoaderMixin {
    @Unique
    private static boolean compatdatapacks76$isLootTable = false;

    @Inject(method = "load", at = @At("HEAD"))
    private static void compatdatapacks76$load_head(ResourceManager manager, String dataType, Gson gson, Map<Identifier, JsonElement> results, CallbackInfo ci) {
        if (Config.isUseCompatLootTable()) {
            compatdatapacks76$isLootTable = dataType.equals("loot_tables") || dataType.equals("loot_table");
        }
    }

    @ModifyArg(method = "load", at = @At(value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", remap = false),
            index = 1)
    private static <T> T compatdatapacks76$modifyJsonElement(T obj) throws IOException {
        if (!(obj instanceof JsonElement)) return obj;
        JsonElement jsonElement = (JsonElement) obj;

        if (Config.isUseCompatLootTable() && compatdatapacks76$isLootTable) {
            JsonFixer.fixLootTable(jsonElement);
        }

        return obj;
    }
}
