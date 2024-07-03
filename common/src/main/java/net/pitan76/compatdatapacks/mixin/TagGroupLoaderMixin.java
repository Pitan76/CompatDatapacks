package net.pitan76.compatdatapacks.mixin;

import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.pitan76.compatdatapacks.CompatDatapacks;
import net.pitan76.compatdatapacks.OldTags;
import net.pitan76.compatdatapacks.RewriteLogs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(TagGroupLoader.class)
public class TagGroupLoaderMixin {
    @Shadow @Final private String dataType;

    @ModifyVariable(method = "loadTags", at = @At("STORE"), ordinal = 0)
    private Iterator compatdatapacks76$modifyVar4(Iterator var4, ResourceManager resourceManager) {
        if (!OldTags.contains(dataType)) return var4;

        var oldTags = OldTags.get(dataType);
        if (oldTags == null || oldTags.isEmpty()) return var4;

        List<Map.Entry<Identifier, List<Resource>>> entries = new ArrayList<>();
        while(var4.hasNext()) {
            Map.Entry<Identifier, List<Resource>> entry = (Map.Entry)var4.next();
            entries.add(entry);
        }

        Map<Identifier, List<Resource>> map = entries.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b));

        for (var oldTag : oldTags) {
            ResourceFinder resourceFinder = ResourceFinder.json(oldTag);
            map.putAll(resourceFinder.findAllResources(resourceManager));

            ++RewriteLogs.loadingOldTags;
        }

        CompatDatapacks.log("Loaded old tags " + String.join(", ", oldTags) + " for " + dataType);
        return entries.iterator();
    }
}
