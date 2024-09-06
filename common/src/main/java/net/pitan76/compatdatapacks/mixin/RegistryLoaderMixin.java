package net.pitan76.compatdatapacks.mixin;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.registry.*;
import net.pitan76.compatdatapacks.RewriteLogs;
import net.pitan76.compatdatapacks.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {

    @ModifyVariable(method = "parseAndAdd",
            at = @At("STORE"), ordinal = 0)
    private static <E> JsonElement compatdatapacks76$modifyEntry(JsonElement json, @Local(argsOnly = true) LocalRef<RegistryKey<E>> key) {
        if (Config.isUseCompatDimensionType() && key.get().equals(RegistryKeys.DIMENSION_TYPE))
            return compatdatapacks76$fixDimensionType(json);

        return json;
    }

    @Unique
    private static JsonElement compatdatapacks76$fixDimensionType(JsonElement json) {
        if (json.isJsonObject()) {
            var obj = json.getAsJsonObject();
            if (obj.has("monster_spawn_light_level") && obj.get("monster_spawn_light_level").isJsonObject()) {
                var monster_spawn_light_level = obj.getAsJsonObject("monster_spawn_light_level");
                if (monster_spawn_light_level.has("value") && monster_spawn_light_level.get("value").isJsonObject()) {
                    var value = monster_spawn_light_level.getAsJsonObject("value");
                    for (var entry : value.entrySet()) {
                        monster_spawn_light_level.add(entry.getKey(), entry.getValue());
                    }
                    monster_spawn_light_level.remove("value");
                }
            }
        }

        ++RewriteLogs.fixingDimensionType;

        return json;
    }
}
