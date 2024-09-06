package net.pitan76.compatdatapacks.mixin;

import com.google.gson.JsonElement;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import net.pitan76.compatdatapacks.JsonFixer;
import net.pitan76.compatdatapacks.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @ModifyVariable(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
            at = @At("STORE"), ordinal = 0)
    private Map.Entry<Identifier, JsonElement> compatdatapacks76$modifyEntry(Map.Entry<Identifier, JsonElement> entry) {
        if (!Config.isUseCompatRecipe()) return entry;

        JsonFixer.fixRecipe(entry.getValue());
        return entry;
    }

}
