package com.teamresourceful.resourcefullib.common.recipe.ingredient;

import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Stream;

public record FabricIngredient<T extends CodecIngredient<T>>(T ingredient) implements CustomIngredient {

    @Override
    public boolean test(ItemStack stack) {
        return ingredient.test(stack);
    }

    @Override
    public Stream<Holder<Item>> items() {
        return this.ingredient.getStream();
    }

    @Override
    public boolean requiresTesting() {
        return ingredient.isComplex();
    }

    @Override
    public CustomIngredientSerializer<?> getSerializer() {
        return FabricIngredientHelper.get(ingredient.serializer().id());
    }
}
