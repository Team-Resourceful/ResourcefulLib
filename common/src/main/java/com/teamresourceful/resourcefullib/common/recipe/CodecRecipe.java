package com.teamresourceful.resourcefullib.common.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public interface CodecRecipe<C extends Container> extends Recipe<C> {

    @Override
    default boolean isSpecial() {
        return true;
    }

    @Override
    @NotNull
    default ItemStack assemble(@NotNull C container, @NotNull RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    @Override
    @NotNull
    default ItemStack getResultItem(@NotNull RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    @Override
    default boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    @NotNull
    default RecipeSerializer<?> getSerializer() {
        return serializer();
    }

    CodecRecipeSerializer<? extends CodecRecipe<C>> serializer();
}

