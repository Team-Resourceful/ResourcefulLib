package com.teamresourceful.resourcefullib.common.codecs.recipes;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.world.item.crafting.Ingredient;

public final class IngredientCodec {

    public static final Codec<Ingredient> CODEC = Codec.PASSTHROUGH.comapFlatMap(IngredientCodec::decodeIngredient, IngredientCodec::encodeIngredient);

    private IngredientCodec() throws UtilityClassException {
        throw new UtilityClassException();
    }

    private static DataResult<Ingredient> decodeIngredient(Dynamic<?> dynamic) {
        Object object = dynamic.convert(JsonOps.INSTANCE).getValue();
        if (object instanceof JsonElement jsonElement) {
            return DataResult.success(Ingredient.fromJson(jsonElement));
        }
        return DataResult.error("Value was not an instance of JsonElement");
    }

    private static Dynamic<JsonElement> encodeIngredient(Ingredient ingredient) {
        return new Dynamic<>(JsonOps.INSTANCE, ingredient.toJson()).convert(JsonOps.COMPRESSED);
    }
}
