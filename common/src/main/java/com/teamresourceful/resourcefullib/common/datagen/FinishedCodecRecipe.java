package com.teamresourceful.resourcefullib.common.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.resourcefullib.common.lib.Constants;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record FinishedCodecRecipe<C extends Container, T extends CodecRecipe<C>>(T recipe, @Nullable ResourceLocation advancement, @Nullable Advancement.Builder builder) implements FinishedRecipe {

    @Override
    public void serializeRecipeData(@NotNull JsonObject json) {
        recipe.jsonCodec(recipe.id())
            .encodeStart(JsonOps.INSTANCE, recipe)
            .resultOrPartial(Constants.LOGGER::error)
            .map(JsonElement::getAsJsonObject)
            .ifPresent(data -> data.entrySet().forEach(entry -> json.add(entry.getKey(), entry.getValue())));
    }

    @NotNull
    @Override
    public ResourceLocation getId() {
        return recipe.getId();
    }

    @NotNull
    @Override
    public RecipeSerializer<?> getType() {
        return recipe.getSerializer();
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return builder == null ? null : builder.serializeToJson();
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return advancement;
    }
}
