package com.teamresourceful.resourcefullib.common.recipe.ingredient;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.lib.PlatformService;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@PlatformService
interface IngredientHelperService {

    <T extends CodecIngredient<T>> Ingredient get(T ingredient);

    <C extends CodecIngredient<C>, T extends CodecIngredientSerializer<C>> void register(T serializer);

    static IngredientHelperService create() {
        throw new NotImplementedException();
    }
}
