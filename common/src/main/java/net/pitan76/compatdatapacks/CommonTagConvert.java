package net.pitan76.compatdatapacks;

import java.util.HashMap;
import java.util.Map;

public class CommonTagConvert {

    public static Map<String, String> TAG_CONVERT_MAP = new HashMap<>();

    public static void init() {
    }

    public static void add(String oldTag, String newTag) {
        TAG_CONVERT_MAP.put(oldTag, newTag);
    }

    public static String convert(String oldTag) {
        if (!TAG_CONVERT_MAP.containsKey(oldTag))
            return oldTag;

        return TAG_CONVERT_MAP.getOrDefault(oldTag, oldTag);
    }

    static {
        add("c:iron_ingots", "c:ingots/iron");
        add("c:gold_ingots", "c:ingots/gold");
        add("c:copper_ingots", "c:ingots/copper");
        add("c:refined_iron_ingots", "c:ingots/refined_iron");
        add("c:tin_ingots", "c:ingots/tin");
        add("c:silver_ingots", "c:ingots/silver");
        add("c:bronze_ingots", "c:ingots/bronze");
        add("c:lead_ingots", "c:ingots/lead");
        add("c:steel_ingots", "c:ingots/steel");

        add("c:iron_nuggets", "c:nuggets/iron");
        add("c:gold_nuggets", "c:nuggets/gold");
        add("c:copper_nuggets", "c:nuggets/copper");
        add("c:refined_iron_nuggets", "c:nuggets/refined_iron");
        add("c:tin_nuggets", "c:nuggets/tin");
        add("c:silver_nuggets", "c:nuggets/silver");
        add("c:bronze_nuggets", "c:nuggets/bronze");
        add("c:lead_nuggets", "c:nuggets/lead");
        add("c:steel_nuggets", "c:nuggets/steel");

        add("c:iron_ores", "c:ores/iron");
        add("c:gold_ores", "c:ores/gold");
        add("c:copper_ores", "c:ores/copper");
        add("c:tin_ores", "c:ores/tin");
        add("c:silver_ores", "c:ores/silver");
        add("c:lead_ores", "c:ores/lead");
        add("c:lapis_ores", "c:ores/lapis");
        add("c:redstone_ores", "c:ores/redstone");
        add("c:coal_ores", "c:ores/coal");
        add("c:diamond_ores", "c:ores/diamond");
        add("c:emerald_ores", "c:ores/emerald");

        add("c:iron_dusts", "c:dusts/iron");
        add("c:gold_dusts", "c:dusts/gold");
        add("c:copper_dusts", "c:dusts/copper");
        add("c:tin_dusts", "c:dusts/tin");
        add("c:silver_dusts", "c:dusts/silver");
        add("c:lead_dusts", "c:dusts/lead");
        add("c:bronze_dusts", "c:dusts/bronze");
        add("c:steel_dusts", "c:dusts/steel");
        add("c:charcoal_dusts", "c:dusts/charcoal");
        add("c:coal_dusts", "c:dusts/coal");
        add("c:diamond_dusts", "c:dusts/diamond");
        add("c:emerald_dusts", "c:dusts/emerald");
        add("c:lapis_dusts", "c:dusts/lapis");
        add("c:redstone_dusts", "c:dusts/redstone");
    }


}
