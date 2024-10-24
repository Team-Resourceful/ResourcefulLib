package com.teamresourceful.resourcefullib.common.recipe.ingredient.fabric;

import com.mojang.serialization.MapCodec;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredient;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredientSerializer;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public class FabricIngredientSerializer<T extends CodecIngredient<T>> implements CustomIngredientSerializer<FabricIngredient<T>> {

    private final CodecIngredientSerializer<T> serializer;
    private final MapCodec<FabricIngredient<T>> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, FabricIngredient<T>> packetCodec;

    public FabricIngredientSerializer(CodecIngredientSerializer<T> serializer) {
        this.serializer = serializer;
        this.codec = serializer.codec().xmap(FabricIngredient::new, FabricIngredient::ingredient);
        this.packetCodec = serializer.network().map(FabricIngredient::new, FabricIngredient::ingredient);
    }

    @Override
    public ResourceLocation getIdentifier() {
        return serializer.id();
    }

    @Override
    public MapCodec<FabricIngredient<T>> getCodec() {
        return this.codec;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, FabricIngredient<T>> getPacketCodec() {
        return this.packetCodec;
    }

}
