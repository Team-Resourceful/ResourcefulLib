package com.teamresourceful.resourcefullib.common.recipe;

import com.mojang.serialization.MapCodec;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.StreamCodecByteCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public class CodecRecipeSerializer<R extends Recipe<?>> implements RecipeSerializer<R> {

    private final RecipeType<R> recipeType;
    private final MapCodec<R> jsonCodec;
    private final StreamCodec<RegistryFriendlyByteBuf, R> networkCodec;

    public CodecRecipeSerializer(RecipeType<R> recipeType, MapCodec<R> jsonCodec, ByteCodec<R> networkCodec) {
        this(recipeType, jsonCodec, StreamCodecByteCodec.toRegistry(networkCodec));
    }

    public CodecRecipeSerializer(RecipeType<R> recipeType, MapCodec<R> jsonCodec, StreamCodec<RegistryFriendlyByteBuf, R> networkCodec) {
        this.recipeType = recipeType;
        this.jsonCodec = jsonCodec;
        this.networkCodec = networkCodec;
    }

    @Override
    public @NotNull MapCodec<R> codec() {
        return jsonCodec;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
        return networkCodec;
    }

    public RecipeType<R> type() {
        return recipeType;
    }
}
