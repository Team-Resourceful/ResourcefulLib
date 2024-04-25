package com.teamresourceful.resourcefullib.common.recipe.ingredient;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public interface CodecIngredient<T extends CodecIngredient<T>> extends Predicate<@Nullable ItemStack> {

    @Override
    boolean test(@Nullable ItemStack stack);

    List<ItemStack> getStacks();

    /**
     * Determines if this ingredient is complex.
     * i.e. it can change based on the context.
     * Note: This does not mean if it changes based on its item.
     * @return true if it for example changes depending on nbt of the itemstack.
     */
    default boolean isComplex() {
        return true;
    }

    CodecIngredientSerializer<T> serializer();
}
