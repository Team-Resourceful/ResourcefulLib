package com.teamresourceful.resourcefullib.common.recipe.ingredient;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class FabricIngredientSerializer<T extends CodecIngredient<T>> implements CustomIngredientSerializer<@NotNull FabricIngredient<T>> {

    private final CodecIngredientSerializer<T> serializer;
    private final MapCodec<FabricIngredient<T>> codec;
    private final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull FabricIngredient<T>> streamCodec;

    public FabricIngredientSerializer(CodecIngredientSerializer<T> serializer) {
        this.serializer = serializer;
        this.codec = serializer.codec().xmap(FabricIngredient::new, FabricIngredient::ingredient);
        this.streamCodec = serializer.network().map(FabricIngredient::new, FabricIngredient::ingredient);
    }

    @Override
    public @NotNull Identifier getIdentifier() {
        return serializer.id();
    }

    @Override
    public @NotNull MapCodec<FabricIngredient<T>> getCodec() {
        return this.codec;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, @NotNull FabricIngredient<T>> getStreamCodec() {
        return this.streamCodec;
    }

}
