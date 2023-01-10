package com.teamresourceful.resourcefullib.common.recipe.ingredient.fabric;

import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FabricIngredient<T extends CodecIngredient<T>> implements CustomIngredient {

    private final T ingredient;
    private List<ItemStack> stacks;

    public FabricIngredient(T ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public boolean test(ItemStack stack) {
        return ingredient.test(stack);
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        if (stacks == null) {
            stacks = ingredient.getStacks();
        }
        return stacks;
    }

    @Override
    public boolean requiresTesting() {
        return ingredient.isComplex();
    }

    @Override
    public CustomIngredientSerializer<?> getSerializer() {
        return FabricIngredientHelper.get(ingredient.serializer().id());
    }

    public T getIngredient() {
        return ingredient;
    }
}
