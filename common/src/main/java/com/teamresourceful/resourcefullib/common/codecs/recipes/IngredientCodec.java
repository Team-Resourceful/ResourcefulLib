package com.teamresourceful.resourcefullib.common.codecs.recipes;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class IngredientCodec {

    public static final Codec<Ingredient> CODEC = Codec.PASSTHROUGH.comapFlatMap(IngredientCodec::decodeIngredient, IngredientCodec::encodeIngredient);
    public static final Codec<Ingredient> NETWORK_CODEC = ItemStackCodec.NETWORK_CODEC.listOf().xmap(IngredientCodec::decodeIngredient, IngredientCodec::encodeIngredientToNetwork);

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

    private static Ingredient decodeIngredient(List<ItemStack> stacks) {
        return Ingredient.of(stacks.stream());
    }

    private static List<ItemStack> encodeIngredientToNetwork(Ingredient ingredient) {
        return Arrays.stream(ingredient.getItems()).collect(Collectors.toList());
    }
}
