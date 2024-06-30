package net.pitan76.compatdatapacks;

import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class OldTags {

    public static final RegistryKey<Registry<Item>> ITEMS = of("items");

    public static void init() {
        // itemsタグ にあるものを itemタグ に登録
        for (TagKey<RegistryKey<Registry<Item>>> tagKey : RegistryEntry.of(ITEMS).streamTags().toList()) {
            Registry.register(ITEMS, tagKey.id(), tagKey.toString()));
        }
    }

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(Identifier.of(id));
    }
}
