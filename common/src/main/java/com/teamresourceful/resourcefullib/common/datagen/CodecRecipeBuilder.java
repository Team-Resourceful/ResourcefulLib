package com.teamresourceful.resourcefullib.common.datagen;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class CodecRecipeBuilder implements RecipeBuilder {

    @Nullable
    protected String group;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    @Override
    public @NotNull RecipeBuilder unlockedBy(String string, Criterion<?> criterion) {
        this.criteria.put(string, criterion);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String string) {
        this.group = string;
        return this;
    }
}
