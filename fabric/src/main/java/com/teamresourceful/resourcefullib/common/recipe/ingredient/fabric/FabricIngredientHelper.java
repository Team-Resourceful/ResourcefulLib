package com.teamresourceful.resourcefullib.common.recipe.ingredient.fabric;

import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredient;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredientSerializer;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public final class FabricIngredientHelper {

    private static final Map<Identifier, FabricIngredientSerializer<?>> SERIALIZERS = new HashMap<>();

    public static FabricIngredientSerializer<?> get(Identifier id) {
        return SERIALIZERS.get(id);
    }

    public static <T extends CodecIngredient<T>> void register(CodecIngredientSerializer<T> serializer) {
        FabricIngredientSerializer<T> fabricSerializer = new FabricIngredientSerializer<>(serializer);
        SERIALIZERS.put(serializer.id(), fabricSerializer);
        CustomIngredientSerializer.register(fabricSerializer);
    }
}
