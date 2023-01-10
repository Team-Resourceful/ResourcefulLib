package com.teamresourceful.resourcefullib.common.codecs.recipes;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public final class IngredientCodec {

    public static final Codec<Ingredient> CODEC = Codec.PASSTHROUGH.comapFlatMap(IngredientCodec::decodeIngredient, IngredientCodec::encodeIngredient);
    public static final Codec<Ingredient> NETWORK_CODEC = Codec.BYTE.listOf().flatXmap(IngredientCodec::decodeIngredientFromNetwork, IngredientCodec::encodeIngredientToNetwork);

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

    private static DataResult<Ingredient> decodeIngredientFromNetwork(List<Byte> data) {
        try {
            byte[] array = new byte[data.size()];
            for (int i = 0; i < data.size(); i++) {
                array[i] = data.get(i);
            }
            ByteBuf buffer = Unpooled.wrappedBuffer(array);
            return DataResult.success(Ingredient.fromNetwork(new FriendlyByteBuf(buffer)));
        }catch (Exception e){
            return DataResult.error("Failed to decode ingredient from network: " + e.getMessage());
        }
    }

    private static DataResult<List<Byte>> encodeIngredientToNetwork(Ingredient ingredient) {
        try {
            ByteBuf buffer = Unpooled.buffer();
            ingredient.toNetwork(new FriendlyByteBuf(buffer));
            byte[] array = buffer.array();
            List<Byte> bytes = new ArrayList<>(array.length);
            for (byte b : array) {
                bytes.add(b);
            }
            return DataResult.success(bytes);
        }catch (Exception e){
            return DataResult.error("Failed to encode ingredient to network: " + e.getMessage());
        }
    }

}
