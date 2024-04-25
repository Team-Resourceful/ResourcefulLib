package com.teamresourceful.resourcefullib.common.recipe.ingredient;

import com.mojang.serialization.MapCodec;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.StreamCodecByteCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record CodecIngredientSerializer<T extends CodecIngredient<T>>(
        ResourceLocation id, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> network
) {

    public CodecIngredientSerializer(ResourceLocation id, MapCodec<T> codec, ByteCodec<T> network) {
        this(id, codec, StreamCodecByteCodec.toRegistry(network));
    }

}
