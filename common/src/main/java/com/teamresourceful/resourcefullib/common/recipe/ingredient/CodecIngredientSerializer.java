package com.teamresourceful.resourcefullib.common.recipe.ingredient;

import com.mojang.serialization.MapCodec;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.StreamCodecByteCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public record CodecIngredientSerializer<T extends CodecIngredient<T>>(
        Identifier id, MapCodec<T> codec, StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull T> network
) {

    public CodecIngredientSerializer(Identifier id, MapCodec<T> codec, ByteCodec<T> network) {
        this(id, codec, StreamCodecByteCodec.toRegistry(network));
    }

}
