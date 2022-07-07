package net.jacg.layered_sand_core.util;

import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Tags {
    public static final TagKey<Item> SAND_SHOVELS = TagKey.of(Registry.ITEM_KEY, new Identifier("layered_sand_core", "sand_shovels"));
}
