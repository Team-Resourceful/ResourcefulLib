package com.teamresourceful.resourcefullib.common.recipe.ingredient.forge;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.resourcefullib.common.lib.Constants;
import com.teamresourceful.resourcefullib.common.networking.PacketHelper;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredient;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredientSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.NotNull;

public class ForgeIngredientSerializer<T extends CodecIngredient<T>> implements IIngredientSerializer<ForgeIngredient<T>> {

    private final CodecIngredientSerializer<T> serializer;

    public ForgeIngredientSerializer(CodecIngredientSerializer<T> serializer) {
        this.serializer = serializer;
    }

    public ResourceLocation id() {
        return serializer.id();
    }

    @Override
    public @NotNull ForgeIngredient<T> parse(@NotNull JsonObject json) {
        T ingredient = serializer.codec().parse(JsonOps.INSTANCE, json).getOrThrow(false, Constants.LOGGER::error);
        return new ForgeIngredient<>(ingredient);
    }

    @Override
    public @NotNull ForgeIngredient<T> parse(@NotNull FriendlyByteBuf buf) {
        T ingredient = PacketHelper.readWithYabn(buf, serializer.network(), true)
                .getOrThrow(false, Constants.LOGGER::error);
        return new ForgeIngredient<>(ingredient);
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf, @NotNull ForgeIngredient<T> ingredient) {
        PacketHelper.writeWithYabn(buf, serializer.network(), ingredient.getIngredient(), true)
                .getOrThrow(false, s -> Constants.LOGGER.error("Could not parse {}", ingredient.getIngredient()));
    }
}
