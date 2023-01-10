package com.teamresourceful.resourcefullib.common.recipe.ingredient;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.crafting.Ingredient;

public final class IngredientHelper {

    private IngredientHelper() throws UtilityClassException {
        throw new UtilityClassException();
    }

    /**
     * This is used to convert your codec ingredient into the vanilla format used in recipes.
     * @param ingredient The ingredient to convert.
     * @return The converted ingredient.
     */
    @ExpectPlatform
    public static <T extends CodecIngredient<T>> Ingredient getIngredient(T ingredient) {
        throw new NotImplementedException();
    }

    /**
     * THis is used to register your custom ingredient serializer.
     * This should be called after everything on fabric and on forge in common setup
     */
    @ExpectPlatform
    public static <C extends CodecIngredient<C>, T extends CodecIngredientSerializer<C>> void registerIngredient(T serializer) {
        throw new NotImplementedException();
    }
}
