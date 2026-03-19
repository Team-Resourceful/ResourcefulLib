package com.teamresourceful.resourcefullib.common.recipe.ingredient;

import net.minecraft.world.item.crafting.Ingredient;

class IngredientHelperServiceFabricImpl implements IngredientHelperService {
    @Override
    public <T extends CodecIngredient<T>> Ingredient get(T ingredient) {
        return new FabricIngredient<>(ingredient).toVanilla();
    }

    @Override
    public <C extends CodecIngredient<C>, T extends CodecIngredientSerializer<C>> void register(T serializer) {
        FabricIngredientHelper.register(serializer);
    }
}
