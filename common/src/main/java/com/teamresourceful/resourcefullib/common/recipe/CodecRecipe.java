package com.teamresourceful.resourcefullib.common.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public interface CodecRecipe<I extends RecipeInput> extends Recipe<I> {

    @Override
    default boolean isSpecial() {
        return true;
    }

    @Override
    @NotNull ItemStack assemble(I input, HolderLookup.Provider provider);

    @Override
    @NotNull
    CodecRecipeSerializer<? extends Recipe<I>> getSerializer();
}

