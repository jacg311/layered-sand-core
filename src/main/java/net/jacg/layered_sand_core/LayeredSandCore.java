package net.jacg.layered_sand_core;

import net.fabricmc.api.ModInitializer;
import net.jacg.layered_sand_core.item.SandPile;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class LayeredSandCore implements ModInitializer {
    public static final String MOD_ID = "layered_sand_core";
    public static final Item SAND_PILE = Registry.register(Registry.ITEM, id("sand_pile"), new SandPile(new Item.Settings(), new Identifier("sand")));
    public static final Item RED_SAND_PILE = Registry.register(Registry.ITEM, id("red_sand_pile"), new SandPile(new Item.Settings(), new Identifier("red_sand")));

    @Override
    public void onInitialize() {
    }

    public static Identifier id(String name) {
        return new Identifier(MOD_ID, name);
    }
}
