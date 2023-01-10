package com.teamresourceful.resourcefullib.common.recipe.ingredient.fabric;

import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredient;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredientSerializer;
import net.minecraft.world.item.crafting.Ingredient;

public class IngredientHelperImpl {
    public static <T extends CodecIngredient<T>> Ingredient getIngredient(T ingredient) {
        return new FabricIngredient<>(ingredient).toVanilla();
    }

    public static <C extends CodecIngredient<C>, T extends CodecIngredientSerializer<C>> void registerIngredient(T serializer) {
        FabricIngredientHelper.register(serializer);
    }
}
