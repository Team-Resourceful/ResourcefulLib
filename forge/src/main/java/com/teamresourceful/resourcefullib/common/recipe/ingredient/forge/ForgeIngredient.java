package com.teamresourceful.resourcefullib.common.recipe.ingredient.forge;

import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredient;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class ForgeIngredient<T extends CodecIngredient<T>> extends Ingredient {

    private final T ingredient;
    @Nullable private ItemStack[] stacks;
    @Nullable private IntList stackingIds;

    protected ForgeIngredient(T ingredient) {
        super(Stream.of());
        this.ingredient = ingredient;
    }

    public T getIngredient() {
        return this.ingredient;
    }

    @Override
    public ItemStack @NotNull [] getItems() {
        if (this.stacks == null) this.stacks = this.ingredient.getStacksAsArray();
        return this.stacks;
    }

    @Override
    public boolean isSimple() {
        return !this.ingredient.isComplex();
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        return this.ingredient.test(stack);
    }

    @Override
    public boolean isEmpty() {
        return this.ingredient.isEmpty();
    }

    @Override
    public @NotNull IntList getStackingIds() {
        if (this.stackingIds == null) {
            this.stackingIds = Ingredient.of(getItems()).getStackingIds();
        }
        return this.stackingIds;
    }

}
