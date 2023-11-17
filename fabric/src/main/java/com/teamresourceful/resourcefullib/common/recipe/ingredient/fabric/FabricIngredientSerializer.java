package com.teamresourceful.resourcefullib.common.recipe.ingredient.fabric;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredient;
import com.teamresourceful.resourcefullib.common.recipe.ingredient.CodecIngredientSerializer;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class FabricIngredientSerializer<T extends CodecIngredient<T>> implements CustomIngredientSerializer<FabricIngredient<T>> {

    private final CodecIngredientSerializer<T> serializer;
    private final Codec<FabricIngredient<T>> codec;

    public FabricIngredientSerializer(CodecIngredientSerializer<T> serializer) {
        this.serializer = serializer;
        this.codec = serializer.codec().xmap(FabricIngredient::new, FabricIngredient::ingredient);
    }

    @Override
    public ResourceLocation getIdentifier() {
        return serializer.id();
    }

    @Override
    public Codec<FabricIngredient<T>> getCodec(boolean allowEmpty) {
        return this.codec;
    }

    @Override
    public FabricIngredient<T> read(FriendlyByteBuf buf) {
        return new FabricIngredient<>(this.serializer.network().decode(buf));
    }

    @Override
    public void write(FriendlyByteBuf buf, FabricIngredient<T> ingredient) {
        this.serializer.network().encode(ingredient.ingredient(), buf);
    }


}
