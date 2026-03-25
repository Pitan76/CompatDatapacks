package net.pitan76.compatdatapacks.mixin;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Decoder;
import net.minecraft.core.WritableRegistry;
import net.minecraft.registry.*;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import net.pitan76.compatdatapacks.JsonFixer;
import net.pitan76.compatdatapacks.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;

@Mixin(RegistryDataLoader.class)
public abstract class RegistryDataLoaderMixin {
    @Inject(method= "loadElementFromResource", at = @At(value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/util/StrictJsonParser;parse(Ljava/io/Reader;)Lcom/google/gson/JsonElement;"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private static <E> void compatdatapacks76$parseAndAdd(WritableRegistry<E> registry, Decoder<E> decoder, RegistryOps<JsonElement> ops, ResourceKey<E> key, Resource resource, RegistrationInfo entryInfo, CallbackInfo cir, @Local JsonElement jsonElement) throws IOException {
        String id = registry.key().identifier().getPath();
        if (Config.isUseCompatDimensionType() && id.equals("dimension_type"))
            JsonFixer.fixDimensionType(jsonElement);
    }
}
