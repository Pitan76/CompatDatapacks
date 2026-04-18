package net.pitan76.compatdatapacks.mixin;

import net.minecraft.tags.TagLoader;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.Identifier;
import net.pitan76.compatdatapacks.CompatDatapacks;
import net.pitan76.compatdatapacks.OldTags;
import net.pitan76.compatdatapacks.RewriteLogs;
import net.pitan76.compatdatapacks.config.Config;
import net.pitan76.compatdatapacks.config.IgnoreConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(TagLoader.class)
public class TagLoaderMixin {
    @Shadow @Final private String directory;

    @ModifyVariable(method = "load", at = @At("STORE"), ordinal = 0)
    private Iterator compatdatapacks76$modifyVar4(Iterator var4, ResourceManager resourceManager) {
        if (!Config.isUseCompatTagGroup()) return var4;

        if (!OldTags.contains(directory)) return var4;

        var oldTags = OldTags.get(directory);
        if (oldTags == null || oldTags.isEmpty()) return var4;

        List<Map.Entry<Identifier, List<Resource>>> entries = new ArrayList<>();
        while (var4.hasNext()) {
            Map.Entry<Identifier, List<Resource>> entry = (Map.Entry)var4.next();
            entries.add(entry);
        }

        for (var oldTag : oldTags) {
            FileToIdConverter resourceFinder = FileToIdConverter.json(oldTag);
            for (var entry : resourceFinder.listMatchingResourceStacks(resourceManager).entrySet()) {
                var replaced = OldTags.replace(entry.getKey());
                if (IgnoreConfig.contains(replaced.toString())) continue;

                entries.add(Map.entry(replaced, entry.getValue()));
            }

            ++RewriteLogs.loadingOldTags;
        }

        CompatDatapacks.log("Loaded old tags " + String.join(", ", oldTags) + " for " + directory);
        return entries.iterator();
    }
}