package com.teamresourceful.resourcefullib.common.recipe.ingredient.neoforge;

import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredient;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

public record NeoForgeIngredient<T extends CodecIngredient<T>>(T ingredient) implements ICustomIngredient {

    @Override
    public boolean test(@NotNull ItemStack arg) {
        return this.ingredient.test(arg);
    }

    @Override
    public @NotNull Stream<Holder<Item>> items() {
        return this.ingredient.getItems().stream();
    }

    @Override
    public boolean isSimple() {
        return !this.ingredient.isComplex();
    }

    @Override
    public @NotNull IngredientType<?> getType() {
        return Objects.requireNonNull(NeoForgeRegistries.INGREDIENT_TYPES.getValue(this.ingredient.serializer().id()));
    }
}
