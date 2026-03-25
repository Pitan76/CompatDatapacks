package net.pitan76.compatdatapacks.mixin;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Decoder;
import net.minecraft.resources.RegistryLoadTask;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import net.pitan76.compatdatapacks.JsonFixer;
import net.pitan76.compatdatapacks.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.Reader;

@Mixin(RegistryLoadTask.PendingRegistration.class)
public abstract class RegistryDataLoaderMixin {

    @Unique
    private static final ThreadLocal<ResourceKey<?>> compatdatapacks76$KEY = new ThreadLocal<>();

    @Inject(method = "loadFromResource", at = @At("HEAD"))
    private static <T> void compatdatapacks76$captureKey(Decoder<T> decoder, RegistryOps<JsonElement> ops, ResourceKey<T> key, Resource resource, CallbackInfoReturnable<?> cir) {
        compatdatapacks76$KEY.set(key);
    }

    @WrapOperation(method = "loadFromResource", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/StrictJsonParser;parse(Ljava/io/Reader;)Lcom/google/gson/JsonElement;"))
    private static JsonElement compatdatapacks76$wrapParse(Reader reader, Operation<JsonElement> original) {
        JsonElement json = original.call(reader);

        ResourceKey<?> key = compatdatapacks76$KEY.get();

        if (key != null && "dimension_type".equals(key.identifier().getPath()) && Config.isUseCompatDimensionType()) {
            JsonFixer.fixDimensionType(json);
        }

        return json;
    }
}
