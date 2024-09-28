package net.pitan76.compatdatapacks.mixin;

import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.pitan76.compatdatapacks.CompatDatapacks;
import net.pitan76.compatdatapacks.OldMineablePaths;
import net.pitan76.compatdatapacks.OldTags;
import net.pitan76.compatdatapacks.RewriteLogs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

        for (var oldTag : oldTags) {
            ResourceFinder resourceFinder = ResourceFinder.json(oldTag);
            for (Map.Entry<Identifier, List<Resource>> entry : resourceFinder.findAllResources(resourceManager).entrySet()) {

                if (compactdatapacks76$fixMineable(entry, entries)) continue;

                // add entry
                entries.add(entry);
            }

            ++RewriteLogs.loadingOldTags;
        }

        CompatDatapacks.log("Loaded old tags " + String.join(", ", oldTags) + " for " + dataType);
        return entries.iterator();
    }

    @Unique
    private static boolean compactdatapacks76$fixMineable(Map.Entry<Identifier, List<Resource>> oldEntry, List<Map.Entry<Identifier, List<Resource>>> entries) {
        var oldKey = oldEntry.getKey().toString();
        if (!OldMineablePaths.contains(oldKey)) return false;

        var newPath = OldMineablePaths.get(oldKey);
        if (newPath.isEmpty()) return false;

        for (Map.Entry<Identifier, List<Resource>> entry : entries) {
            if (entry.getKey().toString().equalsIgnoreCase(newPath)) {
                entry.getValue().addAll(oldEntry.getValue());
                break;
            }
        }

        return true;
    }
}
