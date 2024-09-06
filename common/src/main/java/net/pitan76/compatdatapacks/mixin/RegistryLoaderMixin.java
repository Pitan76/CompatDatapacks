package net.pitan76.compatdatapacks.mixin;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Decoder;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.pitan76.compatdatapacks.DatapackFixer;
import net.pitan76.compatdatapacks.RewriteLogs;
import net.pitan76.compatdatapacks.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;

@Mixin(RegistryLoader.class)
public abstract class RegistryLoaderMixin {

    /*
    @ModifyVariable(method = "parseAndAdd", at = @At(value = "STORE", ordinal = 0, remap = false), ordinal = 0)
    private static <E> JsonElement compatdatapacks76$modifyJsonElement(JsonElement jsonElement, @Local(argsOnly = true) RegistryKey<E> key) throws IOException {
        if (Config.isUseCompatDimensionType() && key.equals(RegistryKeys.DIMENSION_TYPE))
            compatdatapacks76$fixDimensionType(jsonElement);

        return jsonElement;
    }

     */


    @Inject(method="parseAndAdd(Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Lnet/minecraft/registry/RegistryOps;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/resource/Resource;Lnet/minecraft/registry/entry/RegistryEntryInfo;)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/gson/JsonParser;parseReader(Ljava/io/Reader;)Lcom/google/gson/JsonElement;", remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
    private static <E> void compatdatapacks76$parseAndAdd(MutableRegistry<E> registry, com.mojang.serialization.Decoder<E> decoder, RegistryOps<com.google.gson.JsonElement> ops, RegistryKey<E> key, Resource resource, RegistryEntryInfo entryInfo, CallbackInfo cir, @Local JsonElement jsonElement) throws IOException {
        String id = registry.getKey().getValue().toString();
        if (Config.isUseCompatDimensionType() && id.equals("minecraft:dimension_type"))
            DatapackFixer.fixDimensionType(jsonElement);
    }
}
