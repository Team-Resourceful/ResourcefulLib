package com.teamresourceful.resourcefullib.common.recipe.ingredient.forge;

import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredient;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredientSerializer;
import net.minecraft.world.item.crafting.Ingredient;

public class IngredientHelperImpl {

    public static <T extends CodecIngredient<T>> Ingredient getIngredient(T ingredient) {
        return new ForgeIngredient<>(ingredient);
    }

    public static <C extends CodecIngredient<C>, T extends CodecIngredientSerializer<C>> void registerIngredient(T serializer) {
        ForgeIngredientHelper.register(serializer);
    }
}
