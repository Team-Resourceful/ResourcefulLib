package com.teamresourceful.resourcefullib.common.recipe;

import com.mojang.serialization.Codec;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public class CodecRecipeSerializer<R extends Recipe<?>> implements RecipeSerializer<R> {

    private final RecipeType<R> recipeType;
    private final Codec<R> jsonCodec;
    private final ByteCodec<R> networkCodec;

    public CodecRecipeSerializer(RecipeType<R> recipeType, Codec<R> jsonCodec, ByteCodec<R> networkCodec) {
        this.recipeType = recipeType;
        this.jsonCodec = jsonCodec;
        this.networkCodec = networkCodec;
    }

    @Override
    public @NotNull Codec<R> codec() {
        return jsonCodec;
    }

    @Override
    public @NotNull R fromNetwork(FriendlyByteBuf buffer) {
        return this.networkCodec.decode(buffer);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull R recipe) {
        this.networkCodec.encode(recipe, buffer);
    }

    public RecipeType<R> type() {
        return recipeType;
    }
}
