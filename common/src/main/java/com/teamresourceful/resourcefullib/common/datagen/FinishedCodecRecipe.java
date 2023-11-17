package com.teamresourceful.resourcefullib.common.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.resourcefullib.common.lib.Constants;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipeSerializer;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record FinishedCodecRecipe<C extends Container, T extends CodecRecipe<C>>(
        ResourceLocation id, T recipe, @Nullable AdvancementHolder holder
) implements FinishedRecipe {

    @Override
    public void serializeRecipeData(@NotNull JsonObject json) {
        type().codec()
            .encodeStart(JsonOps.INSTANCE, recipe)
            .resultOrPartial(Constants.LOGGER::error)
            .map(JsonElement::getAsJsonObject)
            .ifPresent(data -> data.entrySet().forEach(entry -> json.add(entry.getKey(), entry.getValue())));
    }

    @Override
    public @NotNull CodecRecipeSerializer<T> type() {
        return recipe.serializer();
    }

    @Nullable
    @Override
    public AdvancementHolder advancement() {
        return holder;
    }
}
