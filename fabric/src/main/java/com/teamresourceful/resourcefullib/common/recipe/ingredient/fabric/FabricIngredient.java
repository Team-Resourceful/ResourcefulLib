package com.teamresourceful.resourcefullib.common.recipe.ingredient.fabric;

import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

public record FabricIngredient<T extends CodecIngredient<T>>(T ingredient) implements CustomIngredient {

    @Override
    public boolean test(ItemStack stack) {
        return ingredient.test(stack);
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        return ingredient.getStacks();
    }

    @Override
    public boolean requiresTesting() {
        return ingredient.isComplex();
    }

    @Override
    public CustomIngredientSerializer<?> getSerializer() {
        return FabricIngredientHelper.get(ingredient.serializer().id());
    }

    /**
     * @deprecated Use {@link #ingredient()} instead.
     */
    @Deprecated(forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.19.4")
    public T getIngredient() {
        return ingredient;
    }
}
