package com.teamresourceful.resourcefullib.common.recipe.ingredient.forge;

import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredient;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredientSerializer;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class ForgeIngredientHelper {

    private static final Map<ResourceLocation, ForgeIngredientSerializer<?>> SERIALIZERS = new HashMap<>();

    public static ForgeIngredientSerializer<?> get(ResourceLocation id) {
        return SERIALIZERS.get(id);
    }

    public static <T extends CodecIngredient<T>> void register(CodecIngredientSerializer<T> serializer) {
        SERIALIZERS.put(serializer.id(), new ForgeIngredientSerializer<>(serializer));
    }
}
