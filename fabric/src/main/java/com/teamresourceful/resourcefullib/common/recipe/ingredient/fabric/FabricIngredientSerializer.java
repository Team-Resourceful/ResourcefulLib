package com.teamresourceful.resourcefullib.common.recipe.ingredient.fabric;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.resourcefullib.common.lib.Constants;
import com.teamresourceful.resourcefullib.common.networking.PacketHelper;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredient;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredientSerializer;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class FabricIngredientSerializer<T extends CodecIngredient<T>> implements CustomIngredientSerializer<FabricIngredient<T>> {

    private final CodecIngredientSerializer<T> serializer;

    public FabricIngredientSerializer(CodecIngredientSerializer<T> serializer) {
        this.serializer = serializer;
    }

    @Override
    public ResourceLocation getIdentifier() {
        return serializer.id();
    }

    @Override
    public FabricIngredient<T> read(JsonObject json) {
        T ingredient = serializer.codec().parse(JsonOps.INSTANCE, json).getOrThrow(false, Constants.LOGGER::error);
        return new FabricIngredient<>(ingredient);
    }

    @Override
    public void write(JsonObject json, FabricIngredient<T> ingredient) {
        JsonElement element = serializer.codec().encodeStart(JsonOps.INSTANCE, ingredient.getIngredient())
                .getOrThrow(false, Constants.LOGGER::error);
        if (!element.isJsonObject()) {
            Constants.LOGGER.error("Could not parse {}", ingredient.getIngredient());
            Constants.LOGGER.error("Element is not a JsonObject");
            return;
        }
        for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
            json.add(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public FabricIngredient<T> read(FriendlyByteBuf buf) {
        T ingredient = PacketHelper.readWithYabn(buf, serializer.network(), true)
                .getOrThrow(false, Constants.LOGGER::error);
        return new FabricIngredient<>(ingredient);
    }

    @Override
    public void write(FriendlyByteBuf buf, FabricIngredient<T> ingredient) {
        PacketHelper.writeWithYabn(buf, serializer.network(), ingredient.getIngredient(), true)
                .getOrThrow(false, s -> Constants.LOGGER.error("Could not parse {}", ingredient.getIngredient()));
    }
}
