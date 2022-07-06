package com.teamresourceful.resourcefullib.common.recipe;

import net.minecraft.world.item.crafting.RecipeType;

public class CodecRecipeType<T extends CodecRecipe<?>> implements RecipeType<T> {

    private final String id;

    private CodecRecipeType(String id) {
        this.id = id;
    }

    public static <T extends CodecRecipe<?>> CodecRecipeType<T> of(String id) {
        return new CodecRecipeType<>(id);
    }

    @Override
    public String toString() {
        return "[" + id + "]";
    }
}
